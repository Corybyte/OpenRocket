package net.sf.openrocket.gui.configdialog;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;

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
import net.sf.openrocket.utils.educoder.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransitionConfig extends RocketComponentConfig {
	private static final long serialVersionUID = -1851275950604625741L;
	
	private static final Translator trans = Application.getTranslator();
	private JComboBox<Transition.Shape> typeBox;

	private JLabel shapeLabel;
	private JSpinner shapeSpinner;
	private BasicSlider shapeSlider;
	private final JCheckBox checkAutoAftRadius;
	private final JCheckBox checkAutoForeRadius;
	private DescriptionArea description;
	

	// Prepended to the description from Transition.DESCRIPTIONS
	private static final String PREDESC = "<html>";
	
	
	public TransitionConfig(OpenRocketDocument d, RocketComponent c, JDialog parent) {
		super(d, c, parent);
		
		final JPanel panel = new JPanel(new MigLayout("gap rel unrel, fillx", "[][65lp::][30lp::]", ""));

		////  Shape selection
		//// Transition shape:
		panel.add(new JLabel(trans.get("TransitionCfg.lbl.Transitionshape")));
		
		typeBox = new JComboBox<>(new TransitionShapeModel(c));
		typeBox.setEditable(false);
		typeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Transition.Shape s = (Transition.Shape) typeBox.getSelectedItem();
				if (s != null) {
					description.setText(PREDESC + s.getTransitionDescription());
				}
				updateEnabled();
			}
		});
		panel.add(typeBox, "span 3, split 2");
		order.add(typeBox);



		{//// Clipped
			BooleanModel bm = new BooleanModel(component, "Clipped");
			register(bm);
			final JCheckBox checkbox = new JCheckBox(bm);
			checkbox.setText(trans.get("TransitionCfg.checkbox.Clipped"));
			checkbox.setToolTipText(trans.get("TransitionCfg.checkbox.Clipped.ttip"));
			panel.add(checkbox, "wrap");
			order.add(checkbox);
		}

		{
			////  Shape parameter:
			this.shapeLabel = new JLabel(trans.get("TransitionCfg.lbl.Shapeparam"));
			panel.add(shapeLabel);

			final DoubleModel shapeModel = new DoubleModel(component, "ShapeParameter", UnitGroup.UNITS_SHAPE_PARAMETER, 0, 1);
			register(shapeModel);

			this.shapeSpinner = new JSpinner(shapeModel.getSpinnerModel());
			shapeSpinner.setEditor(new SpinnerEditor(shapeSpinner));
			panel.add(shapeSpinner, "growx");
			order.add(((SpinnerEditor) shapeSpinner.getEditor()).getTextField());

			DoubleModel min = new DoubleModel(component, "ShapeParameterMin");
			DoubleModel max = new DoubleModel(component, "ShapeParameterMax");
			register(min);
			register(max);
			this.shapeSlider = new BasicSlider(shapeModel.getSliderModel(min, max));
			panel.add(shapeSlider, "skip, w 100lp, wrap");

			updateEnabled();
		}

		{/// Transition length:

			panel.add(new JLabel(trans.get("TransitionCfg.lbl.Transitionlength")));

			final DoubleModel lengthModel = new DoubleModel(component, "Length", UnitGroup.UNITS_LENGTH, 0);
			register(lengthModel);

			final JSpinner lengthSpinner = new JSpinner(lengthModel.getSpinnerModel());
			lengthSpinner.setEditor(new SpinnerEditor(lengthSpinner));
			panel.add(lengthSpinner, "growx");
			order.add(((SpinnerEditor) lengthSpinner.getEditor()).getTextField());

			panel.add(new UnitSelector(lengthModel), "growx");
			panel.add(new BasicSlider(lengthModel.getSliderModel(0, 0.05, 0.3)), "w 100lp, wrap");
		}

		{ /// Fore diameter:
			panel.add(new JLabel(trans.get("TransitionCfg.lbl.Forediam")));

	         // Diameter = 2*Radius
			final DoubleModel foreRadiusModel = new DoubleModel(component, "ForeRadius", 2, UnitGroup.UNITS_LENGTH, 0);
			register(foreRadiusModel);

			final JSpinner foreRadiusSpinner = new JSpinner(foreRadiusModel.getSpinnerModel());
			foreRadiusSpinner.setEditor(new SpinnerEditor(foreRadiusSpinner));
			panel.add(foreRadiusSpinner, "growx");
			order.add(((SpinnerEditor) foreRadiusSpinner.getEditor()).getTextField());

			panel.add(new UnitSelector(foreRadiusModel), "growx");
			panel.add(new BasicSlider(foreRadiusModel.getSliderModel(0, 0.04, 0.2)), "w 100lp, wrap 0px");

			checkAutoForeRadius = new JCheckBox(foreRadiusModel.getAutomaticAction());
			//// Automatic
			checkAutoForeRadius.setText(trans.get("TransitionCfg.checkbox.Automatic"));
			panel.add(checkAutoForeRadius, "skip, span 2, wrap");
			order.add(checkAutoForeRadius);
			updateCheckboxAutoForeRadius();
		}

		{	//// Aft diameter:
			panel.add(new JLabel(trans.get("TransitionCfg.lbl.Aftdiam")));

            // Diameter = 2*Radius
			final DoubleModel aftRadiusModel = new DoubleModel(component, "AftRadius", 2, UnitGroup.UNITS_LENGTH, 0);
			register(aftRadiusModel);

			final JSpinner aftRadiusSpinner = new JSpinner(aftRadiusModel .getSpinnerModel());
			aftRadiusSpinner.setEditor(new SpinnerEditor(aftRadiusSpinner));
			panel.add(aftRadiusSpinner, "growx");
			order.add(((SpinnerEditor) aftRadiusSpinner.getEditor()).getTextField());

			panel.add(new UnitSelector(aftRadiusModel), "growx");
			panel.add(new BasicSlider(aftRadiusModel.getSliderModel(0, 0.04, 0.2)), "w 100lp, wrap 0px");

			checkAutoAftRadius = new JCheckBox(aftRadiusModel.getAutomaticAction());
			//// Automatic
			checkAutoAftRadius.setText(trans.get("TransitionCfg.checkbox.Automatic"));
			panel.add(checkAutoAftRadius, "skip, span 2, wrap");
			order.add(checkAutoAftRadius);
			updateCheckboxAutoAftRadius();
		}

		{ ///  Wall thickness:
			panel.add(new JLabel(trans.get("TransitionCfg.lbl.Wallthickness")));

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

			//// Filled
			final JCheckBox thicknessCheckbox = new JCheckBox(new BooleanModel(component, "Filled"));
			//// Filled
			thicknessCheckbox.setText(trans.get("TransitionCfg.checkbox.Filled"));
			thicknessCheckbox.setToolTipText(trans.get("TransitionCfg.checkbox.Filled.ttip"));
			panel.add(thicknessCheckbox, "skip, span 2, wrap");
			order.add(thicknessCheckbox);
		}
		{//// CG calculation demonstration
			panel.add(new JLabel(trans.get("NoseConeCfg.lbl.CgCalc") + ":"));
			JButton button = new JButton(trans.get("NoseConeCfg.lbl.CgEnter"));
			panel.add(button);
			button.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("NoseConeCfg.lbl.CgCalc"));
				System.out.println(component.getComponentCG());
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final TransitionCgRequest request = new TransitionCgRequest();
				request.setAnswer(component.getComponentCG().x);

				String[] transitionMethodNames = {"getForeRadius", "getAftRadius"};
				String[] transitionFieldNames = {"shapeParameter", "type"};

				String[] fieldNames = {"filled", "thickness", "DIVISIONS"};

				try {
					for (String fieldName : fieldNames) {
						Field field = SymmetricComponent.class.getDeclaredField(fieldName);
						Field reqField = TransitionCgRequest.class.getDeclaredField(fieldName);
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
					double density = ((Transition) component).getMaterial().getDensity();
					request.setDensity(density);
					lengthLabelText = trans.get("NoseConeCfg.lbl.density") + ": " + density;
					dialog.add(new JLabel(lengthLabelText), "newline, height 30!");

					for (String fieldName : transitionFieldNames) {
						Field field = Transition.class.getDeclaredField(fieldName);
						String reqFieldName = "transition" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						Field reqField = TransitionCgRequest.class.getDeclaredField(reqFieldName);
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
						Method reqMethod = TransitionCgRequest.class
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
							SwingUtilities.invokeLater(() ->
									JOptionPane.showMessageDialog(parent, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
						}
					}));
				} catch (Exception ex) {
					// ignored
				}
				dialog.setVisible(true);
			});
		}

		{//// CP calculation demonstration
			panel.add(new JLabel(trans.get("BodyTube.lbl.CpCalc") + ":"));
			JButton button = new JButton(trans.get("BodyTube.lbl.CpEnter"));
			panel.add(button, "spanx, wrap");
			button.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("BodyTube.lbl.CpCalc"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final TransitionCpRequest request = new TransitionCpRequest();

				FlightConfiguration curConfig = document.getSelectedConfiguration();
				FlightConditions conditions = new FlightConditions(curConfig);
				String[] methodNames = {"getSincAOA", "getSinAOA", "getRefArea", "getMach", "getAOA"};

				SymmetricComponentCalc componentCalc = new SymmetricComponentCalc(component);
				String[] fieldNames = {"planformCenter", "planformArea"};

				AerodynamicForces forces = new AerodynamicForces().zero();
				componentCalc.calculateNonaxialForces(conditions, new Transformation(0, 0, 0), forces,new WarningSet());
				request.setAnswer(forces.getCP().x);

				try {
					for (String fieldName : fieldNames) {
						Field field = SymmetricComponentCalc.class.getDeclaredField(fieldName);
						Field reqField = BodyTubeCpRequest.class.getDeclaredField(fieldName);
						field.setAccessible(true);
						reqField.setAccessible(true);
						Double value = (Double) field.get(componentCalc); // All values are double type
						reqField.set(request, value);
						String labelText = trans.get("BodyTube.lbl." + fieldName) + ": " + value;
						String constraints = (fieldName.equals(fieldNames[0])) ? "spanx, height 30!" : "newline, height 30!";
						dialog.add(new JLabel(labelText), constraints);
					}
					for (String methodName : methodNames) {
						Method method = FlightConditions.class.getDeclaredMethod(methodName);
						Method reqMethod = BodyTubeCpRequest.class.getDeclaredMethod(methodName.replaceFirst("get", "set"), Double.class);
						Double value = (Double) method.invoke(conditions); // All values are double type
						reqMethod.invoke(request, value);
						String labelText = trans.get("BodyTube.lbl." + methodName.replaceFirst("get", "")) + ": " + value;
						dialog.add(new JLabel(labelText), "newline, height 30!");
					}

					JButton checkButton = new JButton(trans.get("BodyTube.lbl.check"));
					JLabel checkResult = new JLabel(trans.get("BodyTube.lbl.checkResult") + ": ");
					JLabel answerLabel = new JLabel(trans.get("BodyTube.lbl.answer") + ": ");
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
								checkResult.setText(trans.get("BodyTube.lbl.checkResult") + ": " + result.getResult());
								answerLabel.setText(trans.get("BodyTube.lbl.answer") + ": " + forces.getCP().x);
							});
						}

						@Override
						public void onFailure(@NotNull Call<Result> call, @NotNull Throwable throwable) {
							SwingUtilities.invokeLater(() ->
									JOptionPane.showMessageDialog(parent, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
						}
					}));
				} catch (Exception ex) {
					// ignored
				}
				dialog.setVisible(true);
			});
		}

		{ // MOI calculate:
			panel.add(new JLabel(trans.get("NoseConeCfg.lbl.MOICalc") + ":"));
			JButton button = new JButton(trans.get("NoseConeCfg.lbl.MOIEnter"));
			panel.add(button, "spanx, wrap");
			button.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("NoseConeCfg.lbl.MOICalc"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final TransitionMOIRequest request = new TransitionMOIRequest();
				// answer = rotationalUnitInertia
				request.setAnswer(component.getRotationalUnitInertia());

				String[] transitionMethodNames = {"getForeRadius", "getAftRadius"};
				String[] transitionFieldNames = {"shapeParameter", "type"};

				String[] fieldNames = {"filled", "thickness", "DIVISIONS"};

				try{
					//get and set  properties
					for (String fieldName:fieldNames){
						Field field = SymmetricComponent.class.getDeclaredField(fieldName);
						Field reqField = TransitionMOIRequest.class.getDeclaredField(fieldName);
						field.setAccessible(true);
						reqField.setAccessible(true);
						Object value = field.get(component);
						reqField.set(request, value);
						String labelText = trans.get("NoseConeCfg.lbl." + fieldName) + ": " + value;
						String constraints = (fieldName.equals(fieldNames[0])) ? "spanx, height 30!" : "newline, height 30!";
						dialog.add(new JLabel(labelText), constraints);
					}
					//set len
					double length = component.getLength();
					request.setLength(length);
					String lengthLabelText = trans.get("NoseConeCfg.lbl.length") + ": " + length;
					dialog.add(new JLabel(lengthLabelText), "newline, height 30!");

					//set transitionMethodNames
					for (String fieldName : transitionFieldNames) {
						Field field = Transition.class.getDeclaredField(fieldName);
						String reqFieldName = "transition" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						Field reqField = TransitionMOIRequest.class.getDeclaredField(reqFieldName);
						field.setAccessible(true);
						reqField.setAccessible(true);
						Object value = field.get(component);
						reqField.set(request, value);
						String labelText = trans.get("NoseConeCfg.lbl." + reqFieldName) + ": " + value;
						if (value instanceof Transition.Shape)
							labelText = trans.get("NoseConeCfg.lbl." + reqFieldName) + ": " + ((Transition.Shape) value).name();
						dialog.add(new JLabel(labelText), "newline, height 30!");
					}
					// AftRadius
					for (String methodName : transitionMethodNames) {
						Method method = Transition.class.getDeclaredMethod(methodName);
						Method reqMethod = TransitionMOIRequest.class
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
					checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.calculateMOI(request).enqueue(new Callback<>() {
						@Override
						public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
							Result result = response.body();
							if (result == null) return;
							SwingUtilities.invokeLater(() -> {
								checkResult.setText(trans.get("NoseConeCfg.lbl.checkResult") + ": " + result.getResult());
								answerLabel.setText(trans.get("NoseConeCfg.lbl.answer") + ": " + component.getRotationalUnitInertia());
							});
						}

						@Override
						public void onFailure(@NotNull Call<Result> call, @NotNull Throwable throwable) {
							SwingUtilities.invokeLater(() ->
									JOptionPane.showMessageDialog(parent, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
						}
					}));

				}catch (Exception ex){
					//ignored
				}
				dialog.setVisible(true);
			});
		}
		////  Description
		JPanel panel2 = new JPanel(new MigLayout("ins 0"));
		
		description = new DescriptionArea(5);
		description.setText(PREDESC + ((Transition) component).getShapeType().
				getTransitionDescription());
		panel2.add(description, "wmin 250lp, spanx, growx, wrap para");
		

		//// Material
		MaterialPanel materialPanel = new MaterialPanel(component, document, Material.Type.BULK, order);
		register(materialPanel);
		panel2.add(materialPanel, "span, wrap");
		panel.add(panel2, "cell 4 0, gapleft 40lp, aligny 0%, spany");
		
		//// General and General properties
		tabbedPane.insertTab(trans.get("TransitionCfg.tab.General"), null, panel,
				trans.get("TransitionCfg.tab.Generalproperties"), 0);
		//// Shoulder and Shoulder properties
		tabbedPane.insertTab(trans.get("TransitionCfg.tab.Shoulder"), null, shoulderTab(),
				trans.get("TransitionCfg.tab.Shoulderproperties"), 1);
		tabbedPane.setSelectedIndex(0);

		// Apply the custom focus travel policy to this config dialog
		//// Make sure the cancel & ok button is the last component
		order.add(cancelButton);
		order.add(okButton);
		CustomFocusTraversalPolicy policy = new CustomFocusTraversalPolicy(order);
		parent.setFocusTraversalPolicy(policy);
	}
	
	

	private void updateEnabled() {
		boolean e = ((Transition) component).getShapeType().usesParameter();
		shapeLabel.setEnabled(e);
		shapeSpinner.setEnabled(e);
		shapeSlider.setEnabled(e);
	}

	/**
	 * Sets the checkAutoAftRadius checkbox's enabled state and tooltip text, based on the state of its next component.
	 * If there is no next symmetric component or if that component already has its auto checkbox checked, the
	 * checkAutoAftRadius checkbox is disabled.
	 */
	private void updateCheckboxAutoAftRadius() {
		if (component == null || checkAutoAftRadius == null) return;

		Transition transition = (Transition) component;
		boolean enabled = transition.canUseNextCompAutomatic();
		if (enabled) {														// Can use auto radius
			checkAutoAftRadius.setEnabled(true);
			checkAutoAftRadius.setToolTipText(trans.get("TransitionCfg.checkbox.ttip.Automatic"));
		} else if (transition.getNextSymmetricComponent() == null) {		// No next component to take the auto radius from
			checkAutoAftRadius.setEnabled(false);
			((Transition) component).setAftRadiusAutomatic(false);
			checkAutoAftRadius.setToolTipText(trans.get("TransitionCfg.checkbox.ttip.Automatic_noReferenceComponent"));
		} else {															// Next component already has its auto radius checked
			checkAutoAftRadius.setEnabled(false);
			((Transition) component).setAftRadiusAutomatic(false);
			checkAutoAftRadius.setToolTipText(trans.get("TransitionCfg.checkbox.ttip.Automatic_alreadyAuto"));
		}
	}

	/**
	 * Sets the checkAutoForeRadius checkbox's enabled state and tooltip text, based on the state of its next component.
	 * If there is no next symmetric component or if that component already has its auto checkbox checked, the
	 * checkAutoForeRadius checkbox is disabled.
	 */
	private void updateCheckboxAutoForeRadius() {
		if (component == null || checkAutoForeRadius == null) return;

		Transition transition = (Transition) component;
		boolean enabled = transition.canUsePreviousCompAutomatic();
		if (enabled) {														// Can use auto radius
			checkAutoForeRadius.setEnabled(true);
			checkAutoForeRadius.setToolTipText(trans.get("TransitionCfg.checkbox.ttip.Automatic"));
		} else if (transition.getPreviousSymmetricComponent() == null) {		// No next component to take the auto radius from
			checkAutoForeRadius.setEnabled(false);
			((Transition) component).setForeRadiusAutomatic(false);
			checkAutoForeRadius.setToolTipText(trans.get("TransitionCfg.checkbox.ttip.Automatic_noReferenceComponent"));
		} else {															// Next component already has its auto radius checked
			checkAutoForeRadius.setEnabled(false);
			((Transition) component).setForeRadiusAutomatic(false);
			checkAutoForeRadius.setToolTipText(trans.get("TransitionCfg.checkbox.ttip.Automatic_alreadyAuto"));
		}
	}
	
}
