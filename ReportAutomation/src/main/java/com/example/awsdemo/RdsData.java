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
    static private Double averageFreeStorageSpace;
    static private Double maxFreeStorageSpace;
    static private Double averageFreeableMemory;
	static private Double maxFreeableMemory;
	static private Double averageCPU=0.0;
    static private Double maxCPU=0.0;
    static public String rdsIns="rdsdbinstance-1";
    
	public static Double getAverageFreeStorageSpace() {
		return averageFreeStorageSpace;
	}

	public static void setAverageFreeStorageSpace(Double averageFreeStorageSpace) {
		RdsData.averageFreeStorageSpace = averageFreeStorageSpace;
	}

	public static Double getMaxFreeStorageSpace() {
		return maxFreeStorageSpace;
	}

	public static void setMaxFreeStorageSpace(Double maxFreeStorageSpace) {
		RdsData.maxFreeStorageSpace = maxFreeStorageSpace;
	}

	public static Double getAverageFreeableMemory() {
		return averageFreeableMemory;
	}

	public static void setAverageFreeableMemory(Double averageFreeableMemory) {
		RdsData.averageFreeableMemory = averageFreeableMemory;
	}

	public static Double getMaxFreeableMemory() {
		return maxFreeableMemory;
	}

	public static void setMaxFreeableMemory(Double maxFreeableMemory) {
		RdsData.maxFreeableMemory = maxFreeableMemory;
	}

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
         GetMetricStatisticsRequest statRequest2 = new GetMetricStatisticsRequest();
         GetMetricStatisticsRequest statRequest3 = new GetMetricStatisticsRequest();
     
         // set up request message 
         statRequest1.setNamespace("AWS/RDS");
         statRequest2.setNamespace("AWS/RDS");
         statRequest3.setNamespace("AWS/RDS");
        
         statRequest1.setPeriod(86400); //period of data(1-day i have set)
         statRequest2.setPeriod(86400);
         statRequest3.setPeriod(86400);
        
         ArrayList<String> stats = new ArrayList<String>();
         //  Average, Maximum, Minimum 
         stats.add("Average");
         stats.add("Maximum");
        
         statRequest1.setStatistics(stats);
         statRequest2.setStatistics(stats);
         statRequest3.setStatistics(stats);
        
         /*  CPUUtilization, MemoryUtilization, DiskReadBytes, DiskWriteBytes  */
         statRequest1.setMetricName("CPUUtilization");
         statRequest2.setMetricName("FreeableMemory");
         statRequest3.setMetricName("FreeStorageSpace");
         
         
      
         // set time 
     
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         String startDate = "2022-05-01";
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
         
         statRequest2.withStartTime(endDate);
         statRequest2.withEndTime(date);
         
         statRequest3.withStartTime(endDate);
         statRequest3.withEndTime(date);
       
         /* specify an instance */
        
         ArrayList<Dimension> dimensions1 = new ArrayList<Dimension>();
         dimensions1.add(new Dimension().withName("DBInstanceIdentifier").withValue("rdsdbinstance-1"));
        
         ArrayList<Dimension> dimensions2 = new ArrayList<Dimension>();
         dimensions2.add(new Dimension().withName("DBInstanceIdentifier").withValue("rdsdbinstance-1"));
        
         ArrayList<Dimension> dimensions3 = new ArrayList<Dimension>();
         dimensions3.add(new Dimension().withName("DBInstanceIdentifier").withValue("rdsdbinstance-1"));
         
         statRequest1.setDimensions(dimensions1);
         statRequest2.setDimensions(dimensions2);
         statRequest3.setDimensions(dimensions3);
       
         System.out.println("Set up cloud watch for instance: " + rdsIns);

       
             GetMetricStatisticsResult statResult1 = cloudWatch.getMetricStatistics(statRequest1);
             GetMetricStatisticsResult statResult2 = cloudWatch.getMetricStatistics(statRequest2);
             GetMetricStatisticsResult statResult3 = cloudWatch.getMetricStatistics(statRequest3);
             /* display */
             System.out.println("RDSInstance 1: " + statResult1.toString());
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
             dataList = statResult2.getDatapoints();
             for (Datapoint d : dataList) {

           	 setAverageFreeableMemory(d.getAverage());
           	 setMaxFreeableMemory(d.getMaximum());
                System.out.println("average Freeable memory: " + getAverageFreeableMemory());
                System.out.println("Max Freeable memory : "+ getMaxFreeableMemory());
               
                
            }
             
             dataList = statResult3.getDatapoints();
             for (Datapoint d : dataList) {

           	 setAverageFreeStorageSpace(d.getAverage());
           	 setMaxFreeStorageSpace(d.getMaximum());
                System.out.println("average FreeStorageSpace : " + getAverageFreeStorageSpace());
                System.out.println("Max FreeStorageSpace : "+ getMaxFreeStorageSpace());
               
                
            }
            
         

     } catch (AmazonServiceException ase) {
         System.out.println("Caught Exception: " + ase.getMessage());
         System.out.println("Reponse Status Code: " + ase.getStatusCode());
         System.out.println("Error Code: " + ase.getErrorCode());
         System.out.println("Request ID: " + ase.getRequestId());
     }

}


}
