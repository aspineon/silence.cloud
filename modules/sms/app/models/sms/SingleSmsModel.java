package models.sms;

import play.data.validation.Constraints;

public class SingleSmsModel {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String message;
}
