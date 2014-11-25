package starwors.logic.utils.fs;


import org.junit.Test;
import starwors.model.lx.xml.XmlReplayDataService;

public class ReplayParseTest {

    @Test
    public void parseReplayTest(){
        try {
            //TODO дописать тест до нормального состояния
            XmlReplayDataService.getResponsesList("startReplay.smbot");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
