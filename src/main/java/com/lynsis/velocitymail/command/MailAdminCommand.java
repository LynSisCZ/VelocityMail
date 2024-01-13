package com.lynsis.velocitymail.command;

import com.lynsis.velocitymail.VelocityMail;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;


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
                    context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("/mailadmin reload"));
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
                                    context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("VelocityMail reloaded"));
                                    break;
                                default:
                                    context.getSource().sendMessage(MiniMessage.miniMessage().deserialize("/mailadmin reload"));
                                    break;
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
        return new BrigadierCommand(helloNode);
    }
}