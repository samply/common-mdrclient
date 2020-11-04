
package de.samply.common.mdrclient.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A result list of results.
 *
 * @author diogo
 * @see Result
 */
public class ResultList {

  /**
   * List of results.
   *
   * @see Result
   */
  private List<Result> results = new ArrayList<Result>();

  /**
   * The number of results, given by the MDR.
   */
  private Integer totalcount;

  /**
   * Get the result list.
   *
   * @return the result list
   */
  public final List<Result> getResults() {
    return results;
  }

  /**
   * Set the result list.
   *
   * @param results the list of results
   */
  public final void setResults(final List<Result> results) {
    this.results = results;
  }

  /**
   * Get the number of results.
   *
   * @return the number of results
   */
  public final Integer getTotalcount() {
    return totalcount;
  }

  /**
   * Set the number of results.
   *
   * @param totalcount the number of results
   */
  public final void setTotalcount(final Integer totalcount) {
    this.totalcount = totalcount;
  }
}
