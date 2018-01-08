package integration.models.core;

import helpers.BeforeAndAfterTest;
import models.core.PageModelCrud;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class PageIntegrationTest extends BeforeAndAfterTest implements PageModelCrud {

    private Long firstSamplePageId = 1L;
    private Long secondSamplePageId = 2L;

    private String firstSamplePageName = "samplePageName";
    private String firstSamplePageTitle = "samplePageTitle";
    private String firstSamplePageOgTitle = "samplePageOgTitle";
    private String firstSamplePageOgImage = "samplePageOgImage";
    private String firstSamplePageOgUrl = "samplePageOgUrl";
    private String firstSamplePageTwitterUrl = "samplePageTwitterUrl";
    private String firstSamplePageTwitterTitle = "samplePageTwitterTitle";
    private String firstSamplePageTwitterImage = "samplePageTwitterImage";

    private String secondSamplePageName = "samplePageName";
    private String secondSamplePageTitle = "samplePageTitle";
    private String secondSamplePageOgTitle = "samplePageOgTitle";
    private String secondSamplePageOgImage = "samplePageOgImage";
    private String secondSamplePageOgUrl = "samplePageOgUrl";
    private String secondSamplePageTwitterUrl = "samplePageTwitterUrl";
    private String secondSamplePageTwitterTitle = "samplePageTwitterTitle";
    private String secondSamplePageTwitterImage = "samplePageTwitterImage";

    @Test
    public void firstSamplePageTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageById(firstSamplePageId));
            assertNotNull(PageModelCrud.super.findPageByName(firstSamplePageName));
            assertNotNull(PageModelCrud.super.findPageByTitle(firstSamplePageTitle));
            assertNotNull(PageModelCrud.super.findPageByOgTitle(firstSamplePageOgTitle));
            assertNotNull(PageModelCrud.super.findPageByOgImage(firstSamplePageOgImage));
            assertNotNull(PageModelCrud.super.findPageByOgUrl(firstSamplePageOgUrl));
            assertNotNull(PageModelCrud.super.findPageByTwitterUrl(firstSamplePageTwitterUrl));
            assertNotNull(PageModelCrud.super.findPageByTwitterTitle(firstSamplePageTwitterTitle));
            assertNotNull(PageModelCrud.super.findPageByTwitterImage(firstSamplePageTwitterImage));

        });
    }

    public void secondSamplePageTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () ->{

            assertNotNull(PageModelCrud.super.findPageById(secondSamplePageId));
            assertNotNull(PageModelCrud.super.findPageByName(secondSamplePageName));
            assertNotNull(PageModelCrud.super.findPageByTitle(secondSamplePageTitle));
            assertNotNull(PageModelCrud.super.findPageByOgTitle(secondSamplePageOgTitle));
            assertNotNull(PageModelCrud.super.findPageByOgImage(secondSamplePageOgImage));
            assertNotNull(PageModelCrud.super.findPageByOgUrl(secondSamplePageOgUrl));
            assertNotNull(PageModelCrud.super.findPageByTwitterUrl(secondSamplePageTwitterUrl));
            assertNotNull(PageModelCrud.super.findPageByTwitterTitle(secondSamplePageTwitterTitle));
            assertNotNull(PageModelCrud.super.findPageByTwitterImage(secondSamplePageTwitterImage));

        });
    }
}
