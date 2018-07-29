package pl.com.psipoznan.visitorsregistry.visitorsregistry.tools.excel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.Visitor;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.tools.LocalDateUtils;

public class ExcelGenerator {
	// https://www.callicoder.com/java-write-excel-file-apache-poi/
	private Workbook workbook;
	private CreationHelper creationHelper;
	private Sheet sheet;
	private Font headerFont;
	private CellStyle headerCellStyle;
	private Row headerRow;
	
	public ExcelGenerator() {
		
	}
	
	public byte[] generate(String[] cols, List<Visitor> visitors) throws IOException {
		
		workbook = new XSSFWorkbook();
		creationHelper = workbook.getCreationHelper();
		sheet = workbook.createSheet("Visitors");
		
		headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.GREEN.getIndex());
        
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        
        headerRow = sheet.createRow(0);
		//header
        for(int i = 0; i < cols.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(cols[i]);
            cell.setCellStyle(headerCellStyle);
        }
        
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        
        int rowNum = 1;
        
        for (Visitor v : visitors) {
        	Row row = sheet.createRow(rowNum++);
        	
        	row.createCell(0).setCellValue(v.getName());
        	row.createCell(1).setCellValue(v.getCompany());
        	row.createCell(2).setCellValue(v.getTicket());
        	Cell enter = row.createCell(3);
        	enter.setCellValue(LocalDateUtils.LocalDateToDate(v.getEnter()));
        	enter.setCellStyle(dateCellStyle);
        	Cell exit = row.createCell(4);
        	if (v.getExit() == null) {
        		exit.setCellValue(" ");
        	} else {
        		exit.setCellValue(LocalDateUtils.LocalDateToDate(v.getEnter()));
        		exit.setCellStyle(dateCellStyle);
        	}
        }
        
        // autosize
        for(int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
        
        return fileOut.toByteArray();
	}
}
