/* 
	Authors:
	Name: Tehreem Syed 
	UTEID: tfs385

	Name: Amanda Liem
	UTEID: al34232

	Assignment 1: Create a Secure System which executes instructions of the form:

	READ subject_name object_name
	WRITE subject_name object_name value

	Current implementation contains 2 subjects, Hal and Lyle, and 2 objects,
	HObj and LObj. Hal and HObj have a HIGH security level, and Lyle and LObj 
	have a LOW security level. 

	Access to Objects follow the Bell and LaPadula security rules:
	Simple Security, the *-property, and strong tranquility
*/

import java.util.*;
import java.io.*;

public class SecureSystem implements Runnable {

	ReferenceMonitor my_monitor;
	static SecureSystem sys;
	File my_file;

	public void run(){
		try {
			Scanner sc = new Scanner(my_file);
			while(sc.hasNextLine()){
				try{
					Thread.sleep(1);
				}
				catch (Exception e)
				{
					System.out.println(e);
				}
				System.out.println(sc.nextLine());
			}
		}
		catch (FileNotFoundException e){
			System.out.println ("file not found");
		}
	}

    public static void main (String[] args) throws FileNotFoundException{
       	
		/* If a valid file is given, use its contents as input. Otherwise, 
		   input will be taken from STDIN */
		// File f;
  //      	Scanner sc;
 	// 	if (args.length > 0)
 	// 	{
 	// 		f = new File (args[0]);
 	// 		sc = new Scanner (f);
 	// 	}
 	// 	else
 	// 	{
 	// 		sc = new Scanner (System.in);
 	// 	}

		File f1;
		File f2;
		f1 = new File(args[0]);
		f2 = new File(args[1]);
        
    	/* Create secure system and name it sys */
    	// sys = new SecureSystem();

    	ReferenceMonitor shared_monitor = new ReferenceMonitor();

    	Runnable test1 = new SecureSystem(f1, shared_monitor);
    	Runnable test2 = new SecureSystem(f2, shared_monitor);
    	Thread thread1 = new Thread(test1);
    	Thread thread2 = new Thread(test2);
    	thread1.start();
    	thread2.start();

  //   	 /* create high and low security level */
  //   	 SecurityLevel low = SecurityLevel.LOW;
  //   	 SecurityLevel high = SecurityLevel.HIGH;


  //       /** Create LYLE and HAL **/	
  //        sys.createSubject("Lyle", low);
  //        sys.createSubject("Hal", high);

		// /*Create Lobj and HObj*/
		// sys.getReferenceMonitor().createNewObject("LObj", low);
		// sys.getReferenceMonitor().createNewObject("HObj", high);   

		// while (sc.hasNextLine()){
		// 	String line = sc.nextLine();
		// 	if (!line.equals(""))
		// 	{
		// 		InstructionObject currentInstruction = sys.CreateInstruction(line);
		// 		sys.my_monitor.execute_instruction (currentInstruction);
		// 		sys.printState();
		// 	}
		// }
    }

    public SecureSystem(File f, ReferenceMonitor m){
    	my_monitor = m;
    	my_file = f;
    }

    public ReferenceMonitor getReferenceMonitor(){
    	return my_monitor;
    }

    public void createSubject(String name, SecurityLevel level)
    {
    	my_monitor.createNewSubject(name,level);
    }

    public void printState()
    {
    	System.out.println("The current state is: ");
    	my_monitor.print_local();
    	System.out.println("");
    }

    public InstructionObject CreateInstruction(String line)
    {
    	instruction_type type = null;
    	String object_name = null;
    	String subject_name = null;
    	int val = 0;

    	boolean bad = false;

    	int pos = 0;

    	Scanner scanner = new Scanner(line);

    	while (scanner.hasNext()){
    		if (pos > 100)
    			break;
    		switch (pos) {
    			case 0: 
    				// Read the type of instruction
    				String my_type = scanner.next();
    				if (my_type.toUpperCase().equals ("WRITE")){
    					type = instruction_type.WRITE;
    				}
    				else if (my_type.toUpperCase().equals ("READ")){
    					type = instruction_type.READ;
    				}
    				else{
    					type = instruction_type.BAD;
    				}
    				break;
    			case 1:
    				// Read the Subject Name
    				subject_name = scanner.next();
    				if (my_monitor.get_subject(subject_name) == null){
    					type = instruction_type.BAD;
    				}
    				break;
    			case 2:
    				object_name = scanner.next();
    				if (my_monitor.get_object(object_name) == null){
    					type = instruction_type.BAD;
    				}
    				break;
    			case 3: 
    				if (scanner.hasNextInt()){
    					val = scanner.nextInt();
    				}
    				else
    				{
    					type = instruction_type.BAD;
    				}
    				break;
    			default:
    				scanner.next();
    				break;
    		}
    		pos++;
    	}

    	if (pos < 3 || (pos < 4 && type == instruction_type.WRITE))
    		type = instruction_type.BAD;

    	return new InstructionObject(type, subject_name, object_name, val);
    }

	public class InstructionObject{
		instruction_type instr; 
		String subject_name;
		String object_name;
		int value; 

		public InstructionObject(instruction_type instr, String sub, String obj, int val)
		{
			this.instr = instr;
			this.subject_name = sub;
			this.object_name = obj;
			this.value = val;
		}
	}
}




