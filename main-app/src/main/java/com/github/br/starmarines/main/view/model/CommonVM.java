package com.github.br.starmarines.main.view.model;

import com.github.br.starmarines.main.view.model.objects.IColorVM;
import com.github.br.starmarines.main.view.model.objects.impl.ColorVM;

public class CommonVM {
	
	private IColorVM colorVM = new ColorVM();

	public IColorVM getColorVM() {
		return colorVM;
	}

}
