//index.js
//获取应用实例
const app = getApp();
const baseUrl = "Web服务地址的部署地址/weather/";
var WxSearch = require('../../wxSearch/wxSearch.js');

Page({
  data: {
    city:'',
    inputShowed: false,
    baseImgPath:"../images/",
    haveWeatherItems:true,
    tmpweatherItems: null,
    weatherData: { "queryName": "", "date": "", "weatherItems": [] }
  },

  onShareAppMessage: function (res) {
    return {
      title: '我在用小程序查天气，快一起来用~',
      desc: '及时了解一个月的天气情况！',
      path: 'pages/index/index',
      success: function (res) {
        // 转发成功
      },
      fail: function (res) {
        // 转发失败
      }
    }
  },

  onShow: function () {
    var that = this;
    wx.getLocation({
      type: 'wgs84',
      success: function (res) {
        // 根据经纬度获取天气情况
        wx.request({
          url: baseUrl + "?latitude=" + res.latitude + "&longitude=" + res.longitude,
          success: (res) => {
            that.setData({  weatherData: res.data });
            // console.log(res.data);
          },
          fail: (res) => { },
          complete: () => { }
        })
      }
    })
  },

   // 搜索栏
  onLoad: function () {
    console.log('onLoad');
    var that = this;
      //初始化的时候渲染wxSearchdata
    WxSearch.init(that, 43, ['杭州', '嘉兴', "海宁", "桐乡", '宁波', '金华', "绍兴", '上海', '苏州', '无锡', '常州', "南京", "济南", "长沙","北京","广州",'厦门',"香港","澳门","深圳"]
    ,true,false);
    // 搜索提示
    // WxSearch.initMindKeys([]);
  }, 
  wxSearchFn: function (e) {
    // var that = this
    // WxSearch.wxSearchAddHisKey(that);
  },
  wxSearchInput: function (e) {
      WxSearch.wxSearchInput(e, this);
   },
  wxSerchFocus: function (e) {
      WxSearch.wxSearchFocus(e, this);
  },
  wxSearchBlur: function (e) {
    var that = this;
    WxSearch.wxSearchBlur(e, that,function(key){
      // console.log("失去焦点:"+key);
      that.search(key);
    });
  },
  wxSearchKeyTap: function (e) {
    var that = this
    WxSearch.wxSearchKeyTap(e, that, function (key){
      //点击热门城市
     // console.log("点击热门城市:" + key);
     that.search(key);
    });
  },
  wxSearchDeleteKey: function (e) {
      WxSearch.wxSearchDeleteKey(e, this);
  },
  wxSearchDeleteAll: function (e) {
    WxSearch.wxSearchDeleteAll(this);
  },
  wxSearchTap: function (e) {
      WxSearch.wxSearchHiddenPancel(this);
  },

  search : function(cityName){
    if (cityName.length==0){
     return;
    }
    wx.request({
      url: baseUrl + cityName,
      success: (res) => {
        this.setData({
          weatherData: res.data
        });
      },
    })
  }

})
