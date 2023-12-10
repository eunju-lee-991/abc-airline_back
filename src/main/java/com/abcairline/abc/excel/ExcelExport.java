package com.abcairline.abc.excel;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

public class ExportExcel<T> {

    private static final int ROW_START_INDEX = 0;
    private static final int COLUMN_START_INDEX = 0;

    private SXSSFWorkbook wb;
    private SXSSFSheet sheet;


    public ExcelExport(List<T> data,  Class<T> type) {
        this.wb = new SXSSFWorkbook();
        renderExcel(data, type);
    }

    private void renderExcel(List<T> data, Class<T> objectType){
        sheet = wb.createSheet();
        createHeaders(sheet, ROW_START_INDEX, COLUMN_START_INDEX, objectType);

        if(data.isEmpty()) return;

        int rowIndex = ROW_START_INDEX + 1;

        for (T datum : data) {
            createBody(datum, rowIndex++, COLUMN_START_INDEX, objectType);
        }

    }
