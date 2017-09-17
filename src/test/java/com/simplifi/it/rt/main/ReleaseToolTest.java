package com.simplifi.it.rt.main;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simplifi.it.rt.module.ProdModule;
import junit.framework.Assert;
import org.junit.Test;

public class ReleaseToolTest
{
  @Test
  public void testParsingBuildOptions() {
    final String expectedConfigPath = "src/test/resources/simpleBuildConfig.json";

    Injector injector = Guice.createInjector(new ProdModule());
    OptionPackage optionPackage = ReleaseTool.buildCommands(injector);
    JCommander jc = ReleaseTool.buildCommands(optionPackage);
    jc.parse(BuildOptions.BUILD, "-c", expectedConfigPath);

    Assert.assertEquals(BuildOptions.BUILD, jc.getParsedCommand());
    BuildOptions build = (BuildOptions) optionPackage.getCommands().get(BuildOptions.BUILD);
    Assert.assertEquals(expectedConfigPath, build.getConfigPath());
  }
}
