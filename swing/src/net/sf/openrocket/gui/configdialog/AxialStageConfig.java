package net.sf.openrocket.gui.configdialog;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.gui.SpinnerEditor;
import net.sf.openrocket.gui.adaptors.CustomFocusTraversalPolicy;
import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.adaptors.EnumModel;
import net.sf.openrocket.gui.components.StyledLabel;
import net.sf.openrocket.gui.components.StyledLabel.Style;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.rocketcomponent.AxialStage;
import net.sf.openrocket.rocketcomponent.MassObject;
import net.sf.openrocket.rocketcomponent.RocketComponent;
import net.sf.openrocket.rocketcomponent.StageSeparationConfiguration;
import net.sf.openrocket.rocketcomponent.StageSeparationConfiguration.SeparationEvent;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.startup.OpenRocket;
import net.sf.openrocket.utils.educoder.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Method;

public class AxialStageConfig extends ComponentAssemblyConfig {
	private static final long serialVersionUID = -944969957186522471L;
	private static final Translator trans = Application.getTranslator();
	
	public AxialStageConfig(OpenRocketDocument document, RocketComponent component, JDialog parent) {
		super(document, component, parent);
		
		// Stage separation config (for non-first stage)
		if (component.getStageNumber() > 0) {
			JPanel tab = separationTab((AxialStage) component);
			tabbedPane.insertTab(trans.get("ComponentAssemblyConfig.tab.Separation"), null, tab,
					trans.get("ComponentAssemblyConfig.tab.Separation.ttip"), 0);
			tabbedPane.setSelectedIndex(0);
		}

		// Apply the custom focus travel policy to this config dialog
		//// Make sure the cancel & ok button is the last component
		order.add(cancelButton);
		order.add(okButton);
		CustomFocusTraversalPolicy policy = new CustomFocusTraversalPolicy(order);
		parent.setFocusTraversalPolicy(policy);
	}
	
	
	private JPanel separationTab(AxialStage stage) {
		JPanel panel = new JPanel(new MigLayout());
		
		// Select separation event
		panel.add(new StyledLabel(trans.get("ComponentAssemblyConfig.separation.lbl.title") + " " + CommonStrings.dagger, Style.BOLD),
				"spanx, gaptop unrel, wrap 30lp");

		StageSeparationConfiguration sepConfig = stage.getSeparationConfiguration();

		EnumModel<SeparationEvent> em = new EnumModel<>(sepConfig, "SeparationEvent", SeparationEvent.values());
		register(em);
		JComboBox<SeparationEvent> combo = new JComboBox<>(em);
		
		//combo.setSelectedItem(sepConfig);
		panel.add(combo);
		order.add(combo);
		
		// ... and delay
		panel.add(new JLabel(trans.get("ComponentAssemblyConfig.separation.lbl.plus")));
		
		DoubleModel dm = new DoubleModel( sepConfig, "SeparationDelay", 0);
		register(dm);
		JSpinner spin = new JSpinner(dm.getSpinnerModel());
		spin.setEditor(new SpinnerEditor(spin));
		panel.add(spin, "width 65lp");
		order.add(((SpinnerEditor)spin.getEditor()).getTextField());
		
		//// seconds
		panel.add(new JLabel(trans.get("ComponentAssemblyConfig.separation.lbl.seconds")), "wrap unrel");
		
		panel.add(new StyledLabel(CommonStrings.override_description, -1), "spanx, pushy, wrap para");

		{//// CG calculation demonstration
			panel.add(new JLabel(trans.get("common.lbl.CgCalc") + ":"), "alignx left");
			JButton button2 = new JButton(trans.get("common.lbl.CgEnter"));
			panel.add(button2, "spanx, wrap");
			button2.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("common.lbl.CgCalc"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final StageCgRequest request = new StageCgRequest();
				request.setAnswer(stage.getComponentCG().x);

				JButton checkButton = new JButton(trans.get("common.lbl.check"));
				JLabel checkResult = new JLabel(trans.get("common.lbl.checkResult") + ": ");
				JLabel answerLabel = new JLabel(trans.get("common.lbl.answer") + ": ");
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
							checkResult.setText(trans.get("common.lbl.checkResult") + ": " + result.getResult());
							answerLabel.setText(trans.get("common.lbl.answer") + ": " + stage.getComponentCG().x);
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


		{//// CP calculation demonstration
			panel.add(new JLabel(trans.get("common.lbl.CpCalc") + ":"));
			JButton button2 = new JButton(trans.get("common.lbl.CpEnter"));
			panel.add(button2, "spanx, wrap");
			button2.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("common.lbl.CpCalc"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final StageCpRequest request = new StageCpRequest();

				request.setAnswer(0.0);

				try {

					JButton checkButton = new JButton(trans.get("common.lbl.check"));
					JLabel checkResult = new JLabel(trans.get("common.lbl.checkResult") + ": ");
					JLabel answerLabel = new JLabel(trans.get("common.lbl.answer") + ": ");
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
								checkResult.setText(trans.get("common.lbl.checkResult") + ": " + result.getResult());
								answerLabel.setText(trans.get("common.lbl.answer") + ": " + "0.0");
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

		{//// MOI calculation demonstration
			panel.add(new JLabel(trans.get("common.lbl.MOICal") + ":"), "alignx left");
			JButton button2 = new JButton(trans.get("common.lbl.MOIEnter"));
			panel.add(button2, "spanx, wrap");
			button2.addActionListener(e -> {
				JDialog dialog = new JDialog(this.parent, trans.get("common.lbl.MOICal"));
				dialog.setSize(this.parent.getSize());
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new MigLayout("fill, gap 4!, ins panel, hidemode 3", "[]:5[]", "[]:5[]"));

				final StageMOIRequest request = new StageMOIRequest();
				request.setAnswer(new Double[]{stage.getRotationalUnitInertia(),stage.getLongitudinalUnitInertia()});

				JButton checkButton = new JButton(trans.get("common.lbl.check"));
				JLabel checkResult = new JLabel(trans.get("common.lbl.checkResult") + ": ");
				JLabel answerLabel = new JLabel(trans.get("common.lbl.answer") + ": ");
				dialog.add(checkButton, "newline, height 30!");
				dialog.add(checkResult, "height 30!");
				dialog.add(answerLabel, "height 30!");
				// Do not use UI thread to get the answer
				checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.calculateMOI(request).enqueue(new Callback<>() {
					@Override
					public void onResponse(@NotNull Call<Result2> call, @NotNull Response<Result2> response) {
						Result2 result = response.body();
						if (result == null) return;
						SwingUtilities.invokeLater(() -> {
							checkResult.setText(trans.get("NoseConeCfg.lbl.checkResult") + ": " + result.getResult()[0]+","+result.getResult()[1]);
							answerLabel.setText(trans.get("NoseConeCfg.lbl.answer") + ": " + stage.getRotationalUnitInertia()+","+stage.getLongitudinalUnitInertia());
					});
					}

					@Override
					public void onFailure(@NotNull Call<Result2> call, @NotNull Throwable throwable) {
						SwingUtilities.invokeLater(() ->
								JOptionPane.showMessageDialog(parent, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
					}
				}));
				dialog.setVisible(true);
			});
		}



		return panel;
	}

	
}
