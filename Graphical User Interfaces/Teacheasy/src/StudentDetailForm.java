import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;

import net.miginfocom.swing.MigLayout;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;
import javax.swing.JTextField;

import java.awt.Button;

@SuppressWarnings("serial")
public class StudentDetailForm extends JPanel	{
	private static JFrame frame;
	private static JTable table;
	private JLabel lblMyClass = new JLabel(ClassOverviewForm.getSelectedStudent().getFullName() + "'s Grade History");
	private static Student student;
	private String[] columnNames = { "Assignment", "Point Value", "Original Grade", "Days Late", "ID", "Modified Grade" };
	private JLabel lblAverage = new JLabel("Average: " + ClassOverviewForm.getSelectedStudent().getAverageAsString());
	private JTextField studentInfoTextSummary;
	private DistributionGraph studentDetailGraph;
	
	public StudentDetailForm() {
		student = ClassOverviewForm.getSelectedStudent();
		
		setLayout(new MigLayout("", "[60px,grow][92px][56px,grow][158px][19px]", "[16px][grow][grow][:0px:0px][:0px:0px][145px][][12px]"));
		
		lblMyClass.setFont(new Font("Tahoma", Font.BOLD, 13));
		add(lblMyClass, "cell 2 0,alignx center,aligny center");
		
		ImageIcon rightArrowIcon = new ImageIcon("Resources/right_arrow.png");
		ImageIcon leftArrowIcon = new ImageIcon("Resources/left_arrow.png");
		
		JPanel panel = new JPanel();
		add(panel, "cell 2 1,grow");
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel leftArrow = new JLabel(leftArrowIcon);
		leftArrow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				goToNewStudent(-1);
			}
		});
		panel.add(leftArrow);
		JLabel rightArrow = new JLabel(rightArrowIcon);
		rightArrow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				goToNewStudent(1);
			}
		});
		panel.add(rightArrow);
		
		studentInfoTextSummary = new JTextField()
		{
		    @Override public void setBorder(Border border)
		    {
		    }
		};
		
		studentInfoTextSummary.setHorizontalAlignment(SwingConstants.CENTER);
		studentInfoTextSummary.setEditable(false);
		add(studentInfoTextSummary, "cell 0 2 4 1,growx");
		studentInfoTextSummary.setColumns(10);
		
		JLabel lblStudentSummary = new JLabel();
		add(lblStudentSummary, "cell 0 3 4 2");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 5 5 1,grow");
	    
	    Object[][] data = Helpers.getActiveClass().getStudentPerformanceDetails(student);
	    
		table = new JTable()
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return column == 0 || column == 1 || column == 4 ? false : true;
			}
		};
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateStudentInfo();
			}
		});
		refreshComponents(data);
		scrollPane.setViewportView(table);
		
		add(lblAverage, "cell 0 6");
		
		Button button = new Button("Show Graph");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				graphData();
				DistributionGraph.displayGraph();
			}
		});
		add(button, "cell 3 7,alignx right");
	}
	
	public void showForm()
	{
		SwingUtilities.invokeLater(new Runnable()	{
			public void run()
			{
				displayGUI();
			}
		});
		loadStudentInfo();
	}

	private void loadStudentInfo()
	{
	}

	public static void displayGUI()
	{
		frame = new JFrame("Student Details");
		StudentDetailForm gui = new StudentDetailForm();
		frame.setContentPane(gui);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				updateStudentInfo();
				Helpers.updateClassOverviewForm();
			}
		});
	}
	
	private static void updateStudentInfo()
	{
		for (int row = 0; row < table.getRowCount(); row++)
		{
			if (table.getModel().getValueAt(row, 0) != null)
			{
				Integer assignmentID = Integer.parseInt(table.getModel().getValueAt(row, 4).toString());
				List<AssignmentPerformance> assignmentPerformance = Helpers.getActiveClass().getMyGrades(student);
				AssignmentPerformance ap = assignmentPerformance.get(assignmentID);
				
				String originalGradeCellValue = table.getModel().getValueAt(row, 2).toString();
				String daysLateCellValue = table.getModel().getValueAt(row, 3).toString();
				double originalGrade = Helpers.isANumber(originalGradeCellValue) ? Double.parseDouble(originalGradeCellValue) : ap.studentOriginalGrade;
				int daysLate = Helpers.isANumber(daysLateCellValue) ? Integer.parseInt(daysLateCellValue) : ap.daysLate;
				
				if (ap.studentOriginalGrade != originalGrade || daysLate != ap.daysLate)
				{
					Helpers.getActiveClass().setGrade(student, Helpers.getActiveClass().assignments.get(assignmentID - 1), originalGrade, daysLate);
				}
			}
			else
			{
				return;
			}
		}
	}
	
	public static void closeForm()
	{
		frame.dispose();
	}
	
	private void refreshComponents(Object[][] tableData)
	{
		lblMyClass.setText(student.getFullName() + "'s Grade History");
		displayStudentInfoSummary();
		table.setModel(new DefaultTableModel(tableData, columnNames));
		lblAverage.setText("Average: " + student.getAverageAsString());
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(4));
	}
	
	private void displayStudentInfoSummary() {
		try
		{
			String idString = student.studentID.equals("") ? "N/A" : student.studentID;
			String emailString = student.emailAddress.equals("") ? "N/A" : student.emailAddress;
			String phoneNumberString = student.phoneNumber.equals("") ? "N/A" : student.phoneNumber;
			studentInfoTextSummary.setText(String.format("%s%40s%40s", "ID: " + idString, "Email: " + emailString, "Phone: " + phoneNumberString));
		}
		catch(Exception e)
		{
			return;
		}
	}

	private void goToNewStudent(int shift)
	{
		updateStudentInfo();
		
		int indexOfStudent = Helpers.getActiveClass().getListOfStudents().indexOf(student);
		int newIndexOfStudent = indexOfStudent + shift;
		if (newIndexOfStudent >= Helpers.getActiveClass().getListOfStudents().size())
		{
			newIndexOfStudent = 0;
		}
		else if (newIndexOfStudent < 0)
		{
			newIndexOfStudent = Helpers.getActiveClass().getListOfStudents().size() - 1;
		}
		
		student = Helpers.getActiveClass().getListOfStudents().get(newIndexOfStudent);
		refreshComponents(Helpers.getActiveClass().getStudentPerformanceDetails(student));
	}
	
	private void graphData()
	{
		List<AssignmentPerformance> assignmentPerformance = Helpers.getActiveClass().getMyGrades(student);
		List<Assignment> assignments = Helpers.getActiveClass().getListOfAssignments();
		
		double[] values = new double[] { 0, 0, 0, 0, 0 };
		String[] objects = new String[] { "F", "D", "C", "B", "A" };
		
		for (int i = 0; i < assignments.size(); i++)
		{
			AssignmentPerformance ap = assignmentPerformance.get(i);
			if (ap.isGraded())
			{
				Assignment assignment = assignments.get(i - 1);
				double grade = ap.studentModifiedGrade/assignment.pointValue;
				if (grade >= .9) values[4] += 1;
				else if (grade >= .8) values[3] += 1;
				else if (grade >= .7) values[2] += 1;
				else if (grade >= .6) values[1] += 1;
				else values[0] += 1;
			}
		}
		if (studentDetailGraph != null)
		{
			studentDetailGraph.closeForm();
		}
		studentDetailGraph = new DistributionGraph("Grade Distribution", objects, values);
	}
}