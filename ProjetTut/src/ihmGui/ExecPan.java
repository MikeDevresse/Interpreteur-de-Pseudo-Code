package ihmGui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.Controleur;
import pseudoCode.Programme;

/**
 * Class du panel affichant la trace d'execution
 */
public class ExecPan extends JPanel implements ActionListener {
	
	/** pan. */
	private JPanel pan;
	
	/** jsp. */
	private JScrollPane jsp;
	
	/** txt. */
	private JLabel txt;
	
	/** jsp comm. */
	private JScrollPane jspComm;
	
	/** txt comm. */
	private JLabel txtComm;
	
	/** console. */
	private JTextField console;
	
	/** exec. */
	private String exec;
	
	/** prog. */
	private Programme prog;
	
	/** comms. */
	private HashMap<Integer,String> comms;
	
	/**
	 * Instanciation de exec pan.
	 *
	 * @param prog le programme lié au pseudo-code
	 */
	public ExecPan(Programme prog) {
		this.prog = prog;
		console = new JTextField();
		console.addActionListener( this );
		
		comms = Controleur.getControleur().getComms();
		
		pan = new JPanel();
		
		txt = new JLabel();
		jsp = new JScrollPane(txt);
		
		txtComm = new JLabel();
		jspComm = new JScrollPane(txtComm);
		
		paint();
		
		pan.setLayout(new GridLayout(1, 2));
		
		pan.add(jsp);
		pan.add(jspComm);
		
		this.setLayout(new BorderLayout());
		this.add(pan, BorderLayout.CENTER);
		this.add(console, BorderLayout.SOUTH);
	}
	
	/**
	 * Refresh du panel
	 */
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
		try {Thread.sleep(50);}catch(Exception e){}
		jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
		
		int ligneC = prog.getCourant().getLigneCourante();
		String concat = "";
		if(comms.containsKey(ligneC+1)) {
			String strComm = comms.get(ligneC+1);
			
			for( int i=0; i<strComm.length(); i++ )
			if(i%50==0) {
				concat += "\n" + strComm.charAt(i);
			}else {
				concat += strComm.charAt(i);
			}
		}

		txtComm.setText("<html><body style style=\"font-size: 15px\">");
		
		for(String str : concat.split("\n")) {
			txtComm.setText(txtComm.getText()+"<p>"+str+"</p>");
		}
		
		txtComm.setText(txtComm.getText()+"</body></html>");
		
		try {Thread.sleep(50);}catch(Exception e){}
		jspComm.getVerticalScrollBar().setValue(jspComm.getVerticalScrollBar().getMaximum());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed ( ActionEvent e )
	{
		Controleur.getControleur().envoyerCommande( console.getText() );
		console.setText( "" );
	}
}
