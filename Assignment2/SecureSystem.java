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

public class SecureSystem {

	ReferenceMonitor my_monitor;

    public SecureSystem(){
    	my_monitor = new ReferenceMonitor();
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
}




