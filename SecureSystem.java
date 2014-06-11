/* Something Something SecureSystem

*/

import java.util.*;
import java.io.*;


public class SecureSystem{

	ReferenceMonitor my_monitor;
	static SecureSystem sys;

    public static void main (String[] args) throws FileNotFoundException{
       	File f;
       	Scanner sc;
 		if (args.length > 0)
 		{
 			f = new File (args[0]);
 			sc = new Scanner (f);
 		}
 		else
 		{
 			sc = new Scanner (System.in);
 		}
        
    	/*Create secure system and name it sys*/
    	 sys = new SecureSystem();

    	 /*create high and low security level */
    	 SecurityLevel low = SecurityLevel.LOW;
    	 SecurityLevel high = SecurityLevel.HIGH;


        /** Create LYLE and HAL **/	
         sys.createSubject("Lyle", low);
         sys.createSubject("Hal", high);

		/*Create Lobj and HObj*/
		sys.getReferenceMonitor().createNewObject("LObj", low);
		sys.getReferenceMonitor().createNewObject("HObj", high);   

		while (sc.hasNextLine()){
			//System.out.println(sc.nextLine());
			String line = sc.nextLine();
			if (!line.equals(""))
			{
				InstructionObject currentInstruction = sys.CreateInstruction(line);
				sys.my_monitor.execute_instruction (currentInstruction);
				sys.printState();
			}

			//sys.printState();
		}
		//sys.printState(); 
    }

    public SecureSystem(){
    	// Create a SecureSystem Object
    	my_monitor = new ReferenceMonitor();
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
    		//System.out.println("in the while loop");
    		//System.out.println("pos is " + pos);
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

    //************************ OTHER CLASSES **********************//
    public class ReferenceMonitor{	
		
		ObjectManager my_object_manager;
		ArrayList<item> subjects;

		//constructor for ReferenceMonitor
		public ReferenceMonitor()
		{
			//initializes the reference monitor
			my_object_manager = new ObjectManager();
			subjects = new ArrayList<item>();
		}
 			
		public void execute_instruction(InstructionObject instruction)
		{
			if (instruction.instr == instruction_type.BAD)
				System.out.println ("Bad Instruction");
			else if (instruction.instr == instruction_type.READ)
				executeRead (instruction);
			else
				executeWrite(instruction);
		}

		public void executeRead (InstructionObject instruction)
		{
			item subject = get_subject (instruction.subject_name);
			item object = get_object (instruction.object_name);

			System.out.println(instruction.subject_name.toLowerCase() + " reads " + instruction.object_name.toLowerCase());

			if (subject == null || object == null)
			{
				return;
			}

			if (read_dominates (subject, object)){
				subject.value = object.value;
			}
			else
			{
				subject.value = 0;
			}
			
		}

		public void executeWrite (InstructionObject instruction)
		{
			item subject = get_subject (instruction.subject_name);
			item object = get_object (instruction.object_name);

			if (subject == null || object == null)
			{
				return;
			}

			System.out.println(instruction.subject_name.toLowerCase() + " writes value " + instruction.value + " to " + instruction.object_name.toLowerCase());
			if (write_dominates (subject, object)){
				object.value = instruction.value;
			}
		}

		public item get_subject (String name)
		{
			for (int i = 0; i < subjects.size(); i++)
			{
				if (subjects.get(i).name.toUpperCase().equals (name.toUpperCase()))
					return subjects.get(i);
			}
			return null;
		}

		public item get_object (String name)
		{
			ArrayList<item> objects = my_object_manager.objects;

			for (int i = 0; i < objects.size(); i++)
			{
				if (objects.get(i).name.toUpperCase().equals (name.toUpperCase()))
					return objects.get(i);
			}
			return null;
		}

		public boolean read_dominates(item subject, item object)
		{
			return subject.item_level.ordinal() >= object.item_level.ordinal();
		}

		public boolean write_dominates(item subject, item object)
		{
			return subject.item_level.ordinal() <= object.item_level.ordinal();
		}

 		public void createNewSubject(String name, SecurityLevel level)
 		{
 			subjects.add(new item(name,level));
 		}

		private class ObjectManager
		{
			ArrayList<item> objects;
		    private ObjectManager(){
				// Constructor for ObjectManager
				objects = new ArrayList<item>();
		 	}

		 	public void addObj(String name, SecurityLevel level){
		 		objects.add(new item(name, level));
		 	}
		}


		public void createNewObject(String name, SecurityLevel level)
		{
				my_object_manager.addObj (name, level);
		}

		public class item
		{
			String name;
			SecurityLevel item_level;
			int value;

			public item(String name,SecurityLevel level){

				this.name = name;
				this.item_level = level;
				value = 0;
			}

		}

   		public void print_local()
    	{	
    		for(int i = 0; i < my_object_manager.objects.size(); i++)
    		{
    			item current = my_object_manager.objects.get(i);

    			System.out.println("   " + current.name + " has value: " + current.value);
    		}

    		for(int i = 0; i < subjects.size(); i++)
    		{
    			item current = subjects.get(i);
  
    			System.out.println("   " + current.name + " has recently read: " + current.value);
    		}
    	
    	

		}
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

	public enum SecurityLevel {LOW, HIGH};
	public enum instruction_type{READ, WRITE, BAD};
}




