import './Dashboard.css';

const Dashboard = () => {
  return (
    <div className="dashboard-page">
      <h1>Dashboard</h1>
      
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">🚗</div>
          <div className="stat-content">
            <h3>Vehículos</h3>
            <p className="stat-value">45</p>
            <p className="stat-label">Total registrados</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">📋</div>
          <div className="stat-content">
            <h3>Órdenes Activas</h3>
            <p className="stat-value">12</p>
            <p className="stat-label">En proceso</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">👨‍🔧</div>
          <div className="stat-content">
            <h3>Mecánicos</h3>
            <p className="stat-value">8</p>
            <p className="stat-label">Disponibles</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">💰</div>
          <div className="stat-content">
            <h3>Facturación</h3>
            <p className="stat-value">$2.5M</p>
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
