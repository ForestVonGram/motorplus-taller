import { type ReactNode } from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Layout.css';

interface LayoutProps {
  children: ReactNode;
}

const Layout = ({ children }: LayoutProps) => {
  const location = useLocation();

  const isActive = (path: string) => location.pathname.startsWith(path);

  const menuItems = [
    { path: '/dashboard', label: 'Dashboard', icon: '📊' },
    { path: '/vehiculos', label: 'Vehículos', icon: '🚗' },
    { path: '/clientes', label: 'Clientes', icon: '👥' },
    { path: '/mecanicos', label: 'Mecánicos', icon: '👨‍🔧' },
    { path: '/ordenes', label: 'Órdenes', icon: '📋' },
    { path: '/facturas', label: 'Facturas', icon: '💰' },
    { path: '/repuestos', label: 'Repuestos', icon: '🔧' },
    { path: '/proveedores', label: 'Proveedores', icon: '🏭' },
    { path: '/servicios', label: 'Servicios', icon: '⚙️' },
    { path: '/tipos-servicio', label: 'Tipos de servicio', icon: '🏷️' },
    { path: '/reportes', label: 'Reportes', icon: '📈' },
  ];

  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h2>🔧 MotorPlus</h2>
        </div>
        <nav className="sidebar-nav">
          {menuItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`nav-item ${isActive(item.path) ? 'active' : ''}`}
            >
              <span className="nav-icon">{item.icon}</span>
              <span className="nav-label">{item.label}</span>
            </Link>
          ))}
        </nav>
      </aside>
      <main className="main-content">{children}</main>
    </div>
  );
};

export default Layout;
