package com.xyrisdev.mist.util.logger.suppress;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.logging.Filter;
import java.util.logging.Logger;

@UtilityClass
public final class FoliaLibSuppressor {

    public static <T> T suppress(@NotNull JavaPlugin plugin, @NotNull Supplier<T> action) {
        final Logger logger = plugin.getLogger();
        final Filter filter = logger.getFilter();

        logger.setFilter(logRecord -> {
            final String msg = logRecord.getMessage();

            if (msg == null) {
                return true;
            }

            return !(msg.contains("FoliaLib is not relocated correctly")
                    || msg.contains("with other plugins using FoliaLib")
                    || msg.contains("inform them of this issue immediately")
                    || msg.equals("****************************************************************"));
        });

        try {
            return action.get();
        } finally {
            logger.setFilter(filter);
        }
    }
}
