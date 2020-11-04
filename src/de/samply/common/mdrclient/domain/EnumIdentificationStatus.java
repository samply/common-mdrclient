
package de.samply.common.mdrclient.domain;

/**
 * Enumerator of data type status as defined in the MDR.
 *
 * @author diogo
 */
public enum EnumIdentificationStatus {

  /**
   * The Element is not yet released.
   */
  DRAFT,
  /**
   * The current version of the dataelement.
   */
  RELEASED,
  /**
   * A previous version of the dataelement.
   */
  OUTDATED;

}
