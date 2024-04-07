package net.sf.openrocket.gui.configdialog;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.gui.SpinnerEditor;
import net.sf.openrocket.gui.adaptors.BooleanModel;
import net.sf.openrocket.gui.adaptors.CustomFocusTraversalPolicy;
import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.adaptors.TransitionShapeModel;
import net.sf.openrocket.gui.components.BasicSlider;
import net.sf.openrocket.gui.components.DescriptionArea;
import net.sf.openrocket.gui.components.UnitSelector;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.material.Material;
import net.sf.openrocket.rocketcomponent.NoseCone;
import net.sf.openrocket.rocketcomponent.RocketComponent;
import net.sf.openrocket.rocketcomponent.SymmetricComponent;
import net.sf.openrocket.rocketcomponent.Transition;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.unit.UnitGroup;
import net.sf.openrocket.util.Coordinate;

@SuppressWarnings("serial")
public class NoseConeConfig extends RocketComponentConfig {
	
	
	private DescriptionArea description;
	
	private JLabel shapeLabel;
	private JSpinner shapeSpinner;
	private JSlider shapeSlider;
	private final JCheckBox checkAutoBaseRadius;
	private static final Translator trans = Application.getTranslator();
	
	// Prepended to the description from NoseCone.DESCRIPTIONS
	private static final String PREDESC = "<html>";
	
