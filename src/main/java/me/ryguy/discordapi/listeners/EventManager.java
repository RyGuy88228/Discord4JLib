package me.ryguy.discordapi.listeners;

import discord4j.core.event.domain.Event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    protected static Map<Class<? extends Event>, Map<Listener, List<Method>>> events = new ConcurrentHashMap<>();
    static EventManager instance;

    public EventManager() {
        instance = this;
    }

    public static void registerListener(Listener listener) {
        List<Method> methods = new ArrayList<>();
        try {
            for (Method m : listener.getClass().getMethods()) {
                methods.add(m);
            }
            for (Method m : listener.getClass().getDeclaredMethods()) {
                if (!methods.contains(m))
                    methods.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error registering listener " + listener.getClass().getSimpleName());
            e.printStackTrace();
        }
        for (final Method method : methods) {
            //avoid methods that aren't notated
            DiscordEvent annotation = method.getAnnotation(DiscordEvent.class);
            if (annotation == null) continue;
            //avoid registering duplicates
            if (method.isBridge() || method.isSynthetic()) continue;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                System.out.println("Tried to register invalid listener from method " + method.getName() + " in class " + listener.getClass().getSimpleName());
                continue;
            }

            final Class<? extends Event> eventClass = method.getParameterTypes()[0].asSubclass(Event.class);

            if (events.containsKey(eventClass)) {
                if (events.get(eventClass).containsKey(listener)) {
                    events.get(eventClass).get(listener).add(method);
                } else {
                    List<Method> toPut = new ArrayList<>();
                    toPut.add(method);
                    events.get(eventClass).put(listener, toPut);
                }
            } else {
                List<Method> toPut = new ArrayList<>();
                toPut.add(method);
                Map<Listener, List<Method>> newMap = new ConcurrentHashMap<>();
                newMap.put(listener, toPut);
                events.put(eventClass, newMap);
            }
        }

    }

    public static Map<Class<? extends Event>, Map<Listener, List<Method>>> getEvents() {
        return events;
    }
}
