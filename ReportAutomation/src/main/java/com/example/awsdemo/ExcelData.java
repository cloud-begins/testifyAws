package com.example.awsdemo;
import com.example.awsdemo.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class ExcelData {

	  public static void main(String[] args) throws Exception
	    {
		  
		  //SpringApplication.run(HW1.class, args);
		  //SpringApplication.run(ExcelData.class, args);
		  HW1.cpuUtil();
		  MemoryUtil.memUtil();
		  
	        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
				// spreadsheet object
				XSSFSheet spreadsheet = workbook.createSheet(" Data ");
  
				// creating a row object
				XSSFRow row;
				// This data needs to be written (Object[])
				Map<String, Object[]> studentData = new TreeMap<String, Object[]>();
  
				studentData.put("1",new Object[] { "Server", "CPU_util(max)", "CPU_util(avg)","Memory_usage(max)","Memory_usage(avg)","Storage_usage(max)","Storage_usage(avg)" });
  
				studentData.put("2", new Object[] {HW1.insId1, Double.toString(HW1.getMaxCPU()), Double.toString(HW1.getAverageCPU()),Double.toString(MemoryUtil.getMaxMemory()),Double.toString(MemoryUtil.getAverageMemory()),"0","0" });
  

  
      
  
				Set<String> keyid = studentData.keySet();
  
				int rowid = 0;
  
				// writing the data into the sheets...
  
				for (String key : keyid) {
  
				    row = spreadsheet.createRow(rowid++);
				    Object[] objectArr = studentData.get(key);
				    int cellid = 0;
  
				    for (Object obj : objectArr) {
				        Cell cell = row.createCell(cellid++);
				        cell.setCellValue((String)obj);
				    }
				}
  
				
				// writing the workbook into the file...
				FileOutputStream out = new FileOutputStream(new File("Utilsheet.xlsx"));
  
				workbook.write(out);
				out.close();
			}
	    }
}
