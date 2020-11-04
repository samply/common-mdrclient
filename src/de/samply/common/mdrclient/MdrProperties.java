
package de.samply.common.mdrclient;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * MDR properties with locale-specific objects.
 *
 * @author diogo
 */
public final class MdrProperties {

  /**
   * MDR properties bundle name.
   */
  private static final String BUNDLE_NAME = "de.samply.common.mdrclient.mdr"; //$NON-NLS-1$

  /**
   * Resource bundles containing locale-specific objects.
   */
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  /**
   * Prevent instantiation.
   */
  private MdrProperties() {

  }

  /**
   * Get a resource bundle value.
   *
   * @param key a property key
   * @return the resource bundle value for the given key
   */
  public static String getString(final String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}
