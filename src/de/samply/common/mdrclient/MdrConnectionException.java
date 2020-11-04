
package de.samply.common.mdrclient;

/**
 * A connection exception while contacting the MDR.
 *
 * @author diogo
 */
public class MdrConnectionException extends Exception {

  /**
   * Todo.
   */
  private static final long serialVersionUID = 4081507164956643754L;

  /**
   * Create an instance with the given message.
   *
   * @param message the detail message
   */
  public MdrConnectionException(final String message) {
    super(message);
  }

}
