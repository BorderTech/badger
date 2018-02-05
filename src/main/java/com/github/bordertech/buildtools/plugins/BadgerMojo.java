package com.github.bordertech.buildtools.plugins;

import com.github.bordertech.buildtools.Badger;
import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Generates shiny badger as a Maven plugin.
 * It is expected that the badger will be created as part of the mvn site;
 this plugin will therefore need to run later, i.e. mvn post-site or mvn site-deploy.
 *
 * @author Rick Brown
 * @since 1.0.0
 */
@Mojo(name = "badges", defaultPhase = LifecyclePhase.POST_SITE)
public class BadgerMojo extends AbstractMojo {

	/**
	 * The XML reports that will be transformed to status badger.
	 */
	@Parameter(property = "inputfiles")
	private File[] inputFiles;

	/**
	 * The directory where status badger will be created.
	 */
	@Parameter(property = "outputdir")
	private File outputDir;

	/**
	 * Optionally skip execution.
	 */
	@Parameter(property = "skip", defaultValue = "false")
	private boolean skip;

	@Override
	public void execute() throws MojoExecutionException {
		if (skip) {
			getLog().info("Badger skipping execution.");
			return;
		}
		try {
			Badger badger = new Badger();
			badger.setOutputDir(outputDir);
			badger.setInputXml(inputFiles);
			badger.execute();
		} catch (BadgerException ex) {
			throw new MojoExecutionException(ex.getMessage(), ex);
		}
	}
}
