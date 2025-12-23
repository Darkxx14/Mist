package com.xyrisdev.mist.util.matcher;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AhoCorasickMatcher {

	private final Node root = new Node();

	public void clear() {
		root.children.clear();
		root.outputs.clear();
		root.fail = null;
	}

	public void add(@NotNull String word) {
		Node node = root;

		for (char character : word.toCharArray()) {
			node = node.children.computeIfAbsent(character, k -> new Node());
		}

		node.outputs.add(word);
	}

	public void build() {
		final Queue<Node> queue = new ArrayDeque<>();

		root.fail = root;
		queue.add(root);

		while (!queue.isEmpty()) {
			final Node current = queue.poll();

			for (Map.Entry<Character, Node> e : current.children.entrySet()) {
				final char character = e.getKey();
				final Node next = e.getValue();

				Node failed = current.fail;

				while (failed != root && !failed.children.containsKey(character)) {
					failed = failed.fail;
				}

				next.fail = failed.children.getOrDefault(character, root);
				next.outputs.addAll(next.fail.outputs);

				queue.add(next);
			}
		}
	}

	public @NotNull Optional<Match> first(@NotNull String text) {
		Node node = root;

		for (int i = 0; i < text.length(); i++) {
			final char character = text.charAt(i);

			while (node != root && !node.children.containsKey(character)) {
				node = node.fail;
			}

			node = node.children.getOrDefault(character, root);

			if (!node.outputs.isEmpty()) {
				final String word = node.outputs.getFirst();

				return Optional.of(new Match(word, i - word.length() + 1));
			}
		}

		return Optional.empty();
	}

	public record Match(@NotNull String word, int start) {}

	private static class Node {
		final Map<Character, Node> children = new HashMap<>();
		Node fail;
		final List<String> outputs = new ArrayList<>();
	}
}
