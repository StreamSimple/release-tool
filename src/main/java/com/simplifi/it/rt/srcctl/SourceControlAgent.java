package com.simplifi.it.rt.srcctl;

import com.simplifi.it.javautil.err.ReturnError;
import org.apache.commons.lang3.tuple.Pair;

public interface SourceControlAgent
{
  ReturnError checkoutBranch(String branch);
  ReturnError createBranchFrom(String srcBranch, String destBranch);
  Pair<Boolean, ReturnError> hasUncommittedChanges();
}
