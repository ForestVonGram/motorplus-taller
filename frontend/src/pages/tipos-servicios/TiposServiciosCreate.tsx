import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

export default function TiposServiciosCreate() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ idTipo: '', nombreTipo: '', descripcionTipo: '' });
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
      const res = await fetch(`${API_BASE}/api/tipos-servicio`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          idTipo: Number(form.idTipo),
          nombreTipo: form.nombreTipo.trim(),
          descripcionTipo: form.descripcionTipo.trim() || null,
        }),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/tipos-servicio');
    } catch (err: any) {
      setError(err?.message || 'Error al crear el tipo de servicio');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nuevo Tipo de servicio</h1>
      </div>
      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>ID Tipo</label>
          <input name="idTipo" type="number" required value={form.idTipo} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Nombre</label>
          <input name="nombreTipo" required value={form.nombreTipo} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Descripción</label>
          <textarea name="descripcionTipo" value={form.descripcionTipo} onChange={onChange} />
        </div>
        {error && <div className="error-text">{error}</div>}
        <div className="form-actions">
          <button type="button" className="btn-secondary" onClick={() => navigate('/tipos-servicio')}>Cancelar</button>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Tipo'}
          </button>
        </div>
      </form>
    </div>
  );
}
