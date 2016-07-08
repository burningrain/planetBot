package com.github.br.starmarines.main.model.config.properties;

import java.lang.reflect.Field;
import java.util.Dictionary;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Updated;
import org.osgi.service.log.LogService;


@Component(managedservice = "botproperties")
@Instantiate
@Provides
public class BotProperties {

	@Property(name="server")
    private String server;
	
	@Property(name="port")
    private int port;
	
	@Property(name="name")
    private String selfName;
	
	@Property(name="token")
    private String token;
    
    
    @Requires(optional = true)
    private LogService logService;

    @Updated
    public void updated(Dictionary conf) {
    	//PlanetUtils.MY_PLANET_OWNER_NAME = selfName; // FIXME UGLY HUCK
    	logService.log(LogService.LOG_INFO, "configuraion updated. " + conf);      
    }

    public String getToken() {
        return token;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getSelfName() {
        return selfName;
    }
    
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("server=").append(server).
    		append(", port=").append(port).
    		append(", name=").append(selfName).
    		append(", token=").append(token);
    	return sb.toString();
    }

}
