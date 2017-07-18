package com.simplifi.it.rt.config.version;

import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.config.Configurable;
import org.apache.commons.lang3.tuple.Pair;

public interface VersionManipulator extends Configurable
{
  Pair<String, ReturnError> getVersion();
  ReturnError replaceVersion(String nextVersion);
}
