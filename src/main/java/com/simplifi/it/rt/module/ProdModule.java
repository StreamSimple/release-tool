package com.simplifi.it.rt.module;

import com.google.inject.AbstractModule;
import com.simplifi.it.rt.executors.CommandExecutor;
import com.simplifi.it.rt.executors.ProcessCommandExecutor;
import com.simplifi.it.rt.srcctl.GitSourceControlAgent;
import com.simplifi.it.rt.srcctl.SourceControlAgent;

public class ProdModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(CommandExecutor.class).to(ProcessCommandExecutor.class).asEagerSingleton();
    bind(SourceControlAgent.Builder.class).to(GitSourceControlAgent.Builder.class);
  }
}
