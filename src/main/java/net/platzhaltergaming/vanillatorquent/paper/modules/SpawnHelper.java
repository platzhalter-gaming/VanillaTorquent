package net.platzhaltergaming.vanillatorquent.paper.modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SpawnHelper implements CommandExecutor {

    private final JavaPlugin plugin;

    public void onEnable() {
        getPlugin().getCommand("vanillatorquentsetplayerspawn").setExecutor(this);
    }

    public void onDisable() {
        getPlugin().getCommand("vanillatorquentsetplayerspawn").unregister(getPlugin().getServer().getCommandMap());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            return false;
        }

        if (args.length < 1) {
            return false;
        }

        Player player = getPlugin().getServer().getPlayer(args[0]);
        if (player == null) {
            return false;
        }

        return player.performCommand("/setspawn");
    }

}
