package com.github.bordertech.common.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rick Brown on 2017-06-08.
 */
public class StringUtilsTest {

	@Test
	public void testAsciifySymbol() {
		String in = "\u02C6 hat \u02DC tilde";
		String out = "^ hat ~ tilde";
		Assert.assertEquals("Translate symbols", out, StringUtils.asciifySymbol(in));
	}

	@Test
	public void testAsciifySymbolWithQuotes() {
		String in = "\u201Cfancy quotes\u201D and \u201E";
		String out = "\"fancy quotes\" and \"";
		Assert.assertEquals("Quotes not handled correctly", out, StringUtils.asciifySymbol(in));
	}

	@Test
	public void testAsciifySymbolWithSingleQuotes() {
		String in = "\u2018fancy single quotes\u2019";
		String out = "'fancy single quotes'";
		Assert.assertEquals("Single quotes not handled correctly", out, StringUtils.asciifySymbol(in));
	}

	@Test
	public void testAsciifySymbolWithNbspDashesAndEllipsis() {
		String in = "\u8230\u2013\u2013\u2013\u8230\u00A0\u8230\u2014\u2014\u2014\u8230";
		String out = "...---... ...---...";
		Assert.assertEquals("ellipsis endash emdash nbsp", out, StringUtils.asciifySymbol(in));
	}

	@Test
	public void testAsciifySymbolWithFractions() {
		String in = "Glass \u00BD full. \u00BC + \u00BE = 1";
		String out = "Glass 1/2 full. 1/4 + 3/4 = 1";
		Assert.assertEquals("ellipsis endash emdash nbsp", out, StringUtils.asciifySymbol(in));
	}

	@Test
	public void testAsciifySymbolWithAngleBrackets() {
		String in = "\u2039script src='malicious.js'\u203A\u2039/script\u203A";
		String out = "<script src='malicious.js'></script>";
		Assert.assertEquals("LT and GT", out, StringUtils.asciifySymbol(in));
	}

	@Test
	public void testAsciifySymbolWithMarks() {
		String in = "\u00A9 \u00AE \u2122";
		String out = "(C) (R) (TM)";
		Assert.assertEquals("Translate mark symbols", out, StringUtils.asciifySymbol(in));
	}
}
