package net.sf.openrocket.gui.dialogs.motor.thrustcurve;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import net.sf.openrocket.database.motor.ThrustCurveMotorSetDatabase;
import net.sf.openrocket.gui.components.UnitSelector;
import net.sf.openrocket.gui.plot.Util;
import net.sf.openrocket.gui.simulation.FlightDataComboBox;
import net.sf.openrocket.gui.simulation.SimulationPlotPanel;
import net.sf.openrocket.gui.util.*;
import net.sf.openrocket.gui.widgets.SelectColorButton;
import net.sf.openrocket.simulation.FlightDataType;
import net.sf.openrocket.simulation.FlightDataTypeGroup;
import net.sf.openrocket.startup.OpenRocket;
import net.sf.openrocket.unit.Unit;
import net.sf.openrocket.unit.UnitGroup;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.util.StateChangeListener;
import net.sf.openrocket.utils.educoder.DataResult;
import net.sf.openrocket.utils.educoder.Result;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.miginfocom.swing.MigLayout;
import net.sf.openrocket.database.motor.ThrustCurveMotorSet;
import net.sf.openrocket.gui.components.StyledLabel;
import net.sf.openrocket.gui.dialogs.motor.CloseableDialog;
import net.sf.openrocket.gui.dialogs.motor.MotorSelector;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.logging.Markers;
import net.sf.openrocket.motor.Manufacturer;
import net.sf.openrocket.motor.Motor;
import net.sf.openrocket.motor.MotorConfiguration;
import net.sf.openrocket.motor.ThrustCurveMotor;
import net.sf.openrocket.rocketcomponent.FlightConfigurationId;
import net.sf.openrocket.rocketcomponent.MotorMount;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.util.BugException;
import net.sf.openrocket.utils.MotorCorrelation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThrustCurveMotorSelectionPanel extends JPanel implements MotorSelector {
    private static final long serialVersionUID = -8737784181512143155L;

    private static final Logger log = LoggerFactory.getLogger(ThrustCurveMotorSelectionPanel.class);

    private static final Translator trans = Application.getTranslator();

    private static final double MOTOR_SIMILARITY_THRESHOLD = 0.95;

    private static final Paint[] CURVE_COLORS = ChartColor.createDefaultPaintArray();

    private static final ThrustCurveMotorComparator MOTOR_COMPARATOR = new ThrustCurveMotorComparator();

    private List<ThrustCurveMotorSet> database;

    private CloseableDialog dialog = null;

    private final ThrustCurveMotorDatabaseModel model;
    private final JTable table;
    private final TableRowSorter<TableModel> sorter;
    private final MotorRowFilter rowFilter;

    private final JCheckBox hideSimilarBox;
    private final JCheckBox hideUnavailableBox;

    private final JTextField searchField;

    private final JLabel curveSelectionLabel;
    private final JComboBox<MotorHolder> curveSelectionBox;
    private final DefaultComboBoxModel<MotorHolder> curveSelectionModel;
    private final JLabel ejectionChargeDelayLabel;
    private final JComboBox<String> delayBox;
    private final JLabel nrOfMotorsLabel;

    private final MotorInformationPanel motorInformationPanel;
    private final MotorFilterPanel motorFilterPanel;

    private ThrustCurveMotor selectedMotor;
    private ThrustCurveMotorSet selectedMotorSet;
    private double selectedDelay;

    private List<HashMap<String, String>> simulateFunctions;

    private static Color dimTextColor;

    static {
        initColors();
    }

    public ThrustCurveMotorSelectionPanel(final FlightConfigurationId fcid, MotorMount mount) {
        this();
        setMotorMountAndConfig(fcid, mount);

    }

    public ThrustCurveMotorSelectionPanel() {
        super(new MigLayout("fill", "[grow][]"));

        // Construct the database (adding the current motor if not in the db already)
        database = Application.getThrustCurveMotorSetDatabase().getMotorSets();

        model = new ThrustCurveMotorDatabaseModel(database);
        rowFilter = new MotorRowFilter(model);
        motorInformationPanel = new MotorInformationPanel();


        //// MotorFilter
        {
            // Find all the manufacturers:
            Set<Manufacturer> allManufacturers = new HashSet<Manufacturer>();
            for (ThrustCurveMotorSet s : database) {
                allManufacturers.add(s.getManufacturer());
            }

            motorFilterPanel = new MotorFilterPanel(allManufacturers, rowFilter) {
                private static final long serialVersionUID = 8441555209804602238L;

                @Override
                public void onSelectionChanged() {
                    sorter.sort();
                    scrollSelectionVisible();
                }
            };

        }

        ////  GUI
        JPanel panel = new JPanel(new MigLayout("fill", "[][grow]"));

        //// Select thrust curve:
        {
            curveSelectionLabel = new JLabel(trans.get("TCMotorSelPan.lbl.Selectthrustcurve"));
            panel.add(curveSelectionLabel);

            curveSelectionModel = new DefaultComboBoxModel<MotorHolder>();
            curveSelectionBox = new JComboBox<MotorHolder>(curveSelectionModel);
            @SuppressWarnings("unchecked")
            ListCellRenderer<MotorHolder> lcr = (ListCellRenderer<MotorHolder>) curveSelectionBox.getRenderer();
            curveSelectionBox.setRenderer(new CurveSelectionRenderer(lcr));
            curveSelectionBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MotorHolder value = (MotorHolder) curveSelectionBox.getSelectedItem();

                    if (value != null) {
                        select(((MotorHolder) value).getMotor());

                    }
                }
            });
            panel.add(curveSelectionBox, "growx, wrap");
        }

        // Ejection charge delay:
        {
            ejectionChargeDelayLabel = new JLabel(trans.get("TCMotorSelPan.lbl.Ejectionchargedelay"));
            panel.add(ejectionChargeDelayLabel);

            delayBox = new JComboBox<String>();
            delayBox.setEditable(true);
            delayBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sel = (String) delayBox.getSelectedItem();
                    if (sel == null) {
                        log.debug("Selected charge delay is null");
                        return;
                    }
                    //// Plugged (or None)
                    if (sel.equalsIgnoreCase(trans.get("TCMotorSelPan.delayBox.Plugged")) ||
                            sel.equalsIgnoreCase(trans.get("TCMotorSelPan.delayBox.PluggedNone"))) {
                        selectedDelay = Motor.PLUGGED_DELAY;
                    } else {
                        try {
                            selectedDelay = Double.parseDouble(sel);
                        } catch (NumberFormatException ignore) {
                        }
                    }
                    setDelays(false);
                }
            });
            panel.add(delayBox, "growx,wrap");
            //// (or type in desired delay in seconds)
            panel.add(new StyledLabel(trans.get("TCMotorSelPan.lbl.Numberofseconds"), -3), "skip, wrap");
            setDelays(false);
        }

        //// Hide very similar thrust curves
        {
            hideSimilarBox = new JCheckBox(trans.get("TCMotorSelPan.checkbox.hideSimilar"));
            GUIUtil.changeFontSize(hideSimilarBox, -1);
            hideSimilarBox.setSelected(Application.getPreferences().getBoolean(net.sf.openrocket.startup.Preferences.MOTOR_HIDE_SIMILAR, true));
            hideSimilarBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Application.getPreferences().putBoolean(net.sf.openrocket.startup.Preferences.MOTOR_HIDE_SIMILAR, hideSimilarBox.isSelected());
                    updateData();
                }
            });
            panel.add(hideSimilarBox, "gapleft para, spanx, growx, wrap");
        }

        //// Hide unavailable motors
        {
            hideUnavailableBox = new JCheckBox(trans.get("TCMotorSelPan.checkbox.hideUnavailable"));
            GUIUtil.changeFontSize(hideUnavailableBox, -1);
            hideUnavailableBox.setSelected(Application.getPreferences().getBoolean(net.sf.openrocket.startup.Preferences.MOTOR_HIDE_UNAVAILABLE, true));
            hideUnavailableBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Application.getPreferences().putBoolean(net.sf.openrocket.startup.Preferences.MOTOR_HIDE_UNAVAILABLE, hideUnavailableBox.isSelected());
                    motorFilterPanel.setHideUnavailable(hideUnavailableBox.isSelected());
                }
            });
            panel.add(hideUnavailableBox, "gapleft para, spanx, growx, wrap");

        }

        //// Motor name column
        {
            JLabel motorNameColumn = new JLabel(trans.get("TCMotorSelPan.lbl.motorNameColumn"));
            motorNameColumn.setToolTipText(trans.get("TCMotorSelPan.lbl.motorNameColumn.ttip"));
            JRadioButton commonName = new JRadioButton(trans.get("TCMotorSelPan.btn.commonName"));
            JRadioButton designation = new JRadioButton(trans.get("TCMotorSelPan.btn.designation"));
            ButtonGroup bg = new ButtonGroup();
            bg.add(commonName);
            bg.add(designation);
            commonName.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((SwingPreferences) Application.getPreferences()).setMotorNameColumn(false);
                    int selectedRow = table.getSelectedRow();
                    model.fireTableDataChanged();
                    if (selectedRow >= 0) {
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                    curveSelectionBox.revalidate();
                    curveSelectionBox.repaint();
                }
            });
            designation.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((SwingPreferences) Application.getPreferences()).setMotorNameColumn(true);
                    int selectedRow = table.getSelectedRow();
                    model.fireTableDataChanged();
                    if (selectedRow >= 0) {
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                    curveSelectionBox.revalidate();
                    curveSelectionBox.repaint();
                }
            });

            boolean initValue = ((SwingPreferences) Application.getPreferences()).getMotorNameColumn();
            commonName.setSelected(!initValue);
            designation.setSelected(initValue);

            panel.add(motorNameColumn, "gapleft para");
            panel.add(commonName);
            panel.add(designation, "spanx, growx, wrap");
        }

        //// Motor selection table
        {
            table = new JTable(model);

            // Set comparators and widths
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            sorter = new TableRowSorter<TableModel>(model);
            for (int i = 0; i < ThrustCurveMotorColumns.values().length; i++) {
                ThrustCurveMotorColumns column = ThrustCurveMotorColumns.values()[i];
                sorter.setComparator(i, column.getComparator());
                table.getColumnModel().getColumn(i).setPreferredWidth(column.getWidth());
            }
            table.setRowSorter(sorter);
            // force initial sort order to by diameter, total impulse, manufacturer
            {
                RowSorter.SortKey[] sortKeys = {
                        new RowSorter.SortKey(ThrustCurveMotorColumns.DIAMETER.ordinal(), SortOrder.ASCENDING),
                        new RowSorter.SortKey(ThrustCurveMotorColumns.TOTAL_IMPULSE.ordinal(), SortOrder.ASCENDING),
                        new RowSorter.SortKey(ThrustCurveMotorColumns.MANUFACTURER.ordinal(), SortOrder.ASCENDING)
                };
                sorter.setSortKeys(Arrays.asList(sortKeys));
            }

            sorter.setRowFilter(rowFilter);

            // Set selection and double-click listeners
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    selectMotorFromTable();
                }
            });
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                        if (dialog != null) {
                            dialog.close(true);
                        }
                    }
                }
            });

            JScrollPane scrollpane = new JScrollPane();
            scrollpane.setViewportView(table);
            panel.add(scrollpane, "grow, width :500:, spanx, push, wrap");

        }

        // Number of motors
        {
            nrOfMotorsLabel = new StyledLabel(-2f, StyledLabel.Style.ITALIC);
            nrOfMotorsLabel.setToolTipText(trans.get("TCMotorSelPan.lbl.ttip.nrOfMotors"));
            updateNrOfMotors();
            nrOfMotorsLabel.setForeground(dimTextColor);
            panel.add(nrOfMotorsLabel, "gapleft para, spanx, wrap");
            sorter.addRowSorterListener(new RowSorterListener() {
                @Override
                public void sorterChanged(RowSorterEvent e) {
                    updateNrOfMotors();
                }
            });
            rowFilter.addChangeListener(new StateChangeListener() {
                @Override
                public void stateChanged(EventObject e) {
                    updateNrOfMotors();
                }
            });
        }

        // Search field
        {
            //// Search:
            StyledLabel label = new StyledLabel(trans.get("TCMotorSelPan.lbl.Search"));
            panel.add(label);
            searchField = new JTextField();
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                private void update() {
                    String text = searchField.getText().trim();
                    String[] split = text.split("\\s+");
                    rowFilter.setSearchTerms(Arrays.asList(split));
                    sorter.sort();
                    scrollSelectionVisible();
                }
            });
            panel.add(searchField, "span, growx");
        }

        {
            JButton newMotor = new JButton("自定义发动机(数据点拟合)");
            panel.add(newMotor);

            newMotor.addActionListener(e -> {
                // 创建一个模态对话框
                JDialog chartDialog = new JDialog((Frame) null, "自定义曲线", true);
                chartDialog.setSize(800, 600);
                chartDialog.setLocationRelativeTo(null);

                // 设置对话框的布局
                chartDialog.setLayout(new BorderLayout());

                // Thrust curve plot
                JFreeChart chart = ChartFactory.createXYLineChart(
                        "Motor thrust curves",
                        "Time",
                        "Thrust",
                        null,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );

                // 获取 XYPlot
                XYPlot plot = chart.getXYPlot();
                plot.setBackgroundPaint(Color.WHITE);
                plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
                plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

                // 创建 chartPanel 并设置其大小
                ChartPanel chartPanel = new ChartPanel(chart,
                        false, true, false, true, true);
                chartPanel.setMouseWheelEnabled(true);
                chartPanel.setPreferredSize(new Dimension(600, 400));

                // 配置渲染器
                StandardXYItemRenderer renderer = new StandardXYItemRenderer();
                renderer.setBaseShapesVisible(true);
                renderer.setBaseShapesFilled(true);
                plot.setRenderer(renderer);
                simulateFunctions = new ArrayList<>();

                // 创建数据集
                XYSeriesCollection dataset = new XYSeriesCollection();
                XYSeries xySeries = new XYSeries("Thrust Curve");
                dataset.addSeries(xySeries);
                plot.setDataset(dataset);

                // 创建输入框和按钮
                JPanel controlPanel = new JPanel(new MigLayout("wrap 4", "[][][]", "[]"));
                //		controlPanel.setLayout(new MigLayout("gap 10px", "", ""));
//				controlPanel.setLayout(new GridLayout(4, 2, 10, 10));  // 4 行 2 列，10 像素间距
                JTextField xField = new JTextField(40);  // 输入 x 序列
                JTextField yField = new JTextField(40);  // 输入 y 序列

                JTextField designation = new JTextField(20);  // 输入 制造商
                JTextField commonName = new JTextField(20);  // 输入 发动机名称
                JTextField weight = new JTextField(20);  // 输入 发动机质量
                JTextField diameter = new JTextField(20);  // 输入 直径
                JTextField length = new JTextField(20);  // 输入 长度
                String[] options = {"Single-use", "Reloadable", "Hybrid", "Unknown"};
                JComboBox<Object> box = new JComboBox<>(options);//发动机类型

                JButton updateButton = new JButton("生成推力曲线");

                xField.setBorder(BorderFactory.createBevelBorder(1));
                yField.setBorder(BorderFactory.createBevelBorder(1));
                designation.setBorder(BorderFactory.createBevelBorder(1));
                commonName.setBorder(BorderFactory.createBevelBorder(1));
                box.setBorder(BorderFactory.createBevelBorder(1));
                length.setBorder(BorderFactory.createBevelBorder(1));
                diameter.setBorder(BorderFactory.createBevelBorder(1));
                weight.setBorder(BorderFactory.createBevelBorder(1));
                controlPanel.add(new JLabel("X轴:时间(逗号分隔):"));
                controlPanel.add(xField);
                xField.setEditable(false);
                controlPanel.add(updateButton, "gapleft 10,wrap");

                controlPanel.add(new JLabel("Y轴:推力(逗号分隔):"));

                controlPanel.add(yField, "wrap");
                yField.setEditable(false);

                controlPanel.add(new JLabel("发动机质量:(kg)"), "align center");
                controlPanel.add(weight);
                controlPanel.add(new JLabel("发动机类型:"), "gapleft 10");
                controlPanel.add(box, "align left");

                controlPanel.add(new JLabel("发动机直径:(m)"), "align center");
                controlPanel.add(diameter);
                controlPanel.add(new JLabel("发动机长度:(m)"), "gapleft 10");
                controlPanel.add(length, "align left");

                controlPanel.add(new JLabel("设计者:"), "align center");
                controlPanel.add(designation);
                controlPanel.add(new JLabel("发动机名称:"), "gapleft 10");
                controlPanel.add(commonName, "align left");

                JButton loadIn = new JButton("    导入    ");
                controlPanel.add(loadIn, "gapleft 10");
                JButton loadOut = new JButton("    导出    ");
                controlPanel.add(loadOut, "gapleft 10");
                //x序列 y序列 长度
                loadIn.addActionListener(
                        e3 -> {
                            List<Object> objects = doImport(chartDialog);
                            if (objects == null) return;
                            simulateFunctions.clear();
                            List<String> xx = (List<String>) objects.get(0);
                            List<String> yy = (List<String>) objects.get(1);
                            String xList = xx.stream().
                                    map(String::valueOf).collect(Collectors.joining(","));
                            String yList = yy.stream().
                                    map(String::valueOf).collect(Collectors.joining(","));
                            xField.setText(xList);
                            yField.setText(yList);
                            xySeries.clear();
                            for (int i = 0; i < xx.size(); i++) {
                                double x = Double.parseDouble(xx.get(i).trim());
                                double y = Double.parseDouble(yy.get(i).trim());
                                xySeries.add(x, y);
                            }
                            String design = (String) objects.get(2);
                            designation.setText(design);
                            String coName = (String) objects.get(3);
                            commonName.setText(coName);
                            String dia = (String) objects.get(4);
                            diameter.setText(dia);
                            String len = (String) objects.get(5);
                            length.setText(len);
                            String wei = (String) objects.get(6);
                            weight.setText(wei);
                            String caseType = (String) objects.get(7);
                            designation.setText(design);
                            box.setSelectedItem(caseType);

                            //	objects.get()


                        });
                JButton jButton = new JButton("提交");
                controlPanel.add(jButton);
                jButton.addActionListener(e1 -> {
                    try {
                        // 获取 x 和 y 的输入并解析
                        String[] xValues = xField.getText().split(",");
                        String[] yValues = yField.getText().split(",");
                        if ((xValues.length == 1 || yValues.length == 1) && (xValues[0].equals("") || yValues[0].equals(""))) {
                            throw new IllegalArgumentException("X轴或Y轴不能为空");
                        }
                        if (xValues.length != yValues.length) {
                            throw new IllegalArgumentException("X 和 Y 的值数量必须相等");
                        }
                        if (designation.getText().length() == 0) {
                            throw new IllegalArgumentException("请留下你的姓名");
                        }
                        if (commonName.getText().length() == 0) {
                            throw new IllegalArgumentException("请留下你的作品名称");
                        }
                        if (diameter.getText().length() == 0) {
                            throw new IllegalArgumentException("直径不能为空");
                        }
                        if (length.getText().length() == 0) {
                            throw new IllegalArgumentException("长度不能为空");
                        }
                        if (weight.getText().length() == 0) {
                            throw new IllegalArgumentException("发动机初始重量不能为空");
                        }


                        //转double
                        double[] timePoint = new double[xValues.length];
                        double[] thrustPoint = new double[yValues.length];
                        for (int i = 0; i < xValues.length; i++) {
                            timePoint[i] = Double.parseDouble(xValues[i]);
                            thrustPoint[i] = Double.parseDouble(yValues[i]);

                        }

                        //设计者
                        String design = designation.getText();

                        //火箭类型
                        String selectedItem = (String) box.getSelectedItem();
                        Motor.Type motorType = null;
                        if (selectedItem.equals("Single-use")) {
                            motorType = Motor.Type.SINGLE;
                        } else if (selectedItem.equals("Reloadable")) {
                            motorType = Motor.Type.RELOAD;
                        } else if (selectedItem.equals("Hybrid")) {
                            motorType = Motor.Type.HYBRID;
                        } else {
                            motorType = Motor.Type.UNKNOWN;
                        }


                        ThrustCurveMotor motor = new ThrustCurveMotor.Builder()
                                .setManufacturer(Manufacturer.getManufacturer(design))
                                .setDesignation(design)
                                .setDescription(" ")
                                .setCaseInfo(" ")
                                .setMotorType(motorType)
                                .setStandardDelays(new double[]{})
                                .setInitialMass(Double.parseDouble(weight.getText()))
                                .setDiameter(Double.parseDouble(diameter.getText()))
                                .setLength(Double.parseDouble(length.getText()))
                                .setTimePoints(timePoint)
                                .setThrustPoints(thrustPoint)
                                .setCGPoints(new Coordinate[]{
                                        new Coordinate(.035, 0, 0, 0.025), new Coordinate(.035, 0, 0, 0.025)})
                                .setDigest(design + "test")
                                .build();
                        Method method = ThrustCurveMotor.class.getDeclaredMethod("computeStatistics");
                        method.setAccessible(true);
                        method.invoke(motor);

                        //calculate weight
                        double I_sp = motor.getTotalImpulseEstimate() / (motor.getBurnTimeEstimate() * motor.getInitialMass());

                        double[] mass = new double[motor.getTimePoints().length];
                        mass[0] = motor.getInitialMass();

                        for (int i = 1; i < mass.length; i++) {
                            double deltaT = motor.getTimePoints()[i] - motor.getTimePoints()[i - 1];
                            double massFlowRate = motor.getThrustPoints()[i - 1] / (I_sp * 9.80665);
                            double massLoss = massFlowRate * deltaT;
                            mass[i] = mass[i - 1] - massLoss;
                        }


                        Coordinate[] cgs = new Coordinate[xValues.length];
                        for (int i = 0; i < cgs.length; i++) {
                            Coordinate coordinate = new Coordinate(Double.parseDouble(length.getText()) / 2, 0, 0, mass[i]);
                            cgs[i] = coordinate;
                        }

                        Field field = ThrustCurveMotor.class.getDeclaredField("cg");
                        field.setAccessible(true);
                        field.set(motor, cgs);

                        JOptionPane.showMessageDialog(null, "发动机创建成功");
                        ThrustCurveMotorSetDatabase database1 = Application.getThrustCurveMotorSetDatabase();
                        database1.addMotor(motor);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(chartDialog, "请输入有效的数字");
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(chartDialog, ex.getMessage());
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    } catch (NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    } catch (NoSuchFieldException ex) {
                        throw new RuntimeException(ex);
                    }

                });


                loadOut.addActionListener(e2 -> doExport(chartDialog, xField.getText().split(","), yField.getText().split(","),
                        designation.getText(), commonName.getText(),
                        diameter.getText(), length.getText(), weight.getText(), (String) box.getSelectedItem()));


                //添加接口................
                updateButton.addActionListener(e1 -> {
                    //调用接口
                    OpenRocket.eduCoderService.calculatePoint(200).enqueue(new Callback<DataResult>() {
                        @Override
                        public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                            try {
                                DataResult result = response.body();
                                //解析函数.......
                                String xx = result.getResult().get(0).stream().
                                        map(String::valueOf).collect(Collectors.joining(","));
                                String yy = result.getResult().get(1).stream().
                                        map(String::valueOf).collect(Collectors.joining(","));
                                String kg = result.getResult().get(2).get(0).toString();
                                String d = result.getResult().get(3).get(0).toString();
                                String len = result.getResult().get(4).get(0).toString();
                                String design = result.getResult().get(5).get(0).toString();
                                String name = result.getResult().get(6).get(0).toString();
                                xField.setText(xx);
                                yField.setText(yy);
                                weight.setText(kg);
                                length.setText(len);
								diameter.setText(d);
                                designation.setText(design);
                                commonName.setText(name);

                                // 获取 x 和 y 的输入并解析
                                String[] xValues = xField.getText().split(",");
                                String[] yValues = yField.getText().split(",");
                                if (xValues.length != yValues.length) {
                                    throw new IllegalArgumentException("X 和 Y 的值数量必须相等");
                                }
                                xySeries.clear();
                                // 批量添加数据点
                                for (int i = 0; i < xValues.length; i++) {
                                    double x = Double.parseDouble(xValues[i].trim());
                                    double y = Double.parseDouble(yValues[i].trim());
                                    xySeries.add(x, y);
                                }

                            } catch (IllegalStateException e) {
                                JOptionPane.showMessageDialog(chartDialog, "参数不合法");
                            } catch (IllegalArgumentException e) {
                                JOptionPane.showMessageDialog(chartDialog, e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<DataResult> call, @NotNull Throwable throwable) {
                            SwingUtilities.invokeLater(() ->
                                    JOptionPane.showMessageDialog(chartDialog, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                        }
                    });
                    //


                });


                // 将 chartPanel 添加到对话框的中心f
                chartDialog.add(chartPanel, BorderLayout.CENTER);
                // 将控制面板（输入框和按钮）添加到对话框的底部
                chartDialog.add(controlPanel, BorderLayout.SOUTH);

                // 显示对话框
                chartDialog.setVisible(true);
            });
        }
        {
            JButton newMotor = new JButton("自定义发动机(函数定义)");
            panel.add(newMotor);

            newMotor.addActionListener(e -> {
                // 创建一个模态对话框
                JDialog chartDialog = new JDialog((Frame) null, "自定义曲线", true);
                chartDialog.setSize(800, 600);
                chartDialog.setLocationRelativeTo(null);

                // 设置对话框的布局
                chartDialog.setLayout(new BorderLayout());

                // Thrust curve plot
                JFreeChart chart = ChartFactory.createXYLineChart(
                        "Motor thrust curves",
                        "Time",
                        "Thrust",
                        null,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );

                // 获取 XYPlot
                XYPlot plot = chart.getXYPlot();
                plot.setBackgroundPaint(Color.WHITE);
                plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
                plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

                // 创建 chartPanel 并设置其大小
                ChartPanel chartPanel = new ChartPanel(chart,
                        false, true, false, true, true);
                chartPanel.setMouseWheelEnabled(true);
                chartPanel.setPreferredSize(new Dimension(600, 400));

                // 配置渲染器
                StandardXYItemRenderer renderer = new StandardXYItemRenderer();
                renderer.setBaseShapesVisible(true);
                renderer.setBaseShapesFilled(true);
                plot.setRenderer(renderer);
                simulateFunctions = new ArrayList<>();

                // 创建数据集
                XYSeriesCollection dataset = new XYSeriesCollection();
                XYSeries xySeries = new XYSeries("Thrust Curve");
                dataset.addSeries(xySeries);
                plot.setDataset(dataset);

                // 创建输入框和按钮
                JPanel controlPanel = new JPanel(new MigLayout("wrap 4", "[][][]", "[]"));
                //		controlPanel.setLayout(new MigLayout("gap 10px", "", ""));
//				controlPanel.setLayout(new GridLayout(4, 2, 10, 10));  // 4 行 2 列，10 像素间距
                JTextField xField = new JTextField(40);  // 输入 x 序列
                JTextField yField = new JTextField(40);  // 输入 y 序列

                JTextField designation = new JTextField(20);  // 输入 制造商
                JTextField commonName = new JTextField(20);  // 输入 发动机名称
                JTextField weight = new JTextField(20);  // 输入 发动机质量
                JTextField diameter = new JTextField(20);  // 输入 直径
                JTextField length = new JTextField(20);  // 输入 长度
                String[] options = {"Single-use", "Reloadable", "Hybrid", "Unknown"};
                JComboBox<Object> box = new JComboBox<>(options);//发动机类型

                JButton updateButton = new JButton("生成推力曲线");

                xField.setBorder(BorderFactory.createBevelBorder(1));
                yField.setBorder(BorderFactory.createBevelBorder(1));
                designation.setBorder(BorderFactory.createBevelBorder(1));
                commonName.setBorder(BorderFactory.createBevelBorder(1));
                box.setBorder(BorderFactory.createBevelBorder(1));
                length.setBorder(BorderFactory.createBevelBorder(1));
                diameter.setBorder(BorderFactory.createBevelBorder(1));
                weight.setBorder(BorderFactory.createBevelBorder(1));
                controlPanel.add(new JLabel("X轴:时间(逗号分隔):"));
                controlPanel.add(xField);
                controlPanel.add(updateButton, "gapleft 10,wrap");

                controlPanel.add(new JLabel("Y轴:推力(逗号分隔):"));
                controlPanel.add(yField, "wrap");
                xField.setEditable(false);
                yField.setEditable(false);

                controlPanel.add(new JLabel("发动机质量:(kg)"), "align center");
                controlPanel.add(weight);
                controlPanel.add(new JLabel("发动机类型:"), "gapleft 10");
                controlPanel.add(box, "align left");

                controlPanel.add(new JLabel("发动机直径:(m)"), "align center");
                controlPanel.add(diameter);
                controlPanel.add(new JLabel("发动机长度:(m)"), "gapleft 10");
                controlPanel.add(length, "align left");

                controlPanel.add(new JLabel("设计者:"), "align center");
                controlPanel.add(designation);
                controlPanel.add(new JLabel("发动机名称:"), "gapleft 10");
                controlPanel.add(commonName, "align left");

                JButton loadIn = new JButton("    导入    ");
                controlPanel.add(loadIn, "gapleft 10");
                //x序列 y序列 长度
                loadIn.addActionListener(
                        e3 -> {
                            List<Object> objects = doImport(chartDialog);
                            if (objects == null) return;
                            simulateFunctions.clear();
                            List<String> xx = (List<String>) objects.get(0);
                            List<String> yy = (List<String>) objects.get(1);
                            String xList = xx.stream().
                                    map(String::valueOf).collect(Collectors.joining(","));
                            String yList = yy.stream().
                                    map(String::valueOf).collect(Collectors.joining(","));
                            xField.setText(xList);
                            yField.setText(yList);
                            xySeries.clear();
                            for (int i = 0; i < xx.size(); i++) {
                                double x = Double.parseDouble(xx.get(i).trim());
                                double y = Double.parseDouble(yy.get(i).trim());
                                xySeries.add(x, y);
                            }

                            String design = (String) objects.get(2);
                            designation.setText(design);
                            String coName = (String) objects.get(3);
                            commonName.setText(coName);
                            String dia = (String) objects.get(4);
                            diameter.setText(dia);
                            String len = (String) objects.get(5);
                            length.setText(len);
                            String wei = (String) objects.get(6);
                            weight.setText(wei);
                            String caseType = (String) objects.get(7);
                            designation.setText(design);
                            box.setSelectedItem(caseType);

                            //	objects.get()


                        });

                JButton loadOut = new JButton("    导出    ");
                controlPanel.add(loadOut, "gapleft 10");

                JButton jButton = new JButton("提交");
                controlPanel.add(jButton);
                jButton.addActionListener(e1 -> {
                    try {
                        // 获取 x 和 y 的输入并解析
                        String[] xValues = xField.getText().split(",");
                        String[] yValues = yField.getText().split(",");
                        if ((xValues.length == 1 || yValues.length == 1) && (xValues[0].equals("") || yValues[0].equals(""))) {
                            throw new IllegalArgumentException("X轴或Y轴不能为空");
                        }
                        if (xValues.length != yValues.length) {
                            throw new IllegalArgumentException("X 和 Y 的值数量必须相等");
                        }
                        if (designation.getText().length() == 0) {
                            throw new IllegalArgumentException("请留下你的姓名");
                        }
                        if (commonName.getText().length() == 0) {
                            throw new IllegalArgumentException("请留下你的作品名称");
                        }
                        if (diameter.getText().length() == 0) {
                            throw new IllegalArgumentException("直径不能为空");
                        }
                        if (length.getText().length() == 0) {
                            throw new IllegalArgumentException("长度不能为空");
                        }
                        if (weight.getText().length() == 0) {
                            throw new IllegalArgumentException("发动机初始重量不能为空");
                        }


                        //转double
                        double[] timePoint = new double[xValues.length];
                        double[] thrustPoint = new double[yValues.length];
                        for (int i = 0; i < xValues.length; i++) {
                            timePoint[i] = Double.parseDouble(xValues[i]);
                            thrustPoint[i] = Double.parseDouble(yValues[i]);

                        }

                        //设计者
                        String design = designation.getText();

                        //火箭类型
                        String selectedItem = (String) box.getSelectedItem();
                        Motor.Type motorType = null;
                        if (selectedItem.equals("Single-use")) {
                            motorType = Motor.Type.SINGLE;
                        } else if (selectedItem.equals("Reloadable")) {
                            motorType = Motor.Type.RELOAD;
                        } else if (selectedItem.equals("Hybrid")) {
                            motorType = Motor.Type.HYBRID;
                        } else {
                            motorType = Motor.Type.UNKNOWN;
                        }


                        ThrustCurveMotor motor = new ThrustCurveMotor.Builder()
                                .setManufacturer(Manufacturer.getManufacturer(design))
                                .setDesignation(design)
                                .setDescription(" ")
                                .setCaseInfo(" ")
                                .setMotorType(motorType)
                                .setStandardDelays(new double[]{})
                                .setInitialMass(Double.parseDouble(weight.getText()))
                                .setDiameter(Double.parseDouble(diameter.getText()))
                                .setLength(Double.parseDouble(length.getText()))
                                .setTimePoints(timePoint)
                                .setThrustPoints(thrustPoint)
                                .setCGPoints(new Coordinate[]{
                                        new Coordinate(.035, 0, 0, 0.025), new Coordinate(.035, 0, 0, 0.025)})
                                .setDigest(design + "test")
                                .build();
                        Method method = ThrustCurveMotor.class.getDeclaredMethod("computeStatistics");
                        method.setAccessible(true);
                        method.invoke(motor);

                        //calculate weight
                        double I_sp = motor.getTotalImpulseEstimate() / (motor.getBurnTimeEstimate() * motor.getInitialMass());

                        double[] mass = new double[motor.getTimePoints().length];
                        mass[0] = motor.getInitialMass();

                        for (int i = 1; i < mass.length; i++) {
                            double deltaT = motor.getTimePoints()[i] - motor.getTimePoints()[i - 1];
                            double massFlowRate = motor.getThrustPoints()[i - 1] / (I_sp * 9.80665);
                            double massLoss = massFlowRate * deltaT;
                            mass[i] = mass[i - 1] - massLoss;
                        }


                        Coordinate[] cgs = new Coordinate[xValues.length];
                        for (int i = 0; i < cgs.length; i++) {
                            Coordinate coordinate = new Coordinate(Double.parseDouble(length.getText()) / 2, 0, 0, mass[i]);
                            cgs[i] = coordinate;
                        }

                        Field field = ThrustCurveMotor.class.getDeclaredField("cg");
                        field.setAccessible(true);
                        field.set(motor, cgs);

                        JOptionPane.showMessageDialog(null, "发动机创建成功");
                        ThrustCurveMotorSetDatabase database1 = Application.getThrustCurveMotorSetDatabase();
                        database1.addMotor(motor);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(chartDialog, "请输入有效的数字");
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(chartDialog, ex.getMessage());
                    } catch (InvocationTargetException | NoSuchFieldException | IllegalAccessException |
                             NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    }

                });


                loadOut.addActionListener(e2 -> doExport(chartDialog, xField.getText().split(","), yField.getText().split(","),
                        designation.getText(), commonName.getText(),
                        diameter.getText(), length.getText(), weight.getText(), (String) box.getSelectedItem()));

                //添加接口................
                updateButton.addActionListener(e1 -> {
                    //调用接口
                    OpenRocket.eduCoderService.calculateFunction(200).enqueue(new Callback<DataResult>() {
                        @Override
                        public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                            try {
                                DataResult result = response.body();
                                //解析函数.......
                                String xx = result.getResult().get(0).stream().
                                        map(String::valueOf).collect(Collectors.joining(","));
                                String yy = result.getResult().get(1).stream().
                                        map(String::valueOf).collect(Collectors.joining(","));
                                String kg = result.getResult().get(2).get(0).toString();
                                String d = result.getResult().get(3).get(0).toString();
                                String len = result.getResult().get(4).get(0).toString();
                                String design = result.getResult().get(5).get(0).toString();
                                String name = result.getResult().get(6).get(0).toString();
                                xField.setText(xx);
                                yField.setText(yy);
                                weight.setText(kg);
                                length.setText(len);
                                diameter.setText(d);
                                designation.setText(design);
                                commonName.setText(name);

                                // 获取 x 和 y 的输入并解析
                                String[] xValues = xField.getText().split(",");
                                String[] yValues = yField.getText().split(",");
                                if (xValues.length != yValues.length) {
                                    throw new IllegalArgumentException("X 和 Y 的值数量必须相等");
                                }
                                xySeries.clear();
                                // 批量添加数据点
                                for (int i = 0; i < xValues.length; i++) {
                                    double x = Double.parseDouble(xValues[i].trim());
                                    double y = Double.parseDouble(yValues[i].trim());
                                    xySeries.add(x, y);
                                }

                            } catch (IllegalStateException e) {
                                JOptionPane.showMessageDialog(chartDialog, "参数不合法");
                            } catch (IllegalArgumentException e) {
                                JOptionPane.showMessageDialog(chartDialog, e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<DataResult> call, @NotNull Throwable throwable) {
                            SwingUtilities.invokeLater(() ->
                                    JOptionPane.showMessageDialog(chartDialog, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                        }
                    });
                    //


                });

                // 将 chartPanel 添加到对话框的中心
                chartDialog.add(chartPanel, BorderLayout.CENTER);
                // 将控制面板（输入框和按钮）添加到对话框的底部
                chartDialog.add(controlPanel, BorderLayout.SOUTH);

                // 显示对话框
                chartDialog.setVisible(true);
            });
        }


        this.add(panel, "grow");

        // Vertical split
        this.add(new JSeparator(JSeparator.VERTICAL), "growy, gap para para");

        JTabbedPane rightSide = new JTabbedPane();
        rightSide.add(trans.get("TCMotorSelPan.btn.filter"), motorFilterPanel);
        rightSide.add(trans.get("TCMotorSelPan.btn.details"), motorInformationPanel);

        this.add(rightSide, "growy");

        // Update the panel data
        updateData();
        setDelays(false);
        hideUnavailableBox.getActionListeners()[0].actionPerformed(null);
        hideSimilarBox.getActionListeners()[0].actionPerformed(null);

    }

    //导出文件
    //x y序列
    //
    private void doExport(JDialog chartDialog, Object[] xValues, Object[] yValues,
                          String designation, String commonName,
                          String diameter, String length, String weight, String type) {
        if (xValues.length == 0 || xValues == null) {
            JOptionPane.showMessageDialog(chartDialog, "数据点为空,请先生成推力曲线");
            return;
        }
        if ((xValues.length == 1 || yValues.length == 1) && (xValues[0].equals("") || yValues[0].equals(""))) {
            JOptionPane.showMessageDialog(chartDialog, "X轴或Y轴不能为空");
            return;
        }
        if (xValues.length != yValues.length) {
            JOptionPane.showMessageDialog(chartDialog, "X 和 Y 的值数量必须相等");
            return;
        }
        JFileChooser fileChooser = new JFileChooser("/data/workspace/downloadfiles");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file != null) {
                file = new File(file.getAbsolutePath() + ".txt");
                file.setReadOnly();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("xValues:\n");
                    for (Object x : xValues) {
                        writer.write(x + ",");
                    }
                    writer.write("\n");
                    writer.write("yValues:\n");
                    for (Object y : yValues) {
                        writer.write(y + ",");
                    }
                    writer.write("\n");

                    writer.write("designation:\n");
                    writer.write(designation + "\n");
                    writer.write("commonName:\n");
                    writer.write(commonName + "\n");
                    writer.write("diameter:\n");
                    writer.write(diameter + "\n");
                    writer.write("length:\n");
                    writer.write(length + "\n");
                    writer.write("weight:\n");
                    writer.write(weight + "\n");
                    writer.write("type:\n");
                    writer.write(type);

                    JOptionPane.showMessageDialog(chartDialog, "数据导出成功");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(chartDialog, "导出失败");

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(chartDialog, ex.getMessage());
                }
            }
        }
    }


    private List<Object> doImport(JDialog chartDialog) {
        List<Object> returnList = new ArrayList<>();
        List<String> xList = new ArrayList<>();
        List<String> yList = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser("/data/workspace/downloadfiles");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showSaveDialog(null);
        try {
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //判断文件类型
                int i = file.getName().lastIndexOf('.');
                String subString = file.getName().substring(i);
                if (!subString.equals(".txt")) {
                    throw new IllegalArgumentException("无法识别的文件，请重新导入");
                }
                //存储文本
                StringBuilder content = new StringBuilder();
                //解析txt
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //处理文本
                String contentString = content.toString();
                if (!contentString.contains("xValues") || !contentString.contains("yValues") ||
                        !contentString.contains("designation") || !contentString.contains("commonName") ||
                        !contentString.contains("diameter") || !contentString.contains("length") ||
                        !contentString.contains("weight") || !contentString.contains("type")) {
                    throw new IllegalArgumentException("文件导入失败，请检查txt文件");

                }
                if (contentString.contains("xValues:")) {
                    int beginIndex = contentString.lastIndexOf("xValues:");
                    int endIndex = contentString.lastIndexOf("yValues");
                    String functions = contentString.substring(beginIndex, endIndex);
                    functions = functions.substring(functions.indexOf('\n'), functions.length() - 1);
                    functions = functions.trim();
                    String[] strings = functions.split(",");
                    xList = Arrays.stream(strings).toList();
                }
                if (contentString.contains("yValues:")) {
                    int beginIndex = contentString.lastIndexOf("yValues");
                    int endIndex = contentString.lastIndexOf("designation:");
                    String functions = contentString.substring(beginIndex, endIndex);
                    functions = functions.trim();
                    functions = functions.substring(functions.indexOf(':') + 2, functions.length() - 1);

                    String[] strings = functions.split(",");
                    yList = Arrays.stream(strings).toList();
                }
                int designationBegin = contentString.lastIndexOf("designation:");
                int designationEnd = contentString.lastIndexOf("commonName:");
                int commonNameBegin = contentString.lastIndexOf("commonName:");
                int commonNameEnd = contentString.lastIndexOf("diameter:");
                int diameterBegin = contentString.lastIndexOf("diameter:");
                int diameterEnd = contentString.lastIndexOf("length:");
                int lengthBegin = contentString.lastIndexOf("length:");
                int lengthEnd = contentString.lastIndexOf("weight:");
                int weightBegin = contentString.lastIndexOf("weight:");
                int weightEnd = contentString.lastIndexOf("type:");
                int typeStart = contentString.lastIndexOf("type:");
                //     int typeEnd = contentString.lastIndexOf("commonName:");
                //designation:
                String designation = contentString.substring(designationBegin, designationEnd);
                designation = designation.trim();


                if ((designation.indexOf(":") + 2) > designation.length()) {
                    designation = "";
                } else {
                    designation = designation.substring(designation.indexOf(":") + 2);
                }

                String commonName = contentString.substring(commonNameBegin, commonNameEnd);
                commonName = commonName.trim();

                if ((commonName.indexOf(":") + 2) > commonName.length()) {
                    commonName = "";
                } else {
                    commonName = commonName.substring(commonName.indexOf(":") + 2);

                }

                String diameter = contentString.substring(diameterBegin, diameterEnd);
                diameter = diameter.trim();

                if ((diameter.indexOf(":") + 2) > diameter.length()) {
                    diameter = "";
                } else {
                    diameter = diameter.substring(diameter.indexOf(":") + 2);
                }


                String length = contentString.substring(lengthBegin, lengthEnd);
                length = length.trim();

                if ((length.indexOf(":") + 2) > length.length()) {
                    length = "";
                } else {
                    length = length.substring(length.indexOf(":") + 2);
                }

                String weight = contentString.substring(weightBegin, weightEnd);
                weight = weight.trim();

                if ((weight.indexOf(":") + 2) > weight.length()) {
                    weight = "";
                } else {
                    weight = weight.substring(weight.indexOf(":") + 2);
                }

                String type = contentString.substring(typeStart);
                type = type.trim();
                if ((type.indexOf(":") + 2) > type.length()) {
                    type = "";
                } else {
                    type = type.substring(type.indexOf(":") + 2);
                }

                returnList.add(xList);
                returnList.add(yList);
                returnList.add(designation);
                returnList.add(commonName);
                returnList.add(diameter);
                returnList.add(length);
                returnList.add(weight);
                returnList.add(type);

                return returnList;
            } else {
                return null;
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(chartDialog, ex.getMessage());

        }

        return returnList;

    }

    private static void initColors() {
        updateColors();
        UITheme.Theme.addUIThemeChangeListener(ThrustCurveMotorSelectionPanel::updateColors);
    }

    private static void updateColors() {
        dimTextColor = GUIUtil.getUITheme().getDimTextColor();
    }

    public void setMotorMountAndConfig(final FlightConfigurationId _fcid, MotorMount mountToEdit) {
        if (null == _fcid) {
            throw new NullPointerException(" attempted to set mount with a null FCID. bug.  ");
        } else if (null == mountToEdit) {
            throw new NullPointerException(" attempted to set mount with a null mount. bug. ");
        }
        motorFilterPanel.setMotorMount(mountToEdit);

        MotorConfiguration curMotorInstance = mountToEdit.getMotorConfig(_fcid);
        selectedMotor = null;
        selectedMotorSet = null;
        selectedDelay = 0;
        ThrustCurveMotor motorToSelect = null;
        if (curMotorInstance.hasMotor()) {
            motorToSelect = (ThrustCurveMotor) curMotorInstance.getMotor();
            selectedDelay = curMotorInstance.getEjectionDelay();
        }

        // If current motor is not found in db, add a new ThrustCurveMotorSet containing it
        if (motorToSelect != null) {
            ThrustCurveMotorSet motorSetToSelect = null;
            motorSetToSelect = findMotorSet(motorToSelect);
            if (motorSetToSelect == null) {
                database = new ArrayList<ThrustCurveMotorSet>(database);
                ThrustCurveMotorSet extra = new ThrustCurveMotorSet();
                extra.addMotor(motorToSelect);
                database.add(extra);
                Collections.sort(database);
            }

            select(motorToSelect);

        }
        motorFilterPanel.setMotorMount(mountToEdit);
        scrollSelectionVisible();
    }

    @Override
    public Motor getSelectedMotor() {
        return selectedMotor;
    }


    @Override
    public double getSelectedDelay() {
        return selectedDelay;
    }


    @Override
    public JComponent getDefaultFocus() {
        return searchField;
    }

    @Override
    public void selectedMotor(Motor motorSelection) {
        if (!(motorSelection instanceof ThrustCurveMotor)) {
            log.error("Received argument that was not ThrustCurveMotor: " + motorSelection);
            return;
        }

        ThrustCurveMotor motor = (ThrustCurveMotor) motorSelection;
        ThrustCurveMotorSet set = findMotorSet(motor);
        if (set == null) {
            log.error("Could not find set for motor:" + motorSelection);
            return;
        }

        // Store selected motor in preferences node, set all others to false
        Preferences prefs = ((SwingPreferences) Application.getPreferences()).getNode(net.sf.openrocket.startup.Preferences.PREFERRED_THRUST_CURVE_MOTOR_NODE);
        for (ThrustCurveMotor m : set.getMotors()) {
            String digest = m.getDigest();
            prefs.putBoolean(digest, m == motor);
        }
    }

    public void setCloseableDialog(CloseableDialog dialog) {
        this.dialog = dialog;
    }


    /**
     * Called when a different motor is selected from within the panel.
     */
    private void select(ThrustCurveMotor motor) {
        if (selectedMotor == motor || motor == null)
            return;

        ThrustCurveMotorSet set = findMotorSet(motor);
        if (set == null) {
            throw new BugException("Could not find motor from database, motor=" + motor);
        }

        boolean updateDelays = (selectedMotorSet != set);

        selectedMotor = motor;
        selectedMotorSet = set;
        updateData();
        if (updateDelays) {
            setDelays(true);
        }
        scrollSelectionVisible();
    }


    private void updateData() {

        if (selectedMotorSet == null) {
            // No motor selected
            curveSelectionModel.removeAllElements();
            curveSelectionBox.setEnabled(false);
            curveSelectionLabel.setEnabled(false);
            ejectionChargeDelayLabel.setEnabled(false);
            delayBox.setEnabled(false);
            motorInformationPanel.clearData();
            table.clearSelection();
            return;
        }

        ejectionChargeDelayLabel.setEnabled(true);
        delayBox.setEnabled(true);

        // Check which thrust curves to display
        List<ThrustCurveMotor> motors = getFilteredCurves();
        final int index = motors.indexOf(selectedMotor);

        // Update the thrust curve selection box
        curveSelectionModel.removeAllElements();
        for (int i = 0; i < motors.size(); i++) {
            curveSelectionModel.addElement(new MotorHolder(motors.get(i), i));
        }
        curveSelectionBox.setSelectedIndex(index);

        if (motors.size() > 1) {
            curveSelectionBox.setEnabled(true);
            curveSelectionLabel.setEnabled(true);
        } else {
            curveSelectionBox.setEnabled(false);
            curveSelectionLabel.setEnabled(false);
        }

        motorInformationPanel.updateData(motors, selectedMotor);

    }

    List<ThrustCurveMotor> getFilteredCurves() {
        List<ThrustCurveMotor> motors = selectedMotorSet.getMotors();
        if (hideSimilarBox.isSelected() && selectedMotor != null) {
            List<ThrustCurveMotor> filtered = new ArrayList<ThrustCurveMotor>(motors.size());
            for (int i = 0; i < motors.size(); i++) {
                ThrustCurveMotor m = motors.get(i);
                if (m.equals(selectedMotor)) {
                    filtered.add(m);
                    continue;
                }
                double similarity = MotorCorrelation.similarity(selectedMotor, m);
                log.debug("Motor similarity: " + similarity);
                if (similarity < MOTOR_SIMILARITY_THRESHOLD) {
                    filtered.add(m);
                }
            }
            motors = filtered;
        }

        Collections.sort(motors, MOTOR_COMPARATOR);

        return motors;
    }

    private void updateNrOfMotors() {
        if (table != null && nrOfMotorsLabel != null) {
            int rowCount = table.getRowCount();
            String motorCount = trans.get("TCMotorSelPan.lbl.nrOfMotors.None");
            if (rowCount > 0) {
                motorCount = String.valueOf(rowCount);
            }
            nrOfMotorsLabel.setText(trans.get("TCMotorSelPan.lbl.nrOfMotors") + " " + motorCount);
        }
    }


    private void scrollSelectionVisible() {
        if (selectedMotorSet != null) {
            int index = table.convertRowIndexToView(model.getIndex(selectedMotorSet));
            table.getSelectionModel().setSelectionInterval(index, index);
            Rectangle rect = table.getCellRect(index, 0, true);
            rect = new Rectangle(rect.x, rect.y - 100, rect.width, rect.height + 200);
            table.scrollRectToVisible(rect);
        }
    }


    public static Color getColor(int index) {
        Color color = Util.getPlotColor(index);
        if (UITheme.isLightTheme(GUIUtil.getUITheme())) {
            return color;
        } else {
            return color.brighter().brighter();
        }
    }


    /**
     * Find the ThrustCurveMotorSet that contains a motor.
     *
     * @param motor the motor to look for.
     * @return the ThrustCurveMotorSet, or null if not found.
     */
    private ThrustCurveMotorSet findMotorSet(ThrustCurveMotor motor) {
        for (ThrustCurveMotorSet set : database) {
            if (set.getMotors().contains(motor)) {
                return set;
            }
        }

        return null;
    }


    /**
     * Select the default motor from this ThrustCurveMotorSet.  This uses primarily motors
     * that the user has previously used, and secondarily a heuristic method of selecting which
     * thrust curve seems to be better or more reliable.
     *
     * @param set the motor set
     * @return the default motor in this set
     */
    private ThrustCurveMotor selectMotor(ThrustCurveMotorSet set) {
        if (set.getMotorCount() == 0) {
            throw new BugException("Attempting to select motor from empty ThrustCurveMotorSet: " + set);
        }
        if (set.getMotorCount() == 1) {
            return set.getMotors().get(0);
        }


        // Find which motor has been used the most recently
        List<ThrustCurveMotor> list = set.getMotors();
        Preferences prefs = ((SwingPreferences) Application.getPreferences()).getNode(net.sf.openrocket.startup.Preferences.PREFERRED_THRUST_CURVE_MOTOR_NODE);
        for (ThrustCurveMotor m : list) {
            String digest = m.getDigest();
            if (prefs.getBoolean(digest, false)) {
                return m;
            }
        }

        // No motor has been used
        Collections.sort(list, MOTOR_COMPARATOR);
        return list.get(0);
    }

    /**
     * Selects a new motor based on the selection in the motor table
     */
    public void selectMotorFromTable() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            row = table.convertRowIndexToModel(row);
            ThrustCurveMotorSet motorSet = model.getMotorSet(row);
            log.info(Markers.USER_MARKER, "Selected table row " + row + ": " + motorSet);
            if (motorSet != selectedMotorSet) {
                select(selectMotor(motorSet));
            }
        } else {
            log.info(Markers.USER_MARKER, "Selected table row " + row + ", nothing selected");
        }
    }


    /**
     * Set the values in the delay combo box.  If <code>reset</code> is <code>true</code>
     * then sets the selected value as the value closest to selectedDelay, otherwise
     * leaves selection alone.
     */
    private void setDelays(boolean reset) {
        if (selectedMotor == null) {
            //// Display nothing
            delayBox.setModel(new DefaultComboBoxModel<>(new String[]{}));
        } else {
            List<Double> delays = selectedMotorSet.getDelays();
            boolean containsPlugged = delays.contains(Motor.PLUGGED_DELAY);
            int size = delays.size() + (containsPlugged ? 0 : 1);
            String[] delayStrings = new String[size];
            double currentDelay = selectedDelay; // Store current setting locally

            for (int i = 0; i < delays.size(); i++) {
                //// Plugged
                delayStrings[i] = ThrustCurveMotor.getDelayString(delays.get(i), trans.get("TCMotorSelPan.delayBox.Plugged"));
            }
            // We always want the plugged option in the combobox, even if the motor doesn't have it
            if (!containsPlugged) {
                delayStrings[delayStrings.length - 1] = trans.get("TCMotorSelPan.delayBox.Plugged");
            }
            delayBox.setModel(new DefaultComboBoxModel<String>(delayStrings));

            if (reset) {
                // Find and set the closest value
                double closest = Double.NaN;
                for (Double delay : delays) {
                    // if-condition to always become true for NaN
                    if (!(Math.abs(delay - currentDelay) > Math.abs(closest - currentDelay))) {
                        closest = delay;
                    }
                }
                if (!Double.isNaN(closest)) {
                    selectedDelay = closest;
                    delayBox.setSelectedItem(ThrustCurveMotor.getDelayString(closest, trans.get("TCMotorSelPan.delayBox.Plugged")));
                } else {
                    //// Plugged
                    delayBox.setSelectedItem(trans.get("TCMotorSelPan.delayBox.Plugged"));
                }

            } else {
                selectedDelay = currentDelay;
                delayBox.setSelectedItem(ThrustCurveMotor.getDelayString(currentDelay, trans.get("TCMotorSelPan.delayBox.Plugged")));
            }

        }
    }

    //////////////////////


    private class CurveSelectionRenderer implements ListCellRenderer<MotorHolder> {

        private final ListCellRenderer<MotorHolder> renderer;

        public CurveSelectionRenderer(ListCellRenderer<MotorHolder> renderer) {
            this.renderer = renderer;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends MotorHolder> list, MotorHolder value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null) {
                Color color = getColor(value.getIndex());
                if (isSelected || cellHasFocus) {
                    label.setBackground(color);
                    label.setOpaque(true);
                    Color fg = list.getBackground();
                    fg = new Color(fg.getRed(), fg.getGreen(), fg.getBlue());        // List background changes for some reason, so clone the color
                    label.setForeground(fg);
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(color);
                }
            }

            return label;
        }
    }
}

