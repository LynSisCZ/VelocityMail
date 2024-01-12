package com.lynsis.velocitymail.command;

import com.lynsis.velocitymail.VelocityMail;
import com.lynsis.velocitymail.message.MessageManager;
import com.lynsis.velocitymail.storage.Message;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.time.LocalDateTime;


public final class MailAdminCommand {
    public static CommandMeta commandMeta(VelocityMail velocityMail) {
        return velocityMail.getProxy().getCommandManager().metaBuilder("mailadmin")
                .plugin(velocityMail)
                .build();
    }

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy, VelocityMail velocityMail) {
        LiteralCommandNode<CommandSource> helloNode = LiteralArgumentBuilder
                .<CommandSource>literal("mailadmin")
                .requires(source -> source.hasPermission("mail.mailadmin"))
                .executes(context -> {
                    MessageManager.mailHelp(context.getSource());
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("argument", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            builder.suggest("reload");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String argumentProvided = context.getArgument("argument", String.class);

                            switch (argumentProvided) {
                                case "reload":
                                    velocityMail.reload();
                                    MessageManager.mailReload(context.getSource());
                                    break;
                                default:
                                    MessageManager.mailHelp(context.getSource());
                                    break;
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
        return new BrigadierCommand(helloNode);
    }
}