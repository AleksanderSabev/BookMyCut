import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { proceduresApi } from '../api/endpoints';
import './Procedures.css';

export default function Procedures() {
  const [procedures, setProcedures] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    proceduresApi.getAll()
      .then(setProcedures)
      .catch((err) => setError(err.data?.message || err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="loading">Loading procedures…</p>;
  if (error) {
    const isAuth = error.includes('Authentication') || error.includes('401');
    return (
      <section className="procedures-page">
        <p className="error">{error}</p>
        {isAuth && <p><Link to="/login">Log in</Link> to view procedures and book appointments.</p>}
      </section>
    );
  }

  return (
    <section className="procedures-page">
      <h2>Services</h2>
      <p className="procedures-intro">
        Choose from our range of barbering and grooming services.
      </p>
      <div className="procedure-grid">
        {procedures.map((p) => (
          <article key={p.id} className="procedure-card">
            <h3>{p.name}</h3>
            <p className="procedure-duration">{p.durationInMinutes} min</p>
            <p className="procedure-price">${p.price.toFixed(2)}</p>
          </article>
        ))}
      </div>
      {procedures.length === 0 && (
        <p className="empty">No procedures available yet.</p>
      )}
    </section>
  );
}
