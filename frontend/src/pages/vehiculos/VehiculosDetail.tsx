import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

interface Vehiculo {
  placa: string;
  marca: string;
  anio: number | string;
  idCliente: number | string;
}

export default function VehiculosDetail() {
  const navigate = useNavigate();
  const { placa } = useParams<{ placa: string }>();

  const [form, setForm] = useState<Vehiculo | null>(null);
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
        const res = await fetch(`${API_BASE}/api/vehiculos/${encodeURIComponent(placa || '')}`, {
          signal: controller.signal,
        });
        if (res.status === 404) {
          throw new Error('Vehículo no encontrado');
        }
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        if (alive) setForm(data);
      } catch (e: any) {
        if (alive) setError(e?.message || 'Error cargando vehículo');
      } finally {
        if (alive) setLoading(false);
      }
    }

    if (placa) load();
    return () => {
      alive = false;
      controller.abort();
    };
  }, [placa]);

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => (prev ? { ...prev, [name]: value } : prev));
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form || !placa) return;
    setSaving(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/vehiculos/${encodeURIComponent(placa)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          placa: placa,
          marca: String(form.marca || '').trim(),
          anio: Number(form.anio),
          idCliente: Number(form.idCliente),
        }),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/vehiculos');
    } catch (err: any) {
      setError(err?.message || 'Error al guardar cambios');
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async () => {
    if (!placa) return;
    const ok = window.confirm(`¿Eliminar vehículo ${placa}? Esta acción no se puede deshacer.`);
    if (!ok) return;
    setSaving(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/vehiculos/${encodeURIComponent(placa)}`, {
        method: 'DELETE',
      });
      if (res.status !== 204 && !res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/vehiculos');
    } catch (err: any) {
      setError(err?.message || 'Error al eliminar vehículo');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="vehiculos-page">
        <div className="page-header">
          <h1>Vehículo {placa}</h1>
        </div>
        <div className="loading">Cargando vehículo...</div>
      </div>
    );
  }

  if (error && !form) {
    return (
      <div className="vehiculos-page">
        <div className="page-header">
          <h1>Vehículo {placa}</h1>
        </div>
        <div className="error-text">{error}</div>
        <div style={{ marginTop: 16 }}>
          <button className="btn-secondary" onClick={() => navigate('/vehiculos')}>Volver al listado</button>
        </div>
      </div>
    );
  }

  if (!form) return null;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Vehículo {placa}</h1>
        <div>
          <button className="btn-secondary" onClick={() => navigate('/vehiculos')}>Volver</button>
        </div>
      </div>

      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>Placa</label>
          <input name="placa" value={form.placa} disabled />
        </div>
        <div className="form-row">
          <label>Marca</label>
          <input name="marca" required value={form.marca || ''} onChange={onChange} />
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
          <button type="button" className="btn-danger" onClick={onDelete} disabled={saving}>
            {saving ? 'Procesando...' : 'Eliminar'}
          </button>
          <div style={{ flex: 1 }} />
          <button type="button" className="btn-secondary" onClick={() => navigate('/vehiculos')} disabled={saving}>
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
