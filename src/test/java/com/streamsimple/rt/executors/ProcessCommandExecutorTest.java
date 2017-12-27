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

import java.io.File;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautils.testutils.DirTestWatcher;

public class ProcessCommandExecutorTest
{
  @Rule
  public DirTestWatcher dirTestWatcher = new DirTestWatcher();

  @Test
  public void simpleProcessCommand()
  {
    final String command = String.format("bash -c %s/src/test/resources/scripts/makefile.sh",
        new File(".").getAbsolutePath());
    ProcessCommandExecutor commandExecutor = new ProcessCommandExecutor();

    ReturnError returnError = commandExecutor.execute(dirTestWatcher.getDirPath(), command);

    Assert.assertNull(returnError);
    Assert.assertTrue(new File(dirTestWatcher.getDir(), "test.txt").exists());
  }
}
