package com.example.awsdemo;



import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MemoryUtil {
	
	static Double averageMemory = 0.0;
    static Double maxMemory=0.0;
public static Double getAverageMemory() {
		return averageMemory;
	}
	public static void setAverageMemory(Double averageMemory) {
		MemoryUtil.averageMemory = averageMemory;
	}
	public static Double getMaxMemory() {
		return maxMemory;
	}
	public static void setMaxMemory(Double maxMemory) {
		MemoryUtil.maxMemory = maxMemory;
	}
public static void memUtil() {
	
	 
     try {
      
    	 Regions region = Regions.EU_WEST_1;
         AmazonCloudWatchClient cloudWatch = (AmazonCloudWatchClient) AmazonCloudWatchClient.builder()
                 .withRegion(region)
                 .build();
        
		
         /* create request message1 */
         GetMetricStatisticsRequest statRequest1 = new GetMetricStatisticsRequest();
     
         /* set up request message */
         statRequest1.setNamespace("CWAgent"); 
        
         statRequest1.setPeriod(86400); //period of data
        
         ArrayList<String> stats = new ArrayList<String>();
         /*  Average, Maximum, Minimum, Sum */
         stats.add("Average");
         stats.add("Maximum");
        
         statRequest1.setStatistics(stats);
        
         /*  CPUUtilization, MemoryUtilization, DiskReadBytes, DiskWriteBytes  */
         statRequest1.setMetricName("mem_used_percent");
         
         /* set time */
//       
         Date date1 = new Date();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         //String startDate = "2022-05-03";
         String date = sdf.format(date1);
         Date endDate = null;
		try {
			endDate = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(endDate);
         calendar.add(Calendar.DATE, -1);
         Date startDate = calendar.getTime();

         System.out.println(endDate);
         System.out.println(date);
         statRequest1.withStartTime(startDate);
         statRequest1.withEndTime(endDate);
       
         /* specify an instance */
         String insId1="i-06d86faac5c4a9b86";
         ArrayList<Dimension> dimensions1 = new ArrayList<Dimension>();
         dimensions1.add(new Dimension().withName("InstanceId").withValue("i-06d86faac5c4a9b86"));
        
         statRequest1.setDimensions(dimensions1);
       
         System.out.println("Set up cloud watch for instance: " + insId1);

       
             GetMetricStatisticsResult statResult1 = cloudWatch.getMetricStatistics(statRequest1);
            
             /* display */
             System.out.println("Instance 1: " + statResult1.toString());
             System.out.println(statResult1.getDatapoints());
             List<Datapoint> dataList = statResult1.getDatapoints();
             
             
          
            
             for (Datapoint d : dataList) {
//            	 averageMemory = d.getAverage();
//            	 maxMemory=d.getMaximum();
//                 System.out.println("average CPU utilization: " + averageMemory);
//                 System.out.println("Max CPU utilization : "+ maxMemory);
            	 setAverageMemory(d.getAverage());
            	 setMaxMemory(d.getMaximum());
               System.out.println("average CPU utilization: " + getAverageMemory());
               System.out.println("Max CPU utilization : "+ getMaxMemory());
            	 
                 
             }
            
         

     } catch (AmazonServiceException ase) {
         System.out.println("Caught Exception: " + ase.getMessage());
         System.out.println("Reponse Status Code: " + ase.getStatusCode());
         System.out.println("Error Code: " + ase.getErrorCode());
         System.out.println("Request ID: " + ase.getRequestId());
     }

}
}
