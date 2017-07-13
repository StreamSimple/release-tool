package com.simplifi.it.rt.srcctl;

import com.google.common.base.Preconditions;

public interface SourceControlAgent
{
  Error checkoutBranch(String branch);
  Error createBranchFrom(String srcBranch, String destBranch);
  boolean hasUncommittedChanges();

  class Error
  {
    private String message;

    public Error()
    {
    }

    public Error(String message)
    {
      this.message = Preconditions.checkNotNull(message);
    }

    public String getMessage()
    {
      return message;
    }

    @Override
    public String toString()
    {
      return "Error{" +
        "message='" + message + '\'' +
        '}';
    }
  }
}
