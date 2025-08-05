# Password Manager Models Package

This package contains the redesigned object-oriented model classes for the Simple Password Manager application. The design demonstrates all 4 core principles of Object-Oriented Programming.

## Quick Overview

### User Classes
- **BaseUser** - Abstract base class for all user types
- **GuestUser** - Temporary guest users (demo mode)
- **NormalUser** - Regular registered users
- **AdminUser** - Administrative users with special privileges
- **User** - Legacy compatibility class

### Password Entry Classes
- **BasePasswordEntry** - Abstract base class for password entries
- **PasswordEntry** - User-managed password entries
- **SamplePasswordEntry** - Demo password entries for guests

### Utility Classes
- **UserFactory** - Factory pattern for creating user objects
- **EnhancedUserManager** - Enhanced user management with OOP principles
- **OOPDemonstration** - Demonstration of OOP principles in action

## Key Features

### 1. Encapsulation ✅
- Private fields with controlled access through getter/setter methods
- Input validation in setter methods
- Access tracking for sensitive operations

### 2. Inheritance ✅
- Clear class hierarchies for users and password entries
- Shared functionality in base classes
- Specialized behavior in derived classes

### 3. Polymorphism ✅
- Method overriding for different behaviors
- Runtime type checking and casting
- Same interface, different implementations

### 4. Abstraction ✅
- Abstract base classes defining contracts
- Hidden implementation complexity
- Factory pattern for object creation

## Usage Examples

### Creating Users
```java
// Factory pattern usage
BaseUser guest = UserFactory.createGuestUser();
BaseUser normal = UserFactory.createNormalUser("john", "john@email.com", "hash");
BaseUser admin = UserFactory.createAdminUser("admin", "admin@system.com", "hash");
```

### Enhanced User Management
```java
// Register and authenticate users
EnhancedUserManager.registerUser("user", "user@email.com", "password", "normal");
BaseUser user = EnhancedUserManager.authenticateUser("user@email.com", "password");

// Polymorphic operations
String type = user.getUserType(); // Different for each user type
boolean canManage = user.canManagePasswords(); // Different behavior
user.performLoginActions(); // Different actions per type
```

### Password Management
```java
if (user instanceof NormalUser) {
    NormalUser normalUser = (NormalUser) user;
    PasswordEntry entry = new PasswordEntry("GitHub", "username", "password");
    normalUser.addPasswordEntry(entry);
}
```

## Running the Demonstration

To see all OOP principles in action, run:
```java
OOPDemonstration.main(new String[]{});
```

This will demonstrate:
- Polymorphic method calls
- Inheritance relationships
- Encapsulated data access
- Abstract class benefits
- Real-world usage scenarios
- Admin functionality

## Benefits of This Design

1. **Maintainable**: Clear separation of concerns and responsibilities
2. **Extensible**: Easy to add new user types or password entry types
3. **Secure**: Encapsulated access with validation and tracking
4. **Flexible**: Polymorphic behavior based on runtime types
5. **Reusable**: Common functionality shared through inheritance

## Integration Notes

The new OOP model is designed to work alongside existing code:
- **User.java** maintains backward compatibility
- **EnhancedUserManager** works with existing UserManager
- Controllers can gradually adopt new models
- No breaking changes to existing UI code

See `OOP_REDESIGN_DOCUMENTATION.md` for detailed implementation explanations.
