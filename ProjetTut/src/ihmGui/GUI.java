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
	private JScrollPane panelVars;
	private CodePan codepan;
	private VarPan  varspan;
	private ExecPan execpan;
	
	public GUI(String[] codeLignes, Programme prog) {
		this.setTitle("Affichage du pseudo-code");
		this.setSize(1300, 800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		for(String str : codeLignes)
			this.code+=str+"\n";
		
		codepan = new CodePan(this.code, prog, panelCode.getHorizontalScrollBar());
		JScrollPane panelCode = new JScrollPane(codepan);
		varspan = new VarPan(prog);
		JScrollPane panelVars = new JScrollPane(varspan);
		
		execpan = new ExecPan(prog);
		
		JSplitPane jspS = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCode, panelVars);
		JSplitPane jspP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jspS, execpan);
		
		jspP.setDividerLocation(600);
		
		jspS.setDividerLocation(650);
		
		this.add(jspP);
		
		this.setVisible(true);
	}
	
	public void repaint() {
		codepan.paint();
		varspan.paint();
		execpan.paint();
	}
}