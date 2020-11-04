package de.samply.common.mdrclient.domain;

import com.google.common.base.Predicate;

public class CodeUrnPredicate implements Predicate<Code> {

  private String codeUrn;

  public CodeUrnPredicate(final String codeUrn) {
    this.codeUrn = codeUrn;
  }

  @Override
  public boolean apply(Code code) {
    return codeUrn.equals(code.getIdentification().getUrn());
  }
}
