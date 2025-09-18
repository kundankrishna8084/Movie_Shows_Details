# 🎬 Movie Shows — Ticket Booking Application

Movie_Shows_Details** is a Spring Boot microservice to manage movie shows, screens, seats, and bookings. 
It exposes REST APIs to check real-time seat availability and to book tickets with seat selection. Built with Spring Boot, JPA/Hibernate and MySQL.


## 🚀 Key Features
* Create, read, update and delete (CRUD) for Movies, Shows, Screens and Seats
* Show-level real-time seat availability
* Seat selection and ticket booking with transactional safety
* RESTful APIs with clear DTOs and HTTP status codes
* MySQL persistence with JPA / Hibernate
* Concurrency-safe booking (transactional + optimistic locking patterns recommended)


## 🛠 Tech Stack
* Java 17
* Spring Boot (web, data-jpa)
* Hibernate / JPA
* MySQL (or MariaDB)
* Maven


## 📁 Project Structure

```
Movie_Shows_Details/
 ├── src/main/java/com/jsp/movie_shows_details
 │   ├── controller
 │   │    └── AvailabilityController.java     # REST endpoints for show availability
 │   ├── service
 │   │    └── AvailabilityService.java        # Business logic for seat availability and bookings
 │   ├── repository
 │   │    ├── ShowRepository.java             # JPA repository for shows
 │   │    ├── ScreenRepository.java           # JPA repository for screens
 │   │    ├── SeatRepository.java             # JPA repository for seats
 │   │    ├── BookingRepository.java          # JPA repository for bookings
 │   │    └── BookingSeatRepository.java      # JPA repository for booked seats
 │   ├── entity
 │   │    ├── Show.java                       # Entity for movie shows
 │   │    ├── Screen.java                     # Entity for cinema screens
 │   │    ├── Seat.java                       # Entity for seats
 │   │    ├── Booking.java                    # Entity for bookings
 │   │    └── BookingSeat.java                # Entity for booked seats
 │   ├── dto
 │   │    └── AvailabilityResponse.java       # DTO for availability response
 │   └── MovieShowsDetailsApplication.java    # Main Spring Boot application
 └── pom.xml                                  # Maven dependencies
```

---

## ✅ Design & Entities (high-level)

**Show**
* id, movieName, startDateTime, endDateTime, screen (ManyToOne)

**Screen**
* id, name, seats (OneToMany)

**Seat**
* id, seatNumber (eg. A1), row, type (REGULAR / PREMIUM), status (AVAILABLE / BLOCKED / MAINTENANCE)

**Booking**

* id, show (ManyToOne), userIdentifier (email/phone), bookingTime, status (CONFIRMED/CANCELLED/PENDING)
**BookingSeat**

* id, booking (ManyToOne), seat (ManyToOne), price

Notes:
* BookingSeat stores mapping of booked seats to a booking.
* Use `@Version` on frequently updated entities (e.g., Seat or BookingSeat) for optimistic locking to prevent double-booking.
  

## 🔌 REST API Endpoints (examples)
> Base: `http://localhost:8080`

### Get availability for a show
`GET /shows/{showId}/availability`

**Response (200 OK)**

```json
{
  "showId": 101,
  "movieName": "Wednesday",
  "showDateTime": "2025-09-24T16:00:00",
  "screenName": "Screen 1",
  "availableSeats": [
    { "seatId": 1, "seatNumber": "A1", "status": "AVAILABLE" },
    { "seatId": 2, "seatNumber": "A2", "status": "BOOKED" },
    { "seatId": 3, "seatNumber": "A3", "status": "AVAILABLE" }
  ]
}
```

Curl example:

```bash
curl -sS http://localhost:8080/shows/101/availability
```

### Get availability for all shows

`GET /shows/availability`

Returns a list of availability responses for all upcoming shows.

### Create a booking

`POST /bookings`

**Request**

```json
{
  "showId": 101,
  "userIdentifier": "alice@example.com",
  "seatIds": [1, 3]
}
```

**Responses**

* `201 Created` — booking successful (returns booking summary)
* `409 Conflict` — one or more seats already booked (returns details of failed seats)
* `400 Bad Request` — validation error

---

## ⚙️ Implementation notes & best practices

* **Transactional booking:** Use `@Transactional` in service layer for the booking flow: validate available seats → create Booking → persist BookingSeat entries → commit.
* **Locking strategies:** Prefer optimistic locking using `@Version` on entities, and re-check availability inside a transaction before committing. For extremely high throughput, consider a Redis-based seat reservation / token system.
* **Seat status flow:** `AVAILABLE` → `BLOCKED` (temporary while user is in checkout) → `BOOKED` or back to `AVAILABLE` on timeout/cancel.
* **Idempotency:** Ensure booking endpoint is idempotent for retries (e.g., using an idempotency key or checking existing user+show+seats state).
* **Validation:** Validate `showId`, `seatIds` exist and seats belong to the show’s screen.
* **Error handling:** Return clear error codes and messages. Example: `{ "error": "Seat A1 is already booked", "seatId": 1 }`.


## 🧪 Testing
* Unit tests for service logic (mock repositories)
* Integration tests using an in-memory database (H2) or Testcontainers with MySQL
* End-to-end tests which attempt concurrent bookings to validate locking logic


## 🐳 Docker (optional)
Example `docker-compose.yml` for local development using MySQL:
```yaml
version: '3.8'
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: movie_shows
      MYSQL_ROOT_PASSWORD: root
    ports:
      - 3306:3306
    volumes:
      - db_data:/var/lib/mysql

volumes:
  db_data:
```

Set `spring.datasource.url=jdbc:mysql://db:3306/movie_shows` in `application.properties` when running via Docker Compose.


## 🔧 Configuration (application.properties/application.yml)
Minimum properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/movie_shows
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

For production, prefer `ddl-auto=validate` or manage schema with Flyway/Liquibase.

---

## ✅ Quality & Production-readiness Checklist

* [ ] Input validation and strong API contract (DTOs)
* [ ] Proper HTTP status codes & error messages
* [ ] Transactional booking with optimistic locking
* [ ] Automatic seat release for blocked seats after timeout
* [ ] Rate limiting for booking endpoints
* [ ] Centralized logging & observability (structured logs, metrics)
* [ ] Database migration (Flyway/Liquibase)
* [ ] Integration and concurrent booking tests

---

## 📦 Build & Run
Build with Maven:

```bash
mvn clean package
```

Run:

```bash
java -jar target/movie-shows-details-0.0.1-SNAPSHOT.jar
```

Or run from your IDE as a Spring Boot application.

---

## 📬 Postman / Example Collection

Provide a Postman collection file (recommended) with requests for:

* Create / update screens and seats
* Create shows
* Get show availability
* Create and cancel bookings

---

## 👨‍💻 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feat/your-feature`)
3. Commit your changes (`git commit -m "feat: add ..."`)
4. Create a pull request

Follow standard GitHub PR expectations: descriptive title, small focused changes, tests included.


## 📄 License
This project is provided under the MIT License. Feel free to adapt for your needs.

---

## 🧑‍💻 Author
**Kundan Krishna** — original author & maintainer
