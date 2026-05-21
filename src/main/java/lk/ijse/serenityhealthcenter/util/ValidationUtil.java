package lk.ijse.serenityhealthcenter.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    // phone number
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(0\\d{9}|\\+94\\d{9})$"
    );
    //user name
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_]{3,20}$"
    );
    //name
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-Z\\s'-]{2,100}$"
    );
    // lison num
    private static final Pattern LICENSE_PATTERN = Pattern.compile(
            "^[A-Z0-9-]{5,20}$"
    );
    // amount
    private static final Pattern AMOUNT_PATTERN = Pattern.compile(
            "^\\d+(\\.\\d{1,2})?$"
    );

    //email
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    //phone num
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    // user name
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

   // name (person name)
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    //license number
    public static boolean isValidLicense(String license) {
        return license != null && LICENSE_PATTERN.matcher(license).matches();
    }

    //amount
    public static boolean isValidAmount(String amount) {
        return amount != null && AMOUNT_PATTERN.matcher(amount).matches();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    //password
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        return hasLetter && hasDigit;
    }
}
