package Project;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import Base.Base;

public class MyPage extends JPanel implements Base{
	
	JButton btn1 = get(new JButton("구매내역"));
	JButton btn2 = get(new JButton("정보수정"));
	
	public MyPage() {
		
		size(this, 250, 100);
		setLayout(new GridLayout(1, 2, 5, 0));
		setBackground(Color.white);
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		add(btn1);
		add(btn2);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			go(MainFrame.mf.p3, new Buylist(0));
		});
		
		btn2.addActionListener(e->{
			go(MainFrame.mf.p3, new UserUpdat());
		});
		
	}

}
