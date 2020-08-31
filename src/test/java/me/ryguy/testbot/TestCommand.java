package me.ryguy.testbot;

import discord4j.core.object.entity.Message;
import me.ryguy.discordapi.command.Command;
import reactor.core.publisher.Mono;

public class TestCommand extends Command {
    public TestCommand() {
        super("ping", "pinger");
    }

    @Override
    public Mono<Void> execute(Message message, String alias, String[] args) {
        message.getChannel().block().createMessage("Pong!").block();
        return null;
    }
}
