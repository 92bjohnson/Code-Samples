import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Helpers extends JPanel	{
	public static Class activeClass;
	public static ClassOverviewForm classOverviewForm;
	
	public static void showWarningMessage(JFrame frame, String message)
	{
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void showErrorMessage(JFrame frame, String message)
	{
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showInformationalMessage(JFrame frame, String message)
	{
		JOptionPane.showMessageDialog(frame, message, "Tip", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static boolean isEmpty(JTextField textField)
	{
		if (textField.getText().trim().length() == 0) return true;
		return false;
	}
	
	public static boolean containsNumbers(JTextField textField)
	{
		if (textField.getText().matches(".*\\d.*")) return true;
		return false;
	}
	
	public static boolean isANumber(JTextField textField)
	{
		return isANumber(textField.getText());
	}
	
	public static boolean isANumber(String text)
	{
		try
		{
			Double.parseDouble(text);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static double round(double number)
	{
		return Math.round(number * 10.0)/10.0;
	}
	
	public static String formatPhoneNumber(JTextField textField)
	{
		try
		{
			if (isEmpty(textField)) return null;
			String phoneNumber = textField.getText().replace("-", "");
			phoneNumber = phoneNumber.replace("(", "").replace(")", ""); 
			if (phoneNumber.length() != 10) throw new Exception();
			Long.parseLong(phoneNumber);
			phoneNumber = String.format("(%s)-%s-%s", phoneNumber.substring(0,3), phoneNumber.substring(3, 6), phoneNumber.substring(6, 10));
			return phoneNumber;	
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static boolean invalidEmailAddress(JTextField textField)
	{
		if (!textField.getText().matches(".*@.*")) return true;
		return false;
	}
	
	public static void setActiveClass(Class _activeClass)
	{
		activeClass = _activeClass;
	}
	
	public static void setClassOverviewForm(ClassOverviewForm _classOverviewForm)
	{
		classOverviewForm = _classOverviewForm;
	}
	
	public static Class getActiveClass()
	{
		return activeClass;
	}
	
	public static void readCSV(String fileName)
	{
		BufferedReader br = null;
		
		try
		{
			br = new BufferedReader(new FileReader(fileName));
			String delimiter = ",";
			loadClassInfo(br, delimiter);
			loadStudents(br, delimiter);
			loadAssignments(br, delimiter);
		}
		catch (Exception e)
		{		
		}
		finally
		{
			try
			{
				br.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static void loadClassInfo(BufferedReader br, String delimiter) throws IOException {
		String line = br.readLine();
		String className = "";
		String term = "";
		
		while (!line.toLowerCase().startsWith("students in this class"))
		{
			line = replaceNullElementWithEmpty(line, delimiter);
			String[] elements = line.split(delimiter);
			if (elements[0].trim().equals("Class Name")) className = elements[1];
			else if (elements[0].trim().equals("Term")) term = elements[1];
			line = br.readLine();
		}
		activeClass = new Class(className, term);
	}

	private static String replaceNullElementWithEmpty(String line, String delimiter) { 
		return line.replace(delimiter, " " + delimiter);
	}

	private static void loadStudents(BufferedReader br, String delimiter) throws IOException {
		String line = br.readLine();
		line = br.readLine();
		
		Student student = null;
		while (!line.toLowerCase().startsWith("assignments for this class"))
		{
			line = replaceNullElementWithEmpty(line, delimiter);
			String[] elements = line.split(delimiter);
			
			for (int i = 0; i < elements.length; i++)
			{
				elements[i] = elements[i].trim();
			}
			
			if (!elements[0].trim().equals(""))
			{
				if (student != null)
				{
					activeClass.addStudent(student);
				}
				
				String fName = elements[0];
				String mName = elements[1];
				String lName = elements[2];
				String studentID = elements[3];
				String emailAddress = elements[4];
				String phoneNumber = elements[5];
				Integer ID = Helpers.getActiveClass().getListOfStudents().size();
				
				student = new Student(fName, mName, lName, studentID, emailAddress, phoneNumber, ID);
			}
			if (!elements[6].trim().equals(""))
			{
				if (student != null)
				{
					readGrades(br, student, line, delimiter, elements);
				}
			}
			line = br.readLine();
		}
		activeClass.addStudent(student);
	}
	
	private static void readGrades(BufferedReader br, Student student, String line, String delimiter, String[] elements) {
		try
		{
			int assignmentID = isANumber(elements[6]) ? Integer.parseInt(elements[6]) : 0;
			double originalGrade = isANumber(elements[7]) ? Double.parseDouble(elements[7]) : 0;
			int daysLate = isANumber(elements[8]) ? Integer.parseInt(elements[8]) : 0;
			student.setRawGrade(assignmentID, originalGrade, daysLate);
		}
		catch (Exception e)
		{
			return;
		}
	}

	private static void loadAssignments(BufferedReader br, String delimiter) throws IOException {
		String line = br.readLine();
		line = br.readLine();
		boolean nonNumbersDetected = false;
		
		while (line != null)
		{
			line = replaceNullElementWithEmpty(line, delimiter);
			String[] elements = line.split(delimiter);
			
			for (int i = 0; i < elements.length; i++)
			{
				elements[i] = elements[i].trim();
			}
			
			int ID = Helpers.isANumber(elements[0]) ? Integer.parseInt(elements[0]) : activeClass.assignments.size() + 1;
			String assignmentName = elements[1];
			String assignmentType = elements[2];
			
			int pointValue = 0;
			if (Helpers.isANumber(elements[3]))
			{
				pointValue = Integer.parseInt(elements[3]);
			}
			else
			{
				nonNumbersDetected = true;
			}
			
			int latePenalty = 0;		
			if (Helpers.isANumber(elements[4]))
			{
				latePenalty = Integer.parseInt(elements[4]);
			}
			else
			{
				nonNumbersDetected = true;
			}
			
			String penaltyType = elements[5].equals("%") ? "PERCENT" : "POINTS";
			
			Assignment assignment = new Assignment(ID, assignmentName, assignmentType, pointValue, latePenalty, penaltyType);
			activeClass.addAssignment(assignment);
			line = br.readLine();
		}
		
		if (nonNumbersDetected)
		{
			JFrame frame = new JFrame();
			Helpers.showWarningMessage(frame, "Invalid point values or late penalty values were detected for one or more assignments. Please check the input file for invalid values.");
		}
	}


	public static String selectFile()
	{
		JFileChooser openFileDialog = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
		openFileDialog.setFileFilter(filter);
		if(openFileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			return openFileDialog.getSelectedFile().getAbsolutePath();
		}
		else
		{
			return null;
		}
	}
	
	protected static void updateClassOverviewForm()
	{
		classOverviewForm.updateForm();
	}
	
	protected static void saveClass(JFrame frame)
	{
        JFileChooser saveFileDialog = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV File", "csv");
		saveFileDialog.setFileFilter(filter);
        saveFileDialog.showSaveDialog(null);
        if (saveFileDialog.getSelectedFile() == null)
        {
        	return;
        }
        String fileName = saveFileDialog.getSelectedFile().toString();
        
		String outputString;
		String lineTemplate = "{0},{1},{2},{3},{4},{5},{6},{7},{8},{9},\n";
		String e = "";
		outputString = MessageFormat.format(lineTemplate, "Class Name", activeClass.getClassName(), e, e, e, e, e, e, e, e);
		outputString += MessageFormat.format(lineTemplate,  "Term", activeClass.getSchoolYr(), e, e, e, e, e, e, e, e);
		outputString += MessageFormat.format(lineTemplate, e, e, e, e, e, e, e, e, e, e);
		outputString += MessageFormat.format(lineTemplate, "Students in this Class", e, e, e, e, e, e, e, e, e, e);
		outputString += MessageFormat.format(lineTemplate, "First Name", "Middle Name", "Last Name", "Student ID", "Email Address", "Phone Number",
				"Assignment ID", "Original Grade", "Days Late", "Modified Grade");
		outputString += saveStudents(lineTemplate, e);
		outputString += MessageFormat.format(lineTemplate, e, e, e, e, e, e, e, e, e, e);
		outputString += MessageFormat.format(lineTemplate, "Assignments for this Class", e, e, e, e, e, e, e, e, e, e);
		outputString += MessageFormat.format(lineTemplate, "ID", "Assignment", "Assignment Type", "Point Value", "Late Penalty", "Penalty As", e, e, e, e, e);
		outputString += saveAssignments(lineTemplate, e);
		
		writeToFile(frame, fileName, outputString);
	}
	
	protected static String saveStudents(String lineTemplate, String e)
	{
		String outputString = "";
		List<Student> students = activeClass.getListOfStudents(); 
		for (int i = 0; i < students.size(); i++)
		{
			Student student = students.get(i);
			List<AssignmentPerformance> myGradedGrades = activeClass.getMyGradedGrades(student);
			AssignmentPerformance ap = myGradedGrades.get(0);
			Assignment assignment = ap.assignment;
			
			outputString += MessageFormat.format(lineTemplate, student.firstName, student.middleName, student.lastName, student.studentID, student.emailAddress, student.phoneNumber,
					assignment.ID, ap.studentOriginalGrade, ap.daysLate, ap.studentModifiedGrade);
			
			for (int j = 1; j < myGradedGrades.size(); j++)
			{
				ap = myGradedGrades.get(j);
				assignment = ap.assignment;
				
				outputString += MessageFormat.format(lineTemplate, e, e, e, e, e, e, assignment.ID, ap.studentOriginalGrade, ap.daysLate, ap.studentModifiedGrade);
			}
		}
		return outputString;
	}
	
	protected static String saveAssignments(String lineTemplate, String e)
	{
		String outputString = "";
		for (int i = 0; i < activeClass.assignments.size(); i++)
		{
			Assignment assignment = activeClass.assignments.get(i);
			
			String penaltyAs = assignment.penaltyAs.toString().equals("PERCENT") ? "%" : String.valueOf(assignment.penaltyAs);
			outputString += MessageFormat.format(lineTemplate, assignment.ID, assignment.name, assignment.type, assignment.pointValue, assignment.latePenaltyPerDay, penaltyAs,
					e, e, e, e);
		}
		return outputString;
	}
	
	public static void writeToFile(JFrame frame, String fileName, String content)
	{
		if (!fileName.toLowerCase().endsWith(".csv"))
		{
			fileName += ".csv";
		}
		File outputFile = new File(fileName);
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			bw.write(content);
			bw.close();
		}
		catch (IOException e)
		{
			showWarningMessage(frame, "An error has occured while saving this class.");
		}
	}
	
	public static void exitProgram(JFrame frame)
	{
		int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to save changes made to this class?", "Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION);
		if (dialogResult == JOptionPane.CANCEL_OPTION)
		{
			frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}
		else if (dialogResult == JOptionPane.YES_OPTION)
		{
			Helpers.saveClass(frame);
			System.exit(0);
		}
		else if (dialogResult == JOptionPane.NO_OPTION)
		{
			System.exit(0);
		}
		else
		{
			System.exit(1);
		}
	}
	
	public static boolean actionConfirmed(String message)
	{
		int dialogResult = JOptionPane.showConfirmDialog (null, message, "Action Cannot Be Undone", JOptionPane.YES_NO_OPTION);
		if (dialogResult == JOptionPane.YES_OPTION)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}