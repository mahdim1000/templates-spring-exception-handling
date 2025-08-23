# Exception Handling Module

A reusable Spring Boot exception handling module with i18n support, designed to provide consistent error responses across your applications.

## Features

- ✅ **Consistent Error Response Format** - Standardized JSON error structure
- ✅ **Internationalization (i18n)** - Multi-language error messages
- ✅ **Trace ID Generation** - Unique error tracking with prefixes
- ✅ **Multiple Exception Types** - Business, Technical, Validation, and Generic exceptions
- ✅ **Auto-configuration** - Zero configuration required
- ✅ **Java Records** - Modern, immutable DTOs
- ✅ **SOLID Principles** - Clean, maintainable architecture

## Quick Start

### 1. Add Dependency

```gradle
dependencies {
    implementation 'com.github.mahdim1000:exception-handling:1.0.0'
}
```

### 2. Add Message Properties

Create `src/main/resources/messages/messages.properties`:

```properties
# General errors
error.general.internal=An internal server error occurred
error.general.badrequest=Invalid request
error.general.notfound=Resource not found

# Business errors
error.business.user.notfound=User with ID {0} not found
error.business.user.duplicate.email=User with email {0} already exists

# Validation errors
error.validation.required=Field {0} is required
error.validation.email.invalid=Invalid email format
error.validation.age.invalid=Age must be between {0} and {1}

# Technical errors
error.technical.database.connection=Database connection failed
error.technical.external.service=External service unavailable
```

### 3. Use Exceptions

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException(
                "USER_NOT_FOUND",
                "error.business.user.notfound",
                id
            );
        }
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
```

## Error Response Format

All errors follow this consistent format:

```json
{
    "errorCode": "USER_NOT_FOUND",
    "message": "User with ID 123 not found",
    "path": "/api/users/123",
    "status": 404,
    "timestamp": "2025-08-23T16:45:21.607673314",
    "traceId": "NF-87f2fa3fadce4c60"
}
```

## Exception Types

### BusinessException
For expected business logic errors that should be shown to users.

```java
throw new BusinessException(
    "INSUFFICIENT_BALANCE",
    "error.business.account.insufficient.balance",
    accountId, balance
);
```

### TechnicalException
For system-level errors that shouldn't expose technical details.

```java
throw new TechnicalException(
    "DATABASE_CONNECTION_FAILED",
    "error.technical.database.connection",
    originalException
);
```

### ValidationException
For validation errors with custom messages.

```java
throw new ValidationException(
    "INVALID_EMAIL_FORMAT",
    "error.validation.email.invalid"
);
```

### ResourceNotFoundException
For resource not found scenarios.

```java
throw new ResourceNotFoundException(
    "USER_NOT_FOUND",
    "error.business.user.notfound",
    userId
);
```

## Configuration

### Custom Message Properties

Add language-specific message files:

- `messages_en.properties` - English
- `messages_fa.properties` - Persian
- `messages_es.properties` - Spanish

### Custom Trace ID Generator

```java
@Component
public class CustomTraceIdGenerator implements TraceIdGenerator {
    @Override
    public String generate(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
```

## DTOs

The module provides immutable DTOs using Java records:

```java
// Error Response
public record ErrorResponse(
    String errorCode,
    String message,
    String path,
    int status,
    LocalDateTime timestamp,
    List<FieldError> fieldErrors,
    String traceId
) {}

// Field Error
public record FieldError(
    String field,
    Object rejectedValue,
    String message
) {}
```

## Architecture

The module follows SOLID principles and clean architecture:

- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: Extensible without modification
- **Liskov Substitution**: Exception hierarchy follows LSP
- **Interface Segregation**: Focused interfaces
- **Dependency Inversion**: Depends on abstractions

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

MIT License - see LICENSE file for details.
