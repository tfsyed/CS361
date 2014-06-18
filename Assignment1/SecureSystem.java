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
    String name;
    SecurityLevel level;

	public void run(){
		try {
			Scanner sc = new Scanner(my_file);
			while(sc.hasNextLine()){
				
                synchronized(this){
                    String line = sc.nextLine();

                    if (!line.equals("")){
                        synchronized (this){
                            System.out.println(line);
                            InstructionObject currentInstruction = CreateInstruction(line);
                            my_monitor.execute_instruction (currentInstruction);
                            printState();
                        }
                    }
                }
                try{
					Thread.sleep(1);
				}
				catch (Exception e)
				{
					System.out.println(e);
				}

			}
		}
		catch (FileNotFoundException e){
			System.out.println ("file not found");
		}
	}

    public static void main (String[] args) throws FileNotFoundException{
       	
		/* If a valid file is given, use its contents as input. Otherwise, 
		   input will be taken from STDIN */

		File f1;
		File f2;
		f1 = new File(args[0]);
		f2 = new File(args[1]);

    	ReferenceMonitor shared_monitor = new ReferenceMonitor();

        /* create high and low security level */
        SecurityLevel low = SecurityLevel.LOW;
        SecurityLevel high = SecurityLevel.HIGH;

    	Runnable sys1 = new SecureSystem(f1, shared_monitor, "Hal", high);
    	Runnable sys2 = new SecureSystem(f2, shared_monitor, "Lyle", low);
    	Thread thread1 = new Thread(sys1);
    	Thread thread2 = new Thread(sys2);   

        /*Create Lobj and HObj*/
        shared_monitor.createNewObject("LObj", low);
        shared_monitor.createNewObject("HObj", high);   

    	thread1.start();
    	thread2.start();

  //       /** Create LYLE and HAL **/	
        shared_monitor.createNewSubject("Lyle", low);
        shared_monitor.createNewSubject("Hal", high);

       // sys.createSubject("Lyle", low);
       // sys.createSubject("Hal", high);

    }

    public SecureSystem(File f, ReferenceMonitor m, String name, SecurityLevel level){
    	my_monitor = m;
    	my_file = f;
        this.name = name;
        this.level = level;
    }

    public ReferenceMonitor getReferenceMonitor(){
    	return my_monitor;
    }

    public void createSubject(String name, SecurityLevel level)
    {
        my_monitor.createNewSubject(name, level);

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
    	int val = 0;

    	boolean bad = false;

    	int pos = 0;

    	Scanner scanner = new Scanner(line);

    	while (scanner.hasNext()){

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
    				// Read the Object Name
    				object_name = scanner.next();
                    if (my_monitor.get_object(object_name) == null){
                        type = instruction_type.BAD;
                    }
                    break;
    			case 2:
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

    	if (pos < 2 || (pos < 3 && type == instruction_type.WRITE))
    		type = instruction_type.BAD;

    	return new InstructionObject(type, this.name, object_name, val);
    }
}




