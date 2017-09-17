package com.simplifi.it.rt.main;

import com.simplifi.it.javautil.err.ReturnError;

public interface Command {
  ReturnError execute();
}
