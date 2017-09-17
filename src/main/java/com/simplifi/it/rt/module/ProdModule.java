package com.simplifi.it.rt.module;

import com.google.inject.AbstractModule;
import com.simplifi.it.rt.executors.CommandExecutor;
import com.simplifi.it.rt.executors.ProcessCommandExecutor;

public class ProdModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(CommandExecutor.class).to(ProcessCommandExecutor.class).asEagerSingleton();
  }
}
