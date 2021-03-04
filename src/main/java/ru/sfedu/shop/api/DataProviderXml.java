package ru.sfedu.shop.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.shop.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataProviderXml implements DataProvider {


    public final static Logger LOG = LogManager.getLogger(DataProviderCsv.class);

    /**
     * Вставка списка
     *
     * @param items - список элементов
     * @return
     */
    @Override
    public <T> boolean setItemsList(List<T> items, String entity) throws Exception {
        File dbFile = getDbFileForEntity(entity);
        if (items == null || items.contains(null) || items.isEmpty()) {
            dbFile.delete();
            return false;
        }

        File dir = dbFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Serializer serializer = new Persister();
        EntityList entityList = new EntityList(items);
        serializer.write(entityList, dbFile);
        return true;
    }

    private File getDbFileForEntity(String entity) {
        switch (entity) {
            case Constants.BUCKET:
                return Constants.BUCKET_FILE_XML;
            case Constants.CATEGORY:
                return Constants.CATEGORY_FILE_XML;
            case Constants.COMPUTER:
                return Constants.COMPUTER_FILE_XML;
            case Constants.FRIDGE:
                return Constants.FRIDGE_FILE_XML;
            case Constants.RECEIPT:
                return Constants.RECEIPT_FILE_XML;
            case Constants.SODA:
                return Constants.SODA_FILE_XML;
        }
        throw new RuntimeException("Unknown entity: " + entity);
    }

    @Override
    public List getAll(String entity) {
        File dbFile = getDbFileForEntity(entity);
        if (!dbFile.exists()) return new ArrayList<>();
        Serializer serializer = new Persister();
        if (dbFile.length() == 0) return new ArrayList<>();
        try {
            EntityList entityList = serializer.read(EntityList.class, dbFile);
            return entityList.getData();
        } catch (Exception e) {
            LOG.error("CSBucket getAll error", e);
            return new ArrayList<>();
        }
    }
}

@Root
class EntityList<T> {

    public EntityList() {}

    @ElementList(entry = "data", inline = true)
    private List<T> data;

    public EntityList(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
