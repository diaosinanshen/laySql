package com.tttiger.util;

import com.tttiger.excel.DefaultExcelHeaderBodyStyle;
import com.tttiger.excel.ExcelAnnotationException;
import com.tttiger.excel.ExcelHeaderBodyStyle;
import com.tttiger.excel.annotation.*;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2019/09/28 20:20
 */
public class ExcelUtil<T> {

    /**
     * 字符串是否为数字验证
     */
    private final Pattern pattern = Pattern.compile("^//d+(//.//d+)?$");

    /**
     * 保存普通导出属性
     */
    private Map<Field, Integer> fieldMap = new HashMap<>();

    /**
     * 保存复合导出属性
     */
    private Map<Field, Map<Field, Integer>> associateMap = new HashMap<>();

    /**
     * 保存导出标题,与列宽度
     */
    private Map<String, Integer> headers = new LinkedHashMap<>();

    /**
     * 保存需要收集的属性，对应的列
     */
    private List<Integer> collectFieldIndex = new ArrayList<>();


    /**
     * 保存标题头样式
     */
    private ExcelHeaderBodyStyle excelHeaderBodyStyle;

    /**
     * 是否包含数据收集列
     */
    private boolean hasCollect = false;

    /**
     * 设置开始写入数据行
     */
    private int startRow = 0;
    /**
     * 设置开始写入数据列
     */
    private int startColumn = 0;

    private int currentRow;
    private int currentColumn;
    /**
     * 设置布尔值对应字符
     */
    private String booleanTrue = "是";
    /**
     * 设置布尔值对应字符
     */
    private String booleanFalse = "否";

    /**
     * 时间转换格式
     */
    private String datePattern = "yyyy-MM-dd HH:mm:ss";

