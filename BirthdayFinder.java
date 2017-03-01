import java.io.Console;
import java.util.Arrays;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
* Command line script which determines log ages and log birthdays
*
*/
public class BirthdayFinder {
    
    public static void main (String args[]) throws IOException {

        Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }

        String date = c.readLine("ENTER BIRTHDATE OF OBJECT: (as format Jun 18 2009) > ");
        Date ddate = new Date();
        Date dnow = new Date();        
	try {
		SimpleDateFormat parser = new SimpleDateFormat("MMM d yyyy");
		ddate = parser.parse(date);
		//System.out.println(" "+ ddate.getTime());
	} catch (Exception e) {
		e.printStackTrace();
	}

	System.out.println("now: " + dnow.getTime());
	System.out.println("then: " + ddate.getTime());
	long delta = (dnow.getTime()-ddate.getTime())/1000;  // basically the current age in seconds

	String[] labels = {"seconds ","minutes ","hours  ","days   ","weeks  ","months ","years ","great-years"};
	double[] ages = new double[8];
	double[] intervals = new double[labels.length];
	
	int a = 0;	
        intervals[a]=1; a++;
	intervals[a]=60; a++;
	intervals[a]=3600; a++;
	intervals[a]=86400; a++;
	intervals[a]=7*86400; a++;
	intervals[a]=27*86400+7*3600+43*60+11; a++;
	intervals[a]=86400*365.2536; a++;
	intervals[a]=86400*365.2537*25771.0;
	
	a = 0;	
	ages[a]=delta; a++;
	ages[a]=delta/60; a++;
	ages[a]=delta/3600; a++;
	ages[a]=delta/86400; a++;
	ages[a]=delta/7/86400; a++;
	ages[a]=delta/(27*86400+7*3600+43*60+11); a++;
	ages[a]=delta/86400/365.2536; a++;
	ages[a]=delta/86400/365.2537/25771.0;

	System.out.println("AGE OF OBJECT: ");
	for (int i=0; i<labels.length; i++) {
		System.out.println(labels[i] + "\t" + (float)ages[i]);
	}
	System.out.println("\n LOG AGE OF OBJECT (base e, 2, 10): ");
	for (int i=0; i<labels.length; i++) {
		System.out.println(labels[i] + " : \t" + (int)Math.log(ages[i]) + "\t" + (int)(Math.log(ages[i])/Math.log(2))
										+ "\t"	+ (int)(Math.log(ages[i])/Math.log(10)) );
	}
	
        // keep arrays of the birthday's new int ("turning") and the number of seconds left before the birthday	
	// just do E folding ones here        
	double[] delays = new double[labels.length];
        int[] turning = new int[labels.length];	
	for (int j=0; j<labels.length; j++) {	
		turning[j] = nextPowerOfE(ages[j]);
		delays[j]= Math.exp(turning[j])*intervals[j]-(double)delta;
		// dump for debug		
		//System.out.println(j+ "  " + turning[j] + " and " + delays[j]);	
	}

	// find best birthday
	double lowestDelay = Double.MAX_VALUE;
	int best = -1;
	for (int j=0; j<labels.length; j++) {
		if (delays[j]<lowestDelay) {
			best=j;
			lowestDelay=delays[j];
		}
	}
	System.out.println("Next Log Birtday Is: " + labels[best] + " turning " + turning[best] + " in " + ((int)delays[best]/86400) + " days"); 
        
        // now 2 folding birthdays        
	for (int j=0; j<labels.length; j++) {	
		// dump for debug		
		turning[j] = nextPowerOfTwo(ages[j]);
		delays[j]= Math.pow(2.0,turning[j])*intervals[j]-(double)delta;
		//System.out.println(j+ "  " + turning[j] + " and " + delays[j]);	
	}

	// find best birthday
	lowestDelay = Double.MAX_VALUE;
	best = -1;
	for (int j=0; j<labels.length; j++) {
		if (delays[j]<lowestDelay) {
			best=j;
			lowestDelay=delays[j];
		}
	}
	System.out.println("Next Log2 Birtday Is: " + labels[best] + " turning " + turning[best] + " in " + ((int)delays[best]/86400) + " days");


        // now 10 folding birthdays        
	for (int j=0; j<labels.length; j++) {	
		// dump for debug		
		turning[j] = nextPowerOfTen(ages[j]);
		delays[j]= Math.pow(10.0,turning[j])*intervals[j]-(double)delta;
		//System.out.println(j+ "  " + turning[j] + " and " + delays[j]);	
	}

	// find best birthday
	lowestDelay = Double.MAX_VALUE;
	best = -1;
	for (int j=0; j<labels.length; j++) {
		if (delays[j]<lowestDelay) {
			best=j;
			lowestDelay=delays[j];
		}
	}
	System.out.println("Next Log10 Birthday Is:  " + labels[best] + " turning " + turning[best] + " in " + ((int)delays[best]/86400) + " days");



       /* turning[i] = nextPowerOfTwo(delta);
	delays[i]= Math.pow(2.0,turning[i])-delta;
	i=i+1;
        turning[i] = nextPowerOfTen(delta);
	delays[i]= Math.pow(10.0,turning[i])-delta;
	i=i+1;
       */
        	
	
    }

    public static int nextPowerOfTwo(double num) {
	int logTwo = (int)Math.floor(Math.log(num)/Math.log(2));
	return logTwo+1;
    } 
    public static int nextPowerOfTen(double num) {
	int logTen = (int)Math.floor(Math.log(num)/Math.log(10));
	return logTen+1;
    }
    
    public static int nextPowerOfE(double num) { 
	int logE = (int)Math.floor(Math.log(num));
	return logE+1;
    }
       		   
}
