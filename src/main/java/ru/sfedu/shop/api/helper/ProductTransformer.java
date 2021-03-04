package ru.sfedu.shop.api.helper;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.shop.Constants;
import ru.sfedu.shop.api.DataProvider;
import ru.sfedu.shop.api.DataProviderCsv;
import ru.sfedu.shop.beans.Product;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.sfedu.shop.Constants.PRODUCTS_SEPATOR;
import static ru.sfedu.shop.Constants.PRODUCT_CATEGORY_SEPARATOR;

public class ProductTransformer extends AbstractBeanField {

    private static final String SEPARATOR_VALUES = "::";
    private final DataProviderCsv dbProvider = new DataProviderCsv();


    @Override
    protected Object convert(String productsStr) {
        List<Product> productList = productStringToList(productsStr, dbProvider);
        return productList;
    }

    public static List<Product> productStringToList(String productsStr, DataProvider dbProvider) {
        return Arrays.stream(productsStr.split(PRODUCTS_SEPATOR))
                .map(it -> {
                    String[] split = it.split(PRODUCT_CATEGORY_SEPARATOR);
                    long prodId = Long.parseLong(split[0]);
                    String prodCategory = split[1];

                    Product product = dbProvider.getProductByIdAndCategory(prodId, prodCategory).get();
                    return product;
                })
                .collect(Collectors.toList());
    }

    @Override
    protected String convertToWrite(Object value) {
        String productsStr = productListToString((List<Product>) value);
        return productsStr;
    }

    public static String productListToString(List<Product> value) {
        List<Product> products = value;

        return products.stream()
                .map(product -> product.getId() + Constants.PRODUCT_CATEGORY_SEPARATOR + product.getCategory().getName())
                .collect(Collectors.joining(PRODUCTS_SEPATOR));
    }
}
