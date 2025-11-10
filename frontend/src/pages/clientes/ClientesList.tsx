import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Cliente {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  vehiculosCount: number;
  fechaRegistro: string;
}

const ClientesList = () => {
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Cliente[] = [
      {
        id: 1,
        nombre: 'Juan Carlos',
        apellido: 'López',
        email: 'jc.lopez@email.com',
        telefono: '+56 9 1234 5678',
        vehiculosCount: 1,
        fechaRegistro: '15-03-2021'
      },
      {
        id: 2,
        nombre: 'Magdalena',
        apellido: 'Arancibia',
        email: 'm.arancibia@email.com',
        telefono: '+56 9 8765 4321',
        vehiculosCount: 1,
        fechaRegistro: '22-05-2021'
      },
      // Agregar más datos mock aquí...
    ];

    setTimeout(() => {
      setClientes(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const columns: Column[] = [
    {
      key: 'nombre',
      header: 'Nombre Completo',
      render: (_, row) => `${row.nombre} ${row.apellido}`
    },
    {
      key: 'email',
      header: 'Email',
    },
    {
      key: 'telefono',
      header: 'Teléfono',
    },
    {
      key: 'vehiculos',
      header: 'Vehículos',
      render: (_, row) => (
        <span className="badge badge-primary">
          {row.vehiculosCount} vehículo(s)
        </span>
      )
    },
    {
      key: 'fecha',
      header: 'Fecha Registro',
      render: (_, row) => row.fechaRegistro
    }
  ];

  const handleAction = (cliente: Cliente) => {
    console.log('Ver detalle de:', cliente);
  };

  if (loading) {
    return <div className="loading">Cargando clientes...</div>;
  }

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Clientes</h1>
        <button className="btn-primary">+ Nuevo Cliente</button>
      </div>
      
      <DataTable 
        columns={columns} 
        data={clientes}
        onAction={handleAction}
        actionLabel="Ver Detalle"
      />
    </div>
  );
};

export default ClientesList;
