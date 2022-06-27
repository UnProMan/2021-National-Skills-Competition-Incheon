package Base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public interface Base {
	
	public void design();
	public void action();
	
	ArrayList<ArrayList<String>> member = new ArrayList<>();
	ArrayList<ArrayList<String>> data = new ArrayList<>();
	
	DecimalFormat df = new DecimalFormat("#,##0");
	
	default void SetFrame(JFrame f, String title, int ex, int x, int y) {
		f.setTitle(title);
		f.setDefaultCloseOperation(ex);
		f.setSize(x, y);
		f.setLocationRelativeTo(null);
	}
	
	default void SetDial(JDialog d, String title, int ex, int x, int y) {
		d.setTitle(title);
		d.setDefaultCloseOperation(ex);
		d.setSize(x, y);
		d.setModal(true);
		d.setLocationRelativeTo(null);
	}
	
	default <Any> Any get(JComponent comp, Set...sets) {
		
		comp.setBackground(Color.white);
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return (Any) comp;
		
	}
	
	default Set set(boolean tf) {
		return c->c.setEnabled(tf);
	}
	
	default Set set(Border border) {
		return c->c.setBorder(border);
	}
	
	default Set set(int x, int y) {
		return c->c.setPreferredSize(new Dimension(x, y));
	}
	
	default Set setf(Color color) {
		return c->c.setForeground(color);
	}
	
	default Set setb(Color color) {
		return c->c.setBackground(color);
	}
	
	default Set set(int font) {
		return c->c.setFont(new Font("맑은 고딕", 1, font));
	}
	
	default String file(String file) {
		return "지급자료/image/" + file;
	}
	
	default Integer intnum(String txt) {
		return Integer.parseInt(txt);
	}
	
	default JLabel getimg(String file, int x, int y, Set...sets) {
		
		JLabel comp = new JLabel(new ImageIcon(new ImageIcon(file(file)).getImage().getScaledInstance(x, y, 4)));
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return comp;
		
	}
	
	default void size(JPanel p1, int x, int y) {
		p1.setMaximumSize(new Dimension(x, y));
		p1.setMinimumSize(new Dimension(x, y));
		p1.setPreferredSize(new Dimension(x, y));
	}
	
	default void go(JPanel p1, JPanel p2) {
		
		p1.removeAll();
		
		p1.add(Box.createVerticalGlue());
		p1.add(p2);
		p1.add(Box.createVerticalGlue());
		
		p1.revalidate();
		p1.repaint();
		
	}
	
	default void jop(String txt) {
		JOptionPane.showMessageDialog(null, txt, "정보", JOptionPane.INFORMATION_MESSAGE);
	}

	default void err(String txt) {
		JOptionPane.showMessageDialog(null, txt, "경고", JOptionPane.ERROR_MESSAGE);
	}
	
	default void Query(String sql, ArrayList<ArrayList<String>> list, String...v) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/Delivery?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			list.clear();
			ResultSet rs = s.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			
			while (rs.next()) {
				ArrayList row = new ArrayList<>();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				list.add(row);
			}
			
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	default void Updat(String sql, String...v) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/Delivery?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			s.executeUpdate();
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}
