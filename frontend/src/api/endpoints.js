import { api } from './client';

export const authApi = {
  register: (dto) => api.post('/auth/register', dto, { skipAuth: true }),
  login: (dto) => api.post('/auth/login', dto, { skipAuth: true }),
  logout: () => api.post('/auth/logout', { skipAuth: true }),
};

export const proceduresApi = {
  getAll: () => api.get('/procedures'),
  getById: (id) => api.get(`/procedures/${id}`),
  create: (dto) => api.post('/procedures', dto),
  update: (id, dto) => api.put(`/procedures/${id}`, dto),
  remove: (id) => api.delete(`/procedures/${id}`),
};

export const employeesApi = {
  getAll: () => api.get('/employees'),
  getById: (id) => api.get(`/employees/${id}`),
  getByProcedure: (procedureId) => api.get(`/employees/by-procedure/${procedureId}`),
  getWorkingOn: (date) => api.get(`/employees/working-on?date=${date}`),
  search: (name) => api.get(`/employees/search?name=${encodeURIComponent(name)}`),
  getProcedures: (employeeId) => api.get(`/employees/${employeeId}/procedures`),
  create: (dto) => api.post('/employees', dto),
  update: (id, dto) => api.put(`/employees/${id}`, dto),
  remove: (id) => api.delete(`/employees/${id}`),
  assignProcedure: (employeeId, procedureId) =>
    api.post(`/employees/${employeeId}/procedures/${procedureId}`),
};

export const availabilityApi = {
  getSlots: (employeeId, date, durationMinutes) =>
    api.get(`/availability/slots?employeeId=${employeeId}&date=${date}&durationMinutes=${durationMinutes}`),
};

export const appointmentsApi = {
  book: (dto) => api.post('/appointments', dto),
  cancel: (id) => api.delete(`/appointments/${id}`),
  getMyAppointments: () => api.get('/appointments/my-appointments'),
  getForEmployee: (employeeId, date) =>
    api.get(`/appointments/employee/${employeeId}?date=${date || new Date().toISOString().slice(0, 10)}`),
};

export const schedulesApi = {
  getAll: () => api.get('/schedules'),
  getForEmployee: (employeeId) => api.get(`/schedules/employee/${employeeId}`),
  create: (dto) => api.post('/schedules', dto),
  update: (dto) => api.put('/schedules', dto),
  remove: (employeeId, day) => api.delete(`/schedules/${employeeId}/${day}`),
};

export const timeOffsApi = {
  getForEmployee: (employeeId) => api.get(`/time-offs/employee/${employeeId}`),
  create: (dto) => api.post('/time-offs', dto),
  update: (id, dto) => api.put(`/time-offs/${id}`, dto),
  remove: (id) => api.delete(`/time-offs/${id}`),
};

export const usersApi = {
  getMe: () => api.get('/users/me'),
};
