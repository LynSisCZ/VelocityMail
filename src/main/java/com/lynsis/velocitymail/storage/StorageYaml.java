package com.lynsis.velocitymail.storage;

import com.lynsis.velocitymail.VelocityMail;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class StorageYaml extends Storage {

    public StorageYaml(VelocityMail velocityMail) {
        super();
    }

    @Override
    public void getAll(String receiverUuid) {

    }

    @Override
    public ArrayList<Message> getView(String receiverUuid) {
        return null;
    }

    @Override
    public void clear(String receiverUuid) {

    }

    @Override
    public void saveMessage(Message message) {

    }

    @Override
    public void savePlayer(Player player) {

    }

    @Override
    public HashMap<String, String> loadPlayers() {
        return null;
    }

    @Override
    public int getCountUnseen(String uuid) {
        return 0;
    }

    @Override
    public boolean getStatus() {
        return true;
    }

    @Override
    public void connect() {

    }

    @Override
    public void setView(String playerUuid) {

    }

    @Override
    public void disconnect() {

    }
}
