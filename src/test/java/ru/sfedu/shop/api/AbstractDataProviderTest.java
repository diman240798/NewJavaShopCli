package ru.sfedu.shop.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sfedu.shop.Constants;
import ru.sfedu.shop.api.helper.ExConsumer;
import ru.sfedu.shop.api.helper.ExFunction;
import ru.sfedu.shop.api.helper.InitializerData;
import ru.sfedu.shop.beans.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.*;

public class AbstractDataProviderTest {

    List<AbstractDataProvider> dbProviders;

    @Before
    public void beforeEach() {
        Constants.XML_FILES.forEach(File::delete);
        Constants.CSV_FILES.forEach(File::delete);
        Constants.JDBC_MV.delete();
        Constants.JDBC_TRACE.delete();

        dbProviders = Arrays.asList(
                new DataProviderCsv(),
                new DataProviderXml(),
                new DataProviderJdbc()
        );
    }

    public void runForAll(ExConsumer<AbstractDataProvider> action) throws Exception {
        for (int i = 0; i < dbProviders.size(); i++) {
            AbstractDataProvider provider = dbProviders.get(i);
            action.accept(provider);
        }
    }

    @Test
    public void init() throws Exception {
        runForAll(dbProvider -> {
             dbProvider.init();

            Assert.assertTrue(Objects.deepEquals(dbProvider.getAll(Constants.CATEGORY), InitializerData.CATEGORIES));
            Assert.assertTrue(Objects.deepEquals(dbProvider.getAll(Constants.COMPUTER), InitializerData.COMPUTERS));
            Assert.assertTrue(Objects.deepEquals(dbProvider.getAll(Constants.SODA), InitializerData.SODA));
            Assert.assertTrue(Objects.deepEquals(dbProvider.getAll(Constants.FRIDGE), InitializerData.FRIDGES));

        });
    }

    @Test
    public void initBad() throws Exception {
        runForAll(dbProvider -> {
            dbProvider.init();

            Assert.assertFalse(Objects.deepEquals(dbProvider.getAll(Constants.CATEGORY), Collections.emptyList()));
            Assert.assertFalse(Objects.deepEquals(dbProvider.getAll(Constants.COMPUTER), Arrays.asList(null, null, null, null)));
            Assert.assertFalse(Objects.deepEquals(dbProvider.getAll(Constants.SODA), Collections.emptyList()));
            Assert.assertFalse(Objects.deepEquals(dbProvider.getAll(Constants.FRIDGE), Collections.singletonList(null)));

        });
    }

    @Test
    public void getCategory() throws Exception {
        runForAll(dbProvider -> {
            Category model = InitializerData.CATEGORIES.get(0);
            dbProvider.insertList(Collections.singletonList(model), Constants.CATEGORY);
            Object modelDb = dbProvider.getCategory(model.getId()).get();
            Assert.assertEquals(model, modelDb);
        });
    }

    @Test
    public void getCategoryBad() throws Exception {
        runForAll(dbProvider -> {
            Optional<Category> model = dbProvider.getCategory(0);
            Assert.assertFalse(model.isPresent());
        });
    }

    @Test
    public void getFridge() throws Exception {
        runForAll(dbProvider -> {
            Fridge model = InitializerData.FRIDGES.get(0);
            dbProvider.insertList(Collections.singletonList(model), Constants.FRIDGE);
            Object modelDb = dbProvider.getFridge(model.getId()).get();
            Assert.assertEquals(model, modelDb);
        });
    }

    @Test
    public void getFridgeBad() throws Exception {
        runForAll(dbProvider -> {
            Optional<Fridge> model = dbProvider.getFridge(0);
            Assert.assertFalse(model.isPresent());
        });
    }

    @Test
    public void getReceipt() throws Exception {
        runForAll(dbProvider -> {
            Receipt model = new Receipt(21321423, "1 fridge", 4344);
            dbProvider.insertList(Collections.singletonList(model), Constants.RECEIPT);
            Object modelDb = dbProvider.getReceipt(model.getId()).get();
            Assert.assertEquals(model, modelDb);
        });
    }

    @Test
    public void getReceiptBad() throws Exception {
        runForAll(dbProvider -> {
            Optional<Receipt> model = dbProvider.getReceipt(0);
            Assert.assertFalse(model.isPresent());
        });
    }

    @Test
    public void getSoda() throws Exception {
        runForAll(dbProvider -> {
            Soda model = InitializerData.SODA.get(0);
            dbProvider.insertList(Collections.singletonList(model), Constants.SODA);
            Object modelDb = dbProvider.getSoda(model.getId()).get();
            Assert.assertEquals(model, modelDb);
        });
    }

    @Test
    public void getSodaBad() throws Exception {
        runForAll(dbProvider -> {
            Optional<Soda> model = dbProvider.getSoda(0);
            Assert.assertFalse(model.isPresent());
        });
    }

    @Test
    public void insertComputer() throws Exception {
        runForAll(dbProvider -> {
            Computer model = InitializerData.COMPUTERS.get(0);
                dbProvider.insertList(Collections.singletonList(model), Constants.COMPUTER);
            Object modelDb = dbProvider.getComputer(model.getId()).get();
            Assert.assertEquals(model, modelDb);
        });
    }

