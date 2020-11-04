
package de.samply.common.mdrclient.domain;

/**
 * Label of a data element.
 *
 * @author diogo
 */
public class Label {

  /**
   * Language of the label.
   */
  private String language;
  /**
   * Designation of the label.
   */
  private String designation;
  /**
   * Definition of the label.
   */
  private String definition;

  /**
   * Get the language of this label.
   *
   * @return The language of the label
   */
  public final String getLanguage() {
    return language;
  }

  /**
   * Set the language of this label.
   *
   * @param language The language of the label
   */
  public final void setLanguage(final String language) {
    this.language = language;
  }

  /**
   * Get the designation of this label.
   *
   * @return The designation of the label
   */
  public final String getDesignation() {
    return designation;
  }

  /**
   * Set the designation of this label.
   *
   * @param designation The designation of the label
   */
  public final void setDesignation(final String designation) {
    this.designation = designation;
  }

  /**
   * Get the definition of this label.
   *
   * @return The definition of the label
   */
  public final String getDefinition() {
    return definition;
  }

  /**
   * Set the definition of this label.
   *
   * @param definition The definition of the label
   */
  public final void setDefinition(final String definition) {
    this.definition = definition;
  }

}
