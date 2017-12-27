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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.codehaus.plexus.util.cli.CommandLineUtils;

import com.streamsimple.guava.common.base.Preconditions;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessCommandExecutor implements CommandExecutor
{
  @Override
  public ReturnError execute(String workingDirectory, String command)
  {
    log.info("Running command \"{}\" in directory {}", command, workingDirectory);

    String[] args;

    try {
      args = CommandLineUtils.translateCommandline(command);
    } catch (Exception e) {
      return new ReturnErrorImpl(String.format("Error translating command: %s", e.getMessage()));
    }

    Process process;

    try {
      ProcessBuilder pb = new ProcessBuilder();
      process = pb.command(args)
        .directory(new File(workingDirectory))
        .start();
    } catch (IOException e) {
      return new ReturnErrorImpl(String.format("Error running command: %s", e.getMessage()));
    }

    Thread stdout = new Thread(new LogInputStream(LogInputStream.Type.INFO, process.getInputStream()));
    Thread stderr = new Thread(new LogInputStream(LogInputStream.Type.ERROR, process.getErrorStream()));

    stdout.start();
    stderr.start();

    try {
      process.waitFor();
    } catch (InterruptedException e) {
      return new ReturnErrorImpl("Command interrupted");
    }

    return null;
  }

  @Slf4j
  public static class LogInputStream implements Runnable
  {
    public enum Type
    {
      ERROR,
      INFO
    }

    private Type type;
    private InputStream inputStream;

    public LogInputStream(Type type, InputStream inputStream)
    {
      this.type = Preconditions.checkNotNull(type);
      this.inputStream = Preconditions.checkNotNull(inputStream);
    }

    @Override
    public void run()
    {
      try {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
          String line;

          while ((line = bufferedReader.readLine()) != null) {
            switch (type) {
              case ERROR:
                log.error(line);
                break;
              case INFO:
                log.info(line);
                break;
              default:
                throw new IllegalArgumentException(String.format("Unsupported type %s", type));
            }
          }
        }
      } catch (IOException e) {
        log.error("Error while reading process output", e);
      }
    }
  }
}
