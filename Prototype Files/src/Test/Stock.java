package Test;

import java.util.ArrayList;


public class Stock {

	private String name;
	private ArrayList<TimePoint> timeandprice;
		
	public Stock(String s ){
		this.name = s;
		this.timeandprice = new ArrayList<TimePoint>();
		
	}

	public String getName() {
		return name;
	}

	public ArrayList<TimePoint> getDataArray() {
		return this.timeandprice;
	}
	
	public TimePoint getDataPoint(int n) {
		return this.timeandprice.get(n);
	}
	
	public int DataLength() {
		return timeandprice.size();
	}

	public void setTimeandprice(TimePoint t) {
		this.timeandprice.add(t);
	}
	

	public double getOpen() {
		return this.timeandprice.get(0).getprice();
	}
	
//	public double getClose() {
//		int i = timeandprice.size();
//		return this.timeandprice.get(this.timeandprice.size()).getprice();
//	}
	
	
	public String toString(){
//		return "Stock" +":" + this.name + ", Open :" + getOpen()/*+ ", Close :" + this.getClose()*/  ;
		return "Stock" +":" + this.name  ;
		
	}
	
	
}
