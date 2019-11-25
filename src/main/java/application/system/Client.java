package application.system;

import application.ocr.OCRCode;
import application.utils.DateUtils;
import application.utils.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by deweydu
 * Date on 2019/3/29
 */
@Slf4j
@Component
public class Client {

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
        HttpGet getLoginPage = new HttpGet("https://sh.tivolitech.com:1391/login");//登陆页面get
        try {
            client.execute(getLoginPage);
            printCookies();
            getVerifyingCode(client);
            printCookies();
//            /**
//             * 获取自动识别到的验证码
//             */
//            validateCode = getOCRCode();

            //手动输入验证码
            //提醒用户并输入验证码
            System.out.println("请输入验证码:\n");
            Scanner in = new Scanner(System.in);
            validateCode = in.nextLine();
            in.close();


            ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
            postData.add(new BasicNameValuePair("backgroundType", "sh"));
            postData.add(new BasicNameValuePair("uuid", uuid));
            postData.add(new BasicNameValuePair("userName", accout));//用户名
            postData.add(new BasicNameValuePair("password", password));//密码
            postData.add(new BasicNameValuePair("validateCode", validateCode));//验证码
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            HttpPost post = new HttpPost("https://api.tivolitech.com:1391/business/login");//构建post对象
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postData);
            post.setEntity(formEntity);//捆绑参数
            post.setConfig(requestConfig);
            List<Cookie> cookies1 = cookieStore.getCookies();
            String sessionId = null;
            for (int i = 0; i < cookies1.size(); i++) {
                if (cookies1.get(i).getName().equalsIgnoreCase("JSESSIONID")) {
                    sessionId = cookies1.get(i).getValue();
                    break;
                }
            }
            post.setHeader("JSESSIONID", sessionId);

            response = client.execute(post);//执行登录操作

