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
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public interface DataProvider {

    Logger LOG = LogManager.getLogger(DataProviderCsv.class);

    // INIT

    /**
     * Инициализации базовых данных
     *
     * @return void
     */
    default void init() throws Exception {
        setItemsList(InitializerData.CATEGORIES, Constants.CATEGORY);
        setItemsList(InitializerData.COMPUTERS, Constants.COMPUTER);
        setItemsList(InitializerData.FRIDGES, Constants.FRIDGE);
        setItemsList(InitializerData.SODA, Constants.SODA);
    }

    // CRUD

    /**
     * Поиск по имени категории
     *
     * @param name - название категории
     * @return Optional<Category>
     */
    default Optional<Category> getCategory(String name) {
        List<Category> category = getAll(Constants.CATEGORY);
        return category.stream()
                .filter(it -> it.getName().equals(name))
                .findFirst();
    }

    /**
     * Поиск по id
     *
     * @param id - идентификатор модели
     * @return Optional<Computer>
     */
    default Optional<Computer> getComputer(long id) {
        List<Computer> items = getAll(Constants.COMPUTER);
        return items.stream()
                .filter(it -> it.getId() == id)
                .findFirst();
    }

    /**
     * Поиск по id
     *
     * @param id - идентификатор модели
     * @return Optional<Fridge>
     */
    default Optional<Fridge> getFridge(long id) {
        List<Fridge> items = getAll(Constants.FRIDGE);
        return items.stream()
                .filter(it -> it.getId() == id)
                .findFirst();
    }

    /**
     * Поиск по id
     *
     * @param id - идентификатор модели
     * @return Optional<Receipt>
     */
    default Optional<Receipt> getReceipt(long id) {
        List<Receipt> items = getAll(Constants.RECEIPT);
        return items.stream()
                .filter(it -> ((Receipt) it).getId() == id)
                .findFirst();
    }

    /**
     * Поиск по id
     *
     * @param id - идентификатор модели
     * @return Optional<Soda>
     */
    default Optional<Soda> getSoda(long id) {
        List<Soda> items = getAll(Constants.SODA);
        return items.stream()
                .filter(it -> it.getId() == id)
                .findFirst();
    }

    /**
     * Вставка в базу данных
     *
     * @param modelStr - строка пользователя с консоли
     * @return boolean - успешно, неуспешно
     */
    default boolean insertComputer(String modelStr) throws Exception {
        Optional<Computer> itemOption = InsertManager.getComputerFromString(modelStr);
        if (itemOption.isPresent()) {
            Computer item = itemOption.get();
            String entity = Constants.COMPUTER;
            List<Computer> items = getAll(entity);
            List<Long> ids = items.stream().map(Product::getId).collect(Collectors.toList());
            return insertCheckId(item.getId(), ids, item, items, entity);
        }
        return false;
    }

    /**
     * Вставка в базу данных
     *
     * @param modelStr - строка пользователя с консоли
     * @return boolean - успешно, неуспешно
     */
    default boolean insertFridge(String modelStr) throws Exception {
        Optional<Fridge> itemOption = InsertManager.getFridgeFromString(modelStr);
        if (itemOption.isPresent()) {
            Fridge item = itemOption.get();
            String entity = Constants.FRIDGE;
            List<Fridge> items = getAll(entity);
            List<Long> ids = items.stream().map(Product::getId).collect(Collectors.toList());
            return insertCheckId(item.getId(), ids, item, items, entity);
        }
        return false;
    }

    /**
     * Вставка в базу данных
     *
     * @param modelStr - строка пользователя с консоли
     * @return boolean - успешно, неуспешно
     */
    default boolean insertSoda(String modelStr) throws Exception {
        Optional<Soda> itemOption = InsertManager.getSodaFromString(modelStr);
        if (itemOption.isPresent()) {
            Soda item = itemOption.get();
            String entity = Constants.SODA;
            List<Soda> items = getAll(entity);
            List<Long> ids = items.stream().map(Product::getId).collect(Collectors.toList());
            return insertCheckId(item.getId(), ids, item, items, entity);
        }
        return false;
    }


    // SHOP

    /**
     * Завершение покупок в магазине
     *
     * @param bucketId - сессия юзера
     * @return boolean - успешно, неуспешно
     */
    default boolean closeBucket(String bucketId) throws Exception {
        LOG.info("Getting bucket entity");
        Optional<Bucket> bucketOption = getBucketById(bucketId);

        if (!bucketOption.isPresent()) {
            LOG.info("No bucket with such id exists. No check will be printed.");
            return false;
        }

        LOG.info("Found bucket");
        Bucket bucket = bucketOption.get();
        List<Product> products = bucket.getProducts();

        LOG.info("Got bucket products. Counting receipt.");

        LOG.info("Receipt data ready. Printing.");
        long receiptId = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        double totalPrice = products.stream().map(Product::getPrice).reduce(Double::sum).orElse(0.0);
        Receipt receipt = new Receipt(receiptId, products, totalPrice);
        List<Receipt> receipts = getAll(Constants.RECEIPT);
        receipts.add(receipt);
        setItemsList(receipts, Constants.RECEIPT);
        LOG.info("Your receipt is: \n{}", receipt);

        deleteBucket(bucket);
        return true;
    }

    /**
     * Добавление продукта в корзину
     *
     * @param productId    - id продукта
     * @param prodCategory - категория продукта
     * @param bucketId
     * @return void
     */
    default boolean addProduct(long productId, String prodCategory, Optional<String> bucketId) throws Exception {
        Optional<Category> categoryOption = getCategoryByName(prodCategory);
        if (!categoryOption.isPresent()) {
            LOG.info("No such category: {}", prodCategory);
            return false;
        }

        Category category = categoryOption.get();

        Optional<Product> productOptional = getProductByIdAndCategory(productId, category.getName());

        if (!productOptional.isPresent()) {
            LOG.info("Product with such id was not found: {}", productId);
            return false;
        }

        Product product = productOptional.get();
        LOG.info("Found product: {}", product);
        Optional<Bucket> bucketOption = getBucketById(bucketId);

        Bucket bucket;
        if (bucketOption.isPresent()) {
            bucket = bucketOption.get();
        } else if (bucketId.isPresent()) {
            LOG.error("Bucket with id: {} doesnt exist!", bucketId.get());
            return false;
        } else {
            bucket = new Bucket(UUID.randomUUID().toString());
        }

        bucket.getProducts().add(product);
        if (upsertBucket(bucket)) {
            LOG.info("Product added succesfully: Bucket id {}", bucket.getId());
            return true;
        } else {
            LOG.info("Error inserting bucket");
            return false;
        }
    }


    // OUT OF USE CASE

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
     * @param category  - категория продукта
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
     * @param bucketIdOption - Optional<id> корзины
     * @return void
     */
    default Optional<Bucket> getBucketById(Optional<String> bucketIdOption) {
        if (!bucketIdOption.isPresent()) return Optional.empty();
        String bucketId = bucketIdOption.get();
        return getBucketById(bucketId);
    }

    /**
     * Поиск корзины по сессии пользователя
     *
     * @param bucketId - id корзины
     * @return void
     */
    default Optional<Bucket> getBucketById(String bucketId) {
        List<Bucket> all = getAll(Constants.BUCKET);
        return all.stream()
                .filter(it -> Objects.equals(it.getId().toLowerCase(), bucketId.toLowerCase()))
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
        if (newList.size() == all.size()) return false;
        return setItemsList(newList, entity);
    }

    /**
     * Заполнить таблицу списком
     *
     * @param items  - список элементов
     * @param entity - тип объектов
     * @return void
     */
    <T> boolean setItemsList(List<T> items, String entity) throws Exception;

    /**
     * Получить весь список
     *
     * @param entity - тип объектов
     * @return void
     */
    <T> List<T> getAll(String entity);

    /**
     * Проверка id на уникальность и вставка
     *
     * @param id     - идентификатор модели
     * @param ids    - список id существующих моделей
     * @param item   - модель
     * @param items  - список моделей из бд
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
        setItemsList(items, entity);
        return true;
    }

    /**
     * Вставить/обновить корзину
     *
     * @param item - модель корзины
     * @return boolean
     */
    default boolean upsertBucket(Bucket item) throws Exception {
        String entity = Constants.BUCKET;
        List<Bucket> itemsDb = getAll(entity);
        List<Bucket> items = itemsDb.stream()
                .filter(it -> !Objects.equals(it.getId(), item.getId()))
                .collect(Collectors.toList());
        items.add(item);
        return setItemsList(items, entity);
    }

    /**
     * Вставить корзину
     *
     * @param item - модель корзины
     * @return boolean
     */
    default boolean insertBucket(Bucket item) throws Exception {
        String entity = Constants.BUCKET;
        List<Bucket> itemsDb = getAll(entity);
        if (itemsDb.stream().anyMatch(it -> Objects.equals(it.getId(), item.getId()))) {
            LOG.info("Item with such id already exists");
            LOG.info("Item with such id already exists {}", item.getId());
            return false;
        }
        itemsDb.add(item);
        return setItemsList(itemsDb, entity);
    }
}
