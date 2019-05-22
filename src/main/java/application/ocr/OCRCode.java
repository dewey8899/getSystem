package application.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by deweydu
 * Date on 2019/5/22
 */
public class OCRCode {

    @Value("${OriginalImg}")
    private static String OriginalImg;
    @Value("${ocrResult}")
    private static String ocrResult;

    //原始验证码地址
//    private static final String OriginalImg = "e:/images/verifyCode.jpg";

    //识别样本输出地址
//    private static final String ocrResult = "e:/images/orcResult.jpg";
    /**
     * 其中removeBackground方法去除验证码噪点，首先我定义了一个临界阈值，这个值代表像素点的亮度，
     * 我们在实际扫描验证码的每一个像素块时通过判断该像素块的亮度（获取该像素块的三原色）是否超过该自定义值，
     * 从而判断出是否删除或保留该像素块，因此针对不同的验证码我们可以调整这个阈值，比如像我上面列出的几种验证码波动一般都在100~600之间，
     * 通过测试就得出一个比较适中的阈值可以大大提高验证码的提纯度。
     *
     * @param imgUrl
     * @param resUrl
     */
    public static void removeBackground(String imgUrl, String resUrl){
        //定义一个临界阈值
        int threshold = 300;
        try{
            BufferedImage img = ImageIO.read(new File(imgUrl));
            int width = img.getWidth();
            int height = img.getHeight();
            for(int i = 1;i < width;i++){
                for (int x = 0; x < width; x++){
                    for (int y = 0; y < height; y++){
                        Color color = new Color(img.getRGB(x, y));
//                        System.out.println("red:"+color.getRed()+" | green:"+color.getGreen()+" | blue:"+color.getBlue());
                        int num = color.getRed()+color.getGreen()+color.getBlue();
                        if(num >= threshold){
                            img.setRGB(x, y, Color.WHITE.getRGB());
                        }
                    }
                }
            }
            for(int i = 1;i<width;i++){
                Color color1 = new Color(img.getRGB(i, 1));
                int num1 = color1.getRed()+color1.getGreen()+color1.getBlue();
                for (int x = 0; x < width; x++)
                {
                    for (int y = 0; y < height; y++)
                    {
                        Color color = new Color(img.getRGB(x, y));

                        int num = color.getRed()+color.getGreen()+color.getBlue();
                        if(num==num1){
                            img.setRGB(x, y, Color.BLACK.getRGB());
                        }else{
                            img.setRGB(x, y, Color.WHITE.getRGB());
                        }
                    }
                }
            }
            File file = new File(resUrl);
            if (!file.exists())
            {
                File dir = file.getParentFile();
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                try
                {
                    file.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            ImageIO.write(img, "jpg", file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 其中裁剪图片我们需要预先读取宽高度，确认需要裁剪的像素宽度，
     * 输出就得到了一个清晰的图像。到这里基本完成了对验证码的简单处理
     * @param imgUrl
     */
    public static void cuttingImg(String imgUrl){
        try{
            File newfile=new File(imgUrl);
            BufferedImage bufferedimage=ImageIO.read(newfile);
            int width = bufferedimage.getWidth();
            int height = bufferedimage.getHeight();
            if (width > 52) {
                bufferedimage=OCRCode.cropImage(bufferedimage, -1,-1,width,height);
                if (height > 16) {
                    bufferedimage=OCRCode.cropImage(bufferedimage,0,-1,width,height);
                }
            }else{
                if (height > 16) {
                    bufferedimage=OCRCode.cropImage(bufferedimage,0,-1, (width),height);
                }
            }
            ImageIO.write(bufferedimage, "jpg", new File(imgUrl));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static BufferedImage cropImage(BufferedImage bufferedImage, int startX, int startY, int endX, int endY) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (startX == -1) {
            startX = 0;
        }
        if (startY == -1) {
            startY = 0;
        }
        if (endX == -1) {
            endX = width - 1;
        }
        if (endY == -1) {
            endY = height - 1;
        }
        BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                int rgb = bufferedImage.getRGB(x, y);
                result.setRGB(x - startX, y - startY, rgb);
            }
        }
        return result;
    }
    public static String executeTess4J(String imgUrl){
        String ocrResult = "";
        try{
            ITesseract instance = new Tesseract();
            File imgDir = new File(imgUrl);
            ocrResult = instance.doOCR(imgDir);
        }catch (TesseractException e){
            e.printStackTrace();
        }
        return ocrResult;
    }

    public static String getCode(String originalImg,String result){
        if (null == originalImg){
            originalImg = OriginalImg;
        }
        if (null == result){
            result = ocrResult;
        }
        //去噪点
        OCRCode.removeBackground(originalImg, result);
        //裁剪边角
        OCRCode.cuttingImg(ocrResult);
        //OCR识别
        String code = executeTess4J(ocrResult);
        return code;
    }

    public static void main(String[] args){

        String OriginalImg = "e:/images/code.jpg";
        String ocrResult = "e:/images/orcResult.jpg";
        System.out.println("Ocr识别结果: \n" + getCode(OriginalImg,ocrResult));

    }
}
