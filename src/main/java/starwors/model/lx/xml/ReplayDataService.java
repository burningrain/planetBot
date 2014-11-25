package starwors.model.lx.xml;

import java.util.List;

public interface ReplayDataService {

    List<String> getResponsesList(String filePath) throws Exception;
    List<String> getActionsList(String filePath) throws Exception;

}
