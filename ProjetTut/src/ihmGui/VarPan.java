package ihmGui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pseudoCode.Programme;

public class VarPan extends JPanel {
	
	private String[] vars;
	private JLabel txt;
	private Programme prog;
	
	public VarPan(Programme prog) {
		this.prog = prog;
		this.txt  = new JLabel();
		
		paint();
		
		txt.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(txt);
		
	}
	
	public void paint() {
		this.vars = prog.traceVariable.split("\n");
		txt.setText("<html><body style=\"width: 100%\" ><table style=\"width: 100%\" border=\"0\" cellspacing=\"0\">");
		for(String var : vars) {
			String[] splitVar = var.split("\\|");
			
			
			txt.setText(txt.getText()+"<tr>");
			
			for(String str : splitVar) {
				txt.setText(txt.getText()+"<td style=\"border: 1px black solid\">"+ str.trim() +"</td>");
			}

			txt.setText(txt.getText()+"</tr>");
		}
		
		txt.setText(txt.getText()+"</table></body></html>");
	}
}
