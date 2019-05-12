package com.theoryinpractise.halbuilder.xml;

import static org.fest.assertions.api.Assertions.assertThat;

import com.theoryinpractise.halbuilder.api.Contract;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import java.io.InputStreamReader;
import org.testng.annotations.Test;

public class ValidationTest {

  RepresentationFactory representationFactory = new XmlRepresentationFactory();

  ReadableRepresentation representation =
      representationFactory.readRepresentation(
          RepresentationFactory.HAL_XML,
          new InputStreamReader(ValidationTest.class.getResourceAsStream("/example.xml")));

  @Test
  public void testValidation() {

    Contract noWhiteSpaceInName =
        resource -> ((String) resource.getValue("name", "")).matches("\\W*");

    Contract anyCharsInName = resource -> ((String) resource.getValue("name", "")).matches(".*");

    assertThat(representation.isSatisfiedBy(noWhiteSpaceInName)).isFalse();
    assertThat(representation.isSatisfiedBy(anyCharsInName)).isTrue();
  }
}
