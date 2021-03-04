package ru.sfedu.shop.api;

public class DataProviderTestXml extends DataProviderTest {
    @Override
    protected void setProvider() {
        super.dbProvider = new DataProviderXml();
    }
}
