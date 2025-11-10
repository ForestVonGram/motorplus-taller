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

          {/* Facturas */}
          <Route path="/facturas" element={<FacturasList />} />
          <Route path="/facturas/:id" element={<FacturasDetail />} />

          {/* Órdenes */}
          <Route path="/ordenes" element={<OrdenesList />} />

          {/* Redirecciones comunes */}
          <Route path="/home" element={<Navigate to="/" replace />} />

          {/* 404 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Suspense>
    </BrowserRouter>
  )
}