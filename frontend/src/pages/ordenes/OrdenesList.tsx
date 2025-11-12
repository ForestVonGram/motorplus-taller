import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

type ISODate = string; // 'YYYY-MM-DD'

// Estructura devuelta por el backend (Jackson) para OrdenTrabajo
interface OrdenTrabajoDTO {
  idOrden: number;
  fechaIngreso: ISODate;
  diagnosticoInicial: string;
  fechaFinalizacion: ISODate | null;
  placa: string;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

const OrdenesList = () => {
  const [ordenes, setOrdenes] = useState<OrdenTrabajoDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/ordenes/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: OrdenTrabajoDTO[] = await res.json();
        if (alive) setOrdenes(data);
      } catch (err) {
        if (alive) {
          console.error('Error al cargar órdenes desde API:', err);
          setOrdenes([]);
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

  const getEstadoBadge = (fechaFinalizacion: ISODate | null) => {
    return fechaFinalizacion === null ? 'badge badge-warning' : 'badge badge-success';
  };

  const getEstadoTexto = (fechaFinalizacion: ISODate | null) => {
    return fechaFinalizacion === null ? 'En Proceso' : 'Finalizada';
  };

  const columns: Column[] = [
    {
      key: 'idOrden',
      header: 'N° Orden',
      render: (_, row: OrdenTrabajoDTO) => `ORD-${String(row.idOrden).padStart(4, '0')}`
    },
    { key: 'placa', header: 'Placa' },
    {
      key: 'fechaIngreso',
      header: 'Fecha Ingreso',
      render: (_, row: OrdenTrabajoDTO) => new Date(row.fechaIngreso).toLocaleDateString('es-CL')
    },
    {
      key: 'diagnosticoInicial',
      header: 'Diagnóstico',
      render: (_, row: OrdenTrabajoDTO) => (
        <div style={{ maxWidth: '300px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {row.diagnosticoInicial}
        </div>
      )
    },
    {
      key: 'fechaFinalizacion',
      header: 'Fecha Finalización',
      render: (_, row: OrdenTrabajoDTO) => row.fechaFinalizacion ? new Date(row.fechaFinalizacion).toLocaleDateString('es-CL') : '-'
    },
    {
      key: 'estado',
      header: 'Estado',
      render: (_, row: OrdenTrabajoDTO) => (
        <span className={getEstadoBadge(row.fechaFinalizacion)}>
          {getEstadoTexto(row.fechaFinalizacion)}
        </span>
      )
    },
  ];

  const handleAction = (orden: OrdenTrabajoDTO) => {
    console.log('Ver detalle de:', orden);
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Órdenes</h1>
        <button className="btn-primary">+ Nueva Orden</button>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por placa, diagnóstico, fecha o N° de orden..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando órdenes...</div>
      ) : ordenes.length === 0 ? (
        <div className="empty-state">No se encontraron órdenes.</div>
      ) : (
        <DataTable
          columns={columns}
          data={ordenes}
          onAction={handleAction}
          actionLabel="Ver Detalle"
        />
      )}
    </div>
  );
};

export default OrdenesList;
