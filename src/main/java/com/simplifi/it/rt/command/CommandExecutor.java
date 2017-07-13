package com.simplifi.it.rt.command;

public interface CommandExecutor {
  int execute(String workingDirectory, String command);
}
