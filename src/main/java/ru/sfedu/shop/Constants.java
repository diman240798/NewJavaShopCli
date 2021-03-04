package ru.sfedu.shop;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constants {

    // ACTIONS
    public static final String INIT = "init";

    // Add
    public static final String INSERT_FRIDGE = "insert_fridge";
    public static final String INSERT_SODA = "insert_soda";
    public static final String INSERT_COMPUTER = "insert_computer";

    // GET ALL
    public static final String GET_ALL_COMPUTER = "get_all_computer";
    public static final String GET_ALL_CATEGORY = "get_all_category";
    public static final String GET_ALL_FRIDGE = "get_all_fridge";
    public static final String GET_ALL_SODA = "get_all_soda";
    public static final String GET_ALL_RECEIPT = "get_all_receipt";

    // GET BY ID
    public static final String GET_COMPUTER = "get_computer";
    public static final String GET_CATEGORY = "get_category";
    public static final String GET_FRIDGE = "get_fridge";
    public static final String GET_SODA = "get_soda";
    public static final String GET_RECEIPT = "get_receipt";

    // Shop
    public static final String ADD_PRODUCT_TO_BUCKET = "add_product";
    public static final String CLOSE_BUCKET = "close_bucket";

    // Models
    public static final String BUCKET = "bucket";
    public static final String CATEGORY = "category";
    public static final String COMPUTER = "computer";
    public static final String FRIDGE = "fridge";
    public static final String RECEIPT = "receipt";
    public static final String SODA = "soda";

    // Data Providers
    public static final String XML_DATA_PROVIDER = "xml";
    public static final String CSV_DATA_PROVIDER = "csv";
    public static final String JDBC_DATA_PROVIDER = "jdbc";


    // Categories
    public static final String CATEGORY_FRIDGE = "fridge";
    public static final String CATEGORY_SODA = "soda";
    public static final String CATEGORY_COMPUTER = "computer";

    public static final String PRODUCT_CATEGORY_SEPARATOR = ":";
    public static final String PRODUCTS_SEPATOR = "-";

    // KEYS from environment file
    public static final String BUCKET_KEY_XML = "bucket_xml";
    public static final String BUCKET_KEY_CSV = "bucket_csv";
    public static final String CATEGORY_KEY_XML = "category_xml";
    public static final String CATEGORY_KEY_CSV = "category_csv";
    public static final String COMPUTER_KEY_XML = "computer_xml";
    public static final String COMPUTER_KEY_CSV = "computer_csv";
    public static final String FRIDGE_KEY_XML = "fridge_xml";
    public static final String FRIDGE_KEY_CSV = "fridge_csv";
    public static final String RECEIPT_KEY_XML = "receipt_xml";
    public static final String RECEIPT_KEY_CSV = "receipt_csv";
    public static final String SODA_KEY_XML = "soda_xml";
    public static final String SODA_KEY_CSV = "soda_csv";


    // Files CSV
    public static File BUCKET_FILE_CSV = new File("./csv/bucket.csv");
    public static File CATEGORY_FILE_CSV = new File("./csv/category.csv");
    public static File COMPUTER_FILE_CSV = new File("./csv/computer.csv");
    public static File FRIDGE_FILE_CSV = new File("./csv/fridge.csv");
    public static File RECEIPT_FILE_CSV = new File("./csv/receipt.csv");
    public static File SODA_FILE_CSV = new File("./csv/soda.csv");

    // Files XML
    public static File BUCKET_FILE_XML = new File("./xml/BUCKET.xml");
    public static File CATEGORY_FILE_XML = new File("./xml/category.xml");
    public static File COMPUTER_FILE_XML = new File("./xml/computer.xml");
    public static File FRIDGE_FILE_XML = new File("./xml/fridge.xml");
    public static File RECEIPT_FILE_XML = new File("./xml/receipt.xml");
    public static File SODA_FILE_XML = new File("./xml/soda.xml");

    // File JDBC
    public static final String DB_URL = "jdbc:h2:./jdbc.db";
    public static final String USER = "user";
    public static final String PASS = "user";
    public static File JDBC_MV = new File("./jdbc.db.mv.db");
    public static File JDBC_TRACE = new File("./jdbc.db.trace.db");

    public static List<File> DB_FILES = Arrays.asList(
            CATEGORY_FILE_CSV, CATEGORY_FILE_XML,
            SODA_FILE_CSV, SODA_FILE_XML,
            COMPUTER_FILE_CSV, COMPUTER_FILE_XML,
            FRIDGE_FILE_CSV, FRIDGE_FILE_XML,
            BUCKET_FILE_CSV, BUCKET_FILE_XML,
            RECEIPT_FILE_CSV, RECEIPT_FILE_XML,
            JDBC_MV, JDBC_TRACE
    );




    // Messages
    public static final String BAD_ARGS = "BAD_ARGUMENTS";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    // JDBC
    public static final String BUCKET_CREATE_QUERY =
            "id VARCHAR(255) not NULL PRIMARY KEY, " +
            "products VARCHAR(255)";
    public static final String CATEGORY_CREATE_QUERY =
            "name VARCHAR(255) not NULL PRIMARY KEY";
    public static final String COMPUTER_CREATE_QUERY =
            "id BIGINT not NULL PRIMARY KEY, " +
            "name VARCHAR(255), " +
            "weight DOUBLE, " +
            "price DOUBLE, " +
            "category VARCHAR(255), " +
            "processorName VARCHAR(255), " +
            "processorPower INTEGER, " +
            "graphicsName VARCHAR(255), " +
            "graphicsVolume INTEGER, " +
            "integratedWifi BIT, " +
            "integratedBluetooth BIT";
    public static final String FRIDGE_CREATE_QUERY =
            "id BIGINT not NULL PRIMARY KEY, " +
            "name VARCHAR(255), " +
            "weight DOUBLE, " +
            "price DOUBLE, " +
            "category VARCHAR(255), " +
            "volume INT, " +
            "color VARCHAR(255), " +
            "power INT, " +
            "noFrost BIT";
    public static final String RECEIPT_CREATE_QUERY =
            "id BIGINT not NULL PRIMARY KEY, " +
            "products VARCHAR(255)," +
            "totalPrice DOUBLE";
    public static final String SODA_CREATE_QUERY =
            "id BIGINT not NULL PRIMARY KEY, " +
            "name VARCHAR(255), " +
            "weight DOUBLE, " +
            "price DOUBLE, " +
            "category VARCHAR(255), " +
            "flavour VARCHAR(255)," +
            "sparkled BIT";

    public static final List<String> CREATE_TABLES_QUERY_LIST = Arrays.asList(
            BUCKET_CREATE_QUERY,
            CATEGORY_CREATE_QUERY,
            COMPUTER_CREATE_QUERY,
            FRIDGE_CREATE_QUERY,
            RECEIPT_CREATE_QUERY,
            SODA_CREATE_QUERY
    );
    public static final List<String> ENTITIES = Arrays.asList(
            BUCKET,
            CATEGORY,
            COMPUTER,
            FRIDGE,
            RECEIPT,
            SODA
    );
}
