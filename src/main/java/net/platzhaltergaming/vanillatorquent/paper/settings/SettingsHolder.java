package net.platzhaltergaming.vanillatorquent.paper.settings;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class SettingsHolder {

    @Setting
    private List<Locale> locales = Arrays.asList(Locale.ENGLISH);

}
