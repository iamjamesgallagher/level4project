package Test;

import java.lang.Math;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;



public class Main {

	public static void main(String[] args)throws FileNotFoundException {
		Stock stock = new Stock("stock");
		Scanner fileIn = new Scanner(new File("C:\\Users\\tm\\Desktop\\Last Year at University\\level4project\\Stock Data - Google Finance\\Test_Data.txt"));
		fileIn.useDelimiter(",");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,9);
		cal.set(Calendar.MINUTE,30);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		
//		System.out.println(dateFormat.format(cal.getTime()));
		Stock appl = new Stock("Appl");
		
		
		
		
		while (fileIn.hasNextLine() == true){
			cal.add(Calendar.MINUTE, 1);
			//COLUMNS=DATE,CLOSE,HIGH,LOW,OPEN,VOLUME System.out.println(fileIn.nextLine());			
			String singleLine = fileIn.nextLine();
			String[] lineArray = singleLine.split(",");
			Float price = Float.parseFloat(lineArray[2]);
			Float volume = Float.parseFloat(lineArray[5]);			
			Float high = Float.parseFloat(lineArray[2]);
			Float open = Float.parseFloat(lineArray[4]);
			Float low = Float.parseFloat(lineArray[3]);
			Float close = Float.parseFloat(lineArray[1]);
			
//			System.out.println(dateFormat.format(cal.getTime()));
			TimePoint ts = new TimePoint(cal.getTime(),price,volume,high, low,close, open);	
//			System.out.println(ts);
			
			appl.setTimeandprice(ts);			
		}	
		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY,9);
		start.set(Calendar.MINUTE,32);
		start.set(Calendar.SECOND,0);
		start.set(Calendar.MILLISECOND,0);
		
				
		Calendar end = Calendar.getInstance();
		end.set(Calendar.HOUR_OF_DAY,9);
		end.set(Calendar.MINUTE,36);
		end.set(Calendar.SECOND,0);
		end.set(Calendar.MILLISECOND,0);
		
		//TWAP(appl,start, end, 10000,60);				 
		
		
		VWAP(appl,start, end, 10,15);
		
		}
		
	
	//Note Calendars give out dates when call .getTime() method. 
	
	public static double TWAP(Stock s, Calendar start, Calendar end, int amount, int period){
		double totalCost = 0;
		double totalBought = 0;
		
		double diff = end.getTimeInMillis() - start.getTimeInMillis();
		double numOfMinutes=((diff)/1000)/60;
		
		double numOfHours=(((diff)/1000)/60)/60;
		double numberOfOrders = (numOfMinutes/period) ;
		double indivOrderSizeDec = amount/numberOfOrders; 		
		int indivOrderSize = (int)indivOrderSizeDec;
		
		ArrayList buytime = buyTimes(start, end, period);
		
		
		
		for (int x = 0 ; x < s.DataLength(); x++){
			Date testd = s.getDataPoint(x).getTime(); /*System.out.println(s.getDataPoint(x).getTime());*/
			
			if( buytime.contains(testd) ){
				System.out.println( "Purchased :"+indivOrderSize +" at "+testd+ "for " +s.getDataPoint(x).getprice());				
				 totalCost += ( indivOrderSize*s.getDataPoint(x).getprice());				 
				 totalBought += indivOrderSize;
			}
		
	}
		System.out.println( "Purchased a total of  :"+totalBought + " for $" +totalCost);
		System.out.println( "To aquire from a one of purchase, it would have cost : $" + (s.getDataPoint(0).getprice()*totalBought));

		
		return totalCost;
	}
	

	
	
	
	public static double VWAP(Stock s, Calendar start, Calendar end, int amount,int period){
		
		double VWAPCost = 0; double noAlgCost = 0; 
		
		int tt = toalSharesTradedToday(s ) ;  
		
		ArrayList<Date> buyTimeD = buyTimes(start,  end, period);	
		ArrayList<Calendar> buyTimeC = buyTimesC(buyTimeD);
		
		
		for (int x = 0; x < ( (buyTimeC.size() -1 ) ) ; x++){			

			int ip = sharesTradedInPeriod(s, buyTimeC.get(x), buyTimeC.get(x+1));
			double percentageOfDay = tt/ ip; 
			
			int orderSizeForPeriod = (int)(amount * percentageOfDay);
			
			double priceAtTime = getPriceAtDateC(buyTimeC.get(x),s);
			
			VWAPCost += ( orderSizeForPeriod * priceAtTime);
			
			System.out.println("orderSizeForPeriod :" + orderSizeForPeriod + "cost was :" +priceAtTime );
				
		}
		
		
		return 2.5;
		
	}
	
	public static Double getPriceAtDateC(Calendar c, Stock s){
		Double price = 0.0;
		Date hitPoint = c.getTime();
		for (int x = 0; x < ( s.DataLength()) ; x++){		
			Date pointDate = s.getDataPoint(x).getTime();
			if ( hitPoint.equals(pointDate)  )
				price =  s.getDataPoint(x).getprice();
		}	
		return price;	
				
	}
	
	
	public static Double getPriceAtDateD(Date d, Stock s){
		Double price = 0.0;
		for (int x = 0; x < ( s.DataLength()) ; x++){		
			Date pointDate = s.getDataPoint(x).getTime();
			if ( d.equals(pointDate)  )
				price =  s.getDataPoint(x).getprice();
		}	
		return price;	
				
	}
	

	public static int sharesTradedInPeriod(Stock s, Calendar start,Calendar end ){
		int total = 0;
		double p1 = 0 ; double p2 = 0 ;
		
		Date startd  = start.getTime(); Date endd  = end.getTime();
		Date point = null;
		int mins = s.DataLength(); int flag = 0;
		for (int x = 0; x < (mins-1) ; x++){			
			point = s.getDataPoint(x).getTime();
			if (endd.equals(s.getDataPoint(x).getTime()))
				flag = 0;	
			if (startd.equals(point))
				flag = 1;
			if (flag == 1){
				p1 = s.getDataPoint(x).getvolume(); p2 = s.getDataPoint(x+1).getvolume();				
				double ddiff = p2- p1; int dif = (int)ddiff;
				dif =  Math.abs(dif);
				total += dif ;
				}	
						
			}
			
		System.out.println("Total Shares Traded in this period was = " + total);		
		return total;
	}
	
	
	public static int toalSharesTradedToday(Stock s ){
		int total = 0;
		double p1 = 0 ; double p2 = 0 ;
		int mins = s.DataLength();
		for (int x = 0; x < (mins-1) ; x++){
			p1 = s.getDataPoint(x).getvolume(); p2 = s.getDataPoint(x+1).getvolume();				
			double ddiff = p2- p1; int dif = (int)ddiff; 				
			if (dif < 0 )
				dif =  Math.abs(dif);
			total += dif ; 
			}
//		System.out.println("Total Shares Traded Today Was = " + total);		
		return total;
						
	}
	
	
	
	
	public static ArrayList<Date> buyTimes(Calendar s, Calendar e, int period){
		ArrayList<Date> buyTimes = new ArrayList<Date>();
		double diff = e.getTimeInMillis() - s.getTimeInMillis();
		double numOfMinutes=((diff)/1000)/60;
		double numOfOrders = numOfMinutes / period;
		Date startd = s.getTime() ;  
		buyTimes.add(startd);
		Calendar cal = Calendar.getInstance();  
		cal.setTime(startd);
		for (int x = 0; x < numOfOrders ; x++){
			cal.add(Calendar.MINUTE,period);
			Date d4 = cal.getTime();
			buyTimes.add(d4);		}
//		for (int x = 0; x < numOfOrders ; x++){
//			System.out.println(buyTimes.get(x));			
//		}
		return buyTimes;		
	}
	
	public static ArrayList<Calendar> buyTimesC(ArrayList<Date> d){
		ArrayList<Calendar> buyTimesCal = new ArrayList<Calendar>();
		for (int x = 0; x < d.size() ; x++){
			Calendar cal = Calendar.getInstance();  
			cal.setTime(d.get(x));
			buyTimesCal.add(cal);			
			}
		
		return buyTimesCal;
		
		
		
	}
	
	
	
	

	
	
	
	
}


	
	
	
//	
//	public static double POV(Stock s, Calendar start, Calendar end, int amount, int volume){
//		double totalCost = 0;
//		double totalBought = 0;
//		
//		peroid = 30;
//		
//		double diff = end.getTimeInMillis() - start.getTimeInMillis();
//		double numOfMinutes=((diff)/1000)/60;
//		
//		double numOfHours=(((diff)/1000)/60)/60;
//		double numberOfOrders = (numOfMinutes/period) ;
//		double indivOrderSizeDec = amount/numberOfOrders; 		
//		int indivOrderSize = (int)indivOrderSizeDec;
//		
//		ArrayList buytime = buyTimes(start, end, period);
//		
//		
//		
//		for (int x = 0 ; x < s.DataLength(); x++){
//			Date testd = s.getDataPoint(x).getTime(); /*System.out.println(s.getDataPoint(x).getTime());*/
//			
//			if( buytime.contains(testd) ){
//				System.out.println( "Purchased :"+indivOrderSize +" at "+testd+ "for " +s.getDataPoint(x).getprice());				
//				 totalCost += ( indivOrderSize*s.getDataPoint(x).getprice());				 
//				 totalBought += indivOrderSize;
//			}
//		
//	}
//		System.out.println( "Purchased a total of  :"+totalBought + " for $" +totalCost);
//		System.out.println( "To aquire from a one of purchase, it would have cost : $" + (s.getDataPoint(0).getprice()*totalBought));
//
//		
//		return totalCost;
//	}
//	
//	
//	
	
