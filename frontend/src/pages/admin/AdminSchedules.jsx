import { useState, useEffect } from 'react';
import { schedulesApi, employeesApi } from '../../api/endpoints';
import './Admin.css';

const DAYS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
const DAY_NUMS = { MONDAY: 1, TUESDAY: 2, WEDNESDAY: 3, THURSDAY: 4, FRIDAY: 5, SATURDAY: 6, SUNDAY: 7 };

export default function AdminSchedules() {
  const [schedules, setSchedules] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ employeeId: '', dayOfWeek: 1, startTime: '09:00', endTime: '17:00' });

  const load = () => {
    Promise.all([schedulesApi.getAll(), employeesApi.getAll()])
      .then(([scheds, emps]) => {
        setSchedules(scheds);
        setEmployees(emps);
      })
      .catch((err) => setError(err.data?.message || err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await schedulesApi.create({
        employeeId: parseInt(form.employeeId, 10),
        dayOfWeek: form.dayOfWeek,
        startTime: form.startTime,
        endTime: form.endTime,
      });
      setForm({ employeeId: '', dayOfWeek: 1, startTime: '09:00', endTime: '17:00' });
      load();
    } catch (err) {
      if (err.status === 409) {
        try {
          await schedulesApi.update({
            employeeId: parseInt(form.employeeId, 10),
            dayOfWeek: form.dayOfWeek,
            startTime: form.startTime,
            endTime: form.endTime,
          });
          load();
        } catch (e2) {
          setError(e2.data?.message || e2.message);
        }
      } else {
        setError(err.data?.message || (typeof err.data === 'object' ? JSON.stringify(err.data) : err.message));
      }
    }
  };

  const handleDelete = async (employeeId, dayOfWeek) => {
    const day = typeof dayOfWeek === 'number' ? DAYS[dayOfWeek - 1] : String(dayOfWeek).toUpperCase();
    if (!confirm('Remove this schedule entry?')) return;
    try {
      await schedulesApi.remove(employeeId, day);
      load();
    } catch (err) {
      setError(err.data?.message || err.message);
    }
  };

  const empName = (id) => employees.find((e) => e.id === id)?.name || `#${id}`;
  const dayName = (d) => DAYS[d - 1] || d;

  if (loading) return <p className="loading">Loading…</p>;

  return (
    <section className="admin-page">
      <h2>Manage Schedules</h2>
      {error && <p className="error">{error}</p>}

      <div className="admin-layout">
        <div className="admin-form-card">
          <h3>Add / Update Schedule</h3>
          <form onSubmit={handleSubmit}>
            <label>
              Employee
              <select
                value={form.employeeId}
                onChange={(e) => setForm({ ...form, employeeId: e.target.value })}
                required
              >
                <option value="">Select…</option>
                {employees.map((e) => (
                  <option key={e.id} value={e.id}>{e.name}</option>
                ))}
              </select>
            </label>
            <label>
              Day
              <select
                value={form.dayOfWeek}
                onChange={(e) => setForm({ ...form, dayOfWeek: parseInt(e.target.value, 10) })}
              >
                {DAYS.map((d, i) => (
                  <option key={d} value={i + 1}>{d}</option>
                ))}
              </select>
            </label>
            <label>
              Start <input type="time" value={form.startTime} onChange={(e) => setForm({ ...form, startTime: e.target.value })} />
            </label>
            <label>
              End <input type="time" value={form.endTime} onChange={(e) => setForm({ ...form, endTime: e.target.value })} />
            </label>
            <button type="submit" className="btn btn-primary">Save</button>
          </form>
        </div>
        <div className="admin-list">
          {schedules.map((s) => (
            <div key={`${s.employeeId}-${s.dayOfWeek}`} className="admin-row">
              <span><strong>{empName(s.employeeId)}</strong> — {dayName(s.dayOfWeek)}</span>
              <span>{s.startTime} – {s.endTime}</span>
              <button className="btn-link danger" onClick={() => handleDelete(s.employeeId, s.dayOfWeek)}>Delete</button>
            </div>
          ))}
          {schedules.length === 0 && <p className="empty">No schedule entries.</p>}
        </div>
      </div>
    </section>
  );
}
