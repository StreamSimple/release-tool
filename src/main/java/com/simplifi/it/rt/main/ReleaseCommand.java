package com.simplifi.it.rt.main;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.executors.ConfigExecutor;

public class ReleaseCommand extends AbstractBRCommand {
  public static final String RELEASE = "release";

  public ReleaseCommand() {
  }

  public ReleaseCommand(String configPath) {
    this.configPath = Preconditions.checkNotNull(configPath);
  }

  @Override
  public ReturnError execute() {
    return executeHelper(ConfigExecutor.Type.RELEASE);
  }

  @Override
  public String toString() {
    return "ReleaseCommand{" +
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