    private static String[] columnName = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * @param fileName 文件标题
     * @param data     集合数据
     * @param response Http响应，内容直接写入response响应流
     */
    public void exportExcel(String fileName, List<T> data, HttpServletResponse response) throws IllegalAccessException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        exportExcel(fileName, data, response.getOutputStream());
    }


    /**
     * 将数据写入一个workBook
     * @param title sheet名称
     * @param data 写入数据
     * @return 写入数据后的 workbook
     */
    public XSSFWorkbook exportExcel(String title, List<T> data) throws IllegalAccessException {
        if (data == null || data.isEmpty()) {
            return null;
        }
        // 初始化
        init(data);
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(title);
        // 设置表格标题行
        setHeaders(workbook, sheet);
        // 设置表体
        setBody(workbook, sheet, data);
        // 设置数据收集行
        setCollect(workbook, sheet, data.size());
        return workbook;
    }

    /**
     * @param title  文件标题
     * @param data   集合数据
     * @param stream 导出流
     */
    public void exportExcel(String title, List<T> data, OutputStream stream) throws IllegalAccessException, IOException {
        if (data == null || data.isEmpty()) {
            return;
        }
        XSSFWorkbook workbook = exportExcel(title, data);
        workbook.write(stream);
    }

    /**
     * 设置标题头
     */
    private void setHeaders(XSSFWorkbook workbook, XSSFSheet sheet) {
        // 设置标题行
        XSSFFont headerFont = workbook.createFont();
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        this.excelHeaderBodyStyle.headerStyle(headerStyle, headerFont);
        headerStyle.setFont(headerFont);
        XSSFRow row = sheet.createRow(currentRow++);
        this.excelHeaderBodyStyle.headerStyle(headerStyle, headerFont);
        headerStyle.setFont(headerFont);
        this.currentColumn = this.startColumn;
        for (Map.Entry<String, Integer> entry : headers.entrySet()) {
            XSSFCell cell = row.createCell(currentColumn);
            cell.setCellValue(entry.getKey());
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(currentColumn, entry.getValue());
            currentColumn++;
        }
    }

    /**
     * 设置表主体
     *
     * @param data 指定泛型集合数据
     */
    private void setBody(XSSFWorkbook workbook, XSSFSheet sheet, List<T> data) throws IllegalAccessException {
        XSSFCellStyle bodyStyle = workbook.createCellStyle();
        XSSFFont bodyFont = workbook.createFont();
        this.excelHeaderBodyStyle.bodyStyle(bodyStyle, bodyFont);
        bodyStyle.setFont(bodyFont);
        // 普通属性映射
        Set<Map.Entry<Field, Integer>> entries = fieldMap.entrySet();
        // 设置复合属性,关联属性
        Set<Map.Entry<Field, Map<Field, Integer>>> tempEntry = associateMap.entrySet();
        for (T tempObj : data) {
            XSSFRow row = sheet.createRow(currentRow++);
            // 设置普通属性
            for (Map.Entry<Field, Integer> entry : entries) {
                Object obj = entry.getKey().get(tempObj);
                XSSFCell cell = row.createCell(entry.getValue());
                cell.setCellStyle(bodyStyle);
                setCellValue(entry.getKey(), cell, obj);
            }
            // 每个关联属性
            for (Map.Entry<Field, Map<Field, Integer>> mapEntry : tempEntry) {
                // 复合属性值
                Object obj = mapEntry.getKey().get(tempObj);
                // 复合属性所需导出字段
                Set<Map.Entry<Field, Integer>> fieldEntry = mapEntry.getValue().entrySet();
                for (Map.Entry<Field, Integer> entry1 : fieldEntry) {
                    entry1.getKey().setAccessible(true);
                    Object obj2 = entry1.getKey().get(obj);
                    XSSFCell cell = row.createCell(entry1.getValue());
                    cell.setCellStyle(bodyStyle);
                    setCellValue(entry1.getKey(), cell, obj2);
                }
            }
        }

    }

    /**
     * 设置数据收集行，sum函数汇总
     */
    private void setCollect(XSSFWorkbook workbook, XSSFSheet sheet, Integer dataSize) {
        if (hasCollect) {
            XSSFRow collectRow = sheet.createRow(currentRow);
            XSSFCellStyle collectStyle = workbook.createCellStyle();
            XSSFFont collectFont = workbook.createFont();
            this.excelHeaderBodyStyle.collectStyle(collectStyle,collectFont);
            collectStyle.setFont(collectFont);
            for (Integer index : collectFieldIndex) {
                XSSFCell cell = collectRow.createCell(index);
                cell.setCellStyle(collectStyle);
                String coordinate = getCellCoordinate(currentRow - 1, index);
                String coordinate2 = getCellCoordinate(currentRow - dataSize, index);
                cell.setCellFormula("SUM(" + coordinate2 + ":" + coordinate + ")");
            }
        }
        currentRow++;
    }

    /**
     * 根据名字查找属性field
     * @param clazz 类
     * @param name  属性名
     * @return 找到的属性
     */
    private Field findField(Class<?> clazz, String name) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    /**
     * 获取单元格对应的坐标如 0,0 返回 A1
     *
     * @param row    行数
     * @param column 列数
     * @return 坐标字符串
     */
    public String getCellCoordinate(int row, int column) {
        StringBuilder name = new StringBuilder();
        row++;
        if (column < columnName.length) {
            name.append(columnName[column]);
        } else {
            for (int i = 0; i < column / columnName.length; i++) {
                name.append(columnName[0]);
            }
            name.append(columnName[column % columnName.length]);
        }
        name.append(row);
        return name.toString();
    }


    /**
     * 为单元格设置值
     *
     * @param field 属性类型
     * @param cell  单元格
     * @param value 属性值
     * @throws ExcelAnnotationException 注解使用不正确
     */
    private void setCellValue(Field field, XSSFCell cell, Object value) {
        if (field.getAnnotation(ExcelField.class).status()) {
            if (field.isAnnotationPresent(ExcelFieldStatus.class)) {
                ExcelFieldStatus annotation = field.getAnnotation(ExcelFieldStatus.class);
                setStatusField(annotation, cell, value);
            } else {
                throw new ExcelAnnotationException("Excel注解使用错误：未指定@ExcelFieldStatus");
            }
        } else {
            setCellValue(cell, value);
        }
    }

    /**
     * 为状态属性设置值
     *
     * @param status 属性状态标识
     * @param cell   单元格
     * @param obj    属性值
     */
    private void setStatusField(ExcelFieldStatus status, XSSFCell cell, Object obj) {
        Status[] value = status.value();
        for (Status s : value) {
            int var1 = s.value();
            // 状态是数值类型,全部当做Integer来比较
            if (obj instanceof Byte || obj instanceof Short || obj instanceof Integer || obj instanceof Long) {
                if (Integer.valueOf(obj.toString()).equals(var1)) {
                    cell.setCellValue(s.name());
                    return;
                }
            }
        }
        setCellValue(cell, obj);
    }

    /**
     * 对值类型进行适配赋值
     *
     * @param cell  单元格
     * @param value 原类型
     */
    private void setCellValue(XSSFCell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        String textValue = null;
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            textValue = this.booleanTrue;
            if (!(Boolean) value) {
                textValue = this.booleanFalse;
            }
        } else if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(this.datePattern);
            textValue = sdf.format((Date) value);
        } else {
            textValue = value.toString();
        }

        if (textValue != null) {
            // 其它数据类型都当作字符串简单处理
            Matcher matcher = pattern.matcher(textValue);
            if (matcher.matches()) {
                // 是数字当作double处理
                cell.setCellValue(Double.parseDouble(textValue));
            } else {
                XSSFRichTextString richString = new XSSFRichTextString(textValue);
                cell.setCellValue(richString);
            }
        }
    }


    /**
     * 初始化
     *
     * @param entity 导出实体集合
     */
    private void init(List<T> entity) {
        Class<?> clazz = entity.get(0).getClass();
        this.currentColumn = this.startColumn;
        this.currentRow = this.startRow;
        // 获取表格默认表头，表体样式
        if (clazz.isAnnotationPresent(ExcelStyle.class)) {
            Class<? extends ExcelHeaderBodyStyle> styleClazz = clazz.getAnnotation(ExcelStyle.class).excelStyle();
            try {
                Constructor<? extends ExcelHeaderBodyStyle> constructor = styleClazz.getConstructor();
                this.excelHeaderBodyStyle = constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ExcelAnnotationException("指定样式类无法创建:" + e.getMessage());
            }
        } else {
            this.excelHeaderBodyStyle = new DefaultExcelHeaderBodyStyle();
        }
        // 获取需要导出属性排序
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> commonField = new ArrayList<>();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ExcelField.class) || field.isAnnotationPresent(ExcelAssociate.class)) {
                field.setAccessible(true);
                commonField.add(field);
            }
        }
        // 根据Field指定顺序排序
        commonField.sort((x, y) -> {
            int var1 = x.isAnnotationPresent(ExcelField.class) ? x.getAnnotation(ExcelField.class).sort() :
                    x.getAnnotation(ExcelAssociate.class).sort();

            int var2 = y.isAnnotationPresent(ExcelField.class) ? y.getAnnotation(ExcelField.class).sort() :
                    y.getAnnotation(ExcelAssociate.class).sort();
            return var1 - var2;
        });
        // 初始化标题头，和对应映射关系
        for (Field field : commonField) {
            if (field.isAnnotationPresent(ExcelField.class)) {
                // 添加到标题头
                ExcelField ex = field.getAnnotation(ExcelField.class);
                headers.put(ex.value(), ex.width());
                // 普通属性放入
                fieldMap.put(field, currentColumn);
                if (ex.collect()) {
                    this.hasCollect = true;
                    collectFieldIndex.add(currentColumn);
                }
                currentColumn++;
            } else if (field.isAnnotationPresent(ExcelAssociate.class)) {
                // 复合属性
                String[] names = field.getAnnotation(ExcelAssociate.class).value();
                Map<Field, Integer> tempMap = new HashMap<>();
                for (String str : names) {
                    // 找到复合属性需要注入的值
                    Field aField = findField(field.getType(), str);
                    if (aField == null || !aField.isAnnotationPresent(ExcelField.class)) {
                        throw new ExcelAnnotationException("复合属性未找到");
                    }
                    ExcelField af = aField.getAnnotation(ExcelField.class);
                    headers.put(af.value(), af.width());
                    // 放入临时map
                    tempMap.put(aField, currentColumn);
                    if (af.collect()) {
                        this.hasCollect = true;
                        collectFieldIndex.add(currentColumn);
                    }
                    currentColumn++;
                }
                // 复合属性与复合属性字段
                associateMap.put(field, tempMap);
            }
        }

    }


    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public String getBooleanTrue() {
        return booleanTrue;
    }

    public void setBooleanTrue(String booleanTrue) {
        this.booleanTrue = booleanTrue;
    }

    public String getBooleanFalse() {
        return booleanFalse;
    }

    public void setBooleanFalse(String booleanFalse) {
        this.booleanFalse = booleanFalse;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }
}
