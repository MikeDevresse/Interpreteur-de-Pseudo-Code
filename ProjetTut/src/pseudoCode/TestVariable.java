package pseudoCode;

public class TestVariable {
	
	public static void main(String[] args) {
		
//		String expression = "n <-- chaîne de caractère";
//		
//		Variable v = VariableFactory.createVariable(expression);
//		
//		System.out.println(v);
		
		Variable<Integer> v = VariableFactory.createVariable("n : entier", true);
		v.setValue(1);
		
		System.out.println(v.getValue());
		
		
		
		
	}

}
