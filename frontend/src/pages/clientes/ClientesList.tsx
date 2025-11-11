import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Cliente {
  id_cliente: number;
  nombre: string;
  apellido: string;
  contrasenia: string;
}

const ClientesList = () => {
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Cliente[] = [
      {
        id_cliente: 1,
        nombre: 'Juan Carlos',
        apellido: 'López',
        contrasenia: '********'
      },
      {
        id_cliente: 2,
        nombre: 'Magdalena',
        apellido: 'Arancibia',
        contrasenia: '********'
      },
      {
        id_cliente: 3,
        nombre: 'Roberto',
        apellido: 'Fernández',
        contrasenia: '********'
      },
      {
        id_cliente: 4,
        nombre: 'Carolina',
        apellido: 'Muñoz',
        contrasenia: '********'
      },
      {
        id_cliente: 5,
        nombre: 'Diego',
        apellido: 'Vargas',
        contrasenia: '********'
      },
    ];

    setTimeout(() => {
      setClientes(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const columns: Column[] = [
    {
      key: 'id_cliente',
      header: 'ID',
      render: (_, row) => `CLI-${String(row.id_cliente).padStart(3, '0')}`
    },
    {
      key: 'nombre',
      header: 'Nombre',
    },
    {
      key: 'apellido',
      header: 'Apellido',
    },
    {
      key: 'contrasenia',
      header: 'Contraseña',
      render: () => '********'
    },
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
