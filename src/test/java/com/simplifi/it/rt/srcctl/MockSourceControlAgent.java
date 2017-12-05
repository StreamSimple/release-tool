package com.simplifi.it.rt.srcctl;

import com.streamsimple.javautil.err.ReturnError;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

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
    return new ImmutablePair<>(false, null);
  }

  public static class Builder implements SourceControlAgent.Builder {
    @Override
    public SourceControlAgent.Builder setDir(File dir) {
      return this;
    }

    @Override
    public Pair<SourceControlAgent, ReturnError> build() {
      return new ImmutablePair<>(new MockSourceControlAgent(), null);
    }
  }
}
