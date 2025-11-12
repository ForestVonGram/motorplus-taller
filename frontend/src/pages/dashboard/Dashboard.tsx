import { useEffect, useState } from 'react';
import './Dashboard.css';

interface DashboardStats {
  totalVehiculos: number;
  ordenesActivas: number;
  mecanicosDisponibles: number;
  facturacionMes: number;
}

const Dashboard = () => {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDashboardStats = async () => {
      try {
        setLoading(true);
        const response = await fetch('http://localhost:7001/api/dashboard/stats');
        
        if (!response.ok) {
          throw new Error('Error al obtener las estadísticas');
        }
        
        const data = await response.json();
        setStats(data);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Error desconocido');
        console.error('Error fetching dashboard stats:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardStats();
  }, []);

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(value);
  };

  if (loading) {
    return (
      <div className="dashboard-page">
        <h1>Dashboard</h1>
        <div className="loading-message">Cargando estadísticas...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard-page">
        <h1>Dashboard</h1>
        <div className="error-message">
          <p>Error: {error}</p>
          <p>Asegúrate de que el servidor backend esté ejecutándose en http://localhost:7001</p>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-page">
      <h1>Dashboard</h1>
      
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">🚗</div>
          <div className="stat-content">
            <h3>Vehículos</h3>
            <p className="stat-value">{stats?.totalVehiculos ?? 0}</p>
            <p className="stat-label">Total registrados</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">📋</div>
          <div className="stat-content">
            <h3>Órdenes Activas</h3>
            <p className="stat-value">{stats?.ordenesActivas ?? 0}</p>
            <p className="stat-label">En proceso</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">👨‍🔧</div>
          <div className="stat-content">
            <h3>Mecánicos</h3>
            <p className="stat-value">{stats?.mecanicosDisponibles ?? 0}</p>
            <p className="stat-label">Disponibles</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">💰</div>
          <div className="stat-content">
            <h3>Facturación</h3>
            <p className="stat-value">{stats ? formatCurrency(stats.facturacionMes) : '$0'}</p>
            <p className="stat-label">Este mes</p>
          </div>
        </div>
      </div>
      
      <div className="dashboard-section">
        <h2>Órdenes Recientes</h2>
        <p className="section-placeholder">Lista de órdenes recientes aparecerá aquí</p>
      </div>
    </div>
  );
};

export default Dashboard;
