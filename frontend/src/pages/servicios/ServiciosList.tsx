import { useEffect, useState } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Servicio {
  id: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  precioBase: number; // en CLP
}

const currency = new Intl.NumberFormat('es-CL', {
  style: 'currency',
  currency: 'CLP',
  maximumFractionDigits: 0,
});

const ServiciosList = () => {
  const [servicios, setServicios] = useState<Servicio[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const mockData: Servicio[] = [
      { id: 1, codigo: 'SRV-0001', nombre: 'Cambio de aceite', descripcion: 'Incluye filtro y revisión general', precioBase: 19990 },
      { id: 2, codigo: 'SRV-0002', nombre: 'Alineación y balanceo', descripcion: 'Alineación de ejes y balanceo de las 4 ruedas', precioBase: 24990 },
      { id: 3, codigo: 'SRV-0003', nombre: 'Revisión de frenos', descripcion: 'Inspección, limpieza y ajuste de frenos', precioBase: 29990 },
      { id: 4, codigo: 'SRV-0004', nombre: 'Scanner y diagnóstico', descripcion: 'Lectura de códigos OBD-II y reporte de fallas', precioBase: 19990 },
      { id: 5, codigo: 'SRV-0005', nombre: 'Mantención 10.000 km', descripcion: 'Checklist completo según especificación del fabricante', precioBase: 74990 },
    ];

    setTimeout(() => {
      setServicios(mockData);
      setLoading(false);
    }, 400);
  }, []);

  const columns: Column[] = [
    { key: 'codigo', header: 'Código' },
    { key: 'nombre', header: 'Nombre' },
    { key: 'descripcion', header: 'Descripción' },
    { key: 'precioBase', header: 'Precio Base', render: (value) => currency.format(value) },
  ];

  if (loading) return <div className="loading">Cargando servicios...</div>;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Servicios</h1>
        <button className="btn-primary">+ Nuevo Servicio</button>
      </div>

      <DataTable
        columns={columns}
        data={servicios}
        onAction={(s) => console.log('Ver detalle de:', s)}
        actionLabel="Ver Detalle"
      />
    </div>
  );
};

export default ServiciosList;
