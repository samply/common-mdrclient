package de.samply.common.mdrclient.domain;

import com.google.common.base.Predicate;

public class CodePredicate implements Predicate<Code> {

  private String codeString;

  public CodePredicate(final String codeString) {
    this.codeString = codeString;
  }

  @Override
  public boolean apply(Code code) {
    return codeString.equals(code.getCode());
  }
}
