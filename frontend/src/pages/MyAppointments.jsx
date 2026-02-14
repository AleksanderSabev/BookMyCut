import { useState, useEffect } from 'react';
import { appointmentsApi } from '../api/endpoints';
import './MyAppointments.css';

function formatDateTime(dt) {
  if (!dt) return '';
  const d = new Date(dt);
  return d.toLocaleString(undefined, {
    dateStyle: 'medium',
    timeStyle: 'short',
  });
}

export default function MyAppointments() {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = () => {
    setLoading(true);
    appointmentsApi.getMyAppointments()
      .then(setAppointments)
      .catch((err) => setError(err.data?.message || err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const handleCancel = async (id) => {
    if (!confirm('Cancel this appointment?')) return;
    try {
      await appointmentsApi.cancel(id);
      load();
    } catch (err) {
      setError(err.data?.message || err.message);
    }
  };

  const scheduled = appointments.filter((a) => a.status === 'SCHEDULED');

  if (loading) return <p className="loading">Loading appointments…</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <section className="my-appointments">
      <h2>My Appointments</h2>
      {scheduled.length === 0 ? (
        <p className="empty">No upcoming appointments.</p>
      ) : (
        <div className="appointment-list">
          {scheduled.map((a) => (
            <article key={a.id} className="appointment-card">
              <div className="appointment-info">
                <h3>{a.procedureName}</h3>
                <p>with {a.employeeName}</p>
                <p className="datetime">{formatDateTime(a.startTime)}</p>
              </div>
              <button
                onClick={() => handleCancel(a.id)}
                className="btn btn-outline btn-sm"
              >
                Cancel
              </button>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}
