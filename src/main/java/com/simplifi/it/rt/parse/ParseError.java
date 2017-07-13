package com.simplifi.it.rt.parse;

import com.google.common.base.Preconditions;

public class ParseError
{
  private String message;
  private Exception exception;

  public ParseError(String message)
  {
    this.message = Preconditions.checkNotNull(message);
  }

  public ParseError(String message, Exception exception)
  {
    this.message = Preconditions.checkNotNull(message);
    this.exception = Preconditions.checkNotNull(exception);
  }

  public ParseError(Exception exception)
  {
    this.exception = Preconditions.checkNotNull(exception);
  }

  public String getMessage()
  {
    return message;
  }

  public Exception getException()
  {
    return exception;
  }

  @Override
  public String toString() {
    return "ParseError{" +
      "message='" + message + '\'' +
      ", exception=" + exception +
      '}';
  }
}
