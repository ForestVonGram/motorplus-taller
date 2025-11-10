import { useEffect, useState } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Repuesto {
  id: number;
  codigo: string;
  nombre: string;
  proveedor: string;
  stock: number;
  precio: number; // en CLP
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
      { id: 1, codigo: 'RPS-0001', nombre: 'Filtro de Aceite', proveedor: 'ACME Parts', stock: 32, precio: 5990 },
      { id: 2, codigo: 'RPS-0002', nombre: 'Pastillas de Freno', proveedor: 'FrenosPlus', stock: 8, precio: 24990 },
      { id: 3, codigo: 'RPS-0003', nombre: 'Batería 60Ah', proveedor: 'EnergíaMax', stock: 15, precio: 89990 },
      { id: 4, codigo: 'RPS-0004', nombre: 'Correa de Distribución', proveedor: 'Motores S.A.', stock: 5, precio: 45990 },
      { id: 5, codigo: 'RPS-0005', nombre: 'Aceite 5W-30 (4L)', proveedor: 'LubriOil', stock: 54, precio: 21990 },
    ];

    setTimeout(() => {
      setRepuestos(mockData);
      setLoading(false);
    }, 400);
  }, []);

  const columns: Column[] = [
    { key: 'codigo', header: 'Código' },
    { key: 'nombre', header: 'Nombre' },
    { key: 'proveedor', header: 'Proveedor' },
    { key: 'stock', header: 'Stock' },
    { key: 'precio', header: 'Precio', render: (value) => currency.format(value) },
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
