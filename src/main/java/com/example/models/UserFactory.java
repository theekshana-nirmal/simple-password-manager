package com.example.models;

/**
 * UserFactory class for creating different types of users.
 * Demonstrates the Factory Pattern and encapsulation of user creation logic.
 * This class shows how to create appropriate user objects based on
 * requirements.
 */
public class UserFactory {

    // Private constructor to prevent instantiation
    private UserFactory() {
        throw new UnsupportedOperationException("UserFactory is a utility class and cannot be instantiated");
    }

    /**
     * Create a user based on user type
     * 
     * @param userType     The type of user to create ("guest", "normal", "admin")
     * @param username     The username
     * @param email        The email address
     * @param passwordHash The hashed password
     * @return BaseUser instance of the appropriate type
     */
    public static BaseUser createUser(String userType, String username, String email, String passwordHash) {
        if (userType == null) {
            throw new IllegalArgumentException("User type cannot be null");
        }

        switch (userType.toLowerCase()) {
            case "guest":
                return createGuestUser();
            case "normal":
            case "user":
                return createNormalUser(username, email, passwordHash);
            case "admin":
                return createAdminUser(username, email, passwordHash);
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }

    /**
     * Create a user based on user type with creation date
     * 
     * @param userType        The type of user to create
     * @param username        The username
     * @param email           The email address
     * @param passwordHash    The hashed password
     * @param createdAtString The creation date as string
     * @return BaseUser instance of the appropriate type
     */
    public static BaseUser createUser(String userType, String username, String email, String passwordHash,
            String createdAtString) {
        if (userType == null) {
            throw new IllegalArgumentException("User type cannot be null");
        }

        switch (userType.toLowerCase()) {
            case "guest":
                return createGuestUser();
            case "normal":
            case "user":
                return createNormalUser(username, email, passwordHash, createdAtString);
            case "admin":
                return createAdminUser(username, email, passwordHash, createdAtString);
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }

    /**
     * Create a guest user
     * 
     * @return GuestUser instance
     */
    public static GuestUser createGuestUser() {
        return new GuestUser();
    }

    /**
     * Create a normal user
     * 
     * @param username     The username
     * @param email        The email address
     * @param passwordHash The hashed password
     * @return NormalUser instance
     */
    public static NormalUser createNormalUser(String username, String email, String passwordHash) {
        validateUserParams(username, email, passwordHash);
        return new NormalUser(username, email, passwordHash);
    }

    /**
     * Create a normal user with creation date
     * 
     * @param username        The username
     * @param email           The email address
     * @param passwordHash    The hashed password
     * @param createdAtString The creation date as string
     * @return NormalUser instance
     */
    public static NormalUser createNormalUser(String username, String email, String passwordHash,
            String createdAtString) {
        validateUserParams(username, email, passwordHash);
        return new NormalUser(username, email, passwordHash, createdAtString);
    }

    /**
     * Create an admin user
     * 
     * @param username     The username
     * @param email        The email address
     * @param passwordHash The hashed password
     * @return AdminUser instance
     */
    public static AdminUser createAdminUser(String username, String email, String passwordHash) {
        validateUserParams(username, email, passwordHash);
        return new AdminUser(username, email, passwordHash);
    }

    /**
     * Create an admin user with creation date
     * 
     * @param username        The username
     * @param email           The email address
     * @param passwordHash    The hashed password
     * @param createdAtString The creation date as string
     * @return AdminUser instance
     */
    public static AdminUser createAdminUser(String username, String email, String passwordHash,
            String createdAtString) {
        validateUserParams(username, email, passwordHash);
        return new AdminUser(username, email, passwordHash, createdAtString);
    }

    /**
     * Create a user from existing User object
     * 
     * @param user     The existing User object
     * @param userType The desired user type
     * @return BaseUser instance of the appropriate type
     */
    public static BaseUser createUserFromExisting(User user, String userType) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return createUser(userType, user.getUsername(), user.getEmail(),
                user.getPasswordHash(), user.getCreatedAtString());
    }

    /**
     * Determine user type based on email domain or username patterns
     * 
     * @param email    The email address
     * @param username The username
     * @return Suggested user type
     */
    public static String determineUserType(String email, String username) {
        if (email == null && username == null) {
            return "guest";
        }

        // Check for admin patterns
        if ((email != null && (email.toLowerCase().contains("admin") || email.toLowerCase().endsWith("@admin.com"))) ||
                (username != null && username.toLowerCase().contains("admin"))) {
            return "admin";
        }

        // Check for guest patterns
        if ((email != null && email.toLowerCase().contains("guest")) ||
                (username != null && username.toLowerCase().contains("guest"))) {
            return "guest";
        }

        // Default to normal user
        return "normal";
    }

    /**
     * Create appropriate password entry for user type
     * 
     * @param userType The user type
     * @param website  The website
     * @param username The username
     * @param password The password
     * @return BasePasswordEntry instance
     */
    public static BasePasswordEntry createPasswordEntry(String userType, String website, String username,
            String password) {
        if (userType == null) {
            userType = "normal";
        }

        switch (userType.toLowerCase()) {
            case "guest":
                return new SamplePasswordEntry(website, username, password);
            case "normal":
            case "user":
            case "admin":
                return new PasswordEntry(website, username, password);
            default:
                return new PasswordEntry(website, username, password);
        }
    }

    /**
     * Create sample password entries for demo purposes
     * 
     * @return Array of sample password entries
     */
    public static SamplePasswordEntry[] createSamplePasswordEntries() {
        return new SamplePasswordEntry[] {
                new SamplePasswordEntry("Facebook", "demo@email.com", "demopassword123", "Social Media"),
                new SamplePasswordEntry("Gmail", "demouser123", "securemail456", "Email"),
                new SamplePasswordEntry("GitHub", "demo_developer", "coding789", "Development"),
                new SamplePasswordEntry("Netflix", "demo.user", "entertainment101", "Entertainment"),
                new SamplePasswordEntry("Amazon", "demo@example.com", "shopping234", "Shopping"),
                new SamplePasswordEntry("LinkedIn", "demo.professional", "career567", "Professional"),
                new SamplePasswordEntry("Instagram", "demo_photos", "social890", "Social Media")
        };
    }

    /**
     * Check if a user type is valid
     * 
     * @param userType The user type to check
     * @return true if valid user type
     */
    public static boolean isValidUserType(String userType) {
        if (userType == null) {
            return false;
        }

        String type = userType.toLowerCase();
        return type.equals("guest") || type.equals("normal") ||
                type.equals("user") || type.equals("admin");
    }

    /**
     * Get all supported user types
     * 
     * @return Array of supported user types
     */
    public static String[] getSupportedUserTypes() {
        return new String[] { "guest", "normal", "admin" };
    }

    // Private helper methods

    /**
     * Validate user creation parameters
     * 
     * @param username     The username
     * @param email        The email address
     * @param passwordHash The password hash
     */
    private static void validateUserParams(String username, String email, String passwordHash) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be null or empty");
        }
    }
}
