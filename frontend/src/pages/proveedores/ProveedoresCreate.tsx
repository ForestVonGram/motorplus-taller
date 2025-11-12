import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

export default function ProveedoresCreate() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ idProveedor: '', nombre: '' });
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
      const res = await fetch(`${API_BASE}/api/proveedores`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          idProveedor: Number(form.idProveedor),
          nombre: form.nombre.trim(),
        }),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/proveedores');
    } catch (err: any) {
      setError(err?.message || 'Error al crear proveedor');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nuevo Proveedor</h1>
      </div>
      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>ID Proveedor</label>
          <input name="idProveedor" type="number" required value={form.idProveedor} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Nombre</label>
          <input name="nombre" required value={form.nombre} onChange={onChange} />
        </div>
        {error && <div className="error-text">{error}</div>}
        <div className="form-actions">
          <button type="button" className="btn-secondary" onClick={() => navigate('/proveedores')}>Cancelar</button>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Proveedor'}
          </button>
        </div>
      </form>
    </div>
  );
}
