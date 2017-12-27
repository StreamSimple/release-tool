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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.beust.jcommander.Parameter;
import com.google.inject.Inject;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;
import com.streamsimple.rt.config.ConfigFile;
import com.streamsimple.rt.executors.CommandExecutor;
import com.streamsimple.rt.executors.ConfigExecutor;
import com.streamsimple.rt.parse.ParseException;
import com.streamsimple.rt.srcctl.SourceControlAgent;

public abstract class AbstractBRCommand implements Command
{
  @Parameter(names = {"-c"}, description = "The path of the build config.", required = true)
  protected String configPath;

  @Inject
  protected CommandExecutor commandExecutor;

  @Inject
  protected SourceControlAgent.Builder agentBuilder;

  public ReturnError executeHelper(ConfigExecutor.Type type)
  {
    try (FileInputStream fileInputStream = new FileInputStream(new File(configPath))) {
      ConfigFile configFile = ConfigFile.parse(fileInputStream);
      ConfigExecutor configExecutor = new ConfigExecutor(commandExecutor, agentBuilder);
      return configExecutor.execute(configFile, type);
    } catch (ParseException | IOException e) {
      return new ReturnErrorImpl(e.getMessage());
    }
  }

  public String getConfigPath()
  {
    return configPath;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BuildCommand that = (BuildCommand)o;

    return configPath.equals(that.configPath);
  }

  @Override
  public int hashCode()
  {
    return configPath.hashCode();
  }
}
