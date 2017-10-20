package com.simplifi.it.rt.srcctl;

import com.simplifi.it.javautil.err.ReturnError;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public interface SourceControlAgent
{
  ReturnError checkoutBranch(String branch);
  ReturnError createBranchFrom(String srcBranch, String destBranch);
  Pair<String, ReturnError> getCurrentBranch();
  Pair<Boolean, ReturnError> hasUncommittedChanges();

  interface Builder {
    Builder setDir(File dir);
    Pair<SourceControlAgent, ReturnError> build();
  }
}
