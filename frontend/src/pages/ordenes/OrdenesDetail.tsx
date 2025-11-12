import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

type ISODate = string;

interface OrdenTrabajo {
  idOrden: number | string;
  fechaIngreso: ISODate;
  diagnosticoInicial: string;
  fechaFinalizacion?: ISODate | '' | null;
  placa: string;
}

export default function OrdenesDetail() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();

  const [form, setForm] = useState<OrdenTrabajo | null>(null);
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
        const res = await fetch(`${API_BASE}/api/ordenes/${encodeURIComponent(id || '')}`, {
          signal: controller.signal,
        });
        if (res.status === 404) throw new Error('Orden no encontrada');
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        if (alive) {
          // Normalizar fechaFinalizacion a '' si viene null para el input date
          if (!data.fechaFinalizacion) data.fechaFinalizacion = '';
          setForm(data);
        }
      } catch (e: any) {
        if (alive) setError(e?.message || 'Error cargando orden');
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
      const payload: any = {
        idOrden: Number(id),
        fechaIngreso: form.fechaIngreso,
        diagnosticoInicial: String(form.diagnosticoInicial || '').trim(),
        placa: String(form.placa || '').trim(),
      };
      if (form.fechaFinalizacion) payload.fechaFinalizacion = form.fechaFinalizacion;

      const res = await fetch(`${API_BASE}/api/ordenes/${encodeURIComponent(id)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/ordenes');
    } catch (err: any) {
      setError(err?.message || 'Error al guardar cambios');
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async () => {
    if (!id) return;
    const ok = window.confirm(`¿Eliminar orden ${id}? Esta acción no se puede deshacer.`);
    if (!ok) return;
    setSaving(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/ordenes/${encodeURIComponent(id)}`, {
        method: 'DELETE',
      });
      if (res.status !== 204 && !res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/ordenes');
    } catch (err: any) {
      setError(err?.message || 'Error al eliminar orden');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="vehiculos-page">
        <div className="page-header">
          <h1>Orden {id}</h1>
        </div>
        <div className="loading">Cargando orden...</div>
      </div>
    );
  }

  if (error && !form) {
    return (
      <div className="vehiculos-page">
        <div className="page-header">
          <h1>Orden {id}</h1>
        </div>
        <div className="error-text">{error}</div>
        <div style={{ marginTop: 16 }}>
          <button className="btn-secondary" onClick={() => navigate('/ordenes')}>Volver al listado</button>
        </div>
      </div>
    );
  }

  if (!form) return null;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Orden {id}</h1>
        <div>
          <button className="btn-secondary" onClick={() => navigate('/ordenes')}>Volver</button>
        </div>
      </div>

      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>N° Orden</label>
          <input name="idOrden" value={form.idOrden} disabled />
        </div>
        <div className="form-row">
          <label>Placa</label>
          <input name="placa" required value={form.placa || ''} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Fecha Ingreso</label>
          <input name="fechaIngreso" type="date" required value={form.fechaIngreso || ''} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Diagnóstico Inicial</label>
          <textarea name="diagnosticoInicial" required value={form.diagnosticoInicial || ''} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Fecha Finalización (opcional)</label>
          <input name="fechaFinalizacion" type="date" value={form.fechaFinalizacion || ''} onChange={onChange} />
        </div>

        {error && <div className="error-text">{error}</div>}

        <div className="form-actions">
          <button type="button" className="btn-danger" onClick={onDelete} disabled={saving}>
            {saving ? 'Procesando...' : 'Eliminar'}
          </button>
          <div style={{ flex: 1 }} />
          <button type="button" className="btn-secondary" onClick={() => navigate('/ordenes')} disabled={saving}>
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
