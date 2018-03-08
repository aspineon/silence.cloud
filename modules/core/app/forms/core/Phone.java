package forms.core;

import play.data.validation.Constraints;

public class Phone {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;
}
