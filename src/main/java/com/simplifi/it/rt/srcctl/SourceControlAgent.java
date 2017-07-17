package com.simplifi.it.rt.srcctl;

import com.simplifi.it.javautil.err.ReturnError;

public interface SourceControlAgent
{
  ReturnError checkoutBranch(String branch);
  ReturnError createBranchFrom(String srcBranch, String destBranch);
  boolean hasUncommittedChanges();
}
