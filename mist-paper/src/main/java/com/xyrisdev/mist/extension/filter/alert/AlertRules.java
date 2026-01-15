package com.xyrisdev.mist.extension.filter.alert;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.config.ConfigType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class AlertRules {

	private final boolean enabled;
	private final boolean notifyCancel;
	private final boolean notifyModify;
	private final Set<String> disabledRules;

	public AlertRules() {
		final CachableConfiguration config = Mist.INSTANCE.config().get(ConfigType.CONFIGURATION);

		this.enabled = config.getBoolean("staff_alerts.enabled", true);
		this.notifyCancel = config.getBoolean("staff_alerts.notify_on_cancel", true);
		this.notifyModify = config.getBoolean("staff_alerts.notify_on_modify", true);
		this.disabledRules = Set.copyOf(config.getStringList("staff_alerts.disabled_rules"));
	}

	public boolean cancel(@NotNull String rule) {
		return enabled && notifyCancel && !disabledRules.contains(rule);
	}

	public boolean modify(@NotNull String rule) {
		return enabled && notifyModify && !disabledRules.contains(rule);
	}
}
