# ATM Simulation Project

This project implements a simple ATM (Automated Teller Machine) simulation in Java, following Test-Driven Development (TDD) principles. It includes classes for the ATM functionality, current account management, and interfaces for remote services and hardware interactions.

## Features

- **User Authentication**: Log in using a card number retrieved from hardware.
- **Balance Inquiry**: Check the current account balance.
- **Withdrawal**: Withdraw money if sufficient balance is available.
- **Deposit**: Deposit money via envelope reading.
- **Error Handling**: Handles hardware failures and insufficient funds.
- **Mock Objects**: Uses mocks for `ServicoRemoto` and `Hardware` interfaces in tests.

## Architecture

- **CaixaEletronico**: Main ATM class handling all operations.
- **ContaCorrente**: Represents a current account with balance information.
- **ServicoRemoto**: Interface for retrieving and persisting account data.
- **Hardware**: Interface for interacting with physical hardware (card reader, money dispenser, envelope reader).

All monetary values are handled using `BigDecimal` for precision.

## Prerequisites

- Java 8 or higher
- Maven 3.x

## Installation

1. Clone the repository:
   ```
   git clone <repository-url>
   cd Desafiosemana3
   ```

2. Build the project:
   ```
   mvn clean compile
   ```

## Usage

The project is a library; integrate `CaixaEletronico` into your application by providing implementations or mocks for `ServicoRemoto` and `Hardware`.

Example usage:
```java
ServicoRemoto servicoRemoto = new MockServicoRemoto();
Hardware hardware = new MockHardware();
CaixaEletronico atm = new CaixaEletronico(servicoRemoto, hardware);

String loginResult = atm.logar();
String balance = atm.saldo();
String withdrawResult = atm.sacar(new BigDecimal("100.00"));
String depositResult = atm.depositar(new BigDecimal("50.00"));
```

## Testing

Tests are written using JUnit and Mockito for mocking dependencies. Run tests with:
```
mvn test
```

Test coverage includes:
- Successful operations
- Authentication failures
- Insufficient balance
- Hardware failures

## Future Improvements

- **User Interface**: Develop a graphical or web-based UI for better user interaction.
- **Database Integration**: Replace mock `ServicoRemoto` with a real database implementation (e.g., using JPA or JDBC).
- **Security Enhancements**: Add PIN verification, encryption for sensitive data, and secure communication protocols.
- **Multi-threading**: Implement concurrent access handling for multiple users.
- **Transaction Logging**: Add logging for all transactions with timestamps and audit trails.
- **Internationalization**: Support multiple currencies and locales for balance formatting.
- **Exception Handling**: Refine exception types and provide more detailed error messages.
- **Performance Optimization**: Optimize for high-throughput scenarios in a real ATM network.
- **API Endpoints**: Expose RESTful APIs for integration with banking systems.
- **Testing Expansion**: Increase test coverage, including integration tests and load testing.
- **Documentation**: Add Javadoc comments and API documentation.

## Contributing

Contributions are welcome! Please follow TDD practices and ensure all tests pass before submitting a pull request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
