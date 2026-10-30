# Backend Project Structure

The backend follows a layered package structure to maintain a clear separation of concerns and make the application easier to maintain, test, and extend.

### Package Responsibilities

* **config** – Contains application-wide configurations, bean definitions, and security configurations.
* **controllers** – Handles incoming HTTP requests, validates request inputs, and returns appropriate responses.
* **dto** – Defines Data Transfer Objects to control how data moves between API layers without exposing internal entities.
* **entity** – Contains JPA entities representing the application's database models.
* **repositories** – Handles database operations and abstracts data access using Spring Data JPA.
* **security** – Manages authentication, authorization, JWT handling, and security-related components.
* **services** – Contains business logic and coordinates operations between controllers and repositories.
* **enums** - Employee status, gender, etc.
* **exceptions** - Custom exceptions and globl exception handling.

### Why This Structure?

This structure was chosen to improve **maintainability, modularity, and separation of responsibilities**.

1. **Separation of Concerns:** Each package has a clearly defined responsibility, preventing business logic, database operations, and HTTP handling from becoming tightly coupled.
2. **Maintainability:** Changes to one layer can be made with minimal impact on other layers.
3. **Reusability:** Service and repository components can be reused across multiple controllers and application workflows.
4. **Testability:** Individual layers can be tested

### Deliberately Left Out

* **Role based Authorization:** As the application is focused for salary management of employees, and only one user type **HR**.
* **Linking DB to Users:** An HR should be allowed to access data irrespective to region, department or other aspects if not mentioned explicitly.

# Database Selection – PostgreSQL

## Why PostgreSQL?

PostgreSQL because this application deals with sensitive salary information and requires reliable data storage and consistency.

Some of the reasons behind this choice:

* **Data Integrity:** Supports constraints and relationships to keep employee and salary records consistent.
* **Transactions:** ACID compliance ensures salary updates are handled reliably without leaving partially updated records.
* **Relational Data:** Makes it easier to manage relationships between employees, departments, countries, and salary records.
* **Querying:** Supports complex queries and aggregations, which are useful for generating salary reports and analyzing compensation across departments and countries.
* **Concurrency:** Handles multiple users accessing and updating records efficiently.

SQLite would also work for a smaller prototype, but PostgreSQL is a better fit for a multi-user organizational application with room for future growth.