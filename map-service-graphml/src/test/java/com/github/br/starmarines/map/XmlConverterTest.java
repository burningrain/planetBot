package com.github.br.starmarines.map;

import com.github.br.starmarines.map.converter.togalaxy.GraphmlConverter;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


public class XmlConverterTest {

    @Test
    public void testYedXml() throws URISyntaxException, IOException {
    	String resource = "/example/example1yed.graphml";
    	outputXml(resource);
    }
    
    @Test
    public void testGephiXml() throws URISyntaxException, IOException {
    	String resource = "/example/example1gephi.graphml";
    	outputXml(resource);
    }
    
    private void outputXml(String path) throws URISyntaxException, IOException {
    	URL url = this.getClass().getResource(path);
        byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
        String outerGraphml = new String(encoded);
        GraphmlConverter xmlConverter = new GraphmlConverter();
        String resultXml = xmlConverter.convert(outerGraphml);
        System.out.println(resultXml);
    }


}
