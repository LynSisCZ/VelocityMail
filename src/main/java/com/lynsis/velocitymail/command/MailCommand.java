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


public final class MailCommand {
    public static CommandMeta commandMeta(VelocityMail velocityMail) {
        return velocityMail.getProxy().getCommandManager().metaBuilder("mail")
                .aliases("email")
                .plugin(velocityMail)
                .build();
    }

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy, VelocityMail velocityMail) {
        LiteralCommandNode<CommandSource> helloNode = LiteralArgumentBuilder
                .<CommandSource>literal("mail")
                .executes(context -> {
                    MessageManager.mailHelp(context.getSource());
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("argument", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            builder.suggest("view");
                            builder.suggest("list");
                            builder.suggest("all");
                            builder.suggest("clear");
                            velocityMail.storageManager.getPlayers().forEach((key, value) -> builder.suggest(
                                   key
                            ));
                            return builder.buildFuture();
                        }).then(RequiredArgumentBuilder.<CommandSource, String>argument("msg", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String receiverName = context.getArgument("argument", String.class).toLowerCase();
                                    String msg = context.getArgument("msg", String.class);
                                    String receiverUuid = (!receiverName.equals("console")) ? velocityMail.storageManager.getPlayerUuid(receiverName) : "console";

                                    if (receiverUuid == null) {
                                        MessageManager.playerNotFound(context.getSource());
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    if (context.getSource() instanceof ConsoleCommandSource) {
                                        velocityMail.storageManager.saveMessage(new Message("console", receiverUuid, msg, LocalDateTime.now()));
                                    } else {
                                        Player p = ((Player) context.getSource());
                                        velocityMail.storageManager.saveMessage(new Message(p.getUniqueId().toString(), receiverUuid, msg, LocalDateTime.now()));
                                    }

                                    MessageManager.mailSent(context.getSource(), receiverName);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .executes(context -> {
                            String argumentProvided = context.getArgument("argument", String.class);

                            switch (argumentProvided) {
                                case "view":
                                case "list":
                                    velocityMail.storageManager.getView(context.getSource());
                                    break;
                                case "all":
                                case "clear":
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