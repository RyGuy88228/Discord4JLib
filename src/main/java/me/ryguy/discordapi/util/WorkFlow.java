package me.ryguy.discordapi.util;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import reactor.function.Consumer3;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class WorkFlow<T> {

    private static final List<WorkFlow> flows = new ArrayList<>();

    private final T type;
    private final Queue<Step> steps;
    private final long channel;
    private final long user;
    private boolean deletePrevious = false;
    private final Map<String, Consumer<WorkFlow<T>>> rules;

    private long lastSetupMsg;
    private long lastUserMsg;

    public WorkFlow(T type, Channel channel, User user) {
        this.type = type;
        this.steps = new LinkedBlockingQueue<>();
        this.channel = channel.getId().asLong();
        this.user = user.getId().asLong();
        this.rules = new HashMap<>();
    }

    public static void handle(MessageCreateEvent e) {
        for (WorkFlow flow : flows) {
            if (flow.channel == e.getMessage().getChannel().block().getId().asLong() && flow.user == e.getMessage().getAuthor().get().getId().asLong()) {
                if (flow.rules.containsKey(e.getMessage().getContent().toLowerCase())) {
                    ((Consumer<WorkFlow>) flow.rules.get(e.getMessage().getContent().toLowerCase())).accept(flow.getInstance());
                }
                flow.lastUserMsg = e.getMessage().getId().asLong();
                flow.getCurrentStep().handleEvent.accept(flow.type, flow.getInstance(), e.getMessage());
            }
        }
    }

    public WorkFlow<T> getInstance() {
        return this;
    }

    public WorkFlow<T> deletePreviousStep() {
        deletePrevious = true;
        return this;
    }

    public WorkFlow<T> addRule(String trigger, Consumer<WorkFlow<T>> consumer) {
        rules.put(trigger, consumer);
        return this;
    }

    public WorkFlow<T> removeRule(String trigger) {
        rules.remove(trigger);
        return this;
    }

    public WorkFlow<T> andThen(Consumer<WorkFlow<T>> pre, Consumer3<T, WorkFlow<T>, Message> post) {
        steps.add(new Step(pre, post));
        return this;
    }

    public boolean nextStep() {
        steps.remove();
        if (steps.peek() != null) {
            steps.peek().preEvent.accept(getInstance());
            return true;
        }
        return false;
    }

    public void start() {
        flows.add(this);
        steps.peek().preEvent.accept(getInstance());
    }

    public void end() {
        flows.remove(this);
    }

    public Step getCurrentStep() {
        return steps.peek();
    }

    public class Step {
        Consumer<WorkFlow<T>> preEvent;
        Consumer3<T, WorkFlow<T>, Message> handleEvent;

        public Step(Consumer<WorkFlow<T>> con1, Consumer3<T, WorkFlow<T>, Message> con2) {
            this.preEvent = con1;
            this.handleEvent = con2;
        }
    }
}
