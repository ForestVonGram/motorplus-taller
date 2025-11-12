import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import './VehiculosList.css';

// Logos de marcas - puedes reemplazar con rutas reales
const BRAND_LOGOS: Record<string, string> = {
  'Ford': 'https://cdn.worldvectorlogo.com/logos/ford-6.svg',
  'Audi': 'https://cdn.worldvectorlogo.com/logos/audi-2009.svg',
  'Mercedes': 'https://cdn.worldvectorlogo.com/logos/mercedes-benz-9.svg',
  'Land Rover': 'https://cdn.worldvectorlogo.com/logos/land-rover-1.svg',
  'Toyota': 'https://cdn.worldvectorlogo.com/logos/toyota-1.svg',
  'BMW': 'https://cdn.worldvectorlogo.com/logos/bmw.svg',
  'Volkswagen': 'https://cdn.worldvectorlogo.com/logos/volkswagen.svg',
  'Fiat': 'https://cdn.worldvectorlogo.com/logos/fiat-1.svg',
};

// DTO que retorna el backend en /api/vehiculos/search
interface VehiculoDTO {
  placa: string;
  marca: string;
  anio: number;
  idCliente: number;
  clienteNombre?: string;
  clienteApellido?: string;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

const VehiculosList = () => {
  const [vehiculos, setVehiculos] = useState<VehiculoDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');

  useEffect(() => {
    let alive = true;
    setLoading(true);

    const controller = new AbortController();
    const timeout = setTimeout(async () => {
      try {
        const url = `${API_BASE}/api/vehiculos/search${query ? `?q=${encodeURIComponent(query)}` : ''}`;
        const res = await fetch(url, { signal: controller.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: VehiculoDTO[] = await res.json();
        if (alive) setVehiculos(data);
      } catch (e) {
        if (alive) console.error('Error cargando vehículos', e);
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
    { key: 'placa', header: 'Placa' },
    {
      key: 'marca',
      header: 'Marca',
      render: (_, row: VehiculoDTO) => (
        <div className="vehicle-info">
          <img
            src={BRAND_LOGOS[row.marca] || '/placeholder-logo.png'}
            alt={row.marca}
            className="vehicle-logo"
          />
          <span className="vehicle-name">{row.marca}</span>
        </div>
      ),
    },
    { key: 'anio', header: 'Año' },
    {
      key: 'idCliente',
      header: 'ID Cliente',
      render: (_, row: VehiculoDTO) => `CLI-${String(row.idCliente).padStart(3, '0')}`,
    },
    {
      key: 'clienteNombre',
      header: 'Cliente',
      render: (_, row: VehiculoDTO) => {
        const nombre = [row.clienteNombre, row.clienteApellido].filter(Boolean).join(' ');
        return nombre || '-';
      }
    },
  ];

  const handleAction = (vehiculo: VehiculoDTO) => {
    console.log('Ver detalle de:', vehiculo);
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Vehículos</h1>
        <button className="btn-primary">+ Nuevo Vehículo</button>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por placa, marca, cliente o año..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading">Cargando vehículos...</div>
          /*Cuando no hay vehículos*/
      ) : vehiculos.length === 0 ? (
        <div className="empty-state">No se encontraron vehículos.</div>
      ) : (
        <DataTable
          columns={columns}
          data={vehiculos}
          onAction={handleAction}
          actionLabel="En taller"
        />
      )}
    </div>
  );
};

export default VehiculosList;
