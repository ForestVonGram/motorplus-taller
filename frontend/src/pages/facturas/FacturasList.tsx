import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

// DTO que devuelve el backend (Jackson) para Factura
interface FacturaDTO {
  idFactura: number;
  costoManoObra: number;
  total: number;
  impuesto: number;
  fechaEmision: string; // ISO date
  estadoPago: string;
  idOrden: number;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

const FacturasList = () => {
  const navigate = useNavigate();
  const [facturas, setFacturas] = useState<FacturaDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/facturas/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: FacturaDTO[] = await res.json();
        if (alive) setFacturas(data);
      } catch (err) {
        if (alive) {
          console.error('Error al cargar facturas desde API:', err);
          setFacturas([]);
        }
      } finally {
        if (alive) setLoading(false);
      }
    }, 250); // debounce

    return () => {
      alive = false;
      controller.abort();
      clearTimeout(timeout);
    };
  }, [query]);

  const getEstadoBadgeClass = (estado: string) => {
    switch (estado) {
      case 'Pagado':
        return 'badge-success';
      case 'Pendiente':
        return 'badge-warning';
      case 'Vencido':
        return 'badge-danger';
      default:
        return 'badge-primary';
    }
  };

  const formatMonto = (monto: number) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP'
    }).format(monto);
  };

  const columns: Column[] = [
    {
      key: 'idFactura',
      header: 'N° Factura',
      render: (_, row: FacturaDTO) => `FAC-${String(row.idFactura).padStart(4, '0')}`
    },
    {
      key: 'idOrden',
      header: 'N° Orden',
      render: (_, row: FacturaDTO) => `ORD-${String(row.idOrden).padStart(4, '0')}`
    },
    {
      key: 'fechaEmision',
      header: 'Fecha Emisión',
      render: (_, row: FacturaDTO) => new Date(row.fechaEmision).toLocaleDateString('es-CL')
    },
    {
      key: 'costoManoObra',
      header: 'Mano de Obra',
      render: (_, row: FacturaDTO) => formatMonto(row.costoManoObra)
    },
    {
      key: 'impuesto',
      header: 'Impuesto',
      render: (_, row: FacturaDTO) => formatMonto(row.impuesto)
    },
    {
      key: 'total',
      header: 'Total',
      render: (_, row: FacturaDTO) => formatMonto(row.total)
    },
    {
      key: 'estadoPago',
      header: 'Estado',
      render: (_, row: FacturaDTO) => (
        <span className={`badge ${getEstadoBadgeClass(row.estadoPago)}`}>
          {row.estadoPago}
        </span>
      )
    },
  ];

  const handleAction = (factura: FacturaDTO) => {
    navigate(`/facturas/${factura.idFactura}`);
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Facturas</h1>
        <button className="btn-primary" onClick={() => navigate('/facturas/crear')}>+ Nueva Factura</button>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por N° factura, N° orden, estado o fecha..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando facturas...</div>
      ) : facturas.length === 0 ? (
        <div className="empty-state">No se encontraron facturas.</div>
      ) : (
        <DataTable 
          columns={columns} 
          data={facturas}
          onAction={handleAction}
          actionLabel="Ver Detalle"
        />
      )}
    </div>
  );
};

export default FacturasList;
