
package de.samply.common.mdrclient.domain;

/**
 * MDR designation of a data element.
 *
 * @author diogo
 */
public class Designation {

  /**
   * Language code of the designation.
   */
  private String language;

  /**
   * Designation of a data element.
   */
  private String designation;

  /**
   * Definition of a data element.
   */
  private String definition;

  /**
   * Get the language codes of this designation.
   *
   * @return the language code of this designation
   */
  public final String getLanguage() {
    return language;
  }

  /**
   * Set the language code of this designation.
   *
   * @param language the language code of this designation
   */
  public final void setLanguage(final String language) {
    this.language = language;
  }

  /**
   * Get the designation.
   *
   * @return the designation.
   */
  public final String getDesignation() {
    return designation;
  }

  /**
   * Set the designation.
   *
   * @param designation the data element designation.
   */
  public final void setDesignation(final String designation) {
    this.designation = designation;
  }

  /**
   * Get the data element definition.
   *
   * @return the data element definition.
   */
  public final String getDefinition() {
    return definition;
  }

  /**
   * Set the data element definition.
   *
   * @param definition the data element definition.
   */
  public final void setDefinition(final String definition) {
    this.definition = definition;
  }

}
