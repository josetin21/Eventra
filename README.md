# Eventra — Backend

REST API for the Eventra event management platform. Built with Spring Boot, it handles authentication, event lifecycle, attendee registration, QR-based attendance tracking, organizer tools, and an admin approval workflow.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (JJWT 0.12.6) |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Rate Limiting | Bucket4j 8.10.1 |
| Excel Export | Apache POI 5.3.0 |
| QR Generation | ZXing 3.5.3 |
| Email | Spring Mail (Gmail SMTP) |
| Build Tool | Maven |

---

## Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL 15+ (local for development; [Neon](https://neon.tech) is used for the cloud database in production)
- A Gmail account with an [App Password](https://myaccount.google.com/apppasswords) for email sending

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/eventra-backend.git
cd eventra-backend
```

### 2. Create the PostgreSQL database

```sql
CREATE DATABASE eventra;
```

### 3. Set environment variables

The app reads all secrets from environment variables. Set these in your shell, a `.env` file, or your deployment platform's variables panel:

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | JDBC connection string | `jdbc:postgresql://localhost:5432/eventra` |
| `DB_USERNAME` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `yourpassword` |
| `JWT_SECRET` | Long random string (min 256-bit) | `your-very-long-secret-key` |
| `ADMIN_KEY` | Secret key required to register an admin account | `some-secret-admin-key` |
| `CORS_ALLOWED_ORIGINS` | Frontend origin(s) allowed by CORS | `http://localhost:5173` |
| `MAIL_USERNAME` | Gmail address for sending OTP emails | `yourapp@gmail.com` |
| `MAIL_PASSWORD` | Gmail App Password (not your login password) | `xxxx xxxx xxxx xxxx` |

JWT expiration defaults to `86400000` ms (24 hours) and can be overridden in `application.properties`.

### 4. Run

```bash
./mvnw spring-boot:run
```

API starts on `http://localhost:8080`.

---

## Project Structure

```
src/main/java/com/jm/eventra/
├── config/           # CORS and Spring Security configuration
├── controller/       # REST controllers (one per domain)
├── dto/
│   ├── request/      # Incoming request bodies
│   └── response/     # Outgoing response shapes
├── entity/           # JPA entities and enums
├── exception/        # GlobalExceptionHandler + BusinessException
├── mapper/           # Entity ↔ DTO conversion
├── repository/       # Spring Data JPA repositories
├── security/         # JwtService, JwtAuthFilter, UserDetailsService, RateLimitFilter
└── service/          # Business logic layer
```

---

## API Reference

All endpoints are prefixed — no `/api` prefix. Protected routes require:
```
Authorization: Bearer <jwt-token>
```

### Auth — `/auth`

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/auth/register` | Public | Register a new user |
| POST | `/auth/login` | Public | Login, returns JWT |
| POST | `/auth/register/admin` | Public + Admin Key | Register an admin account |
| POST | `/auth/forgot-password/otp` | Public | Request OTP email for password reset |
| POST | `/auth/forgot-password/reset` | Public | Reset password using OTP |

### Events — `/events`

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/events` | Public | List all approved events |
| GET | `/events/{id}` | Public | Get a single event |
| GET | `/events/my` | User | List events created by the logged-in user |
| POST | `/events` | User | Create a new event (starts as PENDING) |
| PUT | `/events/{id}` | Organizer | Update event details |
| DELETE | `/events/{id}` | Organizer | Delete an event |

### Registrations — `/registration`

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/registration/events/{eventId}` | User | Register for an event |
| GET | `/registration/my` | User | List my registrations (includes QR code) |
| DELETE | `/registration/{registrationId}` | User | Cancel a registration |

### Attendance — `/attendance`

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/attendance/sessions/events/{eventId}` | Organizer | Open an attendance session |
| GET | `/attendance/sessions/events/{eventId}` | Organizer | List sessions for an event |
| POST | `/attendance/mark` | User | Mark attendance via QR scan |
| GET | `/attendance/events/{eventId}` | Organizer | View attendance records for an event |
| GET | `/attendance/my` | User | View own attendance history |

### Organizer — Registrant Management

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/events/{eventId}/registrants` | Organizer | List all registrants for an event |
| GET | `/events/{eventId}/registrants/export/csv` | Organizer | Export registrants as CSV |
| GET | `/events/{eventId}/registrants/export/xlsx` | Organizer | Export registrants as Excel |

### User Profile — `/users`

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/users/me` | User | Get own profile |
| PUT | `/users/me` | User | Update profile |
| PUT | `/users/me/password` | User | Change password |

### Admin — `/admin/events`

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/admin/events/pending` | Admin | List events awaiting approval |
| PUT | `/admin/events/{id}/approve` | Admin | Approve an event |
| PUT | `/admin/events/{id}/reject` | Admin | Reject an event (with reason) |

### Other

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/dashboard` | User | Summary stats for the logged-in user |
| GET | `/health` | Public | Health check |

---

## Event Status Flow

```
[Created by user]
       ↓
    PENDING  ──(admin approves)──→  APPROVED
             ──(admin rejects)──→  REJECTED
```

Only `APPROVED` events appear on the public listing.

---

## QR Code Attendance

Each successful registration generates a unique QR code. Two fields are stored:

- **`qrCode`** — Base64-encoded PNG image (for display in the frontend)
- **`qrContent`** — Raw string payload (scanned by the QR reader)

Scanning calls `POST /attendance/mark` with the `qrContent` value to record attendance against an open session.

---

## Rate Limiting

Sensitive endpoints (login, registration, OTP request) are protected by `SensitiveEndPointsRateLimitFilter` using Bucket4j to limit brute-force attempts.

---

## Running Tests

```bash
./mvnw test
```

---

## Deployment

This project uses **Railway** for the backend and **Neon** for the cloud PostgreSQL database.

### Database (Neon)

1. Create a free project at [neon.tech](https://neon.tech).
2. Copy the **connection string** from the Neon dashboard — it looks like:
   ```
   postgresql://user:password@ep-xxx.region.aws.neon.tech/eventra?sslmode=require
   ```
3. Convert it to a JDBC URL for Spring Boot:
   ```
   jdbc:postgresql://ep-xxx.region.aws.neon.tech/eventra?sslmode=require
   ```
   Use this as your `DB_URL` environment variable.

### Backend (Railway)

1. Push to GitHub.
2. Create a Railway project and connect the repo.
3. Set all environment variables in Railway's **Variables** tab (use the Neon JDBC URL for `DB_URL`).
4. Railway builds and deploys automatically on every push to your main branch.

> For local mobile testing (e.g. QR scanning from a phone), use [ngrok](https://ngrok.com/) to expose your backend over HTTPS — the camera API requires a secure context.
