package net.platzhaltergaming.vanillatorquent.paper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Locale;

import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import lombok.Getter;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.vanillatorquent.paper.configurate.LocaleSerializer;
import net.platzhaltergaming.vanillatorquent.paper.modules.BeeHiveInfo;
import net.platzhaltergaming.vanillatorquent.paper.modules.CoalIntoFurnance;
import net.platzhaltergaming.vanillatorquent.paper.settings.SettingsHolder;

@Getter
public class Main extends JavaPlugin {

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private SettingsHolder settings;
    private Messages messages;

    private BeeHiveInfo beeHiveInfo;
    private CoalIntoFurnance coalIntoFurnance;

    @Override
    public void onEnable() {
        loadConfig();

        messages = new Messages(getDataFolder().toPath(), "messages", settings.getLocales(),
                settings.getLocales().iterator().next());

        beeHiveInfo = new BeeHiveInfo(this, messages);
        beeHiveInfo.onEnable();

        coalIntoFurnance = new CoalIntoFurnance(this);
        coalIntoFurnance.onEnable();
    }

    @Override
    public void onDisable() {
        if (beeHiveInfo != null) {
            beeHiveInfo.onDisable();
        }

        if (coalIntoFurnance != null) {
            coalIntoFurnance.onDisable();
        }
    }

    public void loadConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getClassLoader().getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loader = YamlConfigurationLoader.builder()
                .defaultOptions(
                        opts -> opts.serializers(build -> {
                            build.register(Locale.class, LocaleSerializer.INSTANCE);
                        }))
                .file(file).build();

        CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (IOException e) {
            getLogger().severe("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            settings = root.get(SettingsHolder.class);
        } catch (SerializationException e) {
            getLogger().severe("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

}
