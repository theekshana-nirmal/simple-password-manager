package com.example.models;

/**
 * Enhanced UserManager that works with the new OOP model structure.
 * Demonstrates proper encapsulation and polymorphism in user management.
 * This class serves as a bridge between the new OOP models and existing utility
 * classes.
 */
public class EnhancedUserManager {

    // Encapsulated current user - only accessible through controlled methods
    private static BaseUser currentUser = null;

    // Static instance for singleton pattern (optional enhancement)
    private static EnhancedUserManager instance = null;

    // Private constructor for singleton pattern
    private EnhancedUserManager() {
        // Private constructor to control instantiation
    }

    /**
     * Get singleton instance (optional pattern demonstration)
     * 
     * @return EnhancedUserManager instance
     */
    public static EnhancedUserManager getInstance() {
        if (instance == null) {
            instance = new EnhancedUserManager();
        }
        return instance;
    }

    /**
     * Register a new user using the enhanced OOP model
     * 
     * @param username The username
     * @param email    The email address
     * @param password The plain text password
     * @param userType The type of user to create ("normal", "admin")
     * @return true if registration successful
     */
    public static boolean registerUser(String username, String email, String password, String userType) {
        try {
            // Validate input parameters
            if (!isValidRegistrationData(username, email, password, userType)) {
                return false;
            }

            // Check if user already exists using existing UserManager
            if (com.example.utils.UserManager.findUserByUsername(username) != null ||
                    com.example.utils.UserManager.findUserByEmail(email) != null) {
                System.out.println("User already exists with username or email");
                return false;
            }

            // Create password hash
            String passwordHash = com.example.utils.PasswordUtils.createPasswordHash(password);
            // Create user using factory pattern (for validation and logging)
            UserFactory.createUser(userType, username, email, passwordHash);

            // Save using existing UserManager
            boolean saved = com.example.utils.UserManager.registerUser(username, email, password);

            if (saved) {
                System.out.println("Successfully registered " + userType + " user: " + username);
                auditLog("User registration", "New " + userType + " user registered: " + username);
                return true;
            }

        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            auditLog("Registration error", "Failed to register user " + username + ": " + e.getMessage());
        }

        return false;
    }

    /**
     * Authenticate user using enhanced OOP model
     * 
     * @param usernameOrEmail Username or email
     * @param password        Plain text password
     * @return BaseUser object if authentication successful, null otherwise
     */
    public static BaseUser authenticateUser(String usernameOrEmail, String password) {
        try {
            // Use existing UserManager for authentication
            boolean authenticated = com.example.utils.UserManager.authenticateUser(usernameOrEmail, password);

            if (authenticated) {
                // Get the authenticated user from UserManager
                User legacyUser = com.example.utils.UserManager.getCurrentUser();

                if (legacyUser != null) {
                    // Determine user type and create appropriate BaseUser object
                    String userType = determineUserType(legacyUser);
                    BaseUser enhancedUser = UserFactory.createUserFromExisting(legacyUser, userType);

                    // Set as current user and perform login actions
                    setCurrentUser(enhancedUser);
                    enhancedUser.setLoggedIn(true);

                    auditLog("User login",
                            "User authenticated: " + enhancedUser.getUsername() + " (Type: " + userType + ")");
                    return enhancedUser;
                }
            }

        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            auditLog("Authentication error", "Failed to authenticate user " + usernameOrEmail + ": " + e.getMessage());
        }

        return null;
    }

    /**
     * Create guest user session
     * 
     * @return GuestUser object
     */
    public static GuestUser createGuestSession() {
        GuestUser guestUser = UserFactory.createGuestUser();
        setCurrentUser(guestUser);
        guestUser.setLoggedIn(true);
        auditLog("Guest session", "Guest user session created");
        return guestUser;
    }

    /**
     * Authenticate admin user
     * 
     * @param email    Admin email
     * @param password Admin password
     * @return AdminUser object if authentication successful, null otherwise
     */
    public static AdminUser authenticateAdmin(String email, String password) {
        try {
            // Use existing AdminManager for authentication
            boolean authenticated = com.example.utils.AdminManager.authenticateAdmin(email, password);

            if (authenticated) {
                // Create AdminUser object
                String passwordHash = com.example.utils.PasswordUtils.createPasswordHash(password);
                AdminUser adminUser = UserFactory.createAdminUser("admin", email, passwordHash);

                // Set as current user and perform login actions
                setCurrentUser(adminUser);
                adminUser.setLoggedIn(true);

                auditLog("Admin login", "Admin user authenticated: " + email);
                return adminUser;
            }

        } catch (Exception e) {
            System.err.println("Error during admin authentication: " + e.getMessage());
            auditLog("Admin auth error", "Failed to authenticate admin " + email + ": " + e.getMessage());
        }

        return null;
    }

    /**
     * Get currently logged in user
     * 
     * @return Current BaseUser or null if not logged in
     */
    public static BaseUser getCurrentUser() {
        return currentUser;
    }

