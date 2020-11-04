package de.samply.common.mdrclient.lastresort;

import de.samply.common.mdrclient.MdrClient;
import de.samply.common.mdrclient.MdrConnectionException;
import de.samply.common.mdrclient.MdrInvalidResponseException;
import de.samply.common.mdrclient.domain.DataElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import org.apache.commons.lang3.StringUtils;

public class MdrLastResort {


  private static final String MDR_ID_URL_SEPARATOR = ":";
  private Map<MdrIdAndLanguage, String> mdrIdAlternativeDesignationMap = new HashMap<>();
  private MdrClient mdrClient;

  public MdrLastResort(MdrClient mdrClient) {
    this.mdrClient = mdrClient;
  }

  /**
   * Todo.
   * @param alternativeDesignations Todo.
   */
  public void addAlterntiveDesignations(
      Collection<MdrAlternativeDesignation> alternativeDesignations) {

    for (MdrAlternativeDesignation mdrAlternativeDesignation : alternativeDesignations) {

      MdrIdAndLanguage mdrIdAndLanguage = createMdrIdAndLanguage(
          mdrAlternativeDesignation.getMdrId(), mdrAlternativeDesignation.getLanguageCode());
      mdrIdAlternativeDesignationMap
          .put(mdrIdAndLanguage, mdrAlternativeDesignation.getDesignation());

    }

  }

  /**
   * Todo.
   * @param mdrId Todo.
   * @param languageCode Todo.
   * @return Todo.
   */
  private MdrIdAndLanguage createMdrIdAndLanguage(String mdrId, String languageCode) {

    mdrId = getMdrIdCore(mdrId);
    return new MdrIdAndLanguage(mdrId, languageCode);

  }

  /**
   * Todo.
   * @param mdrId Todo.
   * @param languageCode Todo.
   * @return Todo.
   */
  public String getDesignation(String mdrId, String languageCode) {

    String currentDesignation = getCurrentDesignation(mdrId, languageCode);
    String alternativeDesignation = getAlternativeDesignation(mdrId, languageCode);

    return (alternativeDesignation == null) ? currentDesignation : alternativeDesignation;

  }

  private String getMdrIdCore(String mdrId) {

    int matches = StringUtils.countMatches(mdrId, MDR_ID_URL_SEPARATOR);
    if (matches == 4) {

      int index = mdrId.lastIndexOf(MDR_ID_URL_SEPARATOR);
      mdrId = mdrId.substring(0, index);

    }

    return mdrId;
  }

  private String getAlternativeDesignation(String mdrId, String languageCode) {

    mdrId = getMdrIdCore(mdrId);
    MdrIdAndLanguage mdrIdAndLanguage = new MdrIdAndLanguage(mdrId, languageCode);
    return mdrIdAlternativeDesignationMap.get(mdrIdAndLanguage);

  }

  private String getCurrentDesignation(String mdrId, String languageCode) {

    try {
      return getCurrentDesignation_WithoutManagementException(mdrId, languageCode);
    } catch (ExecutionException e) {
      return null;
    } catch (MdrConnectionException e) {
      return null;
    } catch (MdrInvalidResponseException e) {
      return null;
    }

  }

  private String getCurrentDesignation_WithoutManagementException(String mdrId, String languageCode)
      throws ExecutionException, MdrConnectionException, MdrInvalidResponseException {

    DataElement dataElement = mdrClient.getDataElement(mdrId, languageCode);
    return dataElement.getDesignations().get(0).getDesignation();

  }

  private class MdrIdAndLanguage {

    private String mdrId;
    private String languageCode;

    public MdrIdAndLanguage(String mdrId, String languageCode) {
      this.mdrId = mdrId;
      this.languageCode = languageCode;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      MdrIdAndLanguage that = (MdrIdAndLanguage) o;
      return mdrId.equals(that.mdrId)
          && languageCode.equals(that.languageCode);
    }

    @Override
    public int hashCode() {
      return Objects.hash(mdrId, languageCode);
    }

  }


}
