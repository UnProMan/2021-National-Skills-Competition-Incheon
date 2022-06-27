package Project;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.net.NoRouteToHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Receive extends JDialog implements Base{

	JPanel p1;
	JPanel p2 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(50, 0, 0, 20)), set(250, 0));
	JPanel p3 = get(new JPanel(new GridLayout(0, 1)), setb(Color.yellow), set(new LineBorder(Color.black)), set(200, 220));
	
	String st[] = "○ 출발지점, ○ 도착지점, ○ 최단거리, ○ 소요시간, ○ 배송시간, ○ 도착시간".split(", ");
	
	JLabel lab1 = get(new JLabel("", 0),set(15));
	JLabel lab;
	
	int sno, sx, sy;
	int uno[], ux[], uy[];
	
	JRadioButton rdo[] = new JRadioButton[70];
	int x[] = new int[70], y[] = new int[70];
	
	int cindex = 0;
	int type = 0;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> purchase = new ArrayList<>();
	ArrayList<ArrayList<String>> connect = new ArrayList<>();
	
	ArrayList<ArrayList<Integer>> street = new ArrayList<>();
	ArrayList<ArrayList<Color>> color = new ArrayList<>();
	ArrayList<ArrayList<Integer>> time = new ArrayList<>();
	
	LocalDateTime date;
	
	DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public Receive(ArrayList<ArrayList<String>> purchase, int type) {
		
		this.purchase = purchase;
		this.type = type;
		
		SetDial(this, "배송 현황", DISPOSE_ON_CLOSE, 1200, 750);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		Route.rt = new Route();
		
		if (type == 0) {
			add(p2, "East");
			p2.add(p3, "North");
			lab1.setText("[" + purchase.get(0).get(1) + "] 물품 배달 현황");
		}else {
			lab1.setText("[" + member.get(0).get(3) + "] 물품 배달 현황");
			setSize(1000, 750);
			Query("select p.p_Name , u.u_Name, pu.pu_Count, pu.pu_price,u.u_No, pu.pu_No, rc.r_Time from purchase pu left join receive rc on rc.pu_No = pu.pu_No, user u, product p where pu.u_No = u.u_No and pu.p_No = p.p_No and pu.s_No = ? and left(r_time, 10) = ?", purchase, member.get(0).get(0), LocalDate.now().toString());
		}
		
		add(lab1, "North");
		lab1.setOpaque(true);
		
		add(p1 = new JPanel(null) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				this.setBackground(Color.white);
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(3));
				
				g2.setColor(Color.LIGHT_GRAY);
				
				Query("select * from connect", connect);
				
				for (int i = 0; i < connect.size(); i++) {
					g2.drawLine(x[intnum(connect.get(i).get(0)) - 1], y[intnum(connect.get(i).get(0)) - 1], x[intnum(connect.get(i).get(1)) - 1], y[intnum(connect.get(i).get(1)) - 1]);
				}
				
				try {
					
					for (int i = 0; i < color.size(); i++) {
						for (int j = 0; j < color.get(i).size(); j++) {
							g2.setColor(color.get(i).get(j));
							g2.drawLine(x[street.get(i).get(j)], y[street.get(i).get(j)], x[street.get(i).get(j + 1)], y[street.get(i).get(j + 1)]);
						}
					}
					
				} catch (Exception e) {
				}
				
			}
		});
		
		Query("select p.* from point p, seller s where p.po_No = s.s_Addr and s.s_No = ?", list, type == 0 ? purchase.get(0).get(4) : member.get(0).get(0));
		
		sno = intnum(list.get(0).get(0));
		sx = intnum(list.get(0).get(1));
		sy = intnum(list.get(0).get(2));
		
		uno = new int[purchase.size()];
		ux = new int[purchase.size()];
		uy = new int[purchase.size()];
		
		for (int i = 0; i < purchase.size(); i++) {
			
			Query("select p.* from point p, user u where u.u_Addr = p.po_No and u.u_No = ?", list, type == 0 ? member.get(0).get(0) : purchase.get(i).get(4));
			
			uno[i] = intnum(list.get(0).get(0));
			ux[i] = intnum(list.get(0).get(1));
			uy[i] = intnum(list.get(0).get(2));
			
		}
		
		Query("SELECT * FROM delivery.point;", list);
		
		for (int i = 0; i < list.size(); i++) {
			
			p1.add(rdo[i] = get(new JRadioButton(list.get(i).get(1) + "-" + list.get(i).get(2)), set(false)));
			rdo[i].setBounds(intnum(list.get(i).get(1)) - 35, intnum(list.get(i).get(2)) - 15, 70, 50);
			x[i] = rdo[i].getX() + 24;
			y[i] = rdo[i].getY() + 16;
			rdo[i].setVerticalTextPosition(3);
			rdo[i].setHorizontalTextPosition(0);
			rdo[i].setOpaque(false);
			
			if (sno == intnum(list.get(i).get(0))) {
				rdo[i].setEnabled(true);
				rdo[i].setSelected(true);
				p1.add(lab = get(new JLabel("출발")));
				lab.setBounds(x[i] - 15, y[i] - 35, 40, 20);
			}
			
			for (int j = 0; j < purchase.size(); j++) {
				if (uno[j] == intnum(list.get(i).get(0))) {
					rdo[i].setEnabled(true);
					rdo[i].setSelected(true);
					p1.add(lab = get(new JLabel("도착")));
					lab.setBounds(x[i] - 15, y[i] - 35, 40, 20);
				}
			}
			
		}
		
		길찾기();
		
		p3.add(new JLabel(st[0] + " " + sx + "-" + sy));
		p3.add(new JLabel(st[1] + " " + ux[0] + "-" + uy[0]));
		
		int sum = time.get(0).get(time.get(0).size() - 1);
		LocalTime tt = LocalTime.of(0, 0, 0).plusSeconds(sum / 10);
		
		p3.add(new JLabel(st[2] + " " + sum + "m"));
		p3.add(new JLabel(st[3] + " " + tt));
		p3.add(new JLabel(st[4] + " " + date.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
		p3.add(new JLabel(st[5] + " " + date.plusSeconds(sum / 10).format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
		
		new Thread(() ->{
			try {
				
				while (true) {
					
					for (int i = 0; i < color.size(); i++) {
						for (int j = 0; j < color.get(i).size(); j++) {
							
							if (time.get(i).get(j)/10 > cindex/2) {
								color.get(i).set(j, color.get(i).get(j).equals(Color.red) ? Color.blue : Color.red);
								break;
							}else {
								color.get(i).set(j, Color.blue);
							}
							
						}
					}
					
					Thread.sleep(500);
					
					cindex++;
					repaint();
					revalidate();
					
				}
				
			} catch (Exception e) {
			}
		}).start();
		
	}
	
	public void 길찾기() {
		
		for (int i = 0; i < purchase.size(); i++) {
			
			Route.rt.start(sno -1, uno[i] - 1);
			date = LocalDateTime.of(LocalDate.parse(purchase.get(i).get(6).split(" ")[0]), LocalTime.parse(purchase.get(i).get(6).split(" ")[1]));
			
			ArrayList<Color> c = new ArrayList<>();
			ArrayList<Integer> t = new ArrayList<>();
			
			for (int j = 0; j < Route.rt.data.size(); j++) {
				
				rdo[Route.rt.data.get(j)].setEnabled(true);
				rdo[Route.rt.data.get(j)].setSelected(true);
				rdo[Route.rt.data.get(j)].setToolTipText(date.format(dt));
				
			}
			
			for (int j = 0; j < Route.rt.data.size() - 1; j++) {
				
				c.add(Color.red);
				t.add(Route.rt.distance[Route.rt.data.get(j + 1)]);
				
				rdo[Route.rt.data.get(j + 1)].setToolTipText(date.plusSeconds(t.get(t.size() - 1)/10).format(dt));
				
			}
			
			time.add(t);
			color.add(c);
			street.add(Route.rt.data);
			
		}
		
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
