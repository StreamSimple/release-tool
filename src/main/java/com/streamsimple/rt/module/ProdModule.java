package com.streamsimple.rt.module;

import com.google.inject.AbstractModule;
import com.streamsimple.rt.executors.CommandExecutor;
import com.streamsimple.rt.executors.ProcessCommandExecutor;
import com.streamsimple.rt.srcctl.GitSourceControlAgent;
import com.streamsimple.rt.srcctl.SourceControlAgent;

public class ProdModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(CommandExecutor.class).to(ProcessCommandExecutor.class).asEagerSingleton();
    bind(SourceControlAgent.Builder.class).to(GitSourceControlAgent.Builder.class);
  }
}
