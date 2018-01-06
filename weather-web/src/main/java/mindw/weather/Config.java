package mindw.weather;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {

	private static final Logger LOGGER = Logger.getLogger(Config.class);

	/** 腾讯地图 App key */
	public static String LOCATION_APP_KEY;

	static{
		try {
			Properties pps = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream("WeatherWebConfig.txt"));
			pps.load(in);
			LOCATION_APP_KEY = pps.getProperty("LOCATION_APP_KEY", "");

			System.out.println(LOCATION_APP_KEY);
			
		} catch (Exception e) {
			LOGGER.error(e.toString());
			System.exit(1);
		}
	}

}
