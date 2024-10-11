package net.sf.openrocket.startup;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;

import net.miginfocom.layout.LayoutUtil;
import net.sf.openrocket.arch.SystemInfo;
import net.sf.openrocket.arch.SystemInfo.Platform;
import net.sf.openrocket.communication.UpdateInfo;
import net.sf.openrocket.communication.UpdateInfoRetriever;
import net.sf.openrocket.communication.UpdateInfoRetriever.ReleaseStatus;
import net.sf.openrocket.communication.WelcomeInfoRetriever;
import net.sf.openrocket.database.Databases;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.document.OpenRocketDocumentFactory;
import net.sf.openrocket.document.StorageOptions;
import net.sf.openrocket.file.GeneralRocketSaver;
import net.sf.openrocket.file.openrocket.OpenRocketSaver;
import net.sf.openrocket.file.rasaero.RASAeroCommonConstants;
import net.sf.openrocket.gui.dialogs.SwingWorkerDialog;
import net.sf.openrocket.gui.dialogs.UpdateInfoDialog;
import net.sf.openrocket.gui.dialogs.WelcomeDialog;
import net.sf.openrocket.gui.main.BasicFrame;
import net.sf.openrocket.gui.main.Splash;
import net.sf.openrocket.gui.main.SwingExceptionHandler;
import net.sf.openrocket.gui.util.*;
import net.sf.openrocket.logging.LoggingSystemSetup;
import net.sf.openrocket.logging.PrintStreamToSLF4J;
import net.sf.openrocket.plugin.PluginModule;
import net.sf.openrocket.util.BuildProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * 启动 OpenRocket swing 应用程序
 * @author Corybyte <rzd200213@gmail.com>
 */
public class SwingStartup {
	
	private final static Logger log = LoggerFactory.getLogger(SwingStartup.class);
	private static BasicFrame start=null;
	
