package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Base.Base;

public class Find extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()));
	JPanel p2 = get(new JPanel(new BorderLayout(10, 5)));
	JPanel p3 = get(new JPanel(new GridLayout(1, 3)));
	JPanel p4 = get(new JPanel());
	JPanel p5 = get(new JPanel());
	JPanel p6 = get(new JPanel());
	JPanel p7 = get(new JPanel(new FlowLayout(0, 2, 2)));
	JPanel p8 = get(new JPanel(new BorderLayout()), set(250,0));
	JPanel p9 = get(new JPanel(new FlowLayout(0, 5, 5)));
	
	JScrollPane scl1 = get(new JScrollPane(p7, 20, 31), set(new LineBorder(Color.black)));
	JScrollPane scl2 = get(new JScrollPane(p9, 20 ,31), set(new LineBorder(Color.black)));
	
	JLabel lab1 = get(new JLabel("장바구니", 0), set(15));
	JTextField txt1 = get(new JTextField());
	
	JButton btn1 = get(new JButton("<html>&#x1F50D"));
	JButton btn2 = get(new JButton("구매하기"));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	JComboBox com1 =get(new JComboBox<>());
	JComboBox com2 =get(new JComboBox<>());
	JCheckBox ck1 = get(new JCheckBox(""));
	
	int value = 0;
	int v= 0;
	
	public Find() {
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(Color.white);
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		com1.addItem("");
		com2.addItem("전체");
		
		Query("SELECT * FROM delivery.seller;", list);
		for (ArrayList<String> list : list) {
			com1.addItem(list.get(3));
		}
		
		Query("SELECT * FROM delivery.category;", list);
		for (ArrayList<String> list : list) {
			com2.addItem(list.get(1));
		}
		
		add(p1, "North");
		add(p2);
		
		p2.add(p3, "North");
		p2.add(scl1);
		p2.add(p8, "East");
		
		p3.add(p4);
		p3.add(p5);
		p3.add(p6);
		
		p1.add(txt1);
		p1.add(btn1, "East");
		
		p4.add(new JLabel("영업정명"));
		p4.add(com1);
		
		p5.add(new JLabel("카테고리"));
		p5.add(com2);
		
		p6.add(new JLabel("행사 여부"));
		p6.add(ck1);
		
		p8.add(lab1, "North");
		p8.add(scl2);
		p8.add(btn2, "South");
		
	}
	
	public void find() {
		
		p7.removeAll();
		value = 0;
		v = 0;
		scl1.getVerticalScrollBar().setValue(0);
		
		String category = com2.getSelectedIndex() == 0 ? "" : " and p.c_no = " + com2.getSelectedIndex();
		
		Query("select p.p_No, p.p_Name, p.p_Price, s.st_Count, (select event.e_No from event where event.p_No = p.p_No and event.e_Month = month(now())), s.s_no from product p, stock s, category c where p.c_No = c.c_No and s.p_No = p.p_No and s.s_No = ? and p.p_Name like ?" + category, list, com1.getSelectedIndex() + "", "%" + txt1.getText() + "%");
			
		scroll();
		
	}
	
	public void scroll() {
		
		for (int i = value * 9; i < (value +1) * 9; i++) {
			
			if (i >= list.size()) {
				return;
			}
			
			if (ck1.isSelected() && list.get(i).get(4) != null) {
				p7.add(new Productlist(list.get(i)));
			}else if (!ck1.isSelected()) {
				p7.add(new Productlist(list.get(i)));
			}
			
		}
		
		p7.setPreferredSize(new Dimension(950, (value + 1) * 600));
		
		revalidate();
		repaint();
		
	}
	
	public void buylist() {
		
		p9.removeAll();
		p9.setPreferredSize(new Dimension(0, data.size() * 45));
		
		int height = 0;
		for (int i = 0; i < data.size(); i++) {
			
			JPanel pn1 = get(new JPanel(new BorderLayout(5, 5)), set(200, 40), set(new LineBorder(Color.black)));
			JLabel img = getimg(data.get(i).get(1) + ".jpg", 40, 40, set(new LineBorder(Color.black)));
			JButton btn = get(new JButton("X"), set(new LineBorder(Color.black)), set(30, 0));
			
			pn1.add(img, "West");
			pn1.add(new JLabel(data.get(i).get(1)));
			pn1.add(btn, "East");
			
			int j = i;
			btn.addActionListener(e->{
				data.remove(j);
				buylist();
			});
			
			p9.add(pn1);
			height += pn1.getPreferredSize().height;
			
		}
		
		p9.setPreferredSize(new Dimension(0, height + 150));
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (com1.getSelectedIndex() == 0) {
				com1.setSelectedIndex(1);
			}
			
			find();
		});
		
		com1.addActionListener(e->{
			
			com2.setSelectedIndex(0);
			txt1.setText("");
			ck1.setSelected(false);
			data.clear();
			
			find();
			
		});
		
		btn2.addActionListener(e->{
			
			if (data.isEmpty()) {
				err("장바구니가 비어있습니다.");
			}else {
				go(MainFrame.mf.p3, new Pay());
			}
			
		});
		
		scl1.getVerticalScrollBar().addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (scl1.getVerticalScrollBar().getModel().getExtent() + scl1.getVerticalScrollBar().getModel().getValue() >= scl1.getVerticalScrollBar().getModel().getMaximum()) {
					v = value;
				}
			}
		});
		
		scl1.getVerticalScrollBar().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				v = value;
			}
		});
		
		scl1.getVerticalScrollBar().getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				if (list.size() / 9 <= value) {
					return;
				}
				
				if (scl1.getVerticalScrollBar().getModel().getExtent() + scl1.getVerticalScrollBar().getModel().getValue() >= scl1.getVerticalScrollBar().getModel().getMaximum()) {
					value++;
					if (value == v + 1) {
						scroll();
					}else {
						value = v + 1;
					}
				}
				
			}
		});
		
	}

}
