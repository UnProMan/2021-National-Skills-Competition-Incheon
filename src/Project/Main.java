package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Base.Base;

public class Main extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new TitledBorder(new LineBorder(Color.black, 2), "인기 상품", 0, 2, new Font("맑은 고딕", 1, 20))));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(new TitledBorder(new LineBorder(Color.black, 2), "이달의 1+1 행사 상품", 0, 2, new Font("맑은 고딕", 1, 20))));
	
	JPanel np1 = get(new JPanel(new GridLayout(1, 5, 80, 0)), set(new EmptyBorder(30, 30, 30, 30)));
	JPanel np2 = get(new JPanel());
	
	JPanel sp1 = get(new JPanel(new GridLayout(1, 5, 80, 0)), set(new EmptyBorder(40, 30, 40, 30)));
	JPanel sp2 = get(new JPanel());
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	int page1 = 0, page2 = 0;
	
	JLabel lab1[] = new JLabel[3];
	JLabel lab2[];
	
	int x;
	
	public Main() {
		
		setLayout(new GridLayout(2, 1, 0, 10));
		setBackground(Color.white);
		setBorder(new EmptyBorder(20, 20, 20, 20));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		add(p1);
		add(p2);
		
		p1.add(np1);
		p1.add(np2, "South");
		
		p2.add(sp1);
		p2.add(sp2, "South");
		
		인기상품();
		행사상품();
		
	}
	
	public void 인기상품() {
		
		np1.removeAll();
		np2.removeAll();
		
		Query("select p.p_Name, sum(pu_count) from purchase pu, product p where pu.p_No = p.p_No group by pu.p_No order by sum(pu_count) desc limit 15;", list);
		
		for (int i = 0; i < lab1.length; i++) {
			np2.add(lab1[i] = get(new JLabel("●"), set(15), setf(i == page1 ? Color.red : Color.black)));
		}
		
		for (int i = page1 * 5; i < (page1 + 1) * 5; i++) {
			
			if (i >= list.size()) {
					np1.add(new JLabel(""));
			}else {
				
				JPanel p = get(new JPanel(new BorderLayout()));
				JLabel img = getimg(list.get(i).get(0) + ".jpg", 120, 120, set(new LineBorder(Color.black)));
				
				p.add(new JLabel((i + 1) + "위"), "North");
				p.add(img);
				p.add(new JLabel(list.get(i).get(0), 0), "South");
				
				np1.add(p);

			}
			
		}
		
		revalidate();
		repaint();
		
	}
	
	public void 행사상품() {
		
		sp1.removeAll();
		sp2.removeAll();
		
		Query("select p.p_name from event e, product p where e.p_No = p.p_No and e_month = month(now());", temp);
		
		lab2 = new JLabel[temp.size()/5 + (temp.size()%5 == 0 ? 0 : 1)];
		for (int i = 0; i < lab2.length; i++) {
			sp2.add(lab2[i] = get(new JLabel("●"), set(15), setf(i == page2 ? Color.red : Color.black)));
		}
		
		for (int i = page2 * 5; i < (page2 + 1) * 5; i++) {
			
			if (i >= temp.size()) {
				sp1.add(new JLabel(""));
			}else {
				
				JPanel pn1 = get(new JPanel(new BorderLayout()));
				JLabel img = getimg(temp.get(i).get(0) + ".jpg", 120, 120, set(new LineBorder(Color.black)));
				
				pn1.add(img);
				pn1.add(new JLabel(temp.get(i).get(0)), "South");
				
				sp1.add(pn1);
				
			}
			
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		p1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				x = e.getX();
			}
			
			public void mouseReleased(MouseEvent e) {
				
				if (e.getX() > x) {
					page1 = page1 == 0 ? 0 : page1 - 1;
				}else {
					page1 = page1 == 2 ? 2 : page1 + 1;
				}
				
				인기상품();
				
			}
			
		});
		
		p2.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				x = e.getX();
			}
			
			public void mouseReleased(MouseEvent e) {
				
				if (e.getX() > x) {
					page2 = page2 == 0 ? 0 : page2 - 1;
				}else {
					page2 = page2 == lab2.length - 1 ? lab2.length - 1 : page2 + 1;
				}
				
				행사상품();
				
			}
			
		});
		
	}

}
