# BookMyCut Frontend

React SPA for the BookMyCut barber booking API.

## Setup

```bash
npm install
```

## Development

```bash
npm run dev
```

Runs on http://localhost:5173. API requests are proxied to http://localhost:8080 (ensure the Spring Boot backend is running).

## Build

```bash
npm run build
```

Outputs to `dist/`. Serve with any static file server.

## Environment

- `VITE_API_URL` – API base URL (default: `/api` for dev proxy; set to full URL for production, e.g. `https://api.example.com`).
