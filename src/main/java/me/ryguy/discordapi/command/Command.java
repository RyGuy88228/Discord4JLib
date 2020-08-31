package me.ryguy.discordapi.command;

import java.util.Arrays;
import java.util.List;

public abstract class Command implements CommandExecutor {
    protected String name;
    protected List<String> aliases;

    public Command(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean canExecute() {
        return true;
    }
}
