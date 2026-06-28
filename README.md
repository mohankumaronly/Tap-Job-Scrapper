# 🚀 Job Alerts - Automated Job Scraper & Notification System

A production-ready **Spring Boot** application that automatically scrapes job openings from **Tap Academy**, stores them in PostgreSQL, and instantly sends email notifications to subscribed users using **Brevo Email API**.

---

## ✨ Features

### 📌 Core Features

* 🔍 Automated job scraping from Tap Academy
* 📧 Real-time email notifications using Brevo
* 🔐 OTP-based email verification
* ⏰ Configurable cron-based scheduled scraping
* 🚫 Duplicate job detection
* 🔒 Secure APIs with Spring Security
* 📊 RESTful API architecture

### 🛠 Technical Features

* Spring Boot 3
* Java 21
* Spring Data JPA & Hibernate
* PostgreSQL (NeonDB)
* Docker support
* Environment-based configuration
* Spring DevTools
* Production-ready architecture

---

# 🛠 Tech Stack

| Technology      | Version   |
| --------------- | --------- |
| Java            | 21        |
| Spring Boot     | 3.x       |
| Spring Security | 6.x       |
| Spring Data JPA | 3.x       |
| Hibernate       | 6.x       |
| PostgreSQL      | 15+       |
| Maven           | Latest    |
| Docker          | Supported |
| Brevo Email API | REST API  |

---

# 📂 Project Structure

```
src
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── scheduler
├── config
└── util
```

---

# 🚀 Getting Started

## Prerequisites

* Java 21+
* Maven 3.8+
* PostgreSQL
* Brevo API Key
* Git

---

## Clone Repository

```bash
git clone https://github.com/mohankumaronly/Tap-Job-Scrapper.git

cd job-alerts
```

---

## Configure Environment

Copy the example environment file.

```bash
cp .env.example .env
```

Update your credentials inside `.env`.

---

## Build

```bash
mvn clean install
```

---

## Run

```bash
mvn spring-boot:run
```

Application will be available at:

```
http://localhost:8080
```

Health endpoint:

```
GET /api/health
```

---

# ⚙️ Environment Variables

```env
# Database
DB_HOST=
DB_PORT=5432
DB_NAME=
DB_USERNAME=
DB_PASSWORD=

# Tap Academy
TAP_ACADEMY_EMAIL=
TAP_ACADEMY_PASSWORD=
TAP_ACADEMY_URL=https://tai.thetapacademy.com
TAP_ACADEMY_JOB_LIMIT=5

# Brevo
BREVO_USERNAME=
BREVO_SMTP_KEY=
BREVO_API_KEY=
BREVO_FROM_EMAIL=
BREVO_FROM_NAME=Job Alerts

# Scheduler
SCRAPER_ENABLED=true
SCRAPER_CRON_EXPRESSION=0 0 */2 * * *
SCRAPER_SEND_EMAILS=true

# Server
SERVER_PORT=8080
```

---

# ⏰ Cron Examples

| Schedule          | Expression       |
| ----------------- | ---------------- |
| Every 30 minutes  | `0 */30 * * * *` |
| Every 2 hours     | `0 0 */2 * * *`  |
| Every 6 hours     | `0 0 */6 * * *`  |
| Every 12 hours    | `0 0 */12 * * *` |
| Daily at midnight | `0 0 0 * * *`    |

---

# 📡 REST API

## Authentication

| Method | Endpoint                | Description |
| ------ | ----------------------- | ----------- |
| POST   | `/api/auth/send-otp`    | Send OTP    |
| POST   | `/api/auth/verify-otp`  | Verify OTP  |
| POST   | `/api/auth/unsubscribe` | Unsubscribe |

---

## Jobs

| Method | Endpoint           |
| ------ | ------------------ |
| GET    | `/api/jobs`        |
| GET    | `/api/jobs/active` |
| GET    | `/api/jobs/{id}`   |

---

## Monitoring

| Method | Endpoint                   |
| ------ | -------------------------- |
| GET    | `/api/health`              |
| GET    | `/api/scraper/status`      |
| POST   | `/api/scraper/manual-sync` |

---

# 📦 Example API Requests

### Send OTP

```bash
curl -X POST http://localhost:8080/api/auth/send-otp \
-H "Content-Type: application/json" \
-d '{"email":"user@example.com"}'
```

### Verify OTP

```bash
curl -X POST http://localhost:8080/api/auth/verify-otp \
-H "Content-Type: application/json" \
-d '{"email":"user@example.com","otp":"123456"}'
```

### Get Active Jobs

```bash
curl http://localhost:8080/api/jobs/active
```

---

# 🗄 Database Tables

* Users
* Jobs
* OTP Verifications
* Job Preferences

The application uses **Spring Data JPA**, so tables can be generated automatically using Hibernate.

---

# 🐳 Docker

Build image

```bash
docker build -t job-alerts .
```

Run container

```bash
docker run -p 8080:8080 --env-file .env job-alerts
```

---

# ☁️ Deployment (Render)

1. Push your project to GitHub.
2. Create a **Web Service** on Render.
3. Connect the repository.
4. Configure:

**Build Command**

```bash
mvn clean install
```

**Start Command**

```bash
java -jar target/jobalerts-0.0.1-SNAPSHOT.jar
```

5. Add all environment variables.
6. Click **Deploy**.

---

# 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a feature branch

```bash
git checkout -b feature/new-feature
```

3. Commit changes

```bash
git commit -m "Add new feature"
```

4. Push

```bash
git push origin feature/new-feature
```

5. Open a Pull Request

---

# 📄 License

This project is licensed under the **MIT License**.

---

# 🙏 Acknowledgements

* Spring Boot
* PostgreSQL
* NeonDB
* Brevo
* Tap Academy

---

# 👨‍💻 Author

**Mohan Kumar**

GitHub: https://github.com/mohankumaronly

---

⭐ If you found this project useful, consider giving it a **Star** on GitHub!
