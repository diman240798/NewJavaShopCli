package ru.sfedu.shop.api.helper;

import ru.sfedu.shop.beans.Category;
import ru.sfedu.shop.beans.Computer;
import ru.sfedu.shop.beans.Fridge;
import ru.sfedu.shop.beans.Soda;

import java.util.Arrays;
import java.util.List;

import ru.sfedu.shop.Constants;

public class InitializerData {

    public final static List<Category> CATEGORIES = Arrays.asList(
            new Category(0, Constants.CATEGORY_SODA),
            new Category(1, Constants.CATEGORY_COMPUTER),
            new Category(2, Constants.CATEGORY_FRIDGE)
    );

    public final static List<Fridge> FRIDGES = Arrays.asList(
            new Fridge(0, "Indesit c30", 30, 31000, Constants.CATEGORY_FRIDGE, 50, "white", 300),
            new Fridge(1, "Toshiba a52", 50, 60650, Constants.CATEGORY_FRIDGE, 100, "gray", 600, true),
            new Fridge(2, "Samsung wqe123", 30, 45300, Constants.CATEGORY_FRIDGE, 60, "pink", 500, true),
            new Fridge(3, "Beko tr44", 30, 42300, Constants.CATEGORY_FRIDGE, 60, "black", 450),
            new Fridge(4, "Atlant xm435", 30, 50300, Constants.CATEGORY_FRIDGE, 70, "yellow", 600)
    );

    public final static List<Computer> COMPUTERS = Arrays.asList(
            new Computer(0, "Dell compact RT 3", 2.2, 52500, Constants.CATEGORY_COMPUTER, "intel i3", 500, "integrated", 100),
            new Computer(1, "Dell gaming MT 5", 2.9, 73400, Constants.CATEGORY_COMPUTER, "amd ryzen x9", 1000, "geforge gtx 2050ti", 100, true, true),
            new Computer(2, "HP spectre x360", 1.3, 85700, Constants.CATEGORY_COMPUTER, "intel i5", 700, "geforge gtx 1060", 100),
            new Computer(3, "HP pavillion x3", 2.7, 92000, Constants.CATEGORY_COMPUTER, "intel i7", 900, "geforge gtx 1050", 100, true, true),
            new Computer(4, "Dexp laptop sereies", 2.5, 31000, Constants.CATEGORY_COMPUTER, "amd ryzen x1", 300, "integrated", 100)
    );

    public final static List<Soda> SODA = Arrays.asList(
            new Soda(0, "Fanta", 2, 200, Constants.CATEGORY_SODA, "orange", true),
            new Soda(1, "Kind Juice", 1, 70, Constants.CATEGORY_SODA, "apple"),
            new Soda(2, "Rich Juice", 1, 110, Constants.CATEGORY_SODA, "strawberry"),
            new Soda(3, "Limonella", 2.2, 52500, Constants.CATEGORY_SODA, "lemon", true),
            new Soda(4, "Mirinda Blue", 2.2, 52500, Constants.CATEGORY_SODA, "blueberry-lemon", true)
    );
}
