import { useEffect, useState } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Repuesto {
  id_repuesto: number;
  nombre: string;
  costo_unitario: string;
  stock_disponible: number;
}

const currency = new Intl.NumberFormat('es-CL', {
  style: 'currency',
  currency: 'CLP',
  maximumFractionDigits: 0,
});

const RepuestosList = () => {
  const [repuestos, setRepuestos] = useState<Repuesto[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Repuesto[] = [
      { id_repuesto: 1, nombre: 'Filtro de Aceite', costo_unitario: '5990.00', stock_disponible: 32 },
      { id_repuesto: 2, nombre: 'Pastillas de Freno', costo_unitario: '24990.00', stock_disponible: 8 },
      { id_repuesto: 3, nombre: 'Batería 60Ah', costo_unitario: '89990.00', stock_disponible: 15 },
      { id_repuesto: 4, nombre: 'Correa de Distribución', costo_unitario: '45990.00', stock_disponible: 5 },
      { id_repuesto: 5, nombre: 'Aceite 5W-30 (4L)', costo_unitario: '21990.00', stock_disponible: 54 },
      { id_repuesto: 6, nombre: 'Amortiguadores Delanteros', costo_unitario: '125000.00', stock_disponible: 12 },
      { id_repuesto: 7, nombre: 'Bomba de Agua', costo_unitario: '35500.00', stock_disponible: 20 },
      { id_repuesto: 8, nombre: 'Filtro de Aire', costo_unitario: '8990.00', stock_disponible: 45 },
    ];

    setTimeout(() => {
      setRepuestos(mockData);
      setLoading(false);
    }, 400);
  }, []);

  const columns: Column[] = [
    { 
      key: 'id_repuesto', 
      header: 'ID',
      render: (_, row) => `REP-${String(row.id_repuesto).padStart(3, '0')}`
    },
    { key: 'nombre', header: 'Nombre' },
    { 
      key: 'costo_unitario', 
      header: 'Costo Unitario',
      render: (_, row) => currency.format(parseFloat(row.costo_unitario))
    },
    { 
      key: 'stock_disponible', 
      header: 'Stock Disponible',
      render: (_, row) => (
        <span className={`badge ${row.stock_disponible < 10 ? 'badge-danger' : row.stock_disponible < 20 ? 'badge-warning' : 'badge-success'}`}>
          {row.stock_disponible} unidades
        </span>
      )
    },
  ];

  if (loading) return <div className="loading">Cargando repuestos...</div>;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Repuestos</h1>
        <button className="btn-primary">+ Nuevo Repuesto</button>
      </div>

      <DataTable
        columns={columns}
        data={repuestos}
        onAction={(r) => console.log('Ver detalle de:', r)}
        actionLabel="Ver Detalle"
      />
    </div>
  );
};

export default RepuestosList;
