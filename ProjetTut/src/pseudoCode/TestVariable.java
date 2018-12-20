package pseudoCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bsh.Interpreter;
import util.Fonctions;

public class TestVariable {

	public static void main(String[] args) {

		
		String s2 = "cas \"jamil le bo\" :";
		
		System.out.println(s2.split("cas |[ ]*:")[1]);
		
		//System.out.println(s2.split("\\[|\\]")[2]);
	}
}
