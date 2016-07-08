package com.github.br.starmarines.main.model.services.inner.xml;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.github.br.starmarines.main.model.services.inner.IReplayReaderService;


@Component
@Instantiate
@Provides
public class XmlReplayDataService implements IReplayReaderService {
	
	@Requires(optional = true)
	private LogService logService;
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " start");
	}
	
	@Invalidate
	private void stop(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " stop");
	}
	
	


    public List<String> getResponsesList(String filePath) throws Exception {
        File input = new File(filePath);
        String xml = readFileAsString(input.getAbsolutePath());
        Pattern p = Pattern.compile("<response>(.*?)</response>");
        Matcher m = p.matcher(xml);

        List<String> results = new LinkedList<String>();
        while (m.find()) {
            results.add(m.group());
        }
        return results;
    }

    public List<String> getActionsList(String filePath) throws Exception {
        File input = new File(filePath);
        String xml = readFileAsString(input.getAbsolutePath());
        Pattern p = Pattern.compile("<actions>(.*?)</actions>");
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
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }



}
