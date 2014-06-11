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