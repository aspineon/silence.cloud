package models.core;

import io.ebean.Ebean;

import java.util.Date;
import java.util.List;

/**
 * models.core.PageModelCrud.java
 * <p>
 * Crud operations of PageModel.
 */
public interface PageModelCrud {

    /**
     * Create new page.
     *
     * @return new page model when success and null when failed
     */
    default PageModel createPage(Long statusId, String name,String title, String description,
                                 String keywords, String ogTitle, String ogType, String ogImage,
                                 String ogUrl, String ogDescription, String twitterCard,
                                 String twitterUrl, String twitterTitle,
                                 String twitterDescription, String twitterImage) {

        PageModel existsPageModel = PageModel.FINDER.query().where().eq("name", name).findOne();
        StatusModel statusModel = StatusModel.FINDER.ref(statusId);

        if ((statusModel != null) && (existsPageModel == null)) {

            PageModel pageModel = new PageModel();
            pageModel.id = System.currentTimeMillis();
            pageModel.name = name;
            pageModel.status = statusModel;
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

            return PageModel.FINDER.query().where().eq("name", name)
                    .eq("status", statusModel).findOne();
        }

        return null;
    }

    /**
     * Update page name.
     *
     * @param pageId
     * @param name
     * @return page when success or null when failed
     */
    default PageModel updatePageName(Long pageId, String name) {

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("name", name).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.name = name;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("name", name)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page title.
     *
     * @param pageId
     * @param title
     * @return page when success or null when failed
     */
    default PageModel updatePageTitle(Long pageId, String title) {

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("title", title).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.title = title;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("title", title)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page description.
     *
     * @param pageId
     * @param description
     * @return role when success or null when failed
     */
    default PageModel updatePageDescription(Long pageId, String description) {

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("description", description).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.description = description;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("description", description)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page keywords.
     *
     * @param pageId
     * @param keywords
     * @return page when success or null when failed
     */
    default PageModel updatePageKeywords(Long pageId, String keywords){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("keywords", keywords).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.keywords = keywords;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("keywords", keywords)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page ogTitle.
     *
     * @param pageId
     * @param ogTitle
     * @return page when success or null when failed
     */
    default PageModel updatePageOgTitle(Long pageId, String ogTitle){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("ogTitle", ogTitle).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.ogTitle = ogTitle;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("ogTitle", ogTitle)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page ogType.
     *
     * @param pageId
     * @param ogType
     * @return page when success or null when failed
     */
    default PageModel updatePageOgType(Long pageId, String ogType){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("ogType", ogType).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.ogType = ogType;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("ogType", ogType)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page ogImage.
     *
     * @param pageId
     * @param ogImage
     * @return page when success or null when failed
     */
    default PageModel updatePageOgImage(Long pageId, String ogImage){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("ogImage", ogImage).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.ogImage = ogImage;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("ogImage", ogImage)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page ogUrl.
     *
     * @param pageId
     * @param ogUrl
     * @return page when success or null when failed
     */
    default PageModel updatePageOgUrl(Long pageId, String ogUrl){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("ogUrl", ogUrl).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.ogUrl = ogUrl;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("ogUrl", ogUrl)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page ogDescription.
     *
     * @param pageId
     * @param ogDescription
     * @return page when success or null when failed
     */
    default PageModel updatePageOgDescription(Long pageId, String ogDescription){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("ogDescription", ogDescription).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.ogDescription = ogDescription;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("ogDescription", ogDescription)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page twitterCard.
     *
     * @param pageId
     * @param twitterCard
     * @return page when success or null when failed
     */
    default PageModel updatePageTwitterCard(Long pageId, String twitterCard){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("twitterCard", twitterCard).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.twitterCard = twitterCard;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("twitterCard", twitterCard)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page twitterUrl.
     *
     * @param pageId
     * @param twitterUrl
     * @return page when success or null when failed
     */
    default PageModel updatePageTwitterUrl(Long pageId, String twitterUrl){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("twitterUrl", twitterUrl).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.twitterUrl = twitterUrl;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("twitterUrl", twitterUrl)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page twitterTitle.
     *
     * @param pageId
     * @param twitterTitle
     * @return page when success or null when failed
     */
    default PageModel updatePageTwitterTitle(Long pageId, String twitterTitle){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("twitterTitle", twitterTitle).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.twitterTitle = twitterTitle;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("twitterTitle", twitterTitle)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page twitterDescription.
     *
     * @param pageId
     * @param twitterDescription
     * @return page when success or null when failed
     */
    default PageModel updatePageTwitterDescription(Long pageId, String twitterDescription){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("twitterDescription", twitterDescription).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.twitterDescription = twitterDescription;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("twitterDescription", twitterDescription)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page twitterImage.
     *
     * @param pageId
     * @param twitterImage
     * @return page when success or null when failed
     */
    default PageModel updatePageTwitterImage(Long pageId, String twitterImage){

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        PageModel existsPageModel = PageModel.FINDER.query().where().eq("twitterImage", twitterImage).findOne();

        if ((pageModel != null) && (existsPageModel == null)) {

            Ebean.beginTransaction();

            try {

                pageModel.twitterImage = twitterImage;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("twitterImage", twitterImage)
                    .findOne();
        }

        return null;
    }

    /**
     * Update page status.
     *
     * @param pageId
     * @param statusId
     * @return page when success or null when failed
     */
    default PageModel updatePageStatus(Long pageId, Long statusId) {

        PageModel pageModel = PageModel.FINDER.ref(pageId);
        StatusModel statusModel = StatusModel.FINDER.ref(statusId);

        if ((pageModel != null) && (statusModel != null)) {

            Ebean.beginTransaction();
            try {

                pageModel.status = statusModel;
                pageModel.updateAt = new Date();
                pageModel.update();

                Ebean.commitTransaction();
            } finally {

                Ebean.endTransaction();
            }

            return PageModel.FINDER.query().where().eq("id", pageId).eq("status", statusModel)
                    .findOne();
        }

        return null;
    }

    /**
     * Delete page.
     *
     * @param pageId
     * @return page when success or null when failed
     */
    default PageModel deletePage(Long pageId) {

        PageModel pageModel = PageModel.FINDER.ref(pageId);

        if (pageModel != null) {

            pageModel.delete();

            return pageModel;
        }

        return null;
    }

    /**
     * Find all pages.
     *
     * @return list of all pages
     */
    default List<PageModel> findAllPages(){

        List<PageModel> pages = PageModel.FINDER.all();
        return pages;
    }

    /**
     * Find all pages by status.
     *
     * @param statusId
     * @return list of all pages find by status
     */
    default List<PageModel> findAllPagesByStatus(Long statusId){

        StatusModel status = StatusModel.FINDER.ref(statusId);

        List<PageModel> pages = PageModel.FINDER.query().where().eq("status", status).findList();
        return pages;
    }

    /**
     * Find page by id.
     *
     * @param pageId
     * @return page when success or null when failed
     */
    default PageModel findPageById(Long pageId) {

        try {

            return PageModel.FINDER.ref(pageId);
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find page by name.
     *
     * @param name
     * @return page when success or null when failed
     */
    default PageModel findPageByName(String name) {

        try {

            return PageModel.FINDER.query().where().eq("name", name).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find page by title.
     *
     * @param title
     * @return page when success or null when failed
     */
    default PageModel findPageByTitle(String title) {

        try {

            return PageModel.FINDER.query().where().eq("title", title).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find page by ogTitle.
     *
     * @param ogTitle
     * @return page when success or null when failed
     */
    default PageModel findPageByOgTitle(String ogTitle) {

        try {

            return PageModel.FINDER.query().where().eq("ogTitle", ogTitle).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find page by ogImage.
     *
     * @param ogImage
     * @return page when success or null when failed
     */
    default PageModel findPageByOgImage(String ogImage) {

        try {

            return PageModel.FINDER.query().where().eq("ogImage", ogImage).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find page by ogUrl.
     *
     * @param ogUrl
     * @return page when success or null when failed
     */
    default PageModel findPageByOgUrl(String ogUrl) {

        try {

            return PageModel.FINDER.query().where().eq("ogUrl", ogUrl).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find page by twitterUrl.
     *
     * @param twitterUrl
     * @return page when success or null when failed
     */
    default PageModel findPageByTwitterUrl(String twitterUrl) {

        try {

            return PageModel.FINDER.query().where().eq("twitterUrl", twitterUrl).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find page by twitterTitle.
     *
     * @param twitterTitle
     * @return page when success or null when failed
     */
    default PageModel findPageByTwitterTitle(String twitterTitle) {

        try {

            return PageModel.FINDER.query().where().eq("twitterTitle", twitterTitle).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find page by twitterImage.
     *
     * @param twitterImage
     * @return page when success or null when failed
     */
    default PageModel findPageByTwitterImage(String twitterImage) {

        try {

            return PageModel.FINDER.query().where().eq("twitterImage", twitterImage).findOne();
        } catch (NullPointerException e) {

            return null;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}
