package ru.sfedu.shop.api;

public class DataProviderTestJdbc extends DataProviderTest {
    @Override
    protected void setProvider() {
        super.dbProvider = new DataProviderJdbc();
    }
}
