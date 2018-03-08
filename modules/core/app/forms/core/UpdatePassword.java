package forms.core;

import play.data.validation.Constraints;

public class UpdatePassword {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String password;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String confirmPassword;
}
