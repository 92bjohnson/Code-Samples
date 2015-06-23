import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;

import java.awt.Panel;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class AddAssignmentForm extends JPanel	{
	private JTextField assignmentNameTextField;
	private JComboBox<String> assignmentTypeComboBox = new JComboBox<String>();
	private static JFrame frame;
	private JTextField pointValueTextField;
	private JTextField latePenaltyTextField;
	private static ClassOverviewForm overviewForm;
	private JComboBox<String> percentageOrPointsComboBox = new JComboBox<String>();
	private static Assignment assignment;
	
	public AddAssignmentForm(ClassOverviewForm _overviewForm) {
		overviewForm = _overviewForm;
		assignmentTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
		assignmentTypeComboBox.addItem("Homework");
		assignmentTypeComboBox.addItem("Quiz");
		assignmentTypeComboBox.addItem("Exam");
		assignmentTypeComboBox.addItem("Project");
		assignmentTypeComboBox.addItem("Other");
		
		percentageOrPointsComboBox.addItem("Points");
		percentageOrPointsComboBox.addItem("%");
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{12, 0, 12, 50, 50, 12, 0};
		gridBagLayout.rowHeights = new int[]{12, 20, 23, 12, 12, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblFirstName = new JLabel("Assignment Name");
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.WEST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 1;
		gbc_lblFirstName.gridy = 1;
		add(lblFirstName, gbc_lblFirstName);
		
		assignmentNameTextField = new JTextField();
		assignmentNameTextField.setColumns(10);
		GridBagConstraints gbc_assignmentNameTextField = new GridBagConstraints();
		gbc_assignmentNameTextField.gridwidth = 2;
		gbc_assignmentNameTextField.anchor = GridBagConstraints.NORTH;
		gbc_assignmentNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_assignmentNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_assignmentNameTextField.gridx = 3;
		gbc_assignmentNameTextField.gridy = 1;
		add(assignmentNameTextField, gbc_assignmentNameTextField);
		
		JLabel lblMiddleName = new JLabel("Assignment Type");
		GridBagConstraints gbc_lblMiddleName = new GridBagConstraints();
		gbc_lblMiddleName.anchor = GridBagConstraints.WEST;
		gbc_lblMiddleName.insets = new Insets(0, 0, 5, 5);
		gbc_lblMiddleName.gridx = 1;
		gbc_lblMiddleName.gridy = 2;
		add(lblMiddleName, gbc_lblMiddleName);
		
		GridBagConstraints gbc_assignmentTypeComboBox = new GridBagConstraints();
		gbc_assignmentTypeComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_assignmentTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_assignmentTypeComboBox.gridx = 3;
		gbc_assignmentTypeComboBox.gridy = 2;
		add(assignmentTypeComboBox, gbc_assignmentTypeComboBox);
		
		JLabel lblLastName = new JLabel("Point Value");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.WEST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 1;
		gbc_lblLastName.gridy = 3;
		add(lblLastName, gbc_lblLastName);
		
		pointValueTextField = new JTextField();
		GridBagConstraints gbc_pointValueTextField = new GridBagConstraints();
		gbc_pointValueTextField.insets = new Insets(0, 0, 5, 5);
		gbc_pointValueTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_pointValueTextField.gridx = 3;
		gbc_pointValueTextField.gridy = 3;
		add(pointValueTextField, gbc_pointValueTextField);
		pointValueTextField.setColumns(10);
		
		JLabel lblStudentId = new JLabel("Late Penalty (Per Day)");
		GridBagConstraints gbc_lblStudentId = new GridBagConstraints();
		gbc_lblStudentId.anchor = GridBagConstraints.WEST;
		gbc_lblStudentId.insets = new Insets(0, 0, 5, 5);
		gbc_lblStudentId.gridx = 1;
		gbc_lblStudentId.gridy = 4;
		add(lblStudentId, gbc_lblStudentId);
		
		latePenaltyTextField = new JTextField();
		GridBagConstraints gbc_latePenaltyTextField = new GridBagConstraints();
		gbc_latePenaltyTextField.insets = new Insets(0, 0, 5, 5);
		gbc_latePenaltyTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_latePenaltyTextField.gridx = 3;
		gbc_latePenaltyTextField.gridy = 4;
		add(latePenaltyTextField, gbc_latePenaltyTextField);
		latePenaltyTextField.setColumns(10);
		
		GridBagConstraints gbc_percentageOrPointsComboBox = new GridBagConstraints();
		gbc_percentageOrPointsComboBox.anchor = GridBagConstraints.WEST;
		gbc_percentageOrPointsComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_percentageOrPointsComboBox.gridx = 4;
		gbc_percentageOrPointsComboBox.gridy = 4;
		add(percentageOrPointsComboBox, gbc_percentageOrPointsComboBox);
		
		Panel panel = new Panel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 4;
		gbc_panel.gridy = 5;
		add(panel, gbc_panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton confirmButton = new JButton("OK");
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (assignment == null)
				{
					Assignment newAssignment = collectAssignmentInfo();
					if (newAssignment == null) return;
					Helpers.getActiveClass().addAssignment(newAssignment);
					Helpers.getActiveClass().resizeGradeBook();
				}
				else
				{
					if (collectAssignmentInfo() == null) return;
				}
				overviewForm.fillAssignmentOverviewDataTable();
				frame.dispose();
			}
		});
		panel.add(confirmButton);
		
		JButton cancelButton = new JButton("Cancel");
		panel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		
		if (assignment != null)
		{
			autoFillForm();
		}
	}
	
	public Assignment collectAssignmentInfo()
	{
		if (Helpers.isEmpty(assignmentNameTextField))
		{
			Helpers.showWarningMessage(frame, "Please enter a name for this assignment.");
			return null;
		}
		else if (!Helpers.isANumber(pointValueTextField))
		{
			Helpers.showWarningMessage(frame, "Please enter a numeric value in the Point Value field.");
			return null;
		}
		else if (!Helpers.isANumber(latePenaltyTextField) && !Helpers.isEmpty(latePenaltyTextField))
		{
			Helpers.showWarningMessage(frame, "Please enter a numeric value in the Late Penalty field.");
			return null;
		}
		
		int ID = Helpers.getActiveClass().assignments.size() + 1;
		String assignmentName = assignmentNameTextField.getText();
		String assignmentType = (String) assignmentTypeComboBox.getSelectedItem();
		int pointValue = Integer.parseInt(pointValueTextField.getText());
		int latePenalty = Helpers.isEmpty(latePenaltyTextField) ? 0 : Integer.parseInt(latePenaltyTextField.getText());
		String penaltyAs = ((String) percentageOrPointsComboBox.getSelectedItem()).equals("%") ? "PERCENT" : "POINTS";
		
		if (assignment != null)
		{
			assignment.name = assignmentName;
			assignment.type = Assignment.AssignmentTypes.valueOf(assignmentType.toUpperCase());
			assignment.pointValue = pointValue;
			assignment.latePenaltyPerDay = latePenalty;
			assignment.penaltyAs = Assignment.PenaltyType.valueOf(penaltyAs.toUpperCase());;
		}

		return new Assignment(ID, assignmentName, assignmentType, pointValue, latePenalty, penaltyAs);
	}
	
	public void showForm(Assignment _assignment)
	{
		assignment = _assignment;
		SwingUtilities.invokeLater(new Runnable()	{
			public void run()
			{
				displayGUI();
			}
		});
	}
	
	public static void displayGUI()
	{
		if (assignment == null)
		{
			frame = new JFrame("New Assignment");
		}
		else
		{
			frame = new JFrame("Edit Assignment");
		}
		AddAssignmentForm gui = new AddAssignmentForm(overviewForm);
		frame.setContentPane(gui);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	private void autoFillForm()
	{
		assignmentNameTextField.setText(assignment.name); 
		assignmentTypeComboBox.setSelectedItem(assignment.type);
		pointValueTextField.setText(String.valueOf(assignment.pointValue));
		latePenaltyTextField.setText(String.valueOf(assignment.latePenaltyPerDay));
		percentageOrPointsComboBox.setSelectedItem(assignment.penaltyAs);
	}
}