package com.github.br.starmarines.map.converter;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class MapConvernterTest {

	
	@Rule
    public ResourceFile res = new ResourceFile("/test1.gv");

    @Test
    public void test() throws Exception
    {
        assertTrue(res.getContent().length() > 0);
        assertTrue(res.getFile().exists());
    }
	
	@Test
	public void testConvert() throws IOException, URISyntaxException {
		File in = res.getFile();
		MapConvernter mConv = new MapConvernter();
		mConv.convert(in);
		Assert.assertTrue(true);
	}
}
