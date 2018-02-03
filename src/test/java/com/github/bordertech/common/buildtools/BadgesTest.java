package com.github.bordertech.common.buildtools;

import java.io.File;
import org.junit.Test;

/**
 *
 * @author Rick Brown
 */
public class BadgesTest {

	public BadgesTest() {
	}

	@Test
	public void testMain() {
		System.out.println("main");
		Badges badges = new Badges();
		badges.setOutputDir(new File(""));
		File[] files = new File[]{ new File("") };
		badges.setInputXml(files);
		badges.execute();
	}

}
