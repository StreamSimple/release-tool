package com.streamsimple.rt.executors;

import com.streamsimple.javautil.err.ReturnError;

public interface CommandExecutor {
  ReturnError execute(String workingDirectory, String command);
}
