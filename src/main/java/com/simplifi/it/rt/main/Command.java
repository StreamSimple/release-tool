package com.simplifi.it.rt.main;

import com.streamsimple.javautil.err.ReturnError;

public interface Command {
  ReturnError execute();
}
