package pseudoCode;

import bsh.EvalError;
import bsh.Interpreter;

public class TestVariable {
	
	public static void main(String[] args) {
		
		Interpreter i = new Interpreter();
		
		try {
			String current = "si x < 2 alors lol";
			String condition = current.split("si | alors")[1];
			
			System.out.println(condition);
			i.eval("x = 1");
			System.out.println(i.eval(condition));
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		
	}

}
