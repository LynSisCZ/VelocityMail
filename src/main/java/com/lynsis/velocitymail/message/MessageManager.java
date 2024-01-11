package com.lynsis.velocitymail.message;

import com.lynsis.velocitymail.config.ConfigManager;
import com.lynsis.velocitymail.storage.Message;
import com.lynsis.velocitymail.storage.StorageManager;
import com.lynsis.velocitymail.utils.FileUtils;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import ninja.leaping.configurate.ConfigurationNode;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;

public class MessageManager {
    public static String prefix = ConfigManager.config.getNode("prefix").getString();
    public static @NonNull ConfigurationNode messages;

    public static void loadMessages(){
        try {
            String lang =  ConfigManager.config.getNode("language").toString() != null ? ConfigManager.config.getNode("language").getString() : "cs_cz";
            MessageManager.messages = FileUtils.loadOrCreate("languages/" + lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void playerNotFound(CommandSource p){
        p.sendMessage(MiniMessage.miniMessage().deserialize(MessageManager.prefix  + "&r " + MessageManager.messages.getNode("playerNotFound").getString()));
    }

    public static void mailSent(CommandSource p, String receiverName){
        p.sendMessage(MiniMessage.miniMessage().deserialize(MessageManager.prefix  + "&r " + MessageManager.messages.getNode("mailSent").getString(), Placeholder.unparsed("receivername",  receiverName)));
    }
    public static void mailView(CommandSource p){
        p.sendMessage(MiniMessage.miniMessage().deserialize(MessageManager.prefix  + "&r " + MessageManager.messages.getNode("mailView").getString()));
    }

    public static void mail(CommandSource p, Message message, StorageManager storageManager){
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix  + "&r " + MessageManager.messages.getNode("mail").getString(),
                Placeholder.unparsed("sendername", storageManager.getPlayerByUuid(message.senderUuid)),
                Placeholder.unparsed("message", message.message))
        );
    }

    public static void mailEmpty(CommandSource p){
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "&r " +
                      MessageManager.messages.getNode("mailEmpty").getString())
        );
    }

    public static void mailHelp(CommandSource p){
        p.sendMessage(MiniMessage.miniMessage().deserialize(MessageManager.prefix  + "&r " + MessageManager.messages.getNode("mailHelp").getString()));
    }

    public static void mailInfo(Player p) {
        p.sendMessage(MiniMessage.miniMessage().deserialize(MessageManager.prefix  + "&r " + MessageManager.messages.getNode("mailInfo").getString()));
    }
}
