/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsimple.rt.srcctl;

import java.io.File;

import com.streamsimple.commons.lang3.tuple.ImmutablePair;
import com.streamsimple.commons.lang3.tuple.Pair;
import com.streamsimple.javautil.err.ReturnError;

public class MockSourceControlAgent implements SourceControlAgent
{
  @Override
  public ReturnError checkoutBranch(String branch)
  {
    return null;
  }

  @Override
  public ReturnError createBranchFrom(String srcBranch, String destBranch)
  {
    return null;
  }

  @Override
  public Pair<String, ReturnError> getCurrentBranch()
  {
    return null;
  }

  @Override
  public Pair<Boolean, ReturnError> hasUncommittedChanges()
  {
    return new ImmutablePair<>(false, null);
  }

  public static class Builder implements SourceControlAgent.Builder
  {
    @Override
    public SourceControlAgent.Builder setDir(File dir)
    {
      return this;
    }

    @Override
    public Pair<SourceControlAgent, ReturnError> build()
    {
      return new ImmutablePair<>(new MockSourceControlAgent(), null);
    }
  }
}
