package pseudoCode;

import java.util.ArrayList;

public class TestVariable {

	public static void main(String[] args) {

		
//		String lol = "ALGORITHME test (e var1 : entier, e var2 : reel, s var3)";
//		String varDeclar = lol.split("\\(|\\)")[1];
		
//		String[] ensVars = varDeclar.split(",");
//		
//		for (String s : ensVars) {
//			s = s.trim();
//			
//			System.out.println("Param type : " + s.split(":")[0].split(" ")[0].trim());
//			System.out.println("Var name   : " + s.split(":")[0].split(" ")[1].trim());
//			
//			System.out.println("Var type   : " + s.split(":")[1].trim());
//		}
		
		
		String lol = "ALGORITHME sousProg2(e var1 : entier, e var2 : reel, s var3)";
		System.out.println(!lol.matches("([\\w]*)\\([\\w]*\\)"));
			
		
		
			
	}
}
