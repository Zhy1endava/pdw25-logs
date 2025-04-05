# Test Framework Project

This project demonstrates how to set up Log4j2 to create a log file for each test executed. It consists of three sub-projects:

1. **driver_factory**
2. **pom_module**
3. **test_framework** (main project)

## Projects Overview

### driver_factory

This project uses Java Util Logging (JUL) for logging. It serves as an example of how to import and use different logging implementations.

### pom_module

This project uses Apache Commons Logging. It demonstrates another way to implement logging in a Java project.

### test_framework

This is the main project. It uses bridge libraries to route all logging to SLF4J and uses Log4j2 as the logging implementation. The main idea is to show how to set up Log4j2 to create a log file for each test executed.

## Setup Instructions

1. Clone the repository.
2. Navigate to the `test_framework` directory.
3. Build the project using Gradle:
   ./gradlew build
4. Run the tests:
   ./gradlew test

## Logging Configuration

- **driver_factory**: Uses JUL for logging.
- **pom_module**: Uses Apache Commons Logging.
- **test_framework**: Uses SLF4J with Log4j2 as the implementation. Log4j2 is configured to create a separate log file for each test executed.

## Log4j2 Configuration

The `log4j2.properties` file is the main configuration file for Log4j2. You can find it [here](test_framework/src/main/resources/log4j2.properties).

### Appenders

- **ConsoleAppender**: This appender writes log messages to the console. It is useful for debugging and development purposes.
- **FileAppender**: This appender writes log messages to a specified file. In this project, it is configured to create a separate log file for each test executed.
- **RollingFileAppender**: This appender writes log messages to a file and rolls the file over based on a specified policy, such as size or time. It helps manage log file sizes and ensures that old logs are archived.

## Dependencies

- **driver_factory**:
   - Selenium
   - TestNG

- **pom_module**:
   - Commons Logging

- **test_framework**:
   - SLF4J
   - Log4j2

## Example Usage

To see the logging in action, run the tests in the `test_framework` project. Each test will generate a separate log file, demonstrating the logging setup.

## License

This project is licensed under the MIT License.