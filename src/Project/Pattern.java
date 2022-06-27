package Project;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import Base.Base;

public class Pattern extends JDialog implements Base{

	JPanel p1 = get(new JPanel(new GridLayout(0, 3)));
	JPanel p2 = get(new JPanel());
	
	JButton btn1 = new JButton("확인");
	
	JPanel p[] = new JPanel[9];
	JLabel lab[] = new JLabel[9];
	int x[] = new int[9], y[] = new int[9];
	
	int startx, starty, finx, finy;
	
	JTextField txt;
	String number = "";
	
	boolean stop = false;
	boolean click = false;
	
	public Pattern(JTextField txt) {
		
		this.txt = txt;
		
		SetDial(this, "패턴", DISPOSE_ON_CLOSE, 400, 400);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		add(p1);
		add(p2, "South");
		
		p2.add(btn1);
		
		for (int i = 0; i < lab.length; i++) {
			p1.add(p[i] = get(new JPanel(new GridBagLayout()), set(new LineBorder(Color.gray))));
			p[i].add(lab[i] = get(new JLabel("●"), set(20)));
		}
		
	}
	@Override
	public void action() {
		
		for (int i = 0; i < lab.length; i++) {
			
			int n = i;
			
			lab[n].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					
					click = true;
					stop = false;
					
					startx = lab[n].getX() + p[n].getX() + 15;
					starty = lab[n].getY() + p[n].getY() + 45;
					
					x[0] = startx;
					y[0] = starty;
					
					number = (n + 1) + "";
					
				}
				public void mouseEntered(MouseEvent e) {
					
					if (number.contains((n + 1) + "") || stop) {
						return;
					}
					
					x[number.length()] = lab[n].getX() + p[n].getX() + 15;
					y[number.length()] = lab[n].getY() + p[n].getY() + 45;
					
					repaint();
					
					number += (n + 1) + "";
					
				}
				public void mouseReleased(MouseEvent e) {
					stop = true;
					repaint();
				}
			});
			
			lab[i].addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					
					if (click) {
						
						finx = e.getX() + lab[n].getX() + p[n].getX() + 15;
						finy = e.getY() + lab[n].getY() + p[n].getY() + 45;
						
						repaint();
						
					}
					
				}
			});
			
		}
		
		btn1.addActionListener(e->{
			txt.setText(number);
			dispose();
		});
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.blue);
		g2.setStroke(new BasicStroke(4));
		
		for (int i = 0; i < number.length() - 1; i++) {
			g2.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
		}
		
		if (stop) {
			return;
		}
		
		g2.drawLine(startx, starty, finx, finy);
		
		if (number.length() != 0) {
			startx = x[number.length() - 1];
			starty = y[number.length() - 1];
		}
		
	}

}
