import { useEffect, useState } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Proveedor {
  id: number;
  nombre: string;
  rut: string; // RUT/NIF
  contacto: string; // nombre/contacto principal o teléfono/email resumido
  repuestosSuministrados: number;
}

const ProveedoresList = () => {
  const [proveedores, setProveedores] = useState<Proveedor[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Proveedor[] = [
      { id: 1, nombre: 'ACME Parts', rut: '76.123.456-7', contacto: 'Carlos Pérez · +56 9 1111 2222', repuestosSuministrados: 42 },
      { id: 2, nombre: 'FrenosPlus', rut: 'B12345678', contacto: 'María Gómez · +56 2 2345 6789', repuestosSuministrados: 18 },
      { id: 3, nombre: 'EnergíaMax', rut: '96.987.654-3', contacto: 'Soporte: soporte@energiamax.com', repuestosSuministrados: 25 },
      { id: 4, nombre: 'Motores S.A.', rut: '80.345.678-9', contacto: 'Juan Silva · +56 9 3333 4444', repuestosSuministrados: 12 },
      { id: 5, nombre: 'LubriOil', rut: 'A76543210', contacto: 'Atención: ventas@lubrioil.cl', repuestosSuministrados: 30 },
    ];

    setTimeout(() => {
      setProveedores(mockData);
      setLoading(false);
    }, 450);
  }, []);

  const columns: Column[] = [
    { key: 'nombre', header: 'Nombre' },
    { key: 'rut', header: 'RUT/NIF' },
    { key: 'contacto', header: 'Contacto' },
    {
      key: 'repuestos',
      header: 'Repuestos Suministrados',
      render: (_, row: Proveedor) => (
        <span className="badge badge-primary">{row.repuestosSuministrados} repuesto(s)</span>
      ),
    },
  ];

  if (loading) return <div className="loading">Cargando proveedores...</div>;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Proveedores</h1>
        <button className="btn-primary">+ Nuevo Proveedor</button>
      </div>

      <DataTable
        columns={columns}
        data={proveedores}
        onAction={(p) => console.log('Ver detalle de:', p)}
        actionLabel="Ver Detalle"
      />
    </div>
  );
};

export default ProveedoresList;
