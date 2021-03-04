package ru.sfedu.shop.api.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.shop.beans.Computer;
import ru.sfedu.shop.beans.Fridge;
import ru.sfedu.shop.beans.Soda;

import java.util.Optional;

public class InsertManager {
    public final static Logger LOG = LogManager.getLogger(InsertManager.class);


    /**
     * Реализация выдачи распарсенной модели
     * @param modelStr - данные модели в виде строки
     * @return Computer
     */
    public static Optional<Computer> getComputerFromString(String modelStr) {
        if (modelStr == null || modelStr.isEmpty()) {
            LOG.error("Incorrect computer model: {}", modelStr);
            return Optional.empty();
        }
        LOG.info("Parsing string to model");
        LOG.debug("Parsing string to model {}", modelStr);
        String[] split = modelStr.split("\\s");
        long id = Long.parseLong(split[0]);
        String name = split[1];
        double weight = Double.parseDouble(split[2]);
        double price = Double.parseDouble(split[3]);
        String processorName = split[4];
        int processorPower = Integer.parseInt(split[5]);
        String graphicsName = split[6];
        int graphicsVolume = Integer.parseInt(split[7]);

        if (split.length > 8) {
            boolean wifiIntegrated = Boolean.parseBoolean(split[8]);
            boolean bluetoothIntegrated = Boolean.parseBoolean(split[9]);
            return Optional.of(new Computer(id, name, weight, price, processorName, processorPower, graphicsName, graphicsVolume, wifiIntegrated, bluetoothIntegrated));
        }

        return Optional.of(new Computer(id, name, weight, price, processorName, processorPower, graphicsName, graphicsVolume));
    }

    /**
     * Реализация выдачи распарсенной модели
     *
     * @param modelStr - данные модели в виде строки
     * @return Soda
     */
    public static Optional<Soda> getSodaFromString(String modelStr) {
        if (modelStr == null || modelStr.isEmpty()) {
            LOG.error("Incorrect soda model: {}", modelStr);
            return Optional.empty();
        }
        LOG.info("Parsing string to model");
        LOG.debug("Parsing string to model {}", modelStr);
        String[] split = modelStr.split("\\s");
        long id = Long.parseLong(split[0]);
        String name = split[1];
        double weight = Double.parseDouble(split[2]);
        double price = Double.parseDouble(split[3]);
        String flavour = split[4];
        if (split.length > 5) {
            boolean sparkled = Boolean.parseBoolean(split[5]);
            return Optional.of(new Soda(id, name, weight, price, flavour, sparkled));
        } else {
            return Optional.of(new Soda(id, name, weight, price, flavour));
        }
    }

    /**
     * Реализация выдачи распарсенной модели
     *
     * @param modelStr - данные модели в виде строки
     * @return Fridge
     */
    public static Optional<Fridge> getFridgeFromString(String modelStr) {
        if (modelStr == null || modelStr.isEmpty()) {
            LOG.error("Incorrect fridge model: {}", modelStr);
            return Optional.empty();
        }
        LOG.info("Parsing string to model");
        LOG.debug("Parsing string to model {}", modelStr);
        String[] split = modelStr.split("\\s");
        long id = Long.parseLong(split[0]);
        String name = split[1];
        double weight = Double.parseDouble(split[2]);
        double price = Double.parseDouble(split[3]);
        int volume = Integer.parseInt(split[4]);
        String color = split[5];
        int power = Integer.parseInt(split[6]);

        if (split.length > 7) {
            boolean noFrost = Boolean.parseBoolean(split[7]);
            return Optional.of(new Fridge(id, name, weight, price, volume, color, power, noFrost));
        }

        return Optional.of(new Fridge(id, name, weight, price, volume, color, power));
    }
}
