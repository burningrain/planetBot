package starwors.model.lx.xml;


import starwors.model.lx.galaxy.Action;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class ReplayWriter {


    public void writeReplay(byte[] content, Collection<Action> actions) throws IOException {
        File response = new File("replay.smbot");

        // if file doesnt exists, then create it
        if (!response.exists()) {
            response.createNewFile();
        }
        BufferedWriter wr = new BufferedWriter(new FileWriter(response.getName(), true));
        BufferedReader r = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8));
        String str = null;
        StringBuilder sb = new StringBuilder();
        while ((str = r.readLine()) != null) {
            sb.append(str);
        }
        wr.write(sb.toString());
        wr.close();


        File fileWithActions = new File("actions.smbot");
        if (!fileWithActions.exists()) {
            fileWithActions.createNewFile();
        }
        BufferedWriter wra = new BufferedWriter(new FileWriter(fileWithActions.getName(), true));
        StringBuilder sba = new StringBuilder();
        sba.append("<actions>");
        for (Action action : actions) {
            sba.append(action.toString());
        }
        sba.append("</actions>");
        wra.write(sba.toString());
        wra.close();
    }

    /**
     * TODO выбросить исключение, если что-то не удалилось.
     */
    public void clearLastReplay(){
        deleteFile("replay.smbot");
        deleteFile("actions.smbot");
    }

    private static boolean deleteFile(String filename) {
        File file = new File(filename);
        return file.delete();
    }

}
