package me.ryguy.discordapi;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import me.ryguy.discordapi.command.CommandHandler;
import me.ryguy.discordapi.listeners.EventHandler;

public class DiscordBot {

    private DiscordClient client;
    private GatewayDiscordClient gateway;
    private static DiscordBot instance;
    private final String token;
    private final String prefix;

    public DiscordBot(String token, String prefix) {
        this.token = token;
        this.prefix = prefix;
    }

    public void loginBot() {
        instance = this;
        try {
            client = DiscordClientBuilder.create(token).build();
            gateway = client.login().block();
        } catch (Exception e) {
            System.out.println("Error initializing bot with token " + token + "!");
            e.printStackTrace();
            return;
        }
        gateway.on(ReadyEvent.class).subscribe(e -> {
            User self = e.getSelf();
            System.out.println(String.format("[RyDiscordLib] Logged in as %s#%s!", self.getUsername(), self.getDiscriminator()));
            System.out.println(String.format("[RyDiscordLib] Using command prefix '%s'!", this.prefix));
        });

        new CommandHandler();
        new EventHandler();

    }

    public void endStartup() {
        gateway.onDisconnect().block();
    }

    public static DiscordBot getBot() {
        return instance;
    }

    public GatewayDiscordClient getGateway() {
        return this.gateway;
    }

    public DiscordClient getClient() {
        return this.client;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
