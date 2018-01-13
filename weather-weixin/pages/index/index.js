//index.js
//获取应用实例
const app = getApp();
const baseUrl = "你服务部署的网址/weather/";
var WxSearch = require('../../wxSearchView/wxSearchView.js');

Page({
  data: {
    city:'',
    inputShowed: false,
    baseImgPath:"../images/",
    haveWeatherItems:true,
    tmpweatherItems: null,
    isFormSearch : false,
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
    if (this.isFormSearch){
      this.isFormSearch = false;
      return;
    }
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

  // 搜索页面跳回
  onLoad: function (options) {
    if (options && options.searchValue) {
      var value = options.searchValue;
      if (value.length == 0) {
        return;
      }
      this.isFormSearch = true;
      wx.request({
        url: baseUrl + value,
        success: (res) => {
          this.setData({
            weatherData: res.data
          });
        },
      })
    }
  },

  // 搜索入口  
  wxSearchTab: function () {
    wx.redirectTo({
      url: '../search/search'
    })
  }

})
