import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Mecanico {
  id_mecanico: number;
  nombre: string;
  id_supervisor: number | null;
}

const MecanicosList = () => {
  const [mecanicos, setMecanicos] = useState<Mecanico[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Mecanico[] = [
      {
        id_mecanico: 1,
        nombre: 'Carlos Mendoza',
        id_supervisor: null
      },
      {
        id_mecanico: 2,
        nombre: 'María González',
        id_supervisor: 1
      },
      {
        id_mecanico: 3,
        nombre: 'Roberto Silva',
        id_supervisor: null
      },
      {
        id_mecanico: 4,
        nombre: 'Ana Torres',
        id_supervisor: 1
      },
      {
        id_mecanico: 5,
        nombre: 'Luis Ramírez',
        id_supervisor: 3
      },
      {
        id_mecanico: 6,
        nombre: 'Pedro García',
        id_supervisor: 3
      },
      {
        id_mecanico: 7,
        nombre: 'Sofía Morales',
        id_supervisor: null
      }
    ];

    setTimeout(() => {
      setMecanicos(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const getSupervisorNombre = (idSupervisor: number | null) => {
    if (idSupervisor === null) {
      return 'Sin supervisor';
    }
    const supervisor = mecanicos.find(m => m.id_mecanico === idSupervisor);
    return supervisor ? supervisor.nombre : `Supervisor #${idSupervisor}`;
  };

  const columns: Column[] = [
    {
      key: 'id_mecanico',
      header: 'ID',
      render: (_, row) => `MEC-${String(row.id_mecanico).padStart(3, '0')}`
    },
    {
      key: 'nombre',
      header: 'Nombre',
    },
    {
      key: 'id_supervisor',
      header: 'Supervisor',
      render: (_, row) => getSupervisorNombre(row.id_supervisor)
    },
    {
      key: 'rol',
      header: 'Rol',
      render: (_, row) => (
        <span className={`badge ${row.id_supervisor === null ? 'badge-primary' : 'badge-secondary'}`}>
          {row.id_supervisor === null ? 'Supervisor' : 'Mecánico'}
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