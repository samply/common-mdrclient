
package de.samply.common.mdrclient.domain;

import java.util.ArrayList;

/**
 * Designation/definition and the value domain of an MDR data element.
 *
 * @author diogo
 */
public class Definition {

  /**
   * Data element designations.
   */
  private ArrayList<Record> designations;

  /**
   * Data element validations.
   */
  private Validations validation;

  /**
   * Definition of a data element.
   */
  public Definition() {
  }

  /**
   * Get the designations of this data element.
   *
   * @return the designations of this data element.
   */
  public final ArrayList<Record> getDesignations() {
    return designations;
  }

  /**
   * Set the designations of this data element.
   *
   * @param designations the new designations.
   */
  public final void setDesignations(final ArrayList<Record> designations) {
    this.designations = designations;
  }

  /**
   * Get the validations of this data element.
   *
   * @return the validations of this data element.
   */
  public final Validations getValidation() {
    return validation;
  }

  /**
   * Set the validations of this data element.
   *
   * @param validation validation of this data element.
   */
  public final void setValidation(final Validations validation) {
    this.validation = validation;
  }

}
