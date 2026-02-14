import { useState, useRef, useEffect } from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Layout.css';

export default function Layout() {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [adminOpen, setAdminOpen] = useState(false);
  const adminRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (adminRef.current && !adminRef.current.contains(e.target)) {
        setAdminOpen(false);
      }
    };
    document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="layout">
      <header className="header">
        <Link to="/" className="logo">
          <span className="logo-icon">✂</span>
          BookMyCut
        </Link>
        <nav className="nav">
          <Link to="/procedures">Procedures</Link>
          {user ? (
            <>
              <Link to="/book">Book</Link>
              <Link to="/my-appointments">My Appointments</Link>
              {isAdmin && (
                <div className="admin-dropdown" ref={adminRef}>
                  <button
                    type="button"
                    className="admin-trigger"
                    onClick={(e) => { e.stopPropagation(); setAdminOpen((o) => !o); }}
                    aria-expanded={adminOpen}
                  >
                    Admin ▾
                  </button>
                  {adminOpen && (
                    <div className="admin-menu">
                      <Link to="/admin/procedures" onClick={() => setAdminOpen(false)}>Procedures</Link>
                      <Link to="/admin/employees" onClick={() => setAdminOpen(false)}>Employees</Link>
                      <Link to="/admin/schedules" onClick={() => setAdminOpen(false)}>Schedules</Link>
                    </div>
                  )}
                </div>
              )}
              <button onClick={handleLogout} className="btn btn-logout">
                Logout
              </button>
            </>
          ) : (
            <div className="nav-guest">
              <Link to="/login">Login</Link>
              <Link to="/register" className="btn btn-signup">
                Sign Up
              </Link>
            </div>
          )}
        </nav>
      </header>
      <main className="main">
        <Outlet />
      </main>
      <footer className="footer">
        <span>BookMyCut</span> · Barber booking made simple
      </footer>
    </div>
  );
}
