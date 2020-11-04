
package de.samply.common.mdrclient.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A catalogue.
 *
 * @author diogo
 */
public class Catalogue {

  /**
   * Codes from the catalogue.
   */
  private List<Code> codes = new ArrayList<Code>();

  /**
   * The root element and its subcodes.
   */
  private Root root;

  /** Todo.
   * @return The codes
   */
  public final List<Code> getCodes() {
    return codes;
  }

  /** Todo.
   * @param codes The codes
   */
  public final void setCodes(final List<Code> codes) {
    this.codes = codes;
  }

  /** Todo.
   * @return The root
   */
  public final Root getRoot() {
    return root;
  }

  /** Todo.
   * @param root The root
   */
  public final void setRoot(final Root root) {
    this.root = root;
  }

}
