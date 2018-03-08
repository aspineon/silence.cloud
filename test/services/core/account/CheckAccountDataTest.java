package services.core.account;

import forms.core.Account;
import helpers.BeforeAndAfterTest;
import models.core.user.UserByIdFindable;
import models.core.user.UserModel;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class CheckAccountDataTest extends BeforeAndAfterTest implements UserByIdFindable {

    private Long    firstId             = 1L;
    private String  firstUsername       = "John Doe";
    private String  firstEmail          = "john@doe.com";
    private String  firstPhone          = "000000000";

    private String  secondEmail         = "john@smith.com";
    private String  secondPhone         = "1111111111";

    private String notExistsUserName    = "r3v";
    private String notExistsEmail       = "jonathan@doe.com";
    private String notExistsPhone       = "000000001";

    @Test
    public void namesAreTheSame() {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CheckAccountData checkAccountData = app.injector().instanceOf(CheckAccountData.class);
            Account account = new Account();
            account.username = firstUsername;
            account.email = notExistsEmail;
            account.phone = notExistsPhone;

            Optional<UserModel> user = UserByIdFindable.super.findUserById(firstId);

            assertTrue(user.isPresent());
            assertNotNull(user.get());

            final Map<String, Map> result = checkAccountData.checkAccount(account, user.get());
            assertNotNull(result.get("username"));
            assertNotNull(result.get("username").get("warning"));
            assertNull(result.get("email"));
            assertNull(result.get("phone"));

        });
    }

    @Test
    public void emailsAreTheSame(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CheckAccountData checkAccountData = app.injector().instanceOf(CheckAccountData.class);

            Optional<UserModel> user = UserByIdFindable.super.findUserById(firstId);

            assertTrue(user.isPresent());
            assertNotNull(user.get());

            Account account = new Account();
            account.username = notExistsUserName;
            account.email = firstEmail;
            account.phone = notExistsPhone;

            final Map<String, Map> result = checkAccountData.checkAccount(account, user.get());
            assertNull(result.get("username"));
            assertNotNull(result.get("email"));
            assertNull(result.get("email").get("danger"));
            assertNotNull(result.get("email").get("warning"));
        });
    }

    @Test
    public void emailExists(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CheckAccountData checkAccountData = app.injector().instanceOf(CheckAccountData.class);

            Optional<UserModel> user = UserByIdFindable.super.findUserById(firstId);
            assertTrue(user.isPresent());
            assertNotNull(user.get());

            Account account = new Account();
            account.username = notExistsUserName;
            account.email = secondEmail;
            account.phone = notExistsPhone;

            final Map<String, Map> result = checkAccountData.checkAccount(account, user.get());
            assertNull(result.get("username"));
            assertNotNull(result.get("email"));
            assertNull(result.get("email").get("warning"));
            assertNotNull(result.get("email").get("danger"));
            assertNull(result.get("phone"));
        });
    }

    @Test
    public void phonesAreTheSame(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CheckAccountData checkAccountData = app.injector().instanceOf(CheckAccountData.class);

            Optional<UserModel> user = UserByIdFindable.super.findUserById(firstId);
            assertTrue(user.isPresent());
            assertNotNull(user.get());

            Account account = new Account();
            account.username = notExistsUserName;
            account.email = notExistsEmail;
            account.phone = firstPhone;

            final Map<String, Map> result = checkAccountData.checkAccount(account, user.get());
            assertNull(result.get("username"));
            assertNull(result.get("email"));
            assertNotNull(result.get("phone"));
            assertNotNull(result.get("phone").get("warning"));
            assertNull(result.get("phone").get("danger"));
        });
    }

    @Test
    public void phoneExists(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CheckAccountData checkAccountData = app.injector().instanceOf(CheckAccountData.class);

            Optional<UserModel> user = UserByIdFindable.super.findUserById(firstId);
            assertTrue(user.isPresent());
            assertNotNull(user.get());

            Account account = new Account();
            account.username = notExistsUserName;
            account.email = notExistsEmail;
            account.phone = secondPhone;

            final Map<String, Map> result = checkAccountData.checkAccount(account, user.get());
            assertNull(result.get("username"));
            assertNull(result.get("email"));
            assertNotNull(result.get("phone"));
            assertNull(result.get("phone").get("warning"));
            assertNotNull(result.get("phone").get("danger"));
        });
    }

    @Test
    public void allValuesNotExists(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final CheckAccountData checkAccountData = app.injector().instanceOf(CheckAccountData.class);

            Optional<UserModel> user = UserByIdFindable.super.findUserById(firstId);
            assertTrue(user.isPresent());
            assertNotNull(user.get());

            Account account = new Account();
            account.username = notExistsUserName;
            account.email = notExistsEmail;
            account.phone = notExistsPhone;

            final Map<String, Map> result = checkAccountData.checkAccount(account, user.get());
            assertNull(result.get("username"));
            assertNull(result.get("email"));
            assertNull(result.get("phone"));
        });
    }
}
