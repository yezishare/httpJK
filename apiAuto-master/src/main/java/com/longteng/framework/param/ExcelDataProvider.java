package com.longteng.framework.param;

import com.longteng.framework.util.DataUtil;
import com.longteng.framework.util.Log;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class ExcelDataProvider implements Iterator<Object[]> {
    public static Logger logger = Log.getInstance();
    private static DataUtil data = DataUtil.getInstance();
    Sheet sheet;
    int colFlag = 0;
    int rowFlag = 0;
    int usedRowFlag = 1;
    String[] colName = null;

    public ExcelDataProvider(String filePath) throws FileNotFoundException {
        try {
            Workbook hssfWorkbook = data.getHSSFWorkbook(filePath);
            sheet = hssfWorkbook.getSheetAt(0);
            Row hssfRow = sheet.getRow(0);
            colFlag = hssfRow.getPhysicalNumberOfCells();
            int rows = sheet.getLastRowNum();
            colName = new String[colFlag];
            //colFlag代表有多少列
            for (int i = 0; i < colFlag; i++) {
                colName[i] = sheet.getRow(0).getCell(i).toString();
            }
            for (int r = 1; r <= rows; r++) {
                try {
                    String v = sheet.getRow(r).getCell(0).toString();
                    if (v == null || v.equals("")) {
                        break;
                    }
                } catch (NullPointerException e) {
                    break;
                }
                this.rowFlag++;
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * 判断是否仍有元素可以迭代
     *
     * @return
     */
    @Override
    public boolean hasNext() {
        if (rowFlag > 0 && sheet.getRow(rowFlag).getCell(0).toString() != "" && usedRowFlag <= rowFlag)
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        else {
//            try {
//                intputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
            return false;
        }
    }

    /**
     * 返回迭代的下一个元素
     *
     * @return
     */
    @Override
    public Object[] next() {
        Object[] object = new Object[1];
        Map<String, String> map = new LinkedHashMap<String, String>();
        int c = 0;
        //colFlag 代表有多少列
        for (; c < colFlag; c++) {
            String cellValue = "";
            try {
                cellValue = sheet.getRow(this.usedRowFlag).getCell(c).toString();
            } catch (Exception e) {
                map.put(colName[c], cellValue);
                continue;
            }
            map.put(colName[c], cellValue);
        }
        this.usedRowFlag++;
        object[0] = map;
        return object;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 删除
     */
    @Override
    public void remove() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 获得TestData.xls的绝对路径
     *
     * @return
     */
    public String getPath() {
        File file = new File("Data");
        String path = file.getAbsolutePath();
        path = path + "\\" + "TestData.xls";
        return path;
    }
}

