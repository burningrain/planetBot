package com.github.br.starmarines.map.converter;

import java.io.File;

import com.github.br.starmarines.gamecore.api.Galaxy;
import org.eclipse.gef4.dot.internal.DotImport;
import org.eclipse.gef4.graph.Graph;

public class MapConvernter implements Converter<File, Galaxy> {

	@Override
	public Galaxy convert(File in) {
		Graph graph = new DotImport().importDot("graph { 1--2 ; 1--3 }");
		Graph digraph = new DotImport().importDot("digraph { 1->2 ; 1->3 }");
		System.out.println(graph.toString());
		return null;		
	}

}
