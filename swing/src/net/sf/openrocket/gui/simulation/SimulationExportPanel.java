package net.sf.openrocket.gui.simulation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.crypto.Data;

import net.miginfocom.swing.MigLayout;
import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.gui.components.CsvOptionPanel;
import net.sf.openrocket.gui.components.UnitCellEditor;
import net.sf.openrocket.gui.plot.Util;
import net.sf.openrocket.gui.util.FileHelper;
import net.sf.openrocket.gui.util.GUIUtil;
import net.sf.openrocket.gui.util.SaveCSVWorker;
import net.sf.openrocket.gui.util.SwingPreferences;
import net.sf.openrocket.gui.widgets.SaveFileChooser;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.simulation.FlightData;
import net.sf.openrocket.simulation.FlightDataBranch;
import net.sf.openrocket.simulation.FlightDataType;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.startup.OpenRocket;
import net.sf.openrocket.unit.Unit;
import net.sf.openrocket.unit.UnitGroup;
import net.sf.openrocket.gui.widgets.SelectColorButton;
import net.sf.openrocket.utils.educoder.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimulationExportPanel extends JPanel {
	
	private static final long serialVersionUID = 3423905472892675964L;
	private static final String SPACE = "SPACE";
	private static final String TAB = "TAB";
	private static final Translator trans = Application.getTranslator();
	
	private static final int OPTION_SIMULATION_COMMENTS = 0;
	private static final int OPTION_FIELD_DESCRIPTIONS = 1;
	private static final int OPTION_FLIGHT_EVENTS = 2;
	
	private final JTable table;
	private final SelectionTableModel tableModel;
	private final JLabel selectedCountLabel;
	
	private final Simulation simulation;
	private FlightDataBranch branch;
	
	private final boolean[] selected;
	private final FlightDataType[] types;
	private final Unit[] units;
	private final CsvOptionPanel csvOptions;



	public SimulationExportPanel(Simulation sim) {
		super(new MigLayout("fill, flowy"));

		JPanel panel;
		JButton button;
		JButton edu_button;
		JButton edu_all_button;
		
		this.simulation = sim;
		
		final FlightData data = simulation.getSimulatedData();
		
		// Check that data exists
		if (data == null || data.getBranchCount() == 0 ||
				data.getBranch(0).getTypes().length == 0) {
			throw new IllegalArgumentException("No data for panel");
		}
		
		
		// Create the data model
		branch = data.getBranch(0);
		
		types = branch.getTypes();
		Arrays.sort(types);
		
		selected = new boolean[types.length];
		units = new Unit[types.length];
		for (int i = 0; i < types.length; i++) {
			selected[i] = ((SwingPreferences) Application.getPreferences()).isExportSelected(types[i]);
			units[i] = types[i].getUnitGroup().getDefaultUnit();
		}
		
		
		//// Create the panel
		
		
		// Set up the variable selection table
		tableModel = new SelectionTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class,
				new SelectionBackgroundCellRenderer(table.getDefaultRenderer(Object.class)));
		table.setDefaultRenderer(Boolean.class,
				new SelectionBackgroundCellRenderer(table.getDefaultRenderer(Boolean.class)));
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		
		table.setDefaultEditor(Unit.class, new UnitCellEditor() {
			private static final long serialVersionUID = 1088570433902420935L;

			@Override
			protected UnitGroup getUnitGroup(Unit value, int row, int column) {
				return types[row].getUnitGroup();
			}
		});
		
		// Set column widths
		TableColumnModel columnModel = table.getColumnModel();
		TableColumn col = columnModel.getColumn(0);
		int w = table.getRowHeight();
		col.setMinWidth(w);
		col.setPreferredWidth(w);
		col.setMaxWidth(w);
		
		col = columnModel.getColumn(1);
		col.setPreferredWidth(200);
		
		col = columnModel.getColumn(2);
		col.setPreferredWidth(100);
		
		table.addMouseListener(new GUIUtil.BooleanTableClickListener(table));
		
		// Add table
		panel = new JPanel(new MigLayout("fill"));
		panel.setBorder(BorderFactory.createTitledBorder(trans.get("SimExpPan.border.Vartoexport")));
		
		panel.add(new JScrollPane(table), "wmin 300lp, width 300lp, height 1, grow 100, wrap");
		
		// Select all/none buttons
		button = new SelectColorButton(trans.get("SimExpPan.but.Selectall"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.selectAll();
			}
		});
		panel.add(button, "split 2, growx 1, sizegroup selectbutton");
		
		button = new SelectColorButton(trans.get("SimExpPan.but.Selectnone"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.selectNone();
			}
		});
		panel.add(button, "growx 1, sizegroup selectbutton, wrap");

		edu_button = new SelectColorButton(trans.get("SimExpPan.but.Edu"));
		edu_button.addActionListener(e -> {
					// 创建一个模态对话框，父窗口为当前组件的顶层窗口
					JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "弹体法向力系数评测", Dialog.ModalityType.MODELESS);

					// 设置对话框的主布局为 BorderLayout
					dialog.setLayout(new BorderLayout());

					// 创建主内容面板，使用 GridLayout 管理两部分内容
					JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 行 2 列，水平间距 10

					// 左边的大文本框
					JTextArea leftTextArea = new JTextArea();
					leftTextArea.setLineWrap(true); // 自动换行
					leftTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
					leftTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
					leftTextArea.setText(HullCNRequest.Server_cn.toString());
					JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
					mainPanel.add(leftScrollPane);

					// 右边的小文本框
					JTextArea rightTextArea = new JTextArea();
					rightTextArea.setLineWrap(true); // 自动换行
					rightTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
					rightTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
					rightTextArea.setText(HullCNRequest.Client_cn.toString());
					JScrollPane rightScrollPane = new JScrollPane(rightTextArea);
					mainPanel.add(rightScrollPane);

					// 将主面板添加到对话框的中间区域
					dialog.add(mainPanel, BorderLayout.CENTER);

					// 创建关闭按钮
					JButton closeButton = new JButton("关闭");
					closeButton.addActionListener(ev -> dialog.dispose()); // 点击按钮时关闭对话框

					// 创建一个新的按钮
					DataRequest request = new DataRequest(HullCNRequest.Client_cn,HullCNRequest.Server_cn);
					JButton checkButton = new JButton("评测");
					checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.checkJSON(request).enqueue(new Callback<Result>() {
						@Override
						public void onResponse(Call<Result> call, Response<Result> response) {
							JOptionPane.showMessageDialog(dialog, "请点击平台评测按钮");
						}
						@Override
						public void onFailure(Call<Result> call, Throwable throwable) {
							System.out.println(throwable.getMessage());
						}
					}));
			// 创建按钮面板
					JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 水平间距 10
					buttonPanel.add(closeButton); // 添加关闭按钮
					buttonPanel.add(checkButton);   // 添加新按钮
					dialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板放置在底部

					// 设置对话框属性
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 关闭时释放对话框资源
					dialog.setSize(800, 400); // 设置窗口宽度更大，适应两个文本框
					dialog.setLocationRelativeTo(this); // 设置相对于父窗口居中显示
					dialog.setVisible(true); // 显示对话框
				});
		panel.add(edu_button, "growx 1, sizegroup selectbutton, wrap");

		edu_all_button = new SelectColorButton(trans.get("SimExpPan.but.AllEdu"));
		edu_all_button.addActionListener(e -> {
			// 创建一个模态对话框，父窗口为当前组件的顶层窗口
			JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "总体弹体法向力系数", Dialog.ModalityType.MODELESS);

			// 设置对话框的主布局为 BorderLayout
			dialog.setLayout(new BorderLayout());

			// 创建主内容面板，使用 GridLayout 管理两部分内容
			JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 行 2 列，水平间距 10

			// 左边的大文本框
			JTextArea leftTextArea = new JTextArea();
			leftTextArea.setLineWrap(true); // 自动换行
			leftTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			leftTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
			leftTextArea.setText(WingCNRequest.Server_CN.toString());
			JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
			mainPanel.add(leftScrollPane);

			// 右边的小文本框
			JTextArea rightTextArea = new JTextArea();
			rightTextArea.setLineWrap(true); // 自动换行
			rightTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			rightTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
			rightTextArea.setText(WingCNRequest.Client_CN.toString());
			JScrollPane rightScrollPane = new JScrollPane(rightTextArea);
			mainPanel.add(rightScrollPane);

			// 将主面板添加到对话框的中间区域
			dialog.add(mainPanel, BorderLayout.CENTER);

			// 创建关闭按钮
			JButton closeButton = new JButton("关闭");
			closeButton.addActionListener(ev -> dialog.dispose()); // 点击按钮时关闭对话框

			// 创建一个新的按钮
			DataRequest request = new DataRequest(WingCNRequest.Server_CN,WingCNRequest.Client_CN);
			JButton checkButton = new JButton("评测");
			checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.checkJSON(request).enqueue(new Callback<Result>() {
				@Override
				public void onResponse(Call<Result> call, Response<Result> response) {
					JOptionPane.showMessageDialog(dialog, "请点击平台评测按钮");
				}
				@Override
				public void onFailure(Call<Result> call, Throwable throwable) {
					System.out.println(throwable.getMessage());
				}
			}));
			// 创建按钮面板
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 水平间距 10
			buttonPanel.add(closeButton); // 添加关闭按钮
			buttonPanel.add(checkButton);   // 添加新按钮
			dialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板放置在底部

			// 设置对话框属性
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 关闭时释放对话框资源
			dialog.setSize(800, 400); // 设置窗口宽度更大，适应两个文本框
			dialog.setLocationRelativeTo(this); // 设置相对于父窗口居中显示
			dialog.setVisible(true); // 显示对话框
		});
		panel.add(edu_all_button, "growx 1, sizegroup selectbutton, wrap");


		SelectColorButton edu_button2 = new SelectColorButton("弹道轨迹测评");
		edu_button2.addActionListener(e -> {
			// 创建一个模态对话框，父窗口为当前组件的顶层窗口
			JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "弹道轨迹测评", Dialog.ModalityType.MODELESS);

			// 设置对话框的主布局为 BorderLayout
			dialog.setLayout(new BorderLayout());

			// 创建主内容面板，使用 GridLayout 管理两部分内容
			JPanel mainPanel = new JPanel(new GridLayout(1, 4, 0, 0)); // 1 行 2 列，水平间距 10

			// 左边的大文本框
			JTextArea leftTextArea = new JTextArea();
			leftTextArea.setLineWrap(true); // 自动换行
			leftTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			leftTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // 设置字体
			leftTextArea.setText(AccelerationRequest.server_cn.toString()+AccelerationRequest.server_cn2.toString());
			JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
			mainPanel.add(leftScrollPane);

			// 右边的小文本框
			JTextArea rightTextArea = new JTextArea();
			rightTextArea.setLineWrap(true); // 自动换行
			rightTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			rightTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // 设置字体
			rightTextArea.setText(AccelerationRequest.client_cn.toString()+AccelerationRequest.client_cn2.toString());
			JScrollPane rightScrollPane = new JScrollPane(rightTextArea);
			mainPanel.add(rightScrollPane);



			// 将主面板添加到对话框的中间区域
			dialog.add(mainPanel, BorderLayout.CENTER);

			// 创建关闭按钮
			JButton closeButton = new JButton("关闭");
			closeButton.addActionListener(ev -> dialog.dispose()); // 点击按钮时关闭对话框

			DataRequest2 request = new DataRequest2(AccelerationRequest.client_cn,AccelerationRequest.client_cn2,AccelerationRequest.server_cn,AccelerationRequest.server_cn2);
			JButton checkButton = new JButton("评测");;
			checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.checkJSON2(request).enqueue(new Callback<Result>() {
				@Override
				public void onResponse(Call<Result> call, Response<Result> response) {
					JOptionPane.showMessageDialog(dialog, "请点击平台评测按钮");
				}
				@Override
				public void onFailure(Call<Result> call, Throwable throwable) {
					System.out.println(throwable.getMessage());
				}
			}));
			//发送坐标信息
			checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.checkJSON3(new DataRequest(AccelerationRequest.wordCoordinate,null)).enqueue(new Callback<Result>() {
				@Override
				public void onResponse(Call<Result> call, Response<Result> response) {
//					JOptionPane.showMessageDialog(dialog, "请点击平台评测按钮");
				}
				@Override
				public void onFailure(Call<Result> call, Throwable throwable) {
					System.out.println(throwable.getMessage());
				}
			}));



			// 创建按钮面板
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 水平间距 10
			buttonPanel.add(closeButton); // 添加关闭按钮
			buttonPanel.add(checkButton);   // 添加新按钮
			dialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板放置在底部

			// 设置对话框属性
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 关闭时释放对话框资源
			dialog.setSize(800, 400); // 设置窗口宽度更大，适应两个文本框
			dialog.setLocationRelativeTo(this); // 设置相对于父窗口居中显示
			dialog.setVisible(true); // 显示对话框
		});
		panel.add(edu_button2, "growx 1, sizegroup selectbutton, wrap");

		SelectColorButton edu_button3 = new SelectColorButton("稳定性测评");
		edu_button3.addActionListener(e -> {
			// 创建一个模态对话框，父窗口为当前组件的顶层窗口
			JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "稳定性测评", Dialog.ModalityType.MODELESS);

			// 设置对话框的主布局为 BorderLayout
			dialog.setLayout(new BorderLayout());

			// 创建主内容面板，使用 GridLayout 管理两部分内容
			JPanel mainPanel = new JPanel(new GridLayout(1, 4, 0, 0)); // 1 行 2 列，水平间距 10

			// 左边的大文本框
			JTextArea leftTextArea = new JTextArea();
			leftTextArea.setLineWrap(true); // 自动换行
			leftTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			leftTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
			leftTextArea.setText(StabilityRequest.server_cn.toString());
			JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
			mainPanel.add(leftScrollPane);

			// 右边的小文本框
			JTextArea rightTextArea = new JTextArea();
			rightTextArea.setLineWrap(true); // 自动换行
			rightTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			rightTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
			rightTextArea.setText(StabilityRequest.client_cn.toString());
			JScrollPane rightScrollPane = new JScrollPane(rightTextArea);
			mainPanel.add(rightScrollPane);

			// 将主面板添加到对话框的中间区域
			dialog.add(mainPanel, BorderLayout.CENTER);

			// 创建关闭按钮
			JButton closeButton = new JButton("关闭");
			closeButton.addActionListener(ev -> dialog.dispose()); // 点击按钮时关闭对话框

			// 创建一个新的按钮
			JButton checkButton = new JButton("评测");
			DataRequest dataRequest = new DataRequest(StabilityRequest.client_cn,StabilityRequest.server_cn);



			checkButton.addActionListener(e1 -> {
				OpenRocket.eduCoderService.checkJSON(dataRequest).enqueue(new Callback<Result>() {
					@Override
					public void onResponse(Call<Result> call, Response<Result> response) {
						JOptionPane.showMessageDialog(dialog, "请点击平台评测按钮");

					}

					@Override
					public void onFailure(Call<Result> call, Throwable throwable) {

					}
				});
			});


			// 创建按钮面板
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 水平间距 10
			buttonPanel.add(closeButton); // 添加关闭按钮
			buttonPanel.add(checkButton);   // 添加新按钮
			dialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板放置在底部

			// 设置对话框属性
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 关闭时释放对话框资源
			dialog.setSize(800, 400); // 设置窗口宽度更大，适应两个文本框
			dialog.setLocationRelativeTo(this); // 设置相对于父窗口居中显示
			dialog.setVisible(true); // 显示对话框
		});
		panel.add(edu_button3, "growx 1, sizegroup selectbutton, wrap");


		SelectColorButton edu_button4 = new SelectColorButton("总体力矩系数测评");
		edu_button4.addActionListener(e -> {
			// 创建一个模态对话框，父窗口为当前组件的顶层窗口
			JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "总体力矩系数测评", Dialog.ModalityType.MODELESS);

			// 设置对话框的主布局为 BorderLayout
			dialog.setLayout(new BorderLayout());

			// 创建主内容面板，使用 GridLayout 管理两部分内容
			JPanel mainPanel = new JPanel(new GridLayout(1, 4, 0, 0)); // 1 行 2 列，水平间距 10

			// 左边的大文本框
			JTextArea leftTextArea = new JTextArea();
			leftTextArea.setLineWrap(true); // 自动换行
			leftTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			leftTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
			leftTextArea.setText(TotalMomentRequest.Server_cn1.toString()+TotalMomentRequest.Server_cn2.toString()+TotalMomentRequest.Server_cn3.toString());
			JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
			mainPanel.add(leftScrollPane);

			// 右边的小文本框
			JTextArea rightTextArea = new JTextArea();
			rightTextArea.setLineWrap(true); // 自动换行
			rightTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			rightTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
			rightTextArea.setText(TotalMomentRequest.Client_cn1.toString()+TotalMomentRequest.Client_cn2.toString()+TotalMomentRequest.Client_cn3.toString());
			JScrollPane rightScrollPane = new JScrollPane(rightTextArea);
			mainPanel.add(rightScrollPane);

			// 将主面板添加到对话框的中间区域
			dialog.add(mainPanel, BorderLayout.CENTER);

			// 创建关闭按钮
			JButton closeButton = new JButton("关闭");
			closeButton.addActionListener(ev -> dialog.dispose()); // 点击按钮时关闭对话框

			// 创建一个新的按钮
			DataRequest4 request = new DataRequest4(TotalMomentRequest.Client_cn1,TotalMomentRequest.Client_cn2,TotalMomentRequest.Client_cn3,
					TotalMomentRequest.Server_cn1,TotalMomentRequest.Server_cn2,TotalMomentRequest.Server_cn3);
			JButton checkButton = new JButton("评测");
			checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.checkJSON4(request).enqueue(new Callback<Result>() {
				@Override
				public void onResponse(Call<Result> call, Response<Result> response) {
					JOptionPane.showMessageDialog(dialog, "请点击平台评测按钮");
				}
				@Override
				public void onFailure(Call<Result> call, Throwable throwable) {
					System.out.println(throwable.getMessage());
				}
			}));



			// 创建按钮面板
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 水平间距 10
			buttonPanel.add(closeButton); // 添加关闭按钮
			buttonPanel.add(checkButton);   // 添加新按钮
			dialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板放置在底部

			// 设置对话框属性
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 关闭时释放对话框资源
			dialog.setSize(800, 400); // 设置窗口宽度更大，适应两个文本框
			dialog.setLocationRelativeTo(this); // 设置相对于父窗口居中显示
			dialog.setVisible(true); // 显示对话框
		});
		panel.add(edu_button4, "growx 1, sizegroup selectbutton, wrap");



		SelectColorButton edu_button5 = new SelectColorButton("总体压差阻力测评");
		edu_button5.addActionListener(e -> {
			// 创建一个模态对话框，父窗口为当前组件的顶层窗口
			JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "总体压差阻力测评", Dialog.ModalityType.MODELESS);

			// 设置对话框的主布局为 BorderLayout
			dialog.setLayout(new BorderLayout());

			// 创建主内容面板，使用 GridLayout 管理两部分内容
			JPanel mainPanel = new JPanel(new GridLayout(1, 4, 0, 0)); // 1 行 2 列，水平间距 10

			// 左边的大文本框
			JTextArea leftTextArea = new JTextArea();
			leftTextArea.setLineWrap(true); // 自动换行
			leftTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			leftTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
			leftTextArea.setText(TotalPressureCDRequest.server_cn.toString());
			JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
			mainPanel.add(leftScrollPane);

			// 右边的小文本框
			JTextArea rightTextArea = new JTextArea();
			rightTextArea.setLineWrap(true); // 自动换行
			rightTextArea.setWrapStyleWord(true); // 仅在单词边界处换行
			rightTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 设置字体
			rightTextArea.setText(TotalPressureCDRequest.client_cn.toString());
			JScrollPane rightScrollPane = new JScrollPane(rightTextArea);
			mainPanel.add(rightScrollPane);

			// 将主面板添加到对话框的中间区域
			dialog.add(mainPanel, BorderLayout.CENTER);

			// 创建关闭按钮
			JButton closeButton = new JButton("关闭");
			closeButton.addActionListener(ev -> dialog.dispose()); // 点击按钮时关闭对话框

			// 创建一个新的按钮
			DataRequest request = new DataRequest(TotalPressureCDRequest.client_cn, TotalPressureCDRequest.server_cn);
			JButton checkButton = new JButton("评测");
			checkButton.addActionListener(e1 -> OpenRocket.eduCoderService.checkJSON(request).enqueue(new Callback<Result>() {
				@Override
				public void onResponse(Call<Result> call, Response<Result> response) {
					JOptionPane.showMessageDialog(dialog, "请点击平台评测按钮");
				}
				@Override
				public void onFailure(Call<Result> call, Throwable throwable) {
					System.out.println(throwable.getMessage());
				}
			}));



			// 创建按钮面板
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 水平间距 10
			buttonPanel.add(closeButton); // 添加关闭按钮
			buttonPanel.add(checkButton);   // 添加新按钮
			dialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板放置在底部

			// 设置对话框属性
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 关闭时释放对话框资源
			dialog.setSize(800, 400); // 设置窗口宽度更大，适应两个文本框
			dialog.setLocationRelativeTo(this); // 设置相对于父窗口居中显示
			dialog.setVisible(true); // 显示对话框
		});
		panel.add(edu_button5, "growx 1, sizegroup selectbutton, wrap");



		selectedCountLabel = new JLabel();
		updateSelectedCount();
		panel.add(selectedCountLabel);
		
		this.add(panel, "grow 100, wrap");
		
		
		// These need to be in the order of the OPTIONS_XXX indices
		csvOptions = new CsvOptionPanel(SimulationExportPanel.class,
				trans.get("SimExpPan.checkbox.Includesimudesc"),
				trans.get("SimExpPan.checkbox.ttip.Includesimudesc"),
				trans.get("SimExpPan.checkbox.Includefielddesc"),

				trans.get("SimExpPan.checkbox.ttip.Includefielddesc"),
				trans.get("SimExpPan.checkbox.Incflightevents"),
				trans.get("SimExpPan.checkbox.ttip.Incflightevents"));
		
		this.add(csvOptions, "spany, split, growx 1");
		
		//// Add series selection box
		ArrayList<String> stages = new ArrayList<String>();
		stages.addAll(Util.generateSeriesLabels(simulation));
		
		final JComboBox<String> stageSelection = new JComboBox<String>(stages.toArray(new String[0]));
		stageSelection.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				int selectedStage = stageSelection.getSelectedIndex();
				branch = data.getBranch(selectedStage);
			}
			
		});
		if (stages.size() > 1) {
			// Only show the combo box if there are at least 2 entries (ie, "Main", and one other one
			JPanel stagePanel = new JPanel(new MigLayout("fill"));
			stagePanel.setBorder(BorderFactory.createTitledBorder(trans.get("SimExpPan.border.Stage")));
			stagePanel.add(stageSelection, "growx");
			this.add(stagePanel, "spany, split, growx 1");
		}
		
		// Space-filling panel
		panel = new JPanel();
		this.add(panel, "width 1, height 1, grow 1");
		
		/*
		// Export button
		button = new SelectColorButton(trans.get("SimExpPan.but.Exporttofile"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doExport();
			}
		});
		this.add(button, "gapbottom para, gapright para, right");
		*/
	}
	
	public boolean doExport() {
		JFileChooser chooser = new SaveFileChooser();
		chooser.setFileFilter(FileHelper.CSV_FILTER);
		chooser.setCurrentDirectory(((SwingPreferences) Application.getPreferences()).getDefaultDirectory());
		
		if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return false;
		
		File file = chooser.getSelectedFile();
		if (file == null)
			return false;
		
		file = FileHelper.forceExtension(file, "csv");
		if (!FileHelper.confirmWrite(file, this)) {
			return false;
		}
		
		
		String commentChar = csvOptions.getCommentCharacter();
		String fieldSep = csvOptions.getFieldSeparator();
		int decimalPlaces = csvOptions.getDecimalPlaces();
		boolean isExponentialNotation = csvOptions.isExponentialNotation();
		boolean simulationComment = csvOptions.getSelectionOption(OPTION_SIMULATION_COMMENTS);
		boolean fieldComment = csvOptions.getSelectionOption(OPTION_FIELD_DESCRIPTIONS);
		boolean eventComment = csvOptions.getSelectionOption(OPTION_FLIGHT_EVENTS);
		csvOptions.storePreferences();
		
		// Store preferences and export
		int n = 0;
		((SwingPreferences) Application.getPreferences()).setDefaultDirectory(chooser.getCurrentDirectory());
		for (int i = 0; i < selected.length; i++) {
			((SwingPreferences) Application.getPreferences()).setExportSelected(types[i], selected[i]);
			if (selected[i])
				n++;
		}
		
		
		FlightDataType[] fieldTypes = new FlightDataType[n];
		Unit[] fieldUnits = new Unit[n];
		int pos = 0;
		for (int i = 0; i < selected.length; i++) {
			if (selected[i]) {
				fieldTypes[pos] = types[i];
				fieldUnits[pos] = units[i];
				pos++;
			}
		}
		
		if (fieldSep.equals(SPACE)) {
			fieldSep = " ";
		} else if (fieldSep.equals(TAB)) {
			fieldSep = "\t";
		}


		SaveCSVWorker.export(file, simulation, branch, fieldTypes, fieldUnits, fieldSep, decimalPlaces,
				isExponentialNotation, commentChar, simulationComment, fieldComment, eventComment,
				SwingUtilities.getWindowAncestor(this));
		
		return true;
	}
	
	
	private void updateSelectedCount() {
		int total = selected.length;
		int n = 0;
		String str;
		
		for (int i = 0; i < selected.length; i++) {
			if (selected[i])
				n++;
		}
		
		if (n == 1) {
			//// Exporting 1 variable out of 
			str = trans.get("SimExpPan.ExportingVar.desc1") + " " + total + ".";
		} else {
			//// Exporting 
			//// variables out of
			str = trans.get("SimExpPan.ExportingVar.desc2") + " " + n + " " +
					trans.get("SimExpPan.ExportingVar.desc3") + " " + total + ".";
		}
		
		selectedCountLabel.setText(str);
	}
	
	
	
	/**
	 * A table cell renderer that uses another renderer and sets the background and
	 * foreground of the returned component based on the selection of the variable.
	 */
	private class SelectionBackgroundCellRenderer implements TableCellRenderer {
		
		private final TableCellRenderer renderer;
		
		public SelectionBackgroundCellRenderer(TableCellRenderer renderer) {
			this.renderer = renderer;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable myTable, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			
			Component component = renderer.getTableCellRendererComponent(myTable,
					value, isSelected, hasFocus, row, column);
			
			if (selected[row]) {
				component.setBackground(myTable.getSelectionBackground());
				component.setForeground(myTable.getSelectionForeground());
			} else {
				component.setBackground(myTable.getBackground());
				component.setForeground(myTable.getForeground());
			}
			
			return component;
		}
		
	}
	
	
	/**
	 * The table model for the variable selection.
	 */
	private class SelectionTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 493067422917621072L;
		private static final int SELECTED = 0;
		private static final int NAME = 1;
		private static final int UNIT = 2;
		
		@Override
		public int getColumnCount() {
			return 3;
		}
		
		@Override
		public int getRowCount() {
			return types.length;
		}
		
		@Override
		public String getColumnName(int column) {
			switch (column) {
			case SELECTED:
				return "";
			case NAME:
				//// Variable
				return trans.get("SimExpPan.Col.Variable");
			case UNIT:
				//// Unit
				return trans.get("SimExpPan.Col.Unit");
			default:
				throw new IndexOutOfBoundsException("column=" + column);
			}
			
		}
		
		@Override
		public Class<?> getColumnClass(int column) {
			switch (column) {
			case SELECTED:
				return Boolean.class;
			case NAME:
				return FlightDataType.class;
			case UNIT:
				return Unit.class;
			default:
				throw new IndexOutOfBoundsException("column=" + column);
			}
		}
		
		@Override
		public Object getValueAt(int row, int column) {
			
			switch (column) {
			case SELECTED:
				return selected[row];
				
			case NAME:
				return types[row];
				
			case UNIT:
				return units[row];
				
			default:
				throw new IndexOutOfBoundsException("column=" + column);
			}
			
		}
		
		@Override
		public void setValueAt(Object value, int row, int column) {
			
			switch (column) {
			case SELECTED:
				selected[row] = (Boolean) value;
				this.fireTableRowsUpdated(row, row);
				updateSelectedCount();
				break;
			
			case NAME:
				break;
			
			case UNIT:
				units[row] = (Unit) value;
				break;
			
			default:
				throw new IndexOutOfBoundsException("column=" + column);
			}
			
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			switch (column) {
			case SELECTED:
				return true;
				
			case NAME:
				return false;
				
			case UNIT:
				return types[row].getUnitGroup().getUnitCount() > 1;
				
			default:
				throw new IndexOutOfBoundsException("column=" + column);
			}
		}
		
		public void selectAll() {
			Arrays.fill(selected, true);
			updateSelectedCount();
			this.fireTableDataChanged();
		}
		
		public void selectNone() {
			Arrays.fill(selected, false);
			updateSelectedCount();
			this.fireTableDataChanged();
		}
		
	}
	
}
