import { useEffect, useMemo, useState } from 'react';
import DataTable, { type Column } from '../../components/DataTable';
import '../vehiculos/VehiculosList.css';
import '../../components/DataTable.css';
import ReactECharts from 'echarts-for-react';

const API_BASE = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:7001';

interface ReporteDef {
  id: string;
  nombre: string;
  categoria: 'Simple' | 'Intermedio' | 'Complejo';
  descripcion: string;
}

const categoryBadge = (categoria: ReporteDef['categoria']) => {
  switch (categoria) {
    case 'Simple':
      return 'badge-success';
    case 'Intermedio':
      return 'badge-warning';
    case 'Complejo':
      return 'badge-danger';
    default:
      return 'badge-primary';
  }
};

// Renderers de contenido de reportes (consumo real cuando hay endpoint disponible)
function HistorialVehiculoView() {
  const [placa, setPlaca] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any | null>(null);

  const fetchData = async () => {
    if (!placa.trim()) { setError('Ingrese una placa'); return; }
    setLoading(true); setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/reportes/vehiculos/${encodeURIComponent(placa.trim())}/historial`);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const json = await res.json();
      setData(json);
    } catch (e:any) {
      setError(e.message || 'Error al cargar');
      setData(null);
    } finally { setLoading(false); }
  };

  const sectionStyle: React.CSSProperties = { padding: 16, borderRadius: 8, border: '1px solid #e9ecef', marginBottom: 12 };
  const title = (t: string) => <h3 style={{ margin: '16px 0 8px 0' }}>{t}</h3>;

  return (
    <div>
      <div style={{ display:'flex', gap:8, marginBottom:12 }}>
        <input value={placa} onChange={e=>setPlaca(e.target.value)} placeholder="Placa (ej: ABC-123)" />
        <button className="btn-primary" onClick={fetchData}>Cargar</button>
      </div>
      {loading && <div className="loading">Cargando...</div>}
      {error && <div className="empty-state">{error}</div>}
      {data && (
        <div>
          <h2>Historial de vehículo</h2>
          {title('Datos del vehículo')}
          <div style={sectionStyle}>
            <div style={{ display:'flex', gap:24 }}>
              <div><strong>Placa:</strong> {data.vehiculo?.placa}</div>
              <div><strong>Marca:</strong> {data.vehiculo?.marca}</div>
              <div><strong>Año:</strong> {data.vehiculo?.anio}</div>
              <div><strong>ID Cliente:</strong> {data.vehiculo?.idCliente}</div>
            </div>
          </div>
          {title('Órdenes de trabajo')}
          <div style={sectionStyle}>
            <ul>
              {(data.ordenes||[]).map((o:any)=> (
                <li key={o.idOrden}>#OT-{o.idOrden} · {o.fechaIngreso} · {o.diagnosticoInicial} · {o.fechaFinalizacion ? 'Finalizada' : 'En curso'}</li>
              ))}
            </ul>
          </div>
          {title('Facturas')}
          <div style={sectionStyle}>
            <ul>
              {(data.facturas||[]).map((f:any)=> (
                <li key={f.idFactura}>#F-{f.idFactura} · {f.fechaEmision} · ${'{'}f.total{'}'} · {f.estadoPago}</li>
              ))}
            </ul>
          </div>
        </div>
      )}
    </div>
  );
}

function FacturasPorClienteView() {
  const [idCliente, setIdCliente] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any | null>(null);

  const fetchData = async () => {
    const id = parseInt(idCliente.trim(), 10);
    if (isNaN(id)) { setError('Ingrese un ID de cliente válido'); return; }
    setLoading(true); setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/reportes/clientes/${id}/facturas`);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const json = await res.json();
      setData(json);
    } catch (e:any) {
      setError(e.message || 'Error al cargar');
      setData(null);
    } finally { setLoading(false); }
  };

  const sectionStyle: React.CSSProperties = { padding: 16, borderRadius: 8, border: '1px solid #e9ecef', marginBottom: 12 };
  const title = (t: string) => <h3 style={{ margin: '16px 0 8px 0' }}>{t}</h3>;

  const currency = new Intl.NumberFormat('es-CO', { style:'currency', currency:'COP', maximumFractionDigits:0 });

  return (
    <div>
      <div style={{ display:'flex', gap:8, marginBottom:12 }}>
        <input value={idCliente} onChange={e=>setIdCliente(e.target.value)} placeholder="ID Cliente (ej: 1)" />
        <button className="btn-primary" onClick={fetchData}>Cargar</button>
      </div>
      {loading && <div className="loading">Cargando...</div>}
      {error && <div className="empty-state">{error}</div>}
      {data && (
        <div>
          <h2>Facturas por cliente</h2>
          {title('Cliente')}
          <div style={sectionStyle}>{data.cliente?.nombre} {data.cliente?.apellido} · ID {data.cliente?.idCliente}</div>
          {title('Listado de facturas')}
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr>
                <th style={{ textAlign:'left', borderBottom:'1px solid #dee2e6', padding:8 }}>N°</th>
                <th style={{ textAlign:'left', borderBottom:'1px solid #dee2e6', padding:8 }}>Fecha</th>
                <th style={{ textAlign:'left', borderBottom:'1px solid #dee2e6', padding:8 }}>Orden</th>
                <th style={{ textAlign:'right', borderBottom:'1px solid #dee2e6', padding:8 }}>Monto</th>
                <th style={{ textAlign:'left', borderBottom:'1px solid #dee2e6', padding:8 }}>Estado</th>
              </tr>
            </thead>
            <tbody>
              {(data.facturas||[]).map((f:any)=> (
                <tr key={f.idFactura}>
                  <td style={{ padding:8, borderBottom:'1px solid #f1f3f5' }}>F-{f.idFactura}</td>
                  <td style={{ padding:8, borderBottom:'1px solid #f1f3f5' }}>{f.fechaEmision}</td>
                  <td style={{ padding:8, borderBottom:'1px solid #f1f3f5' }}>OT-{f.idOrden}</td>
                  <td style={{ padding:8, borderBottom:'1px solid #f1f3f5', textAlign:'right' }}>{currency.format(f.total)}</td>
                  <td style={{ padding:8, borderBottom:'1px solid #f1f3f5' }}>{f.estadoPago}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

function ClientesActivosView() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any | null>(null);
  const sectionStyle: React.CSSProperties = { padding: 16, borderRadius: 8, border: '1px solid #e9ecef', marginBottom: 12 };
  const title = (t: string) => <h3 style={{ margin: '16px 0 8px 0' }}>{t}</h3>;
  const currency = new Intl.NumberFormat('es-CO', { style:'currency', currency:'COP', maximumFractionDigits:0 });

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true); setError(null);
      try {
        const res = await fetch(`${API_BASE}/api/reportes/clientes-activos`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        setData(json);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
        setData(null);
      } finally { setLoading(false); }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!data) return null;

  return (
    <div>
      <h2>Clientes activos</h2>
      <div style={sectionStyle}>Total clientes: {data.totalClientes}</div>
      {title('Top clientes por facturación')}
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th style={{ textAlign:'left', borderBottom:'1px solid #dee2e6', padding:8 }}>Cliente</th>
            <th style={{ textAlign:'right', borderBottom:'1px solid #dee2e6', padding:8 }}>Total Facturado</th>
          </tr>
        </thead>
        <tbody>
          {(data.topClientes||[]).map((c:any)=> (
            <tr key={c.idCliente}>
              <td style={{ padding:8, borderBottom:'1px solid #f1f3f5' }}>{c.nombre} {c.apellido}</td>
              <td style={{ padding:8, borderBottom:'1px solid #f1f3f5', textAlign:'right' }}>{currency.format(c.totalFacturado)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function RendimientoMecanicosView() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true); setError(null);
      try {
        const res = await fetch(`${API_BASE}/api/reportes/rendimiento-mecanicos`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        setData(json);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
        setData(null);
      } finally { setLoading(false); }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!data) return null;

  return (
    <div>
      <h2>Mecánicos activos</h2>
      <ul>
        {(data.mecanicos||[]).map((m:any)=> (
          <li key={m.idMecanico}>{m.nombre}</li>
        ))}
      </ul>
    </div>
  );
}

function InventarioPorProveedorView() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any[] | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true); setError(null);
      try {
        const res = await fetch(`${API_BASE}/api/proveedores`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        setData(json);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
        setData(null);
      } finally { setLoading(false); }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!data) return null;

  return (
    <div>
      <h2>Lista de proveedores</h2>
      <ul>
        {data.map((p:any) => (
          <li key={p.idProveedor}>{p.nombre}</li>
        ))}
      </ul>
    </div>
  );
}

function OrdenesPorEstadoView() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any | null>(null);
  const sectionStyle: React.CSSProperties = { padding: 16, borderRadius: 8, border: '1px solid #e9ecef', marginBottom: 12 };

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true); setError(null);
      try {
        const res = await fetch(`${API_BASE}/api/reportes/ordenes-por-estado`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        setData(json);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
        setData(null);
      } finally { setLoading(false); }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!data) return null;

  return (
    <div>
      <h2>Órdenes por estado</h2>
      <div style={{ display: 'flex', gap: 16 }}>
        <div style={sectionStyle}>Pendientes: {data.pendientes}</div>
        <div style={sectionStyle}>En Proceso: {data.enProceso}</div>
        <div style={sectionStyle}>Completadas: {data.completadas}</div>
        <div style={sectionStyle}>Facturadas: {data.facturadas}</div>
      </div>
    </div>
  );
}

function FacturacionMensualView() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any | null>(null);
  const sectionStyle: React.CSSProperties = { padding: 16, borderRadius: 8, border: '1px solid #e9ecef', marginBottom: 12 };
  const title = (t: string) => <h3 style={{ margin: '16px 0 8px 0' }}>{t}</h3>;
  const currency = new Intl.NumberFormat('es-CO', { style:'currency', currency:'COP', maximumFractionDigits:0 });

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true); setError(null);
      try {
        const res = await fetch(`${API_BASE}/api/reportes/facturacion-mensual`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        setData(json);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
        setData(null);
      } finally { setLoading(false); }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!data) return null;

  const meses = (data.meses||[]).slice().reverse();
  const lineOptions = {
    tooltip: {
      trigger: 'axis',
      valueFormatter: (val: number) => currency.format(val)
    },
    xAxis: {
      type: 'category',
      data: meses.map((m:any)=> m.mes),
      axisLabel: { rotate: 0 }
    },
    yAxis: {
      type: 'value',
      axisLabel: { formatter: (v:number) => currency.format(v).replace(/\,00$/, '') }
    },
    grid: { left: 40, right: 16, top: 20, bottom: 28 },
    series: [
      {
        name: 'Facturación',
        type: 'line',
        smooth: true,
        areaStyle: {},
        showSymbol: false,
        data: meses.map((m:any)=> m.total)
      }
    ]
  } as any;

  return (
    <div>
      <h2>Facturación mensual</h2>
      <div style={sectionStyle}>Total últimos 6 meses: {currency.format(data.totalAnio)}</div>
      {title('Últimos 6 meses')}
      <div style={{ width: '100%', height: 320 }}>
        <ReactECharts option={lineOptions} notMerge={true} lazyUpdate={true} opts={{ renderer: 'svg' }} style={{ height: '100%', width: '100%' }} />
      </div>
    </div>
  );
}

function ServiciosDisponiblesView() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any[] | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true); setError(null);
      try {
        const res = await fetch(`${API_BASE}/api/servicios/search?q=`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        setData(json);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
        setData(null);
      } finally { setLoading(false); }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!data) return null;

  return (
    <div>
      <h2>Servicios disponibles</h2>
      <ul>
        {data.map((s:any) => (
          <li key={s.idServicio}>{s.nombre}{s.descripcion ? ` — ${s.descripcion}` : ''}</li>
        ))}
      </ul>
    </div>
  );
}

