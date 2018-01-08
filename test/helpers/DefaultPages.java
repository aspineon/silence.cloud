package helpers;

import models.core.PageModel;
import models.core.StatusModel;

import java.util.Date;
import java.util.List;

public class DefaultPages {

    private Long activeStatusId = 1L;

    private Long firstSamplePageId = 1L;
    private Long secondSamplePageId = 2L;

    private String firstSamplePageName = "samplePageName";
    private String firstSamplePageTitle = "samplePageTitle";
    private String firstSamplePageDescription = "samplePageDescription";
    private String firstSamplePageKeywords = "samplePageKeywords";
    private String firstSamplePageOgTitle = "samplePageOgTitle";
    private String firstSamplePageOgType = "samplePageOgType";
    private String firstSamplePageOgImage = "samplePageOgImage";
    private String firstSamplePageOgUrl = "samplePageOgUrl";
    private String firstSamplePageOgDescription = "samplePageOgDescription";
    private String firstSamplePageTwitterCard = "samplePageTwitterCard";
    private String firstSamplePageTwitterUrl = "samplePageTwitterUrl";
    private String firstSamplePageTwitterTitle = "samplePageTwitterTitle";
    private String firstSamplePageTwitterDescription = "samplePageTwitterDescription";
    private String firstSamplePageTwitterImage = "samplePageTwitterImage";

    private String secondSamplePageName = "samplePageName";
    private String secondSamplePageTitle = "samplePageTitle";
    private String secondSamplePageDescription = "samplePageDescription";
    private String secondSamplePageKeywords = "samplePageKeywords";
    private String secondSamplePageOgTitle = "samplePageOgTitle";
    private String secondSamplePageOgType = "samplePageOgType";
    private String secondSamplePageOgImage = "samplePageOgImage";
    private String secondSamplePageOgUrl = "samplePageOgUrl";
    private String secondSamplePageOgDescription = "samplePageOgDescription";
    private String secondSamplePageTwitterCard = "samplePageTwitterCard";
    private String secondSamplePageTwitterUrl = "samplePageTwitterUrl";
    private String secondSamplePageTwitterTitle = "samplePageTwitterTitle";
    private String secondSamplePageTwitterDescription = "samplePageTwitterDescription";
    private String secondSamplePageTwitterImage = "samplePageTwitterImage";

    public void createPages(){
        createPage(firstSamplePageId, firstSamplePageName, activeStatusId, firstSamplePageTitle, firstSamplePageDescription,
                   firstSamplePageKeywords, firstSamplePageOgTitle, firstSamplePageOgType, firstSamplePageOgImage,
                   firstSamplePageOgUrl, firstSamplePageOgDescription, firstSamplePageTwitterCard, firstSamplePageTwitterUrl,
                   firstSamplePageTwitterTitle, firstSamplePageTwitterDescription, firstSamplePageTwitterImage);

        createPage(secondSamplePageId, secondSamplePageName, activeStatusId, secondSamplePageTitle, secondSamplePageDescription,
                secondSamplePageKeywords, secondSamplePageOgTitle, secondSamplePageOgType, secondSamplePageOgImage,
                secondSamplePageOgUrl, secondSamplePageOgDescription, secondSamplePageTwitterCard, secondSamplePageTwitterUrl,
                secondSamplePageTwitterTitle, secondSamplePageTwitterDescription, secondSamplePageTwitterImage);
    }

    public void deletePages() {
        List<PageModel> pages = PageModel.FINDER.all();
        for (PageModel page : pages) {
            page.delete();
        }
    }

    private void createPage(Long pageId, String name, Long statusId, String title, String description,
                            String keywords, String ogTitle, String ogType, String ogImage,
                            String ogUrl, String ogDescription, String twitterCard,
                            String twitterUrl, String twitterTitle,
                            String twitterDescription, String twitterImage) {
        PageModel pageModel = new PageModel();
        pageModel.id = pageId;
        pageModel.name = name;
        pageModel.status = StatusModel.FINDER.ref(statusId);
        pageModel.createdAt = new Date();
        pageModel.updateAt = new Date();
        pageModel.title = title;
        pageModel.description = description;
        pageModel.keywords = keywords;
        pageModel.ogTitle = ogTitle;
        pageModel.ogType = ogType;
        pageModel.ogImage = ogImage;
        pageModel.ogUrl = ogUrl;
        pageModel.ogDescription = ogDescription;
        pageModel.twitterCard = twitterCard;
        pageModel.twitterUrl = twitterUrl;
        pageModel.twitterTitle = twitterTitle;
        pageModel.twitterDescription = twitterDescription;
        pageModel.twitterImage = twitterImage;
        pageModel.save();
    }
}
