package com.github.bordertech.buildtools;

import com.github.bordertech.buildtools.plugins.BadgerException;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Generates shiny badges representing the status of various QA checks.
 * Currently supports:
 * - Findbugs
 * - PMD
 * - Checkstyle
 * - JaCoCo
 *
 * @author Rick Brown
 * @since 1.0.0
 */
public class Badger {

	private static final Logger LOGGER = Logger.getLogger(Badger.class.getName());

	private File[] inputFiles = null;

	private File outputDir = null;

	/**
	 * Paths to XML reports to transform into badges.
	 *
	 * @param paths the input XML files.
	 */
	public void setInputXml(final File[] paths) {
		// No point validating here, the input files don't need to exist until execution.
		this.inputFiles = paths;
	}

	/**
	 * The path to the directory where the badges will be created.
	 * Existing files will be overwritten.
	 * @param path the output directory.
	 */
	public void setOutputDir(final File path) {
		this.outputDir = path;
	}

	/**
	 * Generate badges configured in the current state of this instance.
	 * Assumes the inputs and outputs have been set.
	 * @throws BadgerException if something goes wrong to report to the invoker.
	 */
	public void execute() throws BadgerException {
		if (inputFiles != null && inputFiles.length > 0) {
			checkOutputDir();
			for (File inputXml : inputFiles) {
				if (inputXml != null && inputXml.canRead() && inputXml.isFile()) {
					File output = getOutputFile(inputXml);
					transform(inputXml, output);
				} else {
					LOGGER.log(Level.WARNING, "Badger cannot read input XML file {0}", inputXml);
				}
			}
		} else {
			LOGGER.log(Level.INFO, "No reports found, not building badges");
		}
	}

	/**
	 * Ensure a valid output directory has been specified and exists - attempt to create it if necessary.
	 */
	private void checkOutputDir() {
		if (outputDir != null) {
			if (outputDir.exists()) {
				if (!outputDir.isDirectory()) {
					throw new BadgerException("Could not find directory" + outputDir);
				}
			} else if (!outputDir.mkdirs()) {
				throw new BadgerException("Could not create directory" + outputDir);
			}
		} else {
			LOGGER.log(Level.WARNING, "Badger has not been given an output directory");
		}
	}

	/**
	 * Given a particular input file determines where the resulting output (the badge) should be written.
	 * @param inputXml The input file, for example checkstyleResult.xml
	 * @return The output file, for example /path/to.outdir/checkstyleResult.svg
	 */
	public File getOutputFile(final File inputXml) {
		String name = inputXml.getName();
		name = name.replaceAll("(?i)\\.xml$", ".svg");
		File outputFile = new File(outputDir, name);
		return outputFile;
	}

	/**
	 * Performs the transformation from a QA report in XML format to an SVG badge.
	 * @param inputXml The QA report to transform.
	 * @param outputPath The path where the svg file should be created.
	 */
	private static void transform(final File inputXml, final File outputPath) {
		LOGGER.log(Level.INFO, "Transforming {0} to {1}", new Object[]{inputXml, outputPath});
		TransformerFactory tFactory = TransformerFactory.newInstance();
		InputStream xsl = Badger.class.getResourceAsStream("/bordertech/badges.xsl");
		StreamSource xmlInputFile = new StreamSource(inputXml);
		StreamSource xslTransformationFile = new StreamSource(xsl);
		StreamResult xmlOutputFile = new StreamResult(outputPath);
		try {
			Transformer transformer = tFactory.newTransformer(xslTransformationFile);
			transformer.transform(xmlInputFile, xmlOutputFile);

		} catch (TransformerConfigurationException ex) {
			throw new BadgerException(ex.getMessage(), ex);
		} catch (TransformerException ex) {
			throw new BadgerException(ex.getMessage(), ex);
		}
	}

}