function OrdenesRecientesView() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any[] | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true); setError(null);
      try {
        const res = await fetch(`${API_BASE}/api/ordenes/search?q=`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        // Mostrar las más recientes primero (por id) y limitar a 20
        const sorted = [...json].sort((a:any,b:any)=> b.idOrden - a.idOrden).slice(0,20);
        setData(sorted);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
        setData(null);
      } finally { setLoading(false); }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!data) return null;

  return (
    <div>
      <h2>Órdenes recientes</h2>
      <ul>
        {data.map((o:any) => (
          <li key={o.idOrden}>OT-{o.idOrden} · {o.placa} · {o.fechaIngreso} · {o.fechaFinalizacion ? 'Finalizada' : 'En curso'}</li>
        ))}
      </ul>
    </div>
  );
}

function VentasVsCostosView() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any | null>(null);
  const sectionStyle: React.CSSProperties = { padding: 16, borderRadius: 8, border: '1px solid #e9ecef', marginBottom: 12 };
  const currency = new Intl.NumberFormat('es-CO', { style:'currency', currency:'COP', maximumFractionDigits:0 });

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true); setError(null);
      try {
        const res = await fetch(`${API_BASE}/api/reportes/ventas-vs-costos`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        setData(json);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
        setData(null);
      } finally { setLoading(false); }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!data) return null;

  const barOptions = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      valueFormatter: (val:number) => currency.format(val)
    },
    xAxis: { type: 'category', data: ['Ventas', 'Costos', 'Margen'] },
    yAxis: { type: 'value', axisLabel: { formatter: (v:number)=> currency.format(v).replace(/\,00$/, '') } },
    grid: { left: 40, right: 16, top: 20, bottom: 28 },
    series: [{
      type: 'bar',
      data: [data.ventas, data.costos, data.margen],
      itemStyle: {
        color: function(params:any){
          const colors = ['#228be6', '#fa5252', '#40c057'];
          return colors[params.dataIndex] || '#228be6';
        }
      }
    }]
  } as any;

  return (
    <div>
      <h2>Ventas vs Costos</h2>
      <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
        <div style={sectionStyle}><strong>Ventas</strong><div>{currency.format(data.ventas)}</div></div>
        <div style={sectionStyle}><strong>Costos</strong><div>{currency.format(data.costos)}</div></div>
        <div style={sectionStyle}><strong>Margen</strong><div>{currency.format(data.margen)} ({data.porcentajeMargen.toFixed(1)}%)</div></div>
      </div>
      <div style={{ width: '100%', height: 320 }}>
        <ReactECharts option={barOptions} notMerge={true} lazyUpdate={true} opts={{ renderer: 'svg' }} style={{ height: '100%', width: '100%' }} />
      </div>
    </div>
  );
}

