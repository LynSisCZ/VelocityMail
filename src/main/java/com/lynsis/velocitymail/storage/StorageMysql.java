package com.lynsis.velocitymail.storage;

import com.lynsis.velocitymail.VelocityMail;
import com.lynsis.velocitymail.config.ConfigManager;
import com.velocitypowered.api.proxy.Player;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class StorageMysql extends Storage {
    private Connection connection;
    private VelocityMail velocityMail;
    private HikariDataSource dataSource;
    
    public StorageMysql(VelocityMail velocityMail){
        this.dataSource = new HikariDataSource();
        this.dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        this.dataSource.setJdbcUrl("jdbc:mariadb://" + ConfigManager.config.getNode("storage", "data", "address").getString() + ":3306/" + ConfigManager.config.getNode("storage", "data", "database").getString());
        this.dataSource.setUsername(ConfigManager.config.getNode("storage", "data", "username").getString());
        this.dataSource.setPassword(ConfigManager.config.getNode("storage", "data", "password").getString());
        this.dataSource.setAutoCommit(true);

        this.connect();
        this.checkDatabase();
    }
    @Override
    public void getAll(String receiverUuid) {

    }

    @Override
    public ArrayList<Message> getView(String receiverUuid) {
        try {
            PreparedStatement query = this.connection.prepareStatement("SELECT * FROM  velocitymail WHERE receiver_uuid = ?");
            query.setString(1, receiverUuid);
            ResultSet rs = query.executeQuery();
            ArrayList<Message> messages = new ArrayList<>();
            while (rs.next()){
                messages.add(new Message(rs.getString("sender_uuid"), rs.getString("receiver_uuid"), rs.getString("message")));
            }
            return messages;
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
        }
        return null;
    }

    @Override
    public void clear(String receiverUuid) {

    }

    public void saveMessage(Message message){
        try {
            PreparedStatement query = this.connection.prepareStatement("INSERT INTO velocitymail (sender_uuid, receiver_uuid, message ) VALUES (?,?,?)");
            query.setString(1, message.senderUuid);
            query.setString(2, message.receiverUuid);
            query.setString(3, message.message);
            query.execute();
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
        }
    }

    public void savePlayer(Player player){
        try {
            PreparedStatement query = this.connection.prepareStatement("INSERT IGNORE INTO velocitymail_players VALUES (?,?)");
            query.setString(1,  player.getUniqueId().toString());
            query.setString(2,  player.getUsername().toLowerCase());

            query.execute();
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
        }
    }
    public HashMap<String, String> loadPlayers(){
        try {
            HashMap<String, String> players = new HashMap<String, String>();

            ResultSet rs = this.connection.prepareStatement("SELECT * FROM  velocitymail_players").executeQuery();
            while(rs.next()){
                players.put(rs.getString("username"), rs.getString("uuid"));
            }
            return players;
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
        }
        return null;
    }

    @Override
    public int getCountUnseen(String uuid) {
        int messagesCount = 0;
        try {
            HashMap<String, String> players = new HashMap<String, String>();
            PreparedStatement query = this.connection.prepareStatement("SELECT count(*) FROM  velocitymail WHERE receiver_uuid = ?");
            query.setString(1, uuid);

            ResultSet rs = query.executeQuery();
            if(rs.next()){
                messagesCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
        }
        return messagesCount;
    }

    @Override
    public boolean getStatus() {
        try {
            return this.connection.isValid(10);
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
        }
        return false;
    }

    @Override
    public void connect() {
        try {
            this.connection = this.dataSource.getConnection();
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
            this.velocityMail.unRegisterCommand();
        }
    }

    public void checkDatabase(){
        try {
            PreparedStatement sqlMail = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS velocitymail (id INT NOT NULL AUTO_INCREMENT, sender_uuid VARCHAR(255) NOT NULL, receiver_uuid VARCHAR(255) NOT NULL, message TEXT NOT NULL, viewed tinyint(1) NOT NULL DEFAULT 0, PRIMARY KEY (id)) ENGINE = InnoDB CHARSET=utf8 COLLATE utf8_bin;");
            sqlMail.execute();
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
        }
        try {
            PreparedStatement sqlPlayer = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS velocitymail_players ( uuid VARCHAR(255) NOT NULL , username VARCHAR(255) NOT NULL , PRIMARY KEY (uuid))");
            sqlPlayer.execute();
        } catch (SQLException e) {
            this.velocityMail.getLogger().debug(e.toString());
        }
    }
}
