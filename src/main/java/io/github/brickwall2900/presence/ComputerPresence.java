/*
 * Copyright 2026 brickwall2900
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.brickwall2900.presence;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.formdev.flatlaf.FlatLightLaf;
import de.jcm.discordgamesdk.ActivityManager;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.*;
import io.github.brickwall2900.presence.lua.DiscordPresenceObject;
import io.github.brickwall2900.presence.lua.LuaBridge;
import io.github.brickwall2900.presence.platform.Platform;
import io.github.brickwall2900.presence.platform.PlatformProvider;
import io.github.brickwall2900.presence.systray.*;
import io.github.brickwall2900.presence.systray.TrayIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.ffm.SystemInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ComputerPresence {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerPresence.class);
    private static final String APP_ID = "game.Players.brickwall2900.PlayerScripts.ComputerPresence_0x0001";
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(ComputerPresence.class.getName());
    private static final String TITLE = BUNDLE.getString("app.name");

    private static final String VERSION;

    private static final int MINIMUM_TEXT_LENGTH = 3;
    private static final int MAXIMUM_TEXT_LENGTH = 127;

    static {
        String readVersion;
        try (InputStream stream = ComputerPresence.class.getResourceAsStream("/version.properties")) {
            Properties versionProperties = new Properties();
            versionProperties.load(stream);
            readVersion = versionProperties.getProperty("app.version");
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Version cannot be retrieved!", e);
            readVersion = "Unknown";
        }
        VERSION = readVersion;
    }


    private static final Path FIRST_RUN_FILE = Path.of("firstrun");

    private static long botId;
    private static Path presenceFile;

    private static CreateParams createParams;
    private static InstanceLock uniqueInstance;
    private static TrayIcon trayIcon;
    private static ImageIcon icon;
    private static Core discordCore;
    private static LuaBridge luaBridge;
    private static Platform platform;
    private static SystemInfo systemInfo;

    private static int runningConnectionAttempts;
    private static final AtomicBoolean showingStatus = new AtomicBoolean(true);
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
            (runnable) -> new Thread(runnable, "ComputerPresenceThread"));

    private static ScheduledFuture<?> tickFuture, lockingFuture;
    private static int tickRate;

    public static class CliArguments {
        @Parameter(names = {"-id", "--bot-id"}, description = "Discord Application/Client Bot ID")
        private Long botId;

        @Parameter(names = {"-file", "--presence-file"}, description = "ComputerPresence Lua Presence Script")
        private String presenceFile;
    }

    static void main(String[] args) {
        LOGGER.info("{} version {}", TITLE, VERSION);
        LOGGER.info("Starting!");
        CliArguments cArgs = new CliArguments();

        JCommander.newBuilder()
                .addObject(cArgs)
                .build()
                .parse(args);

        LOGGER.debug("Parsed command line arguments");
        applyCommandLineArguments(cArgs);

        SwingUtilities.invokeLater(ComputerPresence::swingInit);
    }

    private static void applyCommandLineArguments(CliArguments cArgs) {
        if (cArgs.botId != null) {
            botId = cArgs.botId;
        } else {
            botId = Long.parseLong(System.getProperty("discord.botId", "-1"));
            if (botId == -1) {
                throw new IllegalArgumentException(BUNDLE.getString("app.error.noBotId"));
            }
        }

        if (cArgs.presenceFile != null) {
            presenceFile = Path.of(cArgs.presenceFile);
        } else {
            String where = System.getProperty("presence.file", "presence.lua");
            presenceFile = Path.of(where);
        }
    }

    private enum NotifyExceptionOption { CONTINUE, RELOAD_SCRIPT, EXIT }

    private static NotifyExceptionOption notifyException(Throwable t, String header, boolean canContinue) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(header), BorderLayout.NORTH);

        JTextArea errorField = new JTextArea();
        errorField.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(errorField);

        try (StringWriter writer = new StringWriter();
             PrintWriter out = new PrintWriter(writer)) {
            t.printStackTrace(out);
            errorField.setText(writer.toString());
        } catch (IOException e) {
            // silence
            LOGGER.error("Exception thrown in error handler", e);
            errorField.setText(t.toString());
        }

        panel.add(scrollPane, BorderLayout.CENTER);

        if (canContinue) {
            panel.add(new JLabel(BUNDLE.getString("app.dialog.error.footer")), BorderLayout.SOUTH);
        }

        final String CONTINUE = BUNDLE.getString("app.dialog.error.continue");
        final String RELOAD = BUNDLE.getString("app.dialog.error.reload");
        final String EXIT = BUNDLE.getString("app.dialog.error.exit");
        final String OKAY = BUNDLE.getString("app.dialog.error.okay");

        String[] options = canContinue ? new String[] {
                CONTINUE,
                RELOAD,
                EXIT
        } : new String[] { OKAY };

        JOptionPane optionPane = new JOptionPane(panel,
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION);
        optionPane.setOptions(options);

        JDialog dialog = optionPane.createDialog(TITLE);
        dialog.setResizable(true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setMaximumSize(new Dimension((int) (screenSize.width * .8), (int) (screenSize.height * .8))); // haha

        dialog.pack();
        dialog.setVisible(true);

        if (canContinue) {
            Object value = optionPane.getValue();
            if (Objects.equals(CONTINUE, value)) {
                return NotifyExceptionOption.CONTINUE;
            }

            if (Objects.equals(RELOAD, value)) {
                return NotifyExceptionOption.RELOAD_SCRIPT;
            }
        }
        return NotifyExceptionOption.EXIT;
    }

    private static void swingInit() {
        try {
            LOGGER.info("Installing FlatLAF");
            FlatLightLaf.setup();

            executor.submit(ComputerPresence::init);
        } catch (Throwable e) {
            LOGGER.error("Uncaught exception thrown", e);
            notifyException(e, BUNDLE.getString("app.error.init"), false);
            System.exit(1);
        }
    }

    private static void init() {
        try {
            readIcon();
            lockInstance();
            doFirstRun();
            confirmPresenceFile();
            buildTrayIcon();
            initPlatform();
            initLua();
            initDiscord();

            LOGGER.info("Start ticking!");

            tickFuture = executor.scheduleWithFixedDelay(ComputerPresence::tick,
                    3,
                    tickRate,
                    TimeUnit.MILLISECONDS);
            if (!uniqueInstance.isLocked()) {
                lockingFuture = executor.scheduleAtFixedRate(ComputerPresence::tryLockingTheDamnInstance,
                        0,
                        500,
                        TimeUnit.MILLISECONDS);
            }
        } catch (Throwable e) {
            LOGGER.error("Uncaught exception thrown", e);
            notifyException(e, BUNDLE.getString("app.error.init"), false);
            System.exit(1);
        }
    }

    private static void tryLockingTheDamnInstance() {
        if (!uniqueInstance.isLocked()) {
            try {
                uniqueInstance.lock();
            } catch (InstanceLock.LockOperationFailedException e) {
                LOGGER.error("Failed to lock instance", e);
            }
            LOGGER.debug("I LOCKED THE FILE NERD!!!");
            lockingFuture.cancel(true);
        }
    }

    private static void readIcon() {
        LOGGER.info("Reading application icon");
        try (InputStream stream = ComputerPresence.class.getResourceAsStream("logo.png")) {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(stream)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void lockInstance() {
        LOGGER.info("Locking application instance");
        uniqueInstance = new InstanceLock(APP_ID);
        boolean isOwner = uniqueInstance.lock();
        if (!isOwner) {
            LOGGER.error("Instance could not be locked, another one is running!");
            int result = JOptionPane.showConfirmDialog(null,
                    BUNDLE.getString("app.error.notLockOwner").formatted(TITLE),
                    TITLE,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (result != JOptionPane.YES_OPTION) {
                System.exit(0);
            } else {
                LOGGER.warn("Continued running!");
            }
        }
    }

    private static void doFirstRun() {
        if (Files.notExists(FIRST_RUN_FILE)) {
            LOGGER.info("First run check");
            confirmWarning();

            try {
                Files.createFile(FIRST_RUN_FILE);
            } catch (IOException e) {
                LOGGER.info("Error creating firstrun file", e);
                LOGGER.info(String.valueOf(FIRST_RUN_FILE));
                // silent
            }
        }
    }

    private static void confirmWarning() {
        int result = JOptionPane.showConfirmDialog(null,
                BUNDLE.getString("app.firstRunWarning").formatted(TITLE, presenceFile.toString()),
                TITLE,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result != JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private static void confirmPresenceFile() {
        LOGGER.info("Presence file: {}", presenceFile);
        if (Files.notExists(presenceFile)) {
            LOGGER.info("Presence file does not exist, creating default one now.");
            try (InputStream stream = ComputerPresence.class.getResourceAsStream("default.lua")) {
                Files.copy(Objects.requireNonNull(stream), presenceFile);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static void buildTrayIcon() {
        LOGGER.info("Building tray icon");

        TrayIconProvider provider = SystemTrayProvider.get();
        trayIcon = provider.create(APP_ID);
        trayIcon.setImage(icon.getImage());
        trayIcon.setTooltip(TITLE);

        TrayMenu menu = provider.createMenu();
        TrayCheckboxMenuItem enabledItem = provider.createCheckboxMenuItem(BUNDLE.getString("app.popup.enabled"));
        TrayMenuItem reloadItem = provider.createMenuItem(BUNDLE.getString("app.popup.reload"));
        TrayMenuItem exitItem = provider.createMenuItem(BUNDLE.getString("app.popup.exit"));
        TrayMenuItem licensesItem = provider.createMenuItem(BUNDLE.getString("app.popup.licenses"));

        enabledItem.setChecked(true);

        enabledItem.addCallback(() -> {
            // huhu ;((
            // showingStatus.set(enabledItem.isChecked());
            // at least it's true on initialize huhu
            showingStatus.set(!showingStatus.get());

            // do an immediate clearActivity if it's disabled
            if (!showingStatus.get()) {
                executor.submit(() -> discordCore.activityManager().clearActivity());
            }
        });
        reloadItem.addCallback(() -> executor.submit(ComputerPresence::reloadScript));
        exitItem.addCallback(() -> new Thread(ComputerPresence::shutdown, "Shutdown Thread").start());
        licensesItem.addCallback(ComputerPresence::showLicenses);

        menu.add(enabledItem);
        menu.add(reloadItem);
        menu.add(exitItem);
        menu.addSeparator();
        menu.add(licensesItem);

        trayIcon.setTrayMenu(menu);
    }

    private static void initDiscord() {
        LOGGER.info("Connecting to Discord instance");
        createParams = new CreateParams();
        createParams.setClientID(botId);
        createParams.setFlags(CreateParams.getDefaultFlags());

        int maxTries = 30;
        for (int tries = 0; tries < maxTries; tries++) {
            LOGGER.info("Try {} / {}", tries + 1, maxTries);
            try {
                Thread.sleep(5000);
                if (discordCore == null) {
                    discordCore = new Core(createParams);
                }

                if (discordCore.isDiscordRunning()) {
                    break;
                }
            } catch (RuntimeException | InterruptedException e) {
                LOGGER.error("Error connecting to Discord", e);
            }
        }

        if (discordCore == null) {
            JOptionPane.showMessageDialog(null,
                    BUNDLE.getString("app.error.discordNotOpen").formatted(TITLE),
                    TITLE,
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        LOGGER.info("Connected successfully!");
    }

    private static void initPlatform() {
        LOGGER.info("Initializing Platform");
        platform = PlatformProvider.getInstance();
        systemInfo = new SystemInfo();
    }

    private static void initLua() {
        LOGGER.info("Initializing Lua");
        luaBridge = new LuaBridge(platform, systemInfo);
        try {
            luaBridge.getPresenceFunction(Files.readString(presenceFile));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        tickRate = luaBridge.getTickRate();
        LOGGER.info("Tick rate: {}", tickRate);
        LOGGER.info("Lua initialized successfully!");
    }

    private static void tick() {
        try {
            if (checkDiscordConnection()) {
                discordCore.runCallbacks();
                if (showingStatus.get()) {
                    runLuaBridge();
                    updateActivity();
                } else {
                    discordCore.activityManager().clearActivity();
                }
            }
        } catch (Throwable e) {
            if (e instanceof IOException ioException
                    && Objects.equals(ioException.getMessage(), "The pipe has been ended")) {
                return; // give a second change
            }
            NotifyExceptionOption result = notifyException(e, BUNDLE.getString("app.error.tick"), true);
            if (result == NotifyExceptionOption.EXIT) {
                System.exit(1);
            } else if (result == NotifyExceptionOption.RELOAD_SCRIPT) {
                reloadScript();
            }
        }
    }

    private static boolean checkDiscordConnection() {
        if (discordCore != null && !discordCore.isDiscordRunning()) {
            LOGGER.error("Discord stopped running!");
            discordCore.close();
            discordCore = null;
        }

        if (discordCore == null) {
            LOGGER.error("Attempt {} connecting to Discord", runningConnectionAttempts);
            try {
                discordCore = new Core(createParams);
            } catch (Exception e) {
                LOGGER.error("Error connecting to Discord", e);
            }
            runningConnectionAttempts++;
            return false;
        }
        runningConnectionAttempts = 0;
        return true;
    }

    private static void runLuaBridge() {
        try {
            luaBridge.run();
        } catch (Throwable le) {
            LOGGER.error("Lua-side exception", le);
            throw le;
        }
    }

    private static String validify(String input) {
        int length = input.length();
        if (length >= MINIMUM_TEXT_LENGTH && length <= MAXIMUM_TEXT_LENGTH) return input;

        if (length < MINIMUM_TEXT_LENGTH) {
            input = input + " ".repeat(MINIMUM_TEXT_LENGTH - length);
        }

        if (length > MAXIMUM_TEXT_LENGTH) {
            input = input.substring(0, MAXIMUM_TEXT_LENGTH - 4);
            input += "...";
        }

        return input;
    }

    private static void updateActivity() {
        try {
            ActivityManager activityManager = discordCore.activityManager();
            DiscordPresenceObject presenceObject = luaBridge.getPresenceObject();
            try (Activity activity = new Activity()) {
                activity.setDetails(validify(presenceObject.getDetails()));
                activity.setState(validify(presenceObject.getState()));
                activity.setType(presenceObject.getActivityType());

                if (presenceObject.hasParty()) {
                    ActivityPartySize partySize = activity.party().size();
                    partySize.setCurrentSize(presenceObject.getCurrentPartySize());
                    partySize.setMaxSize(presenceObject.getMaxPartySize());
                }

                ActivityTimestamps timestamps = activity.timestamps();
                if (presenceObject.hasTimestampStart()) {
                    timestamps.setStart(Instant.ofEpochMilli(presenceObject.getActivityTimestampStart()));
                }

                if (presenceObject.hasTimestampEnd()) {
                    timestamps.setEnd(Instant.ofEpochMilli(presenceObject.getActivityTimestampEnd()));
                }

                ActivityAssets assets = activity.assets();
                assets.setLargeImage(presenceObject.getLargeImageKey());
                assets.setLargeText(validify(presenceObject.getLargeText()));

                assets.setSmallImage(presenceObject.getSmallImageKey());
                assets.setSmallText(validify(presenceObject.getLargeImageKey()));

                DiscordPresenceObject.Button buttonAObj = presenceObject.getButtonA();
                DiscordPresenceObject.Button buttonBObj = presenceObject.getButtonB();

                if (buttonAObj != null) {
                    activity.setActivityButtonsMode(ActivityButtonsMode.BUTTONS);
                    ActivityButton button = new ActivityButton();
                    button.setLabel(buttonAObj.label);
                    button.setUrl(buttonAObj.url);
                    activity.addButton(button);
                }

                if (buttonBObj != null) {
                    activity.setActivityButtonsMode(ActivityButtonsMode.BUTTONS);
                    ActivityButton button = new ActivityButton();
                    button.setLabel(buttonBObj.label);
                    button.setUrl(buttonBObj.url);
                    activity.addButton(button);
                }

                activityManager.updateActivity(activity);
            }
        } catch (Throwable de) {
            LOGGER.error("Discord-side exception", de);
            throw de;
        }
    }

    private static void reloadScript() {
        if (Files.notExists(presenceFile)) {
            notifyException(new FileNotFoundException(presenceFile.toString()),
                    BUNDLE.getString("app.error.reload.notFound"),
                    false);
            return;
        }

        try {
            if (tickFuture != null) {
                tickFuture.cancel(true);
            }

            if (luaBridge != null) {
                luaBridge.destroy();
            }

            initLua();

            tickFuture = executor.scheduleWithFixedDelay(ComputerPresence::tick,
                    3000,
                    tickRate,
                    TimeUnit.MILLISECONDS);

            LOGGER.info("Successfully reloaded Lua");
        } catch (Throwable lt) {
            NotifyExceptionOption result = notifyException(lt, BUNDLE.getString("app.error.reload"), true);
            if (result == NotifyExceptionOption.EXIT) {
                System.exit(1);
            } else if (result == NotifyExceptionOption.RELOAD_SCRIPT) {
                reloadScript();
            }
        }
    }

    private static void executorContextShutdown() {
        try {
            if (luaBridge != null) {
                luaBridge.destroy();
                LOGGER.info("Destroyed Lua bridge");
            }
        } catch (Exception e) {
            LOGGER.error("Error while destroying Lua", e);
        }

        try {
            if (discordCore != null) {
                discordCore.activityManager().clearActivity();
                discordCore.close();
                LOGGER.info("Closed Discord connection");
            }
        } catch (Exception e) {
            LOGGER.error("Error closing Discord connection", e);
        }

        try {
            if (uniqueInstance != null) {
                uniqueInstance.unlock();
                LOGGER.info("Unlocked instance lock");
            }
        } catch (Exception e) {
            LOGGER.error("Error unlocking InstanceLock", e);
        }

        try {
            if (trayIcon != null) {
                trayIcon.destroy();
                LOGGER.info("Destroyed tray icon");
            }
        } catch (Exception e) {
            LOGGER.error("Error destroying tray icon", e);
        }
    }

    private static void shutdown() {
        LOGGER.info("Shutting down!");

        try {
            executor.submit(ComputerPresence::executorContextShutdown).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.info("Error while calling executor to shut down", e);
        }

        try {
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                LOGGER.warn("Executor did not shut down on time!");
                executor.shutdownNow();
                executor.awaitTermination(10, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            LOGGER.info("Error while shutting down executor", e);
        }

        LOGGER.info("Goodbye!");
        System.exit(0);
    }

    private static void showLicenses() {
        LicenseManager.openLicense(ComputerPresence.class, BUNDLE);
    }
}
