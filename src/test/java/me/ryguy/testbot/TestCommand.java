package me.ryguy.testbot;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Image;
import me.ryguy.discordapi.command.Command;
import reactor.core.publisher.Mono;

public class TestCommand extends Command {
    public TestCommand() {
        super("ping", "pinger");
    }

    @Override
    public Mono<Void> execute(Message message, String alias, String[] args) {
        if (message.getGuild().block().getIconUrl(Image.Format.UNKNOWN).isPresent()) {
            message.getChannel().block().createMessage("Guild Icon URL: " + message.getGuild().block().getIconUrl(Image.Format.JPEG).get()).block();
        } else {
            message.getChannel().block().createMessage("Null Guild Icon URL!").block();
        }
        if (message.getAuthor().get().getAvatarUrl(Image.Format.UNKNOWN).isPresent()) {
            message.getChannel().block().createMessage("Guild Icon URL: " + message.getGuild().block().getIconUrl(Image.Format.JPEG).get()).block();
        } else {
            message.getChannel().block().createMessage("Null Guild Icon URL!").block();
        }
        return null;
    }
}
