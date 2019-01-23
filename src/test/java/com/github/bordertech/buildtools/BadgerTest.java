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
	public void testGetOutputFile() {
		System.out.println("testGetOutputFile");
		File outputDir = getOutputDir();
		Badger badger = new Badger();
		badger.setOutputDir(outputDir);
		File inputFile = new File("checkstyle-result.xml");
		File outputFile = badger.getOutputFile(inputFile);
		String expected = "checkstyle-result.svg";
		String actual = outputFile.getName();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetOutputFileBadlyNamed() {
		System.out.println("testGetOutputFileBadlyNamed");
		File outputDir = getOutputDir();
		Badger badger = new Badger();
		badger.setOutputDir(outputDir);
		File inputFile = new File("checkstyle-result.xml.xml");
		File outputFile = badger.getOutputFile(inputFile);
		String expected = "checkstyle-result.xml.svg";
		String actual = outputFile.getName();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetOutputFileNoExtension() {
		System.out.println("testGetOutputFileNoExtension");
		File outputDir = getOutputDir();
		Badger badger = new Badger();
		badger.setOutputDir(outputDir);
		File inputFile = new File("checkstyle-result");
		File outputFile = badger.getOutputFile(inputFile);
		String expected = "checkstyle-result.svg";
		String actual = outputFile.getName();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetOutputFileWrongExtension() {
		System.out.println("testGetOutputFileWrongExtension");
		File outputDir = getOutputDir();
		Badger badger = new Badger();
		badger.setOutputDir(outputDir);
		File inputFile = new File("checkstyle-result.rdf");
		File outputFile = badger.getOutputFile(inputFile);
		String expected = "checkstyle-result.rdf.svg";
		String actual = outputFile.getName();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testExecuteCheckstylePerfect() {
		System.out.println("testExecuteCheckstylePerfect");
		File[] files = new File[]{ getInputFile("/checkstyle-result-zero.xml") };
		String[] expectedResults = new String[] { "E:0 W:0 I:0" };
		boolean passed = executeTestHelper(files, expectedResults);
		Assert.assertTrue(passed);
	}

	@Test
	public void testExecutePMDPerfect() {
		System.out.println("testExecutePMDPerfect");
		File[] files = new File[]{ getInputFile("/pmd-result-zero.xml") };
		String[] expectedResults = new String[] { "B:0 C:0 M:0 m:0 I:0" };
		boolean passed = executeTestHelper(files, expectedResults);
		Assert.assertTrue(passed);
	}

	@Test
	public void testExecuteMultiple() {
		System.out.println("testExecuteMultiple");
		File[] files = new File[]{ getInputFile("/checkstyle-result-zero.xml"), getInputFile("/pmd-result-zero.xml") };
		String[] expectedResults = new String[] { "E:0 W:0 I:0", "B:0 C:0 M:0 m:0 I:0" };
		boolean passed = executeTestHelper(files, expectedResults);
		Assert.assertTrue(passed);
	}

	@Test
	public void testExecuteCheckstyleWithIssues() {
		System.out.println("testExecuteCheckstyleWithIssues");
		File[] files = new File[]{ getInputFile("/checkstyle-result-issues.xml") };
		String[] expectedResults = new String[] { "E:1 W:1 I:0" };
		boolean passed = executeTestHelper(files, expectedResults);
		Assert.assertTrue(passed);
	}

	@Test
	public void testExecutePMDWithIssues() {
		System.out.println("testExecutePMDWithIssues");
		File[] files = new File[]{ getInputFile("/pmd-result-issues.xml") };
		String[] expectedResults = new String[] { "B:0 C:1 M:0 m:0 I:0" };
		boolean passed = executeTestHelper(files, expectedResults);
		Assert.assertTrue(passed);
	}

	@Test
	public void testExecuteFindBugsWithIssues() {
		System.out.println("testExecuteFindBugsWithIssues");
		File[] files = new File[]{ getInputFile("/findbugsXml-issues.xml") };
		String[] expectedResults = new String[] { "H:2 M:2 L:0" };
		boolean passed = executeTestHelper(files, expectedResults);
		Assert.assertTrue(passed);
	}

	/**
	 * Asserts that, based on given input files the expected output files:
	 * 1. exist
	 * 2. Contain expected content
	 *
	 * @param reports An array of valid input files.
	 * @param expectedResults A corresponding array of content to search for within the output files.
	 * @return true if the tests passed (really just to keep codacy happy).
	 */
	private boolean executeTestHelper(final File[] reports, final String[] expectedResults) {
		boolean result = true;
		File outputDir = getOutputDir();
		Badger badger = new Badger();
		badger.setOutputDir(outputDir);

		badger.setInputXml(reports);
		badger.execute();

		for (int i = 0; i < reports.length; i++) {
			File file = reports[i];
			String expected = expectedResults[i];
			File svg = badger.getOutputFile(file);
			boolean exists = svg.exists();
			boolean valid = contains(svg, expected);
			Assert.assertTrue("File should exist " + svg.getAbsolutePath(), exists);
			Assert.assertTrue("File should contain " + expected, valid);
			result = result && exists && valid;
		}
		return result;
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
					scanner.close();
					return true;
				}
			}
			scanner.close();
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
