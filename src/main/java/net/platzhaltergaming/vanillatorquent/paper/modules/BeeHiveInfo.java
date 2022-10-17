package net.platzhaltergaming.vanillatorquent.paper.modules;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.platzhaltergaming.commonlib.messages.Messages;

@Getter
@RequiredArgsConstructor
public class BeeHiveInfo implements Listener {

    private MiniMessage miniMessage = MiniMessage.miniMessage();

    private final Plugin plugin;
    private final Messages messages;

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

        if (!event.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        Block block = event.getClickedBlock();
        if (!event.hasBlock() || block == null) {
            return;
        }

        Material blockType = block.getType();
        if (!blockType.equals(Material.BEE_NEST) && !blockType.equals(Material.BEEHIVE)) {
            return;
        }

        ItemStack item = event.getItem();
        if (item != null) {
            return;
        }

        Beehive beehive = (Beehive) block.getBlockData();
        org.bukkit.block.Beehive beehiveState = (org.bukkit.block.Beehive) block.getState();

        List<TagResolver> tags = new ArrayList<>();
        tags.add(Placeholder.unparsed("bees", String.valueOf(beehiveState.getEntityCount())));
        tags.add(Placeholder.unparsed("max-bees", String.valueOf(beehiveState.getMaxEntities())));
        tags.add(Placeholder.unparsed("honey-level", String.valueOf(beehive.getHoneyLevel())));
        tags.add(Placeholder.unparsed("max-honey-level", String.valueOf(beehive.getMaximumHoneyLevel())));

        Player player = event.getPlayer();
        player.sendMessage(
                miniMessage.deserialize(messages.get(player.locale(), "beehiveinfo.chat"), TagResolver.resolver(tags)));
    }

}
