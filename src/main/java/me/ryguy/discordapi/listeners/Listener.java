package me.ryguy.discordapi.listeners;

public interface Listener {
    default void register() {
        System.out.println("Registering Listener " + this.getClass().getSimpleName());
        EventManager.registerListener(this);
    }
}
