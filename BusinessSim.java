import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.lang.Object;
import java.util.List;

public class BusinessSim extends JFrame implements ActionListener
{
	private JLabel lblStat = new JLabel("Statistics");				//labels the area where the statistics are displayed
	private JLabel lblBuy = new JLabel("Buy units");
	private JLabel lblAd = new JLabel("Get advertisements");
	private JLabel lblEList = new JLabel("Employees");
	
	private JButton btnHire = new JButton("Hire");					//hires employees
	private JButton btnMonth = new JButton("Go for the month");		//implement your plan for the month
	private JButton btnInfo = new JButton("Show all Statistics");	//shows all of your earinings in the statistics text area
	private JButton btnUp = new JButton(">");						//increases the amount to units you want to buy
	private JButton btnDown = new JButton("<");						//decreases the amount to units you want to buy
	private JButton btnEnter = new JButton("Enter #");				//manually enter the amount to units you want to buy
	
	private JTextField txtUnitNum = new JTextField();
	
	private JTextArea txtStats = new JTextArea();
	private JTextArea txtStorage = new JTextArea();					//a log
	
	private DefaultListModel dlm = new DefaultListModel();
	private JList workerList = new JList(dlm);
	private int i = 0;
	
	private JScrollPane scrWork = new JScrollPane(workerList);
	private JScrollPane scrStats = new JScrollPane(txtStats);
	
	private JRadioButton radCheck = new JRadioButton("Check");
	private JRadioButton radPromote = new JRadioButton("Promote");
	private JRadioButton radFire = new JRadioButton("Fire");
	
	private JCheckBox adWOM = new JCheckBox("Word of Mouth ($0)");
	private JCheckBox adFlr = new JCheckBox("Fliers ($500)");
	private JCheckBox adNp = new JCheckBox("Newspaper ($1,000)");
	private JCheckBox adRad = new JCheckBox("Radio: ($5,000)");
	private JCheckBox adNet = new JCheckBox("Internet: ($7,500)");
	private JCheckBox adTV = new JCheckBox("Television: ($10,000)");
	
	//Variables
	private String unitName = "";
	
	
	private String temp = "";
	private int numUnits = 0;
	private int numUnitsTemp = 0;
	
	private double bank = 25000.0;
	private double loan = 25000.0;
	private double interest = 0.03;
	private double gross = 0;
	private double monthCost = 0;
	private double net = 0;
	private double unitCost = 0;
	
	private int customers = 0;
	private int needHire = 500;
	private int sellPrice = 0;
	private int toggle = 0;
	private int month = 0;
	private int game = 0;
	
	private Random roll = new Random();
	
	
	private Map<String, String> employees = new HashMap<String, String>();
	private Set<String> set = employees.keySet();
	private List<String> names = new ArrayList<String>();
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAbout = new JMenu("About");
	
