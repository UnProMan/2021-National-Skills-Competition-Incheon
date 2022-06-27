package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class List extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p2 = get(new JPanel(new BorderLayout(5,5)));
	JPanel p3 =get(new JPanel(new FlowLayout(FlowLayout.RIGHT)), set(0, 80), set(new EmptyBorder(40, 0, 0, 0)));
	JPanel p4 = get(new JPanel(new GridLayout(0, 1)));
	
	JLabel img;
	
	JButton btn1 = get(new JButton(""));
	
	ArrayList<String> list;
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	String name;
	
	public List(String name,  ArrayList<String> list) {
		
		this.name = name;
		this.list = list;
		
		setPreferredSize(new Dimension(320, 220));
		setBackground(Color.white);
		setLayout(new BorderLayout());
		setBorder(new LineBorder(Color.black));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		img = getimg(list.get(0) + ".jpg", 100, 100, set(new LineBorder(Color.black)));
		
		if (name.contentEquals("판매자")) {
			
			if (list.get(6) == null) {
				btn1.setText("배달준비");
				btn1.setEnabled(false);
			}else {
				btn1.setText("배달 현황");
			}
			
		}else {
			btn1.setText("배달하기");
		}
		
		add(p1);
		
		p1.add(p2);
		p1.add(p3, "South");
		
		p3.add(btn1);
		
		p2.add(img, "West");
		p2.add(p4);
		
		p4.add(new JLabel(list.get(0)));
		p4.add(new JLabel(name + " : " + list.get(1)));
		p4.add(new JLabel("개수 : " + list.get(2) + "개"));
		p4.add(new JLabel("가격 : " + df.format(intnum(list.get(3))) + "원"));
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (btn1.getText().contentEquals("배달 현황")) {
				temp.add(list);
				new Receive(temp, 0);
			}else if (btn1.getText().contentEquals("배달하기")) {
				
				jop("배송을 시작합니다.");
				
				Updat("insert into receive values(null, ?,?);", list.get(4), LocalDateTime.now().toString());
				
				MainFrame.mf.buy.reset();
				
			}
			
		});
		
	}

}
