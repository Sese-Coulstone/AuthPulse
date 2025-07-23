# AuthPulse

A robust authentication service built with Spring Boot, featuring JWT authentication, email verification, and password reset functionality.

## Features

- JWT-based authentication
- User registration with email verification
- Password reset functionality
- Rate limiting for sensitive endpoints
- CORS configuration for frontend integration
- Secure password validation
- Role-based access control

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security
- MySQL Database
- JavaMail for email services
- JSON Web Tokens (JWT)

## Getting Started

### Prerequisites

- JDK 17 or later
- MySQL Database
- Maven

### Environment Variables

The following environment variables need to be set:

```properties
DB_URL=your_database_url
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
MAIL_HOST=your_smtp_host
MAIL_PORT=your_smtp_port
MAIL_USERNAME=your_email_username
MAIL_PASSWORD=your_email_password
MAIL_FROM=noreply@yourdomain.com
```

### Running the Application

1. Clone the repository:
```bash
git clone https://github.com/Sese-Coulstone/AuthPulse.git
```

2. Navigate to the project directory:
```bash
cd AuthPulse
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on port 8080 by default.

## API Endpoints

- POST /api/v1.0/register - Register a new user
- POST /api/v1.0/login - Authenticate user
- POST /api/v1.0/verify-email - Verify email address
- POST /api/v1.0/reset-password - Reset password
- POST /api/v1.0/verify-otp - Verify OTP for password reset
- POST /api/v1.0/reset-password-otp - Complete password reset with OTP

## Security Features

- Password strength validation
- Rate limiting on sensitive endpoints
- CORS configuration for frontend integration
- JWT-based stateless authentication
- Secure password hashing

## Frontend Integration

The service is configured to work with a React frontend (coming soon) running on port 5173.

## License

This project is licensed under the MIT License.
