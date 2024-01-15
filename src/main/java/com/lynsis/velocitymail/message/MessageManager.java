package com.lynsis.velocitymail.message;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.lynsis.velocitymail.VelocityMail;
import com.lynsis.velocitymail.config.ConfigManager;
import com.lynsis.velocitymail.storage.Message;
import com.lynsis.velocitymail.storage.StorageManager;
import com.lynsis.velocitymail.utils.FileUtils;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ninja.leaping.configurate.ConfigurationNode;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.List;

public class MessageManager {
    public static String prefix;
    public static @NonNull ConfigurationNode messages;

    public static void loadMessages() {
        try {
            String lang = ConfigManager.config.getNode("language").toString() != null ? ConfigManager.config.getNode("language").getString() : "cs_cz";
            MessageManager.messages = FileUtils.loadOrCreate("languages/" + lang);
            MessageManager.prefix = ConfigManager.config.getNode("prefix").getString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void playerNotFound(CommandSource p) {
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "<reset> " + MessageManager.messages.getNode("playerNotFound").getString()
        ));
    }

    public static void mailSent(CommandSource p, String receiverName) {
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "<reset> " + MessageManager.messages.getNode("mailSent").getString(),
                Placeholder.unparsed("receivername", receiverName)
        ));
    }

    public static void mailView(CommandSource p) {
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "<reset> " + MessageManager.messages.getNode("mailView").getString()
        ));
    }

    public static void mail(CommandSource p, Message message, StorageManager storageManager) {
        String senderName = storageManager.getPlayerByUuid(message.senderUuid);
        UrlDetector parser = new UrlDetector(message.message, UrlDetectorOptions.Default);

        Component messageComp = null;
        List<Url> links = parser.detect();
        if (!links.isEmpty()) {
            messageComp = MiniMessage.miniMessage().deserialize(message.message);

            for(Url link : links){
                messageComp = messageComp.replaceText(
                        TextReplacementConfig.builder().matchLiteral(
                                link.getOriginalUrl()).replacement(
                                        MiniMessage.miniMessage().deserialize("<a>" + link.getOriginalUrl() + "</a>",TagResolver.resolver("a", createStyledHref(link.getFullUrl())))
                        ).build());

                VelocityMail.get().getLogger().error(link.getOriginalUrl());
                VelocityMail.get().getLogger().error(link.getFullUrl());
            }
        }else{
            messageComp = MiniMessage.miniMessage().deserialize(message.message);
        }

        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "<reset> " + MessageManager.messages.getNode("mail").getString(),
                Placeholder.unparsed("sendername", senderName),
                Placeholder.component("message", messageComp),
                Placeholder.styling("mailplayer", ClickEvent.suggestCommand("/mail " + senderName + " ")),
                Formatter.date("date", message.dateTime)
        ));

    }

    public static void mailEmpty(CommandSource p) {
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "<reset> " + MessageManager.messages.getNode("mailEmpty").getString())
        );
    }

    public static void mailHelp(CommandSource p) {
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "<reset> " + MessageManager.messages.getNode("mailHelp").getString()
        ));
    }

    public static void mailInfo(Player p) {
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "<reset> " + MessageManager.messages.getNode("mailInfo").getString()
        ));
    }

    public static void mailReload(CommandSource p) {
        p.sendMessage(MiniMessage.miniMessage().deserialize(
                MessageManager.prefix + "<reset> VelocityMail reloaded"
        ));
    }

    public static Tag createStyledHref(final String url) {
        return Tag.styling(
                TextDecoration.UNDERLINED,
                ClickEvent.openUrl(url),
                HoverEvent.showText(Component.text("Otevřít " + url))
        );
    }
}
