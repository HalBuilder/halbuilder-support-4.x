package com.theoryinpractise.halbuilder.xml;

import com.theoryinpractise.halbuilder.api.Contract;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.RepresentationException;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import com.theoryinpractise.halbuilder.impl.bytecode.InterfaceContract;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.InputStreamReader;

import static com.google.common.truth.Truth.assertThat;
import static org.testng.Assert.fail;

public class InterfaceSatisfactionTest {

  private RepresentationFactory representationFactory = new XmlRepresentationFactory();

  public interface IPerson {
    Integer getAge();

    Boolean getExpired();

    Integer getId();

    String getName();
  }

  public interface INamed {
    String name();
  }

  public interface IJob {
    Integer getJobId();
  }

  public interface ISimpleJob {
    Integer jobId();
  }

  public interface INullprop {
    String nullprop();
  }

  @DataProvider
  public Object[][] providerSatisfactionData() {
    return new Object[][] {
      {IPerson.class, true},
      {INamed.class, true},
      {IJob.class, false},
      {ISimpleJob.class, false},
    };
  }

  @DataProvider
  public Object[][] provideSatisfactionResources() {
    return new Object[][] {
      {
        representationFactory.readRepresentation(
            RepresentationFactory.HAL_XML, new InputStreamReader(InterfaceSatisfactionTest.class.getResourceAsStream("/example.xml"))),
        representationFactory.readRepresentation(
            RepresentationFactory.HAL_XML, new InputStreamReader(InterfaceSatisfactionTest.class.getResourceAsStream("/exampleWithNullProperty.xml")))
      }
    };
  }

  @Test(dataProvider = "providerSatisfactionData")
  public void testSimpleInterfaceSatisfaction(Class<?> aClass, boolean shouldBeSatisfied) {

    ReadableRepresentation representation =
        representationFactory.readRepresentation(
            RepresentationFactory.HAL_XML, new InputStreamReader(InterfaceSatisfactionTest.class.getResourceAsStream("/example.xml")));
    assertThat(representation.isSatisfiedBy(InterfaceContract.newInterfaceContract(aClass))).isEqualTo(shouldBeSatisfied);
  }

  @Test(dataProvider = "provideSatisfactionResources")
  public void testAnonymousInnerContractSatisfaction(ReadableRepresentation representation, ReadableRepresentation nullPropertyRepresentation) {

    Contract contractHasName = resource -> resource.getProperties().containsKey("name");

    Contract contractHasOptional = resource -> resource.getProperties().containsKey("optional");

    @SuppressWarnings("NullAway")
    Contract contractHasOptionalFalse =
        resource -> resource.getProperties().containsKey("optional") && resource.getProperties().get("optional").equals("false");

    Contract contractHasNullProperty = resource -> resource.getProperties().containsKey("nullprop") && resource.getProperties().get("nullprop") == null;

    assertThat(representation.isSatisfiedBy(contractHasName)).isEqualTo(true);
    assertThat(representation.isSatisfiedBy(contractHasOptional)).isEqualTo(true);
    assertThat(representation.isSatisfiedBy(contractHasOptionalFalse)).isEqualTo(false);
    assertThat(representation.isSatisfiedBy(contractHasNullProperty)).isEqualTo(false);

    assertThat(nullPropertyRepresentation.isSatisfiedBy(contractHasNullProperty)).isEqualTo(true);
  }

  @Test
  public void testClassRendering() {
    ReadableRepresentation representation =
        representationFactory.readRepresentation(
            RepresentationFactory.HAL_XML, new InputStreamReader(InterfaceSatisfactionTest.class.getResourceAsStream("/example.xml")));

    assertThat(representation.toClass(INamed.class).name()).isEqualTo("Example Resource");
    assertThat(representation.toClass(IPerson.class).getName()).isEqualTo("Example Resource");
    try {
      representation.toClass(ISimpleJob.class);
      fail("RepresentationException expected");
    } catch (RepresentationException e) {
      //
    }
    try {
      representation.toClass(IJob.class);
      fail("RepresentationException expected");
    } catch (RepresentationException e) {
      //
    }
  }

  @Test
  public void testNullPropertyClassRendering() {
    ReadableRepresentation representation =
        representationFactory.readRepresentation(
            RepresentationFactory.HAL_XML, new InputStreamReader(InterfaceSatisfactionTest.class.getResourceAsStream("/exampleWithNullProperty.xml")));

    assertThat(representation.toClass(INullprop.class)).isNotNull();
    assertThat(representation.toClass(INullprop.class).nullprop()).isNull();
  }
}
