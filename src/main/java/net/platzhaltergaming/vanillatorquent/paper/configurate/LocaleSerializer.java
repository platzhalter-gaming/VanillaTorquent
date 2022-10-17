package net.platzhaltergaming.vanillatorquent.paper.configurate;

import java.util.Locale;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;

public final class LocaleSerializer implements TypeSerializer<Locale> {

    public static final LocaleSerializer INSTANCE = new LocaleSerializer();

    private LocaleSerializer() {
    }

    @Override
    public Locale deserialize(final Type type, final ConfigurationNode source) throws SerializationException {
        Locale.Builder localeBuilder = new Locale.Builder();

        String[] splitSource = source.getString().split("_", 2);
        localeBuilder.setLanguageTag(splitSource[0]);
        if (splitSource.length > 1) {
            localeBuilder.setRegion(splitSource[1]);
        }

        Locale locale = localeBuilder.build();
        return locale;
    }

    @Override
    public void serialize(final Type type, final @Nullable Locale locale, final ConfigurationNode target)
            throws SerializationException {
        if (locale == null) {
            target.raw(null);
            return;
        }

        target.set(locale.toLanguageTag());
    }

}
