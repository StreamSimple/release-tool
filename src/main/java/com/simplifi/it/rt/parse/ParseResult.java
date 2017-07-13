package com.simplifi.it.rt.parse;

import com.google.common.base.Preconditions;

public class ParseResult<T> {
  private T value;
  private ParseError error;

  public ParseResult(T value)
  {
    this.value = Preconditions.checkNotNull(value);
  }

  public ParseResult(ParseError error)
  {
    this.error = Preconditions.checkNotNull(error);
  }

  public boolean hasError()
  {
    return this.error != null;
  }

  public ParseError getError()
  {
    return error;
  }

  public T getValue()
  {
    return value;
  }
}
