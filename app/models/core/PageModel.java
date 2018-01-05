package models.core;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * models.core.PageModel.java
 * <p>
 * Model of application pages.
 */
@Entity
@Table(name = "page")
public class PageModel extends BaseModel{

    @ManyToOne
    @Column(name = "status_id")
    @Constraints.Required
    public StatusModel status;

    @Column(name = "page_name", unique = true, nullable = false)
    @Constraints.Required
    public String name;

    @Column(name = "title", length = 71, unique = true, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(71)
    public String title;

    @Column(name = "description", length = 155)
    @Constraints.Required
    @Constraints.MaxLength(155)
    public String description;

    @Column(name = "keywords", length = 1000)
    @Constraints.Required
    @Constraints.MaxLength(1000)
    public String keywords;

    @Column(name = "ogTitle", length = 96, unique = true, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(96)
    public String ogTitle;

    @Column(name = "ogType", length = 255, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String ogType;

    @Column(name = "ogImage", length = 1000, unique = true, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(1000)
    public String ogImage;

    @Column(name = "ogUrl", length = 1000, unique = true, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(1000)
    public String ogUrl;

    @Column(name = "ogDescription", length = 300, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(300)
    public String ogDescription;

    @Column(name = "twitterCard", length = 255, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String twitterCard;

    @Column(name = "twitterUrl", length = 1000, unique = true, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(1000)
    public String twitterUrl;

    @Column(name = "twitterTitle", length = 71, unique = true, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(71)
    public String twitterTitle;

    @Column(name = "twitterDescription", length = 201, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(201)
    public String twitterDescription;

    @Column(name = "twitterImage", length = 1000, unique = true, nullable = false)
    @Constraints.Required
    @Constraints.MaxLength(1000)
    public String twitterImage;

    public static Finder<Long, PageModel> FINDER = new Finder(PageModel.class);
}
