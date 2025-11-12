import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

interface Proveedor {
  idProveedor: number | string;
  nombre: string;
}

export default function ProveedoresDetail() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();

  const [form, setForm] = useState<Proveedor | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let alive = true;
    const controller = new AbortController();

    async function load() {
      setError(null);
      setLoading(true);
      try {
        const res = await fetch(`${API_BASE}/api/proveedores/${encodeURIComponent(id || '')}`, { signal: controller.signal });
        if (res.status === 404) throw new Error('Proveedor no encontrado');
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        if (alive) setForm(data);
      } catch (e: any) {
        if (alive) setError(e?.message || 'Error cargando proveedor');
      } finally {
        if (alive) setLoading(false);
      }
    }

    if (id) load();
    return () => {
      alive = false;
      controller.abort();
    };
  }, [id]);

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => (prev ? { ...prev, [name]: value } : prev));
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form || !id) return;
    setSaving(true);
    setError(null);
    try {
      const payload = {
        idProveedor: Number(id),
        nombre: String(form.nombre || '').trim(),
      };
      const res = await fetch(`${API_BASE}/api/proveedores/${encodeURIComponent(id)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/proveedores');
    } catch (err: any) {
      setError(err?.message || 'Error al guardar cambios');
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async () => {
    if (!id) return;
    const ok = window.confirm(`¿Eliminar proveedor ${id}? Esta acción no se puede deshacer.`);
    if (!ok) return;
    setSaving(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/proveedores/${encodeURIComponent(id)}`, { method: 'DELETE' });
      if (res.status !== 204 && !res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/proveedores');
    } catch (err: any) {
      setError(err?.message || 'Error al eliminar proveedor');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="vehiculos-page">
        <div className="page-header">
          <h1>Proveedor {id}</h1>
        </div>
        <div className="loading">Cargando proveedor...</div>
      </div>
    );
  }

  if (error && !form) {
    return (
      <div className="vehiculos-page">
        <div className="page-header">
          <h1>Proveedor {id}</h1>
        </div>
        <div className="error-text">{error}</div>
        <div style={{ marginTop: 16 }}>
          <button className="btn-secondary" onClick={() => navigate('/proveedores')}>Volver al listado</button>
        </div>
      </div>
    );
  }

  if (!form) return null;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Proveedor {id}</h1>
        <div>
          <button className="btn-secondary" onClick={() => navigate('/proveedores')}>Volver</button>
        </div>
      </div>

      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>ID Proveedor</label>
          <input name="idProveedor" value={form.idProveedor} disabled />
        </div>
        <div className="form-row">
          <label>Nombre</label>
          <input name="nombre" required value={form.nombre || ''} onChange={onChange} />
        </div>

        {error && <div className="error-text">{error}</div>}

        <div className="form-actions">
          <button type="button" className="btn-danger" onClick={onDelete} disabled={saving}>
            {saving ? 'Procesando...' : 'Eliminar'}
          </button>
          <div style={{ flex: 1 }} />
          <button type="button" className="btn-secondary" onClick={() => navigate('/proveedores')} disabled={saving}>
            Cancelar
          </button>
          <button type="submit" className="btn-primary" disabled={saving}>
            {saving ? 'Guardando...' : 'Guardar cambios'}
          </button>
        </div>
      </form>
    </div>
  );
}
