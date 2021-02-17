package ru.sfedu.shop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.shop.api.DataProvider;
import ru.sfedu.shop.api.DataProviderCsv;
import ru.sfedu.shop.api.DataProviderJdbc;
import ru.sfedu.shop.api.DataProviderXml;
import ru.sfedu.shop.beans.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.sfedu.shop.Constants.*;

public class Main {


    public final static Logger LOG = LogManager.getLogger(DataProviderCsv.class);


    public static void main(String[] args) {
        initEnvironmentConstants();

        DataProvider dataProvider = resolveDataProvider(args);
        LOG.info("DataProvider chosen: Success!");
        if (dataProvider == null) {
            LOG.error(Constants.BAD_ARGS);
            System.exit(1);
        }
        String result = resolveAPIResult(dataProvider, args);
        LOG.info("Api result {}", result);
    }


    /**
     * Инициализации констант из файла конфигурации
     *
     * @return void
     */
    public static void initEnvironmentConstants() {
        String path;
        if (System.getProperty("environment") != null) {
            path = System.getProperty("environment");
        } else {
            path = "/environment.properties";
        }
        ConfigurationUtil configurationUtil = new ConfigurationUtil(path);

        Constants.CATEGORY_FILE_XML = new File(configurationUtil.readConfig(Constants.CATEGORY_KEY_XML));
        Constants.CATEGORY_FILE_CSV = new File(configurationUtil.readConfig(Constants.CATEGORY_KEY_CSV));

        Constants.SODA_FILE_XML = new File(configurationUtil.readConfig(Constants.SODA_KEY_XML));
        Constants.SODA_FILE_CSV = new File(configurationUtil.readConfig(Constants.SODA_KEY_CSV));

        COMPUTER_FILE_XML = new File(configurationUtil.readConfig(Constants.COMPUTER_KEY_XML));
        Constants.COMPUTER_FILE_CSV = new File(configurationUtil.readConfig( Constants.COMPUTER_KEY_CSV ));

        Constants.FRIDGE_FILE_XML = new File(configurationUtil.readConfig( Constants.FRIDGE_KEY_XML));
        Constants.FRIDGE_FILE_CSV = new File(configurationUtil.readConfig( Constants.FRIDGE_KEY_CSV));

        Constants.BUCKET_FILE_XML = new File(configurationUtil.readConfig( Constants.BUCKET_KEY_XML));
        Constants.BUCKET_FILE_CSV = new File(configurationUtil.readConfig( Constants.BUCKET_KEY_CSV ));

        Constants.SESSION_FILE_XML = new File(configurationUtil.readConfig( Constants.SESSION_KEY_XML));
        Constants.SESSION_FILE_CSV = new File(configurationUtil.readConfig( Constants.SESSION_KEY_CSV));

        Constants.RECEIPT_FILE_XML = new File(configurationUtil.readConfig( Constants.RECEIPT_KEY_XML));
        Constants.RECEIPT_FILE_CSV = new File(configurationUtil.readConfig( Constants.RECEIPT_KEY_CSV));

        Constants.DB_FILES = Arrays.asList(
                CATEGORY_FILE_CSV, CATEGORY_FILE_XML,
                SODA_FILE_CSV, SODA_FILE_XML,
                COMPUTER_FILE_CSV, COMPUTER_FILE_XML,
                FRIDGE_FILE_CSV, FRIDGE_FILE_XML,
                BUCKET_FILE_CSV, BUCKET_FILE_XML,
                SESSION_FILE_CSV, SESSION_FILE_XML,
                RECEIPT_FILE_CSV, RECEIPT_FILE_XML,
                JDBC_MV, JDBC_TRACE
        );
    }


