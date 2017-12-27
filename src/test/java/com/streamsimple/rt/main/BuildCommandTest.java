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
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.streamsimple.commons.io.FileUtils;
import com.streamsimple.commons.lang3.tuple.ImmutablePair;
import com.streamsimple.commons.lang3.tuple.Pair;
import com.streamsimple.guava.common.base.Charsets;
import com.streamsimple.javautils.testutils.DirTestWatcher;
import com.streamsimple.rt.executors.CommandExecutor;
import com.streamsimple.rt.executors.MockCommandExecutor;
import com.streamsimple.rt.module.TestModule;

public class BuildCommandTest
{
  @Rule
  public final DirTestWatcher dirTestWatcher = new DirTestWatcher();

  @Test
  public void test() throws Exception
  {
    final File repo1 = dirTestWatcher.makeSubDir("repo1");
    final File repo2 = dirTestWatcher.makeSubDir("repo2");
    final File repo3 = dirTestWatcher.makeSubDir("repo3");
    final File repo4 = dirTestWatcher.makeSubDir("repo4");

    final File configFile =
        Paths.get(System.getProperty("testResources"), "buildCommand.json").toFile();
    final File newConfigFile = new File(dirTestWatcher.getDir(), "buildConfig.json");

    String buildConfigContents = FileUtils.readFileToString(configFile, Charsets.UTF_8.name());
    buildConfigContents = buildConfigContents.replace("%1%", repo1.getAbsolutePath());
    buildConfigContents = buildConfigContents.replace("%2%", repo2.getAbsolutePath());
    buildConfigContents = buildConfigContents.replace("%3%", repo3.getAbsolutePath());
    buildConfigContents = buildConfigContents.replace("%4%", repo4.getAbsolutePath());

    FileUtils.writeStringToFile(newConfigFile, buildConfigContents, Charsets.UTF_8.name());

    final Injector injector = Guice.createInjector(new TestModule());
    BuildCommand buildCommand = new BuildCommand.Builder(injector, newConfigFile.getAbsolutePath()).build();
    buildCommand.execute();

    final List<Pair<String, String>> expectedArgs = Lists.newArrayList(
        new ImmutablePair<>(repo1.getAbsolutePath(), "com1"),
        new ImmutablePair<>(repo4.getAbsolutePath(), "com4"),
        new ImmutablePair<>(repo2.getAbsolutePath(), "com2"),
        new ImmutablePair<>(repo3.getAbsolutePath(), "com3"));

    final MockCommandExecutor commandExecutor = (MockCommandExecutor)injector.getInstance(CommandExecutor.class);
    Assert.assertEquals(expectedArgs, commandExecutor.getArgs());
  }
}
