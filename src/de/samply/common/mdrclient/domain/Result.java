
package de.samply.common.mdrclient.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A result response from the MDR.
 *
 * @author diogo
 */
public class Result {

  /**
   * The id of the result.
   */
  private String id;

  /**
   * The name of the data type.
   */
  private String type;

  /**
   * The cardinal of the result.
   */
  private int order;

  /**
   * The Identification element, containing the urn and the status of the dataelement.
   */
  private Identification identification;

  /**
   * Designations of the result.
   */
  private List<Designation> designations = new ArrayList<Designation>();

  /**
   * Get the id of this result.
   *
   * @return The id
   */
  public final String getId() {
    return id;
  }

  /**
   * Set the id of this result.
   *
   * @param id The id
   */
  public final void setId(final String id) {
    this.id = id;
  }

  /**
   * Get the type of this result.
   *
   * @return The type of this result
   * @see EnumElementType
   */
  public final String getType() {
    return type;
  }

  /**
   * Set the type of this result.
   *
   * @param type The new type
   */
  public final void setType(final String type) {
    this.type = type;
  }

  /**
   * Get the order of this result.
   *
   * @return The order of this result
   */
  public final int getOrder() {
    return order;
  }

  /**
   * Set the order of this result.
   *
   * @param order The new order
   */
  public final void setOrder(final int order) {
    this.order = order;
  }

  /**
   * Get the identification of this result.
   *
   * @return The identification of this result
   */
  public Identification getIdentification() {
    return identification;
  }

  /**
   * Set the identification of this result.
   *
   * @param identification The new identification
   */
  public void setIdentification(Identification identification) {
    this.identification = identification;
  }

  /**
   * Get the designations of this result.
   *
   * @return The designations of this result
   */
  public final List<Designation> getDesignations() {
    return designations;
  }

  /**
   * Set the designations of this result.
   *
   * @param designations The new designations
   */
  public final void setDesignations(final List<Designation> designations) {
    this.designations = designations;
  }


  /**
   * Compares two result objects based on their type.
   */
  public class CustomComparator implements Comparator<Result> {

    @Override
    public int compare(Result o1, Result o2) {
      return o2.getType().compareTo(o1.getType());
    }
  }

}
