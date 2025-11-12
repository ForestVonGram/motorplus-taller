import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

// DTO entregado por backend (/api/servicios/search) via Jackson
// Campos en camelCase según getters Java: idServicio, nombre, descripcion, idTipo
interface ServicioDTO {
  idServicio: number;
  nombre: string;
  descripcion?: string | null;
  idTipo: number;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

const ServiciosList = () => {
  const navigate = useNavigate();
  const [servicios, setServicios] = useState<ServicioDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/servicios/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: ServicioDTO[] = await res.json();
        if (alive) setServicios(data);
      } catch (e) {
        if (alive) console.error('Error cargando servicios', e);
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
    { key: 'idServicio', header: 'ID' },
    { key: 'nombre', header: 'Nombre del Servicio' },
    { key: 'descripcion', header: 'Descripción' },
    { key: 'idTipo', header: 'Tipo de Servicio (ID)' },
  ];

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Servicios</h1>
        <button className="btn-primary" onClick={() => navigate('/servicios/crear')}>+ Nuevo Servicio</button>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por ID, nombre, descripción o tipo..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando servicios...</div>
      ) : servicios.length === 0 ? (
        <div className="empty-state">No se encontraron servicios.</div>
      ) : (
        <DataTable
          columns={columns}
          data={servicios}
          onAction={(s) => navigate(`/servicios/${s.idServicio}`)}
          actionLabel="Ver Detalle"
        />
      )}
    </div>
  );
};

export default ServiciosList;
