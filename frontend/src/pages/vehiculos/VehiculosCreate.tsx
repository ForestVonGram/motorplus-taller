import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

export default function VehiculosCreate() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ placa: '', marca: '', anio: '', idCliente: '' });
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
      const res = await fetch(`${API_BASE}/api/vehiculos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          placa: form.placa.trim(),
          marca: form.marca.trim(),
          anio: Number(form.anio),
          idCliente: Number(form.idCliente),
        }),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/vehiculos');
    } catch (err: any) {
      setError(err?.message || 'Error al crear vehículo');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nuevo Vehículo</h1>
      </div>
      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>Placa</label>
          <input name="placa" required value={form.placa} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Marca</label>
          <input name="marca" required value={form.marca} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Año</label>
          <input name="anio" type="number" required value={form.anio} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>ID Cliente</label>
          <input name="idCliente" type="number" required value={form.idCliente} onChange={onChange} />
        </div>
        {error && <div className="error-text">{error}</div>}
        <div className="form-actions">
          <button type="button" className="btn-secondary" onClick={() => navigate('/vehiculos')}>Cancelar</button>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Vehículo'}
          </button>
        </div>
      </form>
    </div>
  );
}
