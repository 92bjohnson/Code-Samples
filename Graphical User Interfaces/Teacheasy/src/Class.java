import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Class extends JPanel	{
	public String className;
	private String schoolYr;
	private List<Student> students = new ArrayList<Student>();
	protected List<Assignment> assignments = new ArrayList<Assignment>();
	protected Map<Student, ArrayList<AssignmentPerformance>> studentPerformance = new HashMap<Student, ArrayList<AssignmentPerformance>>();
	
	public Class(String _className, String _schoolYr)
	{
		className = _className.trim();
		schoolYr = _schoolYr.trim();
	}
	
	protected List<Student> getListOfStudents()
	{
		return students;
	}
	
	protected List<Assignment> getListOfAssignments()
	{
		return assignments;
	}
	
	public void setClassName(String _className)
	{
		className = _className;
	}
	
	public String getClassName()
	{
		return className;
	}
	
	public void setSchoolYr(String _schoolYr)
	{
		schoolYr = _schoolYr;
	}
	
	public String getSchoolYr()
	{
		return schoolYr;
	}
	
	public void addStudent(Student student)
	{
		students.add(student);
	}
	
	public void addAssignment(Assignment assignment)
	{
		assignments.add(assignment);
	}
	
	public Student getStudent(int index)
	{
		if (index >= 0)
		{
			return students.get(index);
		}
		else
		{
			return null;
		}
	}
	
	public Assignment getAssignment(int index)
	{
		if (index >= 0)
		{
			return assignments.get(index);
		}
		else
		{
			return null;
		}
	}
	
	public String[][] getStudentsOverview()
	{
		String[][] studentsOverview = new String[students.size()][2];
		int counter = 0;
		for (Student student : students)
		{
			studentsOverview[counter] = new String[] { student.getFullName(), student.getAverageAsString() };
			counter++;
		}
		
		return studentsOverview;
	}
	
	public Object[][] getAssignmentsOverview()
	{		
		String[][] assignmentsOverview = new String[assignments.size()][3];
		int counter = 0;
		for (Assignment assignment : assignments)
		{
			String assignmentAverageAsString = "";
			try
			{
				assignmentAverageAsString = Double.toString(assignment.getAssignmentAverage());
				if (assignmentAverageAsString.equals("0.0"))
				{
					throw new Exception();
				}
			}
			catch (Exception e)
			{
				assignmentAverageAsString = "No Grades for this Assignment";
			}
			assignmentsOverview[counter] = new String[] { assignment.name, assignmentAverageAsString, Integer.toString(assignment.pointValue) };
			counter++;
		}
		
		return assignmentsOverview;
	}
	
	public Object[][] getStudentPerformanceDetails(Student student)
	{		
		List<AssignmentPerformance> myGrades = getMyGrades(student);
		String[][] performanceOverview = new String[assignments.size()][4];
		int counter = 0;
		for (Assignment assignment : assignments)
		{
			AssignmentPerformance ap = myGrades.get(assignment.ID);

			String modifiedGrade = String.valueOf(ap.studentModifiedGrade);
			String originalGrade = String.valueOf(ap.studentOriginalGrade);
			String daysLate = String.valueOf(ap.daysLate);
			String assignmentName = assignment.name;
			
			performanceOverview[counter] = new String[] { assignmentName, String.valueOf(assignment.pointValue), originalGrade, daysLate, Integer.toString(assignment.ID), modifiedGrade };
			counter++;
		}
		
		return performanceOverview;
	}
	
	public void setGrade(Student student, Assignment assignment, double originalGrade, int daysLate)
	{
		if (studentPerformance.get(student) == null)
		{
			studentPerformance.put(student, new ArrayList<AssignmentPerformance>(assignments.size()));			
		}
		while (assignments.size() + 1 > studentPerformance.get(student).size())
		{
			studentPerformance.get(student).add(new AssignmentPerformance(false));
		}
		AssignmentPerformance ap = studentPerformance.get(student).get(assignment.ID);
		if (ap.assignment == null)
		{
			ap = new AssignmentPerformance(true);
		}
		ap.setStudentGrade(assignment, originalGrade, daysLate);
		studentPerformance.get(student).set(assignment.ID, ap);
	}
	
	public List<AssignmentPerformance> getMyGrades(Student student)
	{
		return studentPerformance.get(student);
	}
	
	public List<AssignmentPerformance> getMyGradedGrades(Student student)
	{
		List<AssignmentPerformance> myGrades = getMyGrades(student);
		List<AssignmentPerformance> gradedGrades = new ArrayList<AssignmentPerformance>();
		for (int i = 0; i < myGrades.size(); i++)
		{
			if (myGrades.get(i).isGraded())
			{
				gradedGrades.add(myGrades.get(i));
			}
		}
		return gradedGrades;
	}
	
	public List<AssignmentPerformance> getStudentScores(Assignment assignment)
	{
		List<AssignmentPerformance> assignmentResults = new ArrayList<AssignmentPerformance>();
		for (Student student : studentPerformance.keySet())
		{
			try
			{
				assignmentResults.add(studentPerformance.get(student).get(assignment.ID));
			}
			catch (Exception e)
			{
				continue;
			}
		}
		return assignmentResults;
	}

	public Object[][] getAssignmentStatistics(Assignment assignment)
	{
		String[][] assignmentStatistics = new String[students.size()][4];
		int counter = 0;
		for (Student student : studentPerformance.keySet())
		{
			AssignmentPerformance ap = studentPerformance.get(student).get(assignment.ID);
			String modifiedGrade = Double.toString(ap.studentModifiedGrade);
			String daysLate = Integer.toString(ap.daysLate);
			String originalGrade = Double.toString(ap.studentOriginalGrade);
			assignmentStatistics[counter] = new String[] { student.getFullName(), originalGrade, daysLate, String.valueOf(student.ID), modifiedGrade };
			counter++;
		}
		return assignmentStatistics;
	}
	
	public void resizeGradeBook()
	{
		for (int i = 0; i < students.size(); i++)
		{
			Student student = students.get(i);
			if(studentPerformance.get(student) == null)
			{
				return;
			}
			while (assignments.size() + 1 > studentPerformance.get(student).size())
			{
				studentPerformance.get(student).add(new AssignmentPerformance(false));
			}
		}
	}
	
	public void removeStudent(Student student)
	{
		if (!Helpers.actionConfirmed("Are you sure you wish to delete " + student.getFullName() + " from the class? This action cannot be undone.")) return;
		students.remove(student);
		studentPerformance.remove(student);
		reIndexStudents();
		Helpers.updateClassOverviewForm();
	}

	private void reIndexStudents() {
		for (int i = 0; i < students.size(); i++)
		{
			students.get(i).ID = i;
		}
	}
	
	public void removeAssignment(Assignment assignment)
	{
		if (!Helpers.actionConfirmed("Are you sure you wish to delete " + assignment.name + " from the class? This action cannot be undone.")) return;
		assignments.remove(assignment);
		for (int i = 0; i < studentPerformance.size(); i++)
		{
			List<AssignmentPerformance> assignmentPerformance = studentPerformance.get(students.get(i));
			for (int j = assignment.ID; j < assignmentPerformance.size() - 1; j++)
			{
				assignmentPerformance.set(j, assignmentPerformance.get(j + 1));
			}
		}
		reIndexAssignments();
		Helpers.updateClassOverviewForm();
	}
	
	private void reIndexAssignments() {
		for (int i = 0; i < assignments.size(); i++)
		{
			assignments.get(i).ID = i + 1;
		}
	}
}