# ProductService – E-Commerce Microservice

A Spring Boot microservice that manages products and categories for an e-commerce platform. The service exposes a RESTful API and supports two interchangeable data-source strategies: a local MySQL database and the external [FakeStore API](https://fakestoreapi.com).

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Data Model](#data-model)
- [API Endpoints](#api-endpoints)
- [Design Patterns](#design-patterns)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#database-setup)
  - [Configuration](#configuration)
  - [Build & Run](#build--run)
- [Switching the Service Implementation](#switching-the-service-implementation)
- [Running Tests](#running-tests)

---

## Features

- Create, retrieve, and list products via a REST API.
- Pluggable service layer – switch between a local MySQL database and the FakeStore external API without changing any controller code.
- Automatic category creation: if the category supplied on product creation does not yet exist, it is persisted automatically (JPA `CascadeType.PERSIST`).
- Database schema managed through **Flyway** versioned migrations.
- Global exception handling with `@ControllerAdvice`, returning meaningful HTTP status codes.
- JPA Projections for lightweight, column-specific queries.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.4 |
| Build Tool | Apache Maven |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Migrations | Flyway 10.11.1 |
| HTTP Client | Spring `RestTemplate` |
| Code Generation | Lombok |
| Testing | JUnit 5 / Spring Boot Test |

---

## Architecture

```
Client
  │
  ▼
ProductController          (REST layer – HTTP in/out)
  │
  ▼
ProductService             (interface – contract)
  ├── OwnProductService    (qualifier: "selfproductservice") → MySQL via JPA
  └── FakeStoreProductService (qualifier: "fakestore")       → FakeStore REST API
        │
        ▼
  ProductRepository / CategoryRepository  (Spring Data JPA)
        │
        ▼
  MySQL Database
```

The controller is wired to one implementation via Spring's `@Qualifier`. Changing the active implementation requires only a one-line edit in the controller (or can be driven from configuration).

---

## Project Structure

```
src/
└── main/
    ├── java/com/example/productservice10april/
    │   ├── ProductService10AprilApplication.java   # Application entry point
    │   ├── configs/
    │   │   └── ApplicationConfiguration.java       # Bean definitions (RestTemplate)
    │   ├── controllers/
    │   │   ├── ProductController.java              # REST endpoints
    │   │   └── ControllerAdvisor.java              # Global exception handler
    │   ├── dtos/
    │   │   ├── RequestBodyProductdto.java          # Inbound request payload
    │   │   └── FakeStoreProductdto.java            # FakeStore API mapping + toProduct()
    │   ├── models/
    │   │   ├── BaseModel.java                      # Shared fields (id, timestamps, isDeleted)
    │   │   ├── Category.java                       # Category entity
    │   │   └── Product.java                        # Product entity
    │   ├── repositories/
    │   │   ├── CategoryRepository.java
    │   │   ├── ProductRepository.java              # Custom JPQL queries + derived methods
    │   │   └── Projections/
    │   │       └── ProductProjection.java          # Interface-based JPA projection
    │   └── services/
    │       ├── ProductService.java                 # Service interface
    │       ├── OwnProductService.java              # MySQL implementation
    │       └── FakeStoreProductService.java        # FakeStore API implementation
    └── resources/
        ├── application.properties                  # App & datasource configuration
        └── db/migration/
            └── V1__inti.sql                        # Flyway: initial schema
```

---

## Data Model

### Entity Relationship

```
Category  1 ──────< Product
```

### `BaseModel` (abstract, mapped superclass)

| Field | Type | Notes |
|---|---|---|
| `id` | `Long` | Auto-increment primary key |
| `createdAt` | `Date` | Creation timestamp |
| `lastModifiedAt` | `Date` | Last update timestamp |
| `isDeleted` | `boolean` | Soft-delete flag |

### `Category`

| Field | Type |
|---|---|
| `title` | `String` |
| `description` | `String` |
| `products` | `List<Product>` (EAGER, `@JsonIgnore`) |

### `Product`

| Field | Type | Notes |
|---|---|---|
| `title` | `String` | |
| `description` | `String` | |
| `price` | `double` | |
| `imageURL` | `String` | |
| `category` | `Category` | Many-to-one, `CascadeType.PERSIST` |

### Database Schema (managed by Flyway)

```sql
CREATE TABLE category (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at       DATETIME,
    last_modified_at DATETIME,
    is_deleted       BIT(1) NOT NULL,
    title            VARCHAR(255),
    description      VARCHAR(255)
);

CREATE TABLE product (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at       DATETIME,
    last_modified_at DATETIME,
    is_deleted       BIT(1) NOT NULL,
    description      VARCHAR(255),
    title            VARCHAR(255),
    imageurl         VARCHAR(255),
    price            DOUBLE NOT NULL,
    category_id      BIGINT,
    CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id)
);
```

---

## API Endpoints

All endpoints are served on `http://localhost:8080`.

### Create a Product

```
POST /products
Content-Type: application/json
```

**Request body:**
```json
{
  "title": "Wireless Headphones",
  "description": "Over-ear noise-cancelling headphones",
  "price": 149.99,
  "image": "https://example.com/headphones.jpg",
  "category": "Electronics"
}
```

**Response:** `200 OK` – the persisted `Product` object (JSON).

---

### Get a Single Product

```
GET /products/{id}
```

**Path parameter:** `id` – numeric product ID.  
**Response:** `200 OK` – matching `Product` object (JSON).

---

### Get All Products

```
GET /products
```

**Response:** `200 OK` – JSON array of all `Product` objects.

---

### Update a Product *(stub – not yet implemented)*

```
POST /products/{id}
```

---

### Error Responses

| Exception | HTTP Status |
|---|---|
| `IllegalArgumentException` | `400 Bad Request` |
| `NullPointerException` | `409 Conflict` |

---

## Design Patterns

### Strategy Pattern (Service Layer)
`ProductService` is an interface with two concrete implementations. The active implementation is injected into `ProductController` via `@Qualifier`:

```java
// Switch to "fakestore" to use the external API instead:
public ProductController(@Qualifier("selfproductservice") ProductService productService)
```

| Qualifier | Implementation | Data Source |
|---|---|---|
| `selfproductservice` | `OwnProductService` | Local MySQL database |
| `fakestore` | `FakeStoreProductService` | https://fakestoreapi.com |

### JPA Projections
`ProductProjection` is an interface-based projection that returns only selected fields (`title`, `description`, `price`, `category`), reducing the amount of data fetched from the database for read-heavy queries.

### Global Exception Handling
`ControllerAdvisor` (`@ControllerAdvice`) centralises exception handling so controllers stay clean:

```java
@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<String> handleException(Exception ex) { ... }
```

### Cascade Persistence
When a new product references a category name that does not yet exist, `CascadeType.PERSIST` on the `Product → Category` relationship means saving the product also saves the new category automatically—no separate save call is required.

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+

### Database Setup

1. Create the MySQL database and user:

```sql
CREATE DATABASE productservice25july;
CREATE USER 'productservice25ulyuser'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON productservice25july.* TO 'productservice25ulyuser'@'localhost';
FLUSH PRIVILEGES;
```

2. Flyway will apply `V1__inti.sql` automatically on first startup to create the `category` and `product` tables.

### Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=ProductService10April
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/productservice25july
spring.datasource.username=productservice25ulyuser
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway
spring.flyway.baseline-on-migrate=true
```

> **Note:** `spring.datasource.password` is not included in the committed `application.properties`. Set it via an environment variable or a local override file that is excluded from version control.

### Build & Run

```bash
# Build (skip tests for a quick start)
./mvnw clean package -DskipTests

# Run
./mvnw spring-boot:run
```

The service starts on **http://localhost:8080**.

---

## Switching the Service Implementation

To use the **FakeStore** external API instead of the local database, change the `@Qualifier` value in `ProductController`:

```java
// From:
public ProductController(@Qualifier("selfproductservice") ProductService productService)

// To:
public ProductController(@Qualifier("fakestore") ProductService productService)
```

No other code changes are needed. The FakeStore implementation uses `RestTemplate` to proxy requests to `https://fakestoreapi.com/products`.

---

## Running Tests

```bash
./mvnw test
```

The test suite uses Spring Boot's testing support. The main test class (`ProductService10AprilApplicationTests`) verifies that the application context loads correctly.
