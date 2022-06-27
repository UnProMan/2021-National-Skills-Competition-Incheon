package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class UserUpdat extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()));
	JPanel p2 = get(new JPanel(new GridLayout(0, 1, 0, 80)), set(60, 0));
	JPanel p3 = get(new JPanel(new GridLayout(0, 1, 0, 80)));
	JPanel p4 = get(new JPanel(), set(new EmptyBorder(20, 0, 0, 0)));
	
	JTextField txt1 = get(new JTextField(), set(false));
	JPasswordField txt2 = get(new JPasswordField());
	JTextField txt3 = get(new JTextField());
	JComboBox com1 = get(new JComboBox<>());
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	JLabel lab1 = get(new JLabel("회원 정보 수정", 0), set(15));
	JButton btn1 = get(new JButton("수정"));
	
	public UserUpdat() {
		
		size(this, 350, 500);
		setLayout(new BorderLayout());
		setBackground(Color.white);
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		add(lab1, "North");
		add(p1);
		add(p4, "South");
		
		p1.add(p2, "West");
		p1.add(p3);
		
		p2.add(new JLabel("아이디 :"));
		p2.add(new JLabel("비밀번호 :"));
		p2.add(new JLabel("이름 :"));
		p2.add(new JLabel("주소 :"));
		
		p3.add(txt1);
		p3.add(txt2);
		p3.add(txt3);
		p3.add(com1);
		
		p4.add(btn1);
	
		txt1.setText(member.get(0).get(1));
		txt2.setText(member.get(0).get(2));
		txt3.setText(member.get(0).get(3));
		
		String st = "";
		Query("select user.u_No, concat(po_X, '-', po_Y), po_no from point left outer join user on user.u_Addr = point.po_No left outer join seller on seller.s_Addr = point.po_No where seller.s_Addr is null and user.u_Addr is null or user.u_No = ?;", list, member.get(0).get(0));
		
		for (int i = 0; i < list.size(); i++) {
			
			if (list.get(i).get(0) != null && list.get(i).get(0).contentEquals(member.get(0).get(0))) {
				st = list.get(i).get(0);
			}
			
			com1.addItem(list.get(i).get(1));
			
		}
		
		com1.setSelectedItem(st);
		
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
				txt3.requestFocus();
			}
		});
		
		btn1.addActionListener(e->{
			
			if (txt2.getText().isBlank() || txt3.getText().isBlank()) {
				err("빈칸이 있습니다.");
			}else if (txt2.getText().length() <= 3) {
				err("비밀번호는 3자리 이상으로 입력해주세요.");
			}else {
				
				jop("회원정보가 수정되었습니다.");
				
				Updat("update user set u_pattern = ?, u_name = ?, u_addr = ? where u_no =?;", txt2.getText(), txt3.getText(),list.get(com1.getSelectedIndex()).get(2), member.get(0).get(0));
				Query("select * from user where u_no = ?", member, member.get(0).get(0));
				
				go(MainFrame.mf.p3, new MyPage());
				
			}
			
		});	
		
	}

}
