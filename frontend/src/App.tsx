import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './pages/login/Login';
import Dashboard from './pages/dashboard/Dashboard';
import VehiculosList from './pages/vehiculos/VehiculosList';
import VehiculosCreate from './pages/vehiculos/VehiculosCreate';
import ClientesList from './pages/clientes/ClientesList';
import ClientesCreate from './pages/clientes/ClientesCreate';
import OrdenesList from './pages/ordenes/OrdenesList';
import OrdenesCreate from './pages/ordenes/OrdenesCreate';
import MecanicosList from './pages/mecanicos/MecanicosList';
import MecanicosCreate from './pages/mecanicos/MecanicosCreate';
import FacturasList from './pages/facturas/FacturasList';
import FacturasCreate from './pages/facturas/FacturasCreate';
import RepuestosList from './pages/repuestos/RepuestosList';
import RepuestosCreate from './pages/repuestos/RepuestosCreate';
import ProveedoresList from './pages/proveedores/ProveedoresList';
import ProveedoresCreate from './pages/proveedores/ProveedoresCreate';
import ServiciosList from './pages/servicios/ServiciosList';
import ServiciosCreate from './pages/servicios/ServiciosCreate';
import TiposServiciosList from './pages/tipos-servicios/TiposServiciosList';
import TiposServiciosCreate from './pages/tipos-servicios/TiposServiciosCreate';
import './App.css';
import ReportesList from './pages/reportes/ReportesList';
import ReportesCreate from './pages/reportes/ReportesCreate';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        
        <Route path="/dashboard" element={<Layout><Dashboard /></Layout>} />
        
        {/* Vehículos */}
        <Route path="/vehiculos" element={<Layout><VehiculosList /></Layout>} />
        <Route path="/vehiculos/crear" element={<Layout><VehiculosCreate /></Layout>} />
        
        {/* Clientes */}
        <Route path="/clientes" element={<Layout><ClientesList /></Layout>} />
        <Route path="/clientes/crear" element={<Layout><ClientesCreate /></Layout>} />
        
        {/* Órdenes */}
        <Route path="/ordenes" element={<Layout><OrdenesList /></Layout>} />
        <Route path="/ordenes/crear" element={<Layout><OrdenesCreate /></Layout>} />
        
        {/* Mecánicos */}
        <Route path="/mecanicos" element={<Layout><MecanicosList /></Layout>} />
        <Route path="/mecanicos/crear" element={<Layout><MecanicosCreate /></Layout>} />
        
        {/* Facturas */}
        <Route path="/facturas" element={<Layout><FacturasList /></Layout>} />
        <Route path="/facturas/crear" element={<Layout><FacturasCreate /></Layout>} />
        
        {/* Repuestos */}
        <Route path="/repuestos" element={<Layout><RepuestosList /></Layout>} />
        <Route path="/repuestos/crear" element={<Layout><RepuestosCreate /></Layout>} />
        
        {/* Proveedores */}
        <Route path="/proveedores" element={<Layout><ProveedoresList /></Layout>} />
        <Route path="/proveedores/crear" element={<Layout><ProveedoresCreate /></Layout>} />
        
        {/* Servicios */}
        <Route path="/servicios" element={<Layout><ServiciosList /></Layout>} />
        <Route path="/servicios/crear" element={<Layout><ServiciosCreate /></Layout>} />

        {/* Tipos de servicio */}
        <Route path="/tipos-servicio" element={<Layout><TiposServiciosList /></Layout>} />
        <Route path="/tipos-servicio/crear" element={<Layout><TiposServiciosCreate /></Layout>} />
        
        {/* Reportes */}
        <Route path="/reportes" element={<Layout><ReportesList /></Layout>} />
        <Route path="/reportes/crear" element={<Layout><ReportesCreate /></Layout>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
