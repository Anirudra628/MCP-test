# AI Java Practice

A simple Maven-based Java application for learning and practice.

## Requirements

- Java 17+
- Maven 3.9.0+

## Setup

1. Navigate to the project directory
2. Run `mvn clean install`

## Available Maven Tasks

| Task | Description |
|------|-------------|
| `mvn compile` | Compile source code |
| `mvn test` | Run unit tests |
| `mvn clean install` | Clean and build the project |
| `mvn exec:java -Dexec.mainClass=com.example.Main` | Run the main application |

## VS Code Integration

Use the Tasks feature (`Ctrl+Shift+P` → "Run Task") to execute Maven tasks:

- **maven: clean install** (default build)
- **maven: compile**
- **maven: test**
- **maven: run**

## Project Structure

```
.
├── src/
│   ├── main/java/com/example/    # Source code
│   └── test/java/                 # Test code
├── pom.xml                         # Maven configuration
├── README.md                       # This file
└── .vscode/
    └── tasks.json                  # VS Code Maven tasks
```
