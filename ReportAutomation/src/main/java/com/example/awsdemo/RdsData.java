package com.example.awsdemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;

public class RdsData {

	
	static private Double averageCPU=0.0;
    static private Double maxCPU=0.0;
    static public String rdsIns="rdsdbinstance-1";
    public static Double getAverageCPU() {
    	return averageCPU;
    }

    public static void setAverageCPU(Double averageCPU) {
    	RdsData.averageCPU = averageCPU;
    }

    public static Double getMaxCPU() {
    	return maxCPU;
    }

    public static void setMaxCPU(Double maxCPU) {
    	RdsData.maxCPU = maxCPU;
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
         statRequest1.setNamespace("AWS/RDS"); 
        
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
         String startDate = "2022-04-29";
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
         dimensions1.add(new Dimension().withName("DBInstanceIdentifier").withValue("rdsdbinstance-1"));
        
         statRequest1.setDimensions(dimensions1);
       
         System.out.println("Set up cloud watch for instance: " + rdsIns);

       
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
