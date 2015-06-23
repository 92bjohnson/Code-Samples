import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DistributionGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6589109996611363086L;
	private static double[] values;
	private static String[] objects;
	private static String title;
	private static int windowWidth = 500;
	private static int windowHeight = 500;
	private int barShrinkFactor = 15;
	private static JFrame frame = new JFrame();

	public DistributionGraph(String _title, String[] _objects, double[] _values)
	{
		title = _title;
		values = _values;
		objects = _objects;
	}

	public void paintComponent(Graphics graphics)
	{
	    double[] valuesCopy = values.clone();
	    Arrays.sort(valuesCopy);
		double highestValue = valuesCopy[valuesCopy.length - 1];;
	    double lowestValue = 0.0;
	    
	    if (highestValue - lowestValue == 0) return;
	    		
		int panelWidth = getSize().width;
		int graphWidth = panelWidth/values.length;
		int panelHeight = getSize().height;
		
		graphics.setFont(new Font("Tahoma", Font.BOLD, 15));
		graphics.drawString(title, panelWidth/2 - 40, 20);
		
		double graphScale = (panelHeight - 35)/(highestValue - lowestValue);
		
		graphics.setFont(new Font("Tahoma", Font.PLAIN, 10));

		for (int i = 0; i < values.length; i++)
		{
			int  yCoordinate = 24 + (int) ((highestValue - values[i]) * graphScale);			
			graphics.setColor(Color.blue);
			graphics.fillRect((i * graphWidth) + barShrinkFactor, yCoordinate, graphWidth - barShrinkFactor * 2, (int) (values[i] * graphScale));
			graphics.setColor(Color.black);
			graphics.drawString(objects[i], i * graphWidth + (graphWidth - 7)/2, 470);
		}
	}
	
	public static void displayGraph()
	{
		frame.setSize(windowWidth, windowHeight);
		frame.getContentPane().add(new DistributionGraph(title, objects, values));
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public void closeForm()
	{
		frame.dispose();
	}
}