    @Test
    public void insertComputerBad() throws Exception {
        runForAll(dbProvider -> {
            List<Fridge> items = new ArrayList<Fridge>() { { add(null); }};
            String category = Constants.COMPUTER;
            dbProvider.insertList(items, category);
            Object itemsDb = dbProvider.getAll(category);
            Assert.assertNotEquals(items, itemsDb);
        });
    }

    @Test
    public void insertFridge() throws Exception {
        runForAll(dbProvider -> {
            List<Fridge> items = InitializerData.FRIDGES;
            String category = Constants.FRIDGE;
            dbProvider.insertList(items, category);
            Object itemsDb = dbProvider.getAll(category);
            Assert.assertEquals(items, itemsDb);
        });
    }

    @Test
    public void insertFridgeBad() throws Exception {
        runForAll(dbProvider -> {
            List<Fridge> items = new ArrayList<Fridge>() { { add(null); }};
            String category = Constants.FRIDGE;
            dbProvider.insertList(items, category);
            Object itemsDb = dbProvider.getAll(category);
            Assert.assertNotEquals(items, itemsDb);
        });
    }

    @Test
    public void insertSoda() throws Exception {
        runForAll(dbProvider -> {
            List<Soda> items = InitializerData.SODA;
            String category = Constants.SODA;
            dbProvider.insertList(items, category);
            Object itemsDb = dbProvider.getAll(category);
            Assert.assertEquals(items, itemsDb);
        });
    }

    @Test
    public void insertSodaBad() throws Exception {
        runForAll(dbProvider -> {
            List<Fridge> items = new ArrayList<Fridge>() { { add(null); }};
            String category = Constants.SODA;
            dbProvider.insertList(items, category);
            Object itemsDb = dbProvider.getAll(category);
            Assert.assertNotEquals(items, itemsDb);
        });
    }

    @Test
    public void finishSession() throws Exception {
        runForAll(dbProvider -> {
            Session session = dbProvider.startSession();
            boolean finishSession = dbProvider.finishSession(session.getSession());
            Assert.assertTrue(finishSession);
        });
    }

    @Test
    public void finishSessionBad() throws Exception {
        runForAll(dbProvider -> {
            boolean finishSession = dbProvider.finishSession(UUID.randomUUID().toString());
            Assert.assertFalse(finishSession);
        });
    }

    @Test
    public void startSession() throws Exception {
        runForAll(dbProvider -> {
            Session session = dbProvider.startSession();
            Assert.assertFalse(session.getSession().isEmpty());
            Object sessionDb = dbProvider.getAll(Constants.SESSION).get(0);
            Assert.assertEquals(session, sessionDb);
        });
    }

    @Test
    public void addProduct() throws Exception {
        runForAll(dbProvider -> {
            dbProvider.insertList(InitializerData.CATEGORIES, Constants.CATEGORY);
            Session session = dbProvider.startSession();
            Fridge fridge = InitializerData.FRIDGES.get(0);
            String entity = Constants.FRIDGE;
            dbProvider.insertList(Collections.singletonList(fridge), entity);
            boolean addProduct = dbProvider.addProduct(session.getSession(), fridge.getId(), entity);
            Assert.assertTrue(addProduct);
        });
    }

    @Test
    public void addProductBad() throws Exception {
        runForAll(dbProvider -> {
            dbProvider.insertList(InitializerData.CATEGORIES, Constants.CATEGORY);
            Session session = dbProvider.startSession();
            boolean addProduct = dbProvider.addProduct(session.getSession(), 1, Constants.FRIDGE);
            Assert.assertFalse(addProduct);
        });
    }

    @Test
    public void deleteBucket() throws Exception {
        runForAll(dbProvider -> {
            Bucket model = new Bucket(321321, UUID.randomUUID().toString());
            String entity = Constants.BUCKET;
            dbProvider.insertList(Collections.singletonList(model), entity);
            Object modelDb = dbProvider.getAll(entity).get(0);
            Assert.assertEquals(model, modelDb);
        });
    }

    @Test
    public void deleteBucketBad() throws Exception {
        runForAll(dbProvider -> {
            String entity = Constants.BUCKET;
            dbProvider.insertList(Collections.singletonList(null), entity);
            List items = dbProvider.getAll(entity);
            Assert.assertTrue(items.isEmpty());
        });
    }

    @Test
    public void deleteSession() throws Exception {
        runForAll(dbProvider -> {
            Session model = new Session(321321, UUID.randomUUID().toString());
            String entity = Constants.SESSION;
            dbProvider.insertList(Collections.singletonList(model), entity);
            Object modelDb = dbProvider.getAll(entity).get(0);
            Assert.assertEquals(model, modelDb);
        });
    }

    @Test
    public void deleteSessionBad() throws Exception {
        runForAll(dbProvider -> {
            String entity = Constants.SESSION;
            dbProvider.insertList(Collections.singletonList(null), entity);
            List items = dbProvider.getAll(entity);
            Assert.assertTrue(items.isEmpty());
        });
    }
}