	private JMenuItem newGame = new JMenuItem("New Game");
	private JMenuItem quit = new JMenuItem("Exit");
	private JMenuItem about = new JMenuItem("About");
		
	
	public BusinessSim()
	{
	
		menuBar.add(mnuFile);
		menuBar.add(mnuAbout);
		mnuFile.add(newGame);
		mnuFile.add(quit);
		mnuAbout.add(about);
		
		adWOM.setSelected(true);
		radCheck.setSelected(true);
		
		txtUnitNum.setEditable(false);
		txtStats.setEditable(false);
		
		ButtonGroup bgActions = new ButtonGroup();
		bgActions.add(radCheck);
		bgActions.add(radPromote);
		bgActions.add(radFire);
		
		//Add button listeners
		btnHire.setActionCommand("hire");
    	btnHire.addActionListener(this);
    	btnMonth.setActionCommand("go");
    	btnMonth.addActionListener(this);
    	btnInfo.setActionCommand("stat");
    	btnInfo.addActionListener(this);
    	btnUp.setActionCommand("add");
    	btnUp.addActionListener(this);
    	btnDown.setActionCommand("subtract");
    	btnDown.addActionListener(this);
    	btnEnter.setActionCommand("enter");
    	btnEnter.addActionListener(this);
    	
    	newGame.setActionCommand("new");
    	newGame.addActionListener(this);
    	quit.setActionCommand("quit");
    	quit.addActionListener(this);
    	about.setActionCommand("about");
    	about.addActionListener(this);
		
		this.setJMenuBar(menuBar);
		
		workerList.addMouseListener(new ListMouseListener());
		
		JPanel c = new JPanel(new GridLayout(1,3));
			JPanel cStat = new JPanel(new BorderLayout());
			cStat.add(lblStat, BorderLayout.NORTH);
			cStat.add(scrStats, BorderLayout.CENTER);
			JPanel cBuy = new JPanel(new BorderLayout());
				JPanel cBUnits = new JPanel(new GridLayout(3, 1));
					JPanel cBUBtns = new JPanel(new BorderLayout());
					cBUBtns.add(btnDown, BorderLayout.WEST);
					cBUBtns.add(btnEnter, BorderLayout.CENTER);
					cBUBtns.add(btnUp, BorderLayout.EAST);
				cBUnits.add(lblBuy);
				cBUnits.add(txtUnitNum);
				cBUnits.add(cBUBtns);
				JPanel cBAd = new JPanel(new GridLayout(7, 1));
				cBAd.add(lblAd);
				cBAd.add(adWOM);
				cBAd.add(adFlr);
				cBAd.add(adNp);
				cBAd.add(adRad);
				cBAd.add(adNet);
				cBAd.add(adTV);
			cBuy.add(cBUnits, BorderLayout.NORTH);
			cBuy.add(cBAd, BorderLayout.CENTER);
			JPanel cWorkers = new JPanel(new GridLayout(2,1));
				JPanel cWList = new JPanel(new BorderLayout());
				cWList.add(lblEList, BorderLayout.NORTH);
				cWList.add(scrWork, BorderLayout.CENTER);
				JPanel cWActions = new JPanel(new GridLayout(4,1));
				cWActions.add(radCheck);
				cWActions.add(radPromote);
				cWActions.add(radFire);
				cWActions.add(btnHire);
			cWorkers.add(cWList);
			cWorkers.add(cWActions);
		c.add(cStat);
		c.add(cBuy);
		c.add(cWorkers);
		
		JPanel s = new JPanel(new FlowLayout());
		s.add(btnInfo);
		s.add(btnMonth);
		
		
		Container cont = getContentPane();
    	cont.add(c, BorderLayout.CENTER);
    	cont.add(s, BorderLayout.SOUTH);
	}
	
