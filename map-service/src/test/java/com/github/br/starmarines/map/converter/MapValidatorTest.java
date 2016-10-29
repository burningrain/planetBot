package com.github.br.starmarines.map.converter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by SBT-Burshinov-OA on 05.10.2016.
 */
public class MapValidatorTest {

    private Validator validator = null;

    @Rule
    public ResourceFile schemaPlanet = new ResourceFile("/schema/planet.json");

    @Rule
    public ResourceFile schemaGalaxy = new ResourceFile("/schema/galaxy.json");

    @Before
    public void beforEach() {
        validator = new MapValidator();
    }

    @Test
    public void testPlanetValidator() throws Exception {
        String schemaPlanetStr = schemaPlanet.getContent();
        String planet1 = "{\"id\":1,\"type\":\"TYPE_A\",\"units\":0,\"owner\":\"\",\"isStartPoint\":false}";
        boolean actual = validator.validate(planet1, schemaPlanetStr);
        assertTrue(actual);
    }

    @Test
    public void testGalaxyValidator() throws Exception {
        String schemaGalaxyStr = schemaGalaxy.getContent();
        String galaxy1 = "{\"galaxyType\":\"SMALL_BASES\"}";
        boolean actual = validator.validate(galaxy1, schemaGalaxyStr);
        assertTrue(actual);
    }
}
