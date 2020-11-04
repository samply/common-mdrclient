
package de.samply.common.mdrclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import de.samply.common.mdrclient.domain.Catalogue;
import de.samply.common.mdrclient.domain.Code;
import de.samply.common.mdrclient.domain.DataElement;
import de.samply.common.mdrclient.domain.Definition;
import de.samply.common.mdrclient.domain.EnumElementType;
import de.samply.common.mdrclient.domain.Label;
import de.samply.common.mdrclient.domain.Namespace;
import de.samply.common.mdrclient.domain.RecordDefinition;
import de.samply.common.mdrclient.domain.Result;
import de.samply.common.mdrclient.domain.ResultList;
import de.samply.common.mdrclient.domain.Slot;
import de.samply.common.mdrclient.domain.Validations;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MDR RESTful interface client.
 */
public class MdrClient {

  /**
   * Path separator.
   */
  public static final String PATH_SEPARATOR = "/";
  /**
   * MDR URL property name.
   */
  private static final String MDR_URL = MdrProperties.getString("MdrClient.url"); //$NON-NLS-1$
  /**
   * MDR proxy port property name.
   */
  private static final String MDR_PROXY_PORT = MdrProperties
      .getString("MdrClient.proxy.port"); //$NON-NLS-1$
  /**
   * MDR proxy IP property name.
   */
  private static final String MDR_PROXY_IP = MdrProperties
      .getString("MdrClient.proxy.ip"); //$NON-NLS-1$
  /**
   * HTTP proxy port property name.
   */
  private static final String HTTP_PROXY_PORT_PROPERTY = "http.proxyPort"; //$NON-NLS-1$
  /**
   * HTTP proxy host property name.
   */
  private static final String HTTP_PROXY_HOST_PROPERTY = "http.proxyHost"; //$NON-NLS-1$
  /**
   * Namespaces MDR REST path.
   */
  private static final String NAMESPACES_PATH = "namespaces"; //$NON-NLS-1$

  /**
   * Data elements MDR REST path.
   */
  private static final String DATA_ELEMENTS_PATH = "dataelements"; //$NON-NLS-1$

  /**
   * Validations MDR REST path.
   */
  private static final String VALIDATIONS_PATH = "validations"; //$NON-NLS-1$

  /**
   * Catalogue path from a data element.
   */
  private static final String CATALOGUE_PATH = "catalog"; //$NON-NLS-1$

  /**
   * Catalogues MDR REST path.
   */
  private static final String CATALOGUES_PATH = "catalogs"; //$NON-NLS-1$

  /**
   * Catalogues MDR REST path.
   */
  private static final String CODES_PATH = "codes"; //$NON-NLS-1$

  /**
   * UI Model MDR REST path.
   */
  private static final String DESIGNATION_PATH = "uimodel"; //$NON-NLS-1$

  /**
   * Members MDR REST path.
   */
  private static final String MEMBERS = "members";

  /**
   * Data element groups MDR REST path.
   */
  private static final String DATAELEMENTGROUPS = "dataelementgroups";

  /**
   * Records MDR REST path.
   */
  private static final String RECORDS = "records";

  /**
   * Labels MDR REST path.
   */
  private static final String LABELS = "labels";

  /**
   * Search MDR REST path.
   */
  private static final String SEARCH = "search";

  /**
   * Local search MDR REST path.
   */
  private static final String SEARCH_LOCAL = "local";

  /**
   * Slots MDR REST path.
   */
  private static final String SLOTS = "slots";

  /**
   * Representations MDR REST path.
   */
  private static final String REPRESENTATIONS = "representations";

  /**
   * Query parameter used to search only in released items.
   */
  private static final String STATUS_RELEASED = "released";

  /**
   * Query parameter used to search for dataelements.
   */
  private static final String TYPE_DATAELEMENT = "dataelement";

  /**
   * Query parameter used to search for dataelementgroups.
   */
  private static final String TYPE_DATAELEMENTGROUP = "dataelementgroup";
  /**
   * This class logger.
   */
  private static Logger logger = LoggerFactory.getLogger(MdrClient.class.getName());
  /**
   * The MDR REST interface base URL.
   */
  private String mdrBaseUrl;
  /**
   * The MDR REST interface proxy host.
   */
  private String proxyHost;
  /**
   * The MDR REST interface proxy port.
   */
  private String proxyPort;
  /**
   * The MDR REST interface proxy user name.
   */
  private String proxyUsername;
  /**
   * The MDR REST interface proxy password.
   */
  private String proxyPassword;
  /**
   * The MDR REST interface proxy realm name.
   */
  private String proxyRealm;
  /**
   * The jersey client.
   */
  private Client jerseyClient = null;

  /**
   * Create an MdrClient instance with the properties loaded from mdr.properties file.
   */
  public MdrClient() {
    this(MDR_URL, MDR_PROXY_IP, MDR_PROXY_PORT);
  }

  /**
   * Create an MdrClient instance. Use {@link MdrClient#MdrClient(String, String, String)} instead
   * in case a proxy is necessary to contact the MDR client.
   *
   * @param mdrBaseUrl the MDR REST interface base URL
   */
  public MdrClient(final String mdrBaseUrl) {
    this(mdrBaseUrl, null, null);
  }

  /**
   * Create a MDR client instance with proxy.
   *
   * @param mdrBaseUrl the MDR REST interface base URL
   * @param proxyHost  the proxy host
   * @param proxyPort  the proxy port
   */
  public MdrClient(final String mdrBaseUrl, final String proxyHost, final String proxyPort) {
    // set the proxy settings
    if (proxyHost != null && proxyPort != null) {
      setProxy(proxyHost, proxyPort);
    }

    this.mdrBaseUrl = mdrBaseUrl;
  }

  /**
   * Create an MDR client instance with proxy authentication.
   *
   * @param mdrBaseUrl    the MDR REST interface base URL
   * @param proxyHost     the MDR REST interface proxy host
   * @param proxyPort     the MDR REST interface proxy port
   * @param proxyUsername the MDR REST interface proxy user name
   * @param proxyPassword the MDR REST interface proxy password
   */
  public MdrClient(final String mdrBaseUrl, final String proxyHost, final String proxyPort,
      final String proxyUsername, final String proxyPassword) {
    this.mdrBaseUrl = mdrBaseUrl;
    this.proxyHost = proxyHost;
    this.proxyPort = proxyPort;
    this.proxyUsername = proxyUsername;
    this.proxyPassword = proxyPassword;
  }

