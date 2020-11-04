
package de.samply.common.mdrclient.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A permissible value as know in the MDR. It is a value from an enumerated list.
 *
 * @author diogo
 */
public class PermissibleValue {

  /**
   * The value from an enumerated list.
   */
  private String value;

  /**
   * The meanings of the permissible value.
   */
  private List<Meaning> meanings = new ArrayList<Meaning>();

  /**
   * Get the value from an enumerated list.
   *
   * @return The value from an enumerated list
   */
  public final String getValue() {
    return value;
  }

  /**
   * Set the enumeration value.
   *
   * @param value The value from an enumerated list
   */
  public final void setValue(final String value) {
    this.value = value;
  }

  /**
   * Get the meanings of this permissable value.
   *
   * @return The meanings of the permissible value
   */
  public final List<Meaning> getMeanings() {
    return meanings;
  }

  /**
   * Set the meanings of this permissable value.
   *
   * @param meanings The meanings of the permissible value
   */
  public final void setMeanings(final List<Meaning> meanings) {
    this.meanings = meanings;
  }

}
