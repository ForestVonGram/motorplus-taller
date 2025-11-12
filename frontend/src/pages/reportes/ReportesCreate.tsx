import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

interface ReporteDef {
  id: string;
  nombre: string;
  categoria: 'Simple' | 'Intermedio' | 'Complejo';
  descripcion: string;
}

const ReportesCreate = () => {
  const navigate = useNavigate();
  const [nombre, setNombre] = useState('');
  const [categoria, setCategoria] = useState<ReporteDef['categoria']>('Simple');
  const [descripcion, setDescripcion] = useState('');

  const handleSave = () => {
    if (!nombre.trim()) {
      alert('Ingrese un nombre de reporte');
      return;
    }
    const nuevo: ReporteDef = {
      id: nombre.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$|--+/g, '-'),
      nombre: nombre.trim(),
      categoria,
      descripcion: descripcion.trim() || 'Reporte personalizado',
    };

    const raw = localStorage.getItem('custom-reportes');
    const arr: ReporteDef[] = raw ? JSON.parse(raw) : [];

    // Evitar duplicados por id
    const exists = arr.some(r => r.id === nuevo.id);
    if (exists) {
      alert('Ya existe un reporte con un id derivado del mismo nombre. Cambie el nombre.');
      return;
    }

    arr.push(nuevo);
    localStorage.setItem('custom-reportes', JSON.stringify(arr));
    navigate('/reportes');
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nuevo Reporte</h1>
        <button className="btn-primary" onClick={() => navigate('/reportes')}>Volver</button>
      </div>

      <div style={{ background: '#fff', padding: 24, borderRadius: 12, boxShadow: '0 2px 8px rgba(0,0,0,0.05)', maxWidth: 720 }}>
        <div style={{ display: 'grid', gap: 16 }}>
          <label style={{ display: 'grid', gap: 8 }}>
            <span>Nombre</span>
            <input value={nombre} onChange={e => setNombre(e.target.value)} placeholder="Ej: Pedidos del mes de mayo" />
          </label>

          <label style={{ display: 'grid', gap: 8 }}>
            <span>Categoría</span>
            <select value={categoria} onChange={e => setCategoria(e.target.value as any)}>
              <option value="Simple">Simple</option>
              <option value="Intermedio">Intermedio</option>
              <option value="Complejo">Complejo</option>
            </select>
          </label>

          <label style={{ display: 'grid', gap: 8 }}>
            <span>Descripción</span>
            <textarea rows={4} value={descripcion} onChange={e => setDescripcion(e.target.value)} placeholder="Describe brevemente el objetivo del reporte" />
          </label>

          <div>
            <button className="btn-primary" onClick={handleSave}>Guardar</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReportesCreate;