            HttpEntity entity = response.getEntity();
            log.info("Initial set of cookies:");
            printCookies();
            rawHtml = EntityUtils.toString(entity, "utf-8");
            log.info("输入用户名,密码,验证码后的响应：\n{}",rawHtml);
            JsonParser parse = new JsonParser();  //创建json解析器
            JsonObject object = (JsonObject) parse.parse(rawHtml);
            String dataObj = object.get("code").getAsString();
            if (dataObj.equals("0")){
                return true;
            }else {
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

    private String getOCRCode() {
        String code = OCRCode.getCode(OriginalImg,ocrResult,tessDataPath);
        if (null!=code&& code.length()>=4){
            code = code.substring(0, 4);
            validateCode = code;
        }
        log.info(String.format("自动识别到的验证码为:%s",code));
        return code;
    }

    private void printCookies() {
        List<Cookie> cookies0 = cookieStore.getCookies();
        if (cookies0.isEmpty()) {
            log.info("No cookies");
        } else {
            for (int i = 0; i < cookies0.size(); i++) {
                log.info("- " + cookies0.get(i).toString());
            }
        }
    }

    void getVerifyingCode(HttpClient client) {
//        String url = "https://api.tivolitech.com/user/login/code?uuid=";
        String url = "https://api.tivolitech.com:1391/user/login/code?uuid=";
        HttpGet getVerifyCode = new HttpGet(url + uuid);//验证码get
        OutputStream outputStream = null;
        HttpResponse response;
        try {
            response = client.execute(getVerifyCode);//获取验证码
            /*验证码写入文件,当前工程的根目录,保存为verifyCode.jped*/
            outputStream = new FileOutputStream(new File(OriginalImg));
            response.getEntity().writeTo(outputStream);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //2.获取所有的订单
    public boolean getOrders() throws IOException, URISyntaxException, ParseException {
        HttpResponse response = null;
        String getOrdersUrl = "https://api.tivolitech.com:1391/order/operate";
        ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("backgroundType", "sh"));
        postData.add(new BasicNameValuePair("phone", ""));
        postData.add(new BasicNameValuePair("goodsName", ""));
        postData.add(new BasicNameValuePair("uid", ""));
        postData.add(new BasicNameValuePair("machine_id", ""));
        postData.add(new BasicNameValuePair("machine_name", ""));
        postData.add(new BasicNameValuePair("order_id", ""));
        postData.add(new BasicNameValuePair("busname", ""));
        postData.add(new BasicNameValuePair("pageSize", "100000"));
        postData.add(new BasicNameValuePair("pageNumber", "1"));
        postData.add(new BasicNameValuePair("starDate", "2019-10-01+10:02"));
        postData.add(new BasicNameValuePair("endDate", "2022-12-31+10:02"));

        URIBuilder uriBuilder = new URIBuilder(getOrdersUrl);
        uriBuilder.clearParameters();
        uriBuilder.setParameters(postData);
        HttpGet httpGet =
                new HttpGet(uriBuilder.build());//构建post对象
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectTimeout(60000).build();
        httpGet.setConfig(requestConfig);
        List<Cookie> cookies = cookieStore.getCookies();
        String sessionId = null;
        String shSession = null;
        for (int i = 0; i < cookies.size(); i++) {
            if (cookies.get(i).getName().equalsIgnoreCase("JSESSIONID")) {
                sessionId = cookies.get(i).getValue();
            }
            if (cookies.get(i).getName().equalsIgnoreCase("shSession")) {
                shSession = cookies.get(i).getValue();
            }
        }
        httpGet.setHeader("JSESSIONID", sessionId);
        httpGet.setHeader("shSession", shSession);
        response = client.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            log.info("获取列表成功");
            HttpEntity entity = response.getEntity();
            rawHtml = EntityUtils.toString(entity, "utf-8");
//            log.info("获取到的数据{}",rawHtml);
            if (null == rawHtml){
                return false;
            }
            List<String> orderIds = getOrderIds(rawHtml);//3.获取所有的订单ID
            if (CollectionUtils.isNotEmpty(orderIds)) {
                List<DataVO> vos = getOrderDetail(orderIds);//4.获取每个订单的商品列表
                if (CollectionUtils.isEmpty(vos)){
                    return false;
                }
                exportExcel(vos);
            }else {
                return false;
            }
        }
        return true;
    }

    private void exportExcel(List<DataVO> vos) {
        try {
            ExcelUtils.exportExcel(vos,outPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<DataVO> getOrderDetail(List<String> orderIds) throws URISyntaxException, IOException, ParseException {
        HttpResponse response = null;
        List<DataVO> vos = new ArrayList<DataVO>();
        String getOrdersUrl = "https://api.tivolitech.com:1391/order/operateDetails";
        URIBuilder uriBuilder = new URIBuilder(getOrdersUrl);
        for (int i = 0; i < orderIds.size(); i++) {
            ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
            postData.add(new BasicNameValuePair("backgroundType", "sh"));
            postData.add(new BasicNameValuePair("order_id", orderIds.get(i)));
            uriBuilder.setParameters(postData);

            HttpGet httpGet = new HttpGet(uriBuilder.build());//构建get对象
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(60000)
                    .setConnectTimeout(60000).build();
            httpGet.setConfig(requestConfig);
            List<Cookie> cookies = cookieStore.getCookies();
            String sessionId = null;
            String shSession = null;
            for (int j = 0; j < cookies.size(); j++) {
                if (cookies.get(j).getName().equalsIgnoreCase("shSession")) {
                    shSession = cookies.get(j).getValue();
                }
                if (cookies.get(j).getName().equalsIgnoreCase("JSESSIONID")) {
                    sessionId = cookies.get(j).getValue();
                }
            }
            httpGet.setHeader("JSESSIONID", sessionId);
            httpGet.setHeader("shSession", shSession);
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            rawHtml = EntityUtils.toString(entity, "utf-8");
            List<DataVO> list = getDataVos(rawHtml);
            if (list != null) {
                if (null == list.get(0).getOrder_id() || !list.get(0).isFlag()){
                    break;
                }else {
                    vos.addAll(list);
                }
            }
        }
        return vos;
    }

    private List<DataVO> getDataVos(String rawHtml) throws ParseException {
        log.info("方法：getDataVos入参 【{}】",rawHtml);
        List<DataVO> list = new ArrayList<>();
        JsonParser parse = new JsonParser();  //创建json解析器
        JsonObject object = (JsonObject) parse.parse(rawHtml);
        JsonElement jsonElement = object.get("data");
        if(jsonElement==null){
            log.info("错误数据object：【{}】",rawHtml);
            return null;
        }
        JsonObject dataObj = jsonElement.getAsJsonObject();
        JsonElement operaterInfoElement = dataObj.get("operaterInfo");
        JsonObject operaterInfo = null;
        try {
            if (operaterInfoElement==null){
                return null;
            }else if(operaterInfoElement.getAsJsonObject()==null){
                return null;
            }
            else{
                operaterInfo = operaterInfoElement.getAsJsonObject();
                if (operaterInfo==null){
                    return null;
                }
            }
        }catch (IllegalStateException e){
            return null;
        }

        String order_id = operaterInfo.get("order_id").getAsString();
        String createTime = operaterInfo.get("createTime").getAsString();
        /**
         * 要最新8天的数据sh.tivolitech.com
         */
        if (StringUtils.isNotBlank(createTime)){
            //补货操作时间
            Date date = DateUtils.parse(createTime.substring(0, 10), DateUtils.WEB_FORMAT);
            //当前时间的前8天
            Date date1 = DateUtils.beginDateByToday(-3);
            if (date.before(date1)){
                DataVO vo = new DataVO();
                vo.setFlag(false);
                list.add(vo);
                return list;
            }
        }
        String status = operaterInfo.get("status").getAsString();
        String userName = operaterInfo.get("userName").getAsString();
        String phone = operaterInfo.get("phone").getAsString();
        String machineName = operaterInfo.get("machineName").getAsString();
        JsonArray productList = dataObj.get("productList").getAsJsonArray();
        if (productList.size() <= 0) {
            log.info("订单号：" + order_id + "没有商品数据！");
            return null;
        }
        for (int i = 0; i < productList.size(); i++) {
            DataVO vo = new DataVO();
            String proStatus = status;
            JsonObject obj = productList.get(i).getAsJsonObject();
            String productName = obj.get("productName").getAsString();
            String productUID = obj.get("uid").getAsString();
            String type = obj.get("type").getAsString();

            vo.setProductName(productName);
            vo.setProductUID(productUID);
            vo.setType(type);
            vo.setCreateTime(createTime);
            vo.setNum("1");
            vo.setOrder_id(order_id);
            if (status.equals("2")) {
                proStatus = "成功";
            } else if (status.equals("1")) {
                proStatus = "失败";
            } else {
                proStatus = "无";
            }
            vo.setStatus(proStatus);
            vo.setPhone(phone);
            vo.setUserName(userName);
            vo.setMachineName(machineName);
            list.add(vo);
        }

        return list;
    }

    private List<String> getOrderIds(String rawHtml) {
        JsonParser parse = new JsonParser();  //创建json解析器
        JsonObject object = (JsonObject) parse.parse(rawHtml);
        if (null == object){
            return null;
        }
        JsonArray array = object.get("data").getAsJsonArray();    //得到为json的数组
        if (array.size() <= 0) {
            log.info("没有数据！");
            return null;
        }
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject obj = array.get(i).getAsJsonObject();
            String order_id = obj.get("order_id").getAsString();
            list.add(order_id);
        }
        return list;
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        Client client = new Client();
//        Client client = new Client("yxbh@379634044", "8ddcff3a80f4189ca1c9d4d902c3c909");
        while (true){
            Thread.sleep(1000);
            boolean login = client.login();
            if (login){
                Date date = new Date();
                log.info("登录成功【{}】", DateUtils.format(date,DateUtils.LONG_WEB_FORMAT_NO_SEC));
                try {
                    boolean orders = client.getOrders();
                    if (orders){
                        log.info("导出成功！");
                        Date date2 = new Date();
                        log.info("耗时【{}】秒",(date2.getTime()-date.getTime())/1000);
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
