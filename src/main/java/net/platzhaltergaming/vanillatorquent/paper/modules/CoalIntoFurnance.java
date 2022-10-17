package net.platzhaltergaming.vanillatorquent.paper.modules;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CoalIntoFurnance implements Listener {

    private final Plugin plugin;

    public final static int COAL_MAX_STACK_SIZE = 64;

    public void onEnable() {
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.useInteractedBlock() == Result.DENY) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        // There's also BLAST_FURNACE block
        if (!block.getType().equals(Material.FURNACE) && !block.getType().equals(Material.BLAST_FURNACE)) {
            return;
        }

        Player player = event.getPlayer();
        // Check if item in hand is a valid fuel
        ItemStack playerFuel = player.getInventory().getItemInMainHand();
        if (playerFuel == null || playerFuel.getType().equals(Material.AIR)) {
            return;
        }

        // Check if valid fuel that is supported
        Material playerFuelType = playerFuel.getType();
        if (!playerFuelType.equals(Material.COAL) && !playerFuelType.equals(Material.CHARCOAL)) {
            return;
        }

        Furnace furnace = (Furnace) block.getState();
        FurnaceInventory furnanceInventory = furnace.getInventory();

        ItemStack fuel = furnanceInventory.getFuel();
        if (fuel == null) {
            // No fuel in furnace
            fuel = playerFuel;
            player.getInventory().remove(playerFuel);
            furnanceInventory.setFuel(playerFuel);
        } else if (fuel.getAmount() >= COAL_MAX_STACK_SIZE) {
            // Fuel slot is full (at max stack size)
            return;
        } else if (fuel.getType().equals(playerFuelType)) {
            // If furnace contains the same as the player fuel item "merge" the itemstack amount to max size
            int maxFuelToAdd = COAL_MAX_STACK_SIZE - fuel.getAmount();
            // If playerFuel is euqal or less than what we need to fill the stack
            if (playerFuel.getAmount() <= maxFuelToAdd) {
                fuel.add(maxFuelToAdd);
                player.getInventory().remove(playerFuel);
            } else {
                // Should the player have more than needed, remove from player and add it to furnace
                playerFuel.setAmount(playerFuel.getAmount()-maxFuelToAdd);
                fuel.add(maxFuelToAdd);
            }
        } else {
            // Switch whatever fuel the furnace has and the player is holding
            player.getInventory().remove(playerFuel);
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), fuel);
            furnanceInventory.setFuel(playerFuel);
        }

        event.setCancelled(true);
    }

}
