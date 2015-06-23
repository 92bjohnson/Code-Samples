import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import java.awt.Color;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class ClassOverviewForm extends JPanel	{
	private static JFrame frame;
	private static JTable table;
	private static JTable table_1;
	private StudentDetailForm studentDetail;
	private AssignmentDetailForm assignmentDetail;
	private Class activeClass = Helpers.getActiveClass();
	private static String[] studentOverviewColumnNames = {"Name", "Average"};
	private static String[] assignmentOverviewColumnNames = {"Assignment", "Class Average", "Point Value"};
	private ClassOverviewForm me = this;
	
	public ClassOverviewForm() {		
		setLayout(new MigLayout("", "[60px][92px][56px][158px][1px:n][][1px:n][19px]", "[16px][14px][14px][145px][14px][145px]"));
		
		JLabel lblMyClass = new JLabel(activeClass.getClassName());
		lblMyClass.setFont(new Font("Tahoma", Font.BOLD, 13));
		add(lblMyClass, "cell 2 0,alignx center,aligny center");
		
		JLabel lblclassInfo = new JLabel(activeClass.getSchoolYr());
		add(lblclassInfo, "cell 2 1,alignx center,aligny center");
		
		JLabel lblStudents = new JLabel("Students");
		add(lblStudents, "cell 0 2,alignx left,aligny center");
		
		JLabel lblNewLabel = new JLabel("Add");
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				AddStudentForm studentForm = new AddStudentForm(me);
				studentForm.showForm(null);
			}
		});
		lblNewLabel.setText("<HTML><U>Add<U><HTML>");
		lblNewLabel.setForeground(Color.BLUE);
		add(lblNewLabel, "cell 3 2,alignx right,aligny center");
		
		JLabel lbledit = new JLabel("<HTML><U>Edit<U><HTML>");
		lbledit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (getSelectedStudent() != null)
				{
					AddStudentForm addStudentForm = new AddStudentForm(me);
					addStudentForm.showForm(getSelectedStudent());
				}
			}
		});
		lbledit.setForeground(Color.BLUE);
		add(lbledit, "cell 5 2,alignx center");
		
		JLabel lblDelete = new JLabel("<HTML><U>Delete<U><HTML>");
		lblDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (getSelectedStudent() != null)
				{
					Helpers.getActiveClass().removeStudent(getSelectedStudent());
				}
			}
		});
		lblDelete.setForeground(Color.BLUE);
		add(lblDelete, "cell 7 2");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 3 8 1,grow");
			    
		table = new JTable()
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		table.setColumnSelectionAllowed(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
				{
					if (studentDetail != null) StudentDetailForm.closeForm();
					studentDetail = new StudentDetailForm();
					studentDetail.showForm();
				}
			}
		});
		
		fillStudentOverviewDataTable();
		
		scrollPane.setViewportView(table);
		
		JLabel lblAssignments = new JLabel("Assignments");
		add(lblAssignments, "cell 0 4,alignx left,aligny center");
		
		JLabel lblAdd = new JLabel("Add");
		lblAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				AddAssignmentForm assignmentForm = new AddAssignmentForm(me);
				assignmentForm.showForm(null);
			}
		});
		lblAdd.setText("<HTML><U>Add<U><HTML>");
		lblAdd.setForeground(Color.BLUE);
		add(lblAdd, "cell 3 4,alignx right,aligny center");
		
		JLabel lbledit_1 = new JLabel("<HTML><U>Edit<U><HTML>");
		lbledit_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (getSelectedAssignment() != null)
				{
					AddAssignmentForm addAssignmentForm = new AddAssignmentForm(me);
					addAssignmentForm.showForm(getSelectedAssignment());
				}
			}
		});
		lbledit_1.setForeground(Color.BLUE);
		add(lbledit_1, "cell 5 4,alignx center");
		
		JLabel lblDelete_1 = new JLabel("<HTML><U>Delete<U><HTML>");
		lblDelete_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (getSelectedAssignment() != null)
				{
					Helpers.getActiveClass().removeAssignment(getSelectedAssignment());
				}
			}
		});
		lblDelete_1.setForeground(Color.BLUE);
		add(lblDelete_1, "cell 7 4");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, "cell 0 5 8 1,grow");
		
		table_1 = new JTable()
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
				{
					if (assignmentDetail != null) AssignmentDetailForm.closeForm();
					assignmentDetail = new AssignmentDetailForm();
					assignmentDetail.showForm();
				}
			}
		});
		scrollPane_1.setViewportView(table_1);
		
		fillAssignmentOverviewDataTable();
		
		addMenuBar();
	}
	
	private void addMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem newClassItem = new JMenuItem("New Class");
		newClassItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewClassForm newClass = new NewClassForm();
				newClass.showForm();
			}
		});
		JMenuItem loadClassItem = new JMenuItem("Load Class");
		loadClassItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WelcomeForm.loadClass();
			}
		});
		JMenuItem saveClassItem = new JMenuItem("Save Class");
		saveClassItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Helpers.saveClass(frame);
			}
		});
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Helpers.exitProgram(frame);
			}
		});
		menu.add(newClassItem);
		menu.add(loadClassItem);
		menu.add(saveClassItem);
		menu.add(exitItem);
		if (frame != null)
		{
			frame.setJMenuBar(menuBar);
		}
		
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		JMenuItem helpItem = new JMenuItem("Tips for this Screen");
		helpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Helpers.showInformationalMessage(frame, "Double-click inside a cell to see details about a student or assignment.");
			}
		});
		helpMenu.add(helpItem);
	}

	public void showForm()
	{
		SwingUtilities.invokeLater(new Runnable()	{
			public void run()
			{
				displayGUI();
			}
		});
	}
	
	public static void displayGUI()
	{
		frame = new JFrame("My Class");
		ClassOverviewForm gui = new ClassOverviewForm();
		frame.setContentPane(gui);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Helpers.exitProgram(frame);
			}
		});
	}
	
	protected void fillStudentOverviewDataTable()
	{
		try
		{
			Object[][] data = activeClass.getStudentsOverview();
			table.setModel(new DefaultTableModel(data, studentOverviewColumnNames));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void fillAssignmentOverviewDataTable()
	{
		try
		{
			Object[][] data = activeClass.getAssignmentsOverview();
			table_1.setModel(new DefaultTableModel(data, assignmentOverviewColumnNames));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Student getSelectedStudent()
	{
		int selectedRow = table.getSelectedRow();
		Student studentSelected = Helpers.getActiveClass().getStudent(selectedRow);
		return studentSelected;
	}
	
	public static Assignment getSelectedAssignment()
	{
		int selectedRow = table_1.getSelectedRow();
		Assignment assignmentSelected = Helpers.getActiveClass().getAssignment(selectedRow);
		return assignmentSelected;
	}
	
	public void updateForm()
	{
		fillStudentOverviewDataTable();
		fillAssignmentOverviewDataTable();
	}
	
	public static void closeFrame()
	{
		if (frame != null)
		{
			frame.dispose();
		}
	}
}