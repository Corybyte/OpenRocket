package net.sf.openrocket.gui.configdialog;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import net.sf.openrocket.aerodynamics.AerodynamicForces;
import net.sf.openrocket.aerodynamics.FlightConditions;
import net.sf.openrocket.aerodynamics.barrowman.SymmetricComponentCalc;
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
import net.sf.openrocket.logging.WarningSet;
import net.sf.openrocket.material.Material;
import net.sf.openrocket.rocketcomponent.*;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.startup.OpenRocket;
import net.sf.openrocket.unit.UnitGroup;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.util.Transformation;
import net.sf.openrocket.utils.educoder.NoseConeCgRequest;
import net.sf.openrocket.utils.educoder.NoseConeCpRequest;
import net.sf.openrocket.utils.educoder.Result;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

		{//// CG calculation demonstration
			panel.add(new JLabel(trans.get("NoseConeCfg.lbl.CgCalc") + ":"));
			JButton button = new JButton(trans.get("NoseConeCfg.lbl.CgEnter"));
			panel.add(button, "spanx, wrap");
			button.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("NoseConeCfg.lbl.CgCalc"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final NoseConeCgRequest request = new NoseConeCgRequest();
				request.setAnswer(component.getComponentCG().x);

				String[] transitionMethodNames = {"getForeRadius", "getAftRadius"};
				String[] transitionFieldNames = {"shapeParameter", "type"};

				String[] fieldNames = {"filled", "thickness", "DIVISIONS"};

				try {
					for (String fieldName : fieldNames) {
						Field field = SymmetricComponent.class.getDeclaredField(fieldName);
						Field reqField = NoseConeCgRequest.class.getDeclaredField(fieldName);
						field.setAccessible(true);
						reqField.setAccessible(true);
						Object value = field.get(component);
						reqField.set(request, value);
						String labelText = trans.get("NoseConeCfg.lbl." + fieldName) + ": " + value;
						String constraints = (fieldName.equals(fieldNames[0])) ? "spanx, height 30!" : "newline, height 30!";
						dialog.add(new JLabel(labelText), constraints);
					}
					// Component Length
					double length = component.getLength();
					request.setLength(length);
					String lengthLabelText = trans.get("NoseConeCfg.lbl.length") + ": " + length;
					dialog.add(new JLabel(lengthLabelText), "newline, height 30!");
					// Material Density
					double density = ((NoseCone) component).getMaterial().getDensity();
					request.setDensity(density);
					lengthLabelText = trans.get("NoseConeCfg.lbl.density") + ": " + density;
					dialog.add(new JLabel(lengthLabelText), "newline, height 30!");

					for (String fieldName : transitionFieldNames) {
						Field field = Transition.class.getDeclaredField(fieldName);
						String reqFieldName = "transition" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						Field reqField = NoseConeCgRequest.class.getDeclaredField(reqFieldName);
						field.setAccessible(true);
						reqField.setAccessible(true);
						Object value = field.get(component);
						reqField.set(request, value);
						String labelText = trans.get("NoseConeCfg.lbl." + reqFieldName) + ": " + value;
						if (value instanceof Transition.Shape)
							labelText = trans.get("NoseConeCfg.lbl." + reqFieldName) + ": " + ((Transition.Shape) value).name();
						dialog.add(new JLabel(labelText), "newline, height 30!");
					}
					for (String methodName : transitionMethodNames) {
						Method method = Transition.class.getDeclaredMethod(methodName);
						Method reqMethod = NoseConeCgRequest.class
								.getDeclaredMethod(methodName.replaceFirst("get", "setTransition"), Double.class);
						Double value = (Double) method.invoke(component); // All values are double type
						reqMethod.invoke(request, value);
						String labelText = trans.get("NoseConeCfg.lbl." + methodName.replaceFirst("get", "transition")) + ": " + value;
						dialog.add(new JLabel(labelText), "newline, height 30!");
					}

					JButton checkButton = new JButton(trans.get("NoseConeCfg.lbl.check"));
					JLabel checkResult = new JLabel(trans.get("NoseConeCfg.lbl.checkResult") + ": ");
					JLabel answerLabel = new JLabel(trans.get("NoseConeCfg.lbl.answer") + ": ");
					dialog.add(checkButton, "newline, height 30!");
					dialog.add(checkResult, "height 30!");
					dialog.add(answerLabel, "height 30!");
					// Do not use UI thread to get the answer
					checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.calculateCG(request).enqueue(new Callback<>() {
						@Override
						public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
							Result result = response.body();
							if (result == null) return;
							SwingUtilities.invokeLater(() -> {
								checkResult.setText(trans.get("NoseConeCfg.lbl.checkResult") + ": " + result.getResult());
								answerLabel.setText(trans.get("NoseConeCfg.lbl.answer") + ": " + component.getComponentCG().x);
							});
						}

						@Override
						public void onFailure(@NotNull Call<Result> call, @NotNull Throwable throwable) {
							SwingUtilities.invokeLater(() -> {
								checkResult.setText(trans.get("NoseConeCfg.lbl.checkResult") + ": " + throwable.getMessage());
								answerLabel.setText(trans.get("NoseConeCfg.lbl.answer") + ": " + component.getComponentCG().x);
							});
						}
					}));
				} catch (Exception ex) {
					// ignored
				}
				dialog.setVisible(true);
			});
		}

		{//// CP calculation demonstration
			panel.add(new JLabel(trans.get("NoseConeCfg.lbl.CpCalc") + ":"));
			JButton button = new JButton(trans.get("NoseConeCfg.lbl.CpEnter"));
			panel.add(button, "spanx, wrap");
			button.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("NoseConeCfg.lbl.CpCalc"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final NoseConeCpRequest request = new NoseConeCpRequest();

				FlightConfiguration curConfig = document.getSelectedConfiguration();
				FlightConditions conditions = new FlightConditions(curConfig);
				String[] methodNames = {"getSincAOA", "getSinAOA", "getRefArea", "getMach", "getAOA"};

				SymmetricComponentCalc componentCalc = new SymmetricComponentCalc(component);
				String[] fieldNames = {"foreRadius", "aftRadius", "length", "fullVolume", "planformCenter", "planformArea"};

				AerodynamicForces forces = new AerodynamicForces().zero();
				componentCalc.calculateNonaxialForces(conditions, new Transformation(0, 0, 0), forces,new WarningSet());
				request.setAnswer(forces.getCP().x);

				try {
					for (String fieldName : fieldNames) {
						Field field = SymmetricComponentCalc.class.getDeclaredField(fieldName);
						Field reqField = NoseConeCpRequest.class.getDeclaredField(fieldName);
						field.setAccessible(true);
						reqField.setAccessible(true);
						Double value = (Double) field.get(componentCalc); // All values are double type
						reqField.set(request, value);
						String labelText = trans.get("NoseConeCfg.lbl." + fieldName) + ": " + value;
						String constraints = (fieldName.equals(fieldNames[0])) ? "spanx, height 30!" : "newline, height 30!";
						dialog.add(new JLabel(labelText), constraints);
					}
					for (String methodName : methodNames) {
						Method method = FlightConditions.class.getDeclaredMethod(methodName);
						Method reqMethod = NoseConeCpRequest.class.getDeclaredMethod(methodName.replaceFirst("get", "set"), Double.class);
						Double value = (Double) method.invoke(conditions); // All values are double type
						reqMethod.invoke(request, value);
						String labelText = trans.get("NoseConeCfg.lbl." + methodName.replaceFirst("get", "")) + ": " + value;
						dialog.add(new JLabel(labelText), "newline, height 30!");
					}

					JButton checkButton = new JButton(trans.get("NoseConeCfg.lbl.check"));
					JLabel checkResult = new JLabel(trans.get("NoseConeCfg.lbl.checkResult") + ": ");
					JLabel answerLabel = new JLabel(trans.get("NoseConeCfg.lbl.answer") + ": ");
					dialog.add(checkButton, "newline, height 30!");
					dialog.add(checkResult, "height 30!");
					dialog.add(answerLabel, "height 30!");
					// Do not use UI thread to get the answer
					checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.calculateCP(request).enqueue(new Callback<>() {
						@Override
						public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
							Result result = response.body();
							if (result == null) return;
							SwingUtilities.invokeLater(() -> {
								checkResult.setText(trans.get("NoseConeCfg.lbl.checkResult") + ": " + result.getResult());
								answerLabel.setText(trans.get("NoseConeCfg.lbl.answer") + ": " + forces.getCP().x);
							});
						}

						@Override
						public void onFailure(@NotNull Call<Result> call, @NotNull Throwable throwable) {
							SwingUtilities.invokeLater(() -> {
								checkResult.setText(trans.get("NoseConeCfg.lbl.checkResult") + ": " + throwable.getMessage());
								answerLabel.setText(trans.get("NoseConeCfg.lbl.answer") + ": " + forces.getCP().x);
							});
						}
					}));
				} catch (Exception ex) {
					// ignored
				}
				dialog.setVisible(true);
			});
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
