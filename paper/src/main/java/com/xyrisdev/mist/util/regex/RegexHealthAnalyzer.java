package com.xyrisdev.mist.util.regex;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@UtilityClass
public class RegexHealthAnalyzer {

	public static @NotNull Result analyze(@NotNull String pattern) {
		int score = 100;
		final List<String> notes = new ArrayList<>();

		final int length = pattern.length();

		int alternations = 0;
		int capturingGroups = 0;
		int characterClasses = 0;

		final boolean anchored = pattern.startsWith("^") || pattern.contains("\\b");

		final Deque<GroupState> groupStack = new ArrayDeque<>();

		for (int i = 0; i < length; i++) {
			final char c = pattern.charAt(i);

			if (c == '\\') {
				i++;
				continue;
			}

			switch (c) {
				case '(' -> {
					final boolean capturing =
							i + 1 >= length || pattern.charAt(i + 1) != '?';

					groupStack.push(new GroupState(capturing));

					if (capturing) {
						capturingGroups++;
					}
				}

				case ')' -> {
					if (!groupStack.isEmpty()) {
						groupStack.pop();
					}
				}

				case '|' -> {
					alternations++;

					if (!groupStack.isEmpty()) {
						groupStack.peek().alternations++;
					}
				}

				case '.' -> {

					if (!groupStack.isEmpty()) {
						groupStack.peek().containsWildcard = true;
					}
				}

				case '[' -> characterClasses++;

				case '*', '+' -> {
					if (groupStack.isEmpty()) {
						break;
					}

					final GroupState group = groupStack.peek();

					if (group.quantified) {
						score -= 40;
						notes.add("Nested quantifiers can cause catastrophic backtracking");
					}

					if (group.alternations > 0) {
						score -= 35;
						notes.add("Quantified alternation detected");
					}

					if (group.containsWildcard) {
						score -= 50;
						notes.add("Wildcard inside quantified group");
					}

					group.quantified = true;
				}
			}
		}

		if (pattern.contains(".*")) {
			score -= 15;
			notes.add("Greedy wildcard detected");
		}

		if (pattern.startsWith(".*")) {
			score -= 20;
			notes.add("Leading wildcard prevents prefix optimizations");
		}

		if (!anchored) {
			score -= 15;
			notes.add("Unanchored regex may overmatch");
		}

		if (!pattern.startsWith("(?i)")) {
			score -= 5;
			notes.add("Case-insensitive flag not enabled");
		}

		if (alternations > 10) {
			score -= 10;
			notes.add("High alternation count (" + alternations + ")");
		}

		if (capturingGroups > 10) {
			score -= 10;
			notes.add("Too many capturing groups (" + capturingGroups + ")");
		}

		if (characterClasses > 12) {
			score -= 5;
			notes.add("Excessive character class usage");
		}

		if (length > 300) {
			score -= 10;
			notes.add("Regex is very long (" + length + " characters)");
		}

		score = Math.max(0, score);

		final Risk risk =
				score >= 80 ? Risk.LOW :
						score >= 50 ? Risk.MEDIUM :
								Risk.HIGH;

		final Verdict verdict =
				score >= 85 ? Verdict.EXCELLENT :
						score >= 65 ? Verdict.GOOD :
								score >= 40 ? Verdict.WEAK :
										Verdict.DANGEROUS;

		return new Result(score, verdict, risk, notes);
	}

	private static class GroupState {
		final boolean capturing;

		boolean quantified;
		boolean containsWildcard;
		int alternations;

		private GroupState(boolean capturing) {
			this.capturing = capturing;
		}
	}

	public record Result(
			int score,
			@NotNull Verdict verdict,
			@NotNull Risk risk,
			@NotNull List<String> notes
	) {}

	public enum Verdict {
		EXCELLENT,
		GOOD,
		WEAK,
		DANGEROUS
	}

	public enum Risk {
		LOW,
		MEDIUM,
		HIGH
	}
}