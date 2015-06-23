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

@SuppressWarnings("serial")
public class AddStudentForm extends JPanel	{
	private JTextField fNameTextField;
	private JTextField mNameTextField;
	private static JFrame frame;
	private JTextField lNameTextField;
	private JTextField idTextField;
	private JTextField emailTextField;
	private JTextField phoneNumberTextField;
	private static ClassOverviewForm overviewForm;
	private static Student student;
	
	public AddStudentForm(ClassOverviewForm _overviewForm) {
		overviewForm = _overviewForm;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{12, 0, 12, 100, 50, 12, 0};
		gridBagLayout.rowHeights = new int[]{20, 20, 23, 12, 12, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblFirstName = new JLabel("First Name*");
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.WEST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 1;
		gbc_lblFirstName.gridy = 1;
		add(lblFirstName, gbc_lblFirstName);
		
		fNameTextField = new JTextField();
		fNameTextField.setColumns(10);
		GridBagConstraints gbc_fNameTextField = new GridBagConstraints();
		gbc_fNameTextField.gridwidth = 2;
		gbc_fNameTextField.anchor = GridBagConstraints.NORTH;
		gbc_fNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_fNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_fNameTextField.gridx = 3;
		gbc_fNameTextField.gridy = 1;
		add(fNameTextField, gbc_fNameTextField);
		
		JLabel lblMiddleName = new JLabel("Middle Name");
		GridBagConstraints gbc_lblMiddleName = new GridBagConstraints();
		gbc_lblMiddleName.anchor = GridBagConstraints.WEST;
		gbc_lblMiddleName.insets = new Insets(0, 0, 5, 5);
		gbc_lblMiddleName.gridx = 1;
		gbc_lblMiddleName.gridy = 2;
		add(lblMiddleName, gbc_lblMiddleName);
		
		mNameTextField = new JTextField();
		mNameTextField.setColumns(10);
		GridBagConstraints gbc_mNameTextField = new GridBagConstraints();
		gbc_mNameTextField.gridwidth = 2;
		gbc_mNameTextField.anchor = GridBagConstraints.NORTH;
		gbc_mNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_mNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_mNameTextField.gridx = 3;
		gbc_mNameTextField.gridy = 2;
		add(mNameTextField, gbc_mNameTextField);
		
		JLabel lblLastName = new JLabel("Last Name*");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.WEST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 1;
		gbc_lblLastName.gridy = 3;
		add(lblLastName, gbc_lblLastName);
		
		lNameTextField = new JTextField();
		GridBagConstraints gbc_lNameTextField = new GridBagConstraints();
		gbc_lNameTextField.gridwidth = 2;
		gbc_lNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_lNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_lNameTextField.gridx = 3;
		gbc_lNameTextField.gridy = 3;
		add(lNameTextField, gbc_lNameTextField);
		lNameTextField.setColumns(10);
		
		JLabel lblStudentId = new JLabel("Student ID");
		GridBagConstraints gbc_lblStudentId = new GridBagConstraints();
		gbc_lblStudentId.anchor = GridBagConstraints.WEST;
		gbc_lblStudentId.insets = new Insets(0, 0, 5, 5);
		gbc_lblStudentId.gridx = 1;
		gbc_lblStudentId.gridy = 4;
		add(lblStudentId, gbc_lblStudentId);
		
		idTextField = new JTextField();
		GridBagConstraints gbc_idTextField = new GridBagConstraints();
		gbc_idTextField.gridwidth = 2;
		gbc_idTextField.insets = new Insets(0, 0, 5, 5);
		gbc_idTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_idTextField.gridx = 3;
		gbc_idTextField.gridy = 4;
		add(idTextField, gbc_idTextField);
		idTextField.setColumns(10);
		
		JLabel lblEmailAddress = new JLabel("Email Address");
		GridBagConstraints gbc_lblEmailAddress = new GridBagConstraints();
		gbc_lblEmailAddress.anchor = GridBagConstraints.WEST;
		gbc_lblEmailAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmailAddress.gridx = 1;
		gbc_lblEmailAddress.gridy = 5;
		add(lblEmailAddress, gbc_lblEmailAddress);
		
		emailTextField = new JTextField();
		GridBagConstraints gbc_emailTextField = new GridBagConstraints();
		gbc_emailTextField.gridwidth = 2;
		gbc_emailTextField.insets = new Insets(0, 0, 5, 5);
		gbc_emailTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_emailTextField.gridx = 3;
		gbc_emailTextField.gridy = 5;
		add(emailTextField, gbc_emailTextField);
		emailTextField.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (student == null)
				{
					Student newStudent = collectStudentInfo();
					if (newStudent == null) return;
					newStudent.setRawGrade(1, 0, 0);
					Helpers.getActiveClass().addStudent(newStudent);
				}
				else
				{
					if (collectStudentInfo() == null) return;
				}
				overviewForm.fillStudentOverviewDataTable();
				frame.dispose();
			}
		});
		
		JLabel lblPhoneNumber = new JLabel("Phone Number");
		GridBagConstraints gbc_lblPhoneNumber = new GridBagConstraints();
		gbc_lblPhoneNumber.anchor = GridBagConstraints.WEST;
		gbc_lblPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhoneNumber.gridx = 1;
		gbc_lblPhoneNumber.gridy = 6;
		add(lblPhoneNumber, gbc_lblPhoneNumber);
		
		phoneNumberTextField = new JTextField();
		GridBagConstraints gbc_phoneNumberTextField = new GridBagConstraints();
		gbc_phoneNumberTextField.gridwidth = 2;
		gbc_phoneNumberTextField.insets = new Insets(0, 0, 5, 5);
		gbc_phoneNumberTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_phoneNumberTextField.gridx = 3;
		gbc_phoneNumberTextField.gridy = 6;
		add(phoneNumberTextField, gbc_phoneNumberTextField);
		phoneNumberTextField.setColumns(10);
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 3;
		gbc_btnOk.gridy = 7;
		add(btnOk, gbc_btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnCancel.gridx = 4;
		gbc_btnCancel.gridy = 7;
		add(btnCancel, gbc_btnCancel);
		
		if (student != null)
		{
			autoFillForm(student);
		}
	}
	
	public void showForm(final Student _student)
	{
		student = _student;
		SwingUtilities.invokeLater(new Runnable()	{
			public void run()
			{
				displayGUI();
			}
		});
	}
	
	public static void displayGUI()
	{
		if (student == null)
		{
			frame = new JFrame("New Student");
		}
		else
		{
			frame = new JFrame("Edit Student");
		}
		AddStudentForm gui = new AddStudentForm(overviewForm);
		frame.setContentPane(gui);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	private Student collectStudentInfo()
	{
		if (Helpers.isEmpty(fNameTextField) || Helpers.containsNumbers(fNameTextField))
		{
			Helpers.showWarningMessage(frame, "First name cannot be blank and may not contain numbers.");
			return null;
		}
		else if (Helpers.containsNumbers(mNameTextField))
		{
			Helpers.showWarningMessage(frame, "Middle name cannot contain numbers.");
			return null;
		}
		else if (Helpers.isEmpty(lNameTextField))
		{
			Helpers.showWarningMessage(frame, "Last name cannot be blank and may not contain numbers.");
			return null;
		}
		else if (!Helpers.isEmpty(emailTextField) && Helpers.invalidEmailAddress(emailTextField))
		{
			Helpers.showWarningMessage(frame, "Please enter a valid email address.");
			return null;
		}
		else if (!Helpers.isEmpty(phoneNumberTextField))
		{
			if (Helpers.formatPhoneNumber(phoneNumberTextField) == null)
			{
				Helpers.showWarningMessage(frame, "Please enter a valid phone number.");
				return null;
			}
		}
		
		String fName = fNameTextField.getText();
		String mName = mNameTextField.getText();
		String lName = lNameTextField.getText();
		String studentID = idTextField.getText();
		String emailAddress = emailTextField.getText();
		String phoneNumber = Helpers.formatPhoneNumber(phoneNumberTextField);
		Integer ID = Helpers.getActiveClass().getListOfStudents().size();
		
		if (student != null)
		{
			student.firstName = fName;
			student.middleName = mName;
			student.lastName = lName;
			student.studentID = studentID;
			student.emailAddress = emailAddress;
			student.phoneNumber = phoneNumber;
		}
		return new Student(fName, mName, lName, studentID, emailAddress, phoneNumber, ID);
	}
	
	protected void autoFillForm(Student student)
	{
		fNameTextField.setText(student.firstName);
		mNameTextField.setText(student.middleName);
		lNameTextField.setText(student.lastName);
		idTextField.setText(student.studentID);
		emailTextField.setText(student.emailAddress);
		phoneNumberTextField.setText(student.phoneNumber);
	}
}