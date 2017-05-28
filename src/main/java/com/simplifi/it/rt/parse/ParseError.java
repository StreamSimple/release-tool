package com.simplifi.it.rt.parse;

import com.google.common.base.Preconditions;

/**
 * Created by tfarkas on 5/28/17.
 */
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
}
