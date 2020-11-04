
package de.samply.common.mdrclient.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Catalogue code.
 *
 * @author diogo
 */
public class Code {

  /**
   * Catalogue code URN.
   */
  private Identification identification;

  /**
   * Designation of the catalogue code.
   */
  private List<Designation> designations = new ArrayList<Designation>();

  /**
   * Sub-codes of this data element.
   */
  private List<Object> subCodes = new ArrayList<Object>();

  /**
   * Identifying code of the catalogue code.
   */
  private String code;

  /**
   * Whether the code is valid (i.e. selectable) for the requested catalogue
   */
  private Boolean isValid;

  /** Todo.
   * @return The identification
   */
  public final Identification getIdentification() {
    return identification;
  }

  /**Todo.
   * @param identification The identification
   */
  public final void setIdentification(final Identification identification) {
    this.identification = identification;
  }

  /**Todo.
   * @return The designations
   */
  public final List<Designation> getDesignations() {
    return designations;
  }

  /**Todo.
   * @param designations The designations
   */
  public final void setDesignations(final List<Designation> designations) {
    this.designations = designations;
  }

  /**Todo.
   * @return The subCodes
   */
  public final List<Object> getSubCodes() {
    return subCodes;
  }

  /**Todo.
   * @param subCodes The subCodes
   */
  public final void setSubCodes(final List<Object> subCodes) {
    this.subCodes = subCodes;
  }

  /**Todo.
   * @return The code
   */
  public final String getCode() {
    return code;
  }

  /**Todo.
   * @param code The code
   */
  public final void setCode(final String code) {
    this.code = code;
  }

  /**Todo.
   * @return The isValid
   */
  public final Boolean getIsValid() {
    return isValid;
  }

  /**Todo.
   * @param isValid The isValid
   */
  public final void setIsValid(final Boolean isValid) {
    this.isValid = isValid;
  }

}
