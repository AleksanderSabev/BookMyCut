import { Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Procedures from './pages/Procedures';
import Book from './pages/Book';
import MyAppointments from './pages/MyAppointments';
import AdminProcedures from './pages/admin/AdminProcedures';
import AdminEmployees from './pages/admin/AdminEmployees';
import AdminSchedules from './pages/admin/AdminSchedules';
import { useAuth } from './context/AuthContext';

function ProtectedRoute({ children, adminOnly }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="loading">Loading…</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (adminOnly && user.role !== 'ADMIN') return <Navigate to="/" replace />;
  return children;
}

function GuestRoute({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="loading">Loading…</div>;
  if (user) return <Navigate to="/" replace />;
  return children;
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="login" element={<GuestRoute><Login /></GuestRoute>} />
        <Route path="register" element={<GuestRoute><Register /></GuestRoute>} />
        <Route path="procedures" element={<Procedures />} />
        <Route path="book" element={<ProtectedRoute><Book /></ProtectedRoute>} />
        <Route path="my-appointments" element={<ProtectedRoute><MyAppointments /></ProtectedRoute>} />
        <Route path="admin/procedures" element={<ProtectedRoute adminOnly><AdminProcedures /></ProtectedRoute>} />
        <Route path="admin/employees" element={<ProtectedRoute adminOnly><AdminEmployees /></ProtectedRoute>} />
        <Route path="admin/schedules" element={<ProtectedRoute adminOnly><AdminSchedules /></ProtectedRoute>} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
