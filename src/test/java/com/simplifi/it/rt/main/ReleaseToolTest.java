package com.simplifi.it.rt.main;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simplifi.it.rt.module.TestModule;
import org.junit.Assert;
import org.junit.Test;

public class ReleaseToolTest
{
  @Test
  public void testParsingBuildOptions() {
    final String expectedConfigPath = "src/test/resources/simpleBuildConfig.json";

    Injector injector = Guice.createInjector(new TestModule());
    OptionPackage optionPackage = ReleaseTool.buildCommands(injector);
    JCommander jc = ReleaseTool.buildCommands(optionPackage);
    jc.parse(BuildCommand.BUILD, "-c", expectedConfigPath);

    BuildCommand expectedBuildCommand = new BuildCommand.Builder(injector, expectedConfigPath).build();

    Assert.assertEquals(BuildCommand.BUILD, jc.getParsedCommand());
    BuildCommand build = (BuildCommand) optionPackage.getCommands().get(BuildCommand.BUILD);
    Assert.assertEquals(expectedBuildCommand, build);
  }
}
