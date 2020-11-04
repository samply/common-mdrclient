package de.samply.common.mdrclient;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for the cache of the MDR.
 *
 * @author diogo
 */
public final class CacheManager {

  /**
   * Maximum size of the cache.
   */
  private static final int CACHE_MAXIMUM_SIZE = 10000;

  /**
   * The cache instance.
   */
  private static LoadingCache<CacheKey, String> jsonCache;

  /**
   * The logger for this class.
   */
  private static Logger logger = LoggerFactory.getLogger(CacheManager.class.getName());

  /**
   * Prevent instantiation.
   */
  private CacheManager() {
  }

  /**
   * Static factory method to obtain a cache instance.
   *
   * @param mdrClient MDR Client instance, to be reused (with path and proxy settings already
   *                  loaded).
   * @return cache instance
   */
  public static LoadingCache<CacheKey, String> getCache(final MdrClient mdrClient) {
    if (jsonCache == null) {
      CacheLoader<CacheKey, String> loader = new CacheLoader<CacheKey, String>() {
        public String load(final CacheKey key) throws MdrConnectionException {
          logger.debug(
              "Element was not in cache: " + key.getPath() + " | " + key.getLanguageCode() + " | "
                  + key.getAccessToken());
          if (key.getAccessToken() != null) {
            return mdrClient
                .getJsonFromMdr(key.getPath(), key.getLanguageCode(), key.getAccessToken());
          } else {
            return mdrClient.getJsonFromMdr(key.getPath(), key.getLanguageCode());
          }
        }
      };
      jsonCache = CacheBuilder.newBuilder().maximumSize(CACHE_MAXIMUM_SIZE).build(loader);
    }

    return jsonCache;
  }

  /**
   * Cleans the cache of a specific MDR Client.
   *
   * @param mdrClient the MDR Client for which the cache will be cleaned.
   */
  public static void cleanCache(final MdrClient mdrClient) {
    getCache(mdrClient).invalidateAll();
    logger.debug("Cache cleaned.");
  }
}
