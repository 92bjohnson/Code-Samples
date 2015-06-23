import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Student extends JPanel	{
	protected String firstName;
	protected String middleName;
	protected String lastName;
	protected String studentID;
	protected String emailAddress;
	protected String phoneNumber;
	protected Map<Integer, Object[]> grades = new HashMap<Integer, Object[]>();
	private boolean rawGradesLoaded = false;
	protected Integer ID;
	
	public Student(String _firstName, String _middleName, String _lastName, String _studentID, String _emailAddress, String _phoneNumber, Integer _ID)
	{
		firstName = _firstName;
		middleName = _middleName;
		lastName = _lastName;
		studentID = _studentID;
		emailAddress = _emailAddress;
		phoneNumber = _phoneNumber;
		ID = _ID;
	}
	
	public String getFullName()
	{
		String middleInitial = "";
		if (!middleName.equals(""))
		{
			middleInitial = middleName.substring(0, 1) + ". ";
		}
		return firstName + " " + middleInitial + lastName;
	}
	
	public String getAverageAsString()
	{
		double average = 0;
		
		if (!rawGradesLoaded)
		{
			for (Integer key : grades.keySet())
			{
				try
				{
					Double originalGrade = Double.parseDouble(grades.get(key)[0].toString());
					Integer daysLate = Integer.parseInt(grades.get(key)[1].toString());
					Helpers.activeClass.setGrade(this, Helpers.getActiveClass().assignments.get(key - 1), originalGrade, daysLate);
					rawGradesLoaded = true;
				}
				catch (Exception e)
				{
				}
			}
		}
		
		List<AssignmentPerformance> ap = Helpers.getActiveClass().getMyGrades(this);
		
		if (ap == null) return "0.0";
		
		double totalPointsScored = 0;
		double totalPointsPossible = 0;
		for (int i = 0; i < ap.size(); i++)
		{
			if (ap.get(i).isGraded())
			{
				totalPointsScored += ap.get(i).studentModifiedGrade;
				totalPointsPossible += ap.get(i).assignment.pointValue;
			}
		}
		average = (totalPointsScored/totalPointsPossible) * 100;
		average = Helpers.round(average);
		return String.valueOf(average);
	}
	
	protected void setRawGrade(Integer assignmentID, double originalGrade, int daysLate)
	{
		grades.put(assignmentID, new Object[] { originalGrade, daysLate });
	}
	
	protected Object[] getRawGrade(Integer assignmentID)
	{
		return grades.get(assignmentID);
	}
}