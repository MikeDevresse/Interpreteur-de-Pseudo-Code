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
	private String code="";
	private JScrollPane panelCode;
	private CodePan codepan;
	
	
	public GUI(String[] codeLignes, Programme prog) {
		this.setTitle("Affichage du pseudo-code");
		this.setSize(1920, 1080);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		for(String str : codeLignes)
			this.code+=str+"\n";
		
		codepan = new CodePan(this.code, prog);
		JScrollPane panelCode = new JScrollPane(codepan);
		
		JSplitPane jspS = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCode, new JPanel());
		JSplitPane jspP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jspS, new JLabel());
		
		jspP.setDividerLocation(1000);
		
		jspS.setDividerLocation(1000);
		
		this.add(jspP);
		
		this.setVisible(true);
	}
	
	public void repaint() {
		codepan.paint();
	}
}