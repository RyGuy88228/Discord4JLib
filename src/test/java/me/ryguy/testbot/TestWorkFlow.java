package me.ryguy.testbot;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import me.ryguy.discordapi.command.Command;
import me.ryguy.discordapi.util.WorkFlow;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

public class TestWorkFlow extends Command {
    public TestWorkFlow() {
        super("testworkflow", "twf");
    }

    @Override
    public boolean canExecute(Message e, boolean ignore) {
        return true;
    }

    @Override
    public Mono<Void> execute(Message message, String alias, String[] args) {

        AtomicReference<String> name = new AtomicReference<>();
        AtomicReference<String> desc = new AtomicReference<>();
        AtomicReference<String> footer = new AtomicReference<>();

        WorkFlow<EmbedCreateSpec> workFlow = new WorkFlow<EmbedCreateSpec>(new EmbedCreateSpec(), message.getChannel().block(), message.getAuthor().get());

        workFlow.deletePreviousStep();

        workFlow.addRule("!cancel", embed -> {
            message.getChannel().block().createMessage(":x: Workflow Cancelled!").block();
            workFlow.end();
        }).andThen(embed -> {
            workFlow.sendMessage(message.getChannel().block().createEmbed(e -> {
                e.setTitle("Embed Creator");
                e.setFooter("!cancel to cancel!", null);
                e.setColor(Color.GREEN);
                e.setDescription("Enter a description!");
            }));
        }, (embed, f, m) -> {
            desc.set(m.getContent());
            embed.setDescription(m.getContent());
            f.nextStep();
        }).andThen(embed -> {
            workFlow.sendMessage(message.getChannel().block().createEmbed(e -> {
                e.setTitle("Embed Creator");
                e.setFooter("!cancel to cancel!", null);
                e.setColor(Color.GREEN);
                e.setDescription("Enter a title!");
            }));
        }, (embed, f, m) -> {
            embed.setTitle(m.getContent());
            name.set(m.getContent());
            f.nextStep();
        }).andThen(embed -> {
            workFlow.sendMessage(message.getChannel().block().createEmbed(e -> {
                e.setTitle("Embed Creator");
                e.setFooter("!cancel to cancel!", null);
                e.setColor(Color.GREEN);
                e.setDescription("Enter a footer!");
            }));
        }, (embed, f, m) -> {
            footer.set(m.getContent());
            embed.setFooter(m.getContent(), null);

            message.getChannel().block().createEmbed(e -> {
                e.setTitle(name.get());
                e.setDescription(desc.get());
                e.setFooter(footer.get(), null);
                e.setColor(Color.GREEN);
            }).block();
            f.end();
        }).start();

        return null;
    }
}
