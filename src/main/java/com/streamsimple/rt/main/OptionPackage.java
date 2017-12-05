package com.streamsimple.rt.main;

import com.beust.jcommander.internal.Maps;

import java.util.Collections;
import java.util.Map;

public class OptionPackage
{
  private Map<String, Command> commands;

  public OptionPackage(Map<String, Command> commands)
  {
    Map<String, Command> clonedCommands = Maps.newHashMap();
    clonedCommands.putAll(commands);
    this.commands = Collections.unmodifiableMap(commands);
  }

  public Map<String, Command> getCommands()
  {
    return commands;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OptionPackage that = (OptionPackage)o;

    return commands.equals(that.commands);
  }

  @Override
  public int hashCode()
  {
    return commands.hashCode();
  }

  @Override
  public String toString() {
    return "OptionPackage{" +
      "commands=" + commands +
      '}';
  }
}
