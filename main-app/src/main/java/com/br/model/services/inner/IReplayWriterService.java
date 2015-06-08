package com.br.model.services.inner;

import java.io.IOException;
import java.util.Collection;

import com.br.starwors.lx.galaxy.Action;

public interface IReplayWriterService {

	public abstract void writeReplay(byte[] content, Collection<Action> actions)
			throws IOException;

	/**
	 * TODO выбросить исключение, если что-то не удалилось.
	 */
	public abstract void clearLastReplay();

}