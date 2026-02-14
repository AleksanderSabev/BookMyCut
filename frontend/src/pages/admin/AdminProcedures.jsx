import { useState, useEffect } from 'react';
import { proceduresApi } from '../../api/endpoints';
import './Admin.css';

export default function AdminProcedures() {
  const [procedures, setProcedures] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ name: '', durationInMinutes: 30, price: 0 });

  const load = () => {
    proceduresApi.getAll()
      .then(setProcedures)
      .catch((err) => setError(err.data?.message || err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', durationInMinutes: 30, price: 0 });
  };

  const openEdit = (p) => {
    setEditing(p.id);
    setForm({ name: p.name, durationInMinutes: p.durationInMinutes, price: p.price });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      if (editing) {
        await proceduresApi.update(editing, form);
      } else {
        await proceduresApi.create(form);
      }
      setEditing(null);
      load();
    } catch (err) {
      setError(err.data?.message || (typeof err.data === 'object' ? JSON.stringify(err.data) : err.message));
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Delete this procedure?')) return;
    try {
      await proceduresApi.remove(id);
      load();
    } catch (err) {
      setError(err.data?.message || err.message);
    }
  };

  if (loading) return <p className="loading">Loading…</p>;

  return (
    <section className="admin-page">
      <h2>Manage Procedures</h2>
      {error && <p className="error">{error}</p>}

      <div className="admin-layout">
        <div className="admin-form-card">
          <h3>{editing ? 'Edit' : 'Add'} Procedure</h3>
          <form onSubmit={handleSubmit}>
            <label>
              Name
              <input
                value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })}
                required
              />
            </label>
            <label>
              Duration (minutes)
              <input
                type="number"
                min={1}
                value={form.durationInMinutes}
                onChange={(e) => setForm({ ...form, durationInMinutes: parseInt(e.target.value, 10) || 0 })}
              />
            </label>
            <label>
              Price
              <input
                type="number"
                min={0}
                step={0.01}
                value={form.price}
                onChange={(e) => setForm({ ...form, price: parseFloat(e.target.value) || 0 })}
              />
            </label>
            <div className="form-actions">
              <button type="submit" className="btn btn-primary">{editing ? 'Update' : 'Create'}</button>
              {editing && (
                <button type="button" className="btn btn-outline" onClick={openCreate}>Cancel</button>
              )}
            </div>
          </form>
        </div>

        <div className="admin-list">
          <button onClick={openCreate} className="btn btn-outline btn-sm mb">+ New Procedure</button>
          {procedures.map((p) => (
            <div key={p.id} className="admin-row">
              <span>{p.name}</span>
              <span>{p.durationInMinutes} min · ${p.price?.toFixed(2)}</span>
              <div>
                <button className="btn-link" onClick={() => openEdit(p)}>Edit</button>
                <button className="btn-link danger" onClick={() => handleDelete(p.id)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
