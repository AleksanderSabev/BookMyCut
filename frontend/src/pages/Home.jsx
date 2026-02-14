import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Home.css';

export default function Home() {
  const { user } = useAuth();

  return (
    <div className="home-page">
      <section className="hero">
        <div className="hero-badge">Premium Barber Experience</div>
        <h1>Book Your Cut</h1>
        <p className="hero-tagline">
          Barber scheduling styled to the point
        </p>
        <div className="hero-divider" />
        <div className="hero-actions">
          <Link to="/procedures" className="btn btn-primary btn-lg">
            Book Appointment
          </Link>
          {user ? (
            <Link to="/book" className="btn btn-outline btn-lg">
              Book Now
            </Link>
          ) : (
            <Link to="/register" className="btn btn-outline btn-lg">
              Sign Up to Book
            </Link>
          )}
        </div>
      </section>

      <section className="features">
        <h2>Why BookMyCut?</h2>
        <div className="feature-grid">
          <div className="feature-card">
            <span className="feature-icon">✂️</span>
            <h3>Wide Selection</h3>
            <p>Classic cuts, fades, beard trims, hot towel shaves — find the service that fits you.</p>
          </div>
          <div className="feature-card">
            <span className="feature-icon">🕐</span>
            <h3>Real-Time Slots</h3>
            <p>See exactly what's available and book in one click. No more back-and-forth.</p>
          </div>
          <div className="feature-card">
            <span className="feature-icon">👤</span>
            <h3>Choose Your Barber</h3>
            <p>Pick the barber you trust. Each has their own schedule and specialties.</p>
          </div>
        </div>
      </section>

      <section className="how-it-works">
        <h2>How It Works</h2>
        <div className="steps">
          <div className="step-item">
            <span className="step-num">1</span>
            <h3>Pick a Service</h3>
            <p>Browse our menu of cuts and grooming services.</p>
          </div>
          <div className="step-arrow">→</div>
          <div className="step-item">
            <span className="step-num">2</span>
            <h3>Choose Your Barber</h3>
            <p>Select from barbers who offer that service.</p>
          </div>
          <div className="step-arrow">→</div>
          <div className="step-item">
            <span className="step-num">3</span>
            <h3>Pick Date & Time</h3>
            <p>See available slots and confirm your booking.</p>
          </div>
        </div>
      </section>

      <section className="cta">
        <p>Ready to look sharp?</p>
        <Link to={user ? "/book" : "/register"} className="btn btn-primary btn-lg">
          {user ? "Book Your Next Cut" : "Get Started"}
        </Link>
      </section>
    </div>
  );
}
