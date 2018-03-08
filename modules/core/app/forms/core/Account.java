package forms.core;

import play.data.validation.Constraints;

public class Account {

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

}
