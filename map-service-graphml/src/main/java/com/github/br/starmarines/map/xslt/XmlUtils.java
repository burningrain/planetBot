package com.github.br.starmarines.map.xslt;

import com.google.common.base.Charsets;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.google.common.base.Suppliers.memoize;

/**
 * Created by user on 21.01.2017.
 */
public class XmlUtils {


    public static final String DISALLOW_DOCTYPE_DECL =
            "http://apache.org/xml/features/disallow-doctype-decl";

    // saxon's features
    private static final String ALLOW_EXTERNAL_FUNCTION =
            "http://saxon.sf.net/feature/allow-external-functions";

    private static Supplier<TransformerFactory> tramsformerFactorySupplier = memoize(
            new Supplier<TransformerFactory>() {
                @Override
                public TransformerFactory get() {
                    final TransformerFactory transformerFactory = TransformerFactory
                            .newInstance(net.sf.saxon.TransformerFactoryImpl.class.getCanonicalName(), getClass().getClassLoader());
                    transformerFactory.setAttribute(ALLOW_EXTERNAL_FUNCTION, false);
                    return transformerFactory;
                }
            });

    private static Supplier<DocumentBuilderFactory> documentBuilderFactoryNs =
            memoize(new Supplier<DocumentBuilderFactory>() {
                @Override
                public DocumentBuilderFactory get() {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    dbFactory.setNamespaceAware(true);
                    dbFactory.setExpandEntityReferences(false);
                    dbFactory.setXIncludeAware(false);
                    try {
                        dbFactory.setFeature(DISALLOW_DOCTYPE_DECL, true);
                    } catch (ParserConfigurationException e) {
                        throw Throwables.propagate(e);
                    }
                    return dbFactory;
                }
            });

    public static String transformXml(String inputXml, InputStream xslt) {
        if (xslt == null) {
            throw new IllegalArgumentException("Не задан входной XSLT");
        }
        if (inputXml == null || inputXml.isEmpty()) {
            throw new IllegalArgumentException("Не задан входной XML-документ");
        }
        String res = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document domFromXmlString = XmlUtils.getDomFromXmlString(inputXml);
            DOMSource domSource = new DOMSource(domFromXmlString);
            StreamSource streamSource = new StreamSource(xslt);
            Transformer transformer = getTransformer(streamSource);
            StreamResult streamResult = new StreamResult(out);
            transformer.transform(domSource, streamResult);
            res = new String(out.toByteArray(), Charsets.UTF_8);
        } catch (TransformerException | IOException e) {
            throw Throwables.propagate(e);
        }
        return res;
    }

    private static Document getDomFromXmlString(String xml) {
        ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        return XmlUtils.parse(stream);
    }

    private static Document parse(InputStream inputSource) {
        try {
            DocumentBuilder documentBuilder = getDocumentBuilderFactory().newDocumentBuilder();
            return documentBuilder.parse(inputSource);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private static DocumentBuilderFactory getDocumentBuilderFactory() {
        return documentBuilderFactoryNs.get();
    }


    private static Transformer getTransformer(Source source)
            throws TransformerConfigurationException {
        TransformerFactory transformerFactory = tramsformerFactorySupplier.get();
        final Transformer transformer =
                source == null ? transformerFactory.newTransformer()
                        : transformerFactory.newTransformer(source);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        return transformer;
    }

    private static Transformer getTransformer() throws TransformerConfigurationException {
        return getTransformer(null);
    }

}
