package com.simplifi.it.rt.srcctl;

import com.simplifi.it.javautil.err.ReturnError;
import org.apache.commons.lang3.tuple.Pair;

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
  public Pair<String, ReturnError> getCurrentBranch() {
    return null;
  }

  @Override
  public Pair<Boolean, ReturnError> hasUncommittedChanges() {
    return null;
  }
}
