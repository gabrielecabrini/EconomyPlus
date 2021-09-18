package me.itswagpvp.economyplus.messages;

import me.itswagpvp.economyplus.EconomyPlus;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class DefaultFiles {

    // FileConfiguration for every file
    private FileConfiguration messagesConfigEN;
    private FileConfiguration messagesConfigIT;
    private FileConfiguration messagesConfigRO;
    private FileConfiguration messagesConfigAL;

    // File instance for messages
    private final String path = plugin.getDataFolder() + "/messages";
    private final File messagesITFile = new File(path, "IT.yml");
    private final File messagesENFile = new File(path, "EN.yml");
    private final File messagesROFile = new File(path, "RO.yml");
    private final File messagesALFile = new File(path, "AL.yml");

    /**
     * EN = English
     */

    public void createMessagesEN() {

        if (!messagesENFile.exists()) {
            messagesENFile.getParentFile().mkdirs();
            new MessageUtils().saveResource("EN.yml", false);
        }

        loadMessagesEN();
    }

    private void loadMessagesEN () {
        messagesConfigEN = new YamlConfiguration();
        try {
            messagesConfigEN.load(messagesENFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     *  IT = Italian
     */

    public void createMessagesIT() {

        if (!messagesITFile.exists()) {
            messagesITFile.getParentFile().mkdirs();
            new MessageUtils().saveResource("IT.yml", false);
        }

        loadMessagesIT();
    }

    private void loadMessagesIT() {
        messagesConfigIT = new YamlConfiguration();
        try {
            messagesConfigIT.load(messagesITFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     *  RO = Rumenian
     */

    public void createMessagesRO() {

        if (!messagesROFile.exists()) {
            messagesROFile.getParentFile().mkdirs();
            new MessageUtils().saveResource("RO.yml", false);
        }

        loadMessagesRO();
    }

    private void loadMessagesRO() {
        messagesConfigRO = new YamlConfiguration();
        try {
            messagesConfigRO.load(messagesROFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     *  AL = Albania
     */

    public void createMessagesAL() {

        if (!messagesALFile.exists()) {
            messagesALFile.getParentFile().mkdirs();
            new MessageUtils().saveResource("AL.yml", false);
        }

        loadMessagesAL();
    }

    private void loadMessagesAL() {
        messagesConfigAL = new YamlConfiguration();
        try {
            messagesConfigAL.load(messagesALFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getMessagesFile() {

        if (EconomyPlus.messagesType == MessagesFile.EN) {

            loadMessagesEN();
            return this.messagesConfigEN;
        }

        if (EconomyPlus.messagesType == MessagesFile.IT) {

            loadMessagesIT();
            return this.messagesConfigIT;
        }

        if (EconomyPlus.messagesType == MessagesFile.RO) {

            loadMessagesRO();
            return this.messagesConfigRO;
        }

        if (EconomyPlus.messagesType == MessagesFile.AL) {

            loadMessagesAL();
            return this.messagesConfigAL;
        }

        return null;
    }
}
