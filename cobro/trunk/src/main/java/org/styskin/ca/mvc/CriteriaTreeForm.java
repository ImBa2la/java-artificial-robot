package org.styskin.ca.mvc;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.ComplexCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.IntegralCriteria;
import org.styskin.ca.functions.SingleCriteria;
import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.functions.complex.PowerIOperator;
import org.styskin.ca.functions.single.SingleOperator;
import org.styskin.ca.model.ComplexFunction;
import org.styskin.ca.model.SingleFunction;
import org.styskin.ca.mvc.chart.ComplexGraphPanel;

public class CriteriaTreeForm extends JPanel {

	static Logger logger = Logger.getLogger(CriteriaTreeForm.class);

	private static final long serialVersionUID = -3071115953530320825L;

	private static final NumberFormat NUMBER_FORMAT = DecimalFormat.getInstance();

	private JScrollPane scrollPane = null;
	private JTree criteriaTree = null;
	private JTabbedPane controlPanel = null;
	private JPanel generalPanel = null;
	private JPanel functionPanel = null;
	
	private LambdaTableModel lambdaTableModel = null;
	
	private ComplexGraphPanel complexGraphPanel = new ComplexGraphPanel();
	
	static class CriteriaRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = -2638037386251154389L;
		private Icon complexCriteriaIcon;
		private Icon singleCriteriaIcon;

	    public CriteriaRenderer() {
			complexCriteriaIcon = new ImageIcon(CriteriaRenderer.class.getResource("/Parent.gif"));
			singleCriteriaIcon = new ImageIcon(CriteriaRenderer.class.getResource("/Child.gif"));
	    }

