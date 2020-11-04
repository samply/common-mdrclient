
package de.samply.common.mdrclient.domain;

/**
 * Enumerator of element types as defined in the MDR.
 *
 * @author diogo
 */
public enum EnumElementType {

  /**
   * A data element.
   */
  DATAELEMENT,
  /**
   * A data element group.
   */
  DATAELEMENTGROUP,
  /**
   * A record.
   *
   * @see Record
   */
  RECORD,
  CATALOGUEGROUP,
  CATALOGUEELEMENT;

}
