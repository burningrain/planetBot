package starwors.model.lx.bot;


import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReplyThread implements Runnable {

    private List<String> responses;
    private GameDataService dataService;

    private int count = 0;

    public ReplyThread(List<String> responses, GameDataService dataService){
        this.responses = responses;
        this.dataService = dataService;
    }

    @Override
    public void run() {
        while(count < responses.size()){
            dataService.showStep(new ByteArrayInputStream(responses.get(count).getBytes(StandardCharsets.UTF_8)));
            count++;

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
