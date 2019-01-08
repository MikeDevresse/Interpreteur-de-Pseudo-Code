package ihmGui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.Controleur;
import pseudoCode.Programme;

public class ExecPan extends JPanel implements ActionListener {
	private JScrollPane jsp;
	private JLabel txt;
	
	private JTextField console;
	
	private String exec;
	private Programme prog;
	
	public ExecPan(Programme prog) {
		this.prog = prog;
		console = new JTextField();
		console.addActionListener( this );
		
		txt = new JLabel();
		
		paint();
		
		jsp = new JScrollPane(txt);
		this.setLayout(new BorderLayout());
		this.add(jsp, BorderLayout.CENTER);
		this.add(console, BorderLayout.SOUTH);
	}
	
	public void paint() {
		exec = prog.traceExec;
		txt.setText("<html><body style=\"font-size: 15px\">");

		for(String str : exec.split("\n")) {
			if(str.length()>2) {
				char prefix = str.charAt(0);
				str = str.substring(2);

				switch (prefix) {
				case 'l': txt.setText(txt.getText() + "<p style=\"color:#FFD700\">" + str + "</p>"); 
					break;
				case 'e': txt.setText(txt.getText() + "<p style=\"color:purple\">" + str + "</p>"); 
					break;
				case 'a': txt.setText(txt.getText() + "<p style=\"color:blue\">" + str + "</p>"); 
					break;
				case 'r': txt.setText(txt.getText() + "<p style=\"color:RED\">" + str + "</p>"); 
					break;
				default:  txt.setText(txt.getText() + "<p>" + str + "</p>"); 
					break;
				}
			}
		}
		txt.setText(txt.getText() + "</body></html>");
	}

	public void actionPerformed ( ActionEvent e )
	{
		Controleur.getControleur().envoyerCommande( console.getText() );
		console.setText( "" );
	}
}
