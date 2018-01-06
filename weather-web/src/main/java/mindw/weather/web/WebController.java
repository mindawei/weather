package mindw.weather.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import mindw.weather.Config;
import mindw.weather.data.WeatherData;
import mindw.weather.spider.WeatherSpider;
import net.sf.json.JSONObject;


@RestController
public class WebController {
	
	private final static Logger LOGGER = Logger.getLogger(WebController.class);
	
	static{
		WeatherSpider.active();
	}

	/**
	 * 发送地址：localhost:8080/weather/jiaxing
	 */
	@RequestMapping(value="/weather/{cityName}",method = RequestMethod.GET)  
    @ResponseBody   
    public WeatherData getWeatherData(@PathVariable(value="cityName") String cityName) { 
		return WeatherSpider.getWeatherItems(cityName);
    }  
	
	
	/**
	 * 发送地址：http://localhost:8080/weather/?latitude=31.984154&longitude=121.307490
	 */
	@RequestMapping(value="/weather",method = RequestMethod.GET)  
    @ResponseBody   
    public WeatherData getWeatherData(@RequestParam(value = "latitude") String latitude,@RequestParam(value = "longitude")String longitude) { 
		JSONObject jsonObject = httpRequest(latitude,longitude);
		if(jsonObject==null) {
			return WeatherData.ERROR_RESULT;
		}
		
		try {
			String cityName = (String)jsonObject.getJSONObject("result").getJSONObject("address_component").get("city");
			return WeatherSpider.getWeatherItems(cityName);
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("解析地址错误！"+e.toString());
		}
		return WeatherData.ERROR_RESULT;
    }  
	
	/** 
     * 发起http请求并获取结果 
     */  
    private JSONObject httpRequest(String latitude,String longitude) {  
        JSONObject jsonObject = null;  
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream=null;
        try {
        	String requestUrl = String.format("https://apis.map.qq.com/ws/geocoder/v1/?location=%s,%s&key=%s", 
        			latitude,longitude,Config.LOCATION_APP_KEY);
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
  
            //将返回的输入流转换成字符串  
            inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
          jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {  
              ce.printStackTrace();
              LOGGER.error("Server connection timed out");
        } catch (Exception e) {  
        	   e.printStackTrace();
        	   LOGGER.error("http request error:"+e.toString());
        }finally{
    		try {
    			if(inputStream!=null){
    				inputStream.close();
    			}
			} catch (IOException e) {
				e.printStackTrace();
			}
        } 
        return jsonObject;  
    } 

    
//    为了验证证书
//    @RequestMapping(value = "/.well-known/pki-validation/fileauth.txt",method = RequestMethod.GET)
//	public ResponseEntity<byte[]> statisticsToExcel() throws IOException {
//		String filePath = ".well-known/pki-validation/fileauth.txt";
//	    File file=new File(filePath);  
//        HttpHeaders headers = new HttpHeaders();    
//	    String fileName=new String(file.getName().getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
//        headers.setContentDispositionFormData("attachment", fileName);   
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
//	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
//                                          headers, HttpStatus.CREATED);
//	}
}
