package com.xyrisdev.mist.util.matcher;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public final class SimilarityMatcher {

	public static double similarity(String a, String b) {
		int dis = levenshtein(a, b);
		int max = Math.max(a.length(), b.length());

		return max == 0 ? 1.0 : 1.0 - ((double) dis / max);
	}

	private static int levenshtein(String a, @NotNull String b) {
		int[] costs = new int[b.length() + 1];

		for (int j = 0; j <= b.length(); j++) {
			costs[j] = j;
		}

		for (int i = 1; i <= a.length(); i++) {
			costs[0] = i;
			int prev = i - 1;

			for (int j = 1; j <= b.length(); j++) {
				int cur = costs[j];

				costs[j] = Math.min(
						Math.min(costs[j] + 1, costs[j - 1] + 1),
						prev + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1)
				);
				prev = cur;
			}
		}
		return costs[b.length()];
	}
}