//	public static ArrayList<Double> typicalPrice(Stock s ){
//		//average of the high, low and close {(H+L+C)/3)}.
//		ArrayList<Double>  tp=new ArrayList<Double>(); 		int end = s.DataLength();
//		for (int x = 0; x < (end) ; x++){
//			tp.add( (s.getDataPoint(x).getHigh() + s.getDataPoint(x).getLow()+ s.getDataPoint(x).getClose() ) /3 ); 
//		}
//		return tp;
//	}	
	
	

		
//public static double VWAPMarker(Stock s, Calendar start, Calendar end, int amount){
//		
//		ArrayList<Double>  averagePrice =  typicalPrice( s );
//		ArrayList<Double>  averagePriceXVolume = null;
//		for (int x = 0; x < (s.DataLength()) ; x++){
//			averagePriceXVolume.add( ( averagePrice.get(x) * s.getDataPoint(x).getvolume() ) );
//			
//			}
//		ArrayList<Double>  totalVolumePrice = null;
//		for (int z = 0; z < (( s.DataLength()-1)) ; z++){
//			if (z ==0)
//				totalVolumePrice.add(averagePriceXVolume.get(z));
//			else
//				totalVolumePrice.add( ( averagePriceXVolume.get(z) + averagePriceXVolume.get(z+1) ) );
//			
//			}
//		
//		ArrayList<Double>  totalVolume = null;
//		for (int y = 0; y < (( s.DataLength()-1)) ; y++){
//			if (y ==0)
//				totalVolume.add(s.getDataPoint(y).getvolume());
//			else
//				totalVolume.add( ( totalVolume.get(y) + s.getDataPoint(y+1).getvolume() ) );
//			
//			}
//		
//		ArrayList<Double>  VWAPMarker = null;
//		for (int x = 0; x < (( s.DataLength()-1)) ; x++){
//			VWAPMarker.add( ( totalVolumePrice.get(x) / totalVolume.get(x) ) );
//			
//			}
		
		
		

	

