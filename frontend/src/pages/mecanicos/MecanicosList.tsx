    import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Mecanico {
  id: number;
  nombre: string;
  apellido: string;
  especialidad: string;
  experiencia: number; // años de experiencia
  estado: 'Activo' | 'Inactivo' | 'En Servicio';
  telefono: string;
  fechaContratacion: string;
}

const MecanicosList = () => {
  const [mecanicos, setMecanicos] = useState<Mecanico[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Mecanico[] = [
      {
        id: 1,
        nombre: 'Carlos',
        apellido: 'Mendoza',
        especialidad: 'Motor',
        experiencia: 8,
        estado: 'Activo',
        telefono: '+56 9 1234 5678',
        fechaContratacion: '15-01-2020'
      },
      {
        id: 2,
        nombre: 'María',
        apellido: 'González',
        especialidad: 'Transmisión',
        experiencia: 5,
        estado: 'En Servicio',
        telefono: '+56 9 8765 4321',
        fechaContratacion: '03-06-2021'
      },
      {
        id: 3,
        nombre: 'Roberto',
        apellido: 'Silva',
        especialidad: 'Electricidad',
        experiencia: 12,
        estado: 'Activo',
        telefono: '+56 9 5555 1234',
        fechaContratacion: '22-08-2018'
      },
      {
        id: 4,
        nombre: 'Ana',
        apellido: 'Torres',
        especialidad: 'Frenos',
        experiencia: 3,
        estado: 'Activo',
        telefono: '+56 9 9876 5432',
        fechaContratacion: '10-11-2022'
      },
      {
        id: 5,
        nombre: 'Luis',
        apellido: 'Ramírez',
        especialidad: 'Suspensión',
        experiencia: 15,
        estado: 'Inactivo',
        telefono: '+56 9 1111 2222',
        fechaContratacion: '05-03-2016'
      }
    ];

    setTimeout(() => {
      setMecanicos(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const getEstadoBadgeClass = (estado: string) => {
    switch (estado) {
      case 'Activo':
        return 'badge-success';
      case 'En Servicio':
        return 'badge-warning';
      case 'Inactivo':
        return 'badge-danger';
      default:
        return 'badge-primary';
    }
  };

  const columns: Column[] = [
    {
      key: 'nombre',
      header: 'Nombre Completo',
      render: (_, row) => `${row.nombre} ${row.apellido}`
    },
    {
      key: 'especialidad',
      header: 'Especialidad',
    },
    {
      key: 'experiencia',
      header: 'Experiencia',
      render: (_, row) => `${row.experiencia} año${row.experiencia !== 1 ? 's' : ''}`
    },
    {
      key: 'estado',
      header: 'Estado',
      render: (_, row) => (
        <span className={`badge ${getEstadoBadgeClass(row.estado)}`}>
          {row.estado}
        </span>
      )
    },
  ];

  const handleAction = (mecanico: Mecanico) => {
    console.log('Ver detalle de:', mecanico);
  };

  if (loading) {
    return <div className="loading">Cargando mecánicos...</div>;
  }

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Mecánicos</h1>
        <button className="btn-primary">+ Nuevo Mecánico</button>
      </div>
      
      <DataTable 
        columns={columns} 
        data={mecanicos}
        onAction={handleAction}
        actionLabel="Ver Detalle"
      />
    </div>
  );
};

export default MecanicosList;