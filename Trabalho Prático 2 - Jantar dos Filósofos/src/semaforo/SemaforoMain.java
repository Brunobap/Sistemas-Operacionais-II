package semaforo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;
import java.awt.Window.Type;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import implementacao.Filosofo;
import implementacao.Mesa;

import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.DropMode;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;

public class SemaforoMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Filosofo[] filos, foto;
	private Semaforo mesa;
	private JTable table;
	/**
	 * Launch the application.
	 * FAVOR NÃO MEXER
	 */
	public static void main(String[] args) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SemaforoMain frame = new SemaforoMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SemaforoMain() {
		JTextArea textArea = new JTextArea();
		
		// Criar o contexto
		this.filos = new Filosofo[5];
		this.foto = new Filosofo[5];
		this.mesa = new Semaforo(filos);
		for (int i=0; i<5; i++) this.filos[i] = new Filosofo(i, mesa, textArea);
		mesa.largada();
		
		
		// Parte gráfica, não mexer diretamente
		setTitle("Simulador Jantar dos Filósofos - Controle por Semaforo");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250, 250, 700, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel_2 = new JLabel("Contexto: 5 threads \"Filosofo\" em uma thread \"Mesa\", que monitora a execução");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(lblNewLabel_2);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		JTextPane txtpnBotopassoRealiza = new JTextPane();
		txtpnBotopassoRealiza.setEditable(false);
		txtpnBotopassoRealiza.setText("\"Passo\": realiza a simulação pelo tempo, em segundos, digitado ao lado\r\n\"Seguir\": realiza a simulação por um tempo indeterminado\r\n\"Parar\": para a simulação ligada por \"Seguir\"");
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(0), null, Integer.valueOf(1)));
		
		JButton btnNewButton = new JButton("Passo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mesa.num = 0;
				for (Filosofo fi : filos) fi.m1 = System.currentTimeMillis();
				mesa.up();
				
				try {
					Thread.currentThread().sleep(1000 * (int) spinner.getValue());
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				foto = mesa.tirarFoto();
				for (int i=0; i<10; i++) mesa.num = -1;
				for (Filosofo fi : foto) {
					table.setValueAt(""+fi.esperaMed, fi.idThread+1, 1);
					table.setValueAt(""+fi.esperaMax, fi.idThread+1, 2);
					table.setValueAt(""+fi.pratos, fi.idThread+1, 3);
					table.setValueAt(""+fi.taComendo, fi.idThread+1, 4);
				}	
			}
		});
		
		JButton btnNewButton_3 = new JButton("Seguir");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				mesa.num = 0;
				for (Filosofo fi : filos) fi.m1 = System.currentTimeMillis();
				mesa.up();
			}
		});

		table = new JTable();
		table.setEnabled(false);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Fil\u00F3sofo (id)", "Espera M\u00E9dia (s)", "Espera M\u00E1xima (s)", "Vezes servido", "Est\u00E1 comendo?"},
				{"00", null, null, null, null},
				{"01", null, null, null, null},
				{"02", null, null, null, null},
				{"03", null, null, null, null},
				{"04", null, null, null, null},
			},
			new String[] {
				"Fil\u00F3sofo (id)", "Espera M\u00E9dia (s)", "Espera M\u00E1xima (s)", "Vezes servido", "Est\u00E1 comendo?"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(1).setPreferredWidth(93);
		table.getColumnModel().getColumn(2).setPreferredWidth(99);
		table.getColumnModel().getColumn(4).setResizable(false);
		
		
		JButton btnNewButton_2 = new JButton("Parar");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				foto = mesa.tirarFoto();
				for (int i=0; i<10; i++) mesa.num = -1;
				for (Filosofo fi : foto) {
					table.setValueAt(""+fi.esperaMed, fi.idThread+1, 1);
					table.setValueAt(""+fi.esperaMax, fi.idThread+1, 2);
					table.setValueAt(""+fi.pratos, fi.idThread+1, 3);
					table.setValueAt(""+fi.taComendo, fi.idThread+1, 4);
				}					
			}
		});
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_2.add(txtpnBotopassoRealiza);
		panel_2.add(spinner);
		panel_2.add(btnNewButton);
		panel_2.add(btnNewButton_3);
		panel_2.add(btnNewButton_2);
		
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4, BorderLayout.CENTER);
		
		
		JLabel lblNewLabel = new JLabel("Dados do último momento da execução:");
		
		JLabel lblNewLabel_1 = new JLabel("Operações realizadas:");
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING, false)
							.addComponent(lblNewLabel)
							.addComponent(table, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblNewLabel_1))
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(255, Short.MAX_VALUE))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 375, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(312, Short.MAX_VALUE))
		);
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		panel_4.setLayout(gl_panel_4);
	}
}
