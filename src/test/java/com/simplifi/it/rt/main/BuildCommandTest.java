package com.simplifi.it.rt.main;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Charsets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.streamsimple.javautils.testutils.DirTestWatcher;
import com.simplifi.it.rt.executors.CommandExecutor;
import com.simplifi.it.rt.executors.MockCommandExecutor;
import com.simplifi.it.rt.module.TestModule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class BuildCommandTest {
  @Rule
  public final DirTestWatcher dirTestWatcher = new DirTestWatcher();

  @Test
  public void test() throws Exception {
    final File repo1 = dirTestWatcher.makeSubDir("repo1");
    final File repo2 = dirTestWatcher.makeSubDir("repo2");
    final File repo3 = dirTestWatcher.makeSubDir("repo3");
    final File repo4 = dirTestWatcher.makeSubDir("repo4");

    final File configFile =
      Paths.get(System.getProperty("testResources"), "buildCommand.json").toFile();
    final File newConfigFile = new File(dirTestWatcher.getDir(), "buildConfig.json");

    String buildConfigContents = FileUtils.readFileToString(configFile, Charsets.UTF_8.name());
    buildConfigContents = buildConfigContents.replace("%1%", repo1.getAbsolutePath());
    buildConfigContents = buildConfigContents.replace("%2%", repo2.getAbsolutePath());
    buildConfigContents = buildConfigContents.replace("%3%", repo3.getAbsolutePath());
    buildConfigContents = buildConfigContents.replace("%4%", repo4.getAbsolutePath());

    FileUtils.writeStringToFile(newConfigFile, buildConfigContents, Charsets.UTF_8.name());

    final Injector injector = Guice.createInjector(new TestModule());
    BuildCommand buildCommand = new BuildCommand.Builder(injector, newConfigFile.getAbsolutePath()).build();
    buildCommand.execute();

    final List<Pair<String, String>> expectedArgs = Lists.newArrayList(
      new ImmutablePair<>(repo1.getAbsolutePath(), "com1"),
      new ImmutablePair<>(repo4.getAbsolutePath(), "com4"),
      new ImmutablePair<>(repo2.getAbsolutePath(), "com2"),
      new ImmutablePair<>(repo3.getAbsolutePath(), "com3"));

    final MockCommandExecutor commandExecutor = (MockCommandExecutor) injector.getInstance(CommandExecutor.class);
    Assert.assertEquals(expectedArgs, commandExecutor.getArgs());
  }
}
