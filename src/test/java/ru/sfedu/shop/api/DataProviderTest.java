package ru.sfedu.shop.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sfedu.shop.Constants;
import ru.sfedu.shop.api.helper.InitializerData;
import ru.sfedu.shop.beans.*;

import java.io.File;
import java.util.*;

public abstract class DataProviderTest {

    protected DataProvider dbProvider;

    protected abstract void setProvider();

    @Before
    public void beforeEach() {
        Constants.DB_FILES.forEach(File::delete);
        setProvider();
    }

    @Test
    public void init() throws Exception {
        dbProvider.init();

        Assert.assertTrue(Objects.deepEquals(dbProvider.getAll(Constants.CATEGORY), InitializerData.CATEGORIES));
        Assert.assertTrue(Objects.deepEquals(dbProvider.getAll(Constants.COMPUTER), InitializerData.COMPUTERS));
        Assert.assertTrue(Objects.deepEquals(dbProvider.getAll(Constants.SODA), InitializerData.SODA));
        Assert.assertTrue(Objects.deepEquals(dbProvider.getAll(Constants.FRIDGE), InitializerData.FRIDGES));

    }

    @Test
    public void initBad() throws Exception {
        dbProvider.init();

        Assert.assertFalse(Objects.deepEquals(dbProvider.getAll(Constants.CATEGORY), Collections.emptyList()));
        Assert.assertFalse(Objects.deepEquals(dbProvider.getAll(Constants.COMPUTER), Arrays.asList(null, null, null, null)));
        Assert.assertFalse(Objects.deepEquals(dbProvider.getAll(Constants.SODA), Collections.emptyList()));
        Assert.assertFalse(Objects.deepEquals(dbProvider.getAll(Constants.FRIDGE), Collections.singletonList(null)));

    }

    @Test
    public void getCategory() throws Exception {
        Category model = InitializerData.CATEGORIES.get(0);
        dbProvider.setItemsList(Collections.singletonList(model), Constants.CATEGORY);
        Object modelDb = dbProvider.getCategory(model.getName()).get();
        Assert.assertEquals(model, modelDb);
    }

    @Test
    public void getCategoryBad() throws Exception {
        Optional<Category> model = dbProvider.getCategory("CATS");
        Assert.assertFalse(model.isPresent());
    }

    @Test
    public void getComputer() throws Exception {
        dbProvider.init();
        Computer model = InitializerData.COMPUTERS.get(0);
        dbProvider.setItemsList(Collections.singletonList(model), Constants.COMPUTER);
        Object modelDb = dbProvider.getComputer(model.getId()).get();
        Assert.assertEquals(model, modelDb);
    }

    @Test
    public void getComputerBad() throws Exception {
        Optional<Computer> model = dbProvider.getComputer(0);
        Assert.assertFalse(model.isPresent());
    }

    @Test
    public void getFridge() throws Exception {
        dbProvider.init();
        Fridge model = InitializerData.FRIDGES.get(0);
        dbProvider.setItemsList(Collections.singletonList(model), Constants.FRIDGE);
        Object modelDb = dbProvider.getFridge(model.getId()).get();
        Assert.assertEquals(model, modelDb);
    }

    @Test
    public void getFridgeBad() throws Exception {
        Optional<Fridge> model = dbProvider.getFridge(0);
        Assert.assertFalse(model.isPresent());
    }

    @Test
    public void getReceipt() throws Exception {
        dbProvider.init();
        Receipt model = new Receipt(21321423, new ArrayList<>(InitializerData.FRIDGES), 4344);
        dbProvider.setItemsList(Collections.singletonList(model), Constants.RECEIPT);
        Object modelDb = dbProvider.getReceipt(model.getId()).get();
        Assert.assertEquals(model, modelDb);
    }

    @Test
    public void getReceiptBad() throws Exception {
        Optional<Receipt> model = dbProvider.getReceipt(0);
        Assert.assertFalse(model.isPresent());
    }

    @Test
    public void getSoda() throws Exception {
        dbProvider.init();
        Soda model = InitializerData.SODA.get(0);
        dbProvider.setItemsList(Collections.singletonList(model), Constants.SODA);
        Object modelDb = dbProvider.getSoda(model.getId()).get();
        Assert.assertEquals(model, modelDb);
    }

    @Test
    public void getSodaBad() throws Exception {
        Optional<Soda> model = dbProvider.getSoda(0);
        Assert.assertFalse(model.isPresent());
    }

    @Test
    public void insertComputer() throws Exception {
        dbProvider.init();
        dbProvider.insertComputer("10 Hp_pavillion 2.4 64300 intel_i5 600 geforge_gtx_1060 2 true true");
        Computer modelDb = dbProvider.getComputer(10).get();
        Assert.assertEquals("Hp_pavillion", modelDb.getName());
    }

    @Test
    public void insertComputerBad() throws Exception {
        Assert.assertFalse(dbProvider.insertComputer(""));
    }

    @Test
    public void insertSoda() throws Exception {
        dbProvider.init();
        dbProvider.insertSoda("10 tasty_fanta 1.5 300 orange");
        Soda modelDb = dbProvider.getSoda(10).get();
        Assert.assertEquals("tasty_fanta", modelDb.getName());
    }

    @Test
    public void insertSodaBad() throws Exception {
        Assert.assertFalse(dbProvider.insertSoda(""));
    }

    @Test
    public void insertFridge() throws Exception {
        dbProvider.init();
        dbProvider.insertFridge("10 Toshiba_x_32 55.4 64300 80 white 900 true");
        Fridge modelDb = dbProvider.getFridge(10).get();
        Assert.assertEquals("Toshiba_x_32", modelDb.getName());
    }

    @Test
    public void insertFridgeBad() throws Exception {
        Assert.assertFalse(dbProvider.insertFridge(""));
    }

    @Test
    public void closeBucket() throws Exception {
        dbProvider.init();
        Fridge product = InitializerData.FRIDGES.get(0);
        dbProvider.addProduct(product.getId(), Constants.FRIDGE, Optional.empty());
        Bucket bucket = (Bucket) dbProvider.getAll(Constants.BUCKET).get(0);
        boolean finishSession = dbProvider.closeBucket(bucket.getId());
        Assert.assertTrue(finishSession);
    }

    @Test
    public void closeBucketBad() throws Exception {
        boolean finishSession = dbProvider.closeBucket(UUID.randomUUID().toString());
        Assert.assertFalse(finishSession);
    }

    @Test
    public void addProduct() throws Exception {
        dbProvider.init();
        Fridge fridge = InitializerData.FRIDGES.get(0);
        boolean addProduct = dbProvider.addProduct(fridge.getId(), Constants.FRIDGE, Optional.empty());
        Assert.assertTrue(addProduct);
    }

    @Test
    public void addProductBad() throws Exception {
        boolean addProduct = dbProvider.addProduct(1, Constants.FRIDGE, Optional.empty());
        Assert.assertFalse(addProduct);
    }

    @Test
    public void deleteBucket() throws Exception {
        dbProvider.init();
        Bucket model = new Bucket(UUID.randomUUID().toString(), new ArrayList<>(InitializerData.FRIDGES));
        String entity = Constants.BUCKET;
        dbProvider.setItemsList(Collections.singletonList(model), entity);
        Object modelDb = dbProvider.getAll(entity).get(0);
        Assert.assertEquals(model, modelDb);
    }

    @Test
    public void deleteBucketBad() throws Exception {
        Bucket bucket = new Bucket(UUID.randomUUID().toString());
        Assert.assertFalse(dbProvider.deleteBucket(bucket));
    }
}