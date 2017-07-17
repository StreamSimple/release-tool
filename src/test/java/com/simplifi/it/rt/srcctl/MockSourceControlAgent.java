package com.simplifi.it.rt.srcctl;

import com.simplifi.it.javautil.err.ReturnError;

public class MockSourceControlAgent implements SourceControlAgent
{
  @Override
  public ReturnError checkoutBranch(String branch) {
    return null;
  }

  @Override
  public ReturnError createBranchFrom(String srcBranch, String destBranch) {
    return null;
  }

  @Override
  public boolean hasUncommittedChanges() {
    return false;
  }
}
