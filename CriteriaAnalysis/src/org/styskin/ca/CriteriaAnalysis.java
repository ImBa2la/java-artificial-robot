package org.styskin.ca;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.mvc.CriteriaTreeForm;

public class CriteriaAnalysis extends JFrame {

	private static final long serialVersionUID = -785301931655824129L;

	static {
		BasicConfigurator.configure();
	}

	private JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;

	private JMenu fileMenu = null;

	private JMenu editMenu = null;

	private JMenu helpMenu = null;

	private JMenuItem exitMenuItem = null;

	private JMenuItem aboutMenuItem = null;

	private JMenuItem cutMenuItem = null;

	private JMenuItem copyMenuItem = null;

	private JMenuItem pasteMenuItem = null;

	private JMenuItem saveMenuItem = null;

	private JTabbedPane jTabbedPane = null;

	private JMenuItem newMenuItem = null;

	private JMenuItem openMenuItem = null;

	private JToolBar treeToolBar = null;

	private JButton addComplexCriteriaButton = null;

	private JButton addSingleCriteriaButton = null;

	private JButton removeCriteriaButton = null;

	private JMenuItem optimizeMenu = null;

	/**
	 * This is the default constructor
	 */
	public CriteriaAnalysis() {
		super();
		initialize();
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		Dimension us = this.getSize(), them = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((them.width - us.width) / 2, (them.height - us.height) / 2);

	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(getJJMenuBar());
		this.setSize(700, 537);
		this.setContentPane(getJContentPane());
		this.setTitle("CriteriaAnalysis");
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getTreeToolBar(), java.awt.BorderLayout.NORTH);
			jContentPane.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 *
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getNewMunyItem());
			fileMenu.add(getOptimizeMenu());
			fileMenu.add(getOpenMenuItem());
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
		}
		return editMenu;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new JDialog(CriteriaAnalysis.this, "About", true).setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new JMenuItem();
			saveMenuItem.setText("Save");
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK, true));
			saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser(new File("./"));
					fileChooser.showSaveDialog((Component) e.getSource());
					File file = fileChooser.getSelectedFile();
					Criteria criteria = ((CriteriaTreeForm) getJTabbedPane().getSelectedComponent()).getCriteria();
					try {
						CriteriaXMLParser.saveXML(criteria, file);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(getJTabbedPane(), "Cannot save file");
					}
				}
			});
		}
		return saveMenuItem;
	}

	/**
	 * This method initializes jTabbedPane
	 *
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
						JTabbedPane pane = (JTabbedPane) e.getSource();
						pane.remove(pane.getSelectedIndex());
					}
				}
			});
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes newMunyItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getNewMunyItem() {
		if (newMenuItem == null) {
			newMenuItem = new JMenuItem();
			newMenuItem.setName("New");
			newMenuItem.setText("New");
			newMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JComponent treePanel = null;
					try {
						treePanel = new CriteriaTreeForm();
					} catch(Exception ex) {
						treePanel = new JLabel("Cannot create tree");
					}
					getJTabbedPane().add(treePanel);
				}
			});
		}
		return newMenuItem;
	}

	/**
	 * This method initializes openMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getOpenMenuItem() {
		if (openMenuItem == null) {
			openMenuItem = new JMenuItem();
			openMenuItem.setName("Open");
			openMenuItem.setText("Open");
			openMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser(new File("./"));
					fileChooser.showOpenDialog((Component) e.getSource());
					File file = fileChooser.getSelectedFile();
					JComponent treePanel = null;
					Criteria criteria = null;
					try {
						criteria = CriteriaXMLParser.loadXML(file);
					} catch(Exception ex) {
						treePanel = new JLabel("Cannot open specified file");
					}
					treePanel = new CriteriaTreeForm(criteria);
					getJTabbedPane().add(treePanel);
					getJTabbedPane().setSelectedComponent(treePanel);
				}
			});
		}
		return openMenuItem;
	}

	/**
	 * This method initializes treeToolBar
	 *
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getTreeToolBar() {
		if (treeToolBar == null) {
			treeToolBar = new JToolBar();
			treeToolBar.setPreferredSize(new java.awt.Dimension(18,30));
			treeToolBar.add(getAddComplexCriteriaButton());
			treeToolBar.add(getAddSingleCriteriaButton());
			treeToolBar.add(getRemoveCriteriaButton());
		}
		return treeToolBar;
	}

	/**
	 * This method initializes addCriteriaButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getAddComplexCriteriaButton() {
		if (addComplexCriteriaButton == null) {
//			addComplexCriteriaButton = new JButton(new ImageIcon(this.getClass().getResource("/img/AddCCriteria.gif")));
			addComplexCriteriaButton = new JButton(new ImageIcon("img/AddCCriteria.gif"));
			addComplexCriteriaButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					((CriteriaTreeForm) getJTabbedPane().getSelectedComponent()).addComplexCriteria();
				}
			});
		}
		return addComplexCriteriaButton;
	}

	/**
	 * This method initializes addSingleCriteriaButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getAddSingleCriteriaButton() {
		if (addSingleCriteriaButton == null) {
//			addSingleCriteriaButton = new JButton(new ImageIcon(this.getClass().getResource("/img/AddSCriteria.gif")));
			addSingleCriteriaButton = new JButton(new ImageIcon("img/AddSCriteria.gif"));
			addSingleCriteriaButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					((CriteriaTreeForm) getJTabbedPane().getSelectedComponent()).addSingleCriteria();
				}
			});
		}
		return addSingleCriteriaButton;
	}

	/**
	 * This method initializes removeCriteriaButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveCriteriaButton() {
		if (removeCriteriaButton == null) {
//			removeCriteriaButton = new JButton(new ImageIcon(this.getClass().getResource("/img/DelCriteria.gif")));
			removeCriteriaButton = new JButton(new ImageIcon("img/DelCriteria.gif"));
			removeCriteriaButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getJTabbedPane().getSelectedComponent() != null) {
						((CriteriaTreeForm) getJTabbedPane().getSelectedComponent()).removeCriteria();
					}
				}
			});
		}
		return removeCriteriaButton;
	}

	/**
	 * This method initializes optimizeMenu
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getOptimizeMenu() {
		if (optimizeMenu == null) {
			optimizeMenu = new JMenuItem();
			optimizeMenu.setText("Optimize");
			optimizeMenu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Criteria cr = ((CriteriaTreeForm) getJTabbedPane().getSelectedComponent()).getCriteria();
					Criteria criteria = null;
					try {
						criteria = cr.clone();
					} catch(CloneNotSupportedException ex) {}
//					double[][] F = Optimizer.getMatrix(criteria.getTotalSize(), 300);
					double[][] F = new double[300][criteria.getTotalSize()];
					try {
						Scanner inF = new Scanner(new File("F.txt"));
						for(int i = 0; i < F.length; i++) {
							for(int j = 0; j < F[i].length; j++) {
								F[i][j] = inF.nextDouble();
							}
						}
						inF.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					Optimizer optimizer = new Optimizer(criteria);

					JFileChooser fileChooser = new JFileChooser(new File("./"));
					fileChooser.showOpenDialog((Component) e.getSource());
					File file = fileChooser.getSelectedFile();
					Criteria base = null;
					try {
						base = CriteriaXMLParser.loadXML(file);
					} catch(Exception ex) {}
					optimizer.optimize(base, F);
					try {
						CriteriaXMLParser.saveXML(criteria, "cfg/testOpt.xml");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					JComponent treePanel = null;
					treePanel = new CriteriaTreeForm(criteria);
					getJTabbedPane().add(treePanel);
					getJTabbedPane().setSelectedComponent(treePanel);
				}
			});
		}
		return optimizeMenu;
	}

	/**
	 * Launches this application
	 */
	public static void main(String[] args) {
		CriteriaAnalysis application = new CriteriaAnalysis();
		application.setVisible(true);
	}

}  //  @jve:decl-index=0:visual-constraint="0,0"
