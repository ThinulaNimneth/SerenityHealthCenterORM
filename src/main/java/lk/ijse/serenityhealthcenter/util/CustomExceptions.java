package lk.ijse.serenityhealthcenter.util;

public class CustomExceptions {


    public static class ApplicationException extends Exception {
        public ApplicationException(String message) {
            super(message);
        }

        public ApplicationException(String message, Throwable cause) {
            super(message, cause);
        }
    }


    public static class RegistrationException extends ApplicationException {
        public RegistrationException(String message) {
            super(message);
        }

        public RegistrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DuplicateEntryException extends RegistrationException {
        public DuplicateEntryException(String field, String value) {
            super(String.format("Duplicate entry: %s '%s' already exists", field, value));
        }
    }


    public static class MissingFieldException extends RegistrationException {
        public MissingFieldException(String field) {
            super(String.format("Required field is missing: %s", field));
        }
    }


    public static class LoginException extends ApplicationException {
        public LoginException(String message) {
            super(message);
        }
    }


    public static class InvalidCredentialsException extends LoginException {
        public InvalidCredentialsException() {
            super("Invalid username or password");
        }
    }


    public static class AccountLockedException extends LoginException {
        public AccountLockedException() {
            super("Account is locked. Please contact administrator.");
        }
    }


    public static class PaymentException extends ApplicationException {
        public PaymentException(String message) {
            super(message);
        }

        public PaymentException(String message, Throwable cause) {
            super(message, cause);
        }
    }


    public static class InsufficientPaymentException extends PaymentException {
        public InsufficientPaymentException(String required, String provided) {
            super(String.format("Insufficient payment: Required LKR %s, Provided LKR %s", required, provided));
        }
    }


    public static class SchedulingException extends ApplicationException {
        public SchedulingException(String message) {
            super(message);
        }
    }


    public static class SchedulingConflictException extends SchedulingException {
        public SchedulingConflictException(String details) {
            super(String.format("Scheduling conflict: %s", details));
        }
    }


    public static class TherapistUnavailableException extends SchedulingException {
        public TherapistUnavailableException(String therapistName, String date) {
            super(String.format("Therapist %s is not available on %s", therapistName, date));
        }
    }


    public static class ValidationException extends ApplicationException {
        public ValidationException(String field, String message) {
            super(String.format("Validation failed for %s: %s", field, message));
        }
    }


    /**
     * database operation
     */
    public static class DatabaseException extends ApplicationException {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
