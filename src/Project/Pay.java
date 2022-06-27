package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Pay extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new GridLayout(2, 1)));
	JPanel p2 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	
	JTextField txt1 =new JTextField(10);
	JButton btn1 = get(new JButton("구매하기"), set(new LineBorder(Color.black)));
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("이미지, 상품이름, 행사여부, 가격, 재고, 수량, 총합".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			if (column == 5) {
				return true;
			}else {
				return false;
			}
		};
		public java.lang.Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	public Pay() {

		setBorder(new EmptyBorder(20, 20, 20, 20));
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		add(scl);
		add(p1, "South");
		
		p1.add(p2);
		p1.add(p3);
		
		p2.add(new JLabel("금액"));
		p2.add(txt1);
		p2.add(new JLabel("원"));
		
		p3.add(btn1);
		
		for (int i = 0; i < data.size(); i++) {
			int count = data.get(i).get(4) == null ? 1 : 2;
			int pay = (int) (intnum(data.get(i).get(2)) * count * (count == 1 ? 1 : 0.5));
			model.addRow(new Object[] {new ImageIcon(new ImageIcon(file(data.get(i).get(1) + ".jpg")).getImage().getScaledInstance(50, 50, 4)), data.get(i).get(1), count == 1 ? "X" : "O", data.get(i).get(2), data.get(i).get(3), count + "", pay + ""});
		}
		
		pay();
		tbl.getColumnModel().getColumn(5).setCellEditor(new spinner());
		tbl.setRowHeight(50);
		txt1.setHorizontalAlignment(JLabel.RIGHT);
		
	}
	
	public void pay() {
		
		int pay = 0;
		
		for (int i = 0; i < tbl.getRowCount(); i++) {
			pay += intnum(tbl.getValueAt(i, 6).toString());
		}
		
		txt1.setText(df.format(pay));
		
	}
	
	@Override
	public void action() {
		
		tbl.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				
				if (e.getType() == 0) {
					
					int row = e.getFirstRow();
					int col = e.getColumn();
					
					if (col == 5) {
						
						int pay = intnum(tbl.getValueAt(row, 3).toString()) * intnum(tbl.getValueAt(row, 5).toString());
						pay = (int) (pay * (tbl.getValueAt(row, 2).toString().contentEquals("O") ? 0.5 : 1));
						
						tbl.setValueAt(pay + "", row, 6);
						
					}
					
				}
				
				pay();
				
			}
		});
		
		btn1.addActionListener(e->{
			
			jop("총 " + txt1.getText() +"원 결제 완료되었습니다.");
			
			for (int i = 0; i < tbl.getRowCount(); i++) {
				Updat("update stock set st_count = st_count - ? where s_no = ? and p_no = ?;", tbl.getValueAt(i, 5).toString(), data.get(i).get(5), data.get(i).get(0));
				Updat("insert into purchase values(null, ?,?,?,?,?,?);", data.get(i).get(5), data.get(i).get(0), member.get(0).get(0), tbl.getValueAt(i, 5).toString(), tbl.getValueAt(i, 6).toString(), LocalDate.now().toString());
			}
			
			go(MainFrame.mf.p3, new Main());
			
		});
		
		txt1.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				e.consume();
			}
		});
		
	}
	
	public class spinner extends DefaultCellEditor implements Base{

		JSpinner spin;
		SpinnerNumberModel spinmodel;
		
		public spinner() {
			
			super(new JTextField());
			spin = new JSpinner();
			
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			
			int max = intnum(tbl.getValueAt(tbl.getSelectedRow(), 4).toString());
			
			if (tbl.getValueAt(tbl.getSelectedRow(), 2).toString().contentEquals("O")) {
				spinmodel = new SpinnerNumberModel(2, 2, max, 2);
			}else {
				spinmodel = new SpinnerNumberModel(1, 1, max, 1);
			}
			
			spin = new JSpinner(spinmodel);
			
			spin.setValue(intnum(value.toString()));
			
			return spin;
			
		}
		
		public boolean isCellEditable(EventObject anEvent) {
			if (anEvent instanceof MouseEvent) {
				return ((MouseEvent) anEvent).getClickCount() >= 2;
			}
			return true;
		}
		
		public Object getCellEditorValue() {
			return spin.getValue().toString();
		}
		
		public void design() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}
	
}
