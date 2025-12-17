package com.xyrisdev.mist.api.module.builder;

import com.xyrisdev.mist.api.module.ChatModule;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class ChatModules {

	private final List<ChatModule> modules = new ArrayList<>();

	private ChatModules() {}

	@Contract(" -> new")
	public static @NotNull Builder builder() {
		return new Builder();
	}

	@Contract(pure = true)
	public @Unmodifiable List<ChatModule> build() {
		return List.copyOf(modules);
	}

	public static final class Builder {
		private final ChatModules result = new ChatModules();

		@Contract(value = "_ -> new", pure = true)
		public <R> @NotNull ModuleBuilder<R> module(Function<R, ChatModule> factory) {
			return new ModuleBuilder<>(this, factory);
		}

		@Contract(pure = true)
		public @Unmodifiable List<ChatModule> build() {
			return result.build();
		}
	}

	public static final class ModuleBuilder<R> {

		private final Builder parent;
		private final Function<R, ChatModule> factory;

		private int priority;
		private R rules;

		private ModuleBuilder(Builder parent, Function<R, ChatModule> factory) {
			this.parent = parent;
			this.factory = factory;
		}

		public ModuleBuilder<R> priority(int priority) {
			this.priority = priority;
			return this;
		}

		public ModuleBuilder<R> rules(R rules) {
			this.rules = rules;
			return this;
		}

		public Builder register() {
			final ChatModule module = factory.apply(rules);

			parent.result.modules.add(module);
			parent.result.modules.sort(Comparator.comparingInt(ChatModule::priority));

			return parent;
		}
	}
}
