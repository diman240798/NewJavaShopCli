package ru.sfedu.shop.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.shop.Constants;
import ru.sfedu.shop.api.helper.InitializerData;
import ru.sfedu.shop.api.helper.InsertManager;
import ru.sfedu.shop.beans.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public interface DataProvider {

    Logger LOG = LogManager.getLogger(DataProviderCsv.class);

    // INIT
    /**
     * Инициализации базовых данных
     * @return void
     */
    default void init() throws Exception {
        insertList(InitializerData.CATEGORIES, Constants.CATEGORY);
        insertList(InitializerData.COMPUTERS, Constants.COMPUTER);
        insertList(InitializerData.FRIDGES, Constants.FRIDGE);
        insertList(InitializerData.SODA, Constants.SODA);
    }

    // CRUD
    /**
     * Поиск по id
     * @param id - идентификатор модели
     * @return Optional<Category>
     */
    default Optional<Category> getCategory(long id) {
        return getAll(Constants.CATEGORY).stream()
                .filter(it -> ((Category) it).getId() == id)
                .findFirst();
    }

    /**
     * Поиск по id
     * @param id - идентификатор модели
     * @return Optional<Category>
     */
    default Optional<Computer> getComputer(long id) {
        return getAll(Constants.COMPUTER).stream()
                .filter(it -> ((Computer) it).getId() == id)
                .findFirst();
    }

    /**
     * Поиск по id
     * @param id - идентификатор модели
     * @return Optional<Category>
     */
    default Optional<Fridge> getFridge(long id) {
        return getAll(Constants.FRIDGE).stream()
                .filter(it -> ((Fridge) it).getId() == id)
                .findFirst();
    }

    /**
     * Поиск по id
     * @param id - идентификатор модели
     * @return Optional<Category>
     */
    default Optional<Receipt> getReceipt(long id) {
        return getAll(Constants.RECEIPT).stream()
                .filter(it -> ((Receipt) it).getId() == id)
                .findFirst();
    }

    /**
     * Поиск по id
     * @param id - идентификатор модели
     * @return Optional<Category>
     */
    default Optional<Soda> getSoda(long id) {
        return getAll(Constants.SODA).stream()
                .filter(it -> ((Soda) it).getId() == id)
                .findFirst();
    }

    /**
     * Вставка в базу данных
     * @param modelStr - строка пользователя с консоли
     * @return boolean - успешно, неуспешно
     */
    default boolean insertComputer(String modelStr) throws Exception {
        Optional<Computer> itemOption = InsertManager.getComputerFromString(modelStr);
        if (itemOption.isPresent()) {
            Computer item = itemOption.get();
            String entity = Constants.COMPUTER;
            List items = getAll(entity);
            List ids = (List) items.stream().map(it -> ((Computer) it).getId()).collect(Collectors.toList());
            return insertCheckId(item.getId(), ids, item, items, entity);
        }
        return false;
    }

    /**
     * Вставка в базу данных
     * @param modelStr - строка пользователя с консоли
     * @return boolean - успешно, неуспешно
     */
    default boolean insertFridge(String modelStr) throws Exception {
        Optional<Fridge> itemOption = InsertManager.getFridgeFromString(modelStr);
        if (itemOption.isPresent()) {
            Fridge item = itemOption.get();
            String entity = Constants.FRIDGE;
            List items = getAll(entity);
            List ids = (List) items.stream().map(it -> ((Fridge) it).getId()).collect(Collectors.toList());
            return insertCheckId(item.getId(), ids, item, items, entity);
        }
        return false;
    }

    /**
     * Вставка в базу данных
     * @param modelStr - строка пользователя с консоли
     * @return boolean - успешно, неуспешно
     */
    default boolean insertSoda(String modelStr) throws Exception {
        Optional<Soda> itemOption = InsertManager.getSodaFromString(modelStr);
        if (itemOption.isPresent()) {
            Soda item = itemOption.get();
            String entity = Constants.SODA;
            List items = getAll(entity);
            List ids = (List) items.stream().map(it -> ((Soda) it).getId()).collect(Collectors.toList());
            return insertCheckId(item.getId(), ids, item, items, entity);
        }
        return false;
    }


    // SHOP
    /**
     * Завершение покупок в магазине
     *
     * @param userSession     - сессия юзера
     * @return boolean - успешно, неуспешно
     */
    default boolean finishSession(String userSession) throws Exception {
        LOG.info("Finish session");
        LOG.debug("Finish session for userSession {}", userSession);
        LOG.info("Getting session entity");
        Optional<Session> sessionOption = getSessionByUserSession(userSession);
        if (!sessionOption.isPresent()) {
            LOG.info("Could not find session with key: {}", userSession);
            return false;
        }

        LOG.info("Found session");
        LOG.info("Getting bucket entity");
        Optional<Bucket> bucketOption = getBucketByUserSession(userSession);

        long sessionId = sessionOption.get().getId();

        if (!bucketOption.isPresent()) {
            LOG.info("You have not added single product to bucket. No check will be printed.");
            boolean deleteSession = deleteSession(sessionId);
            if (deleteSession) {
                LOG.info("Session closed");
                return true;
            } else {
                LOG.info("Error closing session");
                LOG.debug("Error closing session {}", sessionOption.get());
                return false;
            }
        }

        LOG.info("Found bucket");
        Bucket bucket = bucketOption.get();
        List<String> products = bucket.getProductsList();

        LOG.info("Got bucket products. Counting receipt.");

        StringBuilder receiptTextSb = new StringBuilder();
        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);

        products.forEach(productStr -> {
            String[] split = productStr.split(Constants.PRODUCT_CATEGORY_SEPARATOR);
            String category = split[0];
            long productId = Long.parseLong(split[1]);
            Product product = getProductByIdAndCategory(productId, category).get();

            receiptTextSb.append(product.toString()).append("\n");
            totalPrice.updateAndGet(v -> v + product.getPrice());
        });

        LOG.info("Receipt data ready. Printing.");
        long receiptId = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        Receipt receipt = new Receipt(receiptId, receiptTextSb.toString(), totalPrice.get());
        List items = getAll(Constants.RECEIPT);
        items.add(receipt);
        insertList(items, Constants.RECEIPT);
        LOG.info("Your receipt is: \n{}", receipt);

        deleteBucket(bucket);
        deleteSession(sessionId);
        return true;
    }

    /**
     * Создание сессии покупок в магазине
     *
     * @return Session - объект сессии
     */
    default Session startSession() throws Exception {
        LOG.info("Start session");
        LOG.debug("Start session for dataProvider: {}", Constants.XML_DATA_PROVIDER);
        long id = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        Session session = new Session(id);
        List sessions = getAll(Constants.SESSION);
        sessions.add(session);
        insertList(sessions, Constants.SESSION);
        LOG.info("Your session key:  {}", session.getSession());
        return session;
    }

    /**
     * Добавление продукта в корзину
     *
     * @param userSession - сессия пользователя
     * @param productId - id продукта
     * @param prodCategory - категория продукта
     * @return void
     */
    default boolean addProduct(String userSession, long productId, String prodCategory) throws Exception {
        Optional<Session> sessionOption = getSessionByUserSession(userSession);
        if (!sessionOption.isPresent()) {
            LOG.info("Could not find session with key: {}", userSession);
            return false;
        }

        LOG.info("Found session");

        Optional<Category> categoryOption = getCategoryByName(prodCategory);
        if (!categoryOption.isPresent()) {
            LOG.info("No such category: {}", prodCategory);
            return false;
        }

        Category category = categoryOption.get();

        Optional productOptional = getProductByIdAndCategory(productId, category.getName());

        if (!productOptional.isPresent()) {
            LOG.info("Product with such id was not found: {}", userSession);
            return false;
        }

        Object product = productOptional.get();
        LOG.info("Found product: {}", product);
        Optional<Bucket> bucketOption = getBucketByUserSession(userSession);


        Bucket bucket = bucketOption.orElseGet(
                () -> new Bucket(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), userSession)
        );

        bucket.addProduct(productId, category);
        if (upsertBucket(bucket)) {
            LOG.info("Product added succesfully");
            return true;
        } else {
            LOG.info("Error inserting bucket");
            return false;
        }
    }


    // OUT OF USE CASE
    /**
     * Поиск сессии по сессии пользователя
     *
     * @param userSession - сессия, зажанная пользователем
     * @return void
     */
    default Optional<Session> getSessionByUserSession(String userSession) {
        List<Session> all = getAll(Constants.SESSION);
        return all.stream()
                .filter(it -> Objects.equals(it.getSession(), userSession.toLowerCase()))
                .findFirst();
    }

    /**
     * Поиск категории по категории пользователя
     *
     * @param userCategory - категоря, заданная пользователем
     * @return void
     */
    default Optional<Category> getCategoryByName(String userCategory) {
        List<Category> all = getAll(Constants.CATEGORY);
        return all.stream()
                .filter(it -> Objects.equals(it.getName(), userCategory.toLowerCase()))
                .findFirst();
    }

    /**
     * Поиск продукта по id и категории
     *
     * @param productId - id продукта
     * @param category - категория продукта
     * @return void
     */
    default Optional<Product> getProductByIdAndCategory(long productId, String category) {
        List<Product> all = getAll(category);
        return all.stream()
                .filter(it -> Objects.equals(it.getId(), productId))
                .findFirst();
    }

    /**
     * Поиск корзины по сессии пользователя
     *
     * @param userSession - сессия пользователя
     * @return void
     */
    default Optional<Bucket> getBucketByUserSession(String userSession) {
        List<Bucket> all = getAll(Constants.BUCKET);
        return all.stream()
                .filter(it -> Objects.equals(it.getSession().toLowerCase(), userSession.toLowerCase()))
                .findFirst();
    }

    /**
     * Удалить козину
     *
     * @param bucket - корзина
     * @return void
     */
    default boolean deleteBucket(Bucket bucket) throws Exception {
        String entity = Constants.BUCKET;
        List<Bucket> all = getAll(entity);
        List<Bucket> newList = all.stream()
                .filter(it -> !Objects.equals(it.getId(), bucket.getId()))
                .collect(Collectors.toList());
        return insertList(newList, entity);
    }

    /**
     * Удалить сессию
     *
     * @param sessionId - id сессии
     * @return void
     */
    default boolean deleteSession(long sessionId) throws Exception {
        String entity = Constants.SESSION;
        List<Session> all = getAll(entity);
        List<Session> newList = all.stream()
                .filter(it -> !Objects.equals(it.getId(), sessionId))
                .collect(Collectors.toList());
        return insertList(newList, entity);
    }

    /**
     * Заполнить таблицу списком
     *
     * @param items - список элементов
     * @param entity - тип объектов
     * @return void
     */
    boolean insertList(List items, String entity) throws Exception;

    /**
     * Получить весь список
     *
     * @param entity - тип объектов
     * @return void
     */
    List getAll(String entity);

    /**
     * Проверка id на уникальность и вставка
     *
     * @param id - идентификатор модели
     * @param ids - список id существующих моделей
     * @param item - модель
     * @param items - список моделей из бд
     * @param entity - тип объектов
     * @return boolean - успешно, неуспешно
     */
    default boolean insertCheckId(long id, List<Long> ids, Object item, List items, String entity) throws Exception {
        boolean present = ids.stream().anyMatch(it -> it == id);
        if (present) {
            LOG.info("Item with such id exists already!");
            return false;
        }
        items.add(item);
        insertList(items, entity);
        return true;
    }

    /**
     * Вставить/обновить корзину
     *
     * @param item - модель корзины
     * @return void
     */
    default boolean upsertBucket(Bucket item) throws Exception {
        String entity = Constants.BUCKET;
        List items = (List) getAll(entity).stream()
                .filter(it -> ((Bucket) it).getId() != item.getId())
                .collect(Collectors.toList());
        items.add(item);
        return insertList(items, entity);

    }
}
