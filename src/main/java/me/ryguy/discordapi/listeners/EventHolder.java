package me.ryguy.discordapi.listeners;

import discord4j.core.event.domain.Event;
import lombok.Getter;
import lombok.Setter;
import me.ryguy.discordapi.util.Cancellable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@Setter
public class EventHolder implements Cancellable {
    Class<? extends Event> eventClass;
    Map<Listener, List<Method>> methods;
    boolean cancelled;

    public EventHolder(Class<? extends Event> clazz) {
        this.eventClass = clazz;
        this.methods = new ConcurrentHashMap<>();
        this.cancelled = false;
    }

    public static EventHolder of(Class<? extends Event> clazz) {
        return EventManager.getEvents().parallelStream().filter(eh -> eh.getEventClass() == clazz).findFirst().orElse(null);
    }
    public static List<EventHolder> of(Listener listener) {
        return EventManager.getEvents().parallelStream().filter(eh -> eh.getMethods().keySet().parallelStream().map(Listener::getClass).collect(Collectors.toList()).contains(listener.getClass())).collect(Collectors.toList());
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean bool) {
        this.cancelled = bool;
    }
}
