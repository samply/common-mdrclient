package de.samply.common.mdrclient.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * The root element of the tree.
 *
 * @author diogo
 */
public class Root {

  /**
   * The URN of the root element.
   */
  private Identification identification;

  /**
   * The designations of the root element.
   */
  private List<Designation> designations = new ArrayList<Designation>();

  /**
   * THe sub codes of the root element.
   */
  private List<String> subCodes = new ArrayList<String>();

  /**Todo.
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
  public final List<String> getSubCodes() {
    return subCodes;
  }

  /**Todo.
   * @param subCodes The subCodes
   */
  public final void setSubCodes(final List<String> subCodes) {
    this.subCodes = subCodes;
  }

}
