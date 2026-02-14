import { useState, useEffect } from 'react';
import {
  proceduresApi,
  employeesApi,
  availabilityApi,
  appointmentsApi,
} from '../api/endpoints';
import './Book.css';

function toDateTimeString(dateStr, timeStr) {
  const t = String(timeStr);
  const normalized = t.length <= 5 ? `${t}:00` : t.slice(0, 8);
  return `${dateStr}T${normalized}`;
}

export default function Book() {
  const [procedures, setProcedures] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [slots, setSlots] = useState([]);
  const [step, setStep] = useState(1);
  const [selectedProcedure, setSelectedProcedure] = useState(null);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [selectedDate, setSelectedDate] = useState('');
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    proceduresApi.getAll()
      .then(setProcedures)
      .catch((err) => setError(err.data?.message || err.message));
  }, []);

  useEffect(() => {
    if (!selectedProcedure) {
      setEmployees([]);
      return;
    }
    setLoading(true);
    employeesApi.getByProcedure(selectedProcedure.id)
      .then(setEmployees)
      .catch((err) => setError(err.data?.message || err.message))
      .finally(() => setLoading(false));
  }, [selectedProcedure]);

  useEffect(() => {
    if (!selectedEmployee || !selectedProcedure || !selectedDate) {
      setSlots([]);
      return;
    }
    setLoading(true);
    availabilityApi.getSlots(
      selectedEmployee.id,
      selectedDate,
      selectedProcedure.durationInMinutes
    )
      .then(setSlots)
      .catch((err) => setError(err.data?.message || err.message))
      .finally(() => setLoading(false));
  }, [selectedEmployee, selectedProcedure, selectedDate]);

  const minDate = new Date().toISOString().slice(0, 10);

  const handleBook = async () => {
    if (!selectedProcedure || !selectedEmployee || !selectedSlot) return;
    setError('');
    const startDateTime = toDateTimeString(selectedDate, selectedSlot);
    try {
      await appointmentsApi.book({
        employeeId: selectedEmployee.id,
        procedureId: selectedProcedure.id,
        startDateTime,
      });
      setSuccess(true);
    } catch (err) {
      setError(err.data?.message || err.message || 'Booking failed');
    }
  };

  const reset = () => {
    setStep(1);
    setSelectedProcedure(null);
    setSelectedEmployee(null);
    setSelectedDate('');
    setSelectedSlot(null);
    setSuccess(false);
    setError('');
  };

  if (success) {
    return (
      <section className="book-page">
        <div className="success-card">
          <h2>Booked!</h2>
          <p>Your appointment with {selectedEmployee?.name} for {selectedProcedure?.name} is confirmed.</p>
          <button onClick={reset} className="btn btn-primary">Book Another</button>
        </div>
      </section>
    );
  }

  return (
    <section className="book-page">
      <h2>Book an Appointment</h2>
      {error && <p className="error">{error}</p>}

      <div className="book-steps">
        <div className={`step ${step >= 1 ? 'active' : ''}`}>1. Service</div>
        <div className={`step ${step >= 2 ? 'active' : ''}`}>2. Barber</div>
        <div className={`step ${step >= 3 ? 'active' : ''}`}>3. Date & Time</div>
      </div>

      {step === 1 && (
        <div className="book-section">
          <h3>Select a service</h3>
          <div className="procedure-select">
            {procedures.map((p) => (
              <button
                key={p.id}
                className={`procedure-option ${selectedProcedure?.id === p.id ? 'selected' : ''}`}
                onClick={() => {
                  setSelectedProcedure(p);
                  setStep(2);
                }}
              >
                <span className="name">{p.name}</span>
                <span className="meta">{p.durationInMinutes} min · ${p.price.toFixed(2)}</span>
              </button>
            ))}
          </div>
        </div>
      )}

      {step === 2 && (
        <div className="book-section">
          <button className="back" onClick={() => setStep(1)}>← Back</button>
          <h3>Select your barber</h3>
          {loading && <p>Loading…</p>}
          <div className="employee-select">
            {employees.map((e) => (
              <button
                key={e.id}
                className={`employee-option ${selectedEmployee?.id === e.id ? 'selected' : ''}`}
                onClick={() => {
                  setSelectedEmployee(e);
                  setStep(3);
                }}
              >
                <span className="name">{e.name}</span>
                <span className="meta">{e.email}</span>
              </button>
            ))}
          </div>
          {employees.length === 0 && !loading && (
            <p className="empty">No barbers available for this service.</p>
          )}
        </div>
      )}

      {step === 3 && (
        <div className="book-section">
          <button className="back" onClick={() => setStep(2)}>← Back</button>
          <h3>Pick date & time</h3>
          <label>
            Date
            <input
              type="date"
              value={selectedDate}
              min={minDate}
              onChange={(e) => setSelectedDate(e.target.value)}
            />
          </label>
          {selectedDate && (
            <>
              {loading && <p>Loading slots…</p>}
              <div className="slot-grid">
                {slots.map((s) => (
                  <button
                    key={s}
                    className={`slot-option ${selectedSlot === s ? 'selected' : ''}`}
                    onClick={() => setSelectedSlot(s)}
                  >
                    {String(s).slice(0, 5)}
                  </button>
                ))}
              </div>
              {slots.length === 0 && !loading && (
                <p className="empty">No available slots on this date.</p>
              )}
            </>
          )}
          {selectedSlot && (
            <div className="book-actions">
              <button onClick={handleBook} className="btn btn-primary btn-lg" disabled={loading}>
                Confirm Booking
              </button>
            </div>
          )}
        </div>
      )}
    </section>
  );
}
