# NexusHR – AI-Enabled Enterprise HR & Workforce Intelligence Platform

NexusHR is a full-stack HR and workforce management platform built as part of the Zidio Development internship program. It streamlines core HR operations — employee lifecycle, attendance, leave, payroll, and performance management — and adds AI-driven workforce insights on top, giving managers a single dashboard to track and act on people-data in real time.

## Live Demo

- **Frontend (Vercel):** https://nexushr-frontend-ebeqjtdtq-yeshoda.vercel.app
- **Backend API (Render):** https://nexushr-backend-lyae.onrender.com

> Note: The backend is hosted on Render's free tier, which spins down after periods of inactivity. The first request after idle time may take 30–50 seconds to respond while the server wakes up — this is expected behavior, not a bug.

## Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | Authentication & RBAC | JWT-based login/signup with role-based access control (Admin / Manager / Employee) |
| 2 | Employee Management | Full CRUD for employee records, departments, and roles |
| 3 | Attendance Tracking | Daily check-in / check-out with attendance history |
| 4 | Leave Management | Leave requests, approval workflow, and balance tracking |
| 5 | Payroll Processing | Salary structure, automated payroll calculation, payslip generation |
| 6 | Performance Management | Goal setting, manager feedback, and performance ratings |
| 7 | AI-Powered Workforce Insights | Rule-based attrition risk scoring using attendance, leave, and performance data |
| 8 | Role-Based Dashboards | Separate dashboard views and permissions for Admin, Manager, and Employee roles |

## Tech Stack

**Backend**
- Java + Spring Boot
- Spring Security with JWT authentication
- Spring Data JPA
- PostgreSQL (hosted on Neon)
- Deployed via Docker on Render

**Frontend**
- React + TypeScript
- Vite
- Tailwind CSS
- Deployed on Vercel

## Project Structure

```
nexushr-fullstack/
├── backend/      # Spring Boot REST API
├── frontend/     # React + TypeScript client
└── README.md
```

> The backend and frontend are deployed as separate services (Render requires a dedicated repo/service for the backend, Vercel for the frontend), so they are maintained as independent applications within this combined repository for submission purposes.

## Getting Started Locally

### Backend
```bash
cd backend
# configure src/main/resources/application.properties with your PostgreSQL credentials
mvn spring-boot:run
```
Backend runs on `http://localhost:8080`

### Frontend
```bash
cd frontend
npm install
npm run dev
```
Frontend runs on `http://localhost:5173`

## Future Roadmap

- Migrate from monolith to microservices architecture
- Container orchestration with Kubernetes
- Cloud deployment on AWS EKS
- Real-time notifications via WebSockets
- Expanded AI insights using Spring AI / ML models
- Monitoring and observability with Prometheus & Grafana

## Author

Yeshoda Gantyada — Zidio Development Internship Project
