package org.styskin.ca.mvc;

import java.awt.GridLayout;
import java.awt.ScrollPane;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.ComplexCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.model.OperatorType;

public class CriteriaTreeForm extends JPanel {

	static Logger logger = Logger.getLogger(CriteriaTreeForm.class);

	private static final long serialVersionUID = -3071115953530320825L;

	private ScrollPane scrollPane = null;
	private JTree criteriaTree = null;
	private JTabbedPane controlPanel = null;
	private JPanel generalPanel = null;
	private JPanel functionPanel = null;


	private Criteria criteria;

	{
		try {
			criteria = new ComplexCriteria(OperatorType.ADDITIVE);
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
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(1);
		gridLayout.setColumns(2);
		this.setLayout(gridLayout);
		this.setSize(416, 376);

		this.setName("CriteriaTree");
		this.add(getScrollPane(), null);
		this.add(getControlPanel(), null);
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
			criteriaTree.setBounds(new java.awt.Rectangle(5,377,78,72));
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
			functionPanel.setVisible(false);
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

}  //  @jve:decl-index=0:visual-constraint="-101,-41"
