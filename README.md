JOB ALERTS - AUTOMATED JOB SCRAPER & NOTIFICATION SYSTEM

A production-ready job scraping and notification system that automatically fetches job listings from Tap Academy and sends real-time email alerts to subscribers.

---

TABLE OF CONTENTS

1. Features
2. Technology Stack
3. Getting Started
4. Installation
5. Configuration
6. API Endpoints
7. Database Schema
8. Deployment
9. Contributing
10. License

---

FEATURES

Core Features:
- Automated Job Scraping - Fetches jobs from Tap Academy API
- Email Notifications - Real-time alerts via Brevo API
- User Subscription - OTP-based email verification
- Scheduled Scraping - Configurable cron-based job fetching
- Duplicate Detection - Prevents duplicate job entries
- Secure Authentication - Spring Security with OTP verification

Technical Features:
- RESTful API - Well-structured endpoints
- PostgreSQL - NeonDB cloud database
- Brevo Integration - REST API for email delivery
- Environment Configuration - Secure .env file management
- Docker Support - Containerized deployment ready
- Development Tools - Hot reload with Spring DevTools

---

TECHNOLOGY STACK

Spring Boot 3.5.14
Java 21
Spring Security 6.5.10
Spring Data JPA 3.5.11
PostgreSQL 42.7.10
Hibernate 6.6.49
Lombok 1.18.46
Maven
Docker
Brevo API

---

GETTING STARTED

Prerequisites:
- Java 21 or higher
- Maven 3.8+
- PostgreSQL database
- Brevo API key
- Git

Quick Start:

1. Clone the repository
   git clone https://github.com/yourusername/job-alerts.git
   cd job-alerts

2. Configure environment variables
   cp .env.example .env
   Edit .env with your credentials

3. Build the application
   mvn clean install

4. Run the application
   mvn spring-boot:run

5. Access the application
   API: http://localhost:8080
   Health Check: http://localhost:8080/api/health

---

INSTALLATION

Using Docker:
docker build -t job-alerts .
docker run -p 8080:8080 --env-file .env job-alerts

Manual Setup:
1. Install dependencies
   mvn clean install

2. Create database
   CREATE DATABASE job_alerts;

3. Set environment variables
   export DB_HOST=your-db-host
   export DB_USERNAME=your-username
   export DB_PASSWORD=your-password

---

CONFIGURATION

Environment Variables (.env):

# Database Configuration
DB_HOST=your-db-host.neon.tech
DB_PORT=5432
DB_NAME=neondb
DB_USERNAME=your-username
DB_PASSWORD=your-password

# Tap Academy API
TAP_ACADEMY_EMAIL=your-email@gmail.com
TAP_ACADEMY_PASSWORD=your-password
TAP_ACADEMY_URL=https://tai.thetapacademy.com
TAP_ACADEMY_JOB_LIMIT=5

# Brevo Email Configuration
BREVO_USERNAME=your-smtp-username@smtp-brevo.com
BREVO_SMTP_KEY=your-smtp-key
BREVO_API_KEY=your-api-key
BREVO_FROM_EMAIL=sender@email.com
BREVO_FROM_NAME=Job Alerts

# Scraper Configuration
SCRAPER_ENABLED=true
SCRAPER_CRON_EXPRESSION=0 0 */2 * * *
SCRAPER_SEND_EMAILS=true
SCRAPER_LOGGING_ENABLED=true

# Server Configuration
SERVER_PORT=8080
LOG_LEVEL_COM_JOB_JOBALERTS=DEBUG

Cron Expressions:
Every 2 hours - 0 0 */2 * * *
Every 6 hours - 0 0 */6 * * *
Every 12 hours - 0 0 */12 * * *
Daily at midnight - 0 0 0 * * *
Every 30 minutes - 0 */30 * * * *

---

API ENDPOINTS

Authentication & Subscription:
POST /api/auth/send-otp - Send OTP to email
POST /api/auth/verify-otp - Verify OTP and subscribe
POST /api/auth/unsubscribe - Unsubscribe user

Job Management:
GET /api/jobs - Get all jobs
GET /api/jobs/active - Get active jobs
GET /api/jobs/{id} - Get job by ID

Health & Monitoring:
GET /api/health - Application health check
GET /api/scraper/status - Scraper status
POST /api/scraper/manual-sync - Trigger manual sync

