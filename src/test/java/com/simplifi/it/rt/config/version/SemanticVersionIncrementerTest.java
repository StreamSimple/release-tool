package com.simplifi.it.rt.config.version;

import com.streamsimple.javautil.err.ReturnError;
import org.junit.Assert;
import org.junit.Test;

public class SemanticVersionIncrementerTest
{
  @Test
  public void simpleVersionValidationSuccessTest() {
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
  public void simpleMajorVersionIncrementerTest() {
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
  public void simpleMinorVersionIncrementerTest() {
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
  public void simplePatchVersionIncrementerTest() {
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
