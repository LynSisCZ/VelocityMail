package com.lynsis.velocitymail.storage;

import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Storage {
    public abstract void getAll(String receiverUuid);

    public abstract ArrayList<Message> getView(String receiverUuid);

    public abstract void clear(String receiverUuid);

    public abstract void saveMessage(Message message);

    public abstract void savePlayer(Player player);

    public abstract HashMap<String, String> loadPlayers();

    public abstract int getCountUnseen(String uuid);

    public abstract boolean getStatus();

    public abstract void connect();

    public abstract void setView(String playerUuid);
}
