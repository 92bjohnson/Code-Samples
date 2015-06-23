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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class AssignmentDetailForm extends JPanel	{
	private static JFrame frame;
	private static JTable table;
	private static Assignment assignment;
	static JLabel lblAverage = new JLabel("Average: " + String.valueOf(ClassOverviewForm.getSelectedAssignment().getAssignmentAverage()));
	JLabel lblAssignmentDetails;
	private String[] columnNames = { "Student", "Original Grade", "Days Late", "ID", "Modified Grade" };
	private JTextField assignmentInfoTextSummary;
	private DistributionGraph assignmentDetailGraph;
	
	public AssignmentDetailForm() {
		assignment = ClassOverviewForm.getSelectedAssignment();
		setLayout(new MigLayout("", "[60px][92px][56px,grow][158px][19px]", "[16px][14px,grow][14px][145px][][12px]"));
		
		lblAssignmentDetails = new JLabel(assignment.name + " Details");
		lblAssignmentDetails.setFont(new Font("Tahoma", Font.BOLD, 13));
		add(lblAssignmentDetails, "cell 2 0,alignx center,aligny center");
		
		JPanel panel = new JPanel();
		add(panel, "cell 2 1,grow");
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		ImageIcon rightArrowIcon = new ImageIcon("Resources/right_arrow.png");
		ImageIcon leftArrowIcon = new ImageIcon("Resources/left_arrow.png");
		
		JLabel leftArrow = new JLabel(leftArrowIcon);
		leftArrow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				goToNewAssignment(-1);
			}
		});
		panel.add(leftArrow);
		
		JLabel rightArrow = new JLabel(rightArrowIcon);
		rightArrow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				goToNewAssignment(1);
			}
		});
		panel.add(rightArrow);
		
		assignmentInfoTextSummary = new JTextField()
		{
		    @Override public void setBorder(Border border)
		    {
		    }
		};
		assignmentInfoTextSummary.setHorizontalAlignment(SwingConstants.CENTER);
		assignmentInfoTextSummary.setEditable(false);
		add(assignmentInfoTextSummary, "cell 0 2 4 1,growx");
		assignmentInfoTextSummary.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 3 5 1,grow");
		
	    Object[][] data = Helpers.activeClass.getAssignmentStatistics(ClassOverviewForm.getSelectedAssignment());
		
		table = new JTable()
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return column == 0 || column == 3 ? false : true;
			}
		};
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				updateAssignmentInfo();
			}
		});
		table.setModel(new DefaultTableModel(data, columnNames));
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));
		scrollPane.setViewportView(table);
		
		add(lblAverage, "cell 0 4");
		
		JButton btnShowGraph = new JButton("Show Graph");
		btnShowGraph.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				graphData();
				DistributionGraph.displayGraph();
			}
		});
		add(btnShowGraph, "cell 3 5,alignx right");
		
		displayAssignmentInfoSummary();
	}

	private static void updateAssignmentInfo()
	{
		for (int row = 0; row < table.getRowCount(); row++)
		{
			if (table.getModel().getValueAt(row, 0) != null)
			{
				Integer studentID = Integer.parseInt(table.getModel().getValueAt(row, 3).toString());
				Student student = Helpers.getActiveClass().getStudent(studentID);
				List<AssignmentPerformance> assignmentPerformance = Helpers.getActiveClass().getStudentScores(assignment);
				AssignmentPerformance ap = assignmentPerformance.get(studentID);
				
				String originalGradeCellValue = table.getModel().getValueAt(row, 1).toString();
				String daysLateCellValue = table.getModel().getValueAt(row, 2).toString();
				double originalGrade = Helpers.isANumber(originalGradeCellValue) ? Double.parseDouble(originalGradeCellValue) : ap.studentOriginalGrade;
				int daysLate = Helpers.isANumber(daysLateCellValue) ? Integer.parseInt(daysLateCellValue) : ap.daysLate;
				
				if ((ap.studentOriginalGrade != originalGrade || ap.daysLate != daysLate) && (originalGrade > 0 || daysLate > 0))
				{
					Helpers.getActiveClass().setGrade(student, assignment, originalGrade, daysLate);
				}
			}
			else
			{
				return;
			}
		}
		lblAverage.setText("Average: " + String.valueOf(assignment.getAssignmentAverage()));
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
		frame = new JFrame("Assignment Details");
		AssignmentDetailForm gui = new AssignmentDetailForm();
		frame.setContentPane(gui);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				updateAssignmentInfo();
				Helpers.updateClassOverviewForm();
			}
		});
	}
	
	public static void closeForm()
	{
		frame.dispose();
	}
	
	private void refreshComponents(Object[][] tableData)
	{
		lblAssignmentDetails.setText(assignment.name + " Details");
		displayAssignmentInfoSummary();
		table.setModel(new DefaultTableModel(tableData, columnNames));
		lblAverage.setText("Average: " + String.valueOf(assignment.getAssignmentAverage()));
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));
	}
	
	private void displayAssignmentInfoSummary() {
		String typeString = String.valueOf(assignment.type);
		String pointValueString = String.valueOf(assignment.pointValue);
		String latePenaltyString = assignment.latePenaltyPerDay == 0 ? "None" : String.valueOf(assignment.latePenaltyPerDay);
		String latePenaltyAsString = assignment.latePenaltyPerDay == 0 ? "" : String.valueOf(assignment.penaltyAs);
		latePenaltyAsString = latePenaltyAsString.equals("PERCENT") ? "%" : latePenaltyAsString;
		latePenaltyAsString = latePenaltyAsString.equals("POINTS") ? " Pts" : latePenaltyAsString;
		assignmentInfoTextSummary.setText(String.format("%s%30s%30s", "Type: " + typeString, "Point Value: " + pointValueString,
				"Late Penalty: " + latePenaltyString + latePenaltyAsString));
	}
	
	private void goToNewAssignment(int shift)
	{
		updateAssignmentInfo();
		
		int indexOfAssignment = Helpers.getActiveClass().getListOfAssignments().indexOf(assignment);
		int newIndexOfAssignment = indexOfAssignment + shift;
		if (newIndexOfAssignment >= Helpers.getActiveClass().getListOfAssignments().size())
		{
			newIndexOfAssignment = 0;
		}
		else if (newIndexOfAssignment < 0)
		{
			newIndexOfAssignment = Helpers.getActiveClass().getListOfAssignments().size() - 1;
		}
		
		assignment = Helpers.getActiveClass().getListOfAssignments().get(newIndexOfAssignment);
		refreshComponents(Helpers.getActiveClass().getAssignmentStatistics(assignment));
	}
	
	private void graphData()
	{
		List<AssignmentPerformance> assignmentPerformance = Helpers.getActiveClass().getStudentScores(assignment);
		List<Student> students = Helpers.getActiveClass().getListOfStudents();
		
		double[] values = new double[] { 0, 0, 0, 0, 0 };
		String[] objects = new String[] { "F", "D", "C", "B", "A" };
		
		for (int i = 0; i < students.size(); i++)
		{
			AssignmentPerformance ap = assignmentPerformance.get(i);
			if (ap.isGraded())
			{
				double grade = ap.studentModifiedGrade/assignment.pointValue;
				if (grade >= .9) values[4] += 1;
				else if (grade >= .8) values[3] += 1;
				else if (grade >= .7) values[2] += 1;
				else if (grade >= .6) values[1] += 1;
				else values[0] += 1;
			}
		}
		if (assignmentDetailGraph != null)
		{
			assignmentDetailGraph.closeForm();
		}
		assignmentDetailGraph = new DistributionGraph("Grade Distribution", objects, values);
	}
}