	public NoseConeConfig(OpenRocketDocument d, RocketComponent c, JDialog parent) {
		super(d, c, parent);
		
		final JPanel panel = new JPanel(new MigLayout("", "[][65lp::][30lp::]"));
		
		////  Shape selection
		{//// Nose cone shape:
			panel.add(new JLabel(trans.get("NoseConeCfg.lbl.Noseconeshape")));

			TransitionShapeModel tm = new TransitionShapeModel(c);
			register(tm);
			final JComboBox<Transition.Shape> typeBox = new JComboBox<>(tm);
			typeBox.setEditable(false);
			typeBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Transition.Shape s = (Transition.Shape) typeBox.getSelectedItem();
					if (s != null) {
						description.setText(PREDESC + s.getNoseConeDescription());
					}
					updateEnabled();
				}
			});
			panel.add(typeBox, "spanx 3, growx, wrap rel");
			order.add(typeBox);

			////  Shape parameter:
			this.shapeLabel = new JLabel(trans.get("NoseConeCfg.lbl.Shapeparam"));
			panel.add(shapeLabel);

			final DoubleModel parameterModel = new DoubleModel(component, "ShapeParameter", UnitGroup.UNITS_SHAPE_PARAMETER, 0, 1);
			register(parameterModel);

			this.shapeSpinner = new JSpinner(parameterModel.getSpinnerModel());
			shapeSpinner.setEditor(new SpinnerEditor(shapeSpinner));
			panel.add(shapeSpinner, "growx");
			order.add(((SpinnerEditor) shapeSpinner.getEditor()).getTextField());

			DoubleModel min = new DoubleModel(component, "ShapeParameterMin");
			DoubleModel max = new DoubleModel(component, "ShapeParameterMax");
			register(min);
			register(max);
			this.shapeSlider = new BasicSlider(parameterModel.getSliderModel(min, max));
			panel.add(shapeSlider, "skip, w 100lp, wrap para");

			updateEnabled();
		}

		{ /// Nose cone length:
			panel.add(new JLabel(trans.get("NoseConeCfg.lbl.Noseconelength")));

			final DoubleModel lengthModel = new DoubleModel(component, "Length", UnitGroup.UNITS_LENGTH, 0);
			register(lengthModel);
			JSpinner spin = new JSpinner(lengthModel.getSpinnerModel());
			spin.setEditor(new SpinnerEditor(spin));
			panel.add(spin, "growx");
			order.add(((SpinnerEditor) spin.getEditor()).getTextField());

			panel.add(new UnitSelector(lengthModel), "growx");
			panel.add(new BasicSlider(lengthModel.getSliderModel(0, 0.1, 0.7)), "w 100lp, wrap");
		}
		{
			/// Base diameter:

			panel.add(new JLabel(trans.get("NoseConeCfg.lbl.Basediam")));

			final DoubleModel baseRadius = new DoubleModel(component, "BaseRadius", 2.0, UnitGroup.UNITS_LENGTH, 0); // Diameter = 2*Radius
			register(baseRadius);
			final JSpinner radiusSpinner = new JSpinner(baseRadius.getSpinnerModel());
			radiusSpinner.setEditor(new SpinnerEditor(radiusSpinner));
			panel.add(radiusSpinner, "growx");
			order.add(((SpinnerEditor) radiusSpinner.getEditor()).getTextField());

			panel.add(new UnitSelector(baseRadius), "growx");
			panel.add(new BasicSlider(baseRadius.getSliderModel(0, 0.04, 0.2)), "w 100lp, wrap 0px");

			checkAutoBaseRadius = new JCheckBox(baseRadius.getAutomaticAction());
			//// Automatic
			checkAutoBaseRadius.setText(trans.get("NoseConeCfg.checkbox.Automatic"));
			panel.add(checkAutoBaseRadius, "skip, span 2, wrap");
			order.add(checkAutoBaseRadius);
			updateCheckboxAutoBaseRadius(((NoseCone) component).isFlipped());
		}

		{////  Wall thickness:
			panel.add(new JLabel(trans.get("NoseConeCfg.lbl.Wallthickness")));
			final DoubleModel thicknessModel = new DoubleModel(component, "Thickness", UnitGroup.UNITS_LENGTH, 0);
			register(thicknessModel);
			final JSpinner thicknessSpinner = new JSpinner(thicknessModel.getSpinnerModel());
			thicknessSpinner.setEditor(new SpinnerEditor(thicknessSpinner));
			panel.add(thicknessSpinner, "growx");
			order.add(((SpinnerEditor) thicknessSpinner.getEditor()).getTextField());

			panel.add(new UnitSelector(thicknessModel), "growx");
			panel.add(new BasicSlider(thicknessModel.getSliderModel(0,
							new DoubleModel(component, "MaxRadius", UnitGroup.UNITS_LENGTH))),
					"w 100lp, wrap 0px");


			BooleanModel bm = new BooleanModel(component, "Filled");
			register(bm);
			final JCheckBox filledCheckbox = new JCheckBox(bm);
			//// Filled
			filledCheckbox.setText(trans.get("NoseConeCfg.checkbox.Filled"));
			filledCheckbox.setToolTipText(trans.get("NoseConeCfg.checkbox.Filled.ttip"));
			panel.add(filledCheckbox, "skip, span 2, wrap para");
			order.add(filledCheckbox);
		}


		{// 重心位置演算过程
			JLabel  jLabel_componentCG = new JLabel("重心位置计算");
			JButton button = new JButton("点此进入");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame frame = new JFrame("重心位置计算");

					frame.setResizable(false);
					frame.setUndecorated(false);
					frame.setBounds(0, 0, 800, 800);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					JPanel panel = new JPanel(new GridBagLayout());
					JLabel label_componentLength = new JLabel("组件长度" + SymmetricComponent.componentLength);
					JLabel label_planCenter = new JLabel("平面中心矩" + SymmetricComponent.componentLength);
					JLabel label_pllanArea = new JLabel("平面面积" + SymmetricComponent.planArea);



					label_componentLength.setHorizontalAlignment(SwingConstants.NORTH_EAST);
					label_componentLength.setFont(new Font("Arial", Font.BOLD, 16));
					label_planCenter.setFont(new Font("Arial", Font.BOLD, 16));
					label_pllanArea.setFont(new Font("Arial", Font.BOLD, 16));




					GridBagConstraints constraints01 = new GridBagConstraints();
					constraints01.anchor = GridBagConstraints.NORTHWEST; // 设置组件对齐方式为左上角
					constraints01.gridx = 0; // 设置组件的网格X坐标
					constraints01.gridy = 0; // 设置组件的网格Y坐标
					constraints01.weightx = 0; // 设置组件在水平方向上的拉伸权重
					constraints01.weighty = 0; // 设置组件在垂直方向上的拉伸权重
					constraints01.fill = GridBagConstraints.HORIZONTAL;  // 设置组件填充整个可用空间

					GridBagConstraints constraints02 = new GridBagConstraints();
					constraints02.anchor = GridBagConstraints.NORTHWEST; // 设置组件对齐方式为左上角
					constraints02.gridx = 0; // 设置组件的网格X坐标
					constraints02.gridy = 1; // 设置组件的网格Y坐标
					constraints02.weightx = 0; // 设置组件在水平方向上的拉伸权重
					constraints02.weighty = 0; // 设置组件在垂直方向上的拉伸权重
					constraints02.fill = GridBagConstraints.HORIZONTAL;  // 设置组件填充整个可用空间


					GridBagConstraints constraints03 = new GridBagConstraints();
					constraints03.anchor = GridBagConstraints.NORTHWEST; // 设置组件对齐方式为左上角
					constraints03.gridx = 0; // 设置组件的网格X坐标
					constraints03.gridy = 2; // 设置组件的网格Y坐标
					constraints03.weightx = 1; // 设置组件在水平方向上的拉伸权重
					constraints03.weighty = 1; // 设置组件在垂直方向上的拉伸权重
					constraints03.fill = GridBagConstraints.HORIZONTAL;  // 设置组件填充整个可用空间





					panel.add(label_componentLength, constraints01); // 将标签添加到面板
					panel.add(label_planCenter,constraints02);
					panel.add(label_pllanArea,constraints03);

					frame.add(panel);
					frame.setVisible(true);

				}
			});

			panel.add(jLabel_componentCG);
			panel.add(button);
		}


		{//// Flip to tail cone:
			BooleanModel bm = new BooleanModel(component, "Flipped");
			register(bm);
			final JCheckBox flipCheckbox = new JCheckBox(bm);
			flipCheckbox.setText(trans.get("NoseConeCfg.checkbox.Flip"));
			flipCheckbox.setToolTipText(trans.get("NoseConeCfg.checkbox.Flip.ttip"));
			panel.add(flipCheckbox, "spanx, wrap");
			order.add(flipCheckbox);
			flipCheckbox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					updateCheckboxAutoBaseRadius(e.getStateChange() == ItemEvent.SELECTED);
				}
			});
		}



		panel.add(new JLabel(""), "growy");

		////  Description
		
		JPanel panel2 = new JPanel(new MigLayout("ins 0"));
		
		description = new DescriptionArea(5);
		description.setText(PREDESC + ((NoseCone) component).getShapeType().getNoseConeDescription());
		panel2.add(description, "wmin 250lp, spanx, growx, wrap para");
		

		//// Material
		MaterialPanel materialPanel = new MaterialPanel(component, document, Material.Type.BULK, order);
		register(materialPanel);
		panel2.add(materialPanel, "span, wrap");
		panel.add(panel2, "cell 4 0, gapleft 40lp, aligny 0%, spany");
		

		//// General and General properties
		tabbedPane.insertTab(trans.get("NoseConeCfg.tab.General"), null, panel,
				trans.get("NoseConeCfg.tab.ttip.General"), 0);
		//// Shoulder and Shoulder properties
		tabbedPane.insertTab(trans.get("NoseConeCfg.tab.Shoulder"), null, shoulderTab(),
				trans.get("NoseConeCfg.tab.ttip.Shoulder"), 1);
		tabbedPane.setSelectedIndex(0);

		// Apply the custom focus travel policy to this config dialog
		//// Make sure the cancel & ok button is the last component
		order.add(cancelButton);
		order.add(okButton);
		CustomFocusTraversalPolicy policy = new CustomFocusTraversalPolicy(order);
		parent.setFocusTraversalPolicy(policy);
	}
	
	
	private void updateEnabled() {
		boolean e = ((NoseCone) component).getShapeType().usesParameter();
		shapeLabel.setEnabled(e);
		shapeSpinner.setEnabled(e);
		shapeSlider.setEnabled(e);
	}

	/**
	 * Sets the checkAutoAftRadius checkbox's enabled state and tooltip text, based on the state of its next component.
	 * If there is no next symmetric component or if that component already has its auto checkbox checked, the
	 * checkAutoAftRadius checkbox is disabled.
	 *
	 * @param isFlipped	whether the nose cone is flipped
	 */
	private void updateCheckboxAutoBaseRadius(boolean isFlipped) {
		if (component == null || checkAutoBaseRadius == null) return;

		// Disable check button if there is no component to get the diameter from
		NoseCone noseCone = ((NoseCone) component);
		SymmetricComponent referenceComp = isFlipped ? noseCone.getPreviousSymmetricComponent() : noseCone.getNextSymmetricComponent();
		if (referenceComp == null) {
			checkAutoBaseRadius.setEnabled(false);
			((NoseCone) component).setBaseRadiusAutomatic(false);
			checkAutoBaseRadius.setToolTipText(trans.get("NoseConeCfg.checkbox.ttip.Automatic_noReferenceComponent"));
			return;
		}
		if ((!isFlipped&& !referenceComp.usesPreviousCompAutomatic()) ||
				isFlipped && !referenceComp.usesNextCompAutomatic()) {
			checkAutoBaseRadius.setEnabled(true);
			checkAutoBaseRadius.setToolTipText(trans.get("NoseConeCfg.checkbox.ttip.Automatic"));
		} else {
			checkAutoBaseRadius.setEnabled(false);
			((NoseCone) component).setBaseRadiusAutomatic(false);
			checkAutoBaseRadius.setToolTipText(trans.get("NoseConeCfg.checkbox.ttip.Automatic_alreadyAuto"));
		}
	}
}
