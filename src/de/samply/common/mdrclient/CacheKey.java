package de.samply.common.mdrclient;

import java.util.Objects;

/**
 * Key class for the caching of MDR json requests.
 *
 * @author diogo
 */
public class CacheKey {

  /**
   * Path of the REST call.
   */
  private String path;

  /**
   * Language code that indicates the response language.
   */
  private String languageCode;

  /**
   * Access token, obtained from OSSE.Auth, that is used on REST calls to the MDR when a user is
   * logged in.
   */
  private String accessToken;

  /**
   * The user authentication server id of the user who is logged in. It is used for the cache loader
   * key - the user specific calls are cached across sessions.
   */
  private String userAuthId;

  /**
   * A key used to identify a cache entry.
   *
   * @param path         the MDR REST path to call
   * @param languageCode the language code for the request
   */
  public CacheKey(final String path, final String languageCode) {
    super();
    this.path = path;
    this.languageCode = languageCode;
  }

  /**
   * A key used to identify a cache entry.
   *
   * @param path         the MDR REST path to call
   * @param languageCode the language code for the request
   * @param accessToken  Access token, obtained from OSSE.Auth, that should be used on REST calls to
   *                     the MDR when a user is logged in. Use null for anonymous access.
   * @param userAuthId   The user authentication server id of the user who is logged in. It is used
   *                     for the cache loader key - the user specific calls are cached across
   *                     sessions. Use null for anonymous access.
   */
  public CacheKey(final String path, final String languageCode, final String accessToken,
      final String userAuthId) {
    super();
    this.path = path;
    this.languageCode = languageCode;
    this.accessToken = accessToken;
    this.userAuthId = userAuthId;
  }

  /**
   * Get the MDR REST path to call.
   *
   * @return the MDR REST path to call
   */
  public final String getPath() {
    return path;
  }

  /**
   * Set the MDR REST path to call.
   *
   * @param path the MDR REST path to call
   */
  public final void setPath(final String path) {
    this.path = path;
  }

  /**
   * Get the language code for the request.
   *
   * @return the language code for the request
   */
  public final String getLanguageCode() {
    return languageCode;
  }

  /**
   * Set the language code for the request.
   *
   * @param languageCode the language code for the request
   */
  public final void setLanguageCode(final String languageCode) {
    this.languageCode = languageCode;
  }

  /**
   * Get the access token that should be used on REST calls to the MDR when a user is logged in.
   *
   * @return The access token, obtained from OSSE.Auth.
   */
  public final String getAccessToken() {
    return accessToken;
  }

  /**
   * Set the access token that should be used on REST calls to the MDR when a user is logged in.
   *
   * @param accessToken Access token, obtained from OSSE.Auth.
   */
  public final void setAccessToken(final String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * Get the user authentication server id. It is used for the cache loader key - the user specific
   * calls are cached across sessions.
   *
   * @return The user authentication server id of the user who is logged in, or null for anonymous
   *        access.
   */
  public final String getUserAuthId() {
    return userAuthId;
  }

  /**
   * Set the user authentication server id. It is used for the cache loader key - the user specific
   * calls are cached across sessions.
   *
   * @param userAuthId the user authentication server id of the user who is logged in. Use null for
   *                   anonymous access.
   */
  public final void setUserAuthId(final String userAuthId) {
    this.userAuthId = userAuthId;
  }

  @Override
  public final boolean equals(final Object obj) {
    if (obj instanceof CacheKey) {
      CacheKey other = (CacheKey) obj;
      return other.getPath().equals(this.getPath()) && other.getLanguageCode()
          .equals(this.getLanguageCode())
          && (other.getUserAuthId() == null || other.getUserAuthId().equals(this.getUserAuthId()));
    } else {
      return false;
    }
  }

  @Override
  public final int hashCode() {
    return Objects.hash(this.getPath(), this.getLanguageCode());
  }

  @Override
  public final String toString() {
    return this.getPath() + this.getLanguageCode();
  }
}
