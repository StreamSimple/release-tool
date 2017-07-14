package com.simplifi.it.rt.command;

import com.simplifi.it.javautil.err.ReturnError;

public interface CommandExecutor {
  ReturnError execute(String workingDirectory, String command);
}
