package com.theoryinpractise.halbuilder.standard;

import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.json.JsonRepresentationReader;
import com.theoryinpractise.halbuilder.json.JsonRepresentationWriter;
import com.theoryinpractise.halbuilder.xml.XmlRepresentationReader;
import com.theoryinpractise.halbuilder.xml.XmlRepresentationWriter;

/**
 * Simple representation factory configured for JSON and XML usage.
 */
public class StandardRepresentationFactory extends DefaultRepresentationFactory {
    public StandardRepresentationFactory() {
        withRenderer(HAL_JSON, JsonRepresentationWriter.class);
        withReader(HAL_JSON, JsonRepresentationReader.class);
        withRenderer(HAL_XML, XmlRepresentationWriter.class);
        withReader(HAL_XML, XmlRepresentationReader.class);
    }
}
