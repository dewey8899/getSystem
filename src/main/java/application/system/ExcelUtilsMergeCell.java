package application.system;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by deweydu
 * Date on 2019/9/9 9:35
 */
public class ExcelUtilsMergeCell {
    public static void exportExcel(List<?> vos, String outPath) throws FileNotFoundException {
        File file = null;
        file = new File(outPath);
        FileOutputStream os = new FileOutputStream(file);
        String[] headers = { "时间",  "时间", "商品编码", "商品名称", "开票或审核","开票或审核","开票或审核","开票或审核","开票或审核",
                "发货", "发货", "发货", "发货", "发货", "补货金额","补货金额","补货金额","补货金额","补货金额","",""};
        String deliveryAgencyToServiceAmount = "92.94%";
        String replenishAgencyToServiceAmount = "92.94%";
        String[] headers1 = { "时间", "时间", "商品编码", "商品名称", deliveryAgencyToServiceAmount,"服务商未税价格","服务商未税价格",
                "总代未税价格","总代未税价格",replenishAgencyToServiceAmount, "服务商未税价格","服务商未税价格","总代未税价格","总代未税价格",
                "","服务商未税价格","服务商未税价格","总代未税价格","总代未税价格","",""};
        String[] headers2 = { "开始时间","结束时间","商品编码", "商品名称", "数量","单价","金额","单价","金额","数量","单价","金额","单价","金额",
                "数量","单价","金额","单价","金额","开票或审核","发货"};
        String[] headers3 = { "-","-","合计：", "合计：", "123","单价--","金额123.78","--","123.654","541","-","87.32","-","98451.23",
                "-","-","金额","-","金额","",""};
        List<String[]> headList = new ArrayList<>();
        headList.add(headers);
        headList.add(headers1);
        headList.add(headers2);
        headList.add(headers3);
        List<Object[]> dataset = new ArrayList<Object[]>();
        for (int i = 0; i < vos.size();i++) {
            Object[] row = new Object[21];
//            ReportOemProductDifferenceExportBean dataVO = vos.get(i);
//            int j = 0;
//            row[j++] = dataVO.getStartDate();
//            row[j++] = dataVO.getEndDate();
//            row[j++] = dataVO.getMaterialNumber();
//            row[j++] = dataVO.getProductName();
//            row[j++] = dataVO.getDeliveryQuantity();
//            row[j++] = dataVO.getDeliveryServiceBusinessTaxExcludedPrice();
//            row[j++] = dataVO.getDeliveryServiceBusinessTaxExcludedAmount();
//            row[j++] = dataVO.getDeliveryGeneralAgencyTaxExcludedPrice();
//            row[j++] = dataVO.getDeliveryGeneralAgencyTaxExcludedAmount();
//            row[j++] = dataVO.getReplenishQuantity();
//            row[j++] = dataVO.getReplenishServiceBusinessTaxExcludedPrice();
//            row[j++] = dataVO.getReplenishServiceBusinessTaxExcludedAmount();
//            row[j++] = dataVO.getReplenishGeneralAgencyTaxExcludedPrice();
//            row[j++] = dataVO.getReplenishGeneralAgencyTaxExcludedAmount();
//            row[j++] = "";
//            row[j++] = "";
//            row[j++] = "";
//            row[j++] = "";
//            row[j++] = "";
//            row[j++] = dataVO.getDeliveryPriceRatio();
//            row[j++] = dataVO.getReplenishPriceRatio();
            dataset.add(row);
        }
        exportExcel("OEM业务商品差异报表", headList, dataset, os, null,false);
    }

