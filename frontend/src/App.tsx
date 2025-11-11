import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './pages/login/Login';
import Dashboard from './pages/dashboard/Dashboard';
import VehiculosList from './pages/vehiculos/VehiculosList';
import ClientesList from './pages/clientes/ClientesList';
import OrdenesList from './pages/ordenes/OrdenesList';
import MecanicosList from './pages/mecanicos/MecanicosList';
import FacturasList from './pages/facturas/FacturasList';
import RepuestosList from './pages/repuestos/RepuestosList';
import ProveedoresList from './pages/proveedores/ProveedoresList';
import ServiciosList from './pages/servicios/ServiciosList';
import './App.css';
import ReportesList from './pages/reportes/ReportesList';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        
        <Route path="/dashboard" element={<Layout><Dashboard /></Layout>} />
        <Route path="/vehiculos" element={<Layout><VehiculosList /></Layout>} />
        <Route path="/clientes" element={<Layout><ClientesList /></Layout>} />
        <Route path="/ordenes" element={<Layout><OrdenesList /></Layout>} />
        
        {/* Páginas placeholder - crear después */}
        <Route path="/mecanicos" element={<Layout><MecanicosList /></Layout>} />
        <Route path="/facturas" element={<Layout><FacturasList /></Layout>} />
        <Route path="/repuestos" element={<Layout><RepuestosList /></Layout>} />
        <Route path="/proveedores" element={<Layout><ProveedoresList /></Layout>} />
        <Route path="/servicios" element={<Layout><ServiciosList /></Layout>} />
        <Route path="/reportes" element={<Layout><ReportesList /></Layout>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
