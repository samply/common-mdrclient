
package de.samply.common.mdrclient.domain;

/**
 * Identification of the catalogue code.
 *
 * @author diogo
 */
public class Identification {

  /**
   * URN of the catalogue code.
   */
  private String urn;

  /**
   * Status of the dataelement.
   */
  private EnumIdentificationStatus status;

  /**Todo.
   * @return The urn
   */
  public final String getUrn() {
    return urn;
  }

  /**Todo.
   * @param urn The urn
   */
  public final void setUrn(final String urn) {
    this.urn = urn;
  }

  /**Todo.
   * @return the status
   */
  public EnumIdentificationStatus getStatus() {
    return status;
  }

  /**Todo.
   * @param status the status to set
   */
  public void setStatus(EnumIdentificationStatus status) {
    this.status = status;
  }

}