	private class ListMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if (dlm.isEmpty())
			{
				return;
			}
			String name = (String)workerList.getSelectedValue();
			if (e.getClickCount() == 2)
			{
				if(radCheck.isSelected())
				{
					JOptionPane.showMessageDialog(null, "" + name + " makes $" + employees.get(name) + " per hour.");
				}
				if(radPromote.isSelected())
				{
					String boost = employees.get(name);
					double increase = Double.parseDouble(boost) + 1.5;
					employees.put(name, "" + increase);
					JOptionPane.showMessageDialog(null, name + " has been working hard so you decide to promote " + name + " now his/her pay rate is $" + increase + " per/hr.");
				}
				if(radFire.isSelected())
				{
					employees.remove(name);
					names.remove(name);
					dlm.clear();
					i = 0;
					for (String nm : set)
					{
						dlm.add(i, nm);
						i++;
					}
					JOptionPane.showMessageDialog(null, "" + name + " was fired.");
					if(employees.size() == 0)
					{
						JOptionPane.showMessageDialog(null, "You now have no workers, and you attempt to work alone" + 
															"\nbut you go insane and sell your business. Start Again.");
						game = 0;
					}
				}
			}
		}
	}
	
    public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("go"))				
		{
			if (game == 0)
			{
				JOptionPane.showMessageDialog(null, "Start a new game.");
			}
			else
			{
			
				if(adWOM.isSelected())
				{
					customers += roll.nextInt(10);
				}
				if(adFlr.isSelected())
				{
					customers += roll.nextInt(100);
					monthCost += 500;
				}
				if(adNp.isSelected())
				{
					customers += roll.nextInt(500);
					monthCost += 1000;
				}
				if(adRad.isSelected())
				{
					customers += roll.nextInt(1000);
					monthCost += 5000;
				}
				if(adNet.isSelected())
				{
					customers += roll.nextInt(2000);
					monthCost += 7500;
				}
				if(adTV.isSelected())
				{
					customers += roll.nextInt(5000);
					monthCost += 10000;
				}
				month++;
				numUnits += numUnitsTemp;
				txtStats.setText("Month #: "+ month + "\nUnits bought: " + numUnitsTemp);
				double buyUnit = numUnitsTemp * unitCost;
				txtStats.append("\nUnit Price: $" + buyUnit);
				txtStats.append("\nUnits started: " + numUnits);
				monthCost +=  buyUnit;
			
				int whoBought = roll.nextInt(customers);
				if (whoBought > numUnits)
					whoBought = numUnits;
				txtStats.append("\nUnits sold: " + whoBought);
				numUnits -= whoBought;
				txtStats.append("\nUnits remaining: " + numUnits);
				
				double totalPay = 0;
				for(String i : set)
				{
					double pay = Double.parseDouble(employees.get(i));
					totalPay += pay * 7 * 6 * 4;
				}
				txtStats.append("\nEmployee pay: $" + totalPay);
				monthCost += totalPay;
				
				gross = whoBought * sellPrice;
				txtStats.append("\nGross: $" + gross);
				
				txtStats.append("\nThis month's cost: $" + monthCost);
			
				if (monthCost > gross)
				{
					double lost = monthCost - gross;
					txtStats.append("\nYou lost: $" + lost);
					bank -= lost;
				}
				else
				{
					net = gross - monthCost;
					txtStats.append("\nNet Pay: $" + net);
					bank += net;
				}
				if (net > loan)
				{
					net -= loan;
					loan = 0;
				}
				if (loan <= 0)
				{
						JOptionPane.showMessageDialog(null, "You are now free from debt. (You won the game)\nStart a new game or continue.");
				}
				else
				{
					double loanPay = net / 2; 
					txtStats.append("\nLoan Payment: $" + loanPay);
					loan -= loanPay;
					loan += loan * interest;
					txtStats.append("\nDebt remaining: $" + loan);
					bank -= loanPay;
				}
				if (bank <= 0)
				{
						JOptionPane.showMessageDialog(null, "You are now bankrupt. (You lost the game)\nStart a new game.");
						game = 0;
				}
				else
				{
					txtStats.append("\nBank: $" + bank);
				}
				int promote = roll.nextInt(100);
				if (promote > 94)
				{
					int randomGuy = roll.nextInt(names.size());
					String name = names.get(randomGuy);
					String boost = employees.get(name);
					double increase = Double.parseDouble(boost) + 1.5;
					employees.put(name, "" + increase);
					JOptionPane.showMessageDialog(null, name + " has been working hard so you decide to promote " + 
														name + " now his/her pay rate is $" + increase + " per/hr.");
					
				}
				if(whoBought > needHire)
				{
					needHire += needHire;
					JOptionPane.showMessageDialog(null, "You need more workers luckily you found some teenager to work for you.");
					String name = JOptionPane.showInputDialog("Enter his/her name: ");
					employees.put(name, "" + 10.25);
					names.add(name);
					dlm.clear();
					int i = 0;
					for (String nm : set)
					{
						dlm.add(i, nm);
						i++;
					}
					
				}
				int needFire = roll.nextInt(100);
				if (needFire > 96)
				{
					int randomGuy = roll.nextInt(names.size());
					String name = names.get(randomGuy);
					employees.remove(name);
					names.remove(name);
					dlm.clear();
					int i = 0;
					for (String nm : set)
					{
						dlm.add(i, nm);
						i++;
					}
					JOptionPane.showMessageDialog(null, "" + name + " was fired.");
					if(employees.size() == 0)
					{
						JOptionPane.showMessageDialog(null, "You now have no workers, and you attempt to work alone" + 
															"\nbut you go insane and sell your business. Start Again.");
						game = 0;
					}
				}
				monthCost = 0;
				gross = 0;
				net = 0;
				numUnitsTemp = 0;
				txtUnitNum.setText("" + numUnitsTemp);
				toggle = 0;
				
				txtStorage.append("\n\n-----\n\n" + txtStats.getText());
			}
		}
		if(e.getActionCommand().equals("hire"))
		{
			if (game == 0)
			{
				JOptionPane.showMessageDialog(null, "Start a new game.");
			}
			else
			{
				if (toggle == 0)
				{
				
					toggle = 1;
			
					int chance = roll.nextInt(100);
			
					if (chance > 25)
					{
						String name = JOptionPane.showInputDialog("Enter one name: ");
						employees.put(name, "" + 10.25);
						names.add(name);
						dlm.clear();
						int i = 0;
						for (String nm : set)
						{
							dlm.add(i, nm);
							i++;
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "You don't have any luck finding a new employee.");
					}
					toggle = 1;
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You already tried finding a new employee this month.");
				}
			}
		}
		if(e.getActionCommand().equals("stat"))
		{
			if (game == 0)
			{
				JOptionPane.showMessageDialog(null, "Start a new game.");
			}
			else
			{
				txtStats.setText(txtStorage.getText());
			}
		}
		if(e.getActionCommand().equals("add"))
		{
			if (game == 0)
			{
				JOptionPane.showMessageDialog(null, "Start a new game.");
			}
			else
			{
				numUnitsTemp += 10;
				txtUnitNum.setText("" + numUnitsTemp);
			}
		}
		if(e.getActionCommand().equals("subtract"))
		{
			if (game == 0)
			{
				JOptionPane.showMessageDialog(null, "Start a new game.");
			}
			else
			{
				numUnitsTemp -= 10;
				if(numUnits < 0)
				{
					JOptionPane.showMessageDialog(null, "You cannot sell units you already bought");
					numUnits = 0;
				}
				txtUnitNum.setText("" + numUnitsTemp);
			}
		}
		if(e.getActionCommand().equals("enter"))
		{
			if (game == 0)
			{
				JOptionPane.showMessageDialog(null, "Start a new game.");
			}
			else
			{
				String strNumUnits = JOptionPane.showInputDialog("Enter the amount you want to add.");
				numUnitsTemp += Integer.parseInt(strNumUnits);
				txtUnitNum.setText("" + numUnitsTemp);
			}
		}
		if(e.getActionCommand().equals("new"))
		{
			while (employees.isEmpty() == false)
			{
				employees.remove(names.remove(0));
			}
			String strSell = JOptionPane.showInputDialog("What do you want to sell?");
			sellPrice = roll.nextInt(100);
			if(sellPrice == 0)
				sellPrice = 1;
			double costPercent = roll.nextDouble();
			while (costPercent == 0)
			{
				costPercent = roll.nextDouble();
			}	
			double unitCostTemp1 = sellPrice * costPercent / 2;
			int unitCostTemp2 = (int)(unitCostTemp1 * 100);
			unitCost = unitCostTemp2 / 100;
			String Name1 = JOptionPane.showInputDialog("Enter the name of one of your friends: ");
			employees.put(Name1, "" + 10.25);
			names.add(Name1);
			
			
			
			dlm.clear();
			i = 0;
			for (String nm : set){
				dlm.add(i, nm);
				i++;
			}
		
			JOptionPane.showMessageDialog(null, "Ok so you were recently fired. You and your" + 
										"\ndeadbeat friend: " + Name1 + 
										"\nattempt to make your own bussiness selling " + strSell +
										".\n You decide to sell them for $" + sellPrice +
										"\nand they cost $" + unitCost + " to make." +
										"\nThey choose you to lead the business because you're" +
										"\nthe most experienced of the three. You apply for a loan" +
										"\nfor $25,000.00 dollars. You must pay them back with a 3%" +
										"\ninterest. You decide to have everyone work 6 days to work" + 
										"\nper week, and work 7 hours a day.");
		
			txtStats.setText("Month: 0\nBank: $25000\nLoan: $25000\nUnit Sell Price: $" + sellPrice + "\nUnit Cost: $" + unitCost);
			txtStorage.append(txtStats.getText());
			txtUnitNum.setText("0");
			game = 1;
			bank = 25000;
			loan = 25000;
			month = 0;
		}
		if(e.getActionCommand().equals("quit"))
		{
			System.exit(0);
		}
		if(e.getActionCommand().equals("about"))
		{
			JOptionPane.showMessageDialog(null, "Teacher: Mr. Ryan\nStudent: Christopher Pilien\n" + 
												"ICS4U1\nJanuary 25, 2012\nProgram: Business Simulator\n" + 
												"About: This program creates a simulated business which the\n" +
												"basic business actions can do. There are also Random events\n" +
												"that can occur which can alter the flow of your business.\n" +
												"The object of this program is to pay your loan.");
		}
	}
	
	public static void main(String[] args)
	{
		JOptionPane.showMessageDialog(null, "Teacher: Mr. Ryan\nStudent: Christopher Pilien\n" + 
												"ICS4U1\nJanuary 25, 2012\nProgram: Business Simulator\n" + 
												"About: This program creates a simulated business which the\n" +
												"basic business actions can do. There are also Random events\n" +
												"that can occur which can alter the flow of your business.\n" +
												"The object of this program is to pay your loan.");
    	BusinessSim gui = new BusinessSim();
		gui.setSize(600, 400);
    	gui.setTitle("Business Simulator");
    	gui.setVisible(true);
    	gui.setResizable(false);
    	gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setLocationRelativeTo(null);
    }
}