package Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
    private final Logger logger = Logger.getLogger(MyLogger.class.getName());
    private FileHandler fh = null;

    public MyLogger() {
        //just to make our log file nicer :)
        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
        try {
            fh = new FileHandler("/home/enaki/Documents/TUIasi/SD/Laboratoare/Lab_2/Tema_2/JEE-App/MyLogFile" + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
    }

    public void log(String mess) {
        logger.info(mess);
    }
}