    /**
     * Set current user (internal method)
     * 
     * @param user The user to set as current
     */
    private static void setCurrentUser(BaseUser user) {
        if (currentUser != null && currentUser.isLoggedIn()) {
            currentUser.setLoggedIn(false); // Logout previous user
        }
        currentUser = user;
    }

    /**
     * Logout current user
     */
    public static void logout() {
        if (currentUser != null) {
            String username = currentUser.getUsername();
            String userType = currentUser.getUserType();

            currentUser.setLoggedIn(false);
            currentUser = null;

            // Also logout from legacy UserManager
            com.example.utils.UserManager.logout();

            auditLog("User logout", "User logged out: " + username + " (Type: " + userType + ")");
            System.out.println("User logged out successfully");
        }
    }

    /**
     * Check if user is currently logged in
     * 
     * @return true if user is logged in
     */
    public static boolean isUserLoggedIn() {
        return currentUser != null && currentUser.isLoggedIn();
    }

    /**
     * Get current user type
     * 
     * @return User type string or "none" if not logged in
     */
    public static String getCurrentUserType() {
        return currentUser != null ? currentUser.getUserType() : "none";
    }

    /**
     * Check if current user has admin privileges
     * 
     * @return true if current user is admin
     */
    public static boolean currentUserIsAdmin() {
        return currentUser != null && currentUser.hasAdminPrivileges();
    }

    /**
     * Check if current user can manage passwords
     * 
     * @return true if current user can manage passwords
     */
    public static boolean currentUserCanManagePasswords() {
        return currentUser != null && currentUser.canManagePasswords();
    }

    /**
     * Delete user (admin action)
     * 
     * @param userEmail Email of user to delete
     * @return true if deletion successful
     */
    public static boolean deleteUser(String userEmail) {
        if (!currentUserIsAdmin()) {
            System.out.println("Only admin users can delete users");
            return false;
        }

        if (currentUser instanceof AdminUser) {
            AdminUser admin = (AdminUser) currentUser;
            return admin.deleteUser(userEmail);
        }

        return false;
    }

    /**
     * Get all users (admin action)
     * 
     * @return List of all users or empty list if not admin
     */
    public static java.util.List<BaseUser> getAllUsers() {
        if (!currentUserIsAdmin()) {
            System.out.println("Only admin users can view all users");
            return new java.util.ArrayList<>();
        }

        if (currentUser instanceof AdminUser) {
            AdminUser admin = (AdminUser) currentUser;
            return admin.getAllUsers();
        }

        return new java.util.ArrayList<>();
    }

    /**
     * Change user password
     * 
     * @param currentPassword Current password
     * @param newPassword     New password
     * @return true if password change successful
     */
    public static boolean changePassword(String currentPassword, String newPassword) {
        if (currentUser == null) {
            System.out.println("No user is logged in");
            return false;
        }

        if (!currentUser.validateCredentials(currentPassword)) {
            System.out.println("Current password is incorrect");
            return false;
        }

        if (currentUser instanceof AdminUser) {
            AdminUser admin = (AdminUser) currentUser;
            return admin.changeAdminPassword(currentPassword, newPassword);
        } else {
            currentUser.updatePassword(newPassword);
            auditLog("Password change", "Password changed for user: " + currentUser.getUsername());
            return true;
        }
    }

    /**
     * Get current user statistics
     * 
     * @return User statistics string
     */
    public static String getCurrentUserStatistics() {
        if (currentUser == null) {
            return "No user logged in";
        }

        if (currentUser instanceof NormalUser) {
            return ((NormalUser) currentUser).getUserStatistics();
        } else if (currentUser instanceof AdminUser) {
            return ((AdminUser) currentUser).getAdminStatistics();
        } else if (currentUser instanceof GuestUser) {
            return "Guest User - Demo Mode";
        }

        return "User: " + currentUser.getUsername() + " (Type: " + currentUser.getUserType() + ")";
    }

    // Private helper methods

    /**
     * Determine user type from legacy User object
     * 
     * @param user Legacy User object
     * @return User type string
     */
    private static String determineUserType(User user) {
        if (user.getEmail().toLowerCase().contains("admin") ||
                user.getUsername().toLowerCase().contains("admin")) {
            return "admin";
        }
        return "normal"; // Default to normal user
    }

    /**
     * Validate registration data
     * 
     * @param username Username
     * @param email    Email
     * @param password Password
     * @param userType User type
     * @return true if data is valid
     */
    private static boolean isValidRegistrationData(String username, String email, String password, String userType) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username cannot be empty");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email cannot be empty");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Password must be at least 6 characters long");
            return false;
        }

        if (!UserFactory.isValidUserType(userType)) {
            System.out.println("Invalid user type: " + userType);
            return false;
        }

        return true;
    }

    /**
     * Audit log method for tracking user actions
     * 
     * @param action  Action performed
     * @param details Action details
     */
    private static void auditLog(String action, String details) {
        String logEntry = String.format("[%s] %s: %s",
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                action, details);
        System.out.println("ENHANCED USER MANAGER LOG: " + logEntry);
    }
}