	    @Override
	    public Component getTreeCellRendererComponent(
	                        JTree tree,
	                        Object value,
	                        boolean sel,
	                        boolean expanded,
	                        boolean leaf,
	                        int row,
	                        boolean hasFocus) {

	        super.getTreeCellRendererComponent(
	                        tree, value, sel,
	                        expanded, leaf, row,
	                        hasFocus);

	        Criteria criteria = (Criteria)((DefaultMutableTreeNode) value).getUserObject();
	        if (criteria instanceof SingleCriteria) {
	            setIcon(singleCriteriaIcon);
	        } else {
	            setIcon(complexCriteriaIcon);
	        }
            setToolTipText(criteria.getName());
	        return this;
	    }
	}

	private Criteria criteria;

	private Panel panel = null;

	private Panel panel1 = null;

	private Button applyButton = null;

	private Button cancelButton = null;

	private JLabel nameLabel = null;

	private JLabel weightLabel = null;

	private JLabel operatorLabel = null;

	private JTextField nameEdit = null;

	private JTextField weightEdit = null;

	private JComboBox operatorCombo = null;
	private JComboBox singleOperatorCombo = null;

	{
		try {
			criteria = new IntegralCriteria(new PowerIOperator());
			criteria.setName("price");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	/**
	 * This is the default constructor
	 */

	public CriteriaTreeForm() {
		super();
		initialize();
	}



	public CriteriaTreeForm(Criteria cr) {
		super();
		criteria = cr;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(690, 460);
		this.setName("CriteriaTree");
		this.add(getJSplitPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes scrollPane
	 *
	 * @return java.awt.ScrollPane
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getCriteriaTree());
		}
		return scrollPane;
	}

	/**
	 * This method initializes criteriaTree
	 *
	 * @return javax.swing.JTree
	 */
	private JTree getCriteriaTree() {
		if (criteriaTree == null) {
	        DefaultMutableTreeNode root = new DefaultMutableTreeNode(criteria);
	        for(Criteria cr : criteria.getChildren()) {
	        	addCriteria(cr, root);
	        }
			criteriaTree = new JTree(root);
			criteriaTree.setCellRenderer(new CriteriaRenderer());
			criteriaTree.setBounds(new java.awt.Rectangle(5,377,78,72));
			criteriaTree.setEditable(false);
	        criteriaTree.addMouseListener(new java.awt.event.MouseAdapter() {
	        	public void mouseClicked(java.awt.event.MouseEvent e) {
	        		reloadPanel();
	        	}
	        });
//	        criteriaTree = new JTree();
		}
		return criteriaTree;
	}

	/**
	 * This method initializes controlPanel
	 *
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JTabbedPane();
			controlPanel.addTab("Общие", null, getGeneralPanel(), null);
			controlPanel.addTab("Функция перевода", null, getFunctionPanel(), null);
		}
		return controlPanel;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getGeneralPanel() {
		if (generalPanel == null) {
			generalPanel = new JPanel();
			generalPanel.setLayout(new GridLayout());
			generalPanel.add(getJSplitPane1(), null);
		}
		return generalPanel;
	}

	/**
	 * This method initializes function
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getFunctionPanel() {
		if (functionPanel == null) {
			functionPanel = complexGraphPanel.getPanel();
			functionPanel.setVisible(true);
			functionPanel.setEnabled(false);
		}
		return functionPanel;
	}

    private void addCriteria(Criteria criteria, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(criteria);
        parent.add(node);
        for(Criteria cr : criteria.getChildren()) {
        	addCriteria(cr, node);
        }
    }

	/**
	 * This method initializes panel
	 *
	 * @return java.awt.Panel
	 */
	private Panel getPanel() {
		if (panel == null) {
			operatorLabel = new JLabel();
			operatorLabel.setText("Оператор агрегирования");
			operatorLabel.setBounds(new java.awt.Rectangle(15,95,149,23));
			weightLabel = new JLabel();
			weightLabel.setText("Вес");
			weightLabel.setBounds(new java.awt.Rectangle(16,62,106,20));
			nameLabel = new JLabel();
			nameLabel.setText("Имя");
			nameLabel.setBounds(new java.awt.Rectangle(15,25,124,20));
			panel = new Panel();
			panel.setLayout(null);
			panel.add(operatorLabel, null);
			panel.add(weightLabel, null);
			panel.add(nameLabel, null);
			panel.add(getNameEdit(), null);
			panel.add(getWeightEdit(), null);
			panel.add(getOperatorCombo(), null);
			panel.add(getSingleOperatorCombo(), null);
			panel.add(getJScrollPane(), null);
		}
		return panel;
	}



	/**
	 * This method initializes panel1
	 *
	 * @return java.awt.Panel
	 */
	private Panel getPanel1() {
		if (panel1 == null) {
			panel1 = new Panel();
			panel1.setComponentOrientation(java.awt.ComponentOrientation.UNKNOWN);
			panel1.add(getApplyButton(), null);
			panel1.add(getCancelButton(), null);
		}
		return panel1;
	}



	/**
	 * This method initializes applyButton
	 *
	 * @return java.awt.Button
	 */
	private Button getApplyButton() {
		if (applyButton == null) {
			applyButton = new Button();
			applyButton.setLabel("Применить");
			applyButton.setEnabled(false);
			applyButton.setMinimumSize(new java.awt.Dimension(80,25));
			applyButton.addActionListener(new java.awt.event.ActionListener(){

				public void actionPerformed(java.awt.event.ActionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent();
					Criteria cr = (Criteria) node.getUserObject();
					ComplexCriteria parent = getCriteriaTree().getSelectionPath().getParentPath() == null ? null : (ComplexCriteria) ((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
					try {
						cr.setName(getNameEdit().getText());
						if (parent != null) {
							double weight = NUMBER_FORMAT.parse(getWeightEdit().getText()).doubleValue();
							parent.getOperator().getWeights().set(parent.getChildren().indexOf(cr), weight);
						}						
						if (cr instanceof ComplexCriteria) {
							ComplexCriteria criteria = (ComplexCriteria) cr;
							ComplexFunction function = (ComplexFunction) getOperatorCombo().getSelectedItem();
							ComplexOperator op = criteria.getOperator();
							if (ComplexFunction.getFunction(op.getClass()) != function) {
								ComplexOperator newOperator = function.createOperator();
								Map<String, Double> lambda = new HashMap<String, Double>();
								op.saveParameters(lambda);								
								newOperator.loadParameters(lambda);
								newOperator.setWeights(op.getWeights());
								newOperator.refresh();
								criteria.setOperator(newOperator);
							}
						} else if(cr instanceof SingleCriteria) {
							SingleCriteria criteria = (SingleCriteria) cr;
							SingleOperator op = criteria.getOperator();
							SingleFunction func = (SingleFunction) getSingleOperatorCombo().getSelectedItem();
							if(SingleFunction.getFunction(op.getClass()) != func) {
								SingleOperator newOp = func.createOperator();							
								Map<String, Double> lambda = new HashMap<String, Double>();
								op.saveParameters(lambda);								
								newOp.loadParameters(lambda);
								criteria.setOperator(newOp);
							}
						}
						getLambdaTableModel().save();
						updateOperator(criteria);							
						enablePanelApplyChanges(false);
						getCriteriaTree().updateUI();
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(getPanel1(), "Cannot save parameters");
					}
				}
			});
		}
		return applyButton;
	}



	/**
	 * This method initializes cancelButton
	 *
	 * @return java.awt.Button
	 */
	private Button getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new Button();
			cancelButton.setLabel("Отменить");
			cancelButton.setEnabled(false);
			cancelButton.setMinimumSize(new java.awt.Dimension(80,25));
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					reloadPanel();
					enablePanelApplyChanges(false);
				}
			});
		}
		return cancelButton;
	}



	class EnablePanel1 extends KeyAdapter {
		public void keyTyped(java.awt.event.KeyEvent e) {
			enablePanelApplyChanges(true);
		}
	}

	private EnablePanel1 enablePanel1 = new EnablePanel1();  //  @jve:decl-index=0:

	private JSplitPane jSplitPane = null;

	private JSplitPane jSplitPane1 = null;

	private JScrollPane jScrollPane = null;

	private JTable lambdaTable = null;

	/**
	 * This method initializes nameEdit
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getNameEdit() {
		if (nameEdit == null) {
			nameEdit = new JTextField();
			nameEdit.setBounds(new java.awt.Rectangle(180,24,138,20));
			nameEdit.addKeyListener(enablePanel1);
		}
		return nameEdit;
	}



	/**
	 * This method initializes 
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getWeightEdit() {
		if (weightEdit == null) {
			weightEdit = new JTextField();
			weightEdit.setBounds(new java.awt.Rectangle(181,61,137,20));
			weightEdit.addKeyListener(enablePanel1);
		}
		return weightEdit;
	}



	/**
	 * This method initializes operatorCombo
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getOperatorCombo() {
		if (operatorCombo == null) {
			operatorCombo = new JComboBox(ComplexFunction.values());
			operatorCombo.setAutoscrolls(true);
			operatorCombo.setBounds(new java.awt.Rectangle(182,97,131,25));
			operatorCombo.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					enablePanelApplyChanges(true);
				}
			});
		}
		return operatorCombo;
	}
	
	private JComboBox getSingleOperatorCombo() {
		if (singleOperatorCombo == null) {
			singleOperatorCombo = new JComboBox(SingleFunction.values());
			singleOperatorCombo.setAutoscrolls(true);
			singleOperatorCombo.setBounds(new java.awt.Rectangle(182,97,131,25));
			singleOperatorCombo.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					enablePanelApplyChanges(true);
//					updateOperator(criteria);
				}
			});
		}
		return singleOperatorCombo;
	}
	
	
	public LambdaTableModel getLambdaTableModel() {
		if(lambdaTableModel == null) {
			lambdaTableModel = new LambdaTableModel(new Runnable() {
				public void run() {
					enablePanelApplyChanges(true);
				}
			});
			
			lambdaTableModel.setSaveLoadParameters(getCriteria().getOperator());			
		}
		return lambdaTableModel;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void addComplexCriteria() {
		ComplexCriteria cr = null;
		try {
			cr = new ComplexCriteria(new PowerIOperator());
		} catch (Exception ex) {}
		ComplexCriteria parent = getCriteriaTree().getSelectionPath() == null ? null : (ComplexCriteria) ((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent()).getUserObject();
		if (parent != null) {
			parent.addChild(cr, 1);
			((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent()).add(new DefaultMutableTreeNode(cr));
		}
		enablePanelApplyChanges(true);
		getCriteriaTree().updateUI();
	}

	public void addSingleCriteria() {
		SingleCriteria cr = null;
		try {
			cr = new SingleCriteria(new SingleOperator());
		} catch (Exception ex) {}
		ComplexCriteria parent = getCriteriaTree().getSelectionPath() == null ? null : (ComplexCriteria) ((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent()).getUserObject();
		if (parent != null) {
			parent.addChild(cr, 1);
			((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent()).add(new DefaultMutableTreeNode(cr));
		}
		enablePanelApplyChanges(true);
		getCriteriaTree().updateUI();
	}

	public void removeCriteria() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent();
		Criteria cr = (Criteria) node.getUserObject();
		ComplexCriteria parent = getCriteriaTree().getSelectionPath().getParentPath() == null ? null : (ComplexCriteria) ((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
		if (parent != null) {
			parent.removeChild(cr);
		}
		if (node.getParent() != null) {
			((DefaultMutableTreeNode) node.getParent()).remove(node);
		}
		enablePanelApplyChanges(false);
		getCriteriaTree().updateUI();
	}

	private void enablePanelApplyChanges(boolean enable) {
		getApplyButton().setEnabled(enable);
		getCancelButton().setEnabled(enable);
	}

	private void reloadPanel() {
		if (getCriteriaTree().getSelectionPath() == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent();
		Criteria cr = (Criteria) node.getUserObject();
		ComplexCriteria parent = getCriteriaTree().getSelectionPath().getParentPath() == null ? null : (ComplexCriteria) ((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
		getNameEdit().setText(cr.getName());
		double weight = parent == null ? 1 : parent.getOperator().getWeights().get(parent.getChildren().indexOf(cr));
		getWeightEdit().setText( NUMBER_FORMAT.format( weight));
		if (cr instanceof ComplexCriteria) {
			ComplexCriteria criteria = (ComplexCriteria) cr;
			getOperatorCombo().setSelectedItem( ComplexFunction.getFunction(criteria.getOperator().getClass()));
			updateOperator(criteria);			

			getOperatorCombo().setVisible(true);
			getSingleOperatorCombo().setVisible(false);
		} else if (cr instanceof SingleCriteria) {
			SingleCriteria criteria = (SingleCriteria) cr;
			getSingleOperatorCombo().setSelectedItem( SingleFunction.getFunction(criteria.getOperator().getClass()));
			updateOperator(criteria);
			
			getOperatorCombo().setVisible(false);
			getSingleOperatorCombo().setVisible(true);
		}
		enablePanelApplyChanges(false);
	}



	private void updateOperator(Criteria criteria) {
		if(criteria == null)
			return;
		getLambdaTableModel().setSaveLoadParameters(criteria.getOperator());
		getLambdaTableModel().fireTableDataChanged();
		if(criteria.getOperator() instanceof ComplexOperator)
			complexGraphPanel.updateChart((ComplexOperator) criteria.getOperator());
	}


	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(300);
			jSplitPane.setLeftComponent(getScrollPane());
			jSplitPane.setRightComponent(getControlPanel());
		}
		return jSplitPane;
	}



	/**
	 * This method initializes jSplitPane1	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane1() {
		if (jSplitPane1 == null) {
			jSplitPane1 = new JSplitPane();
			jSplitPane1.setBounds(new java.awt.Rectangle(5,436,202,28));
			jSplitPane1.setDividerLocation(350);
			jSplitPane1.setTopComponent(getPanel());
			jSplitPane1.setBottomComponent(getPanel1());
			jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		}
		return jSplitPane1;
	}



	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(15, 135, 298, 193));
			jScrollPane.setViewportView(getLambdaTable());
		}
		return jScrollPane;
	}



	/**
	 * This method initializes lambdaTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getLambdaTable() {
		if (lambdaTable == null) {
			lambdaTable = new JTable(getLambdaTableModel());
		}
		return lambdaTable;
	}


}  //  @jve:decl-index=0:visual-constraint="-101,-41"
