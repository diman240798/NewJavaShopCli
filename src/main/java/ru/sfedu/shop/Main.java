package ru.sfedu.shop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.shop.api.AbstractDataProvider;
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

        List<String> arguments = Arrays.asList(args);
        LOG.debug(arguments.toString());

        AbstractDataProvider dataProvider = resolveDataProvider(arguments);
        if (dataProvider == null) {
            LOG.error(Constants.BAD_ARGS);
            System.exit(1);
        }
        String result = resolveAPIResult(dataProvider, arguments);
        System.out.print(result);
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


    private static String resolveAPIResult(AbstractDataProvider dataProvider, List<String> arguments) {
        try {
            String action = arguments.get(1);
            switch (action.trim().toLowerCase()) {
                case Constants.INIT: {
                    dataProvider.init();
                    break;
                }
                // CRUD

                // GET ALL
                case Constants.GET_ALL_CATEGORY: {
                    List items = dataProvider.getAll(Constants.CATEGORY);
                    LOG.info(items);
                }
                case Constants.GET_ALL_COMPUTER: {
                    List items = dataProvider.getAll(Constants.COMPUTER);
                    LOG.info(items);
                }
                case Constants.GET_ALL_FRIDGE: {
                    List items = dataProvider.getAll(Constants.FRIDGE);
                    LOG.info(items);
                }
                case Constants.GET_ALL_RECEIPT: {
                    List items = dataProvider.getAll(Constants.RECEIPT);
                    LOG.info(items);
                }
                case Constants.GET_ALL_SODA: {
                    List items = dataProvider.getAll(Constants.SODA);
                    LOG.info(items);
                }

                // GET BY ID
                case Constants.GET_CATEGORY: {
                    long id = Long.parseLong(arguments.get(2));
                    Optional<Category> item = dataProvider.getCategory(id);
                    if (item.isPresent()) {
                        LOG.info(item);
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                }
                case Constants.GET_COMPUTER: {
                    long id = Long.parseLong(arguments.get(2));
                    Optional<Computer> item = dataProvider.getComputer(id);
                    if (item.isPresent()) {
                        LOG.info(item);
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                }
                case Constants.GET_FRIDGE: {
                    long id = Long.parseLong(arguments.get(2));
                    Optional<Fridge> item = dataProvider.getFridge(id);
                    if (item.isPresent()) {
                        LOG.info(item);
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                }
                case Constants.GET_RECEIPT: {
                    long id = Long.parseLong(arguments.get(2));
                    Optional<Receipt> item = dataProvider.getReceipt(id);
                    if (item.isPresent()) {
                        LOG.info(item);
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                }
                case Constants.GET_SODA: {
                    long id = Long.parseLong(arguments.get(2));
                    Optional<Soda> item = dataProvider.getSoda(id);
                    if (item.isPresent()) {
                        LOG.info(item);
                    } else {
                        LOG.info("Item with such id doesnt exist");
                        LOG.debug("Item with such id doesnt exist: {}", id);
                    }
                }

                // INSERT
                case Constants.INSERT_COMPUTER: {
                    String item = arguments.get(2);
                    dataProvider.insertComputer(item);
                }
                case Constants.INSERT_FRIDGE: {
                    String item = arguments.get(2);
                    dataProvider.insertFridge(item);
                }
                case Constants.INSERT_SODA: {
                    String item = arguments.get(2);
                    dataProvider.insertSoda(item);
                }


                // SHOP ACTIONS
                case Constants.START_SESSION: {
                    dataProvider.startSession();
                    break;
                }
                case Constants.FINISH_SESSION: {
                    String userSession = arguments.get(2);
                    if (!dataProvider.finishSession(userSession)) {
                        return Constants.FAILURE;
                    }
                }
                case Constants.ADD_PRODUCT_TO_BUCKET: {
                    String userSession = arguments.get(2);
                    long prodId = Long.parseLong(arguments.get(3));
                    String category = arguments.get(4);
                    if (!dataProvider.addProduct(userSession, prodId, category)) {
                        return Constants.FAILURE;
                    }
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

    private static AbstractDataProvider resolveDataProvider(List<String> arguments) {
        LOG.info("Resolving csv provider");
        LOG.debug("Resolving csv provider by args {}", String.join(",", arguments));
        if (arguments.size() == 0) return null;
        switch (arguments.get(0)) {
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
