package com.br.view.model;

import com.br.view.model.objects.IColorVM;
import com.br.view.model.objects.impl.ColorVM;

public class CommonVM {
	
	private IColorVM colorVM = new ColorVM();

	public IColorVM getColorVM() {
		return colorVM;
	}

}
