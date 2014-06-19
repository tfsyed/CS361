import java.util.*;
import java.io.*;


public class CovertChannel{
	
	static boolean verbose;
	static	String filename;

	public static void main(String[] args)
	{	
		File f;
		FileWriter my_f;

		/* Parsing the arguments */
		try{
			f = fileParser(args);
		}
		catch(IllegalArgumentException e){
			System.out.println("Please use \"java CovertChannel (v) <inputfilename>\"");
			return;
		}
		catch(Exception e){
			System.out.println(e);
			System.out.println("a");
			return;
		}
		
		/* Hook up the scanner to the file */
		Scanner sc;
		try{
		 	sc = new Scanner(f);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found.");
			return;
		}

        /* create high and low security level */
        SecurityLevel low = SecurityLevel.LOW;
        SecurityLevel high = SecurityLevel.HIGH;

        /* create a Secure System, which contains a ReferenceMonitor */
        SecureSystem sys = new SecureSystem();   

        /* creatng a new java FileWriter*/
        try{
        	my_f = new FileWriter(filename + ".out"); 
        }
        catch(Exception e){
        	System.out.println("File creation failed");
        	return;
        }

        /** Create LYLE and HAL **/	
        sys.createSubject("Lyle", low);
        sys.createSubject("Hal", high);

        /* Main Execution loop */
        try{
			execute(sc, sys, my_f);
		}
		catch(Exception e)
		{
			System.out.println("execute failed");
			System.out.println(e);
			return;
		}
	}


	/* Main Execution Function */
	public static void execute(Scanner sc, SecureSystem sys, FileWriter my_f){

		while(sc.hasNextLine())
		{
			String line = sc.nextLine();
			line = line + "\n";

			byte b[] = line.getBytes();

			ByteArrayInputStream inputStream  = new ByteArrayInputStream(b);
			int my_byte = inputStream.read();
			while(my_byte != -1)
			{
				int[] buff = new int[8];
				for(int i = 0; i < 8; i++)
				{

					buff[i] = (my_byte & 0x00000080)>>7;
					my_byte = (my_byte << 1);
					
					if(buff[i] == 0)
						sys.my_monitor.executeRun((sys.my_monitor.get_subject("Hal")), my_f);
					sys.my_monitor.executeRun(sys.my_monitor.get_subject("Lyle"), my_f);
				}

				
				my_byte = inputStream.read();
				//System.out.println(Arrays.toString(buff));
				//System.out.println(bitsToByte(buff));
				/* Make a method to convert from byte to char */
			}
        }
	}

	

	public static File fileParser(String[] args){
		File f;
		/* If a valid file is given, use its contents as input. */
		if (args.length == 0)
			throw new IllegalArgumentException();
		

		verbose = args[0].equals("v");
		if (verbose)
		{
			if (args.length < 2)
				throw new IllegalArgumentException();
			filename = args[1];
			f = new File (filename);

			System.out.println("I am in verbose mode.");
		}
		else{
			filename = args[0];
			f = new File (filename);
		}

		System.out.println("File name: " + f.toString());
		return f;
	}

}