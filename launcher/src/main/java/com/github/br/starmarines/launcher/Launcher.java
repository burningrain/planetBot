package com.github.br.starmarines.launcher;

import org.apache.felix.main.AutoProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Launcher {
    // Костыль для установки логгеру пути к файлу конфигурации
    public static final String LOG_CONFIG_FILE_PROPERTY = "logback.configurationFile";

    public static void main(String[] args) {    
    	Splash splash = new Splash();
    	
        Launcher launcher = new Launcher();
        try {        	
            launcher.initialize();            
        } catch (Exception e) {
        	appendToFile(e);
        	showErrorMessage();
        	System.exit(0);
        } 
        
        splash.dispose();        
    }
    
    public static void appendToFile(Exception e) {
        try {
           FileWriter fstream = new FileWriter("crash.log", true);
           BufferedWriter out = new BufferedWriter(fstream);
           PrintWriter pWriter = new PrintWriter(out, true);
           e.printStackTrace(pWriter);
        } catch (Exception ie) {
           throw new RuntimeException("Could not write Exception to file", ie);
        }
     }
    
    private static void showErrorMessage(){
    	JFrame frame = new JFrame("Star Marines Bot");
    	JOptionPane.showMessageDialog(frame,
    		    "see crash.log for details",
    		    "Internal error",
    		    JOptionPane.ERROR_MESSAGE);    	
    }

    private void initialize() throws BundleException, URISyntaxException {

        Map<String, String> configMap = loadProperties();
        System.setProperty(LOG_CONFIG_FILE_PROPERTY, configMap.get(LOG_CONFIG_FILE_PROPERTY));
   
        System.out.println("Building OSGi Framework");
        FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
        Framework framework = frameworkFactory.newFramework(configMap);

        framework.init();
        // (9) Use the system bundle context to process the auto-deploy
        // and auto-install/auto-start properties.
        AutoProcessor.process(configMap, framework.getBundleContext());
        // (10) Start the framework.
        System.out.println("Starting OSGi Framework");
        framework.start();
     
        BundleContext context = framework.getBundleContext();
        // declarative services dependency is necessary, otherwise they won't be picked up!
        //loadScrBundle(context);

        try {
            framework.waitForStop(0);
        } catch (InterruptedException e) {
        	appendToFile(e);
        	showErrorMessage();
        }
        System.exit(0);
    }

    private void loadScrBundle(BundleContext context) throws URISyntaxException, BundleException {
        URL url = getClass().getClassLoader().getResource("org/osgi/service/component/runtime/ServiceComponentRuntime.class");
        if (url == null)
            throw new RuntimeException("Could not find the class org.osgi.service.component.runtime.ServiceComponentRuntime");
        String jarPath = url.toURI().getSchemeSpecificPart().replaceAll("!.*", "");
        System.out.println("Found declarative services implementation: " + jarPath);
        context.installBundle(jarPath).start();
    }

    private static Map<String, String> loadProperties() {
        Map<String, String> map = new HashMap<String, String>();
        
        String pathToConfig = "osgi" + File.separator + "config";
        File pathName = new File(pathToConfig);
        String[] fileNames = pathName.list();
        for (int i = 0; i < fileNames.length; i++) {
            Properties prop = loadProperty(pathToConfig + File.separator + fileNames[i]);
            for (final String name : prop.stringPropertyNames())
                map.put(name, prop.getProperty(name));
        }

        return map;
    }

    private static Properties loadProperty(String filepath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(filepath));
        } catch (Exception e) {
        	appendToFile(e);
        	showErrorMessage();
        	System.exit(0);
        } 
        return properties;
    }

}
