package forms.auth;

import play.data.validation.Constraints;

public class SignIn {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String userdata;

    @Constraints.Required
    @Constraints.MinLength(11)
    @Constraints.MaxLength(255)
    public String password;
}
