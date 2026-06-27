# AI Java Practice Project

A Maven-based Java project for learning and practice.

## Project Structure

- `src/main/java/com/example/` - Source code
- `src/test/java/` - Test code
- `pom.xml` - Maven configuration

## Building the Project

```bash
mvn clean install
```

## Running the Application

```bash
mvn exec:java -Dexec.mainClass=com.example.Main
```

## Running Tests

```bash
mvn test
```

## VS Code Tasks

- **maven: clean install** - Clean and build the project (default build task)
- **maven: compile** - Compile source code
- **maven: test** - Run tests
- **maven: run** - Run the main application
