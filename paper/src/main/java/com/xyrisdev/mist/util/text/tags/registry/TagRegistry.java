package com.xyrisdev.mist.util.text.tags.registry;

import com.xyrisdev.mist.util.text.tags.CenterTag;
import com.xyrisdev.mist.util.text.tags.SmallCapsTag;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class TagRegistry {

	private final Map<Set<String>, Function<Audience, ? extends Tag>> tags = new LinkedHashMap<>();

	public TagRegistry() {
		register(Set.of("center"), CenterTag::new);
		register(Set.of("smallcaps", "sc"), audience -> new SmallCapsTag());
	}

	public void register(
			@NotNull Set<String> names,
			@NotNull Function<Audience, ? extends Tag> factory
	) {
		this.tags.put(names, factory);
	}

	public @NotNull TagResolver build(@NotNull Audience audience) {
		final TagResolver.Builder builder = TagResolver.builder();

		this.tags.forEach((names, factory) ->
				builder.tag(names, (args, ctx) -> factory.apply(audience))
		);

		builder.resolvers(StandardTags.defaults());
		return builder.build();
	}
}
