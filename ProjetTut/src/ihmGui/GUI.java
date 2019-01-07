package ihmGui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import pseudoCode.Programme;

public class GUI extends JFrame{
	public GUI(String code, Programme prog) {
		this.setTitle("Affichage du pseudo-code");
		this.setSize(1920, 1080);
		this.setLocationRelativeTo(null);
		
		JScrollPane panelCode = new JScrollPane(new CodePan(code, prog));
		
		JSplitPane jspS = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCode, new JPanel());
		JSplitPane jspP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jspS, new JLabel());
		
		jspP.setDividerLocation(1000);
		
		jspS.setDividerLocation(1000);
		
		this.add(jspP);
		
		this.setVisible(true);
	}
}