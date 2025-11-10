import { useEffect, useMemo, useState } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';
import '../../components/DataTable.css';

interface ReporteDef {
  id: string;
  nombre: string;
  categoria: 'Simple' | 'Intermedio' | 'Complejo';
  descripcion: string;
}

const categoryBadge = (categoria: ReporteDef['categoria']) => {
  switch (categoria) {
    case 'Simple':
      return 'badge-success';
    case 'Intermedio':
      return 'badge-warning';
    case 'Complejo':
      return 'badge-danger';
    default:
      return 'badge-primary';
  }
};

const ReportesList = () => {
  const [reportes, setReportes] = useState<ReporteDef[]>([]);
  const [loading, setLoading] = useState(true);
  const [filtro, setFiltro] = useState<ReporteDef['categoria'] | 'Todos'>('Todos');

  useEffect(() => {
    const defs: ReporteDef[] = [
      { id: 'vehiculo-historial', nombre: 'Historial de vehículo', categoria: 'Simple', descripcion: 'Órdenes, servicios y facturas asociadas a un vehículo.' },
      { id: 'facturas-por-cliente', nombre: 'Facturas por cliente', categoria: 'Simple', descripcion: 'Listado de facturas emitidas para un cliente.' },
      { id: 'rendimiento-mecanicos', nombre: 'Rendimiento de mecánicos', categoria: 'Intermedio', descripcion: 'Tiempos, órdenes completadas y eficiencia por mecánico.' },
      { id: 'inventario-por-proveedor', nombre: 'Inventario por proveedor', categoria: 'Intermedio', descripcion: 'Repuestos disponibles agrupados por proveedor.' },
      { id: 'analisis-rentabilidad', nombre: 'Análisis de rentabilidad', categoria: 'Complejo', descripcion: 'Margen por servicio, repuesto y orden a lo largo del tiempo.' },
      { id: 'trazabilidad-ordenes', nombre: 'Trazabilidad de órdenes', categoria: 'Complejo', descripcion: 'Detalle end-to-end de órdenes y responsables.' },
    ];

    setTimeout(() => {
      setReportes(defs);
      setLoading(false);
    }, 350);
  }, []);

  const filtered = useMemo(
    () => (filtro === 'Todos' ? reportes : reportes.filter(r => r.categoria === filtro)),
    [reportes, filtro]
  );

  const handlePreview = (r: ReporteDef) => {
    console.log('Vista previa de reporte:', r.id);
  };

  const handleExport = (r: ReporteDef) => {
    console.log('Exportar a PDF reporte:', r.id);
  };

  const columns: Column[] = [
    { key: 'nombre', header: 'Reporte' },
    { 
      key: 'categoria', 
      header: 'Categoría',
      render: (value) => <span className={`badge ${categoryBadge(value)}`}>{value}</span>
    },
    { key: 'descripcion', header: 'Descripción' },
    {
      key: 'acciones',
      header: '',
      render: (_v, row: ReporteDef) => (
        <div style={{ display: 'flex', gap: 8 }}>
          <button className="action-button" onClick={() => handlePreview(row)}>Ver</button>
          <button className="action-button" onClick={() => handleExport(row)}>Exportar PDF</button>
        </div>
      )
    }
  ];

  if (loading) return <div className="loading">Cargando reportes...</div>;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Reportes</h1>
        <div style={{ display: 'flex', gap: 12 }}>
          <select 
            className="btn-primary"
            style={{ backgroundColor: '#f8f9fa', color: '#212529' }}
            value={filtro}
            onChange={(e) => setFiltro(e.target.value as any)}
          >
            <option value="Todos">Todas las categorías</option>
            <option value="Simple">Simples</option>
            <option value="Intermedio">Intermedios</option>
            <option value="Complejo">Complejos</option>
          </select>
          <button className="btn-primary" onClick={() => setFiltro('Todos')}>Limpiar</button>
        </div>
      </div>

      <DataTable columns={columns} data={filtered} />
    </div>
  );
};

export default ReportesList;
