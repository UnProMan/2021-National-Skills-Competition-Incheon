package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Base.Base;

public class DayPanel extends JPanel implements Base{
	
	JLabel lab;
	
	Color lred = new Color(255, 102, 102);
	Color lblue = new Color(102, 102, 255);
	
	LocalDate now, date;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	Color color[] = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue};
	
	int count[] = new int[5];
	int max, c;
	
	public DayPanel(LocalDate now, LocalDate date) {

		this.now = now;
		this.date = date;
		
		setLayout(new FlowLayout(0));
		setBorder(new LineBorder(Color.black));
		setBackground(Color.white);
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		Query("select sum(pu_count) from purchase pu, product p where pu.p_No = p.p_No and pu.s_No = ? group by pu_date, p.c_No order by sum(pu_count) desc limit 1; ", list, member.get(0).get(0));
		max = intnum(list.get(0).get(0));
		
		c = date.getDayOfWeek().getValue();
		if (now.getMonthValue() == date.getMonthValue()) {
			lab = get(new JLabel(date.getDayOfMonth() + ""), setf(c == 7 ? Color.red : c == 6 ? Color.blue : Color.black));
		}else {
			lab = get(new JLabel(date.getDayOfMonth() + ""), setf(c == 7 ? lred : c == 6 ? lblue : Color.gray));
		}
		add(lab);
		
		for (int i = 0; i < color.length; i++) {
			Query("select sum(pu_count) from purchase pu, product p, category c where pu.p_No = p.p_No and c.c_No = p.c_No and c.c_No = ? and pu.s_No = ? and pu.pu_Date = ?;", list, (i + 1) + "", member.get(0).get(0), date.toString());
			count[i] = list.get(0).get(0) == null ? 0 : intnum(list.get(0).get(0));
		}
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (int i = 0; i < color.length; i++) {
			
			float a = (float) (Double.parseDouble(count[i] + "") / max);
			int h = (int) (a * 115);
			
			g.setColor(color[i]);
			g.fillRect(10 + i * 30, 115 - h, 20, h);
			
		}
		
	}
	
	@Override
	public void action() {
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new big();
			}
		});
		
	}

	public class big extends JDialog implements Base{

		JPanel pn = get(new JPanel(new BorderLayout(10, 5)), set(new LineBorder(Color.black)));

		JPanel p1;
		JPanel p2 = get(new JPanel(new GridLayout(0, 1)), set(100, 0));
		JPanel p3 = get(new JPanel());
		JPanel p4 = get(new JPanel(), set(20, 0));
		JPanel p;
		
		JLabel lab1 = get(new JLabel(date.toString() + " 매출 실적"), set(20));
		JLabel lab2 = get(new JLabel(date.getDayOfMonth() + ""), set(20), setf(c == 7 ? Color.red : c == 6 ? Color.blue : Color.black));
		JLabel lab;
		
		JButton btn1 = get(new JButton("OK"));
		
		String st[] = "음료, 생활용품, 아이스크림, 식품, 과자류".split(", ");
		
		public big() {
			
			SetDial(this, "", DISPOSE_ON_CLOSE, 1000, 600);
			design();
			action();
			setUndecorated(true);
			setVisible(true);
			
		}
		
		public void design() {
			
			add(pn);
			
			pn.add(lab1, "North");
			pn.add(p2, "East");
			pn.add(p3, "South");
			pn.add(p4,"West");
			
			p3.add(btn1);
			
			for (int i = 0; i < color.length; i++) {
				p2.add(p = get(new JPanel(new FlowLayout(0))));
				p.add(lab = get(new JLabel("■"),setf(color[i]), set(15)));
				p.add(new JLabel(st[i]));
			}
			
			pn.add(p = get(new JPanel(new BorderLayout()) {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					
					for (int i = 0; i < color.length; i++) {
						
						float a = (float) (Double.parseDouble(count[i] + "") / max);
						int h = (int) (550 * a);
						
						g.setColor(color[i]);
						g.fillRect(60 + 175 * i, 550 - h, 80, h);
						
						g.setColor(Color.black);
						g.setFont(new Font("", 1, 14));
						g.drawString(count[i] + "개", 85 + 175 * i, count[i] == 0 ? 520 : 540 - h);
						
					}
					
				}
			}, set(new LineBorder(Color.black))));
			
			p.add(lab2, "North");
			
		}

		@Override
		public void action() {
			
			btn1.addActionListener(e->{
				dispose();
			});
			
		}
		
	}
	
}
