package me.ryguy.discordapi;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.request.RouteMatcher;
import discord4j.rest.response.ResponseFunction;
import discord4j.rest.route.Routes;
import lombok.Setter;
import me.ryguy.discordapi.command.Command;
import me.ryguy.discordapi.command.CommandHandler;
import me.ryguy.discordapi.listeners.EventHandler;
import me.ryguy.discordapi.listeners.MainListener;
import reactor.retry.Retry;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DiscordBot {

    private static DiscordBot instance;
    private final String token;
    private final String prefix;
    public BiConsumer<Throwable, Event> eventException = (ex, ev) -> ex.printStackTrace();
    public BiConsumer<Throwable, Command> commandException = (ex, cmd) -> ex.printStackTrace();
    @Setter
    public BiConsumer<Command, Message> checkCommandCancellation = (cmd, msg) -> {};
    @Setter
    public Consumer<Event> checkEventCancellation = event -> {};
    private DiscordClient client;
    private GatewayDiscordClient gateway;

    public DiscordBot(String token, String prefix) {
        this.token = token;
        this.prefix = prefix;
    }

    public static DiscordBot getBot() {
        return instance;
    }

    public void loginBot() {
        instance = this;
        try {
            client = DiscordClientBuilder.create(token)
                    // globally suppress any not found (404) error
                    .onClientResponse(ResponseFunction.emptyIfNotFound())
                    // server error (500) while creating a message will be retried, with backoff, until it succeeds
                    .onClientResponse(ResponseFunction.retryWhen(RouteMatcher.route(Routes.MESSAGE_CREATE),
                            Retry.onlyIf(ClientException.isRetryContextStatusCode(500))
                                    .exponentialBackoffWithJitter(Duration.ofSeconds(2), Duration.ofSeconds(10))))
                    // wait 1 second and retry any server error (500)
                    .onClientResponse(ResponseFunction.retryOnceOnErrorStatus(500))
                    .build();
            gateway = client.login().block();
        } catch (Exception e) {
            System.out.println("Error initializing bot with token " + token + "!");
            e.printStackTrace();
            System.exit(0);
            return;
        }
        gateway.on(ReadyEvent.class).subscribe(e -> {
            User self = e.getSelf();
            System.out.println(String.format("[RyDiscordLib] Logged in as %s#%s!", self.getUsername(), self.getDiscriminator()));
            System.out.println(String.format("[RyDiscordLib] Using command prefix '%s'!", this.prefix));
        });

        new CommandHandler();
        new EventHandler();

        new MainListener().register();

    }

    public void endStartup() {
        gateway.onDisconnect().block();
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

    public void setCommandErrorHandler(BiConsumer<Throwable, Command> consumer) {
        DiscordBot.getBot().commandException = consumer;
    }

    public void setEventErrorHandler(BiConsumer<Throwable, Event> consumer) {
        DiscordBot.getBot().eventException = consumer;
    }
}
