import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Assignment extends JPanel	{
	protected String name;
	protected int pointValue;
	protected int latePenaltyPerDay;
	protected PenaltyType penaltyAs;
	protected enum PenaltyType { PERCENT, POINTS };
	protected AssignmentTypes type;
	protected enum AssignmentTypes { HOMEWORK, QUIZ, EXAM, PROJECT, OTHER };
	protected double studentOriginalGrade, studentModifiedGrade;
	protected int daysLate;
	protected int ID;
	
	public Assignment(int _ID, String _name, String _type, int _pointValue, int _latePenaltyPerDay, String _penaltyAs)
	{
		ID = _ID;
		name = _name;
		pointValue = _pointValue;
		latePenaltyPerDay = _latePenaltyPerDay;
		penaltyAs = PenaltyType.valueOf(_penaltyAs.toUpperCase());
		type = AssignmentTypes.valueOf(_type.toUpperCase());
	}
	
	public double getAssignmentAverage()
	{
		List<AssignmentPerformance> assignmentResults = Helpers.activeClass.getStudentScores(this); 
		double average = 0;
		double totalPointsScored = 0;
		double totalPointsPossible = 0;
		for (int i = 0; i < assignmentResults.size(); i++)
		{
			if (assignmentResults.get(i).isGraded())
			{
				totalPointsScored += assignmentResults.get(i).studentModifiedGrade;
				totalPointsPossible += assignmentResults.get(i).assignment.pointValue;
			}
		}
		average = (totalPointsScored/totalPointsPossible) * 100;
		average = Helpers.round(average);
		return average;
	}
}