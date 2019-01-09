package ihmGui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import pseudoCode.Programme;

/**
 * Frame principale du gui
 */
public class GUI extends JFrame{
	
	/** code. */
	private String code="";
	
	/** panel code. */
	private JScrollPane panelCode;
	
	/** panel vars. */
	private JScrollPane panelVars;
	
	/** codepan. */
	private CodePan codepan;
	
	/** varspan. */
	private VarPan  varspan;
	
	/** execpan. */
	private ExecPan execpan;
	
	/** prog. */
	private Programme prog;
	
	/**
	 * Instanciation de gui.
	 *
	 * @param codeLignes le pseudo-code ligne par ligne
	 * @param prog le programme lié au pseudo-code
	 */
	public GUI(String[] codeLignes, Programme prog) {
		this.prog = prog;
		
		this.setTitle("Affichage du pseudo-code");
		this.setSize(1300, 800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		for(String str : codeLignes)
			this.code+=str+"\n";
		
		codepan = new CodePan(this.code, prog);
		panelCode = new JScrollPane(codepan);
		codepan.paint();
		
		varspan = new VarPan(prog);
		panelVars = new JScrollPane(varspan);
		
		execpan = new ExecPan(prog);
		
		JSplitPane jspS = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCode, panelVars);
		JSplitPane jspP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jspS, execpan);
		
		jspP.setDividerLocation(600);
		
		jspS.setDividerLocation(650);
		
		this.add(jspP);
		
		this.setVisible(true);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	public void repaint() {
		codepan.paint();
		try {Thread.sleep(50);}catch(Exception e){}
		panelCode.getVerticalScrollBar().setValue(15*prog.getCourant().getLigneCourante());
		
		varspan.paint();
		try {Thread.sleep(50);}catch(Exception e){}
		panelVars.getVerticalScrollBar().setValue(panelVars.getVerticalScrollBar().getMaximum());
		
		execpan.paint();
	}
}