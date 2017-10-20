package com.simplifi.it.rt.module;

import com.google.inject.AbstractModule;
import com.simplifi.it.rt.executors.CommandExecutor;
import com.simplifi.it.rt.executors.MockCommandExecutor;
import com.simplifi.it.rt.srcctl.MockSourceControlAgent;
import com.simplifi.it.rt.srcctl.SourceControlAgent;

public class TestModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(CommandExecutor.class).to(MockCommandExecutor.class).asEagerSingleton();
    bind(SourceControlAgent.Builder.class).to(MockSourceControlAgent.Builder.class);
  }
}
