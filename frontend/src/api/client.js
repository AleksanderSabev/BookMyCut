const API_BASE = import.meta.env.VITE_API_URL || '/api';

function getToken() {
  return localStorage.getItem('jwt');
}

function getHeaders(includeAuth = true) {
  const headers = {
    'Content-Type': 'application/json',
  };
  const token = getToken();
  if (includeAuth && token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return headers;
}

async function handleResponse(res) {
  const text = await res.text();
  let data;
  try {
    data = text ? JSON.parse(text) : null;
  } catch {
    data = text;
  }
  if (!res.ok) {
    const err = new Error(data?.message || data?.error || res.statusText || 'Request failed');
    err.status = res.status;
    err.data = data;
    throw err;
  }
  return data;
}

export const api = {
  get: (path, options = {}) =>
    fetch(`${API_BASE}${path}`, {
      method: 'GET',
      headers: getHeaders(options.skipAuth),
      credentials: 'include',
      ...options,
    }).then(handleResponse),

  post: (path, body, options = {}) =>
    fetch(`${API_BASE}${path}`, {
      method: 'POST',
      headers: getHeaders(options.skipAuth),
      credentials: 'include',
      body: body ? JSON.stringify(body) : undefined,
      ...options,
    }).then(handleResponse),

  put: (path, body) =>
    fetch(`${API_BASE}${path}`, {
      method: 'PUT',
      headers: getHeaders(),
      credentials: 'include',
      body: body ? JSON.stringify(body) : undefined,
    }).then(handleResponse),

  delete: (path) =>
    fetch(`${API_BASE}${path}`, {
      method: 'DELETE',
      headers: getHeaders(),
      credentials: 'include',
    }).then(handleResponse),
};
