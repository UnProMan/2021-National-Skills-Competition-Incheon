package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Base.Base;

public class Buylist extends JPanel implements Base{
	
	JLabel lab1 = get(new JLabel("", 0), set(25));
	
	JPanel p1 = get(new JPanel(new FlowLayout(0, 5, 5)));
	JScrollPane scl = get(new JScrollPane(p1, 22, 31));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	int type;
	int value = 0, v= -1;
	
	public Buylist(int type) {
		
		this.type = type;
		
		size(this, 1000, 500);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		design();
		action();
		
	}

	@Override
	public void design() {

		if (type == 0) {
			lab1.setText(member.get(0).get(3) + "의 구매목록");
		}else {
			lab1.setText(member.get(0).get(3) + "의 물품배송관리");
		}
		
		add(lab1, "North");
		add(scl);
		
		make();
		
	}
	
	public void make() {
		
		if (type == 0) {
			Query("select p.p_Name , s.s_Name, pu.pu_Count, pu.pu_price ,s.s_No, pu.pu_No, rc.r_Time from purchase pu left join receive rc on rc.pu_No = pu.pu_No, seller s, product p where pu.s_No = s.s_No and pu.p_No = p.p_No and pu.u_No = ?;", list, member.get(0).get(0));
		}else {
			Query("select p.p_Name, u.u_Name, pu.pu_Count, pu.pu_price, pu.pu_No from purchase pu left join receive rc on rc.pu_No = pu.pu_No, user u, seller s, product p where rc.pu_No is null and pu.p_No = p.p_No and pu.u_No = u.u_No and s.s_No = pu.s_No and s.s_No = ?;", list, member.get(0).get(0));
		}
		
		p1.setPreferredSize(new Dimension(0, (value + 1) * 480));
		
		for (int i = value * 6; i < (value + 1) * 6; i++) {
			
			if (i >= list.size()) {
				return;
			}
			
			if (type == 0) {
				p1.add(new List("판매자", list.get(i)));
			}else {
				p1.add(new List("구매자", list.get(i)));
			}
			
		}
		
		revalidate();
		repaint();
		
	}

	public void reset() {
		
		p1.removeAll();
		value = 0;
		v = 0;
		
		make();
		
	}
	
	@Override
	public void action() {
		
		scl.getVerticalScrollBar().addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (scl.getVerticalScrollBar().getModel().getExtent() + scl.getVerticalScrollBar().getModel().getValue() >= scl.getVerticalScrollBar().getModel().getMaximum()) {
					v = value;
				}
			}
		});
		
		scl.getVerticalScrollBar().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				v = value;
			}
		});
		
		scl.getVerticalScrollBar().getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				if (list.size() / 6 <= value) {
					return;
				}
				
				if (scl.getVerticalScrollBar().getModel().getExtent() + scl.getVerticalScrollBar().getModel().getValue() >= scl.getVerticalScrollBar().getModel().getMaximum()) {
					value++;
					if (value == v + 1) {
						make();
					}else {
						value = v + 1;
					}
				}
				
			}
		});
		
	}

}
