# Link Generator System

The Link Generator System is a Spring Boot application designed to generate unique transaction links for customers to view and pay their invoices. This README file provides an overview of the project, instructions for setting up the development environment, and guidelines for contributing to the codebase.

Find the Swagger Documentation [here](https://linkk-4iyg.onrender.com/swagger-ui/index.html)

## Features

- Generate unique links for viewing invoice details
- Generate unique links for paying invoices
- Secure and encrypted link generation
- RESTful API endpoints for integration with other systems
- Database storage for generated links and associated data

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL (or any other relational database)
- Maven
- JUnit 5 (for unit testing)
- Mockito (for mocking dependencies in tests)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or later
- MySQL (or any other relational database)
- Maven

### Installation

1. Clone the repository:

```bash
git clone https://github.com/whalewalker/linkk.git
```

1. Navigate to the project directory:

```bash
cd linkk
```

1. Create a mySQL database for the application.

2. Update the database configuration in `src/main/resources/application.properties` with your database credentials.

3. Build the project using Maven:

```bash
mvn clean install
```

1. Run the application:

```bash
mvn spring-boot:run
```

The application should now be running at `http://localhost:8080`.

## Contributing

We welcome contributions to the Link Generator System project. To contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix:

```bash
git checkout -b feature/your-feature-name
```

1. Make the necessary changes and commit them with descriptive commit messages.
2. Push your changes to your forked repository:

```bash
git push origin feature/your-feature-name
```

1. Create a pull request against the main repository's `main` branch.

## Code Style and Guidelines

- Follow Java's coding conventions and idioms.
- Write clean and clear code with appropriate naming conventions.
- Maintain a consistent code style throughout the project.
- Write unit tests for new features and bug fixes.
- Document your code using JavaDoc comments where necessary.
- Ensure your code is maintainable and flexible for future changes.