  /**
   * Create an MDR client instance with proxy authentication and realm.
   *
   * @param mdrBaseUrl    the MDR REST interface base URL
   * @param proxyHost     the MDR REST interface proxy host
   * @param proxyPort     the MDR REST interface proxy port
   * @param proxyUsername the MDR REST interface proxy user name
   * @param proxyPassword the MDR REST interface proxy password
   * @param proxyRealm    the MDR REST interface proxy realm name
   */
  public MdrClient(final String mdrBaseUrl, final String proxyHost, final String proxyPort,
      final String proxyUsername, final String proxyPassword, final String proxyRealm) {
    this.mdrBaseUrl = mdrBaseUrl;
    this.proxyHost = proxyHost;
    this.proxyPort = proxyPort;
    this.proxyUsername = proxyUsername;
    this.proxyPassword = proxyPassword;
    this.proxyRealm = proxyRealm;
  }

  /**
   * Create an MDR client with a readily configured HTTP Client (with proxy settings).
   *
   * @param mdrBaseUrl the MDR URL
   * @param client     a jersey webclient
   */
  public MdrClient(final String mdrBaseUrl, final Client client) {
    this.mdrBaseUrl = mdrBaseUrl;
    jerseyClient = client;
  }

  /**
   * Get the label of an MDR element as JSON string.
   *
   * @param memberId        e.g. "urn:mdr:record:1:1"
   * @param languageCode    e.g. "de".
   * @param enumElementType type of the data element {@link EnumElementType}
   * @param accessToken     Access token, obtained from OSSE.Auth, that should be used on REST calls
   *                        to the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId      The user authentication server id of the user who is logged in. It is
   *                        used for the cache loader key - the user specific calls are cached
   *                        across sessions.
   * @return JSON string representation of the data elements from the selected record
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   */
  protected final String getJsonLabel(final String memberId, final String languageCode,
      final EnumElementType enumElementType, final String accessToken, final String userAuthId)
      throws MdrConnectionException, ExecutionException {
    String path = "";
    if (enumElementType.equals(EnumElementType.RECORD)) {
      path = RECORDS + PATH_SEPARATOR + memberId + PATH_SEPARATOR + LABELS;
    } else if (enumElementType.equals(EnumElementType.DATAELEMENTGROUP)) {
      path = DATAELEMENTGROUPS + PATH_SEPARATOR + memberId + PATH_SEPARATOR + LABELS;
    } else if (enumElementType.equals(EnumElementType.DATAELEMENT)) {
      path = DATA_ELEMENTS_PATH + PATH_SEPARATOR + memberId + PATH_SEPARATOR + LABELS;
    }
    String json =
        accessToken == null ? getJson(path, languageCode) : getJson(path, languageCode, accessToken,
            userAuthId);

    return json;
  }

  /**
   * Get record label, without authentication.
   *
   * @param recordId     e.g. "urn:mdr:record:1:1 (a test record)"
   * @param languageCode e.g. "de".
   * @return the members of a record
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   * @see #getRecordLabel(String, String, String, String)
   */
  public final List<Label> getRecordLabel(final String recordId, final String languageCode)
      throws MdrConnectionException, ExecutionException {
    return getRecordLabel(recordId, languageCode, null, null);
  }

  /**
   * Get record label from the MDR.
   *
   * @param recordId     e.g. "urn:mdr:record:1:1 (a test record)"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions.
   * @return the members of a record - data elements
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   */
  public final List<Label> getRecordLabel(final String recordId, final String languageCode,
      final String accessToken, final String userAuthId)
      throws MdrConnectionException, ExecutionException {
    String response = getJsonLabel(recordId, languageCode, EnumElementType.RECORD, accessToken,
        userAuthId);

    Gson gson = new Gson();
    Type listType = new TypeToken<ArrayList<Label>>() {
    }.getType();
    List<Label> labels = gson.fromJson(response, listType);

    return labels;
  }

  /**
   * Get dataelementgroup label, without authentication.
   *
   * @param recordId     e.g. "urn:mdr:dataelementgroup:1:1 (a test record)"
   * @param languageCode e.g. "de".
   * @return the members of a dataelementgroup
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   * @see #getDataElementGroupLabel(String, String, String, String)
   */
  public final List<Label> getDataElementGroupLabel(final String recordId,
      final String languageCode)
      throws MdrConnectionException, ExecutionException {
    return getDataElementGroupLabel(recordId, languageCode, null, null);
  }

  /**
   * Get dataelementgroup label from the MDR.
   *
   * @param groupId      e.g. "urn:mdr:dataelementgroup:1:1 (a test record)"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions.
   * @return the members of a dataelementgroup
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   */
  public final List<Label> getDataElementGroupLabel(final String groupId, final String languageCode,
      final String accessToken, final String userAuthId)
      throws MdrConnectionException, ExecutionException {
    String response = getJsonLabel(groupId, languageCode, EnumElementType.DATAELEMENTGROUP,
        accessToken, userAuthId);

    Gson gson = new Gson();
    Type listType = new TypeToken<ArrayList<Label>>() {
    }.getType();
    List<Label> labels = gson.fromJson(response, listType);

    return labels;
  }

  /**
   * Gets the slots of a record without authentication.
   *
   * @param mdrRecordId e.g. "urn:mdr:record:1:1 (a test record)"
   * @return the slots of the record
   * @throws MdrConnectionException Todo.
   * @throws MdrInvalidResponseException Todo.
   * @throws ExecutionException Todo.
   * @see #getRecordSlots(String, String, String)
   */
  public final ArrayList<Slot> getRecordSlots(final String mdrRecordId)
      throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {
    return getRecordSlots(mdrRecordId, null, null);
  }

  /**
   * Gets the slots of a record.
   *
   * @param mdrRecordId e.g. "urn:mdr:record:1:1 (a test record)"
   * @param accessToken Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                    the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId  The user authentication server id of the user who is logged in. It is used
   *                    for the cache loader key - the user specific calls are cached across
   *                    sessions.
   * @return the slots of the record
   * @throws MdrConnectionException Todo.
   * @throws MdrInvalidResponseException Todo.
   * @throws ExecutionException Todo.
   */
  public final ArrayList<Slot> getRecordSlots(final String mdrRecordId, final String accessToken,
      final String userAuthId)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {

    String response = getJsonRecordSlots(mdrRecordId, accessToken, userAuthId);

    Gson gson = new Gson();
    ArrayList<Slot> recordSlots = gson.fromJson(response, new TypeToken<ArrayList<Slot>>() {
    }.getType());

    return recordSlots;
  }

  /**
   * Get all the data elements from a record defined in the MDR as JSON string.
   *
   * @param mdrGroupId   e.g. "urn:mdr:record:1:1 (a test record)"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions.
   * @return JSON string representation of the data elements from the selected record
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   */
  protected final String getJsonRecordMembers(final String mdrGroupId, final String languageCode,
      final String accessToken, final String userAuthId)
      throws MdrConnectionException, ExecutionException {
    String path = RECORDS + PATH_SEPARATOR + mdrGroupId + PATH_SEPARATOR + MEMBERS;
    return accessToken == null ? getJson(path, languageCode)
        : getJson(path, languageCode, accessToken,
            userAuthId);
  }

