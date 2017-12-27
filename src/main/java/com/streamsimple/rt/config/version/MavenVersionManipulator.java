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
package com.streamsimple.rt.config.version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import com.streamsimple.commons.lang3.tuple.ImmutablePair;
import com.streamsimple.commons.lang3.tuple.Pair;
import com.streamsimple.guava.common.base.Preconditions;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;

public class MavenVersionManipulator implements VersionManipulator
{
  private File pomFile;

  private MavenVersionManipulator(File pomFile)
  {
    this.pomFile = Preconditions.checkNotNull(pomFile);
  }

  @Override
  public Pair<String, ReturnError> getVersion()
  {
    try {
      MavenXpp3Reader reader = new MavenXpp3Reader();
      Model model = reader.read(new FileReader(pomFile));
      return new ImmutablePair<>(model.getVersion(), null);
    } catch (XmlPullParserException | IOException e) {
      String message =
          String.format("Failed to read pom at %s with error: %s", pomFile.getAbsolutePath(), e.getMessage());
      return new ImmutablePair<>(null, new ReturnErrorImpl(message));
    }
  }

  @Override
  public ReturnError replaceProjectVersion(String nextVersion)
  {
    return null;
  }

  @Override
  public ReturnError replaceDependencyVersion(Artifact artifact, String version)
  {
    return null;
  }

  public static class Builder implements VersionManipulator.Builder
  {
    private File pomFile;

    @Override
    public VersionManipulator.Builder setProjectFile(File file)
    {
      pomFile = Preconditions.checkNotNull(file);
      return this;
    }

    @Override
    public VersionManipulator build()
    {
      return new MavenVersionManipulator(pomFile);
    }
  }
}
