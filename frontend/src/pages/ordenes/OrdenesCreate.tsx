import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

type ISODate = string;

export default function OrdenesCreate() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ idOrden: '', fechaIngreso: '', diagnosticoInicial: '', fechaFinalizacion: '', placa: '' });
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
      const payload: any = {
        idOrden: Number(form.idOrden),
        fechaIngreso: form.fechaIngreso as ISODate,
        diagnosticoInicial: form.diagnosticoInicial.trim(),
        placa: form.placa.trim(),
      };
      if (form.fechaFinalizacion) payload.fechaFinalizacion = form.fechaFinalizacion as ISODate;

      const res = await fetch(`${API_BASE}/api/ordenes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/ordenes');
    } catch (err: any) {
      setError(err?.message || 'Error al crear orden de trabajo');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nueva Orden de Trabajo</h1>
      </div>
      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>N° Orden</label>
          <input name="idOrden" type="number" required value={form.idOrden} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Placa</label>
          <input name="placa" required value={form.placa} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Fecha Ingreso</label>
          <input name="fechaIngreso" type="date" required value={form.fechaIngreso} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Diagnóstico Inicial</label>
          <textarea name="diagnosticoInicial" required value={form.diagnosticoInicial} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Fecha Finalización (opcional)</label>
          <input name="fechaFinalizacion" type="date" value={form.fechaFinalizacion} onChange={onChange} />
        </div>
        {error && <div className="error-text">{error}</div>}
        <div className="form-actions">
          <button type="button" className="btn-secondary" onClick={() => navigate('/ordenes')}>Cancelar</button>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Orden'}
          </button>
        </div>
      </form>
    </div>
  );
}
