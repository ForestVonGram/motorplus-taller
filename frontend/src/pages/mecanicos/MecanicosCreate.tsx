import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

export default function MecanicosCreate() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ idMecanico: '', nombre: '', idSupervisor: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const payload: any = {
        idMecanico: Number(form.idMecanico),
        nombre: form.nombre.trim(),
      };
      if (form.idSupervisor !== '') payload.idSupervisor = Number(form.idSupervisor);

      const res = await fetch(`${API_BASE}/api/mecanicos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/mecanicos');
    } catch (err: any) {
      setError(err?.message || 'Error al crear mecánico');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nuevo Mecánico</h1>
      </div>
      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>ID Mecánico</label>
          <input name="idMecanico" type="number" required value={form.idMecanico} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Nombre</label>
          <input name="nombre" required value={form.nombre} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>ID Supervisor (opcional)</label>
          <input name="idSupervisor" type="number" value={form.idSupervisor} onChange={onChange} />
        </div>
        {error && <div className="error-text">{error}</div>}
        <div className="form-actions">
          <button type="button" className="btn-secondary" onClick={() => navigate('/mecanicos')}>Cancelar</button>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Mecánico'}
          </button>
        </div>
      </form>
    </div>
  );
}
