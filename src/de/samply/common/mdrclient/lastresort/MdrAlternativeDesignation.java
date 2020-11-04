package de.samply.common.mdrclient.lastresort;

public class MdrAlternativeDesignation {

  private String mdrId;
  private String designation;
  private String languageCode;


  public String getMdrId() {
    return mdrId;
  }

  public void setMdrId(String mdrId) {
    this.mdrId = mdrId;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}
