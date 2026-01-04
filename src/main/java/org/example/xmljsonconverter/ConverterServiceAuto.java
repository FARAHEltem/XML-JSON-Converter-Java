package org.example.xmljsonconverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ConverterServiceAuto {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public String xmlToJsonAuto(String xml) {
        try {
            JsonNode node = xmlMapper.readTree(xml.getBytes());
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception e) {
            return "ERREUR API (XML -> JSON) : " + e.getMessage();
        }
    }

    public String jsonToXmlAuto(String json) {
        try {
            JsonNode node = jsonMapper.readTree(json);
            return xmlMapper.writerWithDefaultPrettyPrinter().withRootName("root").writeValueAsString(node);
        } catch (Exception e) {
            return "ERREUR API (JSON -> XML) : " + e.getMessage();
        }
    }
}