import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AssignmentPerformance extends JPanel	{
	protected double studentOriginalGrade = 0.0;
	protected double studentModifiedGrade = 0.0;
	protected int daysLate = 0;
	protected Assignment assignment;
	private boolean graded;
	
	public AssignmentPerformance(boolean _graded)
	{
		graded = _graded;
	}
	
	public void setGraded(boolean _graded)
	{
		graded = _graded;
	}
	
	public void setStudentGrade(Assignment _assignment, double _studentOriginalGrade, int _daysLate)
	{
		assignment = _assignment;
		studentOriginalGrade = Helpers.round(_studentOriginalGrade);
		daysLate = _daysLate;
		studentModifiedGrade = _studentOriginalGrade;
		
		int daysToPenalize = daysLate;
		
		if (assignment.penaltyAs == Assignment.PenaltyType.PERCENT)
		{
			while (daysToPenalize > 0)
			{
				studentModifiedGrade = studentModifiedGrade * .01 * (100 - assignment.latePenaltyPerDay);
				daysToPenalize -= 1;
			}
		}
		else
		{
			studentModifiedGrade -= (assignment.latePenaltyPerDay * daysLate);
		}
		studentModifiedGrade= Helpers.round(studentModifiedGrade);
		if (studentModifiedGrade < 0)
		{
			studentModifiedGrade = 0;
		}
	}
	
	public boolean isGraded()
	{
		return graded;
	}
}