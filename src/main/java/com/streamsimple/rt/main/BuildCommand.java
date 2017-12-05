package com.streamsimple.rt.main;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.rt.executors.ConfigExecutor;

public class BuildCommand extends AbstractBRCommand {
  public static final String BUILD = "build";

  public BuildCommand() {
  }

  private BuildCommand(String configPath) {
    this.configPath = Preconditions.checkNotNull(configPath);
  }

  @Override
  public ReturnError execute() {
    return executeHelper(ConfigExecutor.Type.BUILD);
  }

  @Override
  public String toString() {
    return "BuildCommand{" +
      "configPath='" + configPath + '\'' +
      '}';
  }

  public static class Builder {
    private Injector injector;
    private String configPath;

    public Builder(Injector injector, String configPath) {
      this.injector = Preconditions.checkNotNull(injector);
      this.configPath = Preconditions.checkNotNull(configPath);
    }

    public BuildCommand build() {
      BuildCommand buildCommand = new BuildCommand(configPath);
      injector.injectMembers(buildCommand);
      return buildCommand;
    }
  }
}
