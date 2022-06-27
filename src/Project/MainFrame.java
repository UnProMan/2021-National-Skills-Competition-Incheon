package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class MainFrame extends JFrame implements Base{
	
	public static MainFrame mf;
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new LineBorder(Color.black)));
	JPanel p2 = get(new JPanel(new GridLayout(1, 3)), set(0, 60), set(new LineBorder(Color.black)));
	JPanel p3 = get(new JPanel(new BorderLayout()));
	
	BoxLayout box = new BoxLayout(p3, BoxLayout.Y_AXIS);
	
	JLabel lab1 = get(new JLabel("FN"), set(30), set(new EmptyBorder(0, 10, 0, 0)));
	JLabel lab2 = get(new JLabel("<html><center>&#x1F3E0<br>메인", 0), set(15));
	JLabel lab3 = get(new JLabel("<html><center>&#x1F50D<br>검색", 0), set(15));
	JLabel lab4 = get(new JLabel("<html><center>&#x1F464<br>마이페이지",0), set(15));
	
	JButton btn1 = get(new JButton("Login"), set(new EmptyBorder(0, 0, 0, 20)));
	
	String type = "";
	
	Find find;
	Buylist buy;
	
	public MainFrame() {
		
		SetFrame(this, "FN Mart", EXIT_ON_CLOSE, 1200, 800);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		p3.setLayout(box);
		
		add(p1, "North");
		add(p3);
		add(p2, "South");
		
		p1.add(lab1);
		p1.add(btn1, "East");
		
		p2.add(lab2);
		p2.add(lab3);
		p2.add(lab4);
		
		go(p3, new Main());
		
		reset();
		lab2.setForeground(Color.black);
		
		log();
		
	}
	
	public void reset() {
		lab2.setForeground(Color.gray);
		lab3.setForeground(Color.gray);
		lab4.setForeground(Color.gray);
	}
	
	public void log() {
		
		p2.removeAll();
		
		if (member.isEmpty()) {
			p2.add(lab2);
			p2.add(lab3);
			p2.add(lab4);
			go(p3, new Main());
		}else if (type.contentEquals("user")) {
			p2.add(lab2);
			p2.add(lab3);
			p2.add(lab4);
			go(p3, new Main());
		}else {
			p2.add(new JLabel(""));
			p2.add(lab2);
			p2.add(new JLabel(""));
			go(p3, new Seller());
		}
		
		reset();
		lab2.setForeground(Color.black);
		
		btn1.setText(member.isEmpty() ? "Login" : "Logout");
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		lab1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (type.contentEquals("user")) {
					go(p3, new Main());
				}else if (type.contentEquals("seller")) {
					go(p3, new Seller());
				}
				
			}
		});
		
		lab2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (member.isEmpty()) {
					err("로그인을 하지 않았습니다.");
				}else {
					
					if (type.contentEquals("user")) {
						go(p3, new Main());
					}else if (type.contentEquals("seller")) {
						go(p3, new Seller());
					}
					reset();
					lab2.setForeground(Color.black);

				}
				
			}
		});
		
		lab3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (member.isEmpty()) {
					err("로그인을 하지 않았습니다.");
				}else {
					go(p3, find = new Find());
					reset();
					lab3.setForeground(Color.black);
				}
				
			}
		});
		
		lab4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (member.isEmpty()) {
					err("로그인을 하지 않았습니다.");
				}else {
					go(p3, new MyPage());
					reset();
					lab4.setForeground(Color.black);
				}
				
			}
		});
		
		btn1.addActionListener(e->{
			
			if (member.isEmpty()) {
				go(p3, new Login());
			}else {
				type = "";
				member.clear();
				log();
			}
			
		});
		
	}
	public static void main(String[] args) {
		mf = new MainFrame();
	}
	
}
