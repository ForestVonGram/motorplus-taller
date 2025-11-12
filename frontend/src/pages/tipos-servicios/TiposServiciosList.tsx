import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface TipoServicioDTO {
  idTipo: number;
  nombreTipo: string;
  descripcionTipo?: string | null;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

export default function TiposServiciosList() {
  const navigate = useNavigate();
  const [tipos, setTipos] = useState<TipoServicioDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/tipos-servicio/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: TipoServicioDTO[] = await res.json();
        if (alive) setTipos(data);
      } catch (e) {
        if (alive) console.error('Error cargando tipos de servicio', e);
      } finally {
        if (alive) setLoading(false);
      }
    }, 250);

    return () => {
      alive = false;
      controller.abort();
      clearTimeout(timeout);
    };
  }, [query]);

  const columns: Column[] = [
    { key: 'idTipo', header: 'ID Tipo' },
    { key: 'nombreTipo', header: 'Nombre del Tipo' },
    { key: 'descripcionTipo', header: 'Descripción' },
  ];

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Tipos de servicio</h1>
        <button className="btn-primary" onClick={() => navigate('/tipos-servicio/crear')}>+ Nuevo Tipo</button>
      </div>

      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por ID, nombre o descripción..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando tipos...</div>
      ) : tipos.length === 0 ? (
        <div className="empty-state">No se encontraron tipos de servicio.</div>
      ) : (
        <DataTable columns={columns} data={tipos} />
      )}
    </div>
  );
}
