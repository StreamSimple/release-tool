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
package com.streamsimple.rt.main;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;
import com.streamsimple.guava.common.base.Preconditions;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.rt.config.version.VersionIncrementer;
import com.streamsimple.rt.executors.ConfigExecutor;

public class ReleaseCommand extends AbstractBRCommand
{
  public static final String RELEASE = "release";

  enum ReleaseSet
  {
    ALL,
    SELF,
    SELF_AND_DESCENDANTS,
    SELF_AND_ANCESTORS
  }

  @Parameter(names = {"-i"}, description = "The type version increment we want to do.", required = true)
  private String incrementType;

  @Parameter(names = {"-s"}, description = "The set of repos we want to do a release of.", required = true)
  private String releaseSet;

  @Parameter(names = {"-r"}, description = "The name of the repo we want to do the release relative to.")
  private String referenceRepo;

  public ReleaseCommand()
  {
  }

  private ReleaseCommand(String configPath, String incrementType, String releaseSet, String referenceRepo)
  {
    this.configPath = Preconditions.checkNotNull(configPath);
    this.incrementType = Preconditions.checkNotNull(incrementType);
    this.releaseSet = Preconditions.checkNotNull(releaseSet);
    this.referenceRepo = Preconditions.checkNotNull(referenceRepo);
  }

  public VersionIncrementer.IncrementType getIncrementerType()
  {
    return VersionIncrementer.IncrementType.valueOf(incrementType);
  }

  public ReleaseSet getReleaseSet()
  {
    return ReleaseSet.valueOf(releaseSet);
  }

  public String getReferenceRepo()
  {
    return referenceRepo;
  }

  @Override
  public ReturnError execute()
  {
    return executeHelper(ConfigExecutor.Type.RELEASE);
  }

  @Override
  public String toString()
  {
    return "ReleaseCommand{" +
      "configPath='" + configPath + '\'' +
      '}';
  }

  public static class Builder
  {
    private Injector injector;
    private String configPath;
    private String incrementType;
    private String releaseSet;
    private String referenceRepo;

    public Builder(Injector injector, String configPath)
    {
      this.injector = Preconditions.checkNotNull(injector);
      this.configPath = Preconditions.checkNotNull(configPath);
    }

    public Builder setIncrementType(String incrementType)
    {
      this.incrementType = Preconditions.checkNotNull(incrementType);
      return this;
    }

    public Builder setReleaseSet(String releaseSet)
    {
      this.releaseSet = Preconditions.checkNotNull(releaseSet);
      return this;
    }

    public Builder setReferenceRepo(String referenceRepo)
    {
      this.referenceRepo = Preconditions.checkNotNull(referenceRepo);
      return this;
    }

    public ReleaseCommand build()
    {
      ReleaseCommand releaseCommand = new ReleaseCommand(configPath, incrementType, releaseSet, referenceRepo);
      injector.injectMembers(releaseCommand);
      return releaseCommand;
    }
  }
}
