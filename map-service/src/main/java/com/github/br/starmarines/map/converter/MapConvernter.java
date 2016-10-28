package com.github.br.starmarines.map.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.gef4.dot.internal.DotImport;
import org.eclipse.gef4.graph.Edge;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.Node;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.service.api.MapValidationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class MapConvernter implements Converter<File, Galaxy> {

	private volatile LogService logService;
	private static final String CODE_PAGE = "utf-8";

//	@Activate
//	void activate(BundleContext bundleContext) throws IOException {
//		URL urlPlanetSchema = bundleContext.getBundle().getResource("schema/planet.json");
//		File planetFile = new File("schema/planet.json");
//		org.apache.commons.io.FileUtils.copyURLToFile(urlPlanetSchema, planetFile);
//		File galaxyFile = new File("schema/galaxy.json");
//		URL urlGalaxySchema =  bundleContext.getBundle().getResource("schema/galaxy.json");
//		org.apache.commons.io.FileUtils.copyURLToFile(urlGalaxySchema, galaxyFile);
//	}
//	 
	@Reference(unbind = "unSetLogService", cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public void unSetLogService(LogService logService) {
		this.logService = null;
	}

	@Override
	public Galaxy convert(File in) throws IOException, MapValidationException {
		if (logService != null) {
			logService.log(LogService.LOG_DEBUG, "Convertation start");
		}
		Gson gson = new GsonBuilder().registerTypeAdapter(Planet.class, new NodeLabelDeserializer())
				.registerTypeAdapter(GameMetaInfo.class, new GameMetaInfoDeserializer()).create();
		String graphFile = getContent(in);
		Graph graph = new DotImport().importDot(graphFile);
		Map<String, Object> atribMap = graph.getAttributes();
		String galaxyJSON = (String) atribMap.get("_name");
		JsonParser parser = new JsonParser();
		JsonObject inJSON = parser.parse(galaxyJSON).getAsJsonObject();
		this.isValidGalaxy(inJSON.toString());
		GameMetaInfo galaxyInfo = gson.fromJson(galaxyJSON, GameMetaInfo.class);
		Galaxy.Builder gBuilder = new Galaxy.Builder(galaxyInfo.getType());
		Galaxy galaxy = null;
		List<Node> nProps = graph.nodesProperty();
		for (Node nodeProp : nProps) {
			Map<String, Object> nodePropAttrMap = nodeProp.getAttributes();
			String planetJSON = (String) nodePropAttrMap.get("label");
			inJSON = parser.parse(planetJSON).getAsJsonObject();
			this.isValidPlanet(inJSON.toString());
			Planet planet = gson.fromJson(planetJSON, Planet.class);
			GameMetaInfo gameInfo = gson.fromJson(planetJSON, GameMetaInfo.class);
			gBuilder.addPlanet(planet, gameInfo.getIsStartPoint());
		}
		List<Edge> edges = graph.getEdges();
		for (Edge edge : edges) {
			Node from = edge.getSource();
			Map<String, Object> fromAttr = from.getAttributes();
			Planet planetFrom = gson.fromJson((String) fromAttr.get("label"), Planet.class);
			Node to = edge.getTarget();
			Map<String, Object> toAttr = to.getAttributes();
			Planet planetTo = gson.fromJson((String) toAttr.get("label"), Planet.class);
			gBuilder.addEdge(planetFrom, planetTo);

		}
		galaxy = gBuilder.build();
		if (logService != null) {
			logService.log(LogService.LOG_DEBUG, "galaxy = " + galaxy.getGalaxyType());
		}

		return galaxy;
	}
	
	private boolean isValidGalaxy(String inJSON) throws IOException, MapValidationException{
		boolean result = false;
	    URL urlGalaxySchema = getClass().getClassLoader().getResource("schema/galaxy.json");
	    File galaxyFile = new File("schema/galaxy.json");
	    FileUtils.copyURLToFile(urlGalaxySchema, galaxyFile);
	    String galaxySchema = FileUtils.readFileToString(galaxyFile, CODE_PAGE);
	    Validator validator = new MapValidator();
	    try {
	    	result = validator.validate(inJSON, galaxySchema);
		} catch (Exception e) {
			throw new MapValidationException("Error: Current map have some problem"
					+ " in galaxy meta info.",  e.getMessage());
		}
		return result;
	}
	
	private boolean isValidPlanet(String inJSON) throws IOException, MapValidationException {
		boolean result = false;
		URL urlPlanetSchema = getClass().getClassLoader().getResource("schema/planet.json"); //TODO: DOUBLE CHECK INIT
		File planetFile = new File("schema/planet.json");
		FileUtils.copyURLToFile(urlPlanetSchema, planetFile);
	    String planetSchema = FileUtils.readFileToString(planetFile, CODE_PAGE);
	    Validator validator = new MapValidator();
	    try {
	    	result = validator.validate(inJSON, planetSchema);
		} catch (Exception e) {
			throw new MapValidationException("Error: Current map have some "
					+ "problem in planet meta info.",  e.getMessage());
		}
		return result;
	}

	private String getContent(File in) throws IOException {
		char[] tmp = new char[4096];
		StringBuilder b = new StringBuilder();
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(in), Charset.forName(CODE_PAGE))) {
			while (true) {
				int len = reader.read(tmp);
				if (len < 0) {
					break;
				}
				b.append(tmp, 0, len);
			}
			reader.close();
		}
		return b.toString();
	}

}
