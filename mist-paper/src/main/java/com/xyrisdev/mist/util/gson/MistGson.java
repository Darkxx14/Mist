package com.xyrisdev.mist.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

@UtilityClass
public class MistGson {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(
                    net.kyori.adventure.text.Component.class,
                    GsonComponentSerializer.gson()
            )
            .create();

    public static Gson instance() {
        return GSON;
    }
}