    /**
    * Вызов метода API согласно параметрам
     * @return String - Constants.BAD_ARGS, Constants.FAILURE, Constants.SUCCESS
    * */
    private static String resolveAPIResult(DataProvider dataProvider, String[] arguments) {
        try {
            String action = arguments[1];
            switch (action.trim().toLowerCase()) {
                case Constants.INIT: {
                    dataProvider.init();
                    break;
                }
                // CRUD

                // GET ALL
                case Constants.GET_ALL_CATEGORY: {
                    List items = dataProvider.getAll(Constants.CATEGORY);
                    LOG.info("Categories: \n{}", items);
                    break;
                }
                case Constants.GET_ALL_COMPUTER: {
                    List items = dataProvider.getAll(Constants.COMPUTER);
                    LOG.info("Computers: \n{}", items);
                    break;
                }
                case Constants.GET_ALL_FRIDGE: {
                    List items = dataProvider.getAll(Constants.FRIDGE);
                    LOG.info("Fridges: \n{}", items);
                    break;
                }
                case Constants.GET_ALL_RECEIPT: {
                    List items = dataProvider.getAll(Constants.RECEIPT);
                    LOG.info("Receipts: \n{}", items);
                    break;
                }
                case Constants.GET_ALL_SODA: {
                    List items = dataProvider.getAll(Constants.SODA);
                    LOG.info("Sodas: \n{}", items);
                    break;
                }

                // GET BY ID
                case Constants.GET_CATEGORY: {
                    long id = Long.parseLong(arguments[2]);
                    Optional<Category> item = dataProvider.getCategory(id);
                    if (item.isPresent()) {
                        LOG.info(item.get());
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                    break;
                }
                case Constants.GET_COMPUTER: {
                    long id = Long.parseLong(arguments[2]);
                    Optional<Computer> item = dataProvider.getComputer(id);
                    if (item.isPresent()) {
                        LOG.info(item.get());
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                    break;
                }
                case Constants.GET_FRIDGE: {
                    long id = Long.parseLong(arguments[2]);
                    Optional<Fridge> item = dataProvider.getFridge(id);
                    if (item.isPresent()) {
                        LOG.info(item.get());
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                    break;
                }
                case Constants.GET_RECEIPT: {
                    long id = Long.parseLong(arguments[2]);
                    Optional<Receipt> item = dataProvider.getReceipt(id);
                    if (item.isPresent()) {
                        LOG.info(item.get());
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                    break;
                }
                case Constants.GET_SODA: {
                    long id = Long.parseLong(arguments[2]);
                    Optional<Soda> item = dataProvider.getSoda(id);
                    if (item.isPresent()) {
                        LOG.info(item.get());
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                    break;
                }

                // INSERT
                case Constants.INSERT_COMPUTER: {
                    String item = arguments[2];
                    if (!dataProvider.insertComputer(item)) {
                        return Constants.FAILURE;
                    }
                    break;
                }
                case Constants.INSERT_FRIDGE: {
                    String item = arguments[2];
                    if (!dataProvider.insertFridge(item)) {
                        return Constants.FAILURE;
                    }
                    break;
                }
                case Constants.INSERT_SODA: {
                    String item = arguments[2];
                    if (!dataProvider.insertSoda(item)) {
                        return Constants.FAILURE;
                    }
                    break;
                }

                // SHOP ACTIONS
                case Constants.START_SESSION: {
                    LOG.info("Session started: {}", dataProvider.startSession());
                    break;
                }
                case Constants.FINISH_SESSION: {
                    String userSession = arguments[2];
                    if (!dataProvider.finishSession(userSession)) {
                        return Constants.FAILURE;
                    }
                    break;
                }
                case Constants.ADD_PRODUCT_TO_BUCKET: {
                    String userSession = arguments[2];
                    long prodId = Long.parseLong(arguments[3]);
                    String category = arguments[4];
                    if (!dataProvider.addProduct(userSession, prodId, category)) {
                        return Constants.FAILURE;
                    }
                    break;
                }

                default:
                    return Constants.BAD_ARGS;
            }
            return Constants.SUCCESS;
        } catch (Exception e) {
            LOG.error("Action arguments error: {}", e);
            System.exit(1);
        }
        return Constants.FAILURE;
    }

    /**
    * Выбор нужного dataProvider
    * @return DataProvider
    * */
    private static DataProvider resolveDataProvider(String[] arguments) {
        LOG.info("Resolving csv provider");
        LOG.debug("Resolving csv provider by args {}", String.join(",", arguments));
        if (arguments.length == 0) {
            throw new RuntimeException("Empty arguments!");
        }
        switch (arguments[0]) {
            case Constants.CSV_DATA_PROVIDER:
                return new DataProviderCsv();
            case Constants.XML_DATA_PROVIDER:
                return new DataProviderXml();
            case Constants.JDBC_DATA_PROVIDER:
                return new DataProviderJdbc();
        }
        return null;
    }
}
