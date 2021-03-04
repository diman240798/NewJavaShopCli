package ru.sfedu.shop.api;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.shop.Constants;
import ru.sfedu.shop.beans.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataProviderCsv implements DataProvider {

    public final static Logger LOG = LogManager.getLogger(DataProviderCsv.class);

    /**
     * Вставка списка
     *
     * @param items - список элементов
     * @return
     */
    public <T> boolean setItemsList(List<T> items, String entity) throws Exception {
        File dbFile = getDbFileForEntity(entity);
        Class clazz = getClassForEntity(entity);

        if (items == null || items.contains(null) || items.isEmpty()) {
            dbFile.delete();
            return false;
        }

        File dir = dbFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (Writer writer = new FileWriter(dbFile)) {
            CSVWriter writerCsv = new CSVWriter(writer);
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writerCsv)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(items);
        }
        return true;
    }

    private Class getClassForEntity(String entity) {
        switch (entity) {
            case Constants.BUCKET:
                return Bucket.class;
            case Constants.CATEGORY:
                return Category.class;
            case Constants.COMPUTER:
                return Computer.class;
            case Constants.FRIDGE:
                return Fridge.class;
            case Constants.RECEIPT:
                return Receipt.class;
            case Constants.SODA:
                return Soda.class;
        }
        throw new RuntimeException("Unknown entity: " + entity);
    }

    private File getDbFileForEntity(String entity) {
        switch (entity) {
            case Constants.BUCKET:
                return Constants.BUCKET_FILE_CSV;
            case Constants.CATEGORY:
                return Constants.CATEGORY_FILE_CSV;
            case Constants.COMPUTER:
                return Constants.COMPUTER_FILE_CSV;
            case Constants.FRIDGE:
                return Constants.FRIDGE_FILE_CSV;
            case Constants.RECEIPT:
                return Constants.RECEIPT_FILE_CSV;
            case Constants.SODA:
                return Constants.SODA_FILE_CSV;
        }
        throw new RuntimeException("Unknown entity: " + entity);
    }

    @Override
    public <T> List<T> getAll(String entity) {
        File dbFileForEntity = getDbFileForEntity(entity);
        Class classForEntity = getClassForEntity(entity);
        return getAll(dbFileForEntity, classForEntity);
    }

    private List getAll(File dbFile, Class clazz) {
        if (!dbFile.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(dbFile)) {
            CSVReader csvReader = new CSVReader(reader);

            CsvToBean cb = new CsvToBeanBuilder(reader)
                    .withType(clazz)
                    .build();

            List result = cb.parse();
            return result;
        } catch (Exception e) {
            LOG.error("CSV getALL error", e);
            return new ArrayList<>();
        }
    }
}