// Reporte complejo: KPI de negocio (combina 3 endpoints)
function KpiNegocioView() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [ordenes, setOrdenes] = useState<any | null>(null);
  const [mensual, setMensual] = useState<any | null>(null);
  const [vyc, setVyc] = useState<any | null>(null);
  const currency = new Intl.NumberFormat('es-CO', { style:'currency', currency:'COP', maximumFractionDigits:0 });

  useEffect(() => {
    const load = async () => {
      try {
        const [o, m, v] = await Promise.all([
          fetch(`${API_BASE}/api/reportes/ordenes-por-estado`).then(r=>{ if(!r.ok) throw new Error(`HTTP ${r.status}`); return r.json(); }),
          fetch(`${API_BASE}/api/reportes/facturacion-mensual`).then(r=>{ if(!r.ok) throw new Error(`HTTP ${r.status}`); return r.json(); }),
          fetch(`${API_BASE}/api/reportes/ventas-vs-costos`).then(r=>{ if(!r.ok) throw new Error(`HTTP ${r.status}`); return r.json(); })
        ]);
        setOrdenes(o); setMensual(m); setVyc(v);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!ordenes || !mensual || !vyc) return null;

  const totalUlt6m = mensual.totalAnio;
  const meses = (mensual.meses||[]).slice().reverse();
  const ultimoMes = meses[meses.length-1]?.total || 0;
  const penultimoMes = meses[meses.length-2]?.total || 0;
  const trend = penultimoMes === 0 ? 0 : ((ultimoMes - penultimoMes) / penultimoMes) * 100;

  const card = (title: string, content: any) => (
    <div style={{ padding:16, border:'1px solid #e9ecef', borderRadius:8, minWidth:200 }}>
      <div style={{ fontSize:12, color:'#868e96' }}>{title}</div>
      <div style={{ fontSize:18, fontWeight:600 }}>{content}</div>
    </div>
  );

  const estadosOptions = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    xAxis: { type: 'category', data: ['Pendientes','En Proceso','Completadas','Facturadas'] },
    yAxis: { type: 'value' },
    grid: { left: 40, right: 16, top: 20, bottom: 28 },
    series: [{ type: 'bar', data: [ordenes.pendientes, ordenes.enProceso, ordenes.completadas, ordenes.facturadas], itemStyle: { color: '#228be6' } }]
  } as any;

  const mensualOptions = {
    tooltip: { trigger: 'axis', valueFormatter: (v:number)=> currency.format(v) },
    xAxis: { type: 'category', data: meses.map((m:any)=> m.mes) },
    yAxis: { type: 'value', axisLabel: { formatter: (v:number)=> currency.format(v).replace(/\,00$/, '') } },
    grid: { left: 40, right: 16, top: 20, bottom: 28 },
    series: [{ type: 'line', smooth: true, areaStyle: {}, showSymbol: false, data: meses.map((m:any)=> m.total) }]
  } as any;

  return (
    <div>
      <h2>KPIs de negocio</h2>
      <div style={{ display:'flex', gap:16, flexWrap:'wrap', marginBottom: 12 }}>
        {card('Ventas (6m)', currency.format(totalUlt6m))}
        {card('Ventas último mes', currency.format(ultimoMes))}
        {card('Tendencia vs mes anterior', `${trend.toFixed(1)}%`)}
        {card('Pendientes', ordenes.pendientes)}
        {card('Completadas', ordenes.completadas)}
        {card('Facturadas', ordenes.facturadas)}
        {card('Margen', `${currency.format(vyc.margen)} (${vyc.porcentajeMargen.toFixed(1)}%)`)}
      </div>
      <div style={{ display:'grid', gridTemplateColumns:'repeat(auto-fit, minmax(280px, 1fr))', gap: 16 }}>
        <div style={{ width: '100%', height: 300 }}>
          <ReactECharts option={estadosOptions} notMerge={true} lazyUpdate={true} opts={{ renderer: 'svg' }} style={{ height: '100%', width: '100%' }} />
        </div>
        <div style={{ width: '100%', height: 300 }}>
          <ReactECharts option={mensualOptions} notMerge={true} lazyUpdate={true} opts={{ renderer: 'svg' }} style={{ height: '100%', width: '100%' }} />
        </div>
      </div>
    </div>
  );
}

// Reporte complejo: Tiempo de ciclo de órdenes (cálculo en cliente)
function TiempoCicloOrdenesView() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [stats, setStats] = useState<any | null>(null);
  const [top, setTop] = useState<any[] | null>(null);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await fetch(`${API_BASE}/api/ordenes/search?q=`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const list = await res.json();
        const durations:number[] = [];
        const finalized:any[] = [];
        let enCurso = 0;
        for (const o of list) {
          if (o.fechaFinalizacion) {
            const d1 = Date.parse(o.fechaIngreso);
            const d2 = Date.parse(o.fechaFinalizacion);
            const days = Math.max(0, Math.round((d2 - d1) / 86400000));
            durations.push(days);
            finalized.push({ idOrden: o.idOrden, placa: o.placa, days });
          } else {
            enCurso++;
          }
        }
        durations.sort((a,b)=>a-b);
        const avg = durations.length ? (durations.reduce((a,b)=>a+b,0)/durations.length) : 0;
        const med = durations.length ? (durations.length%2?durations[(durations.length-1)/2]:(durations[durations.length/2-1]+durations[durations.length/2])/2) : 0;
        const min = durations[0] || 0;
        const max = durations[durations.length-1] || 0;
        const top10 = finalized.sort((a,b)=> b.days - a.days).slice(0,10);
        setStats({ promedio: avg, mediana: med, minimo: min, maximo: max, totalFinalizadas: durations.length, enCurso });
        setTop(top10);
      } catch (e:any) {
        setError(e.message || 'Error al cargar');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="empty-state">{error}</div>;
  if (!stats || !top) return null;

  const sectionStyle: React.CSSProperties = { padding: 16, borderRadius: 8, border: '1px solid #e9ecef', marginBottom: 12 };

  return (
    <div>
      <h2>Tiempo de ciclo de órdenes</h2>
      <div style={{ display:'flex', gap:16, flexWrap:'wrap', marginBottom:12 }}>
        <div style={sectionStyle}><strong>Promedio</strong><div>{stats.promedio.toFixed(1)} días</div></div>
        <div style={sectionStyle}><strong>Mediana</strong><div>{stats.mediana.toFixed(1)} días</div></div>
        <div style={sectionStyle}><strong>Mínimo</strong><div>{stats.minimo} días</div></div>
        <div style={sectionStyle}><strong>Máximo</strong><div>{stats.maximo} días</div></div>
        <div style={sectionStyle}><strong>Finalizadas</strong><div>{stats.totalFinalizadas}</div></div>
        <div style={sectionStyle}><strong>En curso</strong><div>{stats.enCurso}</div></div>
      </div>
      <h3 style={{ margin: '16px 0 8px 0' }}>Top 10 ciclos más largos</h3>
      <ul>
        {top.map((t:any)=> (
          <li key={t.idOrden}>OT-{t.idOrden} · {t.placa} · {t.days} días</li>
        ))}
      </ul>
    </div>
  );
}

function ReporteRenderer({ id }: { id: string }) {
  switch (id) {
    case 'vehiculo-historial':
      return <HistorialVehiculoView />;
    case 'facturas-por-cliente':
      return <FacturasPorClienteView />;
    case 'clientes-activos':
      return <ClientesActivosView />;
    case 'rendimiento-mecanicos':
      return <RendimientoMecanicosView />;
    case 'inventario-por-proveedor':
      return <InventarioPorProveedorView />;
    case 'ordenes-por-estado':
      return <OrdenesPorEstadoView />;
    case 'facturacion-mensual':
      return <FacturacionMensualView />;
    case 'servicios-disponibles':
      return <ServiciosDisponiblesView />;
    case 'ordenes-recientes':
      return <OrdenesRecientesView />;
    case 'ventas-vs-costos':
      return <VentasVsCostosView />;
    case 'kpi-negocio':
      return <KpiNegocioView />;
    case 'tiempo-ciclo-ordenes':
      return <TiempoCicloOrdenesView />;
    default:
      return <div>Reporte no disponible.</div>;
  }
};

const ReportesList = () => {
  const [reportes, setReportes] = useState<ReporteDef[]>([]);
  const [loading, setLoading] = useState(true);
  const [filtro, setFiltro] = useState<ReporteDef['categoria'] | 'Todos'>('Todos');
  const [texto, setTexto] = useState('');
  const [preview, setPreview] = useState<ReporteDef | null>(null);

  // Cargar reportes fijos
  useEffect(() => {
    const reportesFijos: ReporteDef[] = [
      // Simples (3)
      { id: 'vehiculo-historial', nombre: 'Historial de vehículo', categoria: 'Simple', descripcion: 'Órdenes, servicios y facturas asociadas a un vehículo.' },
      { id: 'facturas-por-cliente', nombre: 'Facturas por cliente', categoria: 'Simple', descripcion: 'Listado de facturas emitidas para un cliente.' },
      { id: 'clientes-activos', nombre: 'Clientes activos', categoria: 'Simple', descripcion: 'Resumen de clientes activos y top por facturación.' },
      // Intermedios (4)
      { id: 'rendimiento-mecanicos', nombre: 'Mecánicos activos', categoria: 'Intermedio', descripcion: 'Listado de mecánicos activos.' },
      { id: 'inventario-por-proveedor', nombre: 'Lista de proveedores', categoria: 'Intermedio', descripcion: 'Listado de proveedores actuales.' },
      { id: 'ordenes-por-estado', nombre: 'Órdenes por estado', categoria: 'Intermedio', descripcion: 'Distribución de órdenes por estado actual.' },
      { id: 'facturacion-mensual', nombre: 'Facturación mensual', categoria: 'Intermedio', descripcion: 'Ingresos mensuales y acumulados.' },
      // Complejos nuevos (2)
      { id: 'kpi-negocio', nombre: 'KPIs de negocio', categoria: 'Complejo', descripcion: 'Indicadores combinados: ventas, costos y estados de orden.' },
      { id: 'tiempo-ciclo-ordenes', nombre: 'Tiempo de ciclo de órdenes', categoria: 'Complejo', descripcion: 'Duración promedio/mediana y casos más largos.' },
      { id: 'ventas-vs-costos', nombre: 'Ventas vs Costos', categoria: 'Complejo', descripcion: 'Comparativo entre ventas, costos y margen.' },
    ];

    setTimeout(() => {
      setReportes(reportesFijos);
      setLoading(false);
    }, 200);
  }, []);

  const filtered = useMemo(
    () => {
      const byCat = filtro === 'Todos' ? reportes : reportes.filter(r => r.categoria === filtro);
      const q = texto.trim().toLowerCase();
      if (!q) return byCat;
      return byCat.filter(r =>
        r.nombre.toLowerCase().includes(q) ||
        r.descripcion.toLowerCase().includes(q)
      );
    },
    [reportes, filtro, texto]
  );

  const handlePreview = (r: ReporteDef) => setPreview(r);

  const openPrintWindow = (html: string, title: string) => {
    const w = window.open('', '_blank', 'width=900,height=700');
    if (!w) return;
    w.document.open();
    w.document.write(`<!doctype html><html><head><meta charset="utf-8"/>
      <title>${title}</title>
      <style>
        body{font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; padding:24px; line-height: 1.6;}
        h1,h2,h3{margin:16px 0 8px 0; color: #212529;}
        h1{font-size: 24px;}
        h2{font-size: 20px;}
        h3{font-size: 16px;}
        table{width:100%; border-collapse:collapse; margin: 12px 0;}
        th{text-align: left; font-weight: 600; background: #f8f9fa; padding:8px; border-bottom:2px solid #dee2e6;}
        td{padding:8px; border-bottom:1px solid #e9ecef;}
        ul, ol{margin: 8px 0; padding-left: 24px;}
        li{margin: 4px 0;}
        .muted{color:#868e96;}
        .loading, .empty-state{display:none;}
        input{display:none;}
        div[style*="display:flex"]{display:flex !important;}
        div[style*="gap:16"]{gap:16px;}
        div[style*="padding: 16"]{padding:16px; border:1px solid #e9ecef; border-radius:8px; margin-bottom:12px;}
        @media print {
          body{padding:12px;}
          .loading, .empty-state, input, button{display:none !important;}
        }
      </style>
    </head><body>${html}</body></html>`);
    w.document.close();
    w.focus();
    setTimeout(() => w.print(), 500);
  };

  const handleExport = (r: ReporteDef) => {
    // Buscar el contenedor del modal que tiene el reporte renderizado
    const modalContent = document.querySelector('[data-report-content]');
    
    if (!modalContent) {
      // Si no hay modal abierto, mostrar mensaje
      alert('Por favor, abre el reporte en "Ver" antes de exportarlo para asegurar que los datos estén cargados.');
      return;
    }

    const fecha = new Date().toLocaleString();
    const header = `<div style="display:flex;justify-content:space-between;align-items:flex-end;margin-bottom:16px"><h1 style="margin:0">${r.nombre}</h1><div class="muted" style="font-size:12px">Generado: ${fecha}</div></div>`;
    
    // Clonar el contenido para evitar modificar el DOM original
    const clonedContent = modalContent.cloneNode(true) as HTMLElement;
    
    // Remover elementos que no queremos en el PDF (como botones de carga)
    const buttonsToRemove = clonedContent.querySelectorAll('button, .action-button');
    buttonsToRemove.forEach(btn => btn.remove());
    
    const fullHtml = header + clonedContent.innerHTML;
    openPrintWindow(fullHtml, r.nombre);
  };

  const columns: Column[] = [
    { key: 'nombre', header: 'Reporte' },
    { 
      key: 'categoria', 
      header: 'Categoría',
      render: (value) => <span className={`badge ${categoryBadge(value)}`}>{value}</span>
    },
    { key: 'descripcion', header: 'Descripción' },
    {
      key: 'acciones',
      header: '',
      render: (_v, row: ReporteDef) => (
        <div style={{ display: 'flex', gap: 8 }}>
          <button className="action-button" onClick={() => handlePreview(row)}>Ver</button>
          <button className="action-button" onClick={() => handleExport(row)}>Exportar PDF</button>
        </div>
      )
    }
  ];

  if (loading) return <div className="loading">Cargando reportes...</div>;

  return (
    <div className="vehiculos-page">
      <div className="page-header">
        <h1>Reportes</h1>
        <div style={{ display: 'flex', gap: 12 }}>
          <select 
            className="btn-primary"
            style={{ backgroundColor: '#f8f9fa', color: '#212529' }}
            value={filtro}
            onChange={(e) => setFiltro(e.target.value as any)}
          >
            <option value="Todos">Todas las categorías</option>
            <option value="Simple">Simples</option>
            <option value="Intermedio">Intermedios</option>
            <option value="Complejo">Complejos</option>
          </select>
          <button className="btn-primary" onClick={() => setFiltro('Todos')}>Limpiar</button>
        </div>
      </div>

      {/* Cuadro de búsqueda */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nombre o descripción de reporte..."
          value={texto}
          onChange={(e) => setTexto(e.target.value)}
        />
      </div>

      <DataTable columns={columns} data={filtered} />

      {/* Modal Vista previa */}
      {preview && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.45)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }} onClick={() => setPreview(null)}>
          <div style={{ width: 'min(900px, 92vw)', maxHeight: '92vh', overflow: 'auto', background: '#fff', borderRadius: 12, padding: 24, boxShadow: '0 10px 30px rgba(0,0,0,0.2)' }} onClick={e => e.stopPropagation()}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
              <h1 style={{ margin: 0, fontSize: 20 }}>{preview.nombre}</h1>
              <div style={{ display: 'flex', gap: 8 }}>
                <button className="action-button" onClick={() => handleExport(preview!)}>Exportar PDF</button>
                <button className="action-button" onClick={() => setPreview(null)}>Cerrar</button>
              </div>
            </div>
            <div data-report-content>
              <ReporteRenderer id={preview.id} />
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ReportesList;
