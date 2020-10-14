package me.ryguy.discordapi.command;

import lombok.Getter;
import lombok.Setter;
import me.ryguy.discordapi.util.Cancellable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class Command implements CommandExecutor, Cancellable {
    protected String name;
    protected List<String> aliases;
    protected boolean cancelled;

    public Command(String name) {
        this.name = name;
        aliases = new ArrayList<>(); //Empty arraylist
    }

    public Command(String name, String... aliases) {
        this.name = name;
        this.aliases = Arrays.asList(aliases);
    }

    public void register() {
        CommandManager.registerCommand(this);
        System.out.println("Registered command " + this.getName() + "!");
    }
    public void unregister() {
        CommandManager.unregisterCommand(this);
        System.out.println("Unregistered command " + this.getName() + "!");
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
