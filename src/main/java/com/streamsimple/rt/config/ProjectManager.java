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
package com.streamsimple.rt.config;

import com.streamsimple.guava.common.base.Preconditions;
import com.streamsimple.guava.common.base.Throwables;
import com.streamsimple.rt.config.version.VersionManipulator;

public enum ProjectManager
{
  MAVEN("mvn clean install",
    "mvn deploy -DskipTests",
    VersionManipulator.Builder.class);

  private String defaultBuildCommand;
  private String defaultReleaseCommand;
  private Class<VersionManipulator.Builder> versionManipulator;

  ProjectManager(String defaultBuildCommand,
      String defaultReleaseCommand,
      Class<VersionManipulator.Builder> versionManipulator)
  {
    this.defaultBuildCommand = Preconditions.checkNotNull(defaultBuildCommand);
    this.defaultReleaseCommand = Preconditions.checkNotNull(defaultReleaseCommand);
    this.versionManipulator = Preconditions.checkNotNull(versionManipulator);
  }

  public String getDefaultBuildCommand()
  {
    return defaultBuildCommand;
  }

  public String getDefaultReleaseCommand()
  {
    return defaultReleaseCommand;
  }

  public VersionManipulator.Builder getVersionManipulatorBuilder()
  {
    try {
      return versionManipulator.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      Throwables.propagate(e);
    }

    // Make the compiler happy
    return null;
  }
}
