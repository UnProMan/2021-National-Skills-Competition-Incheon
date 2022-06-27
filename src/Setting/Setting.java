package Setting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Setting extends JFrame{

	JPanel p1 = new JPanel(new BorderLayout());
	
	Icon jop = UIManager.getIcon("OptionPane.informationIcon");
	Icon err = UIManager.getIcon("OptionPane.errorIcon");
	
	Connection c;
	Statement s;
	
	Vector v1;
	Vector v2 = new Vector(Arrays.asList("상태, 이름".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
		public java.lang.Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	public Setting() {
	
		setTitle("db 초기화");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
		
		add(p1);
		p1.add(scl);
		
		p1.setBorder(new TitledBorder("Log"));
		
		tbl.setRowHeight(50);
		tbl.setShowGrid(false);
		tbl.setOpaque(false);
		
		tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				if (value instanceof JLabel) {
					return (JLabel)value;
				}
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		
		setVisible(true);
		
		new Thread(()->{
			set();
		}).start();
		
	}
	
	public void up(String name, boolean tf) {
		
		JLabel lab1 = new JLabel(tf ? "성공" : "실패", tf ? jop : err, 0);
		JLabel lab2 = new JLabel(name);
		lab1.setForeground(tf ? Color.blue : Color.red);
		lab2.setForeground(lab1.getForeground());
		
		lab1.setFont(new Font("", 0, 11));
		lab2.setFont(new Font("", 0, 11));
		
		model.addRow(new Object[] {lab1, lab2});
		
		try {
			Thread.sleep(100);
			scl.getVerticalScrollBar().setValue(scl.getVerticalScrollBar().getMaximum());
		} catch (Exception e) {
		}
		
	}
	
	public void dis(boolean tf) {
		
		up(tf ? "<html><b>DB 구성 성공</b>" : "<html><b>DB 구성 실패</b>", tf);
		
		try {
			
			for (int i = 0; i < 5; i++) {
				
				up((tf ? "<html><b>DB구성 성공, " : "<html><b>DB구성 실패, ") + (5 - i) + "초 후 종료됩니다.</b>", tf);
				
				Thread.sleep(1000);
				
			}
			
			System.exit(0);
			
		} catch (Exception e) {
		}
		
	}
	
	public void sql(String name ,String sql) {
		
		try {
			
			s.executeUpdate(sql);
			up(name, true);
			
		} catch (Exception e) {
			up(name, false);
			dis(false);
		}
		
	}
	
	public void create(String name, String sql) {
		sql(name + " Table 생성", sql);
	}
	
	public void reset(String name) {
		sql(name + " Table 초기화", "load data local infile '지급자료/" + name + ".txt' into table " + name + " lines terminated by '\r\n' ignore 1 lines;");
	}
	
	public void set() {
		
		try {
			
			c = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			s = c.createStatement();
			
			sql("DB 삭제", "drop database if exists Delivery;");
			sql("DB 생성", "create database if not exists Delivery;");
			s.executeUpdate("use Delivery;");
			sql("user 삭제", "drop user if exists user@'localhost';");
			sql("user 생성", "create user if not exists user@'localhost' identified by '1234';");
			sql("user 권한 설정", "grant select, update, insert, delete on Delivery.* to user@'localhost';");
			s.executeUpdate("set global local_infile = 1;");
			
			create("point", "create table point(po_No int primary key auto_increment, po_X int, po_Y int);");
			reset("point");
			
			create("connect", "create table connect(c_Node1 int, c_Node2 int, foreign key(c_Node1) references point(po_No), foreign key(c_Node2) references point(po_No));");
			reset("connect");
			
			create("user", "create table user(u_No int primary key auto_increment, u_Id varchar(20), u_Pattern varchar(10), u_Name varchar(20), u_Addr int, foreign key(u_Addr) references point(po_No));");
			reset("user");
			
			create("seller", "create table seller(s_No int primary key auto_increment, s_Id varchar(20), s_Pattern varchar(10), s_Name varchar(20), s_Addr int, foreign key(s_Addr) references point(po_No));");
			reset("seller");
			
			create("category", "create table category(c_No int primary key auto_increment, c_Name varchar(20));");
			reset("category");
			
			create("product", "create table product(p_No int primary key auto_increment, c_No int, p_Name varchar(50), p_Price int, foreign key(c_No) references category(c_No));");
			reset("product");
			
			create("stock", "create table stock(st_No int primary key auto_increment, s_No int, p_No int, st_Count int, foreign key(s_No) references seller(s_No), foreign key(p_No) references product(p_No));");
			reset("stock");
			
			create("event", "create table event(e_No int primary key auto_increment, p_No int, e_Month int, foreign key(p_No) references product(p_No));");
			reset("event");
			
			create("purchase", "create table purchase(pu_No int primary key auto_increment, s_No int, p_No int, u_No int, pu_Count int,pu_price int, pu_Date date, foreign key(s_No) references seller(s_No), foreign key(p_No) references product(p_No), foreign key(u_No) references user(u_No));");
			reset("purchase");
			
			create("receive", "create table receive(r_No int primary key auto_increment, pu_No int, r_Time datetime, foreign key(pu_No) references purchase(pu_No));");
			reset("receive");
			
			dis(true);
			
		} catch (Exception e) {
			dis(false);
		}
		
	}
	public static void main(String[] args) {
		new Setting();
	}
}
