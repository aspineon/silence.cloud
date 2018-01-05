package repositories.core;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.core.PageModel;
import models.core.StatusModel;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;


/**
 * repositories.core.PageRepository.java
 * <p>
 * Implements async crud operation of page model.
 */
public class PageRepository implements PageRepositoryInterface{



    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;


    /**
     * Constructor.
     *
     * @param ebeanConfig
     * @param executionContext
     * @param ebeanDynamicEvolutions
     */
    @Inject
    public PageRepository(
            EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext,
            EbeanDynamicEvolutions ebeanDynamicEvolutions
    ) {

        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
    }

    /**
     * Find all pages.
     *
     * @return all pages list
     */
    @Override
    public CompletionStage<List<PageModel>> findAllPages() {

        return supplyAsync(() -> {

            List roles = new LinkedList();
            roles = ebeanServer.find(PageModel.class).findList();

            return roles;
        }, executionContext);
    }

    /**
     * Find all pages by status.
     *
     * @param statusId
     * @return list of all pages find by status or empty list when status not found
     */
    @Override
    public CompletionStage<List<PageModel>> findAllPagesByStatus(Long statusId) {

        return supplyAsync(() -> {

            List<PageModel> roles = new LinkedList<>();
            StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
            if (status != null) {

                roles = ebeanServer.find(PageModel.class).where().eq("status", status).findList();
            }

            return roles;
        }, executionContext);
    }

