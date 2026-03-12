
# Auction System API

A **Spring Boot REST API** for managing auctions and bids.

The system allows users to create auctions, place bids, and determine winners when auctions close.
The project demonstrates **clean architecture, validation, exception handling, testing, and AOP logging**.

This project was built to demonstrate **backend engineering skills using Spring Boot and Java**.

---

# Features

### Auction Management

* Create auctions
* Retrieve auctions
* Retrieve auctions by seller
* Close auctions manually
* Automatically determine the winning bid

### Bidding System

* Place bids on active auctions
* Prevent invalid bids
* Automatically update auction price

### Validation

* Request validation using `@Valid`
* Custom validation exceptions

### Error Handling

* Global exception handler (`@ControllerAdvice`)
* Structured API error responses

### Logging (AOP)

* Logs important service operations
* Tracks:

    * auction creation
    * bid placement
    * auction closing

### Testing

Includes multiple test layers:

* Unit tests for service logic
* Controller tests using `MockMvc`
* Repository tests using `@DataJpaTest`

---

# Technologies

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* H2 (tests)
* Mockito
* JUnit 5
* AOP (Spring AspectJ)
* Maven

---

# Project Architecture

```
src/main/java/auctionsystem

controller/
    AuctionController
    BidController
    UserController

service/
    AuctionService
    BidService
    UserService

service/impl/
    AuctionServiceImpl
    BidServiceImpl
    UserServiceImpl

repository/
    AuctionRepository
    BidRepository
    UserRepository

entity/
    Auction
    Bid
    User
    AuctionStatus

dto/
    request/
    response/

exception/
    custom exceptions
    GlobalExceptionHandler

aspect/
    LoggingAspect
```

---

# Business Rules

### Bidding rules

* Bid must be **greater than current auction price**
* Bid must be **greater than zero**
* Seller **cannot bid on own auction**
* Auction must be **ACTIVE**

### Auction closing rules

* Auction can only be closed **once**
* Highest bid wins
* If there are **no bids**, the auction closes without a winner

---

# API Endpoints

## Create User

```
POST /api/users
```

### Request

```json
{
  "username": "john",
  "email": "john@email.com"
}
```

### Response

```
201 Created
```

```json
{
  "id": 1,
  "username": "john",
  "email": "john@email.com"
}
```

---

# Create Auction

```
POST /api/auctions
```

### Request

```json
{
  "title": "Gaming Laptop",
  "description": "RTX 4080 laptop",
  "startingPrice": 1000,
  "endTime": "2026-04-01T20:00:00",
  "sellerId": 1
}
```

### Response

```
201 Created
```

```json
{
  "id": 1,
  "title": "Gaming Laptop",
  "description": "RTX 4080 laptop",
  "startingPrice": 1000,
  "currentPrice": 1000,
  "auctionStatus": "ACTIVE",
  "sellerId": 1,
  "sellerUsername": "john"
}
```

---

# Get Auction

```
GET /api/auctions/{id}
```

### Example

```
GET /api/auctions/1
```

Response:

```json
{
  "id": 1,
  "title": "Gaming Laptop",
  "currentPrice": 1200,
  "auctionStatus": "ACTIVE"
}
```

---

# Place Bid

```
POST /api/bids
```

### Request

```json
{
  "auctionId": 1,
  "bidderId": 2,
  "amount": 1200
}
```

### Response

```
201 Created
```

```json
{
  "bidId": 5,
  "amount": 1200,
  "bidderId": 2,
  "bidderUsername": "alex"
}
```

---

# Close Auction

```
PATCH /api/auctions/{id}/close
```

### Example

```
PATCH /api/auctions/1/close
```

### Response

```json
{
  "auctionId": 1,
  "title": "Gaming Laptop",
  "finalPrice": 1400,
  "auctionStatus": "CLOSED",
  "winnerId": 2,
  "winnerUsername": "alex"
}
```

---

# Error Response Format

All errors return structured JSON.

Example:

```json
{
  "timestamp": "2026-03-10T18:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Bid amount must be greater than current price",
  "path": "/api/bids"
}
```

---

# Logging (AOP)

The system logs key service actions automatically:

Example logs:

```
AOP LOG: Method called -> placeBid
AOP LOG: Method finished -> placeBid
```

Logged operations:

* auction creation
* bid placement
* auction closing

---

# Running the Project

### 1. Clone repository

```
git clone <repository-url>
```

### 2. Navigate to project

```
cd auctionsystem
```

### 3. Run the application

```
mvn spring-boot:run
```

Server will start on:

```
http://localhost:8080
```

---

# Running Tests

Run all tests:

```
mvn test
```

Test coverage includes:

* service logic
* controllers
* repositories

---

# Future Improvements

Possible extensions:

* automatic auction closing by end time
* authentication and authorization
* pagination for auctions
* bid history endpoints
* websocket live bidding
* Swagger/OpenAPI documentation

---

# Author

Developed as a backend portfolio project demonstrating:

* Spring Boot architecture
* REST API design
* validation
* exception handling
* testing
* AOP logging

