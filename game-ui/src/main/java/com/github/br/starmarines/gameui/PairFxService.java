package com.github.br.starmarines.gameui;

import com.github.br.starmarines.ui.api.IUiOrderComponent;

public class PairFxService<FXPARENT, FXCHILD, SERVICE extends IUiOrderComponent<FXCHILD>>
		implements Comparable<PairFxService<FXPARENT, FXCHILD, SERVICE>> {

	private final FXPARENT fxcomponent;
	private final FXCHILD child;
	private final SERVICE service;

	public PairFxService(final SERVICE service, final FXPARENT fxcomponent) {
		this.service = service;
		this.fxcomponent = fxcomponent;
		this.child = service.getNode(); // важно получить ноду 1 раз
	}

	public FXPARENT getFxParent() {
		return fxcomponent;
	}

	public SERVICE getService() {
		return service;
	}

	public FXCHILD getFxChild() {
		return child;
	}

	@Override
	public int compareTo(PairFxService<FXPARENT, FXCHILD, SERVICE> o) {
		return Integer.compare(this.getService().getOrder(), o.getService()
				.getOrder());
	}

}
