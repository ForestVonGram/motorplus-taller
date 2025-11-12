import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

interface TipoServicio {
  idTipo: number | string;
  nombreTipo: string;
  descripcionTipo?: string | null;
}

export default function TiposServiciosDetail() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();

  const [form, setForm] = useState<TipoServicio | null>(null);
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
        const res = await fetch(`${API_BASE}/api/tipos-servicio/${encodeURIComponent(id || '')}`, { signal: controller.signal });
        if (res.status === 404) throw new Error('Tipo de servicio no encontrado');
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        if (alive) setForm(data);
      } catch (e: any) {
        if (alive) setError(e?.message || 'Error cargando tipo de servicio');
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

  const onChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
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
        idTipo: Number(id),
        nombreTipo: String(form.nombreTipo || '').trim(),
        descripcionTipo: (form.descripcionTipo ?? '').toString().trim() || null,
      };
      const res = await fetch(`${API_BASE}/api/tipos-servicio/${encodeURIComponent(id)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/tipos-servicio');
    } catch (err: any) {
      setError(err?.message || 'Error al guardar cambios');
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async () => {
    if (!id) return;
    const ok = window.confirm(`¿Eliminar tipo de servicio ${id}? Esta acción no se puede deshacer.`);
    if (!ok) return;
    setSaving(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/tipos-servicio/${encodeURIComponent(id)}`, { method: 'DELETE' });
      if (res.status !== 204 && !res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/tipos-servicio');
    } catch (err: any) {
      setError(err?.message || 'Error al eliminar tipo de servicio');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="vehiculos-page">
        <div className="page-header">
          <h1>Tipo de Servicio {id}</h1>
        </div>
        <div className="loading">Cargando tipo de servicio...</div>
      </div>
    );
  }

  if (error && !form) {
    return (
      <div className="vehiculos-page">
        <div className="page-header">
          <h1>Tipo de Servicio {id}</h1>
        </div>
        <div className="error-text">{error}</div>
        <div style={{ marginTop: 16 }}>
          <button className="btn-secondary" onClick={() => navigate('/tipos-servicio')}>Volver al listado</button>
        </div>
      </div>
    );
  }

  if (!form) return null;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Tipo de Servicio {id}</h1>
        <div>
          <button className="btn-secondary" onClick={() => navigate('/tipos-servicio')}>Volver</button>
        </div>
      </div>

      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>ID Tipo</label>
          <input name="idTipo" value={form.idTipo} disabled />
        </div>
        <div className="form-row">
          <label>Nombre</label>
          <input name="nombreTipo" required value={form.nombreTipo || ''} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Descripción</label>
          <textarea name="descripcionTipo" value={form.descripcionTipo || ''} onChange={onChange} />
        </div>

        {error && <div className="error-text">{error}</div>}

        <div className="form-actions">
          <button type="button" className="btn-danger" onClick={onDelete} disabled={saving}>
            {saving ? 'Procesando...' : 'Eliminar'}
          </button>
          <div style={{ flex: 1 }} />
          <button type="button" className="btn-secondary" onClick={() => navigate('/tipos-servicio')} disabled={saving}>
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
