package specht.General;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPropertyValue {
    String result = "";
    InputStream is;


    public String getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            is = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (is != null) {
                prop.load(is);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not Found in the classpath");
            }

            Date time = new Date(System.currentTimeMillis());
            String user = prop.getProperty("user");
            String cs3Port = prop.getProperty("CS3Port");
            String pcPort = prop.getProperty("PCPort");
            String cs3IpAdresse = prop.getProperty("CS3IP");
            String pcIpAdresse = prop.getProperty("PCIP");
            if (!checkPropIpAdresse(cs3IpAdresse)) {
                throw new InputMismatchException("IP Adress of CS3 is not valid!");
            }
            if (!checkPropIpAdresse(pcIpAdresse)) {
                throw new InputMismatchException("IP Adress of PC is not valid!");
            }
            if (!checkPropPort(Integer.parseInt(cs3Port))) {
                throw new InputMismatchException("CS3 Port Adress is not valid!");
            }
            if (!checkPropPort(Integer.parseInt(pcPort))) {
                throw new InputMismatchException("PC Port Adress is not valid!");
            }

            result = cs3Port + " " + pcPort + " " +  cs3IpAdresse + " " + pcIpAdresse ;
            System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            is.close();
        }
        return result;

    }

    public boolean checkPropInt (int value) {

        return true;
    }

    public boolean checkPropString (String value) {
        return false;
    }

    public boolean checkPropIpAdresse (String value) {
        Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = p.matcher(value);
        return m.find();
    }

    public boolean checkPropPort (int value) {
        if (value >= 0 && value <= 65535) {
            return true;
        } else { return false;}
    }
}
