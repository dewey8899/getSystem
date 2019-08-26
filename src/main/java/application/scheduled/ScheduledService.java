package application.scheduled;

import application.system.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by deweydu
 * Date on 2019/5/22
 */
@Slf4j
@Component
public class ScheduledService {

    private Date date = new Date();
    private static final SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    @Value("${account}")
    private  String account ;
    @Value("${password}")
    private String password;
    @Value("${OriginalImg}")
    private String OriginalImg;
    @Autowired
    private Client client;
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduled() throws InterruptedException {
        log.info("=====>>>>>定时任务使用cron  {}",format.format(System.currentTimeMillis()));
//        Client client = new Client();
        while (true){
            Thread.sleep(5000);
            boolean login = client.login();
            if (login){
                try {
                    boolean orders = client.getOrders();
                    if (orders){
                        log.info("导出成功！");
                        break;
                    }else {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    continue;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
