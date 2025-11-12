import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface ProveedorDTO {
  idProveedor: number;
  nombre: string;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

const ProveedoresList = () => {
  const navigate = useNavigate();
  const [proveedores, setProveedores] = useState<ProveedorDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/proveedores/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: ProveedorDTO[] = await res.json();
        if (alive) setProveedores(data);
      } catch (err) {
        if (alive) {
          console.error('Error al cargar proveedores desde API:', err);
          setProveedores([]);
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
      key: 'idProveedor',
      header: 'ID',
      render: (_, row: ProveedorDTO) => `PROV-${String(row.idProveedor).padStart(3, '0')}`
    },
    { key: 'nombre', header: 'Nombre del Proveedor' },
  ];

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Proveedores</h1>
        <button className="btn-primary" onClick={() => navigate('/proveedores/crear')}>+ Nuevo Proveedor</button>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nombre..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando proveedores...</div>
      ) : proveedores.length === 0 ? (
        <div className="empty-state">No se encontraron proveedores.</div>
      ) : (
        <DataTable
          columns={columns}
          data={proveedores}
          onAction={(p) => navigate(`/proveedores/${p.idProveedor}`)}
          actionLabel="Ver Detalle"
        />
      )}
    </div>
  );
};

export default ProveedoresList;
