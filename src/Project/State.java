package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Base.Base;

public class State extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel());
	JPanel p2 = get(new JPanel(new GridLayout(0, 7)));
	
	JButton btn1 = get(new JButton("◀"));
	JButton btn2 = get(new JButton("▶"));
	
	JLabel lab1 = get(new JLabel("", 0), set(15), set(120, 30));
	
	LocalDate now = LocalDate.now();
	LocalDate date;
	
	public State() {
	
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		now = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
		
		add(p1, "North");
		add(p2);
		
		p1.add(btn1);
		p1.add(lab1);
		p1.add(btn2);
		
		cal();
		
	}
	
	public void cal() {
		
		p2.removeAll();
		
		int val = now.withDayOfMonth(1).getDayOfWeek().getValue();
		
		lab1.setText(now.format(DateTimeFormatter.ofPattern("yyyy년 MM월")));
		
		date = now.plusDays(-val);
		
		for (int i = 0; i < 42; i++) {
			p2.add(new DayPanel(now, date.plusDays(i)));
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			now = now.plusMonths(-1);
			cal();
		});
		
		btn2.addActionListener(e->{
			now = now.plusMonths(1);
			cal();
		});
		
	}

}
