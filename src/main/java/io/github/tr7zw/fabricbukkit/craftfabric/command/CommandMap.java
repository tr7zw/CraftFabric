package io.github.tr7zw.fabricbukkit.craftfabric.command;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

import java.util.Map;

public class CommandMap extends SimpleCommandMap {

    public CommandMap(Server server) {
        super(server);
    }

    public Map<String, Command> getKnownCommands() {
        return knownCommands;
    }
}
