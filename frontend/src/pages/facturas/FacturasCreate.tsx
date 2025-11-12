import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../vehiculos/VehiculosList.css';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

export default function FacturasCreate() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ idFactura: '', costoManoObra: '', impuesto: '', fechaEmision: '', estadoPago: 'Pendiente', idOrden: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const onChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const totalCalc = Number(form.costoManoObra || 0) + Number(form.impuesto || 0);
      const res = await fetch(`${API_BASE}/api/facturas`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          idFactura: Number(form.idFactura),
          costoManoObra: Number(form.costoManoObra),
          total: totalCalc,
          impuesto: Number(form.impuesto),
          fechaEmision: form.fechaEmision,
          estadoPago: form.estadoPago,
          idOrden: Number(form.idOrden),
        }),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      navigate('/facturas');
    } catch (err: any) {
      setError(err?.message || 'Error al crear factura');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Nueva Factura</h1>
      </div>
      <form className="form-card" onSubmit={onSubmit}>
        <div className="form-row">
          <label>N° Factura</label>
          <input name="idFactura" type="number" required value={form.idFactura} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>N° Orden</label>
          <input name="idOrden" type="number" required value={form.idOrden} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Fecha Emisión</label>
          <input name="fechaEmision" type="date" required value={form.fechaEmision} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Mano de Obra</label>
          <input name="costoManoObra" type="number" required value={form.costoManoObra} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Impuesto</label>
          <input name="impuesto" type="number" required value={form.impuesto} onChange={onChange} />
        </div>
        <div className="form-row">
          <label>Total</label>
          <input name="total" type="number" readOnly value={Number(form.costoManoObra || 0) + Number(form.impuesto || 0)} />
        </div>
        <div className="form-row">
          <label>Estado de Pago</label>
          <select name="estadoPago" value={form.estadoPago} onChange={onChange}>
            <option value="Pendiente">Pendiente</option>
            <option value="Pagado">Pagado</option>
            <option value="Cancelado">Cancelado</option>
          </select>
        </div>
        {error && <div className="error-text">{error}</div>}
        <div className="form-actions">
          <button type="button" className="btn-secondary" onClick={() => navigate('/facturas')}>Cancelar</button>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Factura'}
          </button>
        </div>
      </form>
    </div>
  );
}
