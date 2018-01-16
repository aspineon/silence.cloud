package forms.auth;

import play.data.validation.Constraints;

public class SignUp {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String username;

    @Constraints.Required
    @Constraints.Email
    @Constraints.MaxLength(255)
    public String email;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;

    @Constraints.Required
    @Constraints.MinLength(9)
    @Constraints.MaxLength(255)
    public String password;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String confirmPassword;
}
