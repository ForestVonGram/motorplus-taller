import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Orden {
  id_orden: number;
  fecha_ingreso: string;
  diagnostico_inicial: string;
  fecha_finalizacion: string | null;
  placa: string;
}

const OrdenesList = () => {
  const [ordenes, setOrdenes] = useState<Orden[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Orden[] = [
      {
        id_orden: 1,
        fecha_ingreso: '2024-01-15',
        diagnostico_inicial: 'Revisión general del motor y cambio de aceite',
        fecha_finalizacion: null,
        placa: 'ABC123'
      },
      {
        id_orden: 2,
        fecha_ingreso: '2024-01-16',
        diagnostico_inicial: 'Reparación de transmisión',
        fecha_finalizacion: '2024-01-20',
        placa: 'XYZ789'
      },
      {
        id_orden: 3,
        fecha_ingreso: '2024-01-18',
        diagnostico_inicial: 'Cambio de frenos y revisión de suspensión',
        fecha_finalizacion: null,
        placa: 'DEF456'
      },
      {
        id_orden: 4,
        fecha_ingreso: '2024-01-20',
        diagnostico_inicial: 'Reparación sistema eléctrico',
        fecha_finalizacion: '2024-01-25',
        placa: 'GHI789'
      },
      {
        id_orden: 5,
        fecha_ingreso: '2024-01-22',
        diagnostico_inicial: 'Mantenimiento preventivo completo',
        fecha_finalizacion: null,
        placa: 'JKL012'
      },
    ];

    setTimeout(() => {
      setOrdenes(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const getEstadoBadge = (fechaFinalizacion: string | null) => {
    if (fechaFinalizacion === null) {
      return 'badge badge-warning';
    }
    return 'badge badge-success';
  };

  const getEstadoTexto = (fechaFinalizacion: string | null) => {
    return fechaFinalizacion === null ? 'En Proceso' : 'Finalizada';
  };

  const columns: Column[] = [
    {
      key: 'id_orden',
      header: 'N° Orden',
      render: (_, row) => `ORD-${String(row.id_orden).padStart(4, '0')}`
    },
    {
      key: 'placa',
      header: 'Placa',
    },
    {
      key: 'fecha_ingreso',
      header: 'Fecha Ingreso',
      render: (_, row) => new Date(row.fecha_ingreso).toLocaleDateString('es-CL')
    },
    {
      key: 'diagnostico_inicial',
      header: 'Diagnóstico',
      render: (_, row) => (
        <div style={{ maxWidth: '300px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {row.diagnostico_inicial}
        </div>
      )
    },
    {
      key: 'fecha_finalizacion',
      header: 'Fecha Finalización',
      render: (_, row) => row.fecha_finalizacion ? new Date(row.fecha_finalizacion).toLocaleDateString('es-CL') : '-'
    },
    {
      key: 'estado',
      header: 'Estado',
      render: (_, row) => (
        <span className={getEstadoBadge(row.fecha_finalizacion)}>
          {getEstadoTexto(row.fecha_finalizacion)}
        </span>
      )
    },
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