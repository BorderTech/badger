package com.github.bordertech.buildtools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for Badger project.
 * @author Rick Brown
 * @since 1.0.0
 */
public class BadgerTest {

	/**
	 * Create a temporary folder to write badger artefacts generated as part of tests.
	 */
	@Rule
	public final TemporaryFolder scratchDir = new TemporaryFolder();

	@Test
	public void textExecuteCheckstylePerfect() {
		System.out.println("textExecuteCheckstylePerfect");
		File[] files = new File[]{ getInputFile("/checkstyle-result-zero.xml") };
		String[] expectedResults = new String[] { "E:0 W:0 I:0" };
		executeTestHelper(files, expectedResults);
	}

	@Test
	public void textExecutePMDPerfect() {
		System.out.println("textExecutePMDPerfect");
		File[] files = new File[]{ getInputFile("/pmd-result-zero.xml") };
		String[] expectedResults = new String[] { "B:0 C:0 M:0 m:0 I:0" };
		executeTestHelper(files, expectedResults);
	}

	@Test
	public void textExecuteMultiple() {
		System.out.println("textExecuteMultiple");
		File[] files = new File[]{ getInputFile("/checkstyle-result-zero.xml"), getInputFile("/pmd-result-zero.xml") };
		String[] expectedResults = new String[] { "E:0 W:0 I:0", "B:0 C:0 M:0 m:0 I:0" };
		executeTestHelper(files, expectedResults);
	}

	@Test
	public void textExecuteCheckstyleWithIssues() {
		System.out.println("textExecuteCheckstyleWithIssues");
		File[] files = new File[]{ getInputFile("/checkstyle-result-issues.xml") };
		String[] expectedResults = new String[] { "E:1 W:1 I:0" };
		executeTestHelper(files, expectedResults);
	}

	@Test
	public void textExecutePMDWithIssues() {
		System.out.println("textExecutePMDWithIssues");
		File[] files = new File[]{ getInputFile("/pmd-result-issues.xml") };
		String[] expectedResults = new String[] { "B:0 C:1 M:0 m:0 I:0" };
		executeTestHelper(files, expectedResults);
	}

	@Test
	public void textExecuteFindBugsWithIssues() {
		System.out.println("textExecuteFindBugsWithIssues");
		File[] files = new File[]{ getInputFile("/findbugsXml-issues.xml") };
		String[] expectedResults = new String[] { "H:2 M:2 L:0" };
		executeTestHelper(files, expectedResults);
	}

	/**
	 * Asserts that, based on given input files the expected output files:
	 * 1. exist
	 * 2. Contain expected content
	 *
	 * @param reports An array of valid input files.
	 * @param expectedResults A corresponding array of content to search for within the output files.
	 */
	private void executeTestHelper(final File[] reports, final String[] expectedResults) {
		File outputDir = getOutputDir();
		Badger badger = new Badger();
		badger.setOutputDir(outputDir);

		badger.setInputXml(reports);
		badger.execute();

		for (int i = 0; i < reports.length; i++) {
			File file = reports[i];
			String expected = expectedResults[i];
			File svg = badger.getOutputFile(file);
			Assert.assertTrue(svg.exists());
			Assert.assertTrue(contains(svg, expected));
		}
	}

	/**
	 * Get an output directory for use when writing to the file system during tests.
	 * @return A temporary directory.
	 */
	public File getOutputDir() {
		try {
			return scratchDir.newFolder();
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
		return null;
	}

	/**
	 * Determine if this text file contains a given string.
	 * @param file The text file to search in.
	 * @param search The term to search for,
	 * @return true if the search term is found in the text file.
	 */
	public static boolean contains(final File file, final String search) {
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains(search)) {
					return true;
				}
			}
		} catch (FileNotFoundException ex) {
			Assert.fail(ex.getMessage());
		}
		return false;
	}

	/**
	 * Load a resource from the ClassPath and return it as a File.
	 * @param resourcePath The path to the resource on the ClassPath.
	 * @return A File instance representing the resource.
	 */
	public static File getInputFile(final String resourcePath) {
		try {
			URL fileResource = BadgerTest.class.getResource(resourcePath);
			File file = new File(fileResource.toURI());
			return file;
		} catch (URISyntaxException ex) {
			Assert.fail(ex.getMessage());
		}
		return null;
	}
}
