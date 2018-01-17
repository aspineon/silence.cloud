package helpers.core.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validate e-mail address.
 */
public interface EmailValidatable {

    String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Validate email with regular expression
     *
     * @param email
     * @return true valid email, false invalid email
     */
    default boolean validate(String email) {

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
