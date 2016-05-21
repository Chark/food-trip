package io.chark.food.app.data;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.article.ArticleService;
import io.chark.food.app.article.category.ArticleCategoryService;
import io.chark.food.app.article.photo.ArticlePhotoService;
import io.chark.food.app.comment.CommentService;
import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.app.restaurant.details.BankService;
import io.chark.food.app.restaurant.details.RestaurantDetailsService;
import io.chark.food.app.thread.ThreadService;
import io.chark.food.app.thread.categories.ThreadCategoryService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.photo.ArticlePhoto;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.restaurant.Bank;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.domain.restaurant.RestaurantDetails;
import io.chark.food.domain.thread.Thread;
import io.chark.food.domain.thread.category.ThreadCategory;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.photo.PhotoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Component to seed database with test data.
 */
@Service
public class TestDataService {

    /**
     * Default password used for username's and such.
     */
    private static final String DEFAULT_PASSWORD = "password";

    /**
     * Default description for all the stuff.
     */
    private static final String DEFAULT_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
            " Curabitur tincidunt purus urna, non gravida leo porttitor vel. Morbi vel dui quis elit aliquet" +
            " molestie. Morbi ut blandit purus. Donec in tortor mauris. Proin tincidunt aliquam auctor. ";

    /**
     * Default short description for all the stuff.
     */
    private static final String DEFAULT_SHORT_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    /**
     * Default alternate text for all the stuff.
     */
    private static final String DEFAULT_ALTERNATE_TEXT = "This is alt text.";

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataService.class);
    private final RestaurantService restaurantService;
    private final AccountService accountService;
    private final ArticleCategoryService articleCategoryService;
    private final ArticlePhotoService articlePhotoService;
    private final ArticleService articleService;
    private final ThreadCategoryService threadCategoryService;
    private final ThreadService threadService;
    private final CommentService commentService;
    private final RestaurantDetailsService restaurantDetailsService;
    private final BankService bankService;


    // Username's to initialize.
    private final List<String> bankList;
    // Username's to initialize.
    private final List<String> usernameList;

    // Restaurants to initialize.
    private final List<String> restaurantNameList;

    // Article categories to initialize.
    private final List<String> articleCategoryTitleList;

    // Articles to initialize.
    private final List<String> articleTitleList;

    // Article photos to initialize.
    private final List<String> articlePhotoPathList;

    // Add some randomness to test data.
    private final Random random;

    @Autowired
    public TestDataService(RestaurantService restaurantService,
                           AccountService accountService,
                           ArticleCategoryService articleCategoryService,
                           ArticlePhotoService articlePhotoService,
                           ArticleService articleService,
                           ThreadCategoryService threadCategoryService,
                           ThreadService threadService,
                           CommentService commentService,
                           RestaurantDetailsService restaurantDetailsService,
                           BankService bankService
    ) {

        this.restaurantService = restaurantService;
        this.accountService = accountService;
        this.articleCategoryService = articleCategoryService;
        this.articlePhotoService = articlePhotoService;
        this.articleService = articleService;
        this.threadCategoryService = threadCategoryService;
        this.threadService = threadService;
        this.commentService = commentService;
        this.restaurantDetailsService = restaurantDetailsService;
        this.bankService = bankService;
        this.random = new Random();

        this.bankList = new ArrayList<>();
        this.bankList.addAll(Arrays.asList(
                "SWEDBANK",
                "SEB Vilniaus Bankas",
                "NORDEA",
                "Dankse bankas",
                "Snoras",
                "Ketvircio bankas",
                "Medicinos bankas"
        ));


        // Test username's.
        this.usernameList = new ArrayList<>();
        this.usernameList.addAll(Arrays.asList(
                "CoolGuy",
                "FoodLover69",
                "Mr.Meeseeks",
                "JohnDoe",
                "AAaaeeeoouuu",
                "ThatOtherGuy",
                "SuperMega",
                "Padla",
                "Baunyzas",
                "Ugnius",
                "Arvydas",
                "Erlandas",
                "Edvinas"
        ));

        // Test restaurant names.
        this.restaurantNameList = new ArrayList<>();
        this.restaurantNameList.addAll(Arrays.asList(
                "Kebabs",
                "Super Duper pizza place",
                "Not so cool pizza place",
                "Pizzas",
                "Just awesome"
        ));

        // Test article categories titles.
        this.articleCategoryTitleList = new ArrayList<>();
        this.articleCategoryTitleList.addAll(Arrays.asList(
                "Pirma kategorija",
                "Antra kategorija",
                "Trečia kategorija"
        ));

        // Test article categories titles.
        this.articleTitleList = new ArrayList<>();
        this.articleTitleList.addAll(Arrays.asList(
                "Pirmas straipsnis",
                "Antras straipsnis",
                "Trečias straipsnis",
                "Ketvirtas straipsnis",
                "Penktas straipsnis",
                "Šeštas straipsnis"
        ));

        // Test restaurant names.
        this.articlePhotoPathList = new ArrayList<>();
        this.articlePhotoPathList.addAll(Arrays.asList(
                "static/images/default_avatar.JPG",
                "static/images/photo.jpg"
        ));
    }

    /**
     * Fill database with test data.
     */
    @Async
    public Future<Void> initTestData() {

        LOGGER.info("Initializing test data");

        // Initialize main test accounts.
        List<Account> accounts = initTestAccounts();

        // Initialize main restaurants and their test accounts.
        List<Restaurant> restaurants = initTestRestaurants(accounts);

        // Initialize main article categories.
        List<ArticleCategory> articleCategories = initTestArticleCategories();

        // Initialize main article photos.
        List<ArticlePhoto> articlePhotos = initTestArticlePhotos(this.articleTitleList.size());

        // Initialize main articles and their article categories.
        initTestArticles(articleCategories, articlePhotos, restaurants);

        // Initializes test thread categories
        initTestThreadCategories();

        //Initializes test threads with categories
        //initTestThreadCategories(); is required
        initTestThreads();

        initTestBanks();

        LOGGER.info("Finished initializing test data");
        return new AsyncResult<>(null);
    }

    /**
     * Register a list of tests accounts and return their instances.
     *
     * @return list of test accounts.
     */
    private List<Account> initTestAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (String username : this.usernameList) {
            Optional<Account> account = accountService
                    .register(username, String.format("%s@food.com", username), DEFAULT_PASSWORD);

            // Add account to account list if present.
            account.ifPresent(accounts::add);
        }
        return accounts;
    }

    /***
     * Creates a list of test banks.
     *
     * @return list of banks
     * */
    private List<Bank> initTestBanks() {
        List<Bank> banks = new ArrayList<>();
        for (String name : this.bankList) {
            Optional<Bank> bank = bankService
                    .register(name);

            // Add account to account list if present.
            bank.ifPresent(banks::add);
        }
        return banks;
    }

    /***
     * Creates a list of test thread categories.
     *
     * @return list of thread categories
     */
    private List<ThreadCategory> initTestThreadCategories() {
        List<ThreadCategory> threadCategories = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            Optional<ThreadCategory> threadCategory = threadCategoryService.register("Test name: " + i, "Test description: " + i);
            threadCategory.ifPresent(threadCategories::add);
        }
        return threadCategories;
    }

    /***
     * Creates a list of test threads.
     *
     * @return list of threads
     */
    private List<Thread> initTestThreads() {
        List<Thread> threads = new ArrayList<>();
        Account account = accountService.getAccount("admin");

        for (int i = 1; i < 30; i++) {
            int rand = random.nextInt(threadCategoryService.getThreadCategories().size());
            ThreadCategory threadCategory = threadCategoryService.getThreadCategories().get(rand);
            Optional<Thread> thread = threadService.register(account, "Test title: " + i, "Test description: " + i, false, threadCategory);
            thread.ifPresent(threads::add);
        }

        return threads;

    }

    /**
     * Create a list of restaurants.
     *
     * @return list of test restaurants.
     */
    private List<Restaurant> initTestRestaurants(List<Account> accounts) {

        // There must be more accounts than restaurants.
        List<Restaurant> restaurants = new ArrayList<>();
        for (String name : this.restaurantNameList) {

            String email = String.format("%s@food.com", name)
                    .replaceAll(" ", "");

            // Restaurant owner.
            Optional<Account> account = accountService.register(name.replaceAll(" ", ""), email, DEFAULT_PASSWORD);
            if (account.isPresent()) {

                // Need an account in authentication for new restaurant registering.
                AuthenticationUtils.setAccount(account.get());

//                Optional<RestaurantDetails> restaurantDetails = restaurantDetailsService.register(
//                        "+37067715464" + random.nextInt(9999999),
//                        "LT95569514646" + random.nextInt(9999999),
//                        "LT8989898" + random.nextInt(9999999),
//                        "989895526" + random.nextInt(9999999),
//                        "Jonas Ketvirtis" + random.nextInt(9999999));

                Restaurant tempRest = new Restaurant(email, name, DEFAULT_DESCRIPTION);
//                tempRest.setRestaurantDetails(restaurantDetails.get());

                Optional<Restaurant> restaurant = restaurantService
                        .register(tempRest);



                // Add restaurant to restaurant list if present.
                if (restaurant.isPresent()) {

                    // Invite some users to join this restaurant.
                    for (Account invite : accounts) {

                        // Should we invite this account?
                        if (random.nextBoolean()) {
                            restaurantService.invite(invite.getUsername());
                        }
                    }
                    restaurants.add(restaurant.get());
                }
            }

            // Cleanup authentication.
            SecurityContextHolder.clearContext();
        }
        return restaurants;
    }

    /**
     * Create a list of article categories.
     *
     * @return list of test article categories.
     */
    private List<ArticleCategory> initTestArticleCategories() {
        List<ArticleCategory> articleCategories = new ArrayList<>();

        for (String title : this.articleCategoryTitleList) {
            Optional<ArticleCategory> articleCategory = articleCategoryService
                    .register(title, DEFAULT_DESCRIPTION);

            // Add article category to article category list if present.
            articleCategory.ifPresent(articleCategories::add);
        }
        return articleCategories;
    }

    /**
     * Create a list of articles.
     *
     * @param articleCategories list of article categories.
     * @param articlePhotos     list of article photos.
     * @return list of test articles.
     */
    private List<Article> initTestArticles(List<ArticleCategory> articleCategories,
                                           List<ArticlePhoto> articlePhotos,
                                           List<Restaurant> restaurants) {

        // No restaurants, need at least a few.
        if (restaurants.isEmpty()) {
            return Collections.emptyList();
        }

        List<Article> articles = new ArrayList<>();
        for (String title : this.articleTitleList) {
            Optional<Article> article = articleService
                    .register(new Article(title,
                            DEFAULT_DESCRIPTION,
                            DEFAULT_SHORT_DESCRIPTION,
                            "meta keywords",
                            "meta description"), restaurants.get(0));

            // Add article to article list if present.
            article.ifPresent(articles::add);
        }

        // Add article categories to articles
        for (int i = 0; i < articles.size(); i++) {
            int num = i % articleCategories.size();

            Optional<Article> optional = articleService.addCategory(articles.get(i), articleCategories.get(num));
            if (optional.isPresent()) {
                articles.set(i, optional.get());
            }
        }

        // Add article photos to articles
        for (int i = 0; i < articles.size(); i++) {
            int num = (2 * i) % articlePhotos.size();

            Optional<Article> optional = articleService.addPhoto(articles.get(i), articlePhotos.get(num));
            if (optional.isPresent()) {
                articles.set(i, optional.get());
            }

            num = (2 * i + 1) % articlePhotos.size();

            optional = articleService.addPhoto(articles.get(i), articlePhotos.get(num));
            if (optional.isPresent()) {
                articles.set(i, optional.get());
            }
        }

        return articles;
    }

    /**
     * Create a list of article photos.
     *
     * @return list of test article photos.
     */
    private List<ArticlePhoto> initTestArticlePhotos(int count) {
        List<ArticlePhoto> articlePhotos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            for (String path : this.articlePhotoPathList) {
                Optional<ArticlePhoto> articlePhoto = articlePhotoService
                        .register(PhotoUtils.getImageBytes(path), DEFAULT_SHORT_DESCRIPTION, DEFAULT_ALTERNATE_TEXT);

                // Add article photo to article photo list if present.
                articlePhoto.ifPresent(articlePhotos::add);
            }
        }

        return articlePhotos;
    }
}