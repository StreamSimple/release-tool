package com.streamsimple.rt.module;

import com.google.inject.AbstractModule;
import com.streamsimple.rt.executors.CommandExecutor;
import com.streamsimple.rt.executors.MockCommandExecutor;
import com.streamsimple.rt.srcctl.MockSourceControlAgent;
import com.streamsimple.rt.srcctl.SourceControlAgent;

public class TestModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(CommandExecutor.class).to(MockCommandExecutor.class).asEagerSingleton();
    bind(SourceControlAgent.Builder.class).to(MockSourceControlAgent.Builder.class);
  }
}
