# Exception Handling Template

A complete Spring Boot exception handling template with i18n support, featuring a reusable exception handling module and consistent error response format.

## 🎯 Features

- ✅ **Consistent Error Response Format** - Standardized JSON error structure
- ✅ **Reusable Exception Handling Module** - Can be imported into any Spring Boot project
- ✅ **Internationalization (i18n)** - Multi-language error messages
- ✅ **Trace ID Generation** - Unique error tracking with prefixes
- ✅ **Multiple Exception Types** - Business, Technical, Validation, and Generic exceptions
- ✅ **Auto-configuration** - Zero configuration required


## 🚀 Quick Start

### 1. Clone and Run

```bash
# Clone the repository
git clone <repository-url>
cd exception_handing

# Build and run
./gradlew bootRun
```

### 2. Test the Endpoints

```bash
# Test unexpected error
curl -s "http://localhost:8080/api/demo/simulate/unexpected-error" | jq .

# Test database error
curl -s "http://localhost:8080/api/demo/simulate/database-error" | jq .

# Test validation with valid data
curl -s -X POST "http://localhost:8080/api/demo/validate" \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com", "age": 25, "phone": "+1234567890"}' | jq .

# Test validation with invalid data
curl -s -X POST "http://localhost:8080/api/demo/validate" \
  -H "Content-Type: application/json" \
  -d '{"name": "", "email": "invalid", "age": 15}' | jq .
```

## 📋 Error Response Format

All errors follow this consistent format:

```json
{
    "errorCode": "INTERNAL_SERVER_ERROR",
    "message": "An internal server error occurred",
    "path": "/api/demo/simulate/unexpected-error",
    "status": 500,
    "timestamp": "2025-08-23T17:14:58.515022863",
    "traceId": "ERR-7ba03c72f4c24b8d"
}
```

For validation errors, `fieldErrors` is included:

```json
{
    "errorCode": "VALIDATION_FAILED",
    "message": "Validation failed",
    "path": "/api/demo/validate",
    "status": 400,
    "timestamp": "2025-08-23T17:00:47.78207227",
    "fieldErrors": [
        {
            "field": "email",
            "rejectedValue": "invalid",
            "message": "Invalid email format"
        }
    ],
    "traceId": "VALID-90aa30d2eaa64454"
}
```

## 🔧 Using as Template for Other Projects

### Option 1: Copy the Module

1. **Copy the exception handling module** to your new project:
   ```bash
   cp -r exception-handling-module/ your-new-project/
   ```

2. **Add module dependency** in your `build.gradle`:
   ```gradle
   dependencies {
       implementation project(':exception-handling-module')
       // ... other dependencies
   }
   ```

3. **Add message properties** to `src/main/resources/messages/messages.properties`:
   ```properties
   # General errors
   error.general.internal=An internal server error occurred
   error.general.badrequest=Invalid request
   
   # Business errors
   error.business.user.notfound=User with ID {0} not found
   error.business.user.duplicate.email=User with email {0} already exists
   
   # Validation errors
   error.validation.required=Field {0} is required
   error.validation.email.invalid=Invalid email format
   
   # Technical errors
   error.technical.database.connection=Database connection failed
   ```

4. **Use exceptions** in your controllers:
   ```java
   @RestController
   public class UserController {
       @GetMapping("/{id}")
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

### Option 2: Use as Reference

1. **Study the structure** and copy relevant parts
2. **Adapt the exception classes** to your domain
3. **Customize the error response format** if needed
4. **Add your own message properties**

## 📚 Exception Types

### BusinessException
For expected business logic errors that should be shown to users.

```java
throw new BusinessException(
    "INSUFFICIENT_BALANCE",
    "error.business.account.insufficient.balance",
    requiredAmount,
    availableBalance
);
```

### TechnicalException
For system-level errors that shouldn't expose technical details.

```java
try {
    return externalService.call();
} catch (Exception e) {
    throw new TechnicalException(
        "EXTERNAL_SERVICE_UNAVAILABLE",
        "error.technical.external.service",
        e
    );
}
```

### ValidationException
For validation errors with custom messages.

```java
if (!isValidEmail(email)) {
    throw new ValidationException(
        "INVALID_EMAIL_FORMAT",
        "error.validation.email.invalid"
    );
}
```

### ResourceNotFoundException
For resource not found scenarios.

```java
User user = userService.findById(id);
if (user == null) {
    throw new ResourceNotFoundException(
        "USER_NOT_FOUND",
        "error.business.user.notfound",
        id
    );
}
```

## 🌍 Internationalization

### Multiple Languages

Create language-specific message files:

**messages_en.properties** (English):
```properties
error.business.user.notfound=User with ID {0} not found
error.validation.email.invalid=Invalid email format
```

**messages_fa.properties** (Persian):
```properties
error.business.user.notfound=کاربر با شناسه {0} یافت نشد
error.validation.email.invalid=فرمت ایمیل نامعتبر است
```

### Language Detection

The module automatically detects the language from:
- `Accept-Language` header
- URL parameter `?lang=fa`
- Default locale

## 🔧 Configuration

### Application Properties

```properties
# Server configuration
server.port=8080
server.error.include-message=never
server.error.include-binding-errors=never
server.error.include-stacktrace=never
server.error.include-exception=false

# Validation messages
spring.messages.basename=messages/messages
spring.messages.encoding=UTF-8
spring.messages.cache-duration=3600

```

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

## 📦 Dependencies

### Main Project
- Spring Boot 3.5.5
- Java 21
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Spring Boot Starter AOP

## 🚀 Deployment

### Build
```bash
./gradlew clean build
```

### Run
```bash
./gradlew bootRun
```

### Test
```bash
./gradlew test
```

## 📝 Customization Examples

### Custom Exception
```java
public class OrderException extends BusinessException {
    public OrderException(String errorCode, String messageKey, Object... args) {
        super(errorCode, messageKey, args, HttpStatus.BAD_REQUEST);
    }
}
```

