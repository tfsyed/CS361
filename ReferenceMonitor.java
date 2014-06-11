
   	import java.util.*;
	import java.io.*;

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

		/* Methods to compare levels */
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