    /**
     * Find page by id.
     *
     * @param pageId
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageById(Long pageId) {

        return supplyAsync(() -> {

            return Optional.ofNullable(ebeanServer.find(PageModel.class).setId(pageId).findOne());
        }, executionContext);
    }

    /**
     * Find page by name.
     *
     * @param pageName
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageByName(String pageName) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(PageModel.class).where().eq("name", pageName).findOne()
            );
        }, executionContext);
    }

    /**
     * Find page by title.
     *
     * @param title
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageByTitle(String title) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(PageModel.class).where().eq("title", title).findOne()
            );
        }, executionContext);
    }

    /**
     * Find page by ogTitle.
     *
     * @param ogTitle
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageByOgTitle(String ogTitle) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(PageModel.class).where().eq("ogTitle", ogTitle).findOne()
            );
        }, executionContext);
    }

    /**
     * Find page by ogImage.
     *
     * @param ogImage
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageByOgImage(String ogImage) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(PageModel.class).where().eq("ogImage", ogImage).findOne()
            );
        }, executionContext);
    }

    /**
     * Find page by ogUrl.
     *
     * @param ogUrl
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageByOgUrl(String ogUrl) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(PageModel.class).where().eq("ogUrl", ogUrl).findOne()
            );
        }, executionContext);
    }

    /**
     * Find page by twitterUrl.
     *
     * @param twitterUrl
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageByTwitterUrl(String twitterUrl) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(PageModel.class).where().eq("twitterUrl", twitterUrl).findOne()
            );
        }, executionContext);
    }

    /**
     * Find page by twitterTitle.
     *
     * @param twitterTitle
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageByTwitterTitle(String twitterTitle) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(PageModel.class).where().eq("twitterTitle", twitterTitle).findOne()
            );
        }, executionContext);
    }

    /**
     * Find page by twitterImage.
     *
     * @param twitterImage
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> findPageByTwitterImage(String twitterImage) {

        return supplyAsync(() -> {

            return Optional.ofNullable(
                    ebeanServer.find(PageModel.class).where().eq("twitterImage", twitterImage).findOne()
            );
        }, executionContext);
    }

    /**
     * Create page.
     *
     * @return new page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> createPage(Long statusId, String name,String title, String description,
                                                           String keywords, String ogTitle, String ogType, String ogImage,
                                                           String ogUrl, String ogDescription, String twitterCard,
                                                           String twitterUrl, String twitterTitle,
                                                           String twitterDescription, String twitterImage) {
        return supplyAsync(() -> {

            StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
            PageModel existRoleModel = ebeanServer.find(PageModel.class).where().eq("name", name)
                    .findOne();

            if ((status != null) && (existRoleModel == null)) {

                PageModel pageModel = new PageModel();
                pageModel.id = System.currentTimeMillis();
                pageModel.name = name;
                pageModel.status = status;
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
                return Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("name", name)
                                .eq("status", status).findOne()
                );
            }

            return Optional.empty();
        }, executionContext);
    }

    /**
     * Update page name.
     *
     * @param pageId
     * @param pageName
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageName(Long pageId, String pageName) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("name", pageName)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.name = pageName;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("name", pageName).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page title.
     *
     * @param pageId
     * @param title
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageTitle(Long pageId, String title) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("title", title)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.title = title;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("title", title).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page description.
     *
     * @param pageId
     * @param description
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageDescription(Long pageId, String description) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("description", description)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.description = description;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("description", description).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page keywords.
     *
     * @param pageId
     * @param keywords
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageKeywords(Long pageId, String keywords) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("keywords", keywords)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.keywords = keywords;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("keywords", keywords).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page ogTitle.
     *
     * @param pageId
     * @param ogTitle
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageOgTitle(Long pageId, String ogTitle) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("ogTitle", ogTitle)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.ogTitle = ogTitle;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("ogTitle", ogTitle).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page ogType.
     *
     * @param pageId
     * @param ogType
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageOgType(Long pageId, String ogType) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("ogType", ogType)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.ogType = ogType;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("ogType", ogType).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page ogImage.
     *
     * @param pageId
     * @param ogImage
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageOgImage(Long pageId, String ogImage) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("ogImage", ogImage)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.ogImage = ogImage;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("ogImage", ogImage).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page ogUrl.
     *
     * @param pageId
     * @param ogUrl
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageOgUrl(Long pageId, String ogUrl) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("ogUrl", ogUrl)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.ogUrl = ogUrl;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("ogUrl", ogUrl).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page ogDescription.
     *
     * @param pageId
     * @param ogDescription
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageOgDescription(Long pageId, String ogDescription) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("ogDescription", ogDescription)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.ogDescription = ogDescription;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("ogDescription", ogDescription).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page twitterCard.
     *
     * @param pageId
     * @param twitterCard
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageTwitterCard(Long pageId, String twitterCard) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("twitterCard", twitterCard)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.twitterCard = twitterCard;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("twitterCard", twitterCard).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page twitterUrl.
     *
     * @param pageId
     * @param twitterUrl
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageTwitterUrl(Long pageId, String twitterUrl) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("twitterUrl", twitterUrl)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.twitterUrl = twitterUrl;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("twitterUrl", twitterUrl).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page twitterTitle.
     *
     * @param pageId
     * @param twitterTitle
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageTwitterTitle(Long pageId, String twitterTitle) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("twitterTitle", twitterTitle)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.twitterTitle = twitterTitle;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("twitterTitle", twitterTitle).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page twitterDescription.
     *
     * @param pageId
     * @param twitterDescription
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageTwitterDescription(Long pageId, String twitterDescription) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("twitterDescription", twitterDescription)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.twitterDescription = twitterDescription;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("twitterDescription", twitterDescription).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page twitterImage.
     *
     * @param pageId
     * @param twitterImage
     * @return updated page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageTwitterImage(Long pageId, String twitterImage) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();
                PageModel existsPage = ebeanServer.find(PageModel.class).where().eq("twitterImage", twitterImage)
                        .findOne();

                if ((currentPage != null) && (existsPage == null)) {

                    currentPage.twitterImage = twitterImage;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("twitterImage", twitterImage).findOne()
                );
            }finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Update page status.
     *
     * @param pageId
     * @param statusId
     * @return page when success or null when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> updatePageStatus(Long pageId, Long statusId) {

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<PageModel> updatedPage = Optional.empty();

            try {

                StatusModel status = ebeanServer.find(StatusModel.class).setId(statusId).findOne();
                PageModel currentPage = ebeanServer.find(PageModel.class).setId(pageId).findOne();

                if ((currentPage != null) && (status != null)) {

                    currentPage.status = status;
                    currentPage.updateAt = new Date();
                    ebeanServer.update(currentPage);
                    txn.commit();
                }

                updatedPage = Optional.ofNullable(
                        ebeanServer.find(PageModel.class).where().eq("status", status).findOne()
                );
            } finally {

                txn.end();
            }

            return updatedPage;
        }, executionContext);
    }

    /**
     * Delete page.
     *
     * @param pageId
     * @return null when success or page when failed
     */
    @Override
    public CompletionStage<Optional<PageModel>> deletePage(Long pageId) {

        return supplyAsync(() -> {

            PageModel page = ebeanServer.find(PageModel.class).setId(pageId).findOne();
            Optional<PageModel> pageModelOptional = Optional.empty();

            if(page != null) {

                ebeanServer.delete(page);
            }

            if (ebeanServer.find(PageModel.class).setId(page.id).findOne() != null) {

                pageModelOptional = Optional.of(page);
                return pageModelOptional;
            }

            return Optional.ofNullable(ebeanServer.find(PageModel.class).setId(page.id).findOne());
        }, executionContext);
    }
}
