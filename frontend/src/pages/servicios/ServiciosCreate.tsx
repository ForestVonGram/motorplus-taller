import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

export default function ServiciosCreate() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ idServicio: '', nombre: '', descripcion: '', idTipo: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const onChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const res = await fetch(`${API_BASE}/api/servicios`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          idServicio: Number(form.idServicio),
          nombre: form.nombre.trim(),
          descripcion: form.descripcion.trim() || null,
          idTipo: Number(form.idTipo),
        }),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/servicios');
    } catch (err: any) {
      setError(err?.message || 'Error al crear servicio');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nuevo Servicio</h1>
      </div>
      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>ID Servicio</label>
          <input name="idServicio" type="number" required value={form.idServicio} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Nombre</label>
          <input name="nombre" required value={form.nombre} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Descripción</label>
          <textarea name="descripcion" value={form.descripcion} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Tipo (ID)</label>
          <input name="idTipo" type="number" required value={form.idTipo} onChange={onChange} />
        </div>
        {error && <div className="error-text">{error}</div>}
        <div className="form-actions">
          <button type="button" className="btn-secondary" onClick={() => navigate('/servicios')}>Cancelar</button>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Servicio'}
          </button>
        </div>
      </form>
    </div>
  );
}
