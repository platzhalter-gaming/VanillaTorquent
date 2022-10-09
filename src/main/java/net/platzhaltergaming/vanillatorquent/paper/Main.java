package net.platzhaltergaming.vanillatorquent.paper;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.platzhaltergaming.vanillatorquent.paper.modules.CoalIntoFurnance;

@Getter
public class Main extends JavaPlugin {

    private CoalIntoFurnance coalIntoFurnance;

    @Override
    public void onEnable() {
        coalIntoFurnance = new CoalIntoFurnance(this);
        coalIntoFurnance.onEnable();
    }

    @Override
    public void onDisable() {
        if (coalIntoFurnance != null) {
            coalIntoFurnance.onDisable();
        }
    }

}
