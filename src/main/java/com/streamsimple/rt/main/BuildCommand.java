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

import com.google.inject.Injector;
import com.streamsimple.guava.common.base.Preconditions;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.rt.executors.ConfigExecutor;

public class BuildCommand extends AbstractBRCommand
{
  public static final String BUILD = "build";

  public BuildCommand()
  {
  }

  private BuildCommand(String configPath)
  {
    this.configPath = Preconditions.checkNotNull(configPath);
  }

  @Override
  public ReturnError execute()
  {
    return executeHelper(ConfigExecutor.Type.BUILD);
  }

  @Override
  public String toString()
  {
    return "BuildCommand{" +
      "configPath='" + configPath + '\'' +
      '}';
  }

  public static class Builder
  {
    private Injector injector;
    private String configPath;

    public Builder(Injector injector, String configPath)
    {
      this.injector = Preconditions.checkNotNull(injector);
      this.configPath = Preconditions.checkNotNull(configPath);
    }

    public BuildCommand build()
    {
      BuildCommand buildCommand = new BuildCommand(configPath);
      injector.injectMembers(buildCommand);
      return buildCommand;
    }
  }
}
