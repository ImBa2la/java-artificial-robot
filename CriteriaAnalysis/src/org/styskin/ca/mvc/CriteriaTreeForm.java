package org.styskin.ca.mvc;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.ComplexCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.SingleCriteria;
import org.styskin.ca.functions.complex.AdditiveOperator;
import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.model.ComplexFunction;

public class CriteriaTreeForm extends JPanel {

	static Logger logger = Logger.getLogger(CriteriaTreeForm.class);

	private static final long serialVersionUID = -3071115953530320825L;

	private static final NumberFormat NUMBER_FORMAT = DecimalFormat.getInstance();

	private JScrollPane scrollPane = null;
	private JTree criteriaTree = null;
	private JTabbedPane controlPanel = null;
	private JPanel generalPanel = null;
	private JPanel functionPanel = null;


	static class CriteriaRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = -2638037386251154389L;
		private Icon complexCriteriaIcon;
		private Icon singleCriteriaIcon;

	    public CriteriaRenderer() {
			complexCriteriaIcon = new ImageIcon(CriteriaRenderer.class.getResource("/img/Parent.gif"));
			singleCriteriaIcon = new ImageIcon(CriteriaRenderer.class.getResource("/img/Child.gif"));
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

	private JLabel lambdaLabel = null;

	private JTextField nameEdit = null;

	private JTextField weightEdit = null;

	private JTextField lambdaEdit = null;

	private JComboBox operatorCombo = null;

	{
		try {
			criteria = new ComplexCriteria(new AdditiveOperator());
			criteria.setName("Интегральный критерий");
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
			functionPanel = new JPanel();
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
			lambdaLabel = new JLabel();
			lambdaLabel.setText("Коэффициент жесткости");
			lambdaLabel.setBounds(new java.awt.Rectangle(15,135,148,19));
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
			panel.add(lambdaLabel, null);
			panel.add(operatorLabel, null);
			panel.add(weightLabel, null);
			panel.add(nameLabel, null);
			panel.add(getNameEdit(), null);
			panel.add(getWeightEdit(), null);
			panel.add(getLambdaEdit(), null);
			panel.add(getOperatorCombo(), null);
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
				private static final double EPS = 1E-6;

				public void actionPerformed(java.awt.event.ActionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent();
					Criteria cr = (Criteria) node.getUserObject();
					ComplexCriteria parent = getCriteriaTree().getSelectionPath().getParentPath() == null ? null : (ComplexCriteria) ((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
					try {
						if (cr instanceof ComplexCriteria) {
							ComplexCriteria criteria = (ComplexCriteria) cr;
							criteria.setName(getNameEdit().getText());
							double weight = NUMBER_FORMAT.parse(getWeightEdit().getText()).doubleValue();
							double lambda = NUMBER_FORMAT.parse(getLambdaEdit().getText()).doubleValue();
							ComplexFunction function = (ComplexFunction) getOperatorCombo().getSelectedItem();
							if (lambda > 1 - EPS || lambda < EPS || weight < EPS) {
								throw new Exception();
							}
							if (parent != null) {
								parent.getOperator().weights.set(parent.getChildren().indexOf(criteria), weight);
							}
							ComplexOperator op = criteria.getOperator();
							if (ComplexFunction.getFunction(op.getClass()) != function) {
								ComplexOperator newOperator = function.createOperator(lambda);
								newOperator.weights = op.weights;
								newOperator.refresh();
								criteria.setOperator(newOperator);
							} else {
								op.lambda = lambda;
							}
						} else {

						}
						enablePanel1(false);
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
			cancelButton.setLabel("Отмена");
			cancelButton.setEnabled(false);
			cancelButton.setMinimumSize(new java.awt.Dimension(80,25));
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					reloadPanel();
					enablePanel1(false);
				}
			});
		}
		return cancelButton;
	}



	class EnablePanel1 extends KeyAdapter {
		public void keyTyped(java.awt.event.KeyEvent e) {
			enablePanel1(true);
		}
	}

	private EnablePanel1 enablePanel1 = new EnablePanel1();

	private JSplitPane jSplitPane = null;

	private JSplitPane jSplitPane1 = null;

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
	 * This method initializes цушпреУвше
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
	 * This method initializes дфьивфУвше
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getLambdaEdit() {
		if (lambdaEdit == null) {
			lambdaEdit = new JTextField();
			lambdaEdit.setBounds(new java.awt.Rectangle(183,135,135,20));
			lambdaEdit.addKeyListener(enablePanel1);

		}
		return lambdaEdit;
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
					enablePanel1(true);
				}
			});
		}
		return operatorCombo;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void addComplexCriteria() {

	}

	public void addSingleCriteria() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getLastPathComponent();
		Criteria cr = (Criteria) node.getUserObject();
		ComplexCriteria parent = getCriteriaTree().getSelectionPath().getParentPath() == null ? null : (ComplexCriteria) ((DefaultMutableTreeNode) getCriteriaTree().getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
		if (parent != null) {
			parent.removeChild(cr);
		}
		if (node.getParent() != null) {
			((DefaultMutableTreeNode) node.getParent()).remove(node);
		}
		enablePanel1(false);
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
		enablePanel1(false);
		getCriteriaTree().updateUI();
	}

	private void enablePanel1(boolean enable) {
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
		double weight = parent == null ? 1 : parent.getOperator().weights.get(parent.getChildren().indexOf(cr));
		getWeightEdit().setText( NUMBER_FORMAT.format( weight));
		if (cr instanceof ComplexCriteria) {
			ComplexCriteria criteria = (ComplexCriteria) cr;
			getLambdaEdit().setText( NUMBER_FORMAT.format( criteria.getOperator().lambda));
			getOperatorCombo().setSelectedItem( ComplexFunction.getFunction(criteria.getOperator().getClass()));
		} else if (criteria instanceof SingleCriteria) {
		}
		enablePanel1(false);
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


}  //  @jve:decl-index=0:visual-constraint="-101,-41"
