package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Base.Base;

public class Login extends JPanel implements Base{
	
	JButton btn1 = get(new JButton("구매자로 로그인"));
	JButton btn2 = get(new JButton("판매자로 로그인"));
	JButton btn3 = get(new JButton("구매자로 로그인"));
	
	JTextField txt1 = get(new JTextField());
	JPasswordField txt2 = get(new JPasswordField());
	
	JPanel p1 = get(new JPanel(new GridLayout(2, 1, 0, 5)));
	JPanel p2 = get(new JPanel(new GridLayout(2, 1, 0, 5)));
	
	public Login() {
		
		size(this, 300, 150);
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
	
	public void next() {
		
		removeAll();
		setLayout(new BorderLayout(5, 5));
		
		add(p1, "West");
		add(p2);
		add(btn3, "South");
		
		p1.add(new JLabel("아이디"));
		p1.add(new JLabel("비밀번호"));
		
		p2.add(txt1);
		p2.add(txt2);
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		txt2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new Pattern(txt2);
			}
		});
		
		txt2.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				txt1.requestFocus();
			}
		});
		
		btn1.addActionListener(e->{
			next();
			btn3.setText(btn1.getText());
		});
		
		btn2.addActionListener(e->{
			next();
			btn3.setText(btn2.getText());
		});
		
		btn3.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank()) {
				err("빈칸이 있습니다.");
			}else if (txt2.getText().length() < 3) {
				err("비밀번호는 3자리 이상으로 입력해주세요.");
			}else {
				
				if (btn3.getText().contentEquals("구매자로 로그인")) {
					Query("select * from user where u_id = ? and u_pattern = ?;", member, txt1.getText(), txt2.getText());
				}else {
					Query("select * from seller where s_id = ? and s_pattern = ?;", member, txt1.getText(), txt2.getText());
				}
				
				if (member.isEmpty()) {
					err("아이디나 비밀번호를 다시 확인해주세요.");
				}else {
					
					jop(member.get(0).get(3) + (btn3.getText().contentEquals("구매자로 로그인") ? " 회원님, 환영합니다." : " 판매자님, 환영합니다."));
					MainFrame.mf.type = btn3.getText().contentEquals("구매자로 로그인") ? "user" : "seller";
					
					MainFrame.mf.log();
					
				}
				
			}
			
		});
		
	}

}
