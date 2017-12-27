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

import java.util.List;

import com.streamsimple.commons.lang3.tuple.ImmutablePair;
import com.streamsimple.commons.lang3.tuple.Pair;
import com.streamsimple.guava.common.collect.Lists;
import com.streamsimple.javautil.err.ReturnError;

public class MockCommandExecutor implements CommandExecutor
{
  private List<Pair<String, String>> args = Lists.newArrayList();

  @Override
  public ReturnError execute(String workingDirectory, String command)
  {
    args.add(new ImmutablePair<>(workingDirectory, command));
    return null;
  }

  public List<Pair<String, String>> getArgs()
  {
    return args;
  }
}
