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
public class NewClassForm extends JPanel	{
	private JTextField classNameTextField;
	private JTextField schoolYrTextField;
	private static JFrame frame;
	
	public NewClassForm() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{12, 0, 12, 65, 50, 12, 0};
		gridBagLayout.rowHeights = new int[]{20, 20, 23, 12, 12, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblClassName = new JLabel("Class Name*");
		GridBagConstraints gbc_lblClassName = new GridBagConstraints();
		gbc_lblClassName.anchor = GridBagConstraints.WEST;
		gbc_lblClassName.insets = new Insets(0, 0, 5, 5);
		gbc_lblClassName.gridx = 1;
		gbc_lblClassName.gridy = 1;
		add(lblClassName, gbc_lblClassName);
		
		classNameTextField = new JTextField();
		classNameTextField.setColumns(10);
		GridBagConstraints gbc_classNameTextField = new GridBagConstraints();
		gbc_classNameTextField.gridwidth = 2;
		gbc_classNameTextField.anchor = GridBagConstraints.NORTH;
		gbc_classNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_classNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_classNameTextField.gridx = 3;
		gbc_classNameTextField.gridy = 1;
		add(classNameTextField, gbc_classNameTextField);
		
		JLabel lblSchoolYearterm = new JLabel("School Year/Term*");
		GridBagConstraints gbc_lblSchoolYearterm = new GridBagConstraints();
		gbc_lblSchoolYearterm.anchor = GridBagConstraints.WEST;
		gbc_lblSchoolYearterm.insets = new Insets(0, 0, 5, 5);
		gbc_lblSchoolYearterm.gridx = 1;
		gbc_lblSchoolYearterm.gridy = 2;
		add(lblSchoolYearterm, gbc_lblSchoolYearterm);
		
		schoolYrTextField = new JTextField();
		schoolYrTextField.setColumns(10);
		GridBagConstraints gbc_schoolYrTextField = new GridBagConstraints();
		gbc_schoolYrTextField.gridwidth = 2;
		gbc_schoolYrTextField.anchor = GridBagConstraints.NORTH;
		gbc_schoolYrTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_schoolYrTextField.insets = new Insets(0, 0, 5, 5);
		gbc_schoolYrTextField.gridx = 3;
		gbc_schoolYrTextField.gridy = 2;
		add(schoolYrTextField, gbc_schoolYrTextField);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Helpers.isEmpty(classNameTextField))
				{
					Helpers.showWarningMessage(frame, "Please enter a name for this class.");
					return;
				}
				else if (Helpers.isEmpty(schoolYrTextField))
				{
					Helpers.showWarningMessage(frame, "Please enter a school year or term for this class.");
					return;
				}
				Class activeClass = new Class(classNameTextField.getText(), schoolYrTextField.getText());
				Helpers.setActiveClass(activeClass);
				
				WelcomeForm.closeFrame();
				ClassOverviewForm.closeFrame();
				ClassOverviewForm classOverview = new ClassOverviewForm();
				Helpers.setClassOverviewForm(classOverview);
				classOverview.showForm();
				frame.dispose();
			}
		});
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 3;
		gbc_btnOk.gridy = 4;
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
		gbc_btnCancel.gridy = 4;
		add(btnCancel, gbc_btnCancel);
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
		frame = new JFrame("New Class");
		NewClassForm gui = new NewClassForm();
		frame.setContentPane(gui);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
}