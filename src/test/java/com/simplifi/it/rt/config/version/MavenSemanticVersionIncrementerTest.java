package com.simplifi.it.rt.config.version;

import com.simplifi.it.javautil.err.ReturnError;
import org.junit.Assert;
import org.junit.Test;

public class MavenSemanticVersionIncrementerTest
{
  @Test
  public void simpleHasConfig() {
    Assert.assertFalse(MavenSemanticVersionIncrementer.INSTANCE.hasConfig());
  }

  @Test
  public void simpleVersionValidationSuccessTest() {
    MavenSemanticVersionIncrementer incrementer = MavenSemanticVersionIncrementer.INSTANCE;

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
    MavenSemanticVersionIncrementer incrementer = MavenSemanticVersionIncrementer.INSTANCE;

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
    MavenSemanticVersionIncrementer incrementer = MavenSemanticVersionIncrementer.INSTANCE;

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
    MavenSemanticVersionIncrementer incrementer = MavenSemanticVersionIncrementer.INSTANCE;

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
