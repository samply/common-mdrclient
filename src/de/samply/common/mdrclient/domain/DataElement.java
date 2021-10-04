package de.samply.common.mdrclient.domain;

import java.util.List;

/**
 * Representation of a whole Dataelement as received by getting the root resource from the MDR.
 */
public class DataElement {

  private Identification identification;
  private List<Slot> slots;
  private List<Designation> designations;
  private List<Concept> concepts;
  private Validations validation;

  public Identification getIdentification() {
    return identification;
  }

  public void setIdentification(Identification identification) {
    this.identification = identification;
  }

  public List<Slot> getSlots() {
    return slots;
  }

  public void setSlots(List<Slot> slots) {
    this.slots = slots;
  }

  public List<Designation> getDesignations() {
    return designations;
  }

  public void setDesignations(List<Designation> designations) {
    this.designations = designations;
  }

  public List<Concept> getConcepts() {
    return concepts;
  }

  public void setConcepts(List<Concept> concepts) {
    this.concepts = concepts;
  }

  public Validations getValidation() {
    return validation;
  }

  public void setValidation(Validations validation) {
    this.validation = validation;
  }
}
