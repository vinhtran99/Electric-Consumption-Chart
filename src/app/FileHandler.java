package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.TreeMap;

import data.Data__;

public class FileHandler {
	
	//private String fileContent;
	
	public static void openFile(String pathToFile) {	}
	
	public static Data__ readFile(/*String pathToFile*/ File aFile) {
		//File aFile = new File(pathToFile);
		if (aFile.length() == 0) return new Data__();
		TreeMap<Integer, Double> temp = new TreeMap<>();
		try(BufferedReader input = new BufferedReader(new FileReader(aFile))) {		
	        String line = input.readLine();
	        int year = Integer.parseInt(line);
	        while((line = input.readLine()) != null) {
	        	String[] result = line.split(":");
	        	if (temp.containsKey(Integer.parseInt(result[0]))) {	 
					throw new Exception("Month already exists"); 
				}
				else {
					if (Integer.parseInt(result[0]) > 12 || Integer.parseInt(result[0]) < 1) {
						throw new Exception("Invalid month");
					} 
					else temp.put(Integer.parseInt(result[0]), Double.parseDouble(result[1]));
				}     
	        }
	        Data__ myData = new Data__(year, temp);
	        return myData;
	    }
	    catch(IOException e2) {
	        System.out.println("Cannot open file or corrupted file.");
	    } 
		catch (Exception e3) {
			System.out.println("File has the wrong format.");
		}
		return null;
	}
	
	public static void saveToFile(/*String pathToFile*/File aFile, Data__ myData) throws IOException{
		//File aFile = new File(pathToFile);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(aFile))) {
			writer.write(myData.toString());
		} 
		catch(IOException ex) {
			System.out.println("Error writing to file: " + /*pathToFile*/aFile.getName());
		}
	}
	
}
