package com.github.bordertech.buildtools.plugins;

/**
 * Exceptions that happen during badge generation need to be caught and logged.
 *
 * @author Rick Brown
 * @since 1.0.0
 */
public class BadgerException extends IllegalStateException {

	/**
	 * Create an instance of this exception with the message to log.
	 * @param message The message explaining the exception.
	 */
	public BadgerException(final String message) {
		super(message);
	}

	/**
	 * Create an instance of this exception with the message to log.
	 * @param message The message explaining the exception.
	 * @param t The original cause of this exception.
	 */
	public BadgerException(final String message, final Throwable t) {
		super(message, t);
	}
}
