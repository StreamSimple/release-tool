package com.simplifi.it.rt.main;

import com.beust.jcommander.JCommander;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.streamsimple.javautil.err.ReturnError;
import com.simplifi.it.rt.module.ProdModule;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ReleaseTool
{
  public static void main(String[] args)
  {
    Injector injector = Guice.createInjector(new ProdModule());
    OptionPackage optionPackage = buildCommands(injector);
    JCommander jc = buildCommands(optionPackage);
    jc.parse(args);
    String commandString = jc.getParsedCommand();

    ReturnError returnError = optionPackage.getCommands().get(commandString).execute();

    if (returnError != null) {
      log.error(returnError.getMessage());
    }
  }

  public static OptionPackage buildCommands(Injector injector)
  {
    Map<String, Command> commands = Maps.newHashMap();

    BuildCommand buildCommand = new BuildCommand();
    ReleaseCommand releaseCommand = new ReleaseCommand();

    injector.injectMembers(buildCommand);
    injector.injectMembers(releaseCommand);

    commands.put(BuildCommand.BUILD, buildCommand);
    commands.put(ReleaseCommand.RELEASE, releaseCommand);

    return new OptionPackage(commands);
  }

  public static JCommander buildCommands(OptionPackage optionPackage) {
    JCommander.Builder builder = JCommander.newBuilder();

    for (Map.Entry<String, Command> command: optionPackage.getCommands().entrySet()) {
      builder.addCommand(command.getKey(), command.getValue());
    }

    return builder.build();
  }
}
