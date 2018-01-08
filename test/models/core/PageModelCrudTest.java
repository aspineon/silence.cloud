package models.core;

import helpers.BeforeAndAfterTest;
import org.junit.*;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;


public class PageModelCrudTest extends BeforeAndAfterTest implements PageModelCrud {

    private Long activeStatusId = 1L;
    private Long inactiveStatusId = 2L;
    private Long notExistsStatusId = 100L;

    private Long samplePageId = 1L;
    private Long notExistsPageId = 100L;

    private int firstExpectedSize = 13;

    private String newPageName = "newPage";
    private String updatedPage = "updatedPage";
    private String samplePageName = "samplePage";
    private String notExistsPageName = "notExistsPageName";

    private String newTitleName = "newTitle";
    private String updatedTitle = "updatedTitle";
    private String sampleTitleName = "sampleTitle";
    private String notExistsTitleName = "notExistsTitleName";

    private String newDescriptionName = "newDescription";
    private String updatedDescription = "updatedDescription";
    private String sampleDescriptionName = "sampleDescription";
    private String notExistsDescriptionName = "notExistsDescriptionName";

    private String newKeywordsName = "newKeywords";
    private String updatedKeywords = "updatedKeywords";
    private String sampleKeywordsName = "sampleKeywords";
    private String notExistsKeywordsName = "notExistsKeywordsName";

    private String newOgTitleName = "newOgTitle";
    private String updatedOgTitle = "updatedOgTitle";
    private String sampleOgTitleName = "sampleOgTitle";
    private String notExistsOgTitleName = "notExistsOgTitleName";

    private String newOgTypeName = "newOgType";
    private String updatedOgType = "updatedOgType";
    private String sampleOgTypeName = "sampleOgType";
    private String notExistsOgTypeName = "notExistsOgTypeName";

    private String newOgImageName = "newOgImage";
    private String updatedOgImage = "updatedOgImage";
    private String sampleOgImageName = "sampleOgImage";
    private String notExistsOgImageName = "notExistsOgImageName";

    private String newOgUrlName = "newOgUrl";
    private String updatedOgUrl = "updatedOgUrl";
    private String sampleOgUrlName = "sampleOgUrl";
    private String notExistsOgUrlName = "notExistsOgUrlName";

    private String newOgDescriptionName = "newOgDescription";
    private String updatedOgDescription = "updatedOgDescription";
    private String sampleOgDescriptionName = "sampleOgDescription";
    private String notExistsOgDescriptionName = "notExistsOgDescriptionName";

    private String newTwitterCardName = "newTwitterCard";
    private String updatedTwitterCard = "updatedTwitterCard";
    private String sampleTwitterCardName = "sampleTwitterCard";
    private String notExistsTwitterCardName = "notExistsTwitterCardName";

    private String newTwitterUrlName = "newTwitterUrl";
    private String updatedTwitterUrl = "updatedTwitterUrl";
    private String sampleTwitterUrlName = "sampleTwitterUrl";
    private String notExistsTwitterUrlName = "notExistsTwitterUrlName";

    private String newTwitterTitleName = "newTwitterTitle";
    private String updatedTwitterTitle = "updatedTwitterTitle";
    private String sampleTwitterTitleName = "sampleTwitterTitle";
    private String notExistsTwitterTitleName = "notExistsTwitterTitleName";

    private String newTwitterDescriptionName = "newTwitterDescription";
    private String updatedTwitterDescription = "updatedTwitterDescription";
    private String sampleTwitterDescriptionName = "sampleTwitterDescription";
    private String notExistsTwitterDescriptionName = "notExistsTwitterDescriptionName";

    private String newTwitterImageName = "newTwitterImage";
    private String updatedTwitterImage = "updatedTwitterImage";
    private String sampleTwitterImageName = "sampleTwitterImage";
    private String notExistsTwitterImageName = "notExistsTwitterImageName";

