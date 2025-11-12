import { Suspense, lazy } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

// Helper to safely lazy-load pages that might be empty (no default export yet)
const lazySafe = <T extends { default?: React.ComponentType<any> }>(
  loader: () => Promise<T>,
) =>
  lazy(async () => {
    const mod = await loader()
    const Comp = (mod.default ?? (() => null)) as React.ComponentType<any>
    return { default: Comp }
  })

// Pages
const DashboardPage = lazySafe(() => import('../pages/dashboard/DashboardPage'))
const LoginPage = lazySafe(() => import('../pages/login/LoginPage'))
const FacturasList = lazySafe(() => import('../pages/facturas/FacturasList'))
const FacturasDetail = lazySafe(() => import('../pages/facturas/FacturasDetail'))
const OrdenesList = lazySafe(() => import('../pages/ordenes/OrdenesList'))
const OrdenesCreate = lazySafe(() => import('../pages/ordenes/OrdenesCreate'))
const OrdenesDetail = lazySafe(() => import('../pages/ordenes/OrdenesDetail'))
const MecanicosList = lazySafe(() => import('../pages/mecanicos/MecanicosList'))
const MecanicosCreate = lazySafe(() => import('../pages/mecanicos/MecanicosCreate'))
const MecanicosDetail = lazySafe(() => import('../pages/mecanicos/MecanicosDetail'))
const VehiculosList = lazySafe(() => import('../pages/vehiculos/VehiculosList'))
const VehiculosCreate = lazySafe(() => import('../pages/vehiculos/VehiculosCreate'))
const VehiculosDetail = lazySafe(() => import('../pages/vehiculos/VehiculosDetail'))
const ClientesList = lazySafe(() => import('../pages/clientes/ClientesList'))
const ClientesCreate = lazySafe(() => import('../pages/clientes/ClientesCreate'))
const ClientesDetail = lazySafe(() => import('../pages/clientes/ClientesDetail'))
const RepuestosList = lazySafe(() => import('../pages/repuestos/RepuestosList'))
const RepuestosCreate = lazySafe(() => import('../pages/repuestos/RepuestosCreate'))
const RepuestosDetail = lazySafe(() => import('../pages/repuestos/RepuestosDetail'))
const ProveedoresList = lazySafe(() => import('../pages/proveedores/ProveedoresList'))
const ProveedoresCreate = lazySafe(() => import('../pages/proveedores/ProveedoresCreate'))
const ProveedoresDetail = lazySafe(() => import('../pages/proveedores/ProveedoresDetail'))
const ServiciosList = lazySafe(() => import('../pages/servicios/ServiciosList'))
const ServiciosCreate = lazySafe(() => import('../pages/servicios/ServiciosCreate'))
const ServiciosDetail = lazySafe(() => import('../pages/servicios/ServiciosDetail'))

function NotFound() {
  return (
    <div style={{ padding: 24 }}>
      <h1>404</h1>
      <p>Página no encontrada</p>
      <a href="/">Volver al inicio</a>
    </div>
  )
}

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Suspense fallback={<div style={{ padding: 24 }}>Cargando...</div>}>
        <Routes>
          {/* Públicas */}
          <Route path="/login" element={<LoginPage />} />

          {/* App */}
          <Route path="/" element={<DashboardPage />} />

          {/* Vehículos */}
          <Route path="/vehiculos" element={<VehiculosList />} />
          <Route path="/vehiculos/crear" element={<VehiculosCreate />} />
          <Route path="/vehiculos/:placa" element={<VehiculosDetail />} />

          {/* Clientes */}
          <Route path="/clientes" element={<ClientesList />} />
          <Route path="/clientes/crear" element={<ClientesCreate />} />
          <Route path="/clientes/:id" element={<ClientesDetail />} />

          {/* Facturas */}
          <Route path="/facturas" element={<FacturasList />} />
          <Route path="/facturas/:id" element={<FacturasDetail />} />

          {/* Órdenes */}
          <Route path="/ordenes" element={<OrdenesList />} />
          <Route path="/ordenes/crear" element={<OrdenesCreate />} />
          <Route path="/ordenes/:id" element={<OrdenesDetail />} />

          {/* Repuestos */}
          <Route path="/repuestos" element={<RepuestosList />} />
          <Route path="/repuestos/crear" element={<RepuestosCreate />} />
          <Route path="/repuestos/:id" element={<RepuestosDetail />} />

          {/* Proveedores */}
          <Route path="/proveedores" element={<ProveedoresList />} />
          <Route path="/proveedores/crear" element={<ProveedoresCreate />} />
          <Route path="/proveedores/:id" element={<ProveedoresDetail />} />

          {/* Servicios */}
          <Route path="/servicios" element={<ServiciosList />} />
          <Route path="/servicios/crear" element={<ServiciosCreate />} />
          <Route path="/servicios/:id" element={<ServiciosDetail />} />

          {/* Mecánicos */}
          <Route path="/mecanicos" element={<MecanicosList />} />
          <Route path="/mecanicos/crear" element={<MecanicosCreate />} />
          <Route path="/mecanicos/:id" element={<MecanicosDetail />} />

          {/* Redirecciones comunes */}
          <Route path="/home" element={<Navigate to="/" replace />} />

          {/* 404 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Suspense>
    </BrowserRouter>
  )
}