	/**
	 * OpenRocket 启动主方法。
	 */
	public static void main(final String[] args) throws Exception {

		//在执行其他任何操作之前检查“openrocket.debug”属性
		checkDebugStatus();



		if (System.getProperty("openrocket.debug.layout") != null) {
			//是否全局调试 millis>0 ? true or false
			LayoutUtil.setGlobalDebugMillis(100);
		}

		initializeLogging();
		log.info("Starting up OpenRocket version {}", BuildProperties.getVersion());

		// 检查 JRE 版本
		boolean ignoreJRE = System.getProperty("openrocket.ignore-jre") != null;
		if (!ignoreJRE && !checkJREVersion()) {
			return;
		}

		//测试该环境是否支持显示器、键盘和鼠标。
		log.info("Checking for graphics head");
		checkHead();

		//如果在 MAC 上运行，请设置 OSX UI Elements。
		if (SystemInfo.getPlatform() == Platform.MAC_OS) {
			OSXSetup.setupOSX();
		}

		final SwingStartup runner = new SwingStartup();

		//在 EDT 中运行实际的启动方法，因为它可以使用进度对话框等。
		log.info("Moving startup to EDT");
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				runner.runInEDT(args);
			}
		});
		
		log.info("Startup complete");
		//监听是否有参数传入
		if (args.length>0){
			System.out.println(args[0]);
		}
		watchDirectory("/tmp");
	}
	public static void watchDirectory(String directoryPath) {
		GeneralRocketSaver ROCKET_SAVER = new GeneralRocketSaver();
		try {
			// 获取文件系统的 WatchService
			WatchService watchService = FileSystems.getDefault().newWatchService();
			// 将目录注册到 WatchService
			Path path = Paths.get(directoryPath);
			path.register(watchService, ENTRY_CREATE);

			System.out.println("监听目录: " + directoryPath);

			// 无限循环监听事件
			while (true) {
				// 等待监听事件
				WatchKey key = watchService.take();

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					// 检查是否是文件创建事件
					if (kind == ENTRY_CREATE) {
						Path createdFilePath = ((WatchEvent<Path>) event).context();
						System.out.println("文件创建");
						// 如果检测到 trigger.save 文件，则执行保存操作
						if (createdFilePath.toString().equals("strugger.save")) {
							OpenRocketDocument document = OpenRocketDocumentFactory.mydoc;
							File file = new File("/data/workspace/downloadfiles/1.ork");
							file = FileHelper.forceExtension(file,"ork");
							document.getDefaultStorageOptions().setFileType(StorageOptions.FileType.OPENROCKET);
							SaveFileWorker worker = new SaveFileWorker(document, file, ROCKET_SAVER);
							SwingWorkerDialog.runWorker(null, "Saving file",
									"Writing " + file.getName() + "...", worker);
							worker.get();
							document.setFile(file);
							document.setSaved(true);


							Files.delete(path.resolve(createdFilePath));  // 保存完成后删除触发文件
						}
					}
				}

				// 重置 WatchKey 并继续监听
				if (!key.reset()) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks whether the Java Runtime Engine version is supported.
	 *
	 * @return true if the JRE is supported, false if not
	 */
	private static boolean checkJREVersion() {
		String JREVersion = System.getProperty("java.version");
		if (JREVersion != null) {
			try {
				// We're only interested in the big decimal part of the JRE version
				int version = Integer.parseInt(JREVersion.split("\\.")[0]);
				if (IntStream.of(Application.SUPPORTED_JRE_VERSIONS).noneMatch(c -> c == version)) {
					String title = "Unsupported Java version";
					String message1 = "Unsupported Java version: %s";
					String message2 = "Supported version(s): %s";
					String message3 = "Please change the Java Runtime Environment version or install OpenRocket using a packaged installer.";

					StringBuilder message = new StringBuilder();
					message.append(String.format(message1, JREVersion));
					message.append("\n");
					String[] supported = Arrays.stream(Application.SUPPORTED_JRE_VERSIONS)
							.mapToObj(String::valueOf)
							.toArray(String[]::new);
					message.append(String.format(message2, String.join(", ", supported)));
					message.append("\n\n");
					message.append(message3);

					JOptionPane.showMessageDialog(null, message.toString(),
							title, JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} catch (NumberFormatException e) {
				log.warn("Malformed JRE version - " + JREVersion);
			}
		}
		return true;
	}

	/**
	 * 如果定义了 openrocket.debug，请设置正确的系统属性。
	 */
	private static void checkDebugStatus() {
		if (System.getProperty("openrocket.debug") != null) {
			setPropertyIfNotSet("openrocket.debug.menu", "true");
			setPropertyIfNotSet("openrocket.debug.mutexlocation", "true");
			setPropertyIfNotSet("openrocket.debug.motordigest", "true");
			setPropertyIfNotSet("jogl.debug", "all");
		}
	}
	
	private static void setPropertyIfNotSet(String key, String value) {
		if (System.getProperty(key) == null) {
			System.setProperty(key, value);
		}
	}
	
	/**
	 * 初始化日志记录系统
	 */
	public static void initializeLogging() {

		LoggingSystemSetup.setupLoggingAppender();

		if (System.getProperty("openrocket.debug") != null) {
			LoggingSystemSetup.addConsoleAppender();
		}
		//Replace System.err with a PrintStream that logs lines to DEBUG, or VBOSE if they are indented.
		//If debug info is not being output to the console then the data is both logged and written to
		//stderr.
		final PrintStream stdErr = System.err;
		System.setErr(PrintStreamToSLF4J.getPrintStream("STDERR", stdErr));
	}
	
	/**
	 *
	 * 启动 OpenRocket 时在 EDT 中运行。
	 * @param args	command line arguments
	 */
	private void runInEDT(String[] args) {

		//使用版本信息初始化初始化初始屏幕
		log.info("使用版本信息初始化初始化初始屏幕");
		Splash.init();
		
		// 设置未捕获的异常处理程序
		log.info("设置未捕获的异常处理程序");
		SwingExceptionHandler exceptionHandler = new SwingExceptionHandler();
		Application.setExceptionHandler(exceptionHandler);
		exceptionHandler.registerExceptionHandler();
		
		// Load motors etc.
		log.info("Loading databases");
		GuiModule guiModule = new GuiModule();
		Module pluginModule = new PluginModule();
		Injector injector = Guice.createInjector(guiModule, pluginModule);
		Application.setInjector(injector);
		guiModule.startLoader();
		
		//开始获取更新信息
		final UpdateInfoRetriever updateRetriever = startUpdateChecker();


		// 设置外观
		log.info("Setting LAF");
		String cmdLAF = System.getProperty("openrocket.laf");
		if (cmdLAF != null) {
			log.info("Setting cmd line LAF '{}'", cmdLAF);
			Preferences prefs = Application.getPreferences();
			prefs.setUITheme(UITheme.Themes.valueOf(cmdLAF));
		}
		GUIUtil.applyLAF();

		// Set tooltip delay time.  Tooltips are used in MotorChooserDialog extensively.
		//ToolTipManager.sharedInstance().setDismissDelay(30000);
		
		// Load defaults
		((SwingPreferences) Application.getPreferences()).loadDefaultUnits();
		((SwingPreferences) Application.getPreferences()).loadDefaultComponentMaterials();
		
		Databases.fakeMethod();

		// Set up the OSX file open handler here so that it can handle files that are opened when OR is not yet running.
		if (SystemInfo.getPlatform() == Platform.MAC_OS) {
			OSXSetup.setupOSXOpenFileHandler();
		}
		
		// Starting action (load files or open new document)
		log.info("打开应用程序主窗口");
		if (!handleCommandLine(args)) {
			BasicFrame startupFrame = BasicFrame.reopen();
			start = startupFrame;
			BasicFrame.setStartupFrame(startupFrame);
			showWelcomeDialog();
		}
		
		// Check whether update info has been fetched or whether it needs more time
		log.info("Checking update status");
		checkUpdateStatus(updateRetriever);
		
	}
	
	/**
	 * 测试该环境是否支持显示器、键盘和鼠标。
	 */
	private static void checkHead() {
		
		if (GraphicsEnvironment.isHeadless()) {
			log.error("Application is headless.");
			System.err.println();
			System.err.println("OpenRocket cannot currently be run without the graphical " +
					"user interface.");
			System.err.println();
			System.exit(1);
		}
		
	}

	public static UpdateInfoRetriever startUpdateChecker() {
		final UpdateInfoRetriever updateRetriever;
		if (Application.getPreferences().getCheckUpdates()) {
			log.info("Starting update check");
			updateRetriever = new UpdateInfoRetriever();
			updateRetriever.startFetchUpdateInfo();
		} else {
			log.info("Update check disabled");
			updateRetriever = null;
		}
		return updateRetriever;
	}
	
	public static void checkUpdateStatus(final UpdateInfoRetriever updateRetriever) {
		if (updateRetriever == null)
			return;
		
		int delay = 1000;
		if (!updateRetriever.isRunning())
			delay = 100;
		
		final Timer timer = new Timer(delay, null);
		
		ActionListener listener = new ActionListener() {
			private int count = 5;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updateRetriever.isRunning()) {
					timer.stop();

					final SwingPreferences preferences = (SwingPreferences) Application.getPreferences();
					UpdateInfo info = updateRetriever.getUpdateInfo();

					// Only display something when an update is found
					if (info != null && info.getException() == null && info.getReleaseStatus() == ReleaseStatus.OLDER &&
						!preferences.getIgnoreUpdateVersions().contains(info.getLatestRelease().getReleaseName())) {
						UpdateInfoDialog infoDialog = new UpdateInfoDialog(info);
						infoDialog.setVisible(true);
					}
				}
				count--;
				if (count <= 0)
					timer.stop();
			}
		};
		timer.addActionListener(listener);
		timer.start();
	}

	/**
	 * 显示一个欢迎对话框，显示此内部版本的发行说明。
	 * */
	public static void showWelcomeDialog() {
		// 如果忽略此内部版本，则不显示
		if (Application.getPreferences().getIgnoreWelcome(BuildProperties.getVersion())) {
			log.debug("Welcome dialog ignored");
			return;
		}

		// Fetch this version's release notes
		String releaseNotes;
		try {
			releaseNotes = WelcomeInfoRetriever.retrieveWelcomeInfo();
		} catch (IOException e) {
			log.error("Error retrieving welcome info", e);
			return;
		}
		if (releaseNotes == null) {
			log.debug("No release notes found");
			return;
		}

		// Show the dialog
		WelcomeDialog dialog = new WelcomeDialog(releaseNotes);
		dialog.setVisible(true);
	}
	
	/**
	 * Handles arguments passed from the command line.  This may be used either
	 * when starting the first instance of OpenRocket or later when OpenRocket is
	 * executed again while running.
	 *
	 * @param args	the command-line arguments.
	 * @return		whether a new frame was opened or similar user desired action was
	 * 				performed as a result.
	 */
	private boolean handleCommandLine(String[] args) {
		
		// Check command-line for files
		boolean opened = false;
		for (String file : args) {
			if (BasicFrame.open(new File(file), null) != null) {
				opened = true;
			}
		}
		return opened;
	}
	
}
