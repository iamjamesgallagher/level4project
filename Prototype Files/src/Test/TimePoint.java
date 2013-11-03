package Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePoint {	
	private Date time;
	private double volume;	
	
	private double price;
	
	private double close ;
	private double open ;	
	private double high;
	private double low;
	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	
	
	DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	
	public TimePoint(){
		this.time = null;
		this.price = 0;
		this.volume = 0;
	}
	
	public TimePoint(Date d,double b,double a, double h, double l , double c, double o){
		this.time = d;
		this.price = b;
		this.volume = a;
		this.high = h;
		this.low = l;
		this.close = c;
		this.open = o;
		
	}
	
	public Date getTime() {
		return this.time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public double getprice() {
		return this.price;
	}

	public void setprice(double price) {
		this.price = price;
	}


	public double getvolume() {
		return volume;
	}


	public void setvolume(double volume) {
		this.volume = volume;
	}

	public String toString(){
		return "Time" +":" + dateFormat.format(this.time.getTime())+ ", Price :" + this.price + ", Volume : " + this.volume;
		
	}
	
	
}
