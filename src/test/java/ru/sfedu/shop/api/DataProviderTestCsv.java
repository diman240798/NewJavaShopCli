package ru.sfedu.shop.api;

public class DataProviderTestCsv extends DataProviderTest {

    @Override
    protected void setProvider() {
        super.dbProvider = new DataProviderCsv();
    }
}