  /**
   * Get the members of a given record defined in the MDR without authentication.
   *
   * @param recordId     e.g. "urn:mdr:record:1:1 (a test record)"
   * @param languageCode e.g. "de".
   * @return the members of a record
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   * @see #getRecordMembers(String, String, String, String)
   */
  public final List<Result> getRecordMembers(final String recordId, final String languageCode)
      throws MdrConnectionException, ExecutionException {
    return getRecordMembers(recordId, languageCode, null, null);
  }

  /**
   * Get the members of a given record defined in the MDR.
   *
   * @param recordId     e.g. "urn:mdr:record:1:1 (a test record)"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions.
   * @return the members of a record
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   */
  public final List<Result> getRecordMembers(final String recordId, final String languageCode,
      final String accessToken, final String userAuthId)
      throws MdrConnectionException, ExecutionException {
    String response = getJsonRecordMembers(recordId, languageCode, accessToken, userAuthId);

    Gson gson = new Gson();
    Type listType = new TypeToken<ArrayList<Result>>() {
    }.getType();
    ArrayList<Result> members = gson.fromJson(response, listType);

    return members;
  }

  /**
   * Get all the data elements from a group defined in the MDR as JSON string.
   *
   * @param mdrGroupId   e.g. "urn:mdr:dataelementgroup:1:1 (for MDS-K)"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions.
   * @return JSON string representation of the data elements from the selected group
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   */
  protected final String getJsonMembers(final String mdrGroupId, final String languageCode,
      final String accessToken, final String userAuthId)
      throws MdrConnectionException, ExecutionException {
    String path = DATAELEMENTGROUPS + PATH_SEPARATOR + mdrGroupId + PATH_SEPARATOR + MEMBERS;
    return accessToken == null ? getJson(path, languageCode)
        : getJson(path, languageCode, accessToken,
            userAuthId);
  }

  /**
   * Get the members of a given group defined in the MDR, not authenticated.
   *
   * @param mdrGroupId   e.g. "urn:mdr:dataelementgroup:1:1 (for MDS-K)"
   * @param languageCode e.g. "de".
   * @return the subgroups of a group
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   * @see #getMembers(String, String, String, String)
   */
  public final List<Result> getMembers(final String mdrGroupId, final String languageCode)
      throws MdrConnectionException, ExecutionException {
    return getMembers(mdrGroupId, languageCode, null, null);
  }

  /**
   * Get the members of a given group defined in the MDR.
   *
   * @param mdrGroupId   e.g. "urn:mdr:dataelementgroup:1:1 (for MDS-K)"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions.
   * @return the subgroups of a group
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   */
  public final List<Result> getMembers(final String mdrGroupId, final String languageCode,
      final String accessToken, final String userAuthId)
      throws MdrConnectionException, ExecutionException {
    String response = getJsonMembers(mdrGroupId, languageCode, accessToken, userAuthId);

    Gson gson = new Gson();
    ResultList members = gson.fromJson(response, ResultList.class);

    return members.getResults();
  }

  /**
   * Get a JSON response, tries to load data from the cache first, without authentication.
   *
   * @param path         MDR REST interface path to be called
   * @param languageCode e.g. "de".
   * @return the JSON string representation of MDR reply to the given parameters (possibly cached)
   * @throws ExecutionException if there is an execution error
   */
  protected final String getJson(final String path, final String languageCode)
      throws ExecutionException {
    CacheKey key = new CacheKey(path, languageCode);
    return CacheManager.getCache(this).get(key);
  }

  /**
   * Get a JSON response, tries to load data from the cache first.
   *
   * @param path         MDR REST interface path to be called
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions.
   * @return the JSON string representation of MDR reply to the given parameters (possibly cached)
   * @throws ExecutionException if there is an execution error
   */
  protected final String getJson(final String path, final String languageCode,
      final String accessToken,
      final String userAuthId) throws ExecutionException {
    CacheKey key = new CacheKey(path, languageCode, accessToken, userAuthId);
    return CacheManager.getCache(this).get(key);
  }

  /**
   * Get a JSON response directly from the MDR, without authentication. For cached responses, {@link
   * #getJson(String, String)} should be used instead.
   *
   * @param path         MDR REST interface path to be called
   * @param languageCode e.g. "de".
   * @return the JSON string representation of MDR reply to the given parameters (overrides cache)
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   */
  final String getJsonFromMdr(final String path, final String languageCode)
      throws MdrConnectionException {
    String string = null;
    try {
      string = getService().path(path).accept(MediaType.APPLICATION_JSON)
          .header(HttpHeaders.ACCEPT_LANGUAGE, languageCode).get(String.class);
    } catch (Exception e) {
      throw new MdrConnectionException(e.getMessage());
    }
    return string;
  }

