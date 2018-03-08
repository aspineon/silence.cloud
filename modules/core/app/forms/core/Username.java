package forms.core;

import play.data.validation.Constraints;

public class Username {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String username;
}