    @Test
    public void findAllPagesTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertEquals(firstExpectedSize, PageModelCrud.super.findAllPages().size());
        });
    }

    @Test
    public void findPageByExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageById(activeStatusId));
        });
    }

    @Test
    public void findPageByNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageById(notExistsStatusId));
        });
    }

    @Test
    public void findPageByExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageByName(samplePageName));
        });
    }

    @Test
    public void findPageByExistsTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageByTitle(sampleTitleName));
        });
    }

    @Test
    public void findPageByExistsOgTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageByOgTitle(sampleOgTitleName));
        });
    }

    @Test
    public void findPageByExistsOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageByOgImage(sampleOgImageName));
        });
    }

    @Test
    public void findPageByExistsOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageByOgUrl(sampleOgUrlName));
        });
    }

    @Test
    public void findPageByExistsTwitterUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageByTwitterUrl(sampleTwitterUrlName));
        });
    }

    @Test
    public void findPageByExistsTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageByTwitterTitle(sampleTwitterTitleName));
        });
    }

    @Test
    public void findPageByExistsTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.findPageByTwitterImage(sampleTwitterImageName));
        });
    }

    @Test
    public void findAllPagesByStatusTest() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            assertEquals(firstExpectedSize, PageModelCrud.super.findAllPagesByStatus(activeStatusId).size());
        });
    }

    @Test
    public void findPageByNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageByName(notExistsPageName));
        });
    }

    @Test
    public void findPageByNotExistsTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageByTitle(notExistsTitleName));
        });
    }

    @Test
    public void findPageByNotExistsOgTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageByOgTitle(notExistsOgTitleName));
        });
    }

    @Test
    public void findPageByNotExistsOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageByOgImage(notExistsOgImageName));
        });
    }

    @Test
    public void findPageByNotExistsOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageByOgUrl(notExistsOgUrlName));
        });
    }

    @Test
    public void findPageByNotExistsTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageByTwitterTitle(notExistsTwitterTitleName));
        });
    }

    @Test
    public void findPageByNotExistsTwitterUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageByTwitterUrl(notExistsTwitterUrlName));
        });
    }

    @Test
    public void findPageByNotExistsTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.findPageByTwitterImage(notExistsTwitterImageName));
        });
    }

    @Test
    public void createPageWithExistsNameTest() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.createPage(activeStatusId, samplePageName, sampleTitleName, sampleDescriptionName,
                    sampleKeywordsName, sampleOgTitleName, sampleOgTypeName, sampleOgImageName, sampleOgUrlName, sampleOgDescriptionName,
                    sampleTwitterCardName, sampleTwitterUrlName, sampleTwitterTitleName, sampleTwitterDescriptionName, sampleTwitterImageName));
        });
    }

    @Test
    public void createPageWithNotExistsStatus() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.createPage(notExistsStatusId, samplePageName, sampleTitleName, sampleDescriptionName,
                    sampleKeywordsName, sampleOgTitleName, sampleOgTypeName, sampleOgImageName, sampleOgUrlName, sampleOgDescriptionName,
                    sampleTwitterCardName, sampleTwitterUrlName, sampleTwitterTitleName, sampleTwitterDescriptionName, sampleTwitterImageName));
        });
    }

    @Test
    public void createNewPage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.createPage(activeStatusId, newPageName, newTitleName, newDescriptionName,
                    newKeywordsName, newOgTitleName, newOgTypeName, newOgImageName, newOgUrlName, newOgDescriptionName,
                    newTwitterCardName, newTwitterUrlName, newTwitterTitleName, newTwitterDescriptionName, newTwitterImageName));
        });
    }

    @Test
    public void updateNotExistsPage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageName(notExistsPageId, updatedPage));
        });
    }

    @Test
    public void updatePageWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageName(samplePageId, samplePageName));
        });
    }

    @Test
    public void updatePageByNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageName(samplePageId, updatedPage));
        });
    }

    @Test
    public void updatePageWithExistsTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageTitle(samplePageId, sampleTitleName));
        });
    }

    @Test
    public void updatePageByNotExistsTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageTitle(samplePageId, updatedTitle));
        });
    }

    @Test
    public void updatePageWithExistsDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageDescription(samplePageId, sampleDescriptionName));
        });
    }

    @Test
    public void updatePageByNotExistsDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageDescription(samplePageId, updatedDescription));
        });
    }

    @Test
    public void updatePageWithExistsKeywords() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageKeywords(samplePageId, sampleKeywordsName));
        });
    }

    @Test
    public void updatePageByNotExistsKeywords() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageKeywords(samplePageId, updatedKeywords));
        });
    }

    @Test
    public void updatePageWithExistsOgTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageOgTitle(samplePageId, sampleOgTitleName));
        });
    }

    @Test
    public void updatePageByNotExistsOgTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageOgTitle(samplePageId, updatedOgTitle));
        });
    }

    @Test
    public void updatePageWithExistsOgType() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageOgType(samplePageId, sampleOgTypeName));
        });
    }

    @Test
    public void updatePageByNotExistsOgType() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageOgType(samplePageId, updatedOgType));
        });
    }

    @Test
    public void updatePageWithExistsOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageOgImage(samplePageId, sampleOgImageName));
        });
    }

    @Test
    public void updatePageByNotExistsOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageOgImage(samplePageId, updatedOgImage));
        });
    }

    @Test
    public void updatePageWithExistsOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageOgUrl(samplePageId, sampleOgUrlName));
        });
    }

    @Test
    public void updatePageByNotExistsOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageOgUrl(samplePageId, updatedOgUrl));
        });
    }

    @Test
    public void updatePageWithExistsOgDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageOgDescription(samplePageId, sampleOgDescriptionName));
        });
    }

    @Test
    public void updatePageByNotExistsOgDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageOgDescription(samplePageId, updatedOgDescription));
        });
    }

    @Test
    public void updatePageWithExistsTwitterCard() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageTwitterCard(samplePageId, sampleTwitterCardName));
        });
    }

    @Test
    public void updatePageByNotExistsTwitterCard() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageTwitterCard(samplePageId, updatedTwitterCard));
        });
    }

    @Test
    public void updatePageWithExistsTwitterUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageTwitterUrl(samplePageId, sampleTwitterUrlName));
        });
    }

    @Test
    public void updatePageByNotExistsTwitterUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageTwitterUrl(samplePageId, updatedTwitterUrl));
        });
    }

    @Test
    public void updatePageWithExistsTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageTwitterTitle(samplePageId, sampleTwitterTitleName));
        });
    }

    @Test
    public void updatePageByNotExistsTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageTwitterTitle(samplePageId, updatedTwitterTitle));
        });
    }

    @Test
    public void updatePageWithExistsTwitterDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageTwitterDescription(samplePageId, sampleTwitterDescriptionName));
        });
    }

    @Test
    public void updatePageByNotExistsTwitterDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageTwitterDescription(samplePageId, updatedTwitterDescription));
        });
    }

    @Test
    public void updatePageWithExistsTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageTwitterImage(samplePageId, sampleTwitterImageName));
        });
    }

    @Test
    public void updatePageByNotExistsTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageTwitterImage(samplePageId, updatedTwitterImage));
        });
    }

    @Test
    public void updateNotExistsPageWithStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageStatus(notExistsPageId, activeStatusId));
        });
    }

    @Test
    public void updatePageWithNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.updatePageStatus(samplePageId, notExistsStatusId));
        });
    }

    @Test
    public void updatePageStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.updatePageStatus(samplePageId, inactiveStatusId));
        });
    }

    @Test
    public void deletePageWithNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNotNull(PageModelCrud.super.deletePage(notExistsPageId));
        });
    }

    @Test
    public void deletePageWithExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            assertNull(PageModelCrud.super.deletePage(samplePageId));
        });
    }
}
