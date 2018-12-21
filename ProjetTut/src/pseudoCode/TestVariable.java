package pseudoCode;

import java.util.ArrayList;

public class TestVariable {

	public static void main(String[] args) {

		
		String s = "a < 2 et b > 5 ou x < 10";
		String[] conditions = s.split("et|ou");
		ArrayList<String> condVariables = new ArrayList<String>();
		
		for (String cond : conditions) {
			cond = cond.trim();
			
			String[] vars = cond.split("<=|>=|<|>|=");
			for (int i = 0; i < vars.length; i+=2)
				condVariables.add(vars[i]);
		}
			
	}
}
