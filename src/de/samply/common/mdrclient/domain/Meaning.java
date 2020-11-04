package de.samply.common.mdrclient.domain;

/**
 * Meaning of a permissible value, as known in the MDR.
 *
 * @author diogo
 * @see PermissibleValue
 */
public class Meaning {

  /**
   * Language of the meaning.
   */
  private String language;
  /**
   * Designation of the meaning.
   */
  private String designation;
  /**
   * Definition of the meaning.
   */
  private String definition;

  /**
   * Get the language of this meaning.
   *
   * @return The language of the meaning
   */
  public final String getLanguage() {
    return language;
  }

  /**
   * Set the language of this meaning.
   *
   * @param language The language of the meaning
   */
  public final void setLanguage(final String language) {
    this.language = language;
  }

  /**
   * Get the designation of this meaning.
   *
   * @return The designation of the meaning
   */
  public final String getDesignation() {
    return designation;
  }

  /**
   * Set the designation of this meaning.
   *
   * @param designation The designation of the meaning
   */
  public final void setDesignation(final String designation) {
    this.designation = designation;
  }

  /**
   * Get the definition of this meaning.
   *
   * @return The definition of the meaning
   */
  public final String getDefinition() {
    return definition;
  }

  /**
   * Set the definition of this meaning.
   *
   * @param definition The definition of the meaning
   */
  public final void setDefinition(final String definition) {
    this.definition = definition;
  }

}
