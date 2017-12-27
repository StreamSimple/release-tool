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

import org.junit.Assert;
import org.junit.Test;

import com.streamsimple.javautil.err.ReturnError;

public class SemanticVersionIncrementerTest
{
  @Test
  public void simpleVersionValidationSuccessTest()
  {
    SemanticVersionIncrementer incrementer = SemanticVersionIncrementer.INSTANCE;

    ReturnError validateError = incrementer.validateVersion("1.5.0");
    Assert.assertNull(validateError);

    validateError = incrementer.validateVersion("1.5.0-SNAPSHOT");
    Assert.assertNull(validateError);

    validateError = incrementer.validateVersion("1.5.0SNAPSHOT");
    Assert.assertNotNull(validateError);

    validateError = incrementer.validateVersion("1.0-SNAPSHOT");
    Assert.assertNotNull(validateError);

    validateError = incrementer.validateVersion("1.0");
    Assert.assertNotNull(validateError);

    validateError = incrementer.validateVersion("1/5/0-SNAPSHOT");
    Assert.assertNotNull(validateError);
  }

  @Test
  public void simpleMajorVersionIncrementerTest()
  {
    SemanticVersionIncrementer incrementer = SemanticVersionIncrementer.INSTANCE;

    String actualIncrement = incrementer.incrementVersion("1.5.0-SNAPSHOT",
        VersionIncrementer.IncrementType.MAJOR,
        VersionIncrementer.ReleaseType.SNAPSHOT);

    Assert.assertEquals("2.5.0-SNAPSHOT", actualIncrement);

    actualIncrement = incrementer.incrementVersion("1.5.0-SNAPSHOT",
        VersionIncrementer.IncrementType.MAJOR,
        VersionIncrementer.ReleaseType.RELEASE);

    Assert.assertEquals("2.5.0", actualIncrement);
  }

  @Test
  public void simpleMinorVersionIncrementerTest()
  {
    SemanticVersionIncrementer incrementer = SemanticVersionIncrementer.INSTANCE;

    String actualIncrement = incrementer.incrementVersion("1.5.0-SNAPSHOT",
        VersionIncrementer.IncrementType.MINOR,
        VersionIncrementer.ReleaseType.SNAPSHOT);

    Assert.assertEquals("1.6.0-SNAPSHOT", actualIncrement);

    actualIncrement = incrementer.incrementVersion("1.5.0-SNAPSHOT",
        VersionIncrementer.IncrementType.MINOR,
        VersionIncrementer.ReleaseType.RELEASE);

    Assert.assertEquals("1.6.0", actualIncrement);
  }

  @Test
  public void simplePatchVersionIncrementerTest()
  {
    SemanticVersionIncrementer incrementer = SemanticVersionIncrementer.INSTANCE;

    String actualIncrement = incrementer.incrementVersion("1.5.0-SNAPSHOT",
        VersionIncrementer.IncrementType.PATCH,
        VersionIncrementer.ReleaseType.SNAPSHOT);

    Assert.assertEquals("1.5.1-SNAPSHOT", actualIncrement);

    actualIncrement = incrementer.incrementVersion("1.5.0-SNAPSHOT",
        VersionIncrementer.IncrementType.PATCH,
        VersionIncrementer.ReleaseType.RELEASE);

    Assert.assertEquals("1.5.1", actualIncrement);
  }
}
