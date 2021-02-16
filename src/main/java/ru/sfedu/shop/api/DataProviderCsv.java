package ru.sfedu.shop.api;

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
    public boolean insertList(List items, String entity) throws Exception {
        File dbFile = getDbFileForEntity(entity);
        if (items == null || items.contains(null)) {
            dbFile.delete();
            return false;
        }

        File dir = dbFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }


        try (Writer writer = new FileWriter(dbFile)) {
            StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer).build();
            sbc.write(items);
        }
        return true;
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
            case Constants.SESSION:
                return Constants.SESSION_FILE_CSV;
            case Constants.SODA:
                return Constants.SODA_FILE_CSV;
        }
        throw new RuntimeException("Unknown entity: " + entity);
    }

    @Override
    public List getAll(String entity) {
        if (entity.equals(Constants.BUCKET)) {
            return getAll(Constants.BUCKET_FILE_CSV, Bucket.class);
        } else if (entity.equals(Constants.CATEGORY)) {
            return getAll(Constants.CATEGORY_FILE_CSV, Category.class);
        } else if (entity.equals(Constants.COMPUTER)) {
            return getAll(Constants.COMPUTER_FILE_CSV, Computer.class);
        } else if (entity.equals(Constants.FRIDGE)) {
            return getAll(Constants.FRIDGE_FILE_CSV, Fridge.class);
        } else if (entity.equals(Constants.RECEIPT)) {
            return getAll(Constants.RECEIPT_FILE_CSV, Receipt.class);
        } else if (entity.equals(Constants.SESSION)) {
            return getAll(Constants.SESSION_FILE_CSV, Session.class);
        } else if (entity.equals(Constants.SODA)) {
            return getAll(Constants.SODA_FILE_CSV, Soda.class);
        }
        throw new RuntimeException("Incorrect csv dbFile");
    }

    private List getAll(File dbFile, Class clazz) {
        if (!dbFile.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(dbFile)) {
            HeaderColumnNameMappingStrategy ms = new HeaderColumnNameMappingStrategy();
            ms.setType(clazz);

            CsvToBean cb = new CsvToBeanBuilder(reader)
                    .withMappingStrategy(ms)
                    .build();

            List result = cb.parse();
            return result;
        } catch (Exception e) {
            LOG.error("CSV getALL error", e);
            return new ArrayList<>();
        }
    }
}