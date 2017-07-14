package com.simplifi.it.rt.parse;

import com.google.common.base.Preconditions;

public class ParseResult<T> {
  private T value;

  public ParseResult(T value)
  {
    this.value = Preconditions.checkNotNull(value);
  }

  public T getValue()
  {
    return value;
  }
}
