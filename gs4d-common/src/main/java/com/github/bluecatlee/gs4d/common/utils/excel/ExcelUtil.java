package com.github.bluecatlee.gs4d.common.utils.excel;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

public class ExcelUtil {

    public static final String HEADERINFO = "headInfo";
    public static final String DATAINFON = "dataInfo";

    public ExcelUtil() {
    }

    /**
     * 获取工作簿WorkBook对象  todo 建议使用SXSSFWorkbook
     * @param filename
     * @return
     * @throws IOException
     */
    public static Workbook getWorkBook(String filename) throws IOException {
        Workbook workbook = null;
        if (null != filename) {
            String fileType = filename.substring(filename.lastIndexOf("."), filename.length());
            FileInputStream fileStream = new FileInputStream(new File(filename));
            if (".xls".equals(fileType.trim().toLowerCase())) {
                try {
                    workbook = new XSSFWorkbook(filename);
                } catch (Exception e) {
                    workbook = new HSSFWorkbook(fileStream);
                }
            } else if (".xlsx".equals(fileType.trim().toLowerCase())) {
                workbook = new XSSFWorkbook(fileStream);
            }
        }

        return (Workbook)workbook;
    }

    /**
     * 获取工作簿WorkBook对象
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkBook(MultipartFile file) throws IOException {
        Workbook workbook = null;
        String filename = file.getOriginalFilename();
        if (null != filename) {
            String fileType = filename.substring(filename.lastIndexOf("."), filename.length());
            InputStream fileStream = file.getInputStream();
            if (".xls".equals(fileType.trim().toLowerCase())) {
                try {
                    workbook = new XSSFWorkbook(fileStream);
                } catch (Exception e) {
                    workbook = new HSSFWorkbook(fileStream);
                }
            } else if (".xlsx".equals(fileType.trim().toLowerCase())) {
                workbook = new XSSFWorkbook(fileStream);
            }
        }

        return (Workbook)workbook;
    }

    /**
     * 输出工作簿中所有工作表的内容
     *      可以改造成返回List<T>
     * @param fileAbsolutePath
     * @param headInfo
     * @param <T>
     * @return
     * @throws IOException
     */
    @Deprecated
    public static <T> List<T> readExcel(String fileAbsolutePath, String headInfo) throws IOException {
        String[] headList = headInfo.split(",");
        Workbook workbook = getWorkBook(fileAbsolutePath);
        System.out.println("总表页数为：" + workbook.getNumberOfSheets());

        for(int num = 0; num < workbook.getNumberOfSheets(); ++num) {
            Sheet sheet = workbook.getSheetAt(num);
            int rownum = sheet.getLastRowNum();

            for(int i = 0; i <= rownum; ++i) {
                Row row = sheet.getRow(i);

                for(int j = row.getFirstCellNum(); j < row.getLastCellNum(); ++j) {
                    Cell celldata = row.getCell(j);
                    System.out.print(celldata + "\t");
                }

                System.out.println();
            }
        }

        return null;
    }

