package com.xyrisdev.mist.util.text.tags.registry;

import com.xyrisdev.mist.util.text.tags.ColorTag;
import com.xyrisdev.mist.util.text.tags.PlaceholderAPITag;
import com.xyrisdev.mist.util.text.tags.SmallCapsTag;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TagRegistry {

	private final Map<Set<String>, Function<Audience, ? extends Tag>> tags = new LinkedHashMap<>();
	private final Set<Function<Audience, TagResolver>> resolvers = new LinkedHashSet<>();

	public TagRegistry() {
		register(Set.of("smallcaps"), audience -> new SmallCapsTag());
		registerResolver(PlaceholderAPITag::create);
	}

	public void register(@NotNull Set<String> names, @NotNull Function<Audience, ? extends Tag> factory) {
		this.tags.put(names, factory);
	}

	public void registerResolver(@NotNull Function<Audience, TagResolver> factory) {
		this.resolvers.add(factory);
	}

	public @NotNull TagResolver build(@NotNull Audience audience) {
		final TagResolver.Builder builder = TagResolver.builder();

		this.tags.forEach((names, factory) ->
				builder.tag(names, (args, ctx) -> factory.apply(audience))
		);

		this.resolvers.forEach(factory ->
				builder.resolver(factory.apply(audience))
		);

		builder.resolvers(StandardTags.defaults());
		builder.resolver(ColorTag.resolver());
		return builder.build();
	}
}