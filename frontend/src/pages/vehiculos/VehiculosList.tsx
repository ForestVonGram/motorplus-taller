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

interface Vehiculo {
  placa: string;
  marca: string;
  anio: number;
  id_cliente: number;
}

const VehiculosList = () => {
  const [vehiculos, setVehiculos] = useState<Vehiculo[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulación de datos - reemplazar con llamada a API
    const mockData: Vehiculo[] = [
      {
        placa: 'ABC123',
        marca: 'Ford',
        anio: 2020,
        id_cliente: 1
      },
      {
        placa: 'XYZ789',
        marca: 'Audi',
        anio: 2010,
        id_cliente: 2
      },
      {
        placa: 'DEF456',
        marca: 'Mercedes',
        anio: 2012,
        id_cliente: 3
      },
      {
        placa: 'GHI789',
        marca: 'Land Rover',
        anio: 2009,
        id_cliente: 4
      },
      {
        placa: 'JKL012',
        marca: 'Audi',
        anio: 2010,
        id_cliente: 5
      },
      {
        placa: 'MNO345',
        marca: 'Toyota',
        anio: 2008,
        id_cliente: 1
      },
      {
        placa: 'PQR678',
        marca: 'BMW',
        anio: 2020,
        id_cliente: 2
      },
      {
        placa: 'STU901',
        marca: 'Volkswagen',
        anio: 2019,
        id_cliente: 3
      },
      {
        placa: 'VWX234',
        marca: 'Fiat',
        anio: 2021,
        id_cliente: 4
      }
    ];

    setTimeout(() => {
      setVehiculos(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const columns: Column[] = [
    {
      key: 'placa',
      header: 'Placa',
    },
    {
      key: 'marca',
      header: 'Marca',
      render: (_, row) => (
        <div className="vehicle-info">
          <img 
            src={BRAND_LOGOS[row.marca] || '/placeholder-logo.png'} 
            alt={row.marca}
            className="vehicle-logo"
          />
          <span className="vehicle-name">
            {row.marca}
          </span>
        </div>
      )
    },
    {
      key: 'anio',
      header: 'Año',
    },
    {
      key: 'id_cliente',
      header: 'ID Cliente',
      render: (_, row) => `CLI-${String(row.id_cliente).padStart(3, '0')}`
    },
  ];

  const handleAction = (vehiculo: Vehiculo) => {
    console.log('Ver detalle de:', vehiculo);
    // Aquí puedes navegar a la página de detalle
  };

  if (loading) {
    return <div className="loading">Cargando vehículos...</div>;
  }

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Vehículos</h1>
        <button className="btn-primary">+ Nuevo Vehículo</button>
      </div>
      
      <DataTable 
        columns={columns} 
        data={vehiculos}
        onAction={handleAction}
        actionLabel="En taller"
      />
    </div>
  );
};

export default VehiculosList;
