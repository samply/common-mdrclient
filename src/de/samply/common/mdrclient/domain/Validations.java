
package de.samply.common.mdrclient.domain;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Validations of a data element.
 */
public class Validations {

  /**
   * Data type of the data element.
   */
  private String datatype;

  /**
   * Format of a data validation.
   */

  private String format;

  /**
   * Description of the data element.
   */

  private String description;

  /**
   * Error message of the validation. The error messages to show in case the validation failed.
   */

  private List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();

  /**
   * The maximum character quantity.
   */
  @SerializedName("maximum_character_quantity")
  private String maximumCharacterQuantity;

  /**
   * The value domain type of the data element.
   */
  @SerializedName("value_domain_type")
  private String valueDomainType;

  /**
   * The validation type.
   *
   * @see EnumValidationType
   */
  @SerializedName("validation_type")
  private String validationType;

  /**
   * Validation description data.
   */
  @SerializedName("validation_data")
  private String validationData;

  /**
   * Definition and designation of a data element validation.
   */

  private List<Object> meanings = new ArrayList<Object>();

  /**
   * Permissible values of the validation.
   */
  @SerializedName("permissible_values")
  private List<PermissibleValue> permissibleValues = new ArrayList<PermissibleValue>();

  /**
   * The unit of measure.
   */
  @SerializedName("unit_of_measure")
  private String unitOfMeasure;

  /**
   * Get the data type of the validation.
   *
   * @return The data type
   */
  public final String getDatatype() {
    return datatype;
  }

  /**
   * Set the data type.
   *
   * @param datatype The data type
   */
  public final void setDatatype(final String datatype) {
    this.datatype = datatype;
  }

  /**Todo.
   * @return The format
   */
  public final String getFormat() {
    return format;
  }

  /**
   * Get the validation format.
   *
   * @param format The format
   */
  public final void setFormat(final String format) {
    this.format = format;
  }

  /**
   * Set the description.
   *
   * @return The description
   */
  public final String getDescription() {
    return description;
  }

  /**Todo.
   * @param description The description
   */
  public final void setDescription(final String description) {
    this.description = description;
  }

  /**Todo.
   * @return The errorMessages
   */
  public final List<ErrorMessage> getErrorMessages() {
    return errorMessages;
  }

  /**Todo.
   * @param errorMessages The errorMessages
   */
  public final void setErrorMessages(final List<ErrorMessage> errorMessages) {
    this.errorMessages = errorMessages;
  }

  /**Todo.
   * @return The maximumCharacterQuantity
   */
  public final String getMaximumCharacterQuantity() {
    return maximumCharacterQuantity;
  }

  /**Todo.
   * @param maximumCharacterQuantity The maximum_character_quantity
   */
  public final void setMaximumCharacterQuantity(final String maximumCharacterQuantity) {
    this.maximumCharacterQuantity = maximumCharacterQuantity;
  }

  /**Todo.
   * @return The valueDomainType
   */
  public final String getValueDomainType() {
    return valueDomainType;
  }

  /**Todo.
   * @param valueDomainType The value_domain_type
   */
  public final void setValueDomainType(final String valueDomainType) {
    this.valueDomainType = valueDomainType;
  }

  /**Todo.
   * @return The validationType
   */
  public final String getValidationType() {
    return validationType;
  }

  /**Todo.
   * @param validationType The validation_type
   */
  public final void setValidationType(final String validationType) {
    this.validationType = validationType;
  }

  /**Todo.
   * @return The validationData
   */
  public final String getValidationData() {
    return validationData;
  }

  /**Todo.
   * @param validationData The validation_data
   */
  public final void setValidationData(final String validationData) {
    this.validationData = validationData;
  }

  /**Todo.
   * @return The meanings
   */
  public final List<Object> getMeanings() {
    return meanings;
  }

  /**Todo.
   * @param meanings The meanings
   */
  public final void setMeanings(final List<Object> meanings) {
    this.meanings = meanings;
  }

  /**Todo.
   * @return the permissable values of a data element validation
   */
  public final List<PermissibleValue> getPermissibleValues() {
    return permissibleValues;
  }

  /**Todo.
   * @param permissibleValues the permissable values of a data element validation
   */
  public final void setPermissibleValues(final List<PermissibleValue> permissibleValues) {
    this.permissibleValues = permissibleValues;
  }

  public String getUnitOfMeasure() {
    return unitOfMeasure;
  }

  public void setUnitOfMeasure(String unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
  }
}
