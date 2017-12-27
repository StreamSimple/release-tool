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

import org.junit.Assert;
import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.streamsimple.rt.module.TestModule;

public class ReleaseToolTest
{
  @Test
  public void testParsingBuildOptions()
  {
    final String expectedConfigPath = "src/test/resources/simpleBuildConfig.json";

    Injector injector = Guice.createInjector(new TestModule());
    OptionPackage optionPackage = ReleaseTool.buildCommands(injector);
    JCommander jc = ReleaseTool.buildCommands(optionPackage);
    jc.parse(BuildCommand.BUILD, "-c", expectedConfigPath);

    BuildCommand expectedBuildCommand = new BuildCommand.Builder(injector, expectedConfigPath).build();

    Assert.assertEquals(BuildCommand.BUILD, jc.getParsedCommand());
    BuildCommand build = (BuildCommand)optionPackage.getCommands().get(BuildCommand.BUILD);
    Assert.assertEquals(expectedBuildCommand, build);
  }
}
