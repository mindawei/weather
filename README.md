### 前置说明
1. /doc 文件夹中包含项目截图和以下将要描述的文档(<a href="https://github.com/mindawei/weather/blob/master/doc/%E5%A4%A9%E6%B0%94%E6%9C%8D%E5%8A%A1%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3.pdf">PDF文档</a> )。
2. weather-web 项目： 天气Web服务。 
3. weather-weixin 项目：查询天气微信小程序（微信扫描下图可以查看）。

![查询天气小程序](https://github.com/mindawei/weather/blob/master/doc/imgs/weahter-weixin.jpg)

4. 项目中一些关键Key已经去除，开发者可以自行添加，如有问题可以留言或者邮件联系。
5. 欢迎 fork 和 star ~

天气服务说明文档
====
# 1背景
## 1.1意义
&emsp;&emsp;设计该服务，主要有以下几个原因：
>1. 目前的一些天气API（高德、百度等）都只有3天的预报，不能提供长期预报。
>2. 第三方API有调用次数限制、认证情况比较麻烦。
## 1.2不足
&emsp;&emsp;该服务存在以下缺陷：
>1. 依赖第三方网站，第三方网站的网页结构变化后需要重新修改代码。
>2. 第三方服务器崩溃或者提供反爬技术的话，该服务失效。
## 1.3 声明
&emsp;&emsp;本服务只是为了学习交流使用。
# 2服务结构
&emsp;&emsp;如果开发者需要基于该服务项目进行开发的话，需要了解该服务的一些结构。该服务（weather-web项目）的总体结构如下图所示。<br>
<p align="center">
<img src="https://github.com/mindawei/weather/blob/master/doc/imgs/frame.png"/>
</p>

&emsp;&emsp;其中，淡粉色部分表示支持查询的接口，淡紫色部分表示第三方的接口或网站，淡绿色部分是项目中主要的几个类。使用该服务，需要注意一下几个方面：
>1. 如果是按经纬度查询，则需要利用第三方API（腾讯的API）将经纬度转换为对应的城市名称。
>2. 因为最后是根据查询城市的拼音找到解析数据的网页的，所以需要事先爬取城市名称和拼音的关系，最后统一转换为按拼音查询。
>3. 为了减少频繁爬取第三方数据，该服务按天粒度进行更新，每次查询的时候会比对天气日期，如果过期才会重新爬取，否则则会返回缓存中的数据。
为了服务的快速，服务所有数据都在内存中，并没有落盘。
# 3服务接口
## 3.1访问接口
&emsp;&emsp;访问接口如下表所示，接口中中文部分表示需要提供的参数。开发者在部署服务后，最终的访问接口是：协议（http 或 https）+ 主机地址 + 接口。<br>
<br>

| 功能        | 接口          | 
| ------------- |:-------------:| 
| 按经纬度进行查询      | /weather/?latitude=纬度&longitude=经度 |
| 按城市名称中文（或拼音）进行查询      | /weather/城市名称或拼音 |

__开放测试接口__：https://weather.mindawei.cn/weather/

以杭州为例：
* https://weather.mindawei.cn/weather/hangzhou
* https://weather.mindawei.cn/weather/杭州
* https://weather.mindawei.cn/weather/?latitude=30.27415&longitude=120.15515

## 3.2返回结果
&emsp;&emsp;返回的结果是Json格式，包含30天天气基本情况，具体如下所示：
<pre><code>
{
    "queryName": "泰州",  // 查询的城市名字，如果无数据返回空
    "date": "2018-01-06",  // 第一天数据日期，缓存可以判断是否过期
    "weatherItems": [     // 天气情况数组，30天
        {
            "date": "01月06日",      // 日期
            "dayKind": "今天",        // 星期几，前三天是今天明天后天 
            "weather": "雪",           // 天气描述
            "weatherImg": "b15.png",   // 天气用什么图片表示
            "minTemperature": "-6",    // 最低温度 
            "maxTemperature": "4",     // 最高温度
            "wind": "东北风 3级"     // 风力描述
        },
        ...
    ]
}
</code></pre>

# 4基于该服务的小程序
&emsp;&emsp;基于该服务可以构建一些天气查询应用。为了演示该服务，写了一个小程序。下面几幅图就是小程序项目（weather-weixin）的演示效果图。小程序启动时可以根据经纬度给出一个当前位置的天气情况，之后可以根据查询关键字更新数据。<br>
<p align="center">
<img src="https://github.com/mindawei/weather/blob/master/doc/imgs/1.png" width="33%" height="33%" />
<img src="https://github.com/mindawei/weather/blob/master/doc/imgs/2.png" width="33%" height="33%" />
<img src="https://github.com/mindawei/weather/blob/master/doc/imgs/3.png" width="33%" height="33%" />
</p>

# 5项目实际部署
## 5.1运行Web服务
1. 如果要支持经纬度查询，则需要配置WeatherWebConfig.txt中的腾讯地图API KEY。
2. 由于小程序使用https，所以如果要运行小程序，则需要将域名指向Web服务主机，申请证书，并配置src/main/resources下的application.properties文件。
## 5.2运行小程序
1. project.config.json中填入你申请的appid。
2. pages\index\index.js内容中开头部分的baseUrl替换成：Web服务地址的部署地址+”weather/”。
