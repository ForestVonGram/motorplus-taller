import { useEffect, useState } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';

interface Proveedor {
    id_proveedor: number;
    nombre: string;
}

const ProveedoresList = () => {
    const [proveedores, setProveedores] = useState<Proveedor[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Aquí luego puedes reemplazar el fetch con tu API real (por ejemplo: fetch('/api/proveedores'))
        const mockData: Proveedor[] = [
            { id_proveedor: 1, nombre: 'ACME Parts' },
            { id_proveedor: 2, nombre: 'FrenosPlus' },
            { id_proveedor: 3, nombre: 'EnergíaMax' },
            { id_proveedor: 4, nombre: 'Motores S.A.' },
            { id_proveedor: 5, nombre: 'LubriOil' },
        ];

        setTimeout(() => {
            setProveedores(mockData);
            setLoading(false);
        }, 450);
    }, []);

    const columns: Column[] = [
        { key: 'id_proveedor', header: 'ID' },
        { key: 'nombre', header: 'Nombre del Proveedor' },
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
