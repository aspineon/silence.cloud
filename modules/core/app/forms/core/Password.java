package forms.core;

import play.data.validation.Constraints;

public class Password {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String oldPassword;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String newPassword;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String confirmPassword;
}
