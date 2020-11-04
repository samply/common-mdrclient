
package de.samply.common.mdrclient.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Key-value pairs as defined in the MDR.
 *
 * @author diogo
 */
public class Slot {

  /**
   * The key.
   */
  @SerializedName("slot_name")
  private String slotName;
  /**
   * The value.
   */
  @SerializedName("slot_value")
  private String slotValue;

  /**
   * Get the slot name (i.e. key).
   *
   * @return The slot name
   */
  public final String getSlotName() {
    return slotName;
  }

  /**
   * Set the slot name (i.e. key).
   *
   * @param slotName The slot name
   */
  public final void setSlotName(final String slotName) {
    this.slotName = slotName;
  }

  /**
   * Get the slot value.
   *
   * @return The slot value
   */
  public final String getSlotValue() {
    return slotValue;
  }

  /**
   * Set the slot value.
   *
   * @param slotValue The slot value
   */
  public final void setSlotValue(final String slotValue) {
    this.slotValue = slotValue;
  }

}
