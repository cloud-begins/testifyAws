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
public class HW1 {
	
	static private Double averageCPU=0.0;
    static private Double maxCPU=0.0;
    static public String insId1="i-06d86faac5c4a9b86";
    public static Double getAverageCPU() {
    	return averageCPU;
    }

    public static void setAverageCPU(Double averageCPU) {
    	HW1.averageCPU = averageCPU;
    }

    public static Double getMaxCPU() {
    	return maxCPU;
    }

    public static void setMaxCPU(Double maxCPU) {
    	HW1.maxCPU = maxCPU;
    }
	
public static void main(String args[]) {
	//SpringApplication.run(HW1.class, args);
	 
     try {
      
    	 Regions region = Regions.EU_WEST_1;
         AmazonCloudWatchClient cloudWatch = (AmazonCloudWatchClient) AmazonCloudWatchClient.builder()
                 .withRegion(region)
                 .build();
        
		
         // create request message 
         GetMetricStatisticsRequest statRequest1 = new GetMetricStatisticsRequest();
     
         // set up request message 
         statRequest1.setNamespace("AWS/EC2"); 
        
         statRequest1.setPeriod(86400); //period of data(1-day i have set)
        
         ArrayList<String> stats = new ArrayList<String>();
         //  Average, Maximum, Minimum 
         stats.add("Average");
         stats.add("Maximum");
        
         statRequest1.setStatistics(stats);
        
         /*  CPUUtilization, MemoryUtilization, DiskReadBytes, DiskWriteBytes  */
         statRequest1.setMetricName("CPUUtilization");
         
         
      
         // set time 
     
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         String startDate = "2022-04-25";
         Date date = null;
		try {
			date = sdf.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         calendar.add(Calendar.DATE, -1);
         Date endDate = calendar.getTime();

         System.out.println(endDate);
         System.out.println(date);
         statRequest1.withStartTime(endDate);
         statRequest1.withEndTime(date);
       
         /* specify an instance */
        
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
//                averageCPU = d.getAverage();
//                 maxCPU=d.getMaximum();
//                 System.out.println("average CPU utilization: " + averageCPU);
//                 System.out.println("Max CPU utilization : "+ maxCPU);
            	 setAverageCPU(d.getAverage());
            	 setMaxCPU(d.getMaximum());
                 System.out.println("average CPU utilization: " + getAverageCPU());
                 System.out.println("Max CPU utilization : "+ getMaxCPU());
                
                 
             }
            
         

     } catch (AmazonServiceException ase) {
         System.out.println("Caught Exception: " + ase.getMessage());
         System.out.println("Reponse Status Code: " + ase.getStatusCode());
         System.out.println("Error Code: " + ase.getErrorCode());
         System.out.println("Request ID: " + ase.getRequestId());
     }

}


}
