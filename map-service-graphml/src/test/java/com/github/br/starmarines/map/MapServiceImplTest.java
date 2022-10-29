package com.github.br.starmarines.map;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.gamecore.api.Galaxy;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;

public class MapServiceImplTest {

	private final ZipMapConverter mapConverter = new ZipMapConverter();

	@Test
	public void testConvertGalaxyToGraphML() throws IOException {
		Galaxy.Builder builder = new Galaxy.Builder("test", new byte[] {(byte)0,(byte)1,(byte)2,(byte)3,(byte)4});
		
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

		byte[] bytes = mapConverter.toByteArray(galaxy);
		assertNotNull(bytes);

		Galaxy galaxyAfter = mapConverter.toGalaxy(galaxy.getTitle(), bytes);
		assertNotNull(galaxyAfter);

		System.out.println(galaxy);
		System.out.println(galaxyAfter);
	}

	@Test
	public void testConvertGraphMLToGalaxy() throws IOException {
		InputStream resourceAsStream = this.getClass().getResourceAsStream("/example/example1.pb");
		byte[] bytes = Objects.requireNonNull(resourceAsStream.readAllBytes());
		Galaxy map = mapConverter.toGalaxy("example1", bytes);

		assertNotNull(map);
		assertNotNull(map.getMinimap());
		System.out.println(map);
	}

}
