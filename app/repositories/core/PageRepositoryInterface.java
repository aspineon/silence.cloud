package repositories.core;

import models.core.PageModel;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface PageRepositoryInterface {

    /**
     * Create new page.
     *
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> createPage(Long statusId, String name,String title, String description,
                                                    String keywords, String ogTitle, String ogType, String ogImage,
                                                    String ogUrl, String ogDescription, String twitterCard,
                                                    String twitterUrl, String twitterTitle,
                                                    String twitterDescription, String twitterImage);
    /**
     * Find all pages.
     *
     * @return all pages list
     */
    CompletionStage<List<PageModel>> findAllPages();

    /**
     * Find all pages by status.
     *
     * @param statusId
     * @return list of all pages find by status
     */
    CompletionStage<List<PageModel>> findAllPagesByStatus(Long statusId);

    /**
     * Find all pages by id.
     *
     * @param pageId
     * @return list of all pages find by id
     */
    CompletionStage<Optional<PageModel>> findPageById(Long pageId);

    /**
     * Find all pages by name.
     *
     * @param pageName
     * @return list of all pages find by name
     */
    CompletionStage<Optional<PageModel>> findPageByName(String pageName);

    /**
     * Find all pages by title.
     *
     * @param title
     * @return list of all pages find by title
     */
    CompletionStage<Optional<PageModel>> findPageByTitle(String title);

    /**
     * Find all pages by ogTitle.
     *
     * @param ogTitle
     * @return list of all pages find by ogTitile
     */
    CompletionStage<Optional<PageModel>> findPageByOgTitle(String ogTitle);

    /**
     * Find all pages by ogImage.
     *
     * @param ogImage
     * @return list of all pages find by ogImage
     */
    CompletionStage<Optional<PageModel>> findPageByOgImage(String ogImage);

    /**
     * Find all pages by ogUrl.
     *
     * @param ogUrl
     * @return list of all pages find by ogUrl
     */
    CompletionStage<Optional<PageModel>> findPageByOgUrl(String ogUrl);

    /**
     * Find all pages by twitterUrl.
     *
     * @param twitterUrl
     * @return list of all pages find by twitter Url
     */
    CompletionStage<Optional<PageModel>> findPageByTwitterUrl(String twitterUrl);

    /**
     * Find all pages by twitterTitle.
     *
     * @param twitterTitle
     * @return list of all pages find by twitter Title
     */
    CompletionStage<Optional<PageModel>> findPageByTwitterTitle(String twitterTitle);

    /**
     * Find all pages by twitterImage.
     *
     * @param twitterImage
     * @return list of all pages find by twitter Image
     */
    CompletionStage<Optional<PageModel>> findPageByTwitterImage(String twitterImage);

    /**
     * Update page name.
     *
     * @param pageId
     * @param pageName
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageName(Long pageId, String pageName);

    /**
     * Update page title.
     *
     * @param pageId
     * @param title
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageTitle(Long pageId, String title);

    /**
     * Update page description.
     *
     * @param pageId
     * @param description
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageDescription(Long pageId, String description);

    /**
     * Update page keywords.
     *
     * @param pageId
     * @param keywords
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageKeywords(Long pageId, String keywords);

    /**
     * Update page ogTitle.
     *
     * @param pageId
     * @param ogTitle
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageOgTitle(Long pageId, String ogTitle);

    /**
     * Update page ogType.
     *
     * @param pageId
     * @param ogType
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageOgType(Long pageId, String ogType);

    /**
     * Update page ogImage.
     *
     * @param pageId
     * @param ogImage
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageOgImage(Long pageId, String ogImage);

    /**
     * Update page ogUrl.
     *
     * @param pageId
     * @param ogUrl
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageOgUrl(Long pageId, String ogUrl);

    /**
     * Update page ogDescription.
     *
     * @param pageId
     * @param ogDescription
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageOgDescription(Long pageId, String ogDescription);

    /**
     * Update page twitterCard.
     *
     * @param pageId
     * @param twitterCard
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageTwitterCard(Long pageId, String twitterCard);

    /**
     * Update page twitterUrl.
     *
     * @param pageId
     * @param twitterUrl
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageTwitterUrl(Long pageId, String twitterUrl);

    /**
     * Update page twitterTitle.
     *
     * @param pageId
     * @param twitterTitle
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageTwitterTitle(Long pageId, String twitterTitle);

    /**
     * Update page twitterDescription.
     *
     * @param pageId
     * @param twitterDescription
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageTwitterDescription(Long pageId, String twitterDescription);

    /**
     * Update page twitterImage.
     *
     * @param pageId
     * @param twitterImage
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageTwitterImage(Long pageId, String twitterImage);

    /**
     * Update page status.
     *
     * @param pageId
     * @param statusId
     * @return page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> updatePageStatus(Long pageId, Long statusId);

    /**
     * Delete page by id.
     *
     * @param pageId
     * @return delete page when success or null when failed
     */
    CompletionStage<Optional<PageModel>> deletePage(Long pageId);

}
