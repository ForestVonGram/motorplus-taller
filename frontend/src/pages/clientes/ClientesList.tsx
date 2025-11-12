import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

// DTO que retorna el backend en /api/clientes/search
interface ClienteDTO {
  idCliente: number;
  nombre: string;
  apellido: string;
  vehiculos: string; // placas separadas por coma
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

const ClientesList = () => {
  const navigate = useNavigate();
  const [clientes, setClientes] = useState<ClienteDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/clientes/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: ClienteDTO[] = await res.json();
        if (alive) setClientes(data);
      } catch (err) {
        if (alive) {
          console.error('Error al cargar clientes desde API:', err);
          setClientes([]);
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
      key: 'idCliente',
      header: 'ID',
      render: (_, row: ClienteDTO) => `CLI-${String(row.idCliente).padStart(3, '0')}`
    },
    { key: 'nombre', header: 'Nombre' },
    { key: 'apellido', header: 'Apellido' },
    {
      key: 'vehiculos',
      header: 'Vehículos',
      render: (_, row: ClienteDTO) => row.vehiculos || '-',
    },
  ];

  const handleAction = (cliente: ClienteDTO) => {
    console.log('Ver detalle de:', cliente);
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Clientes</h1>
        <button className="btn-primary" onClick={() => navigate('/clientes/crear')}>+ Nuevo Cliente</button>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nombre, apellido, ID o vehículo..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando clientes...</div>
      ) : clientes.length === 0 ? (
        <div className="empty-state">No se encontraron clientes.</div>
      ) : (
        <DataTable
          columns={columns}
          data={clientes}
          onAction={handleAction}
          actionLabel="Ver Detalle"
        />
      )}
    </div>
  );
};

export default ClientesList;
