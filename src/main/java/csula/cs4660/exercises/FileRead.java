package csula.cs4660.exercises;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Introduction Java exercise to read file
 */
public class FileRead {
    private int[][] numbers;
    /**
     * Read the file and store the content to 2d array of int
     * @param file read file
     */
    public FileRead(File file) {
        // TODO: read the file content and store content into numbers
    	try {
    		int row=0;
    		int column = 0;
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine())
			{
				row++;
				String line = scanner.nextLine();
				String[] lineValuesStr = line.split(" ");
				for(int i=0;i<lineValuesStr.length;i++)
				{
					column = i;
				}
				column = column + 1;
			}
			numbers = new int[row][column];
			Scanner scanner1 = new Scanner(file);
			int lineNumber = 0;
			while(scanner1.hasNextLine())
			{
				String line = scanner1.nextLine();
				String[] lineValuesStr = line.split(" ");
				int[] lineValuesInt = new int[lineValuesStr.length];
				for(int i=0;i<lineValuesStr.length;i++)
				{
					lineValuesInt[i] = Integer.parseInt(lineValuesStr[i]);
				}
				
				for(int i=0;i<numbers.length;i++)
		    	{
					if(lineNumber==i)
					{
	    			for(int j=0;j<numbers[i].length;j++)
	    			{
	    				numbers[i][j] = lineValuesInt[j];
	    			}
					}
		    	}
				lineNumber++;
				
			}
			scanner.close();
			scanner1.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    /**
     * Read the file assuming following by the format of split by space and next
     * line. Display the sum for each line and tell me
     * which line has the highest mean.
     *
     * lineNumber starts with 0 (programming friendly!)
     */
    public int mean(int lineNumber) {
    	int numberCount = 0;
    	int sum =0;
    	for(int i=0;i<numbers.length;i++)
    	{
    		if(i==lineNumber)
    		{
    			for(int j=0;j<numbers[i].length;j++)
    			{
    				sum = sum + numbers[i][j];
    				numberCount++;
    			}
    		}
    	}
    	
    	int meanValue = sum/numberCount;
        return meanValue;
    }

    public int max(int lineNumber) {

    	int maxValue =0;
    	for(int i=0;i<numbers.length;i++)
    	{
    		if(i==lineNumber)
    		{
    			for(int j=0;j<numbers[i].length;j++)
    			{
    				if(maxValue<numbers[i][j])
    				{
    					maxValue = numbers[i][j];
    				}
    			}
    		}
    	}
    	
        return maxValue;
    }

    public int min(int lineNumber) {
    	int minValue =0;
    	for(int i=0;i<numbers.length;i++)
    	{
    		if(i==lineNumber)
    		{
    			for(int j=0;j<numbers[i].length;j++)
    			{
    				if(minValue>numbers[i][j])
    				{
    					minValue = numbers[i][j];
    				}
    			}
    		}
    	}
    	
        return minValue;
    }

    public int sum(int lineNumber) {
    	int numberCount = 0;
    	int sum =0;
    	for(int i=0;i<numbers.length;i++)
    	{
    		if(i==lineNumber)
    		{
    			for(int j=0;j<numbers[i].length;j++)
    			{
    				sum = sum + numbers[i][j];
    				numberCount += numberCount;
    			}
    		}
    	}
        return sum;
    }
}
