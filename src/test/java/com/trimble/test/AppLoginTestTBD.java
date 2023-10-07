package com.trimble.test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class AppLoginTestTBD {

//    private static ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();

    public static void setDriver(AppiumDriver appiumDriver){
        driver.set(appiumDriver);
    }
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");
        startServerAndInitializeDriver();
    }

    /**
     * Start the Appium server automatically, whip up the Emulator, initialize the AppiumDriver instance
     * @throws Exception
     */
    private static void startServerAndInitializeDriver() throws Exception {
        startServer();
        URL serverUrl = server.get().getUrl();//new URL("http://127.0.0.1:4723/");
        /** app bundle(s) are being saved under '<project_root>\src\main\resources\apps' folder
         *  and to make the path cross-platform, java standard file constants are being used
         */
        String appUrl = new StringBuffer().append(System.getProperty("user.dir")).append(File.separator).append("src")
                .append(File.separator).append("main").append(File.separator).append("resources").append(File.separator)
                .append("apps").append(File.separator).append("app-debug.apk").toString();// The app name will change for iOS

        /**
         * Make sure in your config file for TestNG, you introduce various parameters used by the program/framework
         * Many test variables can be added like 'deviceName', 'appUrl' etc.,.
         * 'platform' can be among them. For now, I am hardcoding it
         */
        String platform = "Android"; // like params.get("platform");

        try {
            switch (platform) {
                case ("Android"):
                    //androidUserLoginCheck(serverUrl,appUrl);
                    initializeAndroidDriver(serverUrl, appUrl);
                    break;
                case ("iOS"):
                    initializeiOSDriver(serverUrl, appUrl);
                    break;
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Driver initialization failure. ABORT !!!!" + e);
            throw e;
        }
    }

    /**
     * Simply start the Appium Server and save it in the ThreadLocal variable 'server'.
     */
    private static void startServer(){
           AppiumDriverLocalService srvr = GetAppiumService();
       // AppiumDriverLocalService srvr = AppiumDriverLocalService.buildDefaultService();
        srvr.start();
        if(srvr == null || !srvr.isRunning()){
            throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
        }
        srvr.clearOutPutStreams();
        server.set(srvr);
    }

    /**
     * For appium ver >= 2.0 and java-client ver >= 8.0, run the appium server
     * with plugins to use in lieu of Actions class for 'gestures'.
     * @return
     */
    private static AppiumDriverLocalService GetAppiumService(){
        String appiumJSFilePath = new StringBuffer()//C:\Users\pnuta\AppData\Roaming\npm\node_modules\appium\build\lib
                .append(System.getProperty("user.home")).append(File.separator).append("AppData").append(File.separator)
                .append("Roaming").append(File.separator).append("npm").append(File.separator).append("node_modules")
                .append(File.separator).append("appium").append(File.separator).append("build").append(File.separator)
                .append("lib").append(File.separator).append("main.js").toString();
        String nodeExeFilePath = new StringBuffer()//C:\Program Files\nodejs\node.exe
                .append(System.getenv("ProgramFiles")).append(File.separator).append("nodejs").append(File.separator)
                .append("node.exe").toString();

        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .withAppiumJS(new File(appiumJSFilePath))
                .usingDriverExecutable(new File(nodeExeFilePath))
                .usingPort(4723)//   .usingAnyFreePort()
                .withTimeout(Duration.ofSeconds(40)) // to prevent failing to startLocally error
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE) // session clobbering. new session takes over even the old still running
                .withArgument(GeneralServerFlag.LOCAL_TIMEZONE));
    }
    /**
     * Specific for Android device
     * @param serverUrl
     * @param appUrl
     * @return
     */
    private static boolean initializeAndroidDriver(final URL serverUrl, final String appUrl) throws Exception {
        boolean logged_in = false;
        UiAutomator2Options androidOptions = new UiAutomator2Options()
                /**
                 * DeviceName could be different for different set up(s)
                 */
                .setDeviceName("Pixel_7_Pro_API_34")
                /** UDID could be different for different set up(s)
                 * But if you have only one emulator running on your m/c, you don't need this to be specified.
                 */
                .setUdid("emulator-5554")
                /**
                 * avd name, and AvdLaunchTimeout are to launch the emulator automatically
                 */
                .setAvd("Pixel_7_Pro_API_34")
                .setAvdLaunchTimeout(Duration.ofSeconds(180)) // default is 60
                .setAvdReadyTimeout(Duration.ofSeconds(120)) // default is 60
                .setAutomationName("UiAutomator2")
                .setApp(appUrl);
        AppiumDriver drvr = new AndroidDriver(serverUrl,androidOptions);
        if(drvr == null){
            throw new Exception("driver is null. ABORT!!!");
        }
        setDriver(drvr);
        return logged_in;
    }

    /**
     * Specific for iOS device
     * @param serverUrl
     * @param appUrl
     * @return
     */
    private static boolean initializeiOSDriver(final URL serverUrl, final String appUrl) throws Exception {
        boolean logged_in = false;
        XCUITestOptions androidOptions = new XCUITestOptions()
                .setDeviceName("<iOS_Simulator_Name instead of Emulator_name>")
                /** UDID might be different for different set up(s)
                 * But if you have only one emulator running on your m/c, you don't need this to be specified.
                 */
                .setUdid("<UDID of the iOS Simulator here>")
                .setAutomationName("XCUITest")
                .setApp(appUrl);
        AppiumDriver drvr = new IOSDriver(serverUrl,androidOptions);
        if(drvr == null){
            throw new Exception("driver is null. ABORT!!!");
        }
        setDriver(drvr);
        return logged_in;
    }






}