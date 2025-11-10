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
  id: number;
  marca: string;
  modelo: string;
  anio: number;
  matricula: string;
  cliente: string;
  ordenesCount: number;
  ultimaFecha: string;
}

const VehiculosList = () => {
  const [vehiculos, setVehiculos] = useState<Vehiculo[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulación de datos - reemplazar con llamada a API
    const mockData: Vehiculo[] = [
      {
        id: 1,
        marca: 'Ford',
        modelo: 'Explorer 2020',
        anio: 2020,
        matricula: 'JKPQ11',
        cliente: 'Juan Carlos López',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      },
      {
        id: 2,
        marca: 'Audi',
        modelo: 'A4 2010',
        anio: 2010,
        matricula: 'DJHG018',
        cliente: 'Magdalena Arancibia',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      },
      {
        id: 3,
        marca: 'Mercedes',
        modelo: 'Benz A250 2012',
        anio: 2012,
        matricula: 'QTRJ12',
        cliente: 'Joaquín Montecinos',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      },
      {
        id: 4,
        marca: 'Land Rover',
        modelo: 'Evoque 2009',
        anio: 2009,
        matricula: 'HGLQ90',
        cliente: 'Felipe Toloza',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      },
      {
        id: 5,
        marca: 'Audi',
        modelo: 'Q5 2010',
        anio: 2010,
        matricula: 'LQRQ12',
        cliente: 'María Paz Jiménez',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      },
      {
        id: 6,
        marca: 'Toyota',
        modelo: '4Runner 2008',
        anio: 2008,
        matricula: 'PQKL22',
        cliente: 'Héctor Reyes',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      },
      {
        id: 7,
        marca: 'BMW',
        modelo: 'M4 2020',
        anio: 2020,
        matricula: 'KQRT12',
        cliente: 'Patricia Sánchez',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      },
      {
        id: 8,
        marca: 'Volkswagen',
        modelo: 'Golf GTI',
        anio: 2019,
        matricula: 'YT9103',
        cliente: 'Marcial Fernández',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      },
      {
        id: 9,
        marca: 'Fiat',
        modelo: 'Fiorino 2021',
        anio: 2021,
        matricula: 'KPLQ91',
        cliente: 'Karl Stevenson',
        ordenesCount: 1,
        ultimaFecha: '09-01-2022'
      }
    ];

    setTimeout(() => {
      setVehiculos(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const columns: Column[] = [
    {
      key: 'vehiculo',
      header: 'Vehículo',
      render: (_, row) => (
        <div className="vehicle-info">
          <img 
            src={BRAND_LOGOS[row.marca] || '/placeholder-logo.png'} 
            alt={row.marca}
            className="vehicle-logo"
          />
          <span className="vehicle-name">
            {row.marca} {row.modelo}
          </span>
        </div>
      )
    },
    {
      key: 'cliente',
      header: 'Cliente',
    },
    {
      key: 'matricula',
      header: 'Matrícula',
    },
    {
      key: 'ordenes',
      header: 'Órdenes',
      render: (_, row) => `${row.ordenesCount} orden(es)`
    },
    {
      key: 'fecha',
      header: 'Fecha',
      render: (_, row) => row.ultimaFecha
    }
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
