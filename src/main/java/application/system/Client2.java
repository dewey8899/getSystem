package application.system;

import application.utils.DateUtils;
import application.utils.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by deweydu
 * Date on 2019/3/29
 */
@Slf4j
@Component
public class Client2 {

    static CookieStore cookieStore = new BasicCookieStore();
    @Value("${account}")
    private String accout;
    @Value("${password}")
    private String password;
    private String validateCode;
    @Value("${uuid}")
    private String uuid;
    @Value("${OriginalImg}")
    private String OriginalImg;
    @Value("${ocrResult}")
    private String ocrResult;

    @Value("${outPath}")
    private String outPath;

    @Value("${tessDataPath}")
    private String tessDataPath;
    /**
     * 实例化httpclient
     */
    CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

    CloseableHttpResponse response = null;
    String rawHtml;

//    public Client(String accout, String password) {
//        super();
//        this.accout = accout;
//        this.password = password;
//    }

    //1.登录
    public boolean login() {
        try {
            HttpEntity entity;
            ArrayList<NameValuePair> postData = new ArrayList<>();
            postData.add(new BasicNameValuePair("imgCheck", ""));
            postData.add(new BasicNameValuePair("loginType", "1"));
            postData.add(new BasicNameValuePair("vCity", "徐州市"));//城市
            postData.add(new BasicNameValuePair("vIp", "49.233.242.46"));//IP
            postData.add(new BasicNameValuePair("vLogin", "p434585"));//账号
            postData.add(new BasicNameValuePair("vPwd", "7fs1FLJ7LBZnNNGILzrWig=="));//密码
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            HttpPost post = new HttpPost("https://vwmep.faw-vw.com/api/cccs/cccs/login/login?username=P434585@758326900&password=96e79218965eb72c92a549dd5a330112&logintype=1");//构建post对象
            post.addHeader("Content-Type", "application/json");
            post.addHeader("cookie", "cna=AJoqGR6tXwkCAbSiSg6ahZmi; jwt=eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlZGU5NzVlYi1kNjZjLTRlZDMtYjZjMS1mOWEwNTg2MTY1MTU6PnBjIiwidXNlcklkIjoiZWRlOTc1ZWItZDY2Yy00ZWQzLWI2YzEtZjlhMDU4NjE2NTE1IiwibmFtZSI6IiIsInJlbWFyayI6IiIsImtpY2tPdXQiOnRydWUsIm5ESUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ2QWRkcmVzcyI6IiIsImNJbml0UHdkIjoiMCIsInZSb2xlTmFtZSI6IumUgOWUrumhvumXriIsInZEZWFsZXIiOiI3NTgzMjY5IiwidlBlcnNvbiI6IjAxMjIiLCJ2V2VDaGF0UGMiOiIiLCJuTURNSUQiOiJmMTcwOTFiOC0xZGIxLTQ4OGMtOTlhNi0zMjk0YjhlNGQ2NzgiLCJ2T3JnTmFtZSI6IumUgOWUrumDqCIsIm5ocnBlcnNvbmlkIjoiMjAxOTEyMDI0MTQ4Mzg1OSIsInZXZUNoYXQiOiIiLCJuaWNrbmFtZSI6IiIsIm5CSUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJncmF5Q29kZSI6IjAiLCJ2QnJhbmNoQ29kZSI6IjAwIiwibkN1c3RHcm91cElkIjoiRDQ5RjY2QzYtNTJBMi00RDg3LUI1REMtQzA2OThFMkE4RTc0Iiwic2FsZXNDb2RlIjoiUzczMjA1OCIsInZSb2xlIjoiU0FMRVIiLCJjTWFpbiI6IjEiLCJ2TG9naW4iOiJQNDM0NTg1IiwiY01haW5CcmFuY2giOiIxIiwiY0JpbmRTdGF0ZSI6IjAiLCJ2Um9sZVZlcnNpb24iOiIyIiwidlVzZXJWZXJzaW9uIjoiMiIsInZVc2VyUm9sZUlEIjoiZTk1MmVjYTMtZTc5YS00NWU5LWFjOTktMDcyNjY4YTUxMGQ4Iiwidk1vYmlsZSI6IjE1OTk2OTc4MTgzIiwidlVzZXJUeXBlIjoiMCIsInZPcmciOiIwMiIsInZQZXJzb25OYW1lIjoi6JSh5aOr6aG6IiwiaUQiOiJlZGU5NzVlYi1kNjZjLTRlZDMtYjZjMS1mOWEwNTg2MTY1MTUiLCJ1c2VyVHlwZSI6IjAiLCJkZXZpY2UiOiJwYyIsInZEZWFsZXJOYW1lIjoi5b6Q5bee6YeR5rqQ5rG96L2m5pyN5Yqh5pyJ6ZmQ5YWs5Y-4IiwiZXhwIjoxNjIyMzg0MjcxfQ.NOxQS3FtbksfKPWczezczZ1LBGVsFQtMqdwCnGlLoldDcnonHQZwWzL3gL0x_DCWn3oI8_w09VVhuvbSstvSDJhybkOrVcjI3ljFUj9WiFFve_AEG96fSstruqsTm-841rSF3j5eLWF8DaZ8g96eRDittzleT17RurW7dCp-dYw; userid=ede975eb-d66c-4ed3-b6c1-f9a058616515; username=%E8%94%A1%E5%A3%AB%E9%A1%BA; nDid=75832690-0000-0000-0000-000000000000; vDealerName=%E5%BE%90%E5%B7%9E%E9%87%91%E6%BA%90%E6%B1%BD%E8%BD%A6%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8");
            post.addHeader("jwt", "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlZGU5NzVlYi1kNjZjLTRlZDMtYjZjMS1mOWEwNTg2MTY1MTU6PnBjIiwidXNlcklkIjoiZWRlOTc1ZWItZDY2Yy00ZWQzLWI2YzEtZjlhMDU4NjE2NTE1IiwibmFtZSI6IiIsInJlbWFyayI6IiIsImtpY2tPdXQiOnRydWUsIm5ESUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ2QWRkcmVzcyI6IiIsImNJbml0UHdkIjoiMCIsInZSb2xlTmFtZSI6IumUgOWUrumhvumXriIsInZEZWFsZXIiOiI3NTgzMjY5IiwidlBlcnNvbiI6IjAxMjIiLCJ2V2VDaGF0UGMiOiIiLCJuTURNSUQiOiJmMTcwOTFiOC0xZGIxLTQ4OGMtOTlhNi0zMjk0YjhlNGQ2NzgiLCJ2T3JnTmFtZSI6IumUgOWUrumDqCIsIm5ocnBlcnNvbmlkIjoiMjAxOTEyMDI0MTQ4Mzg1OSIsInZXZUNoYXQiOiIiLCJuaWNrbmFtZSI6IiIsIm5CSUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJncmF5Q29kZSI6IjAiLCJ2QnJhbmNoQ29kZSI6IjAwIiwibkN1c3RHcm91cElkIjoiRDQ5RjY2QzYtNTJBMi00RDg3LUI1REMtQzA2OThFMkE4RTc0Iiwic2FsZXNDb2RlIjoiUzczMjA1OCIsInZSb2xlIjoiU0FMRVIiLCJjTWFpbiI6IjEiLCJ2TG9naW4iOiJQNDM0NTg1IiwiY01haW5CcmFuY2giOiIxIiwiY0JpbmRTdGF0ZSI6IjAiLCJ2Um9sZVZlcnNpb24iOiIyIiwidlVzZXJWZXJzaW9uIjoiMiIsInZVc2VyUm9sZUlEIjoiZTk1MmVjYTMtZTc5YS00NWU5LWFjOTktMDcyNjY4YTUxMGQ4Iiwidk1vYmlsZSI6IjE1OTk2OTc4MTgzIiwidlVzZXJUeXBlIjoiMCIsInZPcmciOiIwMiIsInZQZXJzb25OYW1lIjoi6JSh5aOr6aG6IiwiaUQiOiJlZGU5NzVlYi1kNjZjLTRlZDMtYjZjMS1mOWEwNTg2MTY1MTUiLCJ1c2VyVHlwZSI6IjAiLCJkZXZpY2UiOiJwYyIsInZEZWFsZXJOYW1lIjoi5b6Q5bee6YeR5rqQ5rG96L2m5pyN5Yqh5pyJ6ZmQ5YWs5Y-4IiwiZXhwIjoxNjIyMzg0MjcxfQ.NOxQS3FtbksfKPWczezczZ1LBGVsFQtMqdwCnGlLoldDcnonHQZwWzL3gL0x_DCWn3oI8_w09VVhuvbSstvSDJhybkOrVcjI3ljFUj9WiFFve_AEG96fSstruqsTm-841rSF3j5eLWF8DaZ8g96eRDittzleT17RurW7dCp-dYw");
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postData);
            post.setEntity(formEntity);//捆绑参数
            post.setConfig(requestConfig);
            response = client.execute(post);//执行登录操作
            entity = response.getEntity();
            log.info("Initial set of cookies:");
            rawHtml = EntityUtils.toString(entity, "utf-8");
            log.info("响应提示：\n{}", rawHtml);
            JsonParser parse = new JsonParser();  //创建json解析器
            JsonObject object = (JsonObject) parse.parse(rawHtml);
            String dataObj = object.get("code").getAsString();
            if (dataObj.equals("1004")) {
                return true;
            } else {
                return false;
            }
        } catch (ClientProtocolException e) {
            log.info(e.getMessage());
            return false;
        } catch (IOException e) {
            log.info(e.getMessage());
            return false;
        }
    }


    //2.获取所有的订单
    public boolean getOrders() throws IOException, URISyntaxException, ParseException {
        HttpResponse response = null;
        String getOrdersUrl = "ps://vwmep.faw-vw.com/api/mep-retail/customersList/listRecordsNew?_t=1622381191548";
        ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("currentPage", "1"));
        postData.add(new BasicNameValuePair("pageSize", "1000000"));
        String json = "{\"currentPage\":1,\"pageSize\":\"50000\",\"searchType\":\"2\"}";
        StringEntity formEntity = new StringEntity(json);
        HttpPost post = new HttpPost(getOrdersUrl);
        post.setHeader("Accept", "application/json");
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        post.addHeader("request_time", "1621340672159");
        post.addHeader("appid", "JDMC");
        post.addHeader("authorization", "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5ZDBjNGYwNy00NDFjLTRiMjYtYTJlYS0zZTRkMjNiZTRkZDc6PnBjIiwidXNlcklkIjoiOWQwYzRmMDctNDQxYy00YjI2LWEyZWEtM2U0ZDIzYmU0ZGQ3IiwibmFtZSI6IiIsInJlbWFyayI6IiIsImtpY2tPdXQiOnRydWUsIm5ESUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ2QWRkcmVzcyI6IiIsImNJbml0UHdkIjoiMCIsInZSb2xlTmFtZSI6IumUgOWUrumhvumXriIsInZEZWFsZXIiOiI3NTgzMjY5IiwidlBlcnNvbiI6IlhTNTkiLCJ2V2VDaGF0UGMiOiIiLCJuTURNSUQiOiJmMTcwOTFiOC0xZGIxLTQ4OGMtOTlhNi0zMjk0YjhlNGQ2NzgiLCJ2T3JnTmFtZSI6IumUgOWUrumDqCIsIm5ocnBlcnNvbmlkIjoiMjAxMzA0MTY0NjMyNzY4OSIsInZXZUNoYXQiOiIiLCJuaWNrbmFtZSI6IiIsIm5CSUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJncmF5Q29kZSI6IjAiLCJ2QnJhbmNoQ29kZSI6IjAwIiwibkN1c3RHcm91cElkIjoiRDQ5RjY2QzYtNTJBMi00RDg3LUI1REMtQzA2OThFMkE4RTc0Iiwic2FsZXNDb2RlIjoiUzczMjA1OCIsInZSb2xlIjoiU0FMRVIiLCJjTWFpbiI6IjEiLCJ2TG9naW4iOiJQMjQ3NzMyIiwiY01haW5CcmFuY2giOiIxIiwiY0JpbmRTdGF0ZSI6IjAiLCJ2Um9sZVZlcnNpb24iOiIzIiwidlVzZXJWZXJzaW9uIjoiMiIsInZVc2VyUm9sZUlEIjoiYjVhNzViYzItNTQ0YS00NmJiLWJkNzItM2Y1ZmY2ZmY4NGRkIiwidk1vYmlsZSI6IjE1OTUwNjg1MjI1IiwidlVzZXJUeXBlIjoiMCIsInZPcmciOiIwMiIsInZQZXJzb25OYW1lIjoi6ZmI5bqXIiwiaUQiOiI5ZDBjNGYwNy00NDFjLTRiMjYtYTJlYS0zZTRkMjNiZTRkZDciLCJ1c2VyVHlwZSI6IjAiLCJkZXZpY2UiOiJwYyIsInZEZWFsZXJOYW1lIjoi5b6Q5bee6YeR5rqQ5rG96L2m5pyN5Yqh5pyJ6ZmQ5YWs5Y-4IiwiZXhwIjoxNjIxMzQyNzc3fQ.GxH1vjTtnWj6C9WsKIKi3xAKzrZKxPGl93OCTGg3m9j8x__JbY6pCFa_FX3FqpfPEVgI0tQYi2WrGu7tQ58qklJt4hb1TB8hRnMys4MysEjt83QKmP6850zadEYqQQGHuQBjLIzAKMwKNbFaU6mrzey4UB14ZlMMwFVJH4Lke6A");
        post.addHeader("cookie", "cna=AJoqGR6tXwkCAbSiSg6ahZmi; jwt=eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlZGU5NzVlYi1kNjZjLTRlZDMtYjZjMS1mOWEwNTg2MTY1MTU6PnBjIiwidXNlcklkIjoiZWRlOTc1ZWItZDY2Yy00ZWQzLWI2YzEtZjlhMDU4NjE2NTE1IiwibmFtZSI6IiIsInJlbWFyayI6IiIsImtpY2tPdXQiOnRydWUsIm5ESUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ2QWRkcmVzcyI6IiIsImNJbml0UHdkIjoiMCIsInZSb2xlTmFtZSI6IumUgOWUrumhvumXriIsInZEZWFsZXIiOiI3NTgzMjY5IiwidlBlcnNvbiI6IjAxMjIiLCJ2V2VDaGF0UGMiOiIiLCJuTURNSUQiOiJmMTcwOTFiOC0xZGIxLTQ4OGMtOTlhNi0zMjk0YjhlNGQ2NzgiLCJ2T3JnTmFtZSI6IumUgOWUrumDqCIsIm5ocnBlcnNvbmlkIjoiMjAxOTEyMDI0MTQ4Mzg1OSIsInZXZUNoYXQiOiIiLCJuaWNrbmFtZSI6IiIsIm5CSUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJncmF5Q29kZSI6IjAiLCJ2QnJhbmNoQ29kZSI6IjAwIiwibkN1c3RHcm91cElkIjoiRDQ5RjY2QzYtNTJBMi00RDg3LUI1REMtQzA2OThFMkE4RTc0Iiwic2FsZXNDb2RlIjoiUzczMjA1OCIsInZSb2xlIjoiU0FMRVIiLCJjTWFpbiI6IjEiLCJ2TG9naW4iOiJQNDM0NTg1IiwiY01haW5CcmFuY2giOiIxIiwiY0JpbmRTdGF0ZSI6IjAiLCJ2Um9sZVZlcnNpb24iOiIyIiwidlVzZXJWZXJzaW9uIjoiMiIsInZVc2VyUm9sZUlEIjoiZTk1MmVjYTMtZTc5YS00NWU5LWFjOTktMDcyNjY4YTUxMGQ4Iiwidk1vYmlsZSI6IjE1OTk2OTc4MTgzIiwidlVzZXJUeXBlIjoiMCIsInZPcmciOiIwMiIsInZQZXJzb25OYW1lIjoi6JSh5aOr6aG6IiwiaUQiOiJlZGU5NzVlYi1kNjZjLTRlZDMtYjZjMS1mOWEwNTg2MTY1MTUiLCJ1c2VyVHlwZSI6IjAiLCJkZXZpY2UiOiJwYyIsInZEZWFsZXJOYW1lIjoi5b6Q5bee6YeR5rqQ5rG96L2m5pyN5Yqh5pyJ6ZmQ5YWs5Y-4IiwiZXhwIjoxNjIyMzg0MjcxfQ.NOxQS3FtbksfKPWczezczZ1LBGVsFQtMqdwCnGlLoldDcnonHQZwWzL3gL0x_DCWn3oI8_w09VVhuvbSstvSDJhybkOrVcjI3ljFUj9WiFFve_AEG96fSstruqsTm-841rSF3j5eLWF8DaZ8g96eRDittzleT17RurW7dCp-dYw; userid=ede975eb-d66c-4ed3-b6c1-f9a058616515; username=%E8%94%A1%E5%A3%AB%E9%A1%BA; nDid=75832690-0000-0000-0000-000000000000; vDealerName=%E5%BE%90%E5%B7%9E%E9%87%91%E6%BA%90%E6%B1%BD%E8%BD%A6%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8");
        post.addHeader("jwt", "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlZGU5NzVlYi1kNjZjLTRlZDMtYjZjMS1mOWEwNTg2MTY1MTU6PnBjIiwidXNlcklkIjoiZWRlOTc1ZWItZDY2Yy00ZWQzLWI2YzEtZjlhMDU4NjE2NTE1IiwibmFtZSI6IiIsInJlbWFyayI6IiIsImtpY2tPdXQiOnRydWUsIm5ESUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ2QWRkcmVzcyI6IiIsImNJbml0UHdkIjoiMCIsInZSb2xlTmFtZSI6IumUgOWUrumhvumXriIsInZEZWFsZXIiOiI3NTgzMjY5IiwidlBlcnNvbiI6IjAxMjIiLCJ2V2VDaGF0UGMiOiIiLCJuTURNSUQiOiJmMTcwOTFiOC0xZGIxLTQ4OGMtOTlhNi0zMjk0YjhlNGQ2NzgiLCJ2T3JnTmFtZSI6IumUgOWUrumDqCIsIm5ocnBlcnNvbmlkIjoiMjAxOTEyMDI0MTQ4Mzg1OSIsInZXZUNoYXQiOiIiLCJuaWNrbmFtZSI6IiIsIm5CSUQiOiI3NTgzMjY5MC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJncmF5Q29kZSI6IjAiLCJ2QnJhbmNoQ29kZSI6IjAwIiwibkN1c3RHcm91cElkIjoiRDQ5RjY2QzYtNTJBMi00RDg3LUI1REMtQzA2OThFMkE4RTc0Iiwic2FsZXNDb2RlIjoiUzczMjA1OCIsInZSb2xlIjoiU0FMRVIiLCJjTWFpbiI6IjEiLCJ2TG9naW4iOiJQNDM0NTg1IiwiY01haW5CcmFuY2giOiIxIiwiY0JpbmRTdGF0ZSI6IjAiLCJ2Um9sZVZlcnNpb24iOiIyIiwidlVzZXJWZXJzaW9uIjoiMiIsInZVc2VyUm9sZUlEIjoiZTk1MmVjYTMtZTc5YS00NWU5LWFjOTktMDcyNjY4YTUxMGQ4Iiwidk1vYmlsZSI6IjE1OTk2OTc4MTgzIiwidlVzZXJUeXBlIjoiMCIsInZPcmciOiIwMiIsInZQZXJzb25OYW1lIjoi6JSh5aOr6aG6IiwiaUQiOiJlZGU5NzVlYi1kNjZjLTRlZDMtYjZjMS1mOWEwNTg2MTY1MTUiLCJ1c2VyVHlwZSI6IjAiLCJkZXZpY2UiOiJwYyIsInZEZWFsZXJOYW1lIjoi5b6Q5bee6YeR5rqQ5rG96L2m5pyN5Yqh5pyJ6ZmQ5YWs5Y-4IiwiZXhwIjoxNjIyMzg0MjcxfQ.NOxQS3FtbksfKPWczezczZ1LBGVsFQtMqdwCnGlLoldDcnonHQZwWzL3gL0x_DCWn3oI8_w09VVhuvbSstvSDJhybkOrVcjI3ljFUj9WiFFve_AEG96fSstruqsTm-841rSF3j5eLWF8DaZ8g96eRDittzleT17RurW7dCp-dYw");
        post.setEntity(formEntity);//捆绑参数
        response = client.execute(post);//执行登录操作
        HttpEntity entity = response.getEntity();
        rawHtml = EntityUtils.toString(entity, "utf-8");
        log.info("响应提示：\n{}", rawHtml);
        JsonParser parse = new JsonParser();  //创建json解析器
        JsonObject object = (JsonObject) parse.parse(rawHtml);
        String dataObj = object.get("code").getAsString();
        if (Integer.valueOf(dataObj).intValue() == HttpStatus.SC_OK) {
            log.info("获取列表成功");
            List<PersonVO> list = getDataVos(object);
            exportExcel(list);
        }
        return true;
    }

    private void exportExcel(List<PersonVO> vos) {
        try {
            ExcelUtils2.exportExcel(vos, outPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private List<PersonVO> getDataVos(JsonObject object) throws ParseException {
        List<PersonVO> list = new ArrayList<>();
        JsonElement jsonElement = object.get("data");
        if (jsonElement == null) {
            log.info("错误数据object：【{}】", rawHtml);
            return null;
        }
        JsonObject dataObj = jsonElement.getAsJsonObject();
        JsonArray recordList = dataObj.get("records").getAsJsonArray();
        String customerType01 = "销售联系人";
        String customerType02 = "试驾联系人";
        String relationship = "车主本人";
        for (int i = 0; i < recordList.size(); i++) {
            PersonVO vo = new PersonVO();
            JsonObject obj = recordList.get(i).getAsJsonObject();
            Optional.ofNullable(obj.get("consultant")).ifPresent(column->{
                if (!column.isJsonNull()) {
                    vo.setConsultant(column.getAsString());
                }
            });
            Optional.ofNullable(obj.get("contactMobile")).ifPresent(column->{
                if (!column.isJsonNull()) {
                    vo.setContactMobile(column.getAsString());
                }
            });
            Optional.ofNullable(obj.get("mobilePhone")).ifPresent(column->{
                if (!column.isJsonNull()) {
                    vo.setMobilePhone(column.getAsString());
                }
            });
            Optional.ofNullable(obj.get("contactName")).ifPresent(column->{
                if (!column.isJsonNull()) {
                    vo.setContactName(column.getAsString());
                }
            });
            Optional.ofNullable(obj.get("customerName")).ifPresent(column->{
                if (!column.isJsonNull()) {
                    vo.setCustomerName(column.getAsString());
                }
            });
            Optional.ofNullable(obj.get("relationship")).ifPresent(column->{
                if (!column.isJsonNull()) {
                    vo.setRelationship(column.getAsString());
                    if (StringUtils.equals("01",column.getAsString())) {
                        vo.setRelationship(relationship);
                    }
                }
            });
            Optional.ofNullable(obj.get("remark")).ifPresent(column->{
                if (!column.isJsonNull()) {
                    vo.setRemark(column.getAsString());
                }
            });
            Optional.ofNullable(obj.get("contactType")).ifPresent(column->{
                if (!column.isJsonNull()) {
                    vo.setContactType(column.getAsString());
                    if (StringUtils.equals("1",column.getAsString())) {
                        vo.setContactType(customerType01);
                    }
                    if (StringUtils.equals("2",column.getAsString())) {
                        vo.setContactType(customerType02);
                    }
                }
            });
            list.add(vo);
        }

        return list;
    }



    public static void main(String[] args) throws IOException, InterruptedException {

        Client2 client = new Client2();
//        Client client = new Client("yxbh@379634044", "8ddcff3a80f4189ca1c9d4d902c3c909");
//            Thread.sleep(1000);
        boolean login = client.login();
        if (login) {
            Date date = new Date();
            log.info("时间戳：_t {}",System.currentTimeMillis());
            log.info("登录成功【{}】", DateUtils.format(date, DateUtils.LONG_WEB_FORMAT_NO_SEC));
            try {
                boolean orders = client.getOrders();
                if (orders) {
                    log.info("导出成功！");
                    Date date2 = new Date();
                    log.info("耗时【{}】秒", (date2.getTime() - date.getTime()) / 1000);
                } else {
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
