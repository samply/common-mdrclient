package de.samply.common.mdrclient.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A namespace response from the MDR.
 *
 * @author michael
 */
public class Namespace {

  /**
   * The name of the namespace.
   */
  private String name;

  /**
   * Is the namespace writeable?.
   */
  private boolean writeable;

  /**
   * Designations of the namespace.
   */
  private List<Designation> designations = new ArrayList<Designation>();

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Checks if the namespace is writeable.
   *
   * @return true, if is writeable
   */
  public boolean isWriteable() {
    return writeable;
  }

  /**
   * Sets the writeable.
   *
   * @param writeable the new writeable
   */
  public void setWriteable(boolean writeable) {
    this.writeable = writeable;
  }

  /**
   * Gets the designations.
   *
   * @return the designations
   */
  public List<Designation> getDesignations() {
    return designations;
  }

  /**
   * Sets the designations.
   *
   * @param designations the new designations
   */
  public void setDesignations(List<Designation> designations) {
    this.designations = designations;
  }

}
