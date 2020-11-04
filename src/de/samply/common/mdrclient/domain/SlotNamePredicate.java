package de.samply.common.mdrclient.domain;

import com.google.common.base.Predicate;

public class SlotNamePredicate implements Predicate<Slot> {

  private String name;

  public SlotNamePredicate(final String name) {
    this.name = name;
  }

  @Override
  public boolean apply(Slot slot) {
    return name.equals(slot.getSlotName());
  }
}
