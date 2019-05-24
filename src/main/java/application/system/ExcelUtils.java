package application.system;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: Administrator
 * Date 2019/3/30 0030 上午 0:31
 */
public class ExcelUtils {

    public static void exportExcel(List<DataVO> vos,String outPath) throws FileNotFoundException {
        File file = null;

        file = new File(outPath);

        FileOutputStream os = new FileOutputStream(file);
        String[] headers = { "补货单编号", "补货操作时间", "补货状态", "姓名", "电话", "设备名称", "商品名称", "\n" +
                "商品标签UID" ,"数量" , "商品状态"};
        List<Object[]> dataset = new ArrayList<Object[]>();
        for (int i = 0; i < vos.size();i++) {
            Object[] row = new Object[10];
            DataVO dataVO = vos.get(i);
            row[0] = dataVO.getOrder_id();
            row[1] = dataVO.getCreateTime();
            row[2] = dataVO.getStatus();
            row[3] = dataVO.getUserName();
            row[4] = dataVO.getPhone();
            row[5] = dataVO.getMachineName();
            row[6] = dataVO.getProductName();
            row[7] = dataVO.getProductUID();
            row[8] = dataVO.getNum();
            row[9] = dataVO.getType();
            dataset.add(row);
        }
        exportExcel("补货订单", headers, dataset, os, null);

    }

    public static void exportExcel(String title, String[] headers,
                            List<Object[]> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        // font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        HSSFCellStyle fstyle = workbook.createCellStyle();
        // 设置这些样式
        fstyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        fstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        fstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        fstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        fstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        fstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        fstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        fstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        fstyle.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        int index = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object[] o : dataset) {
            index++;
            row = sheet.createRow(index);

            for (short i = 0; i < o.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                if (o[i] instanceof String) {
                    HSSFRichTextString text = new HSSFRichTextString(
                            (String) o[i]);
                    cell.setCellValue(text);
                }
                if (o[i] instanceof Integer) {
                    cell.setCellValue(((Integer) o[i]).intValue());
                }
                if (o[i] instanceof Long) {
                    cell.setCellValue(scale2points(((Long) o[i]).floatValue()));
                }
                if (o[i] instanceof Date) {
                    HSSFRichTextString text = new HSSFRichTextString(
                            sdf.format(o[i]));
                    cell.setCellValue(text);
                }
                if (o[i] instanceof Double) {
                    cell.setCellValue(scale2points(((Double) o[i]).floatValue()));
                }
                if (o[i] instanceof Float) {
                    cell.setCellValue(scale2points(((Float) o[i]).floatValue()));
                }
                if (o[i] instanceof BigDecimal) {
                    cell.setCellValue(scale2points(((BigDecimal) o[i])
                            .floatValue()));
                }
                if (o[i] instanceof BigInteger) {
                    cell.setCellValue(((BigInteger) o[i]).intValue());
                }
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.getMessage();
        }

    }
    private static Float scale2points(Float f) {
        if (f.isNaN() || f.isInfinite())
            return 0f;
        BigDecimal b = new BigDecimal(f);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }
}
