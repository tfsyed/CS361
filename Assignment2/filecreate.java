import java.io.*;
import java.util.*;

public class filecreate{
	FileWriter f;
	public filecreate (){
		
	}

	public void setFile(String name){
		try{
			f = new FileWriter(name); 
		}
		catch (IOException e){
			System.out.println(e);
	
		}
	}

	public void write(String c){
		try{
			f.write(c);
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

	public void close(){
		try{
			f.flush();
			f.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
}