package ru.sfedu.shop.api.helper;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.shop.api.DataProviderCsv;
import ru.sfedu.shop.beans.Category;

public class CategoryTransformer extends AbstractBeanField {
    private final DataProviderCsv dbProvider = new DataProviderCsv();

    @Override
    protected Object convert(String s) {
        return dbProvider.getCategory(s).get();
    }

    @Override
    protected String convertToWrite(Object value) {
        return ((Category) value).getName();
    }
}
