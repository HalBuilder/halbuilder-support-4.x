package com.theoryinpractise.halbuilder.xml;

import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import org.jdom2.Namespace;

public class XmlRepresentationFactory extends DefaultRepresentationFactory {

    /**
     * Define the XML schema instance namespace, so we can use it when
     * rendering nil elements.
     */
    public static final Namespace XSI_NAMESPACE = Namespace.getNamespace(
            "xsi", "http://www.w3.org/2001/XMLSchema-instance");

    public XmlRepresentationFactory() {
        withRenderer(HAL_XML, XmlRepresentationWriter.class);
        withReader(HAL_XML, XmlRepresentationReader.class);
    }
}