Example Requests:

Send OTP:
curl -X POST http://localhost:8080/api/auth/send-otp -H "Content-Type: application/json" -d '{"email": "user@example.com"}'

Verify OTP:
curl -X POST http://localhost:8080/api/auth/verify-otp -H "Content-Type: application/json" -d '{"email": "user@example.com", "otp": "123456"}'

Get Active Jobs:
curl -X GET http://localhost:8080/api/jobs/active

---

DATABASE SCHEMA

Users Table:
CREATE TABLE users (
id BIGSERIAL PRIMARY KEY,
email VARCHAR(255) NOT NULL UNIQUE,
verified BOOLEAN DEFAULT FALSE,
subscribed BOOLEAN DEFAULT TRUE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Jobs Table:
CREATE TABLE jobs (
id BIGSERIAL PRIMARY KEY,
portal_job_id VARCHAR(255) NOT NULL UNIQUE,
job_title VARCHAR(255),
job_role VARCHAR(255),
location VARCHAR(255),
package_lpa DECIMAL(10,2),
job_id INTEGER,
applied BOOLEAN DEFAULT FALSE,
expired BOOLEAN DEFAULT FALSE,
expires_in TIMESTAMP,
interview_date TIMESTAMP,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

OTP Verifications Table:
CREATE TABLE otp_verifications (
id BIGSERIAL PRIMARY KEY,
email VARCHAR(255) NOT NULL,
otp VARCHAR(255) NOT NULL,
expires_at TIMESTAMP NOT NULL,
verified BOOLEAN DEFAULT FALSE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Job Preferences Table:
CREATE TABLE job_preferences (
id BIGSERIAL PRIMARY KEY,
user_id BIGINT NOT NULL REFERENCES users(id) UNIQUE,
location VARCHAR(255),
minimum_package DECIMAL(10,2),
active BOOLEAN DEFAULT TRUE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

---

DEPLOYMENT

Deploy to Render:

1. Push to GitHub
   git add .
   git commit -m "Initial commit"
   git push origin main

2. Connect to Render
- Log in to Render (https://render.com)
- Click "New +" -> "Web Service"
- Connect your GitHub repository
- Configure:
  Build Command: mvn clean install
  Start Command: java -jar target/jobalerts-0.0.1-SNAPSHOT.jar

3. Add Environment Variables
- Copy all .env variables to Render's environment section
- Ensure all required variables are set

4. Deploy
- Click "Deploy"

Environment Variables for Render:

DB_HOST=your-neon-db-host.neon.tech
DB_PORT=5432
DB_NAME=neondb
DB_USERNAME=your-username
DB_PASSWORD=your-password

TAP_ACADEMY_EMAIL=your-email@gmail.com
TAP_ACADEMY_PASSWORD=your-password
TAP_ACADEMY_URL=https://tai.thetapacademy.com
TAP_ACADEMY_JOB_LIMIT=5

BREVO_USERNAME=your-smtp-username@smtp-brevo.com
BREVO_SMTP_KEY=your-smtp-key
BREVO_API_KEY=your-api-key
BREVO_FROM_EMAIL=sender@email.com
BREVO_FROM_NAME=Job Alerts

SCRAPER_ENABLED=true
SCRAPER_CRON_EXPRESSION=0 0 */2 * * *
SCRAPER_SEND_EMAILS=true
SCRAPER_LOGGING_ENABLED=true

SERVER_PORT=8080
LOG_LEVEL_COM_JOB_JOBALERTS=INFO

---

CONTRIBUTING

1. Fork the repository
2. Create your feature branch (git checkout -b feature/AmazingFeature)
3. Commit your changes (git commit -m 'Add some AmazingFeature')
4. Push to the branch (git push origin feature/AmazingFeature)
5. Open a Pull Request

Development Guidelines:
- Follow Spring Boot best practices
- Write unit tests for new features
- Update documentation as needed
- Keep API endpoints RESTful

---

LICENSE

This project is licensed under the MIT License.

---

ACKNOWLEDGMENTS

- Spring Boot - Excellent framework
- Brevo - Email delivery service
- NeonDB - Cloud PostgreSQL
- Tap Academy - Job API provider

---

CONTACT

Email: support@yourdomain.com
Issues: https://github.com/mohankumaronly/job-alerts/issues

---

BUILT WITH ❤️ BY Mohan

