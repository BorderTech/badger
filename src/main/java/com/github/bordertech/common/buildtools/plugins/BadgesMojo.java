package com.github.bordertech.common.buildtools.plugins;

import com.github.bordertech.common.buildtools.Badges;
import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Generates shiny badges as a Maven plugin.
 * It is expected that the badges will be created as part of the mvn site;
 * this plugin will therefore need to run later, i.e. mvn post-site or mvn site-deploy.
 *
 * @author Rick Brown
 * @since 1.0.4
 * @goal badges
 * @phase post-site
 */
public class BadgesMojo extends AbstractMojo {

	/**
	 * The XML reports that will be transformed to status badges.
	 * @parameter property="inputfiles"
	 */
	private File[] inputFiles;

	/**
	 * The directory where status badges will be created.
	 * @parameter property="outputdir"
	 */
	private File outputDir;

	/**
	 * Optionally skip execution.
	 * @parameter property="skip"
	 */
	private boolean skip;

	@Override
	public void execute() throws MojoExecutionException {
		if (skip) {
			getLog().info("Badges skipping execution.");
			return;
		}
		try {
			Badges badges = new Badges();
			badges.setOutputDir(outputDir);
			badges.setInputXml(inputFiles);
			badges.execute();
		} catch (BadgesException ex) {
			throw new MojoExecutionException(ex.getMessage(), ex);
		}
	}
}
