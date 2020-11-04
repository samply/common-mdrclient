package de.samply.common.mdrclient.domain;

/**
 * A group of data elements with a common designation.
 *
 * @author diogo
 */
public class Record {

  /**
   * The definition of the record.
   */
  private String definition;
  /**
   * The designation of the record.
   */
  private String designation;
  /**
   * The language of the record.
   */
  private String language;

  /**Todo.
   * @return the definition of the record
   */
  public final String getDefinition() {
    return this.definition;
  }

  /**Todo.
   * @param definition the definition of the record
   */
  public final void setDefinition(final String definition) {
    this.definition = definition;
  }

  /**Todo.
   * @return the designation of the record
   */
  public final String getDesignation() {
    return this.designation;
  }

  /**Todo.
   * @param designation the designation of the record
   */
  public final void setDesignation(final String designation) {
    this.designation = designation;
  }

  /**Todo.
   * @return the language of the record
   */
  public final String getLanguage() {
    return this.language;
  }

  /**Todo.
   * @param language the language of the record
   */
  public final void setLanguage(final String language) {
    this.language = language;
  }
}
