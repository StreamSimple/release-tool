package com.streamsimple.rt.main;

import com.beust.jcommander.Parameter;
import com.google.inject.Inject;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;
import com.streamsimple.rt.config.ConfigFile;
import com.streamsimple.rt.executors.CommandExecutor;
import com.streamsimple.rt.executors.ConfigExecutor;
import com.streamsimple.rt.parse.ParseException;
import com.streamsimple.rt.srcctl.SourceControlAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class AbstractBRCommand implements Command {
  @Parameter(names = {"-c"}, description = "The path of the build config.", required = true)
  protected String configPath;

  @Inject
  protected CommandExecutor commandExecutor;

  @Inject
  protected SourceControlAgent.Builder agentBuilder;

  public ReturnError executeHelper(ConfigExecutor.Type type) {
    try (FileInputStream fileInputStream = new FileInputStream(new File(configPath))) {
      ConfigFile configFile = ConfigFile.parse(fileInputStream);
      ConfigExecutor configExecutor = new ConfigExecutor(commandExecutor, agentBuilder);
      return configExecutor.execute(configFile, type);
    } catch (ParseException | IOException e) {
      return new ReturnErrorImpl(e.getMessage());
    }
  }

  public String getConfigPath() {
    return configPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BuildCommand that = (BuildCommand) o;

    return configPath.equals(that.configPath);
  }

  @Override
  public int hashCode() {
    return configPath.hashCode();
  }
}
