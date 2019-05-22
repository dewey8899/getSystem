package application.scheduled;

import application.system.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by deweydu
 * Date on 2019/5/22
 */
@Slf4j
@Component
public class ScheduledService {

    private static final String account = "yxbh@379634044";
    private static final String password = "8ddcff3a80f4189ca1c9d4d902c3c909";
    @Scheduled(cron = "0/5 * * * * *")
    public void scheduled(){
        log.info("=====>>>>>定时任务使用cron  {}",System.currentTimeMillis());
        Client client = new Client(account, password);
        while (true){
            boolean login = client.login();
            if (login){
                try {
                    client.getOrders();
                    log.info("导出成功！");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }
}
