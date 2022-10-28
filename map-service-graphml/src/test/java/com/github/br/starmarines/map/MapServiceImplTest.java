package com.github.br.starmarines.map;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.gamecore.api.Galaxy;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

public class MapServiceImplTest {

	private final ZipMapConverter mapConverter = new ZipMapConverter();

	@Test
	public void testConvertGalaxyToGraphML() throws IOException {
		Galaxy.Builder builder = new Galaxy.Builder("test", new byte[0]);
		
		Planet planet1 = new Planet();
		planet1.setId((short) 1);
		planet1.setOwnerId((short) 1);
		planet1.setType(PlanetType.TYPE_C);
		planet1.setUnits(100);
		
		Planet planet2 = new Planet();
		planet2.setId((short) 2);
		planet2.setOwnerId((short) 2);
		planet2.setType(PlanetType.TYPE_D);
		planet2.setUnits(200);
		
		builder.addPlanet(planet1, true);
		builder.addPlanet(planet2, true);
		
		builder.addEdge(planet1.getId(), planet2.getId());
	    Galaxy galaxy = builder.build();

		byte[] bytes = null;
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			mapConverter.saveMap(baos, galaxy);
			bytes = baos.toByteArray();
		}

		assertNotNull(bytes);
		Files.write(Paths.get(System.getProperty("user.dir")).resolve("test.pb"), bytes);
	}

	@Test
	public void testConvertGraphMLToGalaxy() throws URISyntaxException {
		URL url = this.getClass().getResource("/example/example1.pb");
		Galaxy map = mapConverter.getMap(Paths.get(url.toURI()).toFile());

		assertNotNull(map);
		assertNotNull(map.getMinimap());
		System.out.println(map);
	}

}
