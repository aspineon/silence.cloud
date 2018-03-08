package forms.core;

import play.data.validation.Constraints;

public class NewUser {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String username;

    @Constraints.Required
    @Constraints.MaxLength(255)
    @Constraints.Email
    public String email;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;

    @Constraints.Required
    @Constraints.MinLength(11)
    @Constraints.MaxLength(255)
    public String password;

    @Constraints.Required
    @Constraints.MinLength(11)
    @Constraints.MaxLength(255)
    public String confirmPassword;

    public String isAdmin;
}
