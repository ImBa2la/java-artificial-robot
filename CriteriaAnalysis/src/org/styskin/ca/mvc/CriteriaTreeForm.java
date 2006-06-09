package org.styskin.ca.mvc;

import java.awt.Button;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.ScrollPane;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import org.styskin.ca.model.ComplexFunction;

public class CriteriaTreeForm extends JPanel {

	static Logger logger = Logger.getLogger(CriteriaTreeForm.class);

	private static final long serialVersionUID = -3071115953530320825L;

	private ScrollPane scrollPane = null;
	private JTree criteriaTree = null;
	private JTabbedPane controlPanel = null;
	private JPanel generalPanel = null;
	private JPanel functionPanel = null;


	static class CriteriaRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = -2638037386251154389L;
		private Icon complexCriteriaIcon;
		private Icon singleCriteriaIcon;

	    public CriteriaRenderer() {
			complexCriteriaIcon = new ImageIcon("img/Parent.gif");
			singleCriteriaIcon = new ImageIcon("img/Child.gif");
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
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.ipadx = 208;
		gridBagConstraints1.ipady = 347;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridLayout());
		this.setSize(690, 460);

		this.setName("CriteriaTree");
		this.add(getScrollPane(), gridBagConstraints);
		this.add(getControlPanel(), gridBagConstraints1);
	}

	/**
	 * This method initializes scrollPane
	 *
	 * @return java.awt.ScrollPane
	 */
	private ScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new ScrollPane();
			scrollPane.add(getCriteriaTree(), null);
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
			generalPanel.setLayout(null);
			generalPanel.add(getPanel(), null);
			generalPanel.add(getPanel1(), null);
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
			panel.setBounds(new java.awt.Rectangle(0,0,340,368));
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
			panel1.setBounds(new java.awt.Rectangle(0,369,340,62));
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
			applyButton.setMinimumSize(new java.awt.Dimension(80,25));
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
			cancelButton.setMinimumSize(new java.awt.Dimension(80,25));
		}
		return cancelButton;
	}



	/**
	 * This method initializes nameEdit
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getNameEdit() {
		if (nameEdit == null) {
			nameEdit = new JTextField();
			nameEdit.setBounds(new java.awt.Rectangle(180,24,138,20));
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
			operatorCombo.setBounds(new java.awt.Rectangle(182,97,131,25));
		}
		return operatorCombo;
	}

	public Criteria getCriteria() {
		return criteria;
	}


}  //  @jve:decl-index=0:visual-constraint="-101,-41"