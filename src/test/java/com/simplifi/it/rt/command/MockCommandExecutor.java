package com.simplifi.it.rt.command;

import com.google.common.collect.Lists;
import com.simplifi.it.javautil.err.ReturnError;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class MockCommandExecutor implements CommandExecutor
{
  private List<Pair<String, String>> args = Lists.newArrayList();

  @Override
  public ReturnError execute(String workingDirectory, String command) {
    args.add(new ImmutablePair<>(workingDirectory, command));

    return null;
  }

  public List<Pair<String, String>> getArgs() {
    return args;
  }
}
