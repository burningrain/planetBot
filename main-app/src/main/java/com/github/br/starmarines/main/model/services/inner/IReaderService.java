package com.github.br.starmarines.main.model.services.inner;

import java.io.InputStream;
import java.util.Collection;

import com.br.starwors.lx.galaxy.Action;
import com.github.br.starmarines.main.model.services.inner.beans.Response;
import com.github.br.starmarines.main.model.services.inner.exception.ResponseReadException;

public interface IReaderService {

    Response readGalaxy(InputStream input) throws ResponseReadException;
    Collection<Action> readActions(InputStream input) throws ResponseReadException;

}
