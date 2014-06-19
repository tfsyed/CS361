
   	import java.util.*;
	import java.io.*;

    //************************ OTHER CLASSES **********************//
    public class ReferenceMonitor{	
		
		ObjectManager my_object_manager;
		ArrayList<item> subjects;
		int[] byteBuffer ;
		int bitCounter; 
		//constructor for ReferenceMonitor
		public ReferenceMonitor()
		{
			//initializes the reference monitor
			my_object_manager = new ObjectManager();
			subjects = new ArrayList<item>();
			byteBuffer = new int[8];
			bitCounter = 0;
		}

		public void executeRead (item Subject, String objectName)
		{
			//System.out.println("in read");
			/* Read value of Object into Subject */
			item my_object = get_object(objectName);
			if(read_dominates(Subject, my_object))
			{
				Subject.value = my_object.value;
			}
			else
			{
				Subject.value = 0;
			}

		}

		public void executeWrite (item Subject, String objectName, int val)
		{
			//System.out.println("in write");
			/* Modify value of Object to value */
			item my_object = get_object(objectName);
			//if (my_object == null)
			//	System.out.println("object was null");
			if(write_dominates(Subject, my_object))
			{
			//	System.out.println("write_dominates");
				/*Set the objecive val to passed by val*/
				my_object.value = val;
			}

		}	

		public void executeCreate (item Subject, String objectName)
		{
			//System.out.println("in create");
			/* Create Object name with SecurityLevel of Subject */
			if(get_object("OBJ") == null)
				createNewObject(objectName, Subject.item_level);


		}

		public void executeDestory (item Subject, String objectName)
		{
			item my_object = get_object(objectName);
			/* Destroy Object name */
			if(my_object != null)
			{	
				if(write_dominates(Subject,my_object ))
					destroyObject(objectName);//change it back to my_object
			}
		}

		public void executeRun (item Subject, FileWriter f)
		{
			if (Subject.name.equals("Hal")){
				/* Run Hal */

				executeCreate(Subject, "OBJ");
			}
			else{
				/* Run Lyle */
				executeCreate(Subject, "OBJ");
				executeWrite(Subject, "OBJ", 1);
				executeRead(Subject, "OBJ");
				executeDestory(Subject, "OBJ");

				/*Writing bits to a file*/
				byteBuffer[bitCounter] = Subject.value;

				bitCounter++;
				if(bitCounter == 8)
				{
					try{
						//using local function to get bytes for 8 bits and writing it to a file 
						char output = bitsToByte(byteBuffer);
						f.write(output);
						f.flush();

					}
					catch (Exception e){
						System.out.println("write failed");
						return;
					}
					bitCounter = 0;
				}

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

		 	public void destroyObj(item object)
		 	{
		 		objects.remove(object);
		 	}
		}


		public void createNewObject(String name, SecurityLevel level)
		{
				//System.out.println("creating new Object " + name);
				my_object_manager.addObj (name, level);
		}

		
		public void destroyObject(String name)
		{
			item object = get_object(name);
			if(object != null)
				my_object_manager.destroyObj(object);
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


		public char bitsToByte(int[] buff){

		int result = 0;
		for (int i = 0; i < 8; i++){
			int value = (buff[i] << (7 - i));
			result = result | value;
		}
		return (char) result;

	} 
}


