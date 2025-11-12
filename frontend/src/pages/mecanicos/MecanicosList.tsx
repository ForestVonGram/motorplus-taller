import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface MecanicoDTO {
  idMecanico: number;
  nombre: string;
  idSupervisor: number | null;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

const MecanicosList = () => {
  const navigate = useNavigate();
  const [mecanicos, setMecanicos] = useState<MecanicoDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/mecanicos/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: MecanicoDTO[] = await res.json();
        if (alive) setMecanicos(data);
      } catch (err) {
        if (alive) {
          console.error('Error al cargar mecánicos desde API:', err);
          setMecanicos([]);
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

  const getSupervisorNombre = (idSupervisor: number | null) => {
    if (idSupervisor === null) {
      return 'Sin supervisor';
    }
    const supervisor = mecanicos.find(m => m.idMecanico === idSupervisor);
    return supervisor ? supervisor.nombre : `Supervisor #${idSupervisor}`;
  };

  const columns: Column[] = [
    {
      key: 'idMecanico',
      header: 'ID',
      render: (_, row: MecanicoDTO) => `MEC-${String(row.idMecanico).padStart(3, '0')}`
    },
    { key: 'nombre', header: 'Nombre' },
    {
      key: 'idSupervisor',
      header: 'Supervisor',
      render: (_, row: MecanicoDTO) => getSupervisorNombre(row.idSupervisor)
    },
    {
      key: 'rol',
      header: 'Rol',
      render: (_, row: MecanicoDTO) => (
        <span className={`badge ${row.idSupervisor === null ? 'badge-primary' : 'badge-secondary'}`}>
          {row.idSupervisor === null ? 'Supervisor' : 'Mecánico'}
        </span>
      )
    },
  ];

  const handleAction = (mecanico: MecanicoDTO) => {
    console.log('Ver detalle de:', mecanico);
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Mecánicos</h1>
        <button className="btn-primary" onClick={() => navigate('/mecanicos/crear')}>+ Nuevo Mecánico</button>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nombre o ID de supervisor..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando mecánicos...</div>
      ) : mecanicos.length === 0 ? (
        <div className="empty-state">No se encontraron mecánicos.</div>
      ) : (
        <DataTable
          columns={columns}
          data={mecanicos}
          onAction={handleAction}
          actionLabel="Ver Detalle"
        />
      )}
    </div>
  );
};

export default MecanicosList;
