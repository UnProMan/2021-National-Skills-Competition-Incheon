package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Productlist extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p2 = get(new JPanel(new BorderLayout(5,5)));
	JPanel p3 =get(new JPanel(new FlowLayout(FlowLayout.RIGHT)), set(new EmptyBorder(20, 0, 0, 0)), set(0, 60));
	JPanel p4 = get(new JPanel(new GridLayout(0, 1)));
	
	JLabel img;
	
	JButton btn1 = get(new JButton("장바구니에 추가"), set(180, 30));
	
	ArrayList<String> list;
	
	public Productlist(ArrayList<String> list) {
		
		this.list = list;
		
		setLayout(new BorderLayout());
		setBorder(new LineBorder(Color.black));
		setBackground(Color.white);
		setPreferredSize(new Dimension(295, 190));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		img = getimg(list.get(1) + ".jpg", 80, 80, set(new LineBorder(Color.black)));
		
		add(p1);
		
		p1.add(p2);
		p1.add(p3, "South");
		
		p3.add(btn1);
		
		p2.add(img, "West");
		p2.add(p4);
		
		p4.add(new JLabel(list.get(1)));
		p4.add(new JLabel("가격 : " + list.get(2) + "원"));
		p4.add(new JLabel("재고 : " + list.get(3) + "개"));
		p4.add(new JLabel("행사상품 " + (list.get(4) == null ? "X" : "O")));
		
		btn1.setEnabled(list.get(3).contentEquals("0") ? false : true);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (data.indexOf(list) != -1) {
				err("이미 추가된 상품입니다.");
			}else {
				
				data.add(list);
				
				MainFrame.mf.find.buylist();
				
			}
			
		});
		
	}

}
