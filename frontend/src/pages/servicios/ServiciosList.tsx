import { useEffect, useState } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Servicio {
    id_servicio: number;
    nombre: string;
    descripcion: string;
    id_tipo: number;
}

const ServiciosList = () => {
    const [servicios, setServicios] = useState<Servicio[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Luego puedes reemplazar este mock por un fetch a tu API real (por ejemplo: fetch('/api/servicios'))
        const mockData: Servicio[] = [
            { id_servicio: 1, nombre: 'Cambio de aceite', descripcion: 'Incluye filtro y revisión general', id_tipo: 1 },
            { id_servicio: 2, nombre: 'Alineación y balanceo', descripcion: 'Alineación de ejes y balanceo de las 4 ruedas', id_tipo: 2 },
            { id_servicio: 3, nombre: 'Revisión de frenos', descripcion: 'Inspección, limpieza y ajuste de frenos', id_tipo: 3 },
            { id_servicio: 4, nombre: 'Scanner y diagnóstico', descripcion: 'Lectura de códigos OBD-II y reporte de fallas', id_tipo: 4 },
            { id_servicio: 5, nombre: 'Mantención 10.000 km', descripcion: 'Checklist completo según especificación del fabricante', id_tipo: 5 },
        ];

        setTimeout(() => {
            setServicios(mockData);
            setLoading(false);
        }, 400);
    }, []);

    const columns: Column[] = [
        { key: 'id_servicio', header: 'ID' },
        { key: 'nombre', header: 'Nombre del Servicio' },
        { key: 'descripcion', header: 'Descripción' },
        { key: 'id_tipo', header: 'Tipo de Servicio (ID)' },
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
