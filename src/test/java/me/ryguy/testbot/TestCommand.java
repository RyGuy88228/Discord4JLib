package me.ryguy.testbot;

import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import me.ryguy.discordapi.command.Command;
import reactor.core.publisher.Mono;

public class TestCommand extends Command {
    public TestCommand() {
        super("ping", "pinger");
    }

    @Override
    public Mono<Void> execute(User sender, Mono<MessageChannel> channel, String alias, String[] args) {
        channel.block().createMessage("Pong!").block();
        return null;
    }
}
