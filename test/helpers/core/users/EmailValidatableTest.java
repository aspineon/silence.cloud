package helpers.core.users;

import helpers.core.user.EmailValidatable;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class EmailValidatableTest implements EmailValidatable {

    // list of valid email addresses
    private final String[] validEmailIds = new String[] { "test@yahoo.com", "test-100@yahoo.com",
            "test.100@yahoo.com", "test111@test.com", "test-100@test.net",
            "test.100@test.com.au", "test@1.com", "test@gmail.com.com",
            "test+100@gmail.com", "test-100@yahoo-test.com", "test_100@yahoo-test.ABC.CoM" };

    // list of invalid email addresses
    private final String[] invalidEmailIds = new String[] { "test", "test@.com.my",
            "test123@gmail.a", "test123@.com", "test123@.com.com", ".test@test.com",
            "test()*@gmail.com", "test@%*.com", "test..2002@gmail.com", "test.@gmail.com",
            "test@test@gmail.com", "test@gmail.com.1a" };

    @Test
    public void validEmailsTest() {

        for(String email: validEmailIds){

            assertTrue(EmailValidatable.super.validate(email));
        }
    }

    @Test
    public void invalidEmailTests() {

        for(String email: invalidEmailIds) {

            assertFalse(EmailValidatable.super.validate(email));
        }
    }
}
