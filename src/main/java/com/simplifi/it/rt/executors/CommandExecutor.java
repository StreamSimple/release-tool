package com.simplifi.it.rt.executors;

import com.streamsimple.javautil.err.ReturnError;

public interface CommandExecutor {
  ReturnError execute(String workingDirectory, String command);
}
