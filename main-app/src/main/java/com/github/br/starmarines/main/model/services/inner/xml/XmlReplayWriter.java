package com.github.br.starmarines.main.model.services.inner.xml;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.starwors.lx.galaxy.Action;
import com.github.br.starmarines.main.model.services.inner.IReplayWriterService;

@Component
@Instantiate
@Provides
public class XmlReplayWriter implements IReplayWriterService {


	@Requires(optional = true)
	private LogService logService;
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " start");
	}
	
	@Invalidate
	private void stop(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " stop");
	}
	
	
    @Override
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

//FIXME WET CODE
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

    /* (non-Javadoc)
	 * @see com.br.model.services.xml.ReplayWriterService#clearLastReplay()
	 */
    @Override
	public void clearLastReplay(){
        deleteFile("replay.smbot");
        deleteFile("actions.smbot");
    }

    private static boolean deleteFile(String filename) {
        File file = new File(filename);
        return file.delete();
    }

}
