import java.util.*;
import java.io.*;


public class CovertChannel{
	
	public static void main(String[] args)
	{
		/* If a valid file is given, use its contents as input. */
		if (args[0] == null)
			throw new IllegalArgumentException("Please use \"java CovertChannel (v) <inputfilename>\"");
		
		File f;

		boolean verbose = args[0].equals("v");

		if (verbose)
		{
			f = new File (args[1]);
			System.out.println("I am in verbose mode.");
		}
		else{
			f = new File (args[0]);

		}

		if (f == null)
			throw new IllegalArgumentException("Please use \"java CovertChannel (v) <inputfilename>\"");

		System.out.println("File name :" + f.toString());

		Scanner sc;
		try{
		 sc = new Scanner(f);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found.");
			//throw new FileNotFoundException(e);
			return;
		}

		while(sc.hasNextLine())
		{
			String line = sc.nextLine();

			byte b[] = line.getBytes();

			ByteArrayInputStream inputStream  = new ByteArrayInputStream(b);
			int my_byte = inputStream.read();
			while(my_byte != -1)
			{
				//System.out.println("val" + my_byte);
				for(int i = 0; i < 8; i++)
				{
					//int mask = my_byte & 0x01;

					System.out.println("num" + ((my_byte & 0x00000080)>>7));
					my_byte = (my_byte << 1);
				}
				//System.out.println("Output in bits" + bitStream);
				my_byte = inputStream.read();

			}
        }

        /* create high and low security level */
        SecurityLevel low = SecurityLevel.LOW;
        SecurityLevel high = SecurityLevel.HIGH;

        /* create a Secure System, which contains a ReferenceMonitor */
        SecureSystem sys = new SecureSystem();    

        /** Create LYLE and HAL **/	
        sys.createSubject("Lyle", low);
        sys.createSubject("Hal", high);
	}
}