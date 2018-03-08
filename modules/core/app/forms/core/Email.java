package forms.core;

import play.data.validation.Constraints;

public class Email {

    @Constraints.Required
    @Constraints.MaxLength(255)
    @Constraints.Email
    public String email;
}
