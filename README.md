# Tax Statement Service

This is a Spring Boot API that provides two key endpoints for managing tax statements. It includes features like asynchronous parsing and persistence using Hibernate, retry mechanisms with exponential backoff, and configurable active service hours. The API uses Testcontainers for testing, Swagger UI for documentation, and a simple PostgreSQL table for storing tax statements.

## Features

- **File Upload Endpoint**: Accepts a PDF tax statement, parses it, and persists the data asynchronously using Hibernate.
- **Forward Endpoint**: Sends the tax statement to an external API and receives their response.
- **Asynchronous Parsing & Persistence**: Utilizes Hibernate for persistent storage, with asynchronous processing.
- **Retry Mechanism**: Implements exponential backoff with a maximum number of attempts for retrying failed requests to the external API.
- **Configurable Service Hours**: The active hours for the service can be easily configured using environmental variables.
- **Testcontainers for End-to-End Testing**: Uses Dockerized PostgreSQL and other services to ensure comprehensive integration tests.
- **Swagger UI**: Interactive API documentation powered by Swagger for easy exploration and testing of endpoints.

## Technology Stack

- **Spring Boot** (Java 17)
- **Hibernate** (for persistence)
- **PostgreSQL** (for data storage)
- **Testcontainers** (for integration tests)
- **Swagger UI** (for API documentation)
- **Retry Mechanism with Exponential Backoff**
- **Spring Security** (Optional, for securing endpoints)
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
