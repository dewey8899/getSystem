package application.system;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * Created by deweydu
 * Date on 2019/9/9 11:33
 */
public class WritExcel {
        public static final Integer WRIT_TITLE = 1;
        public static final Integer NO_WRIT_TITLE = 0;
        private static String format = "yyyy-MM-dd HH:mm:ss";
        private static String nullDisplay = "";
        private List<String> columnName = new ArrayList<String>();
        private Map<String, String> columnObject = new HashMap<String, String>();
        private String savePath;
        private OutputStream outputStream;
        /**
         * 构造写入EXCEL的路径
         * 使用 put 方法创建导出的内容
         * 使用 writ 方法将对象写入EXCEL
         * @param savePath
         * @throws FileNotFoundException
         */
        public WritExcel(String savePath) {
            this.savePath = savePath;
        }
/**
 * 构造写入EXCEL的路径
 * 使用 put 方法创建导出的内容
 * 使用 writOutputStream 方法将对象写入 HSSFWorkbook
 * @param savePath
 * @throws FileNotFoundException
 */
/*public WritExcel() {
}*/
        /**
         * 构建导出顺序及导出字段
         * @param key 导出字段名
         * @param value 表头
         */
        public void put(String key,String value) {
            this.columnName.add(key);
            this.columnObject.put(key, value);
        }
        /**
         * 写入EXCEL
         * @param <T> 写入EXCEL的对象类型
         * @param objects 写入EXCEL的对象
         * @param model 写不写表头
         * @throws IOException
         * @throws InvocationTargetException
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         */
        public <T> void writ(List<T> objects,Integer model) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
// 创建一个Excel
            HSSFWorkbook book = new HSSFWorkbook();
// 创建一个工作簿
            HSSFSheet sheet = book.createSheet();
// 设置工作簿的默认列宽
            sheet.setDefaultColumnWidth(30);
            Integer start = 0;
            if (model == WRIT_TITLE) {
                HSSFCellStyle titleStyle = buildTitleStyle(book);
                writExcelTitle(sheet,titleStyle);
                start = WRIT_TITLE;
            }
            HSSFCellStyle columnStyle = buildColumnStyle(book);
            writExcelColumn(objects,start,sheet,columnStyle);
            outputStream = new FileOutputStream(savePath);
            book.write(outputStream);
            outputStream.close();
        }
/**
 * 写入EXCEL
 * @param <T> 写入EXCEL的对象类型
 * @param objects 写入EXCEL的对象
 * @param model 写不写表头
 * @throws IOException
 * @throws InvocationTargetException
 * @throws IllegalAccessException
 * @throws IllegalArgumentException
 */
/*public <T> HSSFWorkbook writOutputStream(List<T> objects,Integer model) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
// 创建一个Excel
HSSFWorkbook book = new HSSFWorkbook();
// 创建一个工作簿
HSSFSheet sheet = book.createSheet();
// 设置工作簿的默认列宽
sheet.setDefaultColumnWidth((short)30);
Integer start = 0;
if (model == WRIT_TITLE) {
HSSFCellStyle titleStyle = buildTitleStyle(book);
writExcelTitle(sheet,titleStyle);
start = WRIT_TITLE;
}
HSSFCellStyle columnStyle = buildColumnStyle(book);
writExcelColumn(objects,start,sheet,columnStyle);
return book;
}*/
        /**
         * 使用内容样式
         * @return
         */
        private HSSFCellStyle buildColumnStyle(HSSFWorkbook book){
// 设置样式
            HSSFCellStyle columnStyle = book.createCellStyle();
//titleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            columnStyle.setLeftBorderColor(HSSFCellStyle.BORDER_THIN);
            columnStyle.setRightBorderColor(HSSFCellStyle.BORDER_THIN);
            columnStyle.setTopBorderColor(HSSFCellStyle.BORDER_THIN);
            columnStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
// 设置字体
            HSSFFont font = book.createFont();
//font.setColor(HSSFColor.VIOLET.index);
//font.setFontHeight((short)12);
//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
// 把字体应用到当前样式
            columnStyle.setFont(font);
            return columnStyle;
        }
        /**
         * 使用表头样式
         */
        private HSSFCellStyle buildTitleStyle(HSSFWorkbook book){
// 设置样式
            HSSFCellStyle titleStyle = book.createCellStyle();
//titleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            titleStyle.setLeftBorderColor(HSSFCellStyle.BORDER_THIN);
            titleStyle.setRightBorderColor(HSSFCellStyle.BORDER_THIN);
            titleStyle.setTopBorderColor(HSSFCellStyle.BORDER_THIN);
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
// 设置字体
            HSSFFont font = book.createFont();
//font.setColor(HSSFColor.VIOLET.index);
//font.setFontHeight((short)12);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
// 把字体应用到当前样式
            titleStyle.setFont(font);
            return titleStyle;
        }
        /**
         * 写入表头
         */
        private void writExcelTitle(HSSFSheet sheet,HSSFCellStyle titleStyle) throws IOException {
// 表头
            HSSFRow title = sheet.createRow(0);
            for (Integer i = 0; i < columnName.size(); i++) {
                HSSFCell cell = title.createCell(i);
                cell.setCellStyle(titleStyle);
                HSSFRichTextString value = new HSSFRichTextString(columnObject.get(columnName.get(i)));
                cell.setCellValue(value);
            }
        }
        /**
         * 写入内容
         * @throws InvocationTargetException
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         */
        private <T> void writExcelColumn(List<T> objects,Integer start,HSSFSheet sheet,HSSFCellStyle titleStyle) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
            for (int i = 0; i < objects.size(); i++) {
                Object object = objects.get(i);
                Class<?> cls = object.getClass();
                HSSFRow column = sheet.createRow(start);
                for (int j = 0; j < columnName.size(); j++) {
                    try {
                        Method method = cls.getMethod(getMethod(columnName.get(j)));
                        Object invokeResult = method.invoke(object);
                        String result = returnTypeResult(invokeResult);
                        HSSFCell cell = column.createCell(j);
                        cell.setCellStyle(titleStyle);
                        HSSFRichTextString value = new HSSFRichTextString(result);
                        cell.setCellValue(value);
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                }
                start++;
            }
        }
        /**
         * 构建get方法
         */
        private static String getMethod(String fieldName){
            return "get"+fieldName.substring(0,1).toUpperCase(Locale.CHINA)+fieldName.substring(1);
        }
        private static String returnTypeResult(Object object){
            if (object == null) {
                return nullDisplay;
            }
            if (object.getClass().getName().equals("java.util.Date")) {
                return formatDate(object);
            }else if (object.getClass().getName().equals("java.sql.Timestamp")) {
                Timestamp timestamp = Timestamp.valueOf(object.toString());
                Date date = timestamp;
                return formatDate(date);
            }else {
                return object.toString();
            }
        }
        /**
         * formatDate
         */
        private static String formatDate(Object date) {
            return new SimpleDateFormat(format).format(date);
        }
}
