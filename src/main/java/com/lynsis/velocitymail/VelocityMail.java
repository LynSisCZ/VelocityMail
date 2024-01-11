package com.lynsis.velocitymail;

import com.google.inject.Inject;
import com.lynsis.velocitymail.command.MailCommand;
import com.lynsis.velocitymail.config.ConfigManager;
import com.lynsis.velocitymail.message.MessageManager;
import com.lynsis.velocitymail.storage.StorageManager;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "velocitymail",
        name = "VelocityMail",
        version = "1.0-SNAPSHOT",
        authors = {"LynSisCZ"}
)
public class VelocityMail {
    static VelocityMail instance;
    private final Logger logger;
    private final ProxyServer proxy;
    private final Path dataDirectory;
    public StorageManager storageManager;

    @Inject
    public VelocityMail(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;
        this.logger = logger;
        this.proxy = proxy;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
        this.proxy.getCommandManager().register(MailCommand.commandMeta(this), MailCommand.createBrigadierCommand(this.proxy, this));
        ConfigManager.loadConfig();
        this.logger.error(ConfigManager.config.getNode("storage", "data", "address").getString());
        MessageManager.loadMessages();
        this.storageManager = new StorageManager(this);
        this.storageManager.loadPlayersOnStart();
    }

    public void unRegisterCommand(){
        this.proxy.getCommandManager().unregister(MailCommand.commandMeta(this));
    }

    @Subscribe
    public void onPlayerJoin(ServerConnectedEvent joinEvent) {
        this.storageManager.addPlayer(joinEvent.getPlayer());
        this.storageManager.findMessages(joinEvent.getPlayer());
    }

    public ProxyServer getProxy() {
        return this.proxy;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Path getDataDirectory() {
        return this.dataDirectory;
    }

    public static VelocityMail get() {
        return instance;
    }
}
