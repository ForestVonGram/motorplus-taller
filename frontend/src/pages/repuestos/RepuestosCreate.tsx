import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

export default function RepuestosCreate() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ idRepuesto: '', nombre: '', costoUnitario: '', stockDisponible: '' });
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
      const res = await fetch(`${API_BASE}/api/repuestos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          idRepuesto: Number(form.idRepuesto),
          nombre: form.nombre.trim(),
          costoUnitario: Number(form.costoUnitario),
          stockDisponible: Number(form.stockDisponible),
        }),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/repuestos');
    } catch (err: any) {
      setError(err?.message || 'Error al crear repuesto');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nuevo Repuesto</h1>
      </div>
      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>ID Repuesto</label>
          <input name="idRepuesto" type="number" required value={form.idRepuesto} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Nombre</label>
          <input name="nombre" required value={form.nombre} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Costo Unitario</label>
          <input name="costoUnitario" type="number" required value={form.costoUnitario} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Stock Disponible</label>
          <input name="stockDisponible" type="number" required value={form.stockDisponible} onChange={onChange} />
        </div>
        {error && <div className="error-text">{error}</div>}
        <div className="form-actions">
          <button type="button" className="btn-secondary" onClick={() => navigate('/repuestos')}>Cancelar</button>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Repuesto'}
          </button>
        </div>
      </form>
    </div>
  );
}
