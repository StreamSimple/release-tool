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
}
