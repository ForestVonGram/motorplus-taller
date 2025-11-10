import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Orden {
  id: number;
  numeroOrden: string;
  vehiculo: string;
  cliente: string;
  fechaIngreso: string;
  estado: 'Pendiente' | 'En Proceso' | 'Completada' | 'Facturada';
  mecanicos: string[];
}

const OrdenesList = () => {
  const [ordenes, setOrdenes] = useState<Orden[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Orden[] = [
      {
        id: 1,
        numeroOrden: 'ORD-2024-001',
        vehiculo: 'Ford Explorer 2020',
        cliente: 'Juan Carlos López',
        fechaIngreso: '15-01-2024',
        estado: 'En Proceso',
        mecanicos: ['Carlos Pérez', 'Ana García']
      },
      {
        id: 2,
        numeroOrden: 'ORD-2024-002',
        vehiculo: 'Audi A4 2010',
        cliente: 'Magdalena Arancibia',
        fechaIngreso: '16-01-2024',
        estado: 'Completada',
        mecanicos: ['Luis Martínez']
      },
    ];

    setTimeout(() => {
      setOrdenes(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const getEstadoBadge = (estado: string) => {
    const badges: Record<string, string> = {
      'Pendiente': 'badge-warning',
      'En Proceso': 'badge-primary',
      'Completada': 'badge-success',
      'Facturada': 'badge-success'
    };
    return `badge ${badges[estado] || 'badge-primary'}`;
  };

  const columns: Column[] = [
    {
      key: 'numeroOrden',
      header: 'N° Orden',
    },
    {
      key: 'vehiculo',
      header: 'Vehículo',
    },
    {
      key: 'cliente',
      header: 'Cliente',
    },
    {
      key: 'mecanicos',
      header: 'Mecánicos',
      render: (_, row) => row.mecanicos.join(', ')
    },
    {
      key: 'estado',
      header: 'Estado',
      render: (_, row) => (
        <span className={getEstadoBadge(row.estado)}>
          {row.estado}
        </span>
      )
    },
    {
      key: 'fecha',
      header: 'Fecha',
      render: (_, row) => row.fechaIngreso
    }
  ];

  const handleAction = (orden: Orden) => {
    console.log('Ver detalle de:', orden);
  };

  if (loading) {
    return <div className="loading">Cargando órdenes...</div>;
  }

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Órdenes de Trabajo</h1>
        <button className="btn-primary">+ Nueva Orden</button>
      </div>
      
      <DataTable 
        columns={columns} 
        data={ordenes}
        onAction={handleAction}
        actionLabel="Ver Detalle"
      />
    </div>
  );
};

export default OrdenesList;