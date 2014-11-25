package starwors.model.lx.xml;


import starwors.model.lx.bot.Response;
import starwors.model.lx.galaxy.Action;

import java.io.InputStream;
import java.util.Collection;

public interface ReaderService {

    Response readGalaxy(InputStream input) throws ResponseReadException;
    Collection<Action> readActions(InputStream input) throws ResponseReadException;

}
