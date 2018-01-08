package repositories.core;

import helpers.BeforeAndAfterTest;
import models.core.PageModel;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class PageRepositoryTest extends BeforeAndAfterTest {

    private Long activeStatusId = 1L;
    private Long inactiveStatusId = 2L;

    private Long samplePageId = 1L;

    private Long notExistsPageId = 100L;

    private Long notExistsStatusId = 100L;


    private int expectedSize = 13;
    private int emptyListSize = 0;

    private String updatedPage = "updatedPage";
    private String samplePageName = "samplePageName";
    private String notExistsPageName = "notExists";

    private String updatedTitle = "updatedTitle";
    private String sampleTitleName = "sampleTitle";
    private String notExistsTitleName = "notExistsTitleName";

    private String updatedDescription = "updatedDescription";
    private String sampleDescriptionName = "sampleDescription";
    private String notExistsDescriptionName = "notExistsDescriptionName";

    private String updatedKeywords = "updatedKeywords";
    private String sampleKeywordsName = "sampleKeywords";
    private String notExistsKeywordsName = "notExistsKeywordsName";

    private String updatedOgTitle = "updatedOgTitle";
    private String sampleOgTitleName = "sampleOgTitle";
    private String notExistsOgTitleName = "notExistsOgTitleName";

    private String updatedOgType = "updatedOgType";
    private String sampleOgTypeName = "sampleOgType";
    private String notExistsOgTypeName = "notExistsOgTypeName";

    private String updatedOgImage = "updatedOgImage";
    private String sampleOgImageName = "sampleOgImage";
    private String notExistsOgImageName = "notExistsOgImageName";

    private String updatedOgUrl = "updatedOgUrl";
    private String sampleOgUrlName = "sampleOgUrl";
    private String notExistsOgUrlName = "notExistsOgUrlName";

    private String updatedOgDescription = "updatedOgDescription";
    private String sampleOgDescriptionName = "sampleOgDescription";
    private String notExistsOgDescriptionName = "notExistsOgDescriptionName";

    private String updatedTwitterCard = "updatedTwitterCard";
    private String sampleTwitterCardName = "sampleTwitterCard";
    private String notExistsTwitterCardName = "notExistsTwitterCardName";

    private String updatedTwitterUrl = "updatedTwitterUrl";
    private String sampleTwitterUrlName = "sampleTwitterUrl";
    private String notExistsTwitterUrlName = "notExistsTwitterUrlName";

    private String updatedTwitterTitle = "updatedTwitterTitle";
    private String sampleTwitterTitleName = "sampleTwitterTitle";
    private String notExistsTwitterTitleName = "notExistsTwitterTitleName";

    private String updatedTwitterDescription = "updatedTwitterDescription";
    private String sampleTwitterDescriptionName = "sampleTwitterDescription";
    private String notExistsTwitterDescriptionName = "notExistsTwitterDescriptionName";

    private String updatedTwitterImage = "updatedTwitterImage";
    private String sampleTwitterImageName = "sampleTwitterImage";
    private String notExistsTwitterImageName = "notExistsTwitterImageName";

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }


    @Test
    public void findAllPages() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            CompletionStage<List<PageModel>> stage = pageRepository.findAllPages();

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {

                    return list.size() == expectedSize;
                });
            });
        });
    }

    @Test
    public void findAllPagesByNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {
            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            CompletionStage<List<PageModel>> stage = pageRepository.findAllPagesByStatus(notExistsStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                    return list.size() == emptyListSize;
                });
            });
        });
    }

    @Test
    public void findAllPagesByExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            CompletionStage<List<PageModel>> stage = pageRepository.findAllPagesByStatus(activeStatusId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(list -> {
                    return list.size() == expectedSize;
                });
            });
        });
    }

    @Test
    public void findPageByNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageById(notExistsPageId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void findPageByExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageById(samplePageId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void findPageByNotExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByName(notExistsPageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void findPageByExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByName(samplePageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void findPageByNotExistsTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByTitle(notExistsTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void findPageByExistsTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByTitle(sampleTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void findPageByNotExistsOgTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByOgTitle(notExistsOgTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void findPageByExistsOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByOgImage(sampleOgImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void findPageByNotExistsOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByOgImage(notExistsOgImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void findPageByExistsOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByOgUrl(sampleOgUrlName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void findPageByNotExistsOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByOgUrl(notExistsOgUrlName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void findPageByExistsTwitterUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByTwitterUrl(sampleTwitterUrlName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void findPageByNotExistsTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByTwitterTitle(notExistsTwitterTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void findPageByExistsTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByTwitterTitle(sampleTwitterTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void findPageByNotExistsTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByTwitterImage(notExistsTwitterImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void findPageByExistsTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.findPageByTwitterImage(sampleTwitterImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void createPageWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            CompletionStage<Optional<PageModel>> stage = pageRepository.createPage(activeStatusId,samplePageName,
                    sampleTitleName, sampleDescriptionName, sampleKeywordsName, sampleOgTitleName, sampleOgTypeName,
                    sampleOgImageName, sampleOgUrlName, sampleOgDescriptionName, sampleTwitterCardName, sampleTwitterUrlName,
                    sampleTwitterTitleName, sampleTwitterDescriptionName, sampleTwitterImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void createPageWithNotExistsStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.createPage(activeStatusId,samplePageName,
                    sampleTitleName, sampleDescriptionName, sampleKeywordsName, sampleOgTitleName, sampleOgTypeName,
                    sampleOgImageName, sampleOgUrlName, sampleOgDescriptionName, sampleTwitterCardName, sampleTwitterUrlName,
                    sampleTwitterTitleName, sampleTwitterDescriptionName, sampleTwitterImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void createNewPage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.createPage(activeStatusId,samplePageName,
                    sampleTitleName, sampleDescriptionName, sampleKeywordsName, sampleOgTitleName, sampleOgTypeName,
                    sampleOgImageName, sampleOgUrlName, sampleOgDescriptionName, sampleTwitterCardName, sampleTwitterUrlName,
                    sampleTwitterTitleName, sampleTwitterDescriptionName, sampleTwitterImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageName(notExistsPageId, samplePageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageName(samplePageId, samplePageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageName(samplePageId, updatedPage);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTitle(notExistsPageId, notExistsTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTitle(samplePageId, sampleTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateTitleName() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTitle(samplePageId, updatedTitle);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageDescription(notExistsPageId, notExistsDescriptionName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageDescription(samplePageId, sampleDescriptionName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageDescription(samplePageId, updatedDescription);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageKeywords() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageKeywords(notExistsPageId, notExistsKeywordsName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsKeywords() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageDescription(samplePageId, sampleKeywordsName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateKeywords() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageKeywords(samplePageId, updatedKeywords);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageOgTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgTitle(notExistsPageId, notExistsOgTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsOgTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgTitle(samplePageId, sampleOgTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateOgTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgTitle(samplePageId, updatedOgTitle);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageOgType() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgType(notExistsPageId, notExistsOgTypeName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsOgType() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgType(samplePageId, sampleOgTypeName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateOgType() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgType(samplePageId, updatedOgType);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgImage(notExistsPageId, notExistsOgImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgImage(samplePageId, sampleOgImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateOgImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgType(samplePageId, updatedOgImage);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgUrl(notExistsPageId, notExistsOgUrlName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgUrl(samplePageId, sampleOgUrlName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateOgUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgUrl(samplePageId, updatedOgUrl);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageOgDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgDescription(notExistsPageId, notExistsOgDescriptionName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsOgDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgDescription(samplePageId, sampleOgDescriptionName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateOgDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageOgDescription(samplePageId, updatedOgDescription);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageTwitterCard() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterCard(notExistsPageId, notExistsTwitterCardName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsTwitterCard() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterCard(samplePageId, sampleTwitterCardName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateTwitterCard() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterCard(samplePageId, updatedTwitterCard);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageTwitterUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterUrl(notExistsPageId, notExistsTwitterUrlName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsTwitterUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterUrl(samplePageId, sampleTwitterUrlName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateTwitterUrl() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterUrl(samplePageId, updatedTwitterUrl);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterTitle(notExistsPageId, notExistsTwitterTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterTitle(samplePageId, sampleTwitterTitleName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateTwitterTitle() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterTitle(samplePageId, updatedTwitterTitle);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageTwitterDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterDescription(notExistsPageId, notExistsTwitterDescriptionName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsTwitterDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterDescription(samplePageId, sampleTwitterDescriptionName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateTwitterDescription() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterDescription(samplePageId, updatedTwitterDescription);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterImage(notExistsPageId, notExistsTwitterImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithExistsTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterImage(samplePageId, sampleTwitterImageName);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updateTwitterImage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageTwitterImage(samplePageId, updatedTwitterImage);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {
                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void updateNotExistsPageStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageStatus(
                    notExistsPageId, inactiveStatusId
            );

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageWithNotExistsStatus(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageStatus(
                    samplePageId, notExistsStatusId
            );

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }

    @Test
    public void updatePageStatus() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.updatePageStatus(
                    samplePageId, inactiveStatusId
            );

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void deletePageWithNotExistsId() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.deletePage(notExistsPageId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return page.isPresent() && page.get() != null;
                });
            });
        });
    }

    @Test
    public void deletePage() throws Exception {

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            final PageRepository pageRepository = app.injector().instanceOf(PageRepository.class);
            final CompletionStage<Optional<PageModel>> stage = pageRepository.deletePage(samplePageId);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(page -> {

                    return !page.isPresent();
                });
            });
        });
    }
}
