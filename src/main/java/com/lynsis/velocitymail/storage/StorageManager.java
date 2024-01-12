package com.lynsis.velocitymail.storage;


import com.lynsis.velocitymail.VelocityMail;
import com.lynsis.velocitymail.config.ConfigManager;
import com.lynsis.velocitymail.message.MessageManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageManager {
    private final Storage storage;
    private final VelocityMail velocityMail;
    private HashMap<String, String> players = new HashMap<String, String>();


    public StorageManager(VelocityMail velocityMail) {
        this.velocityMail = velocityMail;
        String method = ConfigManager.config.getNode("storage", "method").getString();
        if (method.equalsIgnoreCase("mysql")) {
            this.storage = new StorageMysql(velocityMail);
        } else {
            this.storage = new StorageYaml(velocityMail);
        }
    }

    public Storage getStorage() {
        if (this.storage.getStatus()) {
            return this.storage;
        } else {
            this.storage.connect();
            if (this.storage.getStatus()) {
                return this.storage;
            }
        }
        return null;
    }

    public HashMap<String, String> getPlayers() {
        return players;

    }

    public String getPlayerUuid(String playername) {
        return players.get(playername);
    }

    public String getPlayerByUuid(String uuid) {
        return players.entrySet().stream().filter(p -> uuid.equals(p.getValue())).map(Map.Entry::getKey).findAny().orElse(uuid);
    }

    public void addPlayer(Player player) {
        if (players.get(player.getUsername()) == null) {
            players.put(player.getUsername().toLowerCase(), player.getUniqueId().toString());
            this.getStorage().savePlayer(player);
        }
    }

    public void saveMessage(Message message) {
        this.getStorage().saveMessage(message);
        Player p = this.velocityMail.getProxy().getAllPlayers().stream().filter(player -> message.receiverUuid.equals(player.getUniqueId().toString())).findAny().orElse(null);
        if (p != null) {
            MessageManager.mailInfo(p);
        }
    }


    public void findMessages(Player player) {
        int countUnseen = this.getStorage().getCountUnseen(player.getUniqueId().toString());
        if (countUnseen > 0) {
            MessageManager.mailInfo(player);
        }
    }

    public void loadPlayersOnStart() {
        this.players = this.getStorage().loadPlayers();
    }

    public void getAll(CommandSource source) {
    }

    public void getView(CommandSource source) {
        String playerUuid = "";

        if (source instanceof ConsoleCommandSource) {
            playerUuid = "console";
        } else {
            playerUuid = ((Player) source).getUniqueId().toString();
        }

        ArrayList<Message> messages = this.getStorage().getView(playerUuid);
        if (!messages.isEmpty()) {
            MessageManager.mailView(source);
            messages.forEach(m -> {
                MessageManager.mail(source, m, this);
            });
            this.getStorage().setView(playerUuid);
        } else {
            MessageManager.mailEmpty(source);
        }
    }

    public void clear(CommandSource source) {
    }

}


