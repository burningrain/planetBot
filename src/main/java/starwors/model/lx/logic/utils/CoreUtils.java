package starwors.model.lx.logic.utils;


import starwors.model.lx.galaxy.Action;
import starwors.model.lx.galaxy.Move;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public final class CoreUtils {

    private CoreUtils(){
    }


    public static byte[] readByteArrayFromInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int bytesRead = 0;
        while ((bytesRead = in.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        return baos.toByteArray();
    }

    public static Collection<Action> toActionList(final Collection<Move> moves) {
        Collection<Action> actions = new ArrayList<Action>(moves.size());
        for (Move move : moves) {
            actions.add(move.toAction());
        }
        return actions;
    }

}
