package starwors.model.lx.xml;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class XmlReplaySplitter {





    public static List<String> split() throws Exception{
        File input = new File("replay.smbot");
        String xml = readFileAsString(input.getAbsolutePath());
        Pattern p = Pattern.compile("<response>(.*?)</response>");
        Matcher m = p.matcher(xml);

        List<String> results = new LinkedList<String>();

        while (m.find()) {
            results.add(m.group());
        }

        return results;
    }

    private static String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }




}
