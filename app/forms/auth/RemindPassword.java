package forms.auth;

import play.data.validation.Constraints;

public class RemindPassword {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String userdata;
}
