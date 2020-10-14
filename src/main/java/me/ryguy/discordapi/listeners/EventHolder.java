package me.ryguy.discordapi.listeners;

import discord4j.core.event.domain.Event;
import lombok.Getter;
import lombok.Setter;
import me.ryguy.discordapi.util.Cancellable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class EventHolder implements Cancellable {
    Class<? extends Event> eventClass;
    Map<Listener, List<Method>> methods;
    boolean cancelled;

    public EventHolder(Class<? extends Event> clazz) {
        this.eventClass = clazz;
        this.methods = new ConcurrentHashMap<>();
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean bool) {
        this.cancelled = bool;
    }

    public static EventHolder of(Class<? extends Event> clazz) {
        return EventManager.getEvents().parallelStream().filter(eh -> eh.getEventClass() == clazz).findFirst().orElse(null);
    }
}
