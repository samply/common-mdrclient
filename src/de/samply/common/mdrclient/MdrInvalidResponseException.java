
package de.samply.common.mdrclient;

/**
 * A MDR invalid response exception.
 *
 * @author diogo
 */
public class MdrInvalidResponseException extends Exception {

  /**
   * Todo.
   */
  private static final long serialVersionUID = 4081507164956643754L;

  /**
   * Create an instance with the given message.
   *
   * @param message the detail message
   */
  public MdrInvalidResponseException(final String message) {
    super(message);
  }

}