  /**
   * Get a JSON response directly from the MDR. For cached responses, {@link #getJson(String,
   * String)} should be used instead.
   *
   * @param path         MDR REST interface path to be called
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @return the JSON string representation of MDR reply to the given parameters (overrides cache)
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   */
  final String getJsonFromMdr(final String path, final String languageCode,
      final String accessToken)
      throws MdrConnectionException {
    String string = null;
    try {
      string = getService().path(path).accept(MediaType.APPLICATION_JSON)
          .header(HttpHeaders.ACCEPT_LANGUAGE, languageCode)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).get(String.class);
    } catch (Exception e) {
      throw new MdrConnectionException(e.getMessage());
    }
    return string;
  }

  /**
   * Get all the data elements from a group defined in the MDR, without authentication.
   *
   * @param mdrGroupId   e.g. "urn:mdr:dataelementgroup:1:1 (for MDS-K)"
   * @param languageCode e.g. "de".
   * @return data elements from the selected group
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   * @see #getDataElementsFromGroup(String, String, String, String)
   * @deprecated no longer supported directly by the MDR. Use {@link #getMembers(String, String)}
   *        instead.
   */
  @Deprecated
  public final List<Result> getDataElementsFromGroup(final String mdrGroupId,
      final String languageCode)
      throws MdrConnectionException, ExecutionException {
    return getDataElementsFromGroup(mdrGroupId, languageCode, null, null);
  }

  /**
   * Get all the data elements from a group defined in the MDR.
   *
   * @param mdrGroupId   e.g. "urn:mdr:dataelementgroup:1:1 (for MDS-K)"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions.
   * @return data elements from the selected group
   * @throws MdrConnectionException if there is an error while connecting to the MDR
   * @throws ExecutionException     if there is an execution error
   * @deprecated no longer supported directly by the MDR. Use {@link #getMembers(String, String)}
   *        instead.
   */
  @Deprecated
  public final List<Result> getDataElementsFromGroup(final String mdrGroupId,
      final String languageCode,
      final String accessToken, final String userAuthId)
      throws MdrConnectionException, ExecutionException {

    String response = getJsonMembers(mdrGroupId, languageCode, accessToken, userAuthId);

    Gson gson = new Gson();
    ResultList dataElements = gson.fromJson(response, ResultList.class);

    return filterMembers(dataElements.getResults(), EnumElementType.DATAELEMENT);
  }

  /**
   * Get a data element's validations.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions.
   * @return JSON string representation of validations from the given MDR data element ID
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  protected final String getJsonDataElementValidations(final String mdrDataElementId,
      final String languageCode,
      final String accessToken, final String userAuthId) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {

    String path = DATA_ELEMENTS_PATH + PATH_SEPARATOR + mdrDataElementId //$NON-NLS-1$
        + PATH_SEPARATOR + VALIDATIONS_PATH; //$NON-NLS-1$
    String json =
        accessToken == null ? getJson(path, languageCode) : getJson(path, languageCode, accessToken,
            userAuthId);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrDataElementId
          + ". Is the MDR ID valid?");
    }
  }

  /**
   * Get the JSON string representation of the catalogue from the given data element.
   *
   * @param mdrDataElementId data element that has a catalogue e.g. "urn:test:dataelement:3:1"
   * @param languageCode     e.g. "de".
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return JSON string representation of the catalogue from the given MDR data element ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  protected final String getJsonDataElementCatalogue(final String mdrDataElementId,
      final String languageCode,
      final String accessToken, final String userAuthId) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {

    String path = DATA_ELEMENTS_PATH + PATH_SEPARATOR + mdrDataElementId //$NON-NLS-1$
        + PATH_SEPARATOR + CATALOGUE_PATH; //$NON-NLS-1$
    String json =
        accessToken == null ? getJson(path, languageCode) : getJson(path, languageCode, accessToken,
            userAuthId);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrDataElementId
          + ". Is the MDR ID valid and is it a catalogue?");
    }
  }


  /**
   * Get the JSON string representation of a catalogue from the given catalog URN.
   *
   * @param catalogueId  urn of the catalogue e.g. "urn:test:catalog:1:1"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @return JSON string representation of the catalogue with the given catalogue ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  protected final String getJsonCatalogue(final String catalogueId, final String languageCode,
      final String accessToken, final String userAuthId) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {

    String path = CATALOGUES_PATH + PATH_SEPARATOR + catalogueId + PATH_SEPARATOR + CODES_PATH;
    String json =
        accessToken == null ? getJson(path, languageCode) : getJson(path, languageCode, accessToken,
            userAuthId);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + catalogueId
          + ". Is the MDR ID valid and is it a catalogue?");
    }
  }

  /**
   * Get a data element's slots from the MDR.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1:0"
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return JSON string representation of slots from the given MDR data element ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  protected final String getJsonDataElementSlots(final String mdrDataElementId,
      final String accessToken,
      final String userAuthId) throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {

    String path = DATA_ELEMENTS_PATH + PATH_SEPARATOR + mdrDataElementId + PATH_SEPARATOR + SLOTS;
    String json =
        accessToken == null ? getJson(path, "en") : getJson(path, "en", accessToken, userAuthId);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrDataElementId
          + ". Is the MDR ID valid?");
    }
  }

  /**
   * Get a data element group's slots from the MDR.
   *
   * @param mdrDataElementGroupId e.g. "urn:mdr:dataelementgroup:2:1:0"
   * @param accessToken           Access token, obtained from OSSE.Auth, that should be used on REST
   *                              calls to the MDR when a user is logged in. Use null for anonymous
   *                              access.
   * @param userAuthId            The user authentication server id of the user who is logged in. It
   *                              is used for the cache loader key - the user specific calls are
   *                              cached across sessions. Use null for anonymous access.
   * @return JSON string representation of slots from the given MDR data element ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  protected final String getJsonDataElementGroupSlots(final String mdrDataElementGroupId,
      final String accessToken,
      final String userAuthId) throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {

    String path =
        DATAELEMENTGROUPS + PATH_SEPARATOR + mdrDataElementGroupId + PATH_SEPARATOR + SLOTS;
    String json =
        accessToken == null ? getJson(path, "en") : getJson(path, "en", accessToken, userAuthId);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrDataElementGroupId
          + ". Is the MDR ID valid?");
    }
  }

  /**
   * Gets a record's slots from the MDR.
   *
   * @param mdrRecordId e.g. "urn:mdr:record:1:1"
   * @param accessToken Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                    the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId  The user authentication server id of the user who is logged in. It is used
   *                    for the cache loader key - the user specific calls are cached across
   *                    sessions. Use null for anonymous access.
   * @return JSON string representation of slots from the given MDR record ID.
   * @throws MdrConnectionException Todo.
   * @throws MdrInvalidResponseException Todo.
   * @throws ExecutionException Todo.
   */
  protected final String getJsonRecordSlots(final String mdrRecordId, final String accessToken,
      final String userAuthId) throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {

    String path = RECORDS + PATH_SEPARATOR + mdrRecordId + PATH_SEPARATOR + SLOTS;
    String json =
        accessToken == null ? getJson(path, "en") : getJson(path, "en", accessToken, userAuthId);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrRecordId
          + ". Is the MDR ID valid?");
    }
  }

  /**
   * Get a codes slots from the MDR.
   *
   * @param mdrCatalogueId e.g. "urn:mdr:catalog:1:1"
   * @param mdrCodeId      e.g. "urn:mdr:code:2:1.0"
   * @param accessToken    Access token, obtained from OSSE.Auth, that should be used on REST calls
   *                       to the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId     The user authentication server id of the user who is logged in. It is
   *                       used for the cache loader key - the user specific calls are cached across
   *                       sessions. Use null for anonymous access.
   * @return JSON string representation of slots from the given MDR code ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  protected final String getJsonCodeSlots(final String mdrCatalogueId, final String mdrCodeId,
      final String accessToken,
      final String userAuthId) throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {

    String path = CATALOGUES_PATH + PATH_SEPARATOR + mdrCatalogueId + PATH_SEPARATOR + CODES_PATH
        + PATH_SEPARATOR + mdrCodeId + PATH_SEPARATOR + SLOTS;
    String json =
        accessToken == null ? getJson(path, "en") : getJson(path, "en", accessToken, userAuthId);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrCodeId
          + ". Is the MDR ID valid?");
    }
  }

  /**
   * Get a data element's slots, without authentication.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @return JSON string representation of slots from the given MDR data element ID
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   * @see #getDataElementSlots(String, String, String)
   */
  public final ArrayList<Slot> getDataElementSlots(final String mdrDataElementId)
      throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {
    return getDataElementSlots(mdrDataElementId, null, null);
  }

  /**
   * Get a data element's slots.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions.
   * @return JSON string representation of slots from the given MDR data element ID
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  public final ArrayList<Slot> getDataElementSlots(final String mdrDataElementId,
      final String accessToken,
      final String userAuthId) throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {

    String response = getJsonDataElementSlots(mdrDataElementId, accessToken, userAuthId);

    Gson gson = new Gson();
    ArrayList<Slot> dataElementSlots = gson.fromJson(response, new TypeToken<ArrayList<Slot>>() {
    }.getType());

    return dataElementSlots;
  }

  /**
   * Get a data element group's slots, without authentication.
   *
   * @param mdrDataElementGroupId e.g. "urn:mdr:datagroup:2:1.0"
   * @return JSON string representation of slots from the given MDR data element ID
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   * @see #getDataElementGroupSlots(String, String, String)
   */
  public final ArrayList<Slot> getDataElementGroupSlots(final String mdrDataElementGroupId)
      throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {
    return getDataElementGroupSlots(mdrDataElementGroupId, null, null);
  }

  /**
   * Get a data group's slots.
   *
   * @param mdrDataElementGroupId e.g. "urn:mdr:datagroup:2:1.0"
   * @param accessToken           Access token, obtained from OSSE.Auth, that should be used on REST
   *                              calls to the MDR when a user is logged in. Use null for anonymous
   *                              access.
   * @param userAuthId            The user authentication server id of the user who is logged in. It
   *                              is used for the cache loader key - the user specific calls are
   *                              cached across sessions.
   * @return JSON string representation of slots from the given MDR data element ID
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  public final ArrayList<Slot> getDataElementGroupSlots(final String mdrDataElementGroupId,
      final String accessToken,
      final String userAuthId) throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {

    String response = getJsonDataElementGroupSlots(mdrDataElementGroupId, accessToken, userAuthId);

    Gson gson = new Gson();
    ArrayList<Slot> dataGroupSlots = gson.fromJson(response, new TypeToken<ArrayList<Slot>>() {
    }.getType());

    return dataGroupSlots;
  }

  /**
   * Get a code, without authentication.
   *
   * @param mdrCatalogueId e.g. "urn:mdr:catalog:1:1"
   * @param mdrCodeId      e.g. "urn:mdr:code:2:1.0"
   * @return the code
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   * @see #getCode(String, String, String, String)
   */
  public final Code getCode(final String mdrCatalogueId,
      final String mdrCodeId)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    return getCode(mdrCatalogueId, mdrCodeId, null, null);
  }

  /**
   * Get a code.
   *
   * @param mdrCatalogueId e.g. "urn:mdr:catalog:1:1"
   * @param mdrCodeId      e.g. "urn:mdr:code:2:1.0"
   * @param accessToken    Access token, obtained from OSSE.Auth, that should be used on REST calls
   *                       to the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId     The user authentication monitor id of the user who is logged in. It is
   *                       used for the cache loader key - the user specific calls are cached across
   *                       sessions.
   * @return the code
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  public final Code getCode(final String mdrCatalogueId, final String mdrCodeId,
      final String accessToken,
      final String userAuthId)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {

    String response = getJsonCode(mdrCatalogueId, mdrCodeId, accessToken, userAuthId);

    Gson gson = new Gson();
    Code code = gson.fromJson(response, Code.class);

    return code;
  }

  /**
   * Get a codes from the MDR.
   *
   * @param mdrCatalogueId e.g. "urn:mdr:catalog:1:1"
   * @param mdrCodeId      e.g. "urn:mdr:code:2:1.0"
   * @param accessToken    Access token, obtained from OSSE.Auth, that should be used on REST calls
   *                       to the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId     The user authentication monitor id of the user who is logged in. It is
   *                       used for the cache loader key - the user specific calls are cached across
   *                       sessions. Use null for anonymous access.
   * @return JSON string representation of slots from the given MDR code ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  protected final String getJsonCode(final String mdrCatalogueId, final String mdrCodeId,
      final String accessToken,
      final String userAuthId)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {

    String path = CATALOGUES_PATH + "/" + mdrCatalogueId + "/" + CODES_PATH + "/" + mdrCodeId;
    String json =
        accessToken == null ? getJson(path, "en") : getJson(path, "en", accessToken, userAuthId);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException(
          "Unexpected response for " + mdrCodeId + ". Is the MDR ID valid?");
    }
  }

  /**
   * Get a codes slots, without authentication.
   *
   * @param mdrCatalogueId e.g. "urn:mdr:catalog:1:1"
   * @param mdrCodeId      e.g. "urn:mdr:code:2:1.0"
   * @return JSON string representation of slots from the given MDR code ID
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   * @see #getCodeSlots(String, String, String, String)
   */
  public final ArrayList<Slot> getCodeSlots(final String mdrCatalogueId, final String mdrCodeId)
      throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {
    return getCodeSlots(mdrCatalogueId, mdrCodeId, null, null);
  }

  /**
   * Get a codes slots.
   *
   * @param mdrCatalogueId e.g. "urn:mdr:catalog:1:1"
   * @param mdrCodeId      e.g. "urn:mdr:code:2:1.0"
   * @param accessToken    Access token, obtained from OSSE.Auth, that should be used on REST calls
   *                       to the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId     The user authentication server id of the user who is logged in. It is
   *                       used for the cache loader key - the user specific calls are cached across
   *                       sessions.
   * @return JSON string representation of slots from the given MDR code ID
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error
   */
  public final ArrayList<Slot> getCodeSlots(final String mdrCatalogueId, final String mdrCodeId,
      final String accessToken,
      final String userAuthId) throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {

    String response = getJsonCodeSlots(mdrCatalogueId, mdrCodeId, accessToken, userAuthId);

    Gson gson = new Gson();
    ArrayList<Slot> codeSlots = gson.fromJson(response, new TypeToken<ArrayList<Slot>>() {
    }.getType());

    return codeSlots;
  }

  /**
   * Search for data that are related to the given search text.
   *
   * @param searchText   the text to search for
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param namespace    the namespace to search in. can be omitted to search in own namespace
   * @return JSON string representation of a search
   * @throws MdrConnectionException      if there is an error while connecting to the MDR
   * @throws MdrInvalidResponseException if an invalid response is retrieved from the MDR
   * @throws ExecutionException          if there is an execution error
   */
  protected final String getJsonSearch(final String searchText, final String languageCode,
      final String accessToken, final String namespace)
      throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {
    String json = null;

    String path = "";
    if (namespace != null && namespace.length() > 0) {
      path = NAMESPACES_PATH + PATH_SEPARATOR + namespace + PATH_SEPARATOR;
    }
    path = path + SEARCH;

    json = getService().path(path.toString()).queryParam("query", searchText)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.ACCEPT_LANGUAGE, languageCode)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).get(String.class);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException(
          "Unexpected response for the search query: " + searchText);
    }
  }

  /**
   * Search for data that are related to the given search text.
   *
   * @param searchText   the text to search for
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @return JSON string representation of a search
   * @throws MdrConnectionException      if there is an error while connecting to the MDR
   * @throws MdrInvalidResponseException if an invalid response is retrieved from the MDR
   * @throws ExecutionException          if there is an execution error
   */
  protected final String getJsonSearchLocal(final String searchText, final String languageCode,
      final String accessToken) throws MdrConnectionException, MdrInvalidResponseException,
      ExecutionException {

    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
    List<String> typeList = new ArrayList<String>();
    typeList.add(TYPE_DATAELEMENT);
    typeList.add(TYPE_DATAELEMENTGROUP);

    queryParams.add("query", searchText);
    queryParams.add("status", STATUS_RELEASED);
    queryParams.put("type", typeList);

    String json = null;
    String path = SEARCH + PATH_SEPARATOR + SEARCH_LOCAL;
    json = getService().path(path.toString())
        .queryParams(queryParams)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.ACCEPT_LANGUAGE, languageCode)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).get(String.class);

    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException(
          "Unexpected response for the search query: " + searchText);
    }
  }

  /**
   * Get a data element's validations, without authentication.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @return JSON string representation of validations from the given MDR data element ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   * @see #getDataElementValidations(String, String, String, String)
   */
  public final Validations getDataElementValidations(final String mdrDataElementId,
      final String languageCode)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    return getDataElementValidations(mdrDataElementId, languageCode, null, null);
  }

  /**
   * Get a data element's validations.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return JSON string representation of validations from the given MDR data element ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final Validations getDataElementValidations(final String mdrDataElementId,
      final String languageCode,
      final String accessToken, final String userAuthId) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {

    String response = getJsonDataElementValidations(mdrDataElementId, languageCode, accessToken,
        userAuthId);

    Gson gson = new Gson();
    Validations dataElementValidations = gson.fromJson(response, Validations.class);

    return dataElementValidations;
  }

  /**
   * Get a data element catalogue (when applicable).
   *
   * @param mdrDataElementId data element that has a catalogue e.g. "urn:test:dataelement:3:1"
   * @param languageCode     e.g. "de".
   * @return The catalogue of the given data element.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final Catalogue getDataElementCatalogue(final String mdrDataElementId,
      final String languageCode)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    return getDataElementCatalogue(mdrDataElementId, languageCode, null, null);
  }

  /**
   * Get a data element catalogue (when applicable).
   *
   * @param mdrDataElementId data element that has a catalogue e.g. "urn:test:dataelement:3:1"
   * @param languageCode     e.g. "de".
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return The catalogue of the given data element.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final Catalogue getDataElementCatalogue(final String mdrDataElementId,
      final String languageCode,
      final String accessToken, final String userAuthId) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {

    String response = getJsonDataElementCatalogue(mdrDataElementId, languageCode, accessToken,
        userAuthId);

    Gson gson = new Gson();
    Catalogue catalogue = gson.fromJson(response, Catalogue.class);

    return catalogue;
  }

  /**
   * Get a catalogue by its urn.
   *
   * @param catalogueId  urn of the catalogue e.g. "urn:test:catalog:1:1"
   * @param languageCode e.g. "de".
   * @return The catalogue with the given catalogueId.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final Catalogue getCatalogue(final String catalogueId, final String languageCode)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    return getCatalogue(catalogueId, languageCode, null, null);
  }

  /**
   * Get a catalogue by its urn.
   *
   * @param catalogueId  urn of the catalogue e.g. "urn:test:catalog:1:1"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @return The catalogue with the given catalogueId.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final Catalogue getCatalogue(final String catalogueId, final String languageCode,
      final String accessToken, final String userAuthId) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {

    String response = getJsonCatalogue(catalogueId, languageCode, accessToken, userAuthId);

    Gson gson = new Gson();
    Catalogue catalogue = gson.fromJson(response, Catalogue.class);

    return catalogue;
  }

  /**
   * Search for data that is related to the given search text. It searches only on data that the
   * user has access to.
   *
   * @param searchText   the text to search for
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @return search results
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final List<Result> search(final String searchText, final String languageCode,
      final String accessToken)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {

    logger.debug("lang " + languageCode);

    String response = getJsonSearch(searchText, languageCode, accessToken, null);

    Gson gson = new Gson();
    ResultList results = gson.fromJson(response, ResultList.class);

    return results.getResults();
  }

  /**
   * Search for data that is related to the given search text. It searches in the given namespace
   * which has to be publicly accesible.
   *
   * @param searchText   the text to search for
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param namespace    the namespace to search in.
   * @return search results
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final List<Result> searchInNamespace(final String searchText, final String languageCode,
      final String accessToken, final String namespace)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {

    String response = getJsonSearch(searchText, languageCode, accessToken, namespace);

    Gson gson = new Gson();
    ResultList results = gson.fromJson(response, ResultList.class);

    return results.getResults();
  }

  /**
   * Search for data that is related to the given search text. It searches in all local namespaces.
   *
   * @param searchText   the text to search for
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @return search results
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final List<Result> searchLocal(final String searchText, final String languageCode,
      final String accessToken)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {

    String response = getJsonSearchLocal(searchText, languageCode, accessToken);

    Gson gson = new Gson();
    ResultList results = gson.fromJson(response, ResultList.class);

    return results.getResults();
  }

  /**
   * Get the designation/definition and the value domain of an MDR data element as JSON.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return JSON string representation of the complete metadata set of a data element.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  protected final String getJsonDataElementDefinition(final String mdrDataElementId,
      final String languageCode,
      final String accessToken, final String userAuthId) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {
    String path =
        DATA_ELEMENTS_PATH + PATH_SEPARATOR + mdrDataElementId + PATH_SEPARATOR + DESIGNATION_PATH;
    String json =
        accessToken == null ? getJson(path, languageCode) : getJson(path, languageCode, accessToken,
            userAuthId);
    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrDataElementId
          + ". Is the MDR ID valid?");
    }
  }

  /**
   * Get the whole DataElement as a JSON object from the MDR.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return JSON string representation of the complete data element.
   */
  protected final String getJsonDataElement(final String mdrDataElementId,
      final String languageCode,
      final String accessToken, final String userAuthId) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {
    String path = DATA_ELEMENTS_PATH + PATH_SEPARATOR + mdrDataElementId;
    String json =
        accessToken == null ? getJson(path, languageCode) : getJson(path, languageCode, accessToken,
            userAuthId);
    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrDataElementId
          + ". Is the MDR ID valid?");
    }
  }

  /**
   * Get the designation/definition and the value domain of an MDR record as JSON.
   *
   * @param mdrRecordId  e.g. "urn:mdr:record:2:1.0"
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @return JSON string representation of the complete metadata set of a record.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  protected final String getJsonRecordDefinition(final String mdrRecordId,
      final String languageCode,
      final String accessToken, final String userAuthId)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    String path = RECORDS + PATH_SEPARATOR + mdrRecordId + PATH_SEPARATOR + LABELS;
    String json =
        accessToken == null ? getJson(path, languageCode) : getJson(path, languageCode, accessToken,
            userAuthId);
    if (json != null && !json.isEmpty()) {
      return json;
    } else {
      throw new MdrInvalidResponseException("Unexpected response for " + mdrRecordId
          + ". Is the MDR ID valid?");
    }
  }

  /**
   * Get the designation/definition and the value domain of an MDR data element as {@link
   * Definition}, without authentication.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @return JSON string representation of validations from the given MDR data element ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   * @see #getDataElementDefinition(String, String, String, String)
   */
  public final Definition getDataElementDefinition(final String mdrDataElementId,
      final String languageCode)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    return getDataElementDefinition(mdrDataElementId, languageCode, null, null);
  }

  /**
   * Get the designation/definition and the value domain of a MDR data element as {@link
   * Definition}.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAccessToken  The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return JSON string representation of validations from the given MDR data element ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final Definition getDataElementDefinition(final String mdrDataElementId,
      final String languageCode,
      final String accessToken, final String userAccessToken) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {
    logger.debug(
        "Getting data element definition for " + mdrDataElementId + " | " + languageCode + " | "
            + accessToken);

    String response = getJsonDataElementDefinition(mdrDataElementId, languageCode, accessToken,
        userAccessToken);

    Gson gson = new Gson();
    Definition dataElementDefinition = gson.fromJson(response, Definition.class);

    return dataElementDefinition;
  }

  /**
   * Get the designation/definition and the value domain of an MDR record as {@link
   * RecordDefinition}, without authentication.
   *
   * @param mdrRecordId  e.g. "urn:osse-123:record:2:1"
   * @param languageCode e.g. "de".
   * @return JSON string representation of validations from the given MDR record ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   * @see #getRecordDefinition(String, String, String, String)
   */
  public final RecordDefinition getRecordDefinition(final String mdrRecordId,
      final String languageCode)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    return getRecordDefinition(mdrRecordId, languageCode, null, null);
  }

  /**
   * Get the designation/definition and the value domain of a MDR record as {@link
   * RecordDefinition}.
   *
   * @param mdrRecordId     e.g. "urn:osse-123:record:2:1"
   * @param languageCode    e.g. "de".
   * @param accessToken     Access token, obtained from OSSE.Auth, that should be used on REST calls
   *                        to the MDR when a user is logged in. Use null for anonymous access.
   * @param userAccessToken The user authentication server id of the user who is logged in. It is
   *                        used for the cache loader key - the user specific calls are cached
   *                        across sessions. Use null for anonymous access.
   * @return JSON string representation of validations from the given MDR record ID.
   * @throws MdrConnectionException      if there was an error communicating with the MDR.
   * @throws MdrInvalidResponseException if the MDR response is not valid.
   * @throws ExecutionException          if there is an execution error.
   */
  public final RecordDefinition getRecordDefinition(final String mdrRecordId,
      final String languageCode,
      final String accessToken, final String userAccessToken)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    logger.debug("Getting record  definition for " + mdrRecordId + " | " + languageCode + " | "
        + accessToken);

    String response = getJsonRecordDefinition(mdrRecordId, languageCode, accessToken,
        userAccessToken);

    Gson gson = new Gson();
    RecordDefinition recordDefinition = gson.fromJson(response, RecordDefinition.class);

    return recordDefinition;
  }


  /**
   * Filter members by type. The MDR no longer supports /subrgroups and /entries, so all types are
   * returned in the same list. To easily get specific {@link EnumElementType} from an element
   * result list, use this method.
   *
   * @param members         a list of MDR members.
   * @param enumElementType type of the members list returned.
   * @return list of members of the given type.
   */
  public final List<Result> filterMembers(final List<Result> members,
      final EnumElementType enumElementType) {
    for (Iterator<Result> iterator = members.iterator(); iterator.hasNext(); ) {
      Result result = iterator.next();
      if (result.getType().compareTo(enumElementType.name()) != 0) {
        iterator.remove();
      }
    }
    return members;
  }

  /**
   * Get all available namespaces as JSON.
   *
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @return the list of namespaces as JSON string.
   * @throws ExecutionException if there is an execution error.
   */
  protected final String getJsonNamespaces(final String languageCode, final String accessToken,
      final String userAuthId) throws ExecutionException {
    String path = NAMESPACES_PATH;
    return accessToken == null ? getJson(path, languageCode)
        : getJson(path, languageCode, accessToken,
            userAuthId);
  }

  /**
   * Get elements from a namespace (e.g. MDS-K, CPP-IT) as JSON.
   *
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @param namespace    the namespace from which members will be queried.
   * @return the namespace elements (such as MDS-K, CPP-IT) as JSON string.
   * @throws ExecutionException if there is an execution error.
   */
  protected final String getJsonNamespaceMembers(final String languageCode,
      final String accessToken,
      final String userAuthId, final String namespace) throws ExecutionException {
    String path = NAMESPACES_PATH + PATH_SEPARATOR + namespace + PATH_SEPARATOR + MEMBERS;
    return accessToken == null ? getJson(path, languageCode)
        : getJson(path, languageCode, accessToken,
            userAuthId);
  }

  /**
   * Get the root elements from the MDR for a specific user (e.g. MDS-K, CPP-IT) as JSON.
   *
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @return the root elements for a specific user (such as MDS-K, CPP-IT) as JSON string.
   * @throws ExecutionException if there is an execution error.
   */
  protected final String getUserJsonRootElements(final String languageCode,
      final String accessToken,
      final String userAuthId) throws ExecutionException {
    String path = MEMBERS;
    return accessToken == null ? getJson(path, languageCode)
        : getJson(path, languageCode, accessToken,
            userAuthId);
  }

  /**
   * Get the namespeaces from the MDR, without authentication.
   *
   * @param languageCode e.g. "de".
   * @return the namespaces.
   * @throws ExecutionException if there is an execution error.
   * @see #getNamespaces(String, String, String)
   */
  public final List<Namespace> getNamespaces(final String languageCode)
      throws ExecutionException {
    return getNamespaces(languageCode, null, null);
  }

  /**
   * Get the namespeaces from the MDR.
   *
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @return the namespaces.
   * @throws ExecutionException if there is an execution error.
   */
  public final List<Namespace> getNamespaces(final String languageCode, final String accessToken,
      final String userAuthId) throws ExecutionException {
    String response = getJsonNamespaces(languageCode, accessToken, userAuthId);

    Gson gson = new Gson();
    Type listType = new TypeToken<ArrayList<Namespace>>() {
    }.getType();
    List<Namespace> namespaces = gson.fromJson(response, listType);

    return namespaces;
  }

  /**
   * Get the root groups from the MDR (e.g. MDS-K, CPP-IT), without authentication.
   *
   * @param languageCode e.g. "de".
   * @param namespace    the namespace from which members will be queried.
   * @return the root groups (such as MDS-K, CPP-IT).
   * @throws ExecutionException if there is an execution error.
   * @see #getNamespaceMembers(String, String, String, String)
   */
  public final List<Result> getNamespaceMembers(final String languageCode, final String namespace)
      throws ExecutionException {
    return getNamespaceMembers(languageCode, null, null, namespace);
  }

  /**
   * Get the root groups from the MDR (e.g. MDS-K, CPP-IT)
   *
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @param namespace    the namespace from which members will be queried.
   * @return the namespace members (such as MDS-K, CPP-IT).
   * @throws ExecutionException if there is an execution error.
   */
  public final List<Result> getNamespaceMembers(final String languageCode, final String accessToken,
      final String userAuthId, final String namespace) throws ExecutionException {
    String response = getJsonNamespaceMembers(languageCode, accessToken, userAuthId, namespace);

    Gson gson = new Gson();
    ResultList resultList = gson.fromJson(response, ResultList.class);

    return resultList.getResults();
  }

  /**
   * Get the root elements from the MDR for a specific user.
   *
   * @param languageCode e.g. "de".
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   * @return the root elements (such as MDS-K, CPP-IT).
   * @throws ExecutionException if there is an execution error.
   */
  public final ResultList getUserRootElements(final String languageCode, final String accessToken,
      final String userAuthId) throws ExecutionException {
    String response = getUserJsonRootElements(languageCode, accessToken, userAuthId);

    Gson gson = new Gson();
    ResultList members = gson.fromJson(response, ResultList.class);
    return members;
  }

  /**
   * Get the representations of a dataelement for a specific users namespace.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAuthId       The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return the representations of the dataelement in the users namespace
   * @throws ExecutionException if there is an execution error.
   */
  public final ResultList getUserNamespaceRepresentations(final String mdrDataElementId,
      final String accessToken,
      final String userAuthId) throws ExecutionException {
    String json = null;
    String path = REPRESENTATIONS;
    json = getService().path(path.toString()).queryParam("urn", mdrDataElementId)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).get(String.class);

    Gson gson = new Gson();
    ResultList resultList = gson.fromJson(json, ResultList.class);
    return resultList;
  }

  /**
   * Get the whole DataElement object from the MDR without authentication.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @return The dataelement object
   * @see #getDataElement(String, String, String, String)
   */
  public final DataElement getDataElement(final String mdrDataElementId, final String languageCode)
      throws MdrConnectionException, MdrInvalidResponseException, ExecutionException {
    return getDataElement(mdrDataElementId, languageCode, null, null);
  }

  /**
   * Get the whole DataElement object from the MDR.
   *
   * @param mdrDataElementId e.g. "urn:mdr:dataelement:2:1.0"
   * @param languageCode     e.g. "de".
   * @param accessToken      Access token, obtained from OSSE.Auth, that should be used on REST
   *                         calls to the MDR when a user is logged in. Use null for anonymous
   *                         access.
   * @param userAccessToken  The user authentication server id of the user who is logged in. It is
   *                         used for the cache loader key - the user specific calls are cached
   *                         across sessions. Use null for anonymous access.
   * @return The dataelement object
   */
  public final DataElement getDataElement(final String mdrDataElementId, final String languageCode,
      final String accessToken, final String userAccessToken) throws MdrConnectionException,
      MdrInvalidResponseException, ExecutionException {
    logger.trace("Getting data element " + mdrDataElementId + " | " + languageCode + " | "
        + accessToken);

    String response = getJsonDataElement(mdrDataElementId, languageCode, accessToken,
        userAccessToken);

    Gson gson = new Gson();
    return gson.fromJson(response, DataElement.class);
  }

  /**
   * Clean the cache.
   */
  public final void cleanCache() {
    logger.debug("Cleaning the cache...");
    CacheManager.cleanCache(this);
  }

  /**
   * Get the MDR RESTful interface base URI.
   *
   * @return the MDR RESTful interface base URI
   */
  public final URI getBaseUri() {
    return UriBuilder.fromUri(mdrBaseUrl).build();
  }

  /**
   * Set the proxy settings.
   *
   * @param proxyHost the proxy host
   * @param proxyPort the proxy port
   */
  private void setProxy(final String proxyHost, final String proxyPort) {
    System.setProperty(HTTP_PROXY_HOST_PROPERTY, proxyHost);
    System.setProperty(HTTP_PROXY_PORT_PROPERTY, proxyPort);
  }

  /**
   * Get a {@link WebResource} for the MDR Client. It is always the same for all requests.
   *
   * @return the {@link WebResource} for the MDR Client
   */
  private WebResource getService() {
    // If the constructor was called with a provided jersey client from another application
    // use that one. Otherwise create a new one.
    if (jerseyClient != null) {
      return jerseyClient.resource(getBaseUri());
    } else {
      URLConnectionClientHandler cc = new URLConnectionClientHandler(getConnectionFactory());

      ClientConfig config = new DefaultClientConfig();
      Client client = new Client(cc, config);
      WebResource service = client.resource(getBaseUri());
      return service;
    }
  }

  /**
   * Get a connection factory instance.
   *
   * @return a connection factory instance
   */
  private ConnectionFactory getConnectionFactory() {
    ConnectionFactory cf = new ConnectionFactory();

    if (proxyPort != null && proxyPort.length() > 0) {
      cf.setProxyHost(proxyHost);
      cf.setProxyPort(proxyPort);
      if (proxyUsername != null && proxyUsername.length() > 0) {
        cf.setUsername(proxyUsername);
        cf.setPassword(proxyPassword);
      }
    }
    if (proxyRealm != null && proxyRealm.length() > 0) {
      cf.setProxyRealm(proxyRealm);
    }
    return cf;
  }
}
