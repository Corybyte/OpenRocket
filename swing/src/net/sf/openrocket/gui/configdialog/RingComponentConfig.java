package net.sf.openrocket.gui.configdialog;


import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.gui.SpinnerEditor;
import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.components.BasicSlider;
import net.sf.openrocket.gui.components.UnitSelector;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.material.Material;
import net.sf.openrocket.rocketcomponent.RingComponent;
import net.sf.openrocket.rocketcomponent.RocketComponent;
import net.sf.openrocket.rocketcomponent.ThicknessRingComponent;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.startup.OpenRocket;
import net.sf.openrocket.unit.UnitGroup;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.utils.educoder.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Method;

@SuppressWarnings("serial")
public class RingComponentConfig extends RocketComponentConfig {
	private static final Translator trans = Application.getTranslator();
	
	public RingComponentConfig(OpenRocketDocument d, RocketComponent component, JDialog parent) {
		super(d, component, parent);
	}


	protected JPanel generalTab(String length, String outer, String inner, String thickness) {
		JPanel primary = new JPanel(new MigLayout());

		JPanel panel = new JPanel(new MigLayout("gap rel unrel, ins 0", "[][65lp::][30lp::]", ""));
		DoubleModel m;
		JSpinner spin;
		DoubleModel od = null;

		//// Attributes ----

		//// Length
		if (length != null) {
			panel.add(new JLabel(length));

			m = new DoubleModel(component, "Length", UnitGroup.UNITS_LENGTH, 0);
			register(m);

			spin = new JSpinner(m.getSpinnerModel());
			spin.setEditor(new SpinnerEditor(spin));
			if (component instanceof ThicknessRingComponent) {
				focusElement = spin;
			}
			panel.add(spin, "growx");
			order.add(((SpinnerEditor) spin.getEditor()).getTextField());

			panel.add(new UnitSelector(m), "growx");
			panel.add(new BasicSlider(m.getSliderModel(0, 0.1, 1.0)), "w 100lp, wrap");
		}

		//// Outer diameter
		if (outer != null) {
			panel.add(new JLabel(outer));

			//// OuterRadius
			od = new DoubleModel(component, "OuterRadius", 2, UnitGroup.UNITS_LENGTH, 0);
			register(od);

			spin = new JSpinner(od.getSpinnerModel());
			spin.setEditor(new SpinnerEditor(spin));
			panel.add(spin, "growx");
			order.add(((SpinnerEditor) spin.getEditor()).getTextField());

			panel.add(new UnitSelector(od), "growx");
			panel.add(new BasicSlider(od.getSliderModel(0, 0.04, 0.2)), "w 100lp, wrap");

			if (od.isAutomaticAvailable()) {
				JCheckBox check = new JCheckBox(od.getAutomaticAction());
				//// Automatic
				check.setText(trans.get("ringcompcfg.Automatic"));
				check.setToolTipText(trans.get("ringcompcfg.AutomaticOuter.ttip"));
				panel.add(check, "skip, spanx 2, wrap");
				order.add(check);
			}
		}


		////  Inner diameter
		if (inner != null) {
			panel.add(new JLabel(inner));

			//// InnerRadius
			m = new DoubleModel(component, "InnerRadius", 2, UnitGroup.UNITS_LENGTH, 0);
			register(m);

			spin = new JSpinner(m.getSpinnerModel());
			spin.setEditor(new SpinnerEditor(spin));
			panel.add(spin, "growx");
			order.add(((SpinnerEditor) spin.getEditor()).getTextField());

			panel.add(new UnitSelector(m), "growx");
			if (od == null)
				panel.add(new BasicSlider(m.getSliderModel(0, 0.04, 0.2)), "w 100lp, wrap");
			else
				panel.add(new BasicSlider(m.getSliderModel(new DoubleModel(0), od)),
						"w 100lp, wrap");

			if (m.isAutomaticAvailable()) {
				JCheckBox check = new JCheckBox(m.getAutomaticAction());
				//// Automatic
				check.setText(trans.get("ringcompcfg.Automatic"));
				check.setToolTipText(trans.get("ringcompcfg.AutomaticInner.ttip"));
				panel.add(check, "skip, span 2, wrap");
				order.add(check);
			}
		}


		////  Wall thickness
		if (thickness != null) {
			panel.add(new JLabel(thickness));

			//// Thickness
			m = new DoubleModel(component, "Thickness", UnitGroup.UNITS_LENGTH, 0);
			register(m);

			spin = new JSpinner(m.getSpinnerModel());
			spin.setEditor(new SpinnerEditor(spin));
			panel.add(spin, "growx");
			order.add(((SpinnerEditor) spin.getEditor()).getTextField());

			panel.add(new UnitSelector(m), "growx");
			panel.add(new BasicSlider(m.getSliderModel(0, 0.01)), "w 100lp, wrap");
		}

		primary.add(panel, "grow, gapright 40lp");

		{//// CG calculation demonstration
			panel.add(new JLabel(trans.get("InnerComponent.lbl.CgCalc") + ":"), "alignx left");
			JButton button = new JButton(trans.get("InnerComponent.lbl.CgEnter"));
			panel.add(button, "spanx, wrap");
			button.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("InnerComponent.lbl.CgCalc"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final InnerComponentCgRequest request = new InnerComponentCgRequest();
				request.setAnswer(component.getComponentCG().x);
				request.setLength(component.getLength());
				component.getComponentCG();
				String labelText1 = trans.get("BodyTube.lbl.length") + ": " + request.getLength();
				String constraints = "newline, height 30!";
				dialog.add(new JLabel(labelText1), constraints);

				// Material Density
				double density = ((RingComponent) component).getMaterial().getDensity();
				Coordinate[] instanceOffsets = component.getInstanceOffsets();
				Double[][] offsets=new Double[instanceOffsets.length][3];
				for (int i = 0; i < offsets.length; i++) {
					offsets[i][0]=instanceOffsets[i].x;
					offsets[i][1]=instanceOffsets[i].y;
					offsets[i][2]=instanceOffsets[i].z;
				}
				request.setInstanceOffsets(offsets);
				request.setDensity(density);
				String lengthLabelText = trans.get("InnerComponent.lbl.Density") + ": " + density;
				dialog.add(new JLabel(lengthLabelText), "newline, height 30!");
				int instanceCount = component.getInstanceCount();
				request.setInstanceCount(instanceCount);
				String instanceCountLabelText =  "instanceCount : " + request.getInstanceCount();
				dialog.add(new JLabel(instanceCountLabelText), "newline, height 30!");
				String[] methodNames = { "getOuterRadius", "getInnerRadius"};
				try {
					for (String methodName : methodNames) {
						Method method = RingComponent.class.getDeclaredMethod(methodName);
						Method reqMethod = InnerComponentCgRequest.class.getDeclaredMethod(methodName.replaceFirst("get", "set"),Double.class);
						method.setAccessible(true);
						reqMethod.setAccessible(true);
						Double value = (Double) method.invoke(component);
						reqMethod.invoke(request,value);
						String labelText = trans.get("InnerComponent.lbl." + methodName.replaceFirst("get", "")) + ": " + value;
						dialog.add(new JLabel(labelText), "newline, height 30!");
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}



				JButton checkButton = new JButton(trans.get("InnerComponent.lbl.check"));
				JLabel checkResult = new JLabel(trans.get("InnerComponent.lbl.checkResult") + ": ");
				JLabel answerLabel = new JLabel(trans.get("InnerComponent.lbl.answer") + ": ");
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
							checkResult.setText(trans.get("InnerComponent.lbl.checkResult") + ": " + result.getResult());
							answerLabel.setText(trans.get("InnerComponent.lbl.answer") + ": " + component.getComponentCG().x);
						});
					}

					@Override
					public void onFailure(@NotNull Call<Result> call, @NotNull Throwable throwable) {
						SwingUtilities.invokeLater(() ->
								JOptionPane.showMessageDialog(parent, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
					}
				}));
				dialog.setVisible(true);
			});
		}


		{//// MOI calculation demonstration
			panel.add(new JLabel(trans.get("InnerComponent.lbl.MOICal") + ":"), "alignx left");
			JButton button = new JButton(trans.get("InnerComponent.lbl.MOIEnter"));
			panel.add(button, "spanx, wrap");
			button.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("InnerComponent.lbl.MOICal"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final InnerComponentMOIRequest request = new InnerComponentMOIRequest();
				request.setAnswer(component.getRotationalUnitInertia());

				String[] methodNames = { "getOuterRadius", "getInnerRadius"};
				try {
					for (String methodName : methodNames) {
						Method method = RingComponent.class.getDeclaredMethod(methodName);
						Method reqMethod = InnerComponentMOIRequest.class.getDeclaredMethod(methodName.replaceFirst("get", "set"),Double.class);
						method.setAccessible(true);
						reqMethod.setAccessible(true);
						Double value = (Double) method.invoke(component);
						reqMethod.invoke(request,value);
						String labelText = trans.get("InnerComponent.lbl." + methodName.replaceFirst("get", "")) + ": " + value;
						dialog.add(new JLabel(labelText), "newline, height 30!");
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				JButton checkButton = new JButton(trans.get("InnerComponent.lbl.check"));
				JLabel checkResult = new JLabel(trans.get("InnerComponent.lbl.checkResult") + ": ");
				JLabel answerLabel = new JLabel(trans.get("InnerComponent.lbl.answer") + ": ");
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
							checkResult.setText(trans.get("InnerComponent.lbl.checkResult") + ": " + result.getResult());
							answerLabel.setText(trans.get("InnerComponent.lbl.answer") + ": " + component.getRotationalUnitInertia());
						});
					}

					@Override
					public void onFailure(@NotNull Call<Result> call, @NotNull Throwable throwable) {
						SwingUtilities.invokeLater(() ->
								JOptionPane.showMessageDialog(parent, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
					}
				}));
				dialog.setVisible(true);
			});
		}

		// Right side panel
		JPanel rightSide = new JPanel(new MigLayout("gap rel unrel, ins 0", "[][65lp::][30lp::]", ""));
		primary.add(rightSide, "cell 4 0, aligny 0, spany");

		//// Position
		PlacementPanel pp = new PlacementPanel(component, order);
		register(pp);
		rightSide.add(pp, "span, grow");

		//// Material
		MaterialPanel materialPanel = new MaterialPanel(component, document, Material.Type.BULK, order);
		register(materialPanel);
		rightSide.add(materialPanel, "span, grow, wrap");

		return primary;
	}

	
}
