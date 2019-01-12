import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GeneralGUIBuilding {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		frame.getContentPane().add(panel);
		
		JButton buttom = new JButton("Search");
		panel.add(buttom);
		
		JTextField textfield = new JTextField();
		textfield.setPreferredSize(new Dimension(200,15));
		panel.add(textfield);
		
		
		
		frame.setSize(new Dimension(500,400));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Beat Google");
		frame.setResizable(false);
		frame.setVisible(true);
		
		//it's so diffcult.
	}
	
}
