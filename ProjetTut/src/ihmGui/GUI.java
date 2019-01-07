package ihmGui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class GUI extends JFrame{
	public GUI(String code) {
		this.setTitle("Affichage du pseudo-code");
		this.setSize(1920, 1080);
		this.setLocationRelativeTo(null);
		
		
		JScrollPane panelCode = new JScrollPane(new CodePan(code));
		
		/*
		JSplitPane jspS = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCode, panelCode);
		JSplitPane jspP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jspS, new JLabel());
		
		jspS.setBackground(Color.GREEN);
		jspP.setBackground(Color.RED);
		
		//jspP.setDividerLocation(1000);
		
		//jspS.setDividerLocation(1000);
		
		this.add(jspP);
		*/
		
		this.add(panelCode);
		this.setVisible(true);
	}
}
