
package de.samply.common.mdrclient.domain;

/**
 * Error message from a data element.
 *
 * @author diogo
 */
public class ErrorMessage {

  /**
   * The language of the error message.
   */
  private String language;
  /**
   * The designation of an error message.
   */
  private String designation;
  /**
   * The definition of an error message.
   */
  private String definition;

  /**
   * Get the language of this error message.
   *
   * @return The language of the error message
   */
  public final String getLanguage() {
    return language;
  }

  /**
   * Set the language of this error message.
   *
   * @param language The language of the error message
   */
  public final void setLanguage(final String language) {
    this.language = language;
  }

  /**
   * Get the designation of this error message.
   *
   * @return The designation of the error message
   */
  public final String getDesignation() {
    return designation;
  }

  /**
   * Set the designation of this error message.
   *
   * @param designation The designation o the error message
   */
  public final void setDesignation(final String designation) {
    this.designation = designation;
  }

  /**
   * Get the definition of this error message.
   *
   * @return The definition of the error message.
   */
  public final String getDefinition() {
    return definition;
  }

  /**
   * Set the definition of this error message.
   *
   * @param definition The definition of the error message
   */
  public final void setDefinition(final String definition) {
    this.definition = definition;
  }

}
