package get.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FilenameUtils;
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

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by deweydu
 * Date on 2019/3/29
 */
public class Client {

    static CookieStore cookieStore = new BasicCookieStore();
    private String accout;
    private String password;
    private String validateCode;
    private String uuid = "12E76872-4CC3-4187-B746-0946D7CA49CB";

    /**
     * 实例化httpclient
     */
    CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

    CloseableHttpResponse response = null;
    String rawHtml;

    public Client(String accout, String password) {
        super();
        this.accout = accout;
        this.password = password;
    }

    //1.登录
    public boolean login() {
        HttpGet getLoginPage = new HttpGet("https://sh.tivolitech.com/login");//教务处登陆页面get
        try {
            client.execute(getLoginPage);
            printCookies();
            getVerifyingCode(client);
            printCookies();
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
            HttpPost post = new HttpPost("https://api.tivolitech.com/business/login");//构建post对象
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
            System.out.println("Initial set of cookies:");
            printCookies();
            rawHtml = EntityUtils.toString(entity, "utf-8");
            System.out.println(rawHtml);
            return true;
        } catch (ClientProtocolException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void printCookies() {
        List<Cookie> cookies0 = cookieStore.getCookies();
        if (cookies0.isEmpty()) {
            System.out.println("No cookies");
        } else {
            for (int i = 0; i < cookies0.size(); i++) {
                System.out.println("- " + cookies0.get(i).toString());
            }
        }
    }

    void getVerifyingCode(HttpClient client) {
        String url = "https://api.tivolitech.com/user/login/code?uuid=";
        HttpGet getVerifyCode = new HttpGet(url + uuid);//验证码get
        OutputStream outputStream = null;
        HttpResponse response;
        try {
            response = client.execute(getVerifyCode);//获取验证码
            /*验证码写入文件,当前工程的根目录,保存为verifyCode.jped*/
            outputStream = new FileOutputStream(new File("e:/verifyCode.jpg"));
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

    private static File createImage(String imagePath) throws IOException {
        String fileType = getFileType(imagePath);
        Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName(fileType);
        ImageReader reader = iterator.next();/*获取图片尺寸*/
        InputStream inputStream = new FileInputStream(imagePath);
        ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rectangle = new Rectangle(0,0, 400, 250);        /*指定截取范围*/
        param.setSourceRegion(rectangle);
        BufferedImage bi = reader.read(0,param);
        File outfile = new File("e:/images/verifyCode22.jpg");
        File dir = new File(outfile.getParent());
        if(!dir.exists()) {
            dir.mkdirs();
        }
        ImageIO.write(bi, "JPEG", outfile);
        return outfile;
    }
    public static String getFileType(String filePath) {
        FileInputStream fis = null;
        /**
         * 根据文件名称，获取后缀名的方式，但是不保险
         */
        String extension = FilenameUtils.getExtension(filePath);
        try {
            fis = new FileInputStream(new File(filePath));
            byte[] bs = new byte[1];
            fis.read(bs);
            String type = Integer.toHexString(bs[0]&0xFF);
            if("ff".equalsIgnoreCase(type))  extension = "JPEG";
            if("89".equalsIgnoreCase(type)) extension = "PNG";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取图片类型出错 : " +  filePath);
        } finally {
            try{
                if(fis != null) fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return extension;
    }
    //2.获取所有的订单
    public void getOrders() throws IOException, URISyntaxException {
        HttpResponse response = null;
        String getOrdersUrl = "https://api.tivolitech.com/order/operate";
        ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("backgroundType", "sh"));
        postData.add(new BasicNameValuePair("phone", ""));
        postData.add(new BasicNameValuePair("goodsName", ""));
        postData.add(new BasicNameValuePair("uid", ""));
        postData.add(new BasicNameValuePair("machine_id", ""));
        postData.add(new BasicNameValuePair("order_id", ""));
        postData.add(new BasicNameValuePair("busname", ""));
        postData.add(new BasicNameValuePair("pageSize", "100000"));
        postData.add(new BasicNameValuePair("pageNumber", "1"));
        postData.add(new BasicNameValuePair("starDate", "2019-02-21+10:02"));
        postData.add(new BasicNameValuePair("endDate", "2019-03-30+10:02"));

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
            System.out.println("获取列表成功");
            HttpEntity entity = response.getEntity();
            rawHtml = EntityUtils.toString(entity, "utf-8");
            System.out.println(rawHtml);
            List<String> orderIds = getOrderIds(rawHtml);//3.获取所有的订单ID
            if (orderIds.size() > 0) {
                List<DataVO> vos = getOrderDetail(orderIds);//4.获取每个订单的商品列表
                exportExcel(vos);
            }
        }
    }

    private void exportExcel(List<DataVO> vos) {
        try {
            ExcelUtils.exportExcel(vos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<DataVO> getOrderDetail(List<String> orderIds) throws URISyntaxException, IOException {
        HttpResponse response = null;
        List<DataVO> vos = new ArrayList<DataVO>();
        String getOrdersUrl = "https://api.tivolitech.com/order/operateDetails";
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
                vos.addAll(list);
            }
        }
        return vos;

    }

    private List<DataVO> getDataVos(String rawHtml) {
        System.out.println(rawHtml);
        List<DataVO> list = new ArrayList<DataVO>();
        JsonParser parse = new JsonParser();  //创建json解析器
        JsonObject object = (JsonObject) parse.parse(rawHtml);
        JsonObject dataObj = object.get("data").getAsJsonObject();
        JsonObject operaterInfo = dataObj.get("operaterInfo").getAsJsonObject();
        String order_id = operaterInfo.get("order_id").getAsString();
        String createTime = operaterInfo.get("createTime").getAsString();
        String status = operaterInfo.get("status").getAsString();
        String userName = operaterInfo.get("userName").getAsString();
        String phone = operaterInfo.get("phone").getAsString();
        String machineName = operaterInfo.get("machineName").getAsString();
        JsonArray productList = dataObj.get("productList").getAsJsonArray();
        if (productList.size() <= 0) {
            System.out.println("订单号：" + order_id + "没有商品数据！");
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
        JsonArray array = object.get("data").getAsJsonArray();    //得到为json的数组
        if (array.size() <= 0) {
            System.out.println("没有数据！");
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

    public static void main(String[] args) throws IOException {

//        String imagePath2 = "e:/images/code.jpg";
        String imagePath2 = "e:/images/test.jpg";
//        String imagePath2 = "e:/images/verifyCode.jpg";
//        String imagePath2 = "e:/images/checkcode.gif";
        File imageFile = createImage(imagePath2);
//        File imageFile = new File(imagePath);

        BufferedImage bufferedImage = ImageIO.read(imageFile);
        ITesseract tessreact = new Tesseract();
        tessreact.setLanguage("eng");
        tessreact.setDatapath("e:/tessdata");
        try {
//            String result = tessreact.doOCR(imageFile);
            String result = tessreact.doOCR(bufferedImage);
            System.out.println("识别验证码为："+result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

//        Client client = new Client("yxbh@379634044", "8ddcff3a80f4189ca1c9d4d902c3c909");
//        if (client.login()) {
//
//            try {
//                client.getOrders();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }


    }
}