    /**
     * 将数据写到excel并响应返回
     * @param displayField
     * @param titleName
     * @param data
     * @param request
     * @param response
     * @param <T>
     */
    public static <T> void writeToExcel(String displayField, String titleName, List<T> data, HttpServletRequest request, HttpServletResponse response) {
        try {
            Class obj = data.get(0).getClass();
            Field[] fs = obj.getDeclaredFields();
            Workbook wb = new XSSFWorkbook();
            Map<String, Object> map = new HashMap();
            map.put("HEADERINFO", displayField);
            List<TreeMap<String, Object>> dataList = new ArrayList();

            for(int i = 0; i < data.size(); ++i) {
                TreeMap<String, Object> treeMap = new TreeMap();

                for(int j = 0; j < fs.length; ++j) {
                    treeMap.put(fs[j].getName(), ClassUtil.getFieldValue(data.get(i), fs[j].getName(), data.get(i).getClass()));
                }

                dataList.add(treeMap);
            }

            map.put("DATAINFON", dataList);
            writeExcel(request, response, titleName, map, wb);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建并响应excel文件
     *      耦合性高
     * @param request
     * @param response
     * @param fileName
     * @param map
     * @param wb
     * @throws IOException
     */
    @Deprecated
    public static void writeExcel(HttpServletRequest request, HttpServletResponse response, String fileName, Map<String, Object> map, Workbook wb) throws IOException {
        if (null != map && null != fileName) {
            String displayField = (String)map.get("HEADERINFO");                      // 标题信息从headInfo中获取，逗号分隔
            List<TreeMap<String, Object>> dataList = (List)map.get("DATAINFON");     // 数据信息从dataInfo中获取
            CellStyle style = getCellStyle(wb);
            Sheet sheet = wb.createSheet();                                     // 创建工作表
            List<String> headList = new ArrayList();
            String[] headField = displayField.split(",");
            Row row = sheet.createRow(0);                                    // 创建第一行
            Cell titleCell = row.createCell(0);                              // 创建单元格 第一个单元格作为标题
            titleCell.setCellValue(fileName);
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.LEFT);                 // 水平靠左
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);         // 垂直居中
            titleStyle.setFillBackgroundColor(IndexedColors.BLACK.index);      // 填充颜色 黑色
            titleStyle.setLocked(true);                                        // 单元格不可编辑
            titleCell.setCellStyle(titleStyle);

            Row headRow = sheet.createRow(1);
            int i;
            for(i = 0; i < headField.length; ++i) {                            // 创建表头 第二行作为表头
                String[] desc = headField[i].split(":");
                headList.add(desc[0]);
                Cell headCell = headRow.createCell(i);
                headCell.setCellType(CellType.STRING);
                headCell.setCellStyle(style);
                headCell.setCellValue(desc[1]);
            }

            for(i = 0; i < dataList.size(); ++i) {                             // 循环填充数据
                Row rowdata = sheet.createRow(i + 2);
                TreeMap<String, Object> mapdata = (TreeMap)dataList.get(i);

                for(int j = 0; j < headList.size(); ++j) {
                    String strdata = String.valueOf(mapdata.get(headList.get(j)));
                    Cell celldata = rowdata.createCell(j);
                    celldata.setCellType(CellType.STRING);
                    celldata.setCellValue(strdata);
                }
            }

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headList.size() - 1));  // 作为标题的单元格合并
            response.setContentType("application/vnd.ms-excel");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        }

    }

    /**
     * 创建标题和表头
     * @param wb
     * @param displayField
     * @param header
     */
    @Deprecated
    public static void writeExcelHeader(Workbook wb, String displayField, String header) {
        String[] headField = displayField.split(",");
        CellStyle style = getCellStyle(wb);
        int firstRow = 0;
        Sheet sheet = wb.createSheet();
        Row headRow;
        if (header != null && header != "") {
            headRow = sheet.createRow(0);
            Cell titleCell = headRow.createCell(0);
            titleCell.setCellValue(header);
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.LEFT);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setFillBackgroundColor(IndexedColors.BLACK.index);
            titleStyle.setLocked(true);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headField.length - 1));
            firstRow = 1;
        }

        headRow = sheet.createRow(firstRow);

        for(int i = 0; i < headField.length; ++i) {
            String[] desc = headField[i].split(":");
            Cell headCell = headRow.createCell(i);
            headCell.setCellType(CellType.STRING);
            headCell.setCellStyle(style);
            headCell.setCellValue(desc[1]);
        }

    }

    /**
     * 填充数据
     * @param wb
     * @param displayField
     * @param data
     * @param <T>
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    @Deprecated
    public static <T> void writeExcelContent(Workbook wb, String displayField, List<T> data) throws NoSuchMethodException, IllegalAccessException {
        if (data != null && data.size() >= 1) {
            String[] headField = displayField.split(",");
            Sheet sheet = wb.getSheetAt(0);
            int rowCount = sheet.getLastRowNum() + 1;
            String strData = "";

            for(int i = 0; i < data.size(); ++i) {
                Row rowdata = sheet.createRow(i + rowCount);

                for(int j = 0; j < headField.length; ++j) {
                    Object object = ClassUtil.getFieldValue(data.get(i), headField[j].split(":")[0],data.get(i).getClass());
                    strData = String.valueOf(object);
                    Cell celldata = rowdata.createCell(j);
                    if (object instanceof Long && strData.length() > 11) {
                        celldata.setCellType(CellType.STRING);
                        celldata.setCellValue(strData);
                    } else if (object instanceof Number) {
                        celldata.setCellType(CellType.NUMERIC);
                        if (object != null) {
                            celldata.setCellValue(Double.valueOf(strData));
                        }
                    } else {
                        celldata.setCellType(CellType.STRING);
                        celldata.setCellValue(strData);
                    }
                }
            }

        }
    }

    /**
     * 填充数据
     * @param wb
     * @param displayField
     * @param data
     * @param json
     * @param <T>
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    @Deprecated
    public static <T> void writeExcelContent(Workbook wb, String displayField, List<T> data, String json) throws NoSuchMethodException, IllegalAccessException {
        if (data != null && data.size() >= 1) {
            Map<String, Double> maps = new HashMap();
            String[] cloums = null;
            String[] headField;
            int rowCount;
            String strData;
            if (json != null) {
                cloums = json.split(",");
                headField = cloums;
                int len = cloums.length;

                for(rowCount = 0; rowCount < len; ++rowCount) {
                    strData = headField[rowCount];
                    maps.put(strData, 0.0D);
                }
            }

            headField = displayField.split(",");
            Sheet sheet = wb.getSheetAt(0);
            rowCount = sheet.getLastRowNum() + 1;
            strData = "";

            for(int i = 0; i < data.size(); ++i) {
                Row rowdata = sheet.createRow(i + rowCount);

                Cell celldata;
                for(int j = 0; j < headField.length; ++j) {
                    Object object = ClassUtil.getFieldValue(data.get(i), headField[j].split(":")[0],data.get(i).getClass());
                    strData = String.valueOf(object);
                    celldata = rowdata.createCell(j);
                    if (object instanceof Long && strData.length() > 11) {
                        celldata.setCellType(CellType.STRING);
                        celldata.setCellValue(strData);
                    } else if (object instanceof Number) {
                        celldata.setCellType(CellType.NUMERIC);
                        if (object != null) {
                            celldata.setCellValue(Double.valueOf(strData));
                        }

                        if (maps.get(headField[j].split(":")[0]) != null) {
                            maps.put(headField[j].split(":")[0], Double.valueOf((Double)maps.get(headField[j].split(":")[0])) + Double.valueOf(strData));
                        }
                    } else {
                        celldata.setCellType(CellType.STRING);
                        celldata.setCellValue(strData);
                    }
                }

                if (i == data.size() - 1) {
                    if (maps == null) {
                        return;
                    }

                    Row rowdata1 = sheet.createRow(i + 1 + rowCount);

                    for(int j = 0; j < headField.length; ++j) {
                        celldata = rowdata1.createCell(j);
                        if (maps.get(headField[j].split(":")[0]) != null && !"".equals(maps.get(headField[j].split(":")[0]))) {
                            celldata.setCellType(CellType.STRING);
                            celldata.setCellValue((new DecimalFormat("#.0000")).format(Double.valueOf(((Double)maps.get(headField[j].split(":")[0])).toString())));
                        } else {
                            celldata.setCellType(CellType.STRING);
                            celldata.setCellValue("");
                        }
                    }
                }
            }

        }
    }

    /**
     * 响应xls
     * @param wb
     * @param fileName
     * @param response
     * @throws Exception
     */
    public static void writeExcelToResponse(Workbook wb, String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    public static void writeCSVHeader(OutputStreamWriter out, String displayField, String header) throws Exception {
        out.write(new String(new byte[]{-17, -69, -65}));
        String[] headField = displayField.split(",");
        int j;
        if (header != null && !"".equals(header)) {
            j = headField.length / 2;

            for(int i = 0; i < j; ++i) {
                out.write(",");
            }

            out.write("\"");
            out.write(header);
            out.write("\"");
            out.write("\n");
        }

        for(int i = 0; i < headField.length; ++i) {
            out.write("\"");
            out.write(headField[i].split(":")[1]);
            out.write("\"");
            out.write(",");
        }

        out.write("\n");
    }

    public static <T> void writeCSVContent(OutputStreamWriter out, String displayField, List<T> data) throws Exception {
        if (data != null && data.size() >= 1) {
            String[] headField = displayField.split(",");

            for(int i = 0; i < data.size(); ++i) {
                for(int j = 0; j < headField.length; ++j) {
                    out.write("\"");
                    out.write(String.valueOf(ClassUtil.getFieldValue(data.get(i), headField[j].split(":")[0],data.get(i).getClass())));
                    out.write("\"");
                    out.write(",");
                }

                out.write("\n");
            }

            out.flush();
        }
    }

    public static void writeCSVToResponse(File file, String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("text/csv;charset=UTF-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".csv");
        OutputStream os = response.getOutputStream();
        FileInputStream in = null;

        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[10240];

            while(true) {
                int len;
                if ((len = in.read(buf)) == -1) {
                    os.write(65279);
                    os.flush();
                    break;
                }

                os.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            in.close();
            os.close();
        }

        if (file.exists()) {
            file.delete();
        }

    }

    public static void writeCSVToFile(StringBuffer content, String fileName, HttpServletRequest request) throws Exception {
        String path = request.getSession().getServletContext().getRealPath("");
        fileName = URLEncoder.encode(path + "/" + fileName, "UTF-8");
        FileWriter os = new FileWriter(fileName);
        os.write(new String(new byte[]{-17, -69, -65}));
        os.write(content.toString());
        os.write(65279);
        os.flush();
        os.close();
    }

    public static void writeExcel(String pathname, Map<String, Object> map, Workbook wb) throws IOException {
        if (null != map && null != pathname) {
            List<Object> headList = (List)map.get("HEADERINFO");
            List<TreeMap<String, Object>> dataList = (List)map.get("DATAINFON");
            CellStyle style = getCellStyle(wb);
            Sheet sheet = wb.createSheet();
            Row row = sheet.createRow(0);

            int i;
            for(i = 0; i < headList.size(); ++i) {
                Cell headCell = row.createCell(i);
                headCell.setCellType(CellType.STRING);
                headCell.setCellStyle(style);
                headCell.setCellValue(String.valueOf(headList.get(i)));
            }

            for(i = 0; i < dataList.size(); ++i) {
                Row rowdata = sheet.createRow(i + 1);
                TreeMap<String, Object> mapdata = (TreeMap)dataList.get(i);
                Iterator it = mapdata.keySet().iterator();

                for(int j = 0; it.hasNext(); ++j) {
                    String strdata = String.valueOf(mapdata.get(it.next()));
                    Cell celldata = rowdata.createCell(j);
                    celldata.setCellType(CellType.STRING);
                    celldata.setCellValue(strdata);
                }
            }

            File file = new File(pathname);
            OutputStream os = new FileOutputStream(file);
            os.flush();
            wb.write(os);
            os.close();
        }

    }

    /**
     * 创建单元格样式
     * @param wb
     * @return
     */
    public static CellStyle getCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        font.setBold(true);
        style.setFillForegroundColor((short)50);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font);
        return style;
    }

    /**
     * 列字母转列数字
     * @param column
     * @return
     */
    public static int excelColStrToNum(String column) {
        int result = 0;
        int length = column.length();

        for(int i = 0; i < length; ++i) {
            char ch = column.charAt(length - i - 1);
            int num = ch - 65 + 1;
            num = (int)((double)num * Math.pow(26.0D, (double)i));
            result += num;
        }

        return result - 1;
    }

    /**
     * 列数字转列字母
     * @param columnIndex
     * @return
     */
    public static String excelColIndexToStr(int columnIndex) {
        if (columnIndex < 0) {
            return null;
        } else {
            ++columnIndex;
            String columnStr = "";
            --columnIndex;

            do {
                if (columnStr.length() > 0) {
                    --columnIndex;
                }

                columnStr = (char)(columnIndex % 26 + 65) + columnStr;
                columnIndex = (columnIndex - columnIndex % 26) / 26;
            } while(columnIndex > 0);

            return columnStr;
        }
    }
}
