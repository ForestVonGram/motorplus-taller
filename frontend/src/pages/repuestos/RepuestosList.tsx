import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

// DTO desde backend (Jackson) para Repuesto
interface RepuestoDTO {
  idRepuesto: number;
  nombre: string;
  costoUnitario: number;
  stockDisponible: number;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

const currency = new Intl.NumberFormat('es-CL', {
  style: 'currency',
  currency: 'CLP',
  maximumFractionDigits: 0,
});

const RepuestosList = () => {
  const navigate = useNavigate();
  const [repuestos, setRepuestos] = useState<RepuestoDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/repuestos/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: RepuestoDTO[] = await res.json();
        if (alive) setRepuestos(data);
      } catch (err) {
        if (alive) {
          console.error('Error al cargar repuestos desde API:', err);
          setRepuestos([]);
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

  const columns: Column[] = [
    {
      key: 'idRepuesto',
      header: 'ID',
      render: (_, row: RepuestoDTO) => `REP-${String(row.idRepuesto).padStart(3, '0')}`
    },
    { key: 'nombre', header: 'Nombre' },
    {
      key: 'costoUnitario',
      header: 'Costo Unitario',
      render: (_, row: RepuestoDTO) => currency.format(row.costoUnitario)
    },
    {
      key: 'stockDisponible',
      header: 'Stock Disponible',
      render: (_, row: RepuestoDTO) => (
        <span className={`badge ${row.stockDisponible < 10 ? 'badge-danger' : row.stockDisponible < 20 ? 'badge-warning' : 'badge-success'}`}>
          {row.stockDisponible} unidades
        </span>
      )
    },
  ];

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Repuestos</h1>
        <button className="btn-primary" onClick={() => navigate('/repuestos/crear')}>+ Nuevo Repuesto</button>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nombre o ID..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando repuestos...</div>
      ) : repuestos.length === 0 ? (
        <div className="empty-state">No se encontraron repuestos.</div>
      ) : (
        <DataTable
          columns={columns}
          data={repuestos}
          onAction={(r) => console.log('Ver detalle de:', r)}
          actionLabel="Ver Detalle"
        />
      )}
    </div>
  );
};

export default RepuestosList;
