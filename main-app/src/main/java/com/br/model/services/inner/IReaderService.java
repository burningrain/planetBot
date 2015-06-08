package com.br.model.services.inner;

import java.io.InputStream;
import java.util.Collection;

import com.br.model.services.inner.beans.Response;
import com.br.model.services.inner.exception.ResponseReadException;
import com.br.starwors.lx.galaxy.Action;

public interface IReaderService {

    Response readGalaxy(InputStream input) throws ResponseReadException;
    Collection<Action> readActions(InputStream input) throws ResponseReadException;

}
