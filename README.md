# Tax Statement Service

This is a Spring Boot API that provides two key endpoints for managing tax statements. It includes features like asynchronous parsing and persistence using Hibernate, retry mechanisms with exponential backoff, and configurable active service hours. The API uses Testcontainers for testing, Swagger UI for documentation, and a simple PostgreSQL table for storing tax statements.

## Technology Stack

- **Spring Boot** (Java 17)
- **Hibernate** (for persistence)
- **PostgreSQL** (for data storage)
- **Testcontainers** (for integration tests)
- **Swagger UI** (for API documentation)
- **Retry Mechanism with Exponential Backoff**
- **Spring Web & RestTemplate** (for external API calls)

## Setup & Configuration

### Prerequisites

- JDK 17 or later
- Docker (for Testcontainers)
- PostgreSQL database

### Environment Variables

This project relies on several environment variables for configuring active service hours and external API interaction. You can define these and other variables in your `.env` file or directly in your environment:

- `SERVICE_AVAILABILITY_START`: Start time for service availability (e.g., `"07:00"`).
- `SERVICE_AVAILABILITY_END`: End time for service availability (e.g., `"22:00"`).
- `SECONDARY_SERVICE_URL`: URL of the external API to which the tax statement is forwarded.




 ![tax statement](https://github.com/user-attachments/assets/be7d5e32-1e68-4828-a6fe-b47ec2cc7460)
