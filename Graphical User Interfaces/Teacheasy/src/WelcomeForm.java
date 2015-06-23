import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class WelcomeForm extends JPanel	{
	private static JFrame frame;
	
	public WelcomeForm() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{60, 0, 60, 0};
		gridBagLayout.rowHeights = new int[]{23, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		ImageIcon iconLogo = new ImageIcon("Resources/Teacheasy.png");
		
		JLabel lblTest = new JLabel("");
		lblTest.setIcon(iconLogo);
		GridBagConstraints gbc_lblTest = new GridBagConstraints();
		gbc_lblTest.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTest.insets = new Insets(0, 0, 5, 5);
		gbc_lblTest.gridx = 1;
		gbc_lblTest.gridy = 1;
		add(lblTest, gbc_lblTest);
		
		final JButton newClassButton = new JButton("New Class");
		newClassButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewClassForm newClass = new NewClassForm();
				newClass.showForm();
			}
		});
		GridBagConstraints gbc_newClassButton = new GridBagConstraints();
		gbc_newClassButton.anchor = GridBagConstraints.NORTH;
		gbc_newClassButton.insets = new Insets(0, 0, 5, 5);
		gbc_newClassButton.gridx = 1;
		gbc_newClassButton.gridy = 2;
		add(newClassButton, gbc_newClassButton);
		
		final JButton loadClassButton = new JButton("Load Class");
		loadClassButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadClass();
			}
		});
		GridBagConstraints gbc_loadClassButton = new GridBagConstraints();
		gbc_loadClassButton.insets = new Insets(0, 0, 5, 5);
		gbc_loadClassButton.anchor = GridBagConstraints.NORTH;
		gbc_loadClassButton.gridx = 1;
		gbc_loadClassButton.gridy = 3;
		add(loadClassButton, gbc_loadClassButton);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem newClassItem = new JMenuItem("New Class");
		newClassItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewClassForm newClass = new NewClassForm();
				newClass.showForm();
			}
		});
		JMenuItem loadClassItem = new JMenuItem("Load Class");
		loadClassItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadClass();
			}
		});
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menu.add(newClassItem);
		menu.add(loadClassItem);
		menu.add(exitItem);
		frame.setJMenuBar(menuBar);
	}
	
	public static void main(String[] args)
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
		frame = new JFrame("Welcome to Teacheasy!");
		WelcomeForm gui = new WelcomeForm();
		frame.setContentPane(gui);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	public static void closeFrame()
	{
		frame.dispose();
	}
	
	public static void loadClass()
	{
		String csvFile = Helpers.selectFile();
		if (csvFile == null) return;
		try
		{
			Helpers.readCSV(csvFile);
			ClassOverviewForm.closeFrame();
			ClassOverviewForm classOverview = new ClassOverviewForm();
			classOverview.showForm();
			Helpers.setClassOverviewForm(classOverview);
			frame.dispose();
		}
		catch (Exception e)
		{
			Helpers.showErrorMessage(frame, "Error loading file. Please make sure the file exists and is in CSV format.");
		}
	}
}