package de.samply.common.mdrclient.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Concept of a data element.
 */
public class Concept {

  /**
   * The concept system.
   */
  @SerializedName("concept_system")
  private String conceptSystem;

  /**
   * The concept source.
   */
  @SerializedName("concept_source")
  private Integer conceptSource;

  /**
   * The concept version.
   */
  @SerializedName("concept_version")
  private String conceptVersion;

  /**
   * The concept term.
   */
  @SerializedName("concept_term")
  private String conceptTerm;

  /**
   * The concept text.
   */
  @SerializedName("concept_text")
  private String conceptText;

  /**
   * The concept linktype.
   */
  @SerializedName("concept_linktype")
  private String conceptLinktype;

  /**
   * Get the system of the concept.
   *
   * @return The concept system
   */
  public final String getConceptSystem() {
    return conceptSystem;
  }

  /**
   * Set the system of the concept.
   *
   * @param conceptSystem The concept system
   */
  public final void setConceptSystem(final String conceptSystem) {
    this.conceptSystem = conceptSystem;
  }

  /**
   * Get the source of the concept.
   *
   * @return The concept source
   */
  public final Integer getConceptSource() {
    return conceptSource;
  }

  /**
   * Set the source of the concept.
   *
   * @param conceptSource The concept source
   */
  public final void setConceptSource(final Integer conceptSource) {
    this.conceptSource = conceptSource;
  }

  /**
   * Get the version of the concept.
   *
   * @return The concept version
   */
  public final String getConceptVersion() {
    return conceptVersion;
  }

  /**
   * Set the version of the concept.
   *
   * @param conceptVersion The concept version
   */
  public final void setConceptVersion(final String conceptVersion) {
    this.conceptVersion = conceptVersion;
  }

  /**
   * Get the term of the concept.
   *
   * @return The concept term
   */
  public final String getConceptTerm() {
    return conceptTerm;
  }

  /**
   * Set the term of the concept.
   *
   * @param conceptTerm The concept term
   */
  public final void setConceptTerm(final String conceptTerm) {
    this.conceptTerm = conceptTerm;
  }

  /**
   * Get the text of the concept.
   *
   * @return The concept text
   */
  public final String getConceptText() {
    return conceptText;
  }

  /**
   * Set the text of the concept.
   *
   * @param conceptText The concept text
   */
  public final void setConceptText(final String conceptText) {
    this.conceptText = conceptText;
  }

  /**
   * Get the linktype of the concept.
   *
   * @return The concept linktype
   */
  public final String getConceptLinktype() {
    return conceptLinktype;
  }

  /**
   * Set the linktype of the concept.
   *
   * @param conceptLinktype The concept linktype
   */
  public final void setConceptLinktype(final String conceptLinktype) {
    this.conceptLinktype = conceptLinktype;
  }

}
