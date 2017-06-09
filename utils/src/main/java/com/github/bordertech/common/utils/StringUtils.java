package com.github.bordertech.common.utils;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator;


/**
 * @author Rick Brown on 2017-06-08.
 */
public final class StringUtils {

	/**
	 * Prevent instantiation of static utility class.
	 */
	private StringUtils() {

	}

	/**
	 * Used to substitute commonly encountered "symbols" with ascii representations.
	 */
	private static final CharSequenceTranslator SYMBOLS = new LookupTranslator(
		new String[][]{
			{"\u201C", "\""},
			{"\u201D", "\""},
			{"\u201E", "\""},
			{"\u2018", "'"},
			{"\u2019", "'"},
			{"\u2013", "-"},
			{"\u2014", "-"},
			{"\u00BD", "1/2"},
			{"\u00BC", "1/4"},
			{"\u00BE", "3/4"},
			{"\u00A9", "(C)"},
			{"\u00AE", "(R)"},
			{"\u2122", "(TM)"},
			{"\u8230", "..."},
			{"\u02C6", "^"},
			{"\u2039", "<"},
			{"\u203A", ">"},
			{"\u02DC", "~"},
			{"\u00A0", " "}
		});

	/**
	 * Special symbols in user input will be substituted for plain ascii characters.
	 *
	 * The most common scenario is sanitizing user input which has been copied and pasted from Microsoft Word
	 * before being passed to a system that was either built or configured in the 1980s \uD83D\uDE1B.
	 *
	 * Note: some input characters will be replaced with MULTIPLE output characters (e.g. ellipsis).
	 *
	 * Warning: you should perform any escape or encode routines AFTER calling this method.
	 *
	 * @param input The input text to asciify.
	 * @return The input text, asciified.
	 */
	public static String asciifySymbol(final String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		return SYMBOLS.translate(input);
	}
}
