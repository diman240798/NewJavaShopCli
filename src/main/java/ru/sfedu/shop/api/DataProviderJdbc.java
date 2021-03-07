package ru.sfedu.shop.api;

import ru.sfedu.shop.Constants;
import ru.sfedu.shop.api.helper.ExFunction;
import ru.sfedu.shop.api.helper.InitializerData;
import ru.sfedu.shop.api.helper.ProductTransformer;
import ru.sfedu.shop.beans.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.sfedu.shop.Constants.*;

public class DataProviderJdbc implements DataProvider {

    private static final String QUERY_CREATE = "CREATE TABLE IF NOT EXISTS %s (%s)";
    private static final String QUERY_GET_ALL = "SELECT * FROM %s";
    private static final String QUERY_INSERT = "INSERT INTO %s VALUES (%s)";
    private static final String QUERY_DELETE_ALL = "DELETE FROM %s";

    private static final String JDBC_DRIVER = "org.h2.Driver";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.info("Error registering jdbc driver!", e);
        }
    }


    public DataProviderJdbc() {
        createTables();
    }

    /**
     * Метод создания таблицы
     */
    private void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = conn.createStatement();
            List<String> queriesList = Constants.CREATE_TABLES_QUERY_LIST;

            for (int i = 0; i < queriesList.size(); i++) {

                String entity = Constants.ENTITIES.get(i);
                String query = queriesList.get(i);

                String queryCreate = String.format(QUERY_CREATE, entity, query);
                statement.executeUpdate(queryCreate);
            }
        } catch (Exception ex) {
            LOG.error("Error creating table: ", ex);
        }
    }

    /**
     * Метод выполнения выражений sql на jdbc
     */
    public <M> M runOn(ExFunction<Statement, M> consumer) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = conn.createStatement();
            return consumer.apply(statement);
        } catch (Exception ex) {
            LOG.error("Error creating jdbc statement for entity:", ex);
            return null;
        }
    }

    @Override
    public List getAll(String entity) {
        List result = new ArrayList<>();
        try {
            runOn(statemnt -> {
                ResultSet resultSet = statemnt.executeQuery(String.format(QUERY_GET_ALL, entity));
                while (resultSet.next()) {
                    Object model = getModel(resultSet, entity);
                    result.add(model);
                }
                return true;
            });
        } catch (Exception ex) {
            LOG.error("Error getting entity {} from jdbc", entity, ex);
        }
        return result;
    }

    private Object getModel(ResultSet resultSet, String entity) throws SQLException {
        switch (entity) {
            case Constants.BUCKET:
                return getModelBucket(resultSet);
            case Constants.CATEGORY:
                return getModelCategory(resultSet);
            case Constants.COMPUTER:
                return getModelComputer(resultSet);
            case Constants.FRIDGE:
                return getModelFridge(resultSet);
            case Constants.RECEIPT:
                return getModelReceipt(resultSet);
            case Constants.SODA:
                return getModelSoda(resultSet);
        }
        throw new RuntimeException("Unknown entity: " + entity);
    }

    private Bucket getModelBucket(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String productsStr = resultSet.getString("products");
        List<Product> productList = ProductTransformer.productStringToList(productsStr, this);
        return new Bucket(id, productList);
    }

    private Category getModelCategory(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        return new Category(name);
    }

    private Computer getModelComputer(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        double weight = resultSet.getDouble("weight");
        double price = resultSet.getDouble("price");
        String processorName = resultSet.getString("processorName");
        int processorPower = resultSet.getInt("processorPower");
        String graphicsName = resultSet.getString("graphicsName");
        int graphicsVolume = resultSet.getInt("graphicsVolume");
        boolean integratedWifi = resultSet.getBoolean("integratedWifi");
        boolean integratedBluetooth = resultSet.getBoolean("integratedBluetooth");
        Computer computer = new Computer(
                id,
                name,
                weight,
                price,
                processorName,
                processorPower,
                graphicsName,
                graphicsVolume,
                integratedWifi,
                integratedBluetooth
        );
        return computer;
    }

    private Fridge getModelFridge(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        double weight = resultSet.getDouble("weight");
        double price = resultSet.getDouble("price");
        int volume = resultSet.getInt("volume");
        String color = resultSet.getString("color");
        int power = resultSet.getInt("power");
        boolean noFrost = resultSet.getBoolean("noFrost");
        return new Fridge(id, name, weight, price, volume, color, power, noFrost);
    }

    private Receipt getModelReceipt(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");

        String productsStr = resultSet.getString("products");
        List<Product> productList = ProductTransformer.productStringToList(productsStr, this);

        double totalPrice = resultSet.getDouble("totalPrice");

        return new Receipt(id, productList, totalPrice);
    }

    private Soda getModelSoda(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        double weight = resultSet.getDouble("weight");
        double price = resultSet.getDouble("price");
        String flavour = resultSet.getString("flavour");
        boolean sparkled = resultSet.getBoolean("sparkled");
        return new Soda(id, name, weight, price, flavour, sparkled);
    }

    /**
     * Метод вставки записи
     */
    private boolean insert(Object model, String entity) {
        if (model == null) {
            LOG.info("Attempt to insert null");
            LOG.debug("Attempt to insert null");
            return false;
        }
        try {
            String values = getValues(model, entity);
            String query = String.format(QUERY_INSERT, entity, values);
            return runOn(statement -> statement.executeUpdate(query) != 0);
        } catch (Exception ex) {
            LOG.error("Error inserting entity {} from jdbc", entity, ex);
            return false;
        }
    }

    private String getValues(Object model, String entity) {
        switch (entity) {
            case Constants.BUCKET:
                return getValuesBucket((Bucket) model);
            case Constants.CATEGORY:
                return getValuesCategory((Category) model);
            case Constants.COMPUTER:
                return getValuesComputer((Computer) model);
            case Constants.FRIDGE:
                return getValuesFridge((Fridge) model);
            case Constants.RECEIPT:
                return getValuesReceipt((Receipt) model);
            case Constants.SODA:
                return getValuesSoda((Soda) model);
        }
        throw new RuntimeException("Unknown entity: " + entity);
    }


    public String getValuesBucket(Bucket model) {
        String id = model.getId();
        List<Product> products = model.getProducts();
        String productsStr = ProductTransformer.productListToString(products);
        return String.format("'%s', '%s'", id, productsStr);
    }

    private String getValuesCategory(Category model) {
        return String.format("'%s'", model.getName());
    }

    private String getValuesComputer(Computer model) {
        String baseString =
                "%d, '%s', '%s', " +
                        "'%s', '%s', '%s', " +
                        "%d, '%s', %d, " +
                        "%b, %b";
        String result = String.format(
                baseString,
                model.getId(),
                model.getName(),
                model.getWeight(),
                model.getPrice(),
                Constants.COMPUTER,
                model.getProcessorName(),
                model.getProcessorPower(),
                model.getGraphicsName(),
                model.getGraphicsVolume(),
                model.isIntegratedWifi(),
                model.isIntegratedBluetooth()
        );
        return result;
    }

    private String getValuesFridge(Fridge model) {
        String baseString =
                "%d, '%s', '%s', '%s', '%s'," +
                        "%d, '%s', %d, %b";

        String result = String.format(
                baseString,
                model.getId(),
                model.getName(),
                model.getWeight(),
                model.getPrice(),
                Constants.FRIDGE,
                model.getVolume(),
                model.getColor(),
                model.getPower(),
                model.isNoFrost()
        );
        return result;
    }

    private String getValuesReceipt(Receipt model) {
        List<Product> products = model.getProducts();
        String productsStr = ProductTransformer.productListToString(products);
        return String.format("%d, '%s', '%s'", model.getId(), productsStr, model.getTotalPrice());
    }

    private String getValuesSoda(Soda model) {
        String baseString = "%d, '%s', '%s', '%s', '%s'," +
                "'%s', %b";

        String result = String.format(
                baseString,
                model.getId(),
                model.getName(),
                model.getWeight(),
                model.getPrice(),
                Constants.COMPUTER,
                model.getFlavour(),
                model.isSparkled()
        );
        return result;
    }


    /**
     * Метод вставки списка записей
     */
    @Override
    public <T> boolean setItemsList(List<T> items, String entity) throws Exception {
        if (items == null || items.contains(null)) {
            LOG.info("Attempt to insert list with null");
            LOG.debug("Attempt to insert list with null");
            return false;
        }
        deleteAll(entity);
        for (int i = 0; i < items.size(); i++) {
            Object model = items.get(i);
            insert(model, entity);
        }
        return true;
    }

    private void deleteAll(String entity) {
        runOn(statement -> statement.execute(String.format(DataProviderJdbc.QUERY_DELETE_ALL, entity)));
    }
}
