package Project;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import Base.Base;

public class Seller extends JPanel implements Base{
	
	JButton btn1 = get(new JButton("배송관리"));
	JButton btn2 = get(new JButton("배송현황"));
	JButton btn3 = get(new JButton("통계"));
	
	public Seller() {
		
		size(this, 500, 150);
		setLayout(new GridLayout(1, 3, 5, 5));
		setBackground(Color.white);
		
		design();
		action();
		
	}

	@Override
	public void design() {

		add(btn1);
		add(btn2);
		add(btn3);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			go(MainFrame.mf.p3, MainFrame.mf.buy = new Buylist(1));
		});
		
		btn2.addActionListener(e->{
			new Receive(new ArrayList<>(), 1);
		});
		
		btn3.addActionListener(e->{
			go(MainFrame.mf.p3, new State());
		});
		
	}

}
