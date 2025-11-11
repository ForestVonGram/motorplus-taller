import { useState, useEffect } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Factura {
  id_factura: number;
  costo_mano_obra: number;
  total: number;
  impuesto: number;
  fecha_emision: string;
  estado_pago: string;
  id_orden: number;
}

const FacturasList = () => {
  const [facturas, setFacturas] = useState<Factura[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Factura[] = [
      {
        id_factura: 1,
        costo_mano_obra: 50000,
        total: 150000,
        impuesto: 28500,
        fecha_emision: '2024-01-10',
        estado_pago: 'Pagado',
        id_orden: 101
      },
      {
        id_factura: 2,
        costo_mano_obra: 80000,
        total: 250000,
        impuesto: 47500,
        fecha_emision: '2024-01-15',
        estado_pago: 'Pendiente',
        id_orden: 102
      },
      {
        id_factura: 3,
        costo_mano_obra: 60000,
        total: 180000,
        impuesto: 34200,
        fecha_emision: '2024-01-20',
        estado_pago: 'Pagado',
        id_orden: 103
      },
      {
        id_factura: 4,
        costo_mano_obra: 120000,
        total: 320000,
        impuesto: 60800,
        fecha_emision: '2024-01-05',
        estado_pago: 'Vencido',
        id_orden: 104
      },
      {
        id_factura: 5,
        costo_mano_obra: 65000,
        total: 195000,
        impuesto: 37050,
        fecha_emision: '2024-01-25',
        estado_pago: 'Pendiente',
        id_orden: 105
      }
    ];

    setTimeout(() => {
      setFacturas(mockData);
      setLoading(false);
    }, 500);
  }, []);

  const getEstadoBadgeClass = (estado: string) => {
    switch (estado) {
      case 'Pagado':
        return 'badge-success';
      case 'Pendiente':
        return 'badge-warning';
      case 'Vencido':
        return 'badge-danger';
      default:
        return 'badge-primary';
    }
  };

  const formatMonto = (monto: number) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP'
    }).format(monto);
  };

  const columns: Column[] = [
    {
      key: 'id_factura',
      header: 'N° Factura',
      render: (_, row) => `FAC-${String(row.id_factura).padStart(4, '0')}`
    },
    {
      key: 'id_orden',
      header: 'N° Orden',
      render: (_, row) => `ORD-${String(row.id_orden).padStart(4, '0')}`
    },
    {
      key: 'fecha_emision',
      header: 'Fecha Emisión',
      render: (_, row) => new Date(row.fecha_emision).toLocaleDateString('es-CL')
    },
    {
      key: 'costo_mano_obra',
      header: 'Mano de Obra',
      render: (_, row) => formatMonto(row.costo_mano_obra)
    },
    {
      key: 'impuesto',
      header: 'Impuesto',
      render: (_, row) => formatMonto(row.impuesto)
    },
    {
      key: 'total',
      header: 'Total',
      render: (_, row) => formatMonto(row.total)
    },
    {
      key: 'estado_pago',
      header: 'Estado',
      render: (_, row) => (
        <span className={`badge ${getEstadoBadgeClass(row.estado_pago)}`}>
          {row.estado_pago}
        </span>
      )
    },
  ];

  const handleAction = (factura: Factura) => {
    console.log('Ver detalle de:', factura);
  };

  if (loading) {
    return <div className="loading">Cargando facturas...</div>;
  }

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Facturas</h1>
        <button className="btn-primary">+ Nueva Factura</button>
      </div>
      
      <DataTable 
        columns={columns} 
        data={facturas}
        onAction={handleAction}
        actionLabel="Ver Detalle"
      />
    </div>
  );
};

export default FacturasList;