    public static void exportExcel(String title, List<String[]> headers,
                                   List<Object[]> dataset, OutputStream out, String pattern, boolean mergeColumn) {
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // 生成一个表格
        SXSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 10);
        // 生成一个样式
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        // 生成一个字体
        Font font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 10);
        // 把字体应用到当前的样式
        style.setFont(font);
        CellStyle fstyle = workbook.createCellStyle();
        // 设置这些样式
        fstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        fstyle.setFont(font);
        // 生成并设置另一个样式
        CellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        // 生成另一个字体
        Font font2 = workbook.createFont();
        // 把字体应用到当前的样式
        style2.setFont(font2);
        if (mergeColumn){
            //合并单元格
            CellRangeAddress timeAddr = new CellRangeAddress(0,1,0,1);//起始行,结束行,起始列,结束列
            CellRangeAddress materialNumberAddr = new CellRangeAddress(0,2,2,2);//商品编码
            CellRangeAddress productNameAddr = new CellRangeAddress(0,2,3,3);//商品名称
            CellRangeAddress totalAddr = new CellRangeAddress(3,3,2,3);//合计
            CellRangeAddress deliveryAddr = new CellRangeAddress(0,0,4,8);//开票或审核
            CellRangeAddress replenishAddr = new CellRangeAddress(0,0,9,13);//发货
            CellRangeAddress deliverySecondRowServiceAddr = new CellRangeAddress(1,1,5,6);//开票或审核 ->服务商未税价格
            CellRangeAddress deliverySecondRowAgencyAddr = new CellRangeAddress(1,1,7,8);//开票或审核 ->总代未税价格
            CellRangeAddress replenishSecondRowServiceAddr = new CellRangeAddress(1,1,10,11);//发货-> 服务商未税价格
            CellRangeAddress replenishSecondRowAgencyAddr = new CellRangeAddress(1,1,12,13);//发货-> 总代未税价格
            CellRangeAddress lastAddr = new CellRangeAddress(0,0,14,18);//补货余额
            CellRangeAddress lastServcieAddr = new CellRangeAddress(1,1,15,16);//补货余额->服务商未税价格
            CellRangeAddress lastAgencyAddr = new CellRangeAddress(1,1,17,18);//补货余额->总代未税价格
            sheet.addMergedRegion(timeAddr);
            sheet.addMergedRegion(materialNumberAddr);
            sheet.addMergedRegion(productNameAddr);
            sheet.addMergedRegion(deliveryAddr);
            sheet.addMergedRegion(replenishAddr);
            sheet.addMergedRegion(deliverySecondRowServiceAddr);
            sheet.addMergedRegion(deliverySecondRowAgencyAddr);
            sheet.addMergedRegion(replenishSecondRowServiceAddr);
            sheet.addMergedRegion(replenishSecondRowAgencyAddr);
            sheet.addMergedRegion(lastAddr);
            sheet.addMergedRegion(lastServcieAddr);
            sheet.addMergedRegion(lastAgencyAddr);
            sheet.addMergedRegion(totalAddr);
        }
        // 产生表格标题行
        SXSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.get(0).length; i++) {
            SXSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(0)[i]);
            cell.setCellValue(text);
        }
        row = sheet.createRow(1);
        for (short i = 0; i < headers.get(1).length; i++) {
            SXSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(1)[i]);
            cell.setCellValue(text);
        }
        row = sheet.createRow(2);
        for (short i = 0; i < headers.get(2).length; i++) {
            SXSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(2)[i]);
            cell.setCellValue(text);
        }
        row = sheet.createRow(3);
        for (short i = 0; i < headers.get(3).length; i++) {
            SXSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(3)[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        int index = 3;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object[] o : dataset) {
            index++;
            row = sheet.createRow(index);
            for (short i = 0; i < o.length; i++) {
                SXSSFCell cell = row.createCell(i);
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

    public static void main(String[] args) throws FileNotFoundException {
        File file = null;
        file = new File("e:/9月out.xls");
//        FileOutputStream os = new FileOutputStream(file);
//        String[] headers = { "时间",  "时间", "商品编码", "商品名称", "开票或审核","开票或审核","开票或审核","开票或审核","开票或审核",
//                "发货", "发货", "发货", "发货", "发货", "补货金额","补货金额","补货金额","补货金额","补货金额","",""};
//        String deliveryAgencyToServiceAmount = "92.94%";
//        String replenishAgencyToServiceAmount = "92.94%";
//        String[] headers1 = { "时间", "时间", "商品编码", "商品名称", deliveryAgencyToServiceAmount,"服务商未税价格","服务商未税价格",
//                "总代未税价格","总代未税价格",replenishAgencyToServiceAmount, "服务商未税价格","服务商未税价格","总代未税价格","总代未税价格",
//                "","服务商未税价格","服务商未税价格","总代未税价格","总代未税价格","",""};
//        String[] headers2 = { "开始时间","结束时间","商品编码", "商品名称", "数量","单价","金额","单价","金额","数量","单价","金额","单价","金额",
//                "数量","单价","金额","单价","金额","开票或审核","发货"};
//        String[] headers3 = { "-","-","合计：", "合计：", "123","单价--","金额123.78","--","123.654","541","-","87.32","-","98451.23",
//                "-","-","金额","-","金额","",""};
//        String[] d1 = { "2019-01-01","2019-11-28","1100757-16", "1100757 清漆2.1VOC高表现力清漆 1GAL", "60","526.21","123.78","单价--","金额t","数量","","金额123","","金额",
//                "-","-","金额","-","金额","",""};
//        List<Object[]> dataset = new ArrayList<>();
//        List<String[]> headList = new ArrayList<>();
//        headList.add(headers);
//        headList.add(headers1);
//        headList.add(headers2);
//        headList.add(headers3);
//        dataset.add(d1);
//        exportExcel("sheetDewey", headList, dataset, os, null,true);
    }

}
