package com.github.br.starmarines.map.converter.togalaxy;

import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.xslt.XmlUtils;

import java.io.InputStream;

/**
 * Конвертирует xml (внешний graphml с какими-либо добавочками, т.к. поставлен внешним редактором, например, yEd или Gephi)
 * в xml            (graphml, соответствующий стандарту и понимаемый парсером)
 */
public class GraphmlConverter implements Converter<String, String> {

    @Override
    public String convert(String xml) {
        final InputStream resource = XmlUtils.class.getClassLoader().getResourceAsStream("graphml-transform.xslt");
        return XmlUtils.transformXml(xml, resource);
    }

}
