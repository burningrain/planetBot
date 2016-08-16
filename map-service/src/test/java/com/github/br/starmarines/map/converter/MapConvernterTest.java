package com.github.br.starmarines.map.converter;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;


public class MapConvernterTest {

	@Test
	public void testConvert() {
		MapConvernter mConv = new MapConvernter();
		mConv.convert(null);
		Assert.assertTrue(true);
	}
}
