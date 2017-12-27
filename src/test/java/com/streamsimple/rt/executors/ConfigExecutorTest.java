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
package com.streamsimple.rt.executors;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.streamsimple.commons.lang3.tuple.ImmutablePair;
import com.streamsimple.commons.lang3.tuple.Pair;
import com.streamsimple.guava.common.collect.Lists;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.rt.config.ConfigFile;
import com.streamsimple.rt.config.ConfigFileTest;
import com.streamsimple.rt.parse.ParseException;
import com.streamsimple.rt.srcctl.MockSourceControlAgent;

public class ConfigExecutorTest
{
  @Test
  public void simpleBuildExecutorTest() throws ParseException
  {
    MockCommandExecutor commandExecutor = new MockCommandExecutor();

    InputStream inputStream = ConfigFileTest.class.getClassLoader()
        .getResourceAsStream("simpleBuildConfigWithCmd.json");

    ConfigFile result = ConfigFile.parse(inputStream);

    ConfigExecutor configExecutor = new ConfigExecutor(commandExecutor, new MockSourceControlAgent.Builder());
    ReturnError returnError = configExecutor.execute(result, ConfigExecutor.Type.BUILD);
    Assert.assertNull(returnError);

    List<Pair<String, String>> expectedPairs = Lists.newArrayList();
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/1", "cmd1"));
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/2", "cmd2"));
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/3", "cmd3"));

    List<Pair<String, String>> actualPairs = commandExecutor.getArgs();

    Assert.assertEquals(expectedPairs, actualPairs);
  }
}
