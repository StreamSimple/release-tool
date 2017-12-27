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

import java.util.Map;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.streamsimple.guava.common.collect.Maps;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.rt.module.ProdModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReleaseTool
{
  public static void main(String[] args)
  {
    Injector injector = Guice.createInjector(new ProdModule());
    OptionPackage optionPackage = buildCommands(injector);
    JCommander jc = buildCommands(optionPackage);
    jc.parse(args);
    String commandString = jc.getParsedCommand();

    ReturnError returnError = optionPackage.getCommands().get(commandString).execute();

    if (returnError != null) {
      log.error(returnError.getMessage());
    }
  }

  public static OptionPackage buildCommands(Injector injector)
  {
    Map<String, Command> commands = Maps.newHashMap();

    BuildCommand buildCommand = new BuildCommand();
    ReleaseCommand releaseCommand = new ReleaseCommand();

    injector.injectMembers(buildCommand);
    injector.injectMembers(releaseCommand);

    commands.put(BuildCommand.BUILD, buildCommand);
    commands.put(ReleaseCommand.RELEASE, releaseCommand);

    return new OptionPackage(commands);
  }

  public static JCommander buildCommands(OptionPackage optionPackage)
  {
    JCommander.Builder builder = JCommander.newBuilder();

    for (Map.Entry<String, Command> command: optionPackage.getCommands().entrySet()) {
      builder.addCommand(command.getKey(), command.getValue());
    }

    return builder.build();
  }
}
