package data;

//import java.io.IOException;
import java.util.Scanner;
//import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;

/*
 * 
 * 
 * 
 * 
 */
public class Data__ {
	
	public int year;
	//public double[] values;
	public TreeMap<Integer, Double> values;
	public double total;
	
	//read data from file content, 
	public Data__(String filestream) throws Exception {
		TreeMap<Integer, Double> temp = new TreeMap<>();
		Scanner scan = new Scanner(filestream);
		year = Integer.parseInt(scan.nextLine());
		String line;
		while (scan.hasNextLine()) {
			line = scan.nextLine();
			String[] result = line.split(":");
			if (temp.containsKey(Integer.parseInt(result[0]))) {	 
				throw new Exception("Month already exists"); 
			}
			else {
				temp.put(Integer.parseInt(result[0]), Double.parseDouble(result[1]));
			}
		}
		values = temp;
	}
	
	public Data__(int year, TreeMap<Integer, Double> values) {
		this.year = year;
		this.values = values;
		total = 0;
		for (double num: values.values()) {
			total += num;
		}
	}
	
	  public Double min() { return values.values().stream().max(Double::compare).get(); }
	  
	  public Double max() { return values.values().stream().min(Double::compare).get(); }
	  
	  public Double avg() { return total/values.size(); }
	  
	  public void update(int month, double value) { 
		  if (values.containsKey(month)) {
			  total += (value - values.get(month));
			  values.put(month, value); 
		  } else {
			  total += value;
			  values.put(month, value);
		  }
	  }
	  
	  public Collection<Integer> getMonths() { return values.keySet(); }
	 
	public Collection<Double> getValues() { return values.values(); }
	 

	public Data__() {
		this.values = new TreeMap<>();
	}
    
	
	public String toString() { 
		final StringBuilder str = new StringBuilder(Integer.toString(year) + '\n');
		values.forEach((k, v) -> { str.append(k.toString() + ": " + v.toString() + '\n'); }); 
		return str.toString(); 
	}
	 

}