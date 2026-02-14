import { useState, useEffect } from 'react';
import { employeesApi, proceduresApi, schedulesApi, appointmentsApi, timeOffsApi } from '../../api/endpoints';
import './Admin.css';

const DAYS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

function formatTime(t) {
  if (!t) return '';
  const s = String(t);
  return s.length >= 5 ? s.slice(0, 5) : s;
}

function formatDateTime(dt) {
  if (!dt) return '';
  const d = new Date(dt);
  return d.toLocaleString(undefined, { dateStyle: 'medium', timeStyle: 'short' });
}

export default function AdminEmployees() {
  const [employees, setEmployees] = useState([]);
  const [procedures, setProcedures] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ name: '', email: '', phone: '', procedureIds: [] });
  const [expandedId, setExpandedId] = useState(null);
  const [schedules, setSchedules] = useState({});
  const [appointments, setAppointments] = useState({});
  const [timeOffs, setTimeOffs] = useState({});
  const [viewDate, setViewDate] = useState({});
  const [timeOffForm, setTimeOffForm] = useState({ employeeId: null, startDate: '', startTime: '09:00', endDate: '', endTime: '17:00', reason: '' });

  const load = () => {
    Promise.all([employeesApi.getAll(), proceduresApi.getAll()])
      .then(([emps, procs]) => {
        setEmployees(emps);
        setProcedures(procs);
      })
      .catch((err) => setError(err.data?.message || err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const loadSchedule = async (employeeId) => {
    try {
      const list = await schedulesApi.getForEmployee(employeeId);
      setSchedules((prev) => ({ ...prev, [employeeId]: list }));
    } catch {
      setSchedules((prev) => ({ ...prev, [employeeId]: [] }));
    }
  };

  const loadAppointments = async (employeeId, date) => {
    if (!date) return;
    try {
      const list = await appointmentsApi.getForEmployee(employeeId, date);
      setAppointments((prev) => ({ ...prev, [`${employeeId}-${date}`]: list }));
    } catch {
      setAppointments((prev) => ({ ...prev, [`${employeeId}-${date}`]: [] }));
    }
  };

  const loadTimeOffs = async (employeeId) => {
    try {
      const list = await timeOffsApi.getForEmployee(employeeId);
      setTimeOffs((prev) => ({ ...prev, [employeeId]: list }));
    } catch {
      setTimeOffs((prev) => ({ ...prev, [employeeId]: [] }));
    }
  };

  const toggleExpand = (emp) => {
    const id = emp.id;
    if (expandedId === id) {
      setExpandedId(null);
    } else {
      setExpandedId(id);
      loadSchedule(id);
      loadTimeOffs(id);
    }
  };

  const openTimeOffForm = (employeeId) => {
    const today = new Date().toISOString().slice(0, 10);
    setTimeOffForm({ employeeId, startDate: today, startTime: '09:00', endDate: today, endTime: '17:00', reason: '' });
  };

  const handleTimeOffSubmit = async (e) => {
    e.preventDefault();
    if (!timeOffForm.employeeId || !timeOffForm.startDate || !timeOffForm.endDate || !timeOffForm.reason) return;
    setError('');
    try {
      const dto = {
        employeeId: timeOffForm.employeeId,
        startDateTime: `${timeOffForm.startDate}T${timeOffForm.startTime}:00`,
        endDateTime: `${timeOffForm.endDate}T${timeOffForm.endTime}:00`,
        reason: timeOffForm.reason.trim(),
      };
      await timeOffsApi.create(dto);
      loadTimeOffs(timeOffForm.employeeId);
      setTimeOffForm({ employeeId: null });
    } catch (err) {
      setError(err.data?.message || err.message);
    }
  };

  const handleTimeOffDelete = async (id, employeeId) => {
    if (!confirm('Remove this time off?')) return;
    try {
      await timeOffsApi.remove(id);
      loadTimeOffs(employeeId);
    } catch (err) {
      setError(err.data?.message || err.message);
    }
  };

  const handleDateChange = (employeeId, date) => {
    setViewDate((prev) => ({ ...prev, [employeeId]: date }));
    if (date) loadAppointments(employeeId, date);
  };

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', email: '', phone: '', procedureIds: [] });
  };

  const openEdit = (e) => {
    setEditing(e.id);
    setForm({
      name: e.name,
      email: e.email,
      phone: e.phone,
      procedureIds: e.procedureIds || [],
    });
  };

  const handleSubmit = async (ev) => {
    ev.preventDefault();
    setError('');
    try {
      const payload = { ...form, procedureIds: form.procedureIds.filter(Boolean) };
      if (editing) {
        await employeesApi.update(editing, payload);
      } else {
        await employeesApi.create(payload);
      }
      setEditing(null);
      load();
    } catch (err) {
      setError(err.data?.message || (typeof err.data === 'object' ? JSON.stringify(err.data) : err.message));
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Delete this employee?')) return;
    try {
      await employeesApi.remove(id);
      load();
    } catch (err) {
      setError(err.data?.message || err.message);
    }
  };

  const toggleProcedure = (procId) => {
    const ids = form.procedureIds || [];
    const next = ids.includes(procId) ? ids.filter((i) => i !== procId) : [...ids, procId];
    setForm({ ...form, procedureIds: next });
  };

  if (loading) return <p className="loading">Loading…</p>;

  return (
    <section className="admin-page">
      <h2>Manage Employees</h2>
      {error && <p className="error">{error}</p>}

      <div className="admin-layout">
        <div className="admin-form-card">
          <h3>{editing ? 'Edit' : 'Add'} Employee</h3>
          <form onSubmit={handleSubmit}>
            <label>Name <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required /></label>
            <label>Email <input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required /></label>
            <label>Phone <input value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} required placeholder="7-15 digits" /></label>
            <label>
              Procedures
              <div className="checkbox-list">
                {procedures.map((p) => (
                  <label key={p.id} className="checkbox-row">
                    <input
                      type="checkbox"
                      checked={(form.procedureIds || []).includes(p.id)}
                      onChange={() => toggleProcedure(p.id)}
                    />
                    {p.name}
                  </label>
                ))}
              </div>
            </label>
            <div className="form-actions">
              <button type="submit" className="btn btn-primary">{editing ? 'Update' : 'Create'}</button>
              {editing && <button type="button" className="btn btn-outline" onClick={openCreate}>Cancel</button>}
            </div>
          </form>
        </div>
        <div className="admin-list admin-employees-list">
          <button onClick={openCreate} className="btn btn-outline btn-sm mb">+ New Employee</button>
          {employees.map((e) => (
            <div key={e.id} className="admin-employee-block">
              <div className="admin-row admin-employee-row">
                <span><strong>{e.name}</strong></span>
                <span>{e.email}</span>
                <div>
                  <button className="btn-link" onClick={() => toggleExpand(e)}>
                    {expandedId === e.id ? '▼ Hide' : '▶ Schedule, Time Off & Appointments'}
                  </button>
                  <button className="btn-link" onClick={() => openEdit(e)}>Edit</button>
                  <button className="btn-link danger" onClick={() => handleDelete(e.id)}>Delete</button>
                </div>
              </div>
              {expandedId === e.id && (
                <div className="employee-details">
                  <div className="employee-schedule">
                    <h4>Weekly Schedule</h4>
                    {schedules[e.id] === undefined ? (
                      <p className="muted">Loading…</p>
                    ) : schedules[e.id]?.length ? (
                      <div className="schedule-grid">
                        {DAYS.map((day, i) => {
                          const dayNum = i + 1;
                          const entry = schedules[e.id]?.find((s) => s.dayOfWeek === dayNum);
                          return (
                            <div key={day} className="schedule-day">
                              <span className="day-name">{day}</span>
                              {entry ? (
                                <span className="day-times">{formatTime(entry.startTime)} – {formatTime(entry.endTime)}</span>
                              ) : (
                                <span className="day-off">Off</span>
                              )}
                            </div>
                          );
                        })}
                      </div>
                    ) : (
                      <p className="muted">No schedule set.</p>
                    )}
                  </div>
                  <div className="employee-timeoffs">
                    <h4>Days Off</h4>
                    {timeOffs[e.id] === undefined ? (
                      <p className="muted">Loading…</p>
                    ) : (
                      <>
                        {timeOffs[e.id]?.length ? (
                          <ul className="timeoff-list">
                            {timeOffs[e.id].map((to) => (
                              <li key={to.id}>
                                {formatDateTime(to.startDateTime)} – {formatDateTime(to.endDateTime)}
                                <span className="timeoff-reason"> ({to.reason})</span>
                                <button type="button" className="btn-link danger btn-sm" onClick={() => handleTimeOffDelete(to.id, e.id)}>Remove</button>
                              </li>
                            ))}
                          </ul>
                        ) : (
                          <p className="muted">No days off.</p>
                        )}
                        {timeOffForm.employeeId === e.id ? (
                          <form onSubmit={handleTimeOffSubmit} className="timeoff-form">
                            <label><span>Start</span><input type="date" value={timeOffForm.startDate} onChange={(ev) => setTimeOffForm({ ...timeOffForm, startDate: ev.target.value })} required /></label>
                            <label><span>Start time</span><input type="time" value={timeOffForm.startTime} onChange={(ev) => setTimeOffForm({ ...timeOffForm, startTime: ev.target.value })} /></label>
                            <label><span>End</span><input type="date" value={timeOffForm.endDate} onChange={(ev) => setTimeOffForm({ ...timeOffForm, endDate: ev.target.value })} required /></label>
                            <label><span>End time</span><input type="time" value={timeOffForm.endTime} onChange={(ev) => setTimeOffForm({ ...timeOffForm, endTime: ev.target.value })} /></label>
                            <label><span>Reason</span><input type="text" value={timeOffForm.reason} onChange={(ev) => setTimeOffForm({ ...timeOffForm, reason: ev.target.value })} placeholder="e.g. Vacation" minLength={3} maxLength={255} required /></label>
                            <div className="form-actions">
                              <button type="submit" className="btn btn-primary btn-sm">Add</button>
                              <button type="button" className="btn btn-outline btn-sm" onClick={() => setTimeOffForm({ employeeId: null })}>Cancel</button>
                            </div>
                          </form>
                        ) : (
                          <button type="button" className="btn btn-outline btn-sm" onClick={() => openTimeOffForm(e.id)}>+ Add day off</button>
                        )}
                      </>
                    )}
                  </div>
                  <div className="employee-appointments">
                    <h4>Appointments for a Day</h4>
                    <label>
                      <span>Pick date:</span>
                      <input
                        type="date"
                        value={viewDate[e.id] || ''}
                        onChange={(ev) => handleDateChange(e.id, ev.target.value)}
                      />
                    </label>
                    {viewDate[e.id] && (
                      <div className="appointments-for-day">
                        {appointments[`${e.id}-${viewDate[e.id]}`] === undefined ? (
                          <p className="muted">Loading…</p>
                        ) : appointments[`${e.id}-${viewDate[e.id]}`]?.length ? (
                          <ul>
                            {appointments[`${e.id}-${viewDate[e.id]}`].map((a) => (
                              <li key={a.id}>
                                {formatDateTime(a.startTime)} – {a.procedureName} ({a.username})
                                {a.status === 'CANCELLED' && <span className="badge cancelled">Cancelled</span>}
                              </li>
                            ))}
                          </ul>
                        ) : (
                          <p className="muted">No appointments on this day.</p>
                        )}
                      </div>
                    )}
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
