/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsimple.rt.config.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;

public class SemanticVersionIncrementer implements VersionIncrementer
{
  public static final SemanticVersionIncrementer INSTANCE = new SemanticVersionIncrementer();
  public static final String VERSION_REGEX_STRING = "(\\d)\\.(\\d)\\.(\\d)(-SNAPSHOT)?";
  public static final Pattern VERSION_REGEX = Pattern.compile(VERSION_REGEX_STRING);

  private SemanticVersionIncrementer()
  {
  }

  public ReturnError validateVersion(String version)
  {
    if (!VERSION_REGEX.matcher(version).matches()) {
      String message = String.format("Version: \"%s\" does not match the regex \"%s\"", version, VERSION_REGEX_STRING);
      return new ReturnErrorImpl(message);
    }

    return null;
  }

  @Override
  public String incrementVersion(String version,
      VersionIncrementer.IncrementType type,
      ReleaseType releaseType)
  {
    Matcher matcher = VERSION_REGEX.matcher(version);
    boolean matches = matcher.matches();

    if (!matches) {
      throw new IllegalArgumentException();
    }

    String major = matcher.group(1);
    String minor = matcher.group(2);
    String patch = matcher.group(3);

    long majorLong = Long.parseLong(major);
    long minorLong = Long.parseLong(minor);
    long patchLong = Long.parseLong(patch);

    switch (type) {
      case MAJOR: {
        majorLong++;
        break;
      }
      case MINOR: {
        minorLong++;
        break;
      }
      case PATCH: {
        patchLong++;
        break;
      }
      default: {
        final String message = String.format("Unsupported incrementer type: %s", type);
        throw new UnsupportedOperationException(message);
      }
    }

    String newVersionString = String.format("%s.%s.%s", majorLong, minorLong, patchLong);

    if (releaseType == ReleaseType.SNAPSHOT) {
      newVersionString = newVersionString + "-SNAPSHOT";
    }

    return newVersionString;
  }

  static class Builder implements VersionIncrementer.Builder
  {
    @Override
    public VersionIncrementer build(Config config)
    {
      return INSTANCE;
    }
  }
}
