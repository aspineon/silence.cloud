package models.core.company;

import io.ebean.Finder;
import models.core.BaseModel;
import models.core.user.UserModel;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class CompanyModel extends BaseModel {

    @Column(nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String companyName;

    @Column(nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String companyPhone;

    @Column(nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    @Constraints.Email
    public String companyEmail;

    @Column(nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String companyAddress;

    @Column(nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String companyCity;

    @Column(nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String companyPostalCode;

    @Column(nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String companyCountry;

    @Column(nullable = false, unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String taxNumber;

    @ManyToOne
    @Column(name = "user_id")
    @Constraints.Required
    public UserModel user;

    @Column(name = "is_primary", nullable = false)
    @Constraints.Required
    public boolean isPrimary;

    public Finder<Long, CompanyModel> FINDER = new Finder<Long, CompanyModel>(CompanyModel.class);
}
