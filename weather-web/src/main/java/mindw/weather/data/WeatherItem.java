package mindw.weather.data;

/**
 * 天气的一条记录
 * @author mindw
 */
public class WeatherItem {
	
	/** 01月13日 */
	private String date;
	
	/** 今天、星期一 */
	private String dayKind;
	
	/** 晴、雨等 */
	private String weather;
	
	/** 天气图片*/
	private String weatherImg;
	
	/** 最低温度 */
	private String minTemperature;
	
	/** 最高温度 */
	private String maxTemperature;
	
	/** 风力 */
	private String wind;
	
	public WeatherItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WeatherItem(String date, String dayKind, String weather, String weatherImg, String minTemperature,
			String maxTemperature, String wind) {
		super();
		this.date = date;
		this.dayKind = dayKind;
		this.weather = weather;
		this.weatherImg = weatherImg;
		this.minTemperature = minTemperature;
		this.maxTemperature = maxTemperature;
		this.wind = wind;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDayKind() {
		return dayKind;
	}

	public void setDayKind(String dayKind) {
		this.dayKind = dayKind;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(String minTemperature) {
		this.minTemperature = minTemperature;
	}

	public String getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(String maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getWeatherImg() {
		return weatherImg;
	}

	public void setWeatherImg(String weatherImg) {
		this.weatherImg = weatherImg;
	}

	@Override
	public String toString() {
		return "WeatherItem [date=" + date + ", dayKind=" + dayKind + ", weather=" + weather + ", weatherImg="
				+ weatherImg + ", minTemperature=" + minTemperature + ", maxTemperature=" + maxTemperature + ", wind="
				+ wind + "]";
	}

}
