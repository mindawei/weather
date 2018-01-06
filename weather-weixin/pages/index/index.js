//index.js
//获取应用实例
const app = getApp();
const baseUrl = "此处填入你部署服务的基地址";

Page({
  data: {
    city: '',
    inputShowed: false,
    inputVal: "",
    baseImgPath:"../images/",
    haveWeatherItems:true,
    tmpweatherItems: null,
    weatherData: { "queryName": "", "date": "", "weatherItems": [] }
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },

  showInput: function () {
    this.setData({
      inputShowed: true
    });
  },
  hideInput: function () {
    this.setData({
      inputVal: "",
      inputShowed: false
    });
  },
  clearInput: function () {
    this.setData({
      inputVal: "",
      city: ""
    });
  },
  finishInput: function () {
      wx.request({
        url: baseUrl + app.globalData.city,
        success: (res) => {
          this.setData({ 
            
            weatherData: res.data });
        },
        fail: () => { },
        complete: () => { }
      })
  },
  inputTyping: function (e) {
    app.globalData.city = e.detail.value;
    this.setData({
      inputVal: e.detail.value,
      city: app.globalData.city
    });
  },
  
  onLoad: function () {
    var that = this;
    wx.getLocation({
      type: 'wgs84',
      success: function (res) {
        console.log(res.latitude+" "+res.longitude);
        console.log(baseUrl + "?latitude=" + res.latitude + "&longitude=" + res.longitude);
        // 根据经纬度获取天气情况
        wx.request({
          url: baseUrl + "?latitude=" + res.latitude + "&longitude=" + res.longitude,
          success: (res) => {
            that.setData({ weatherData: res.data});
            // console.log(res.data);
          },
          fail: (res) => {
            // console.log(res);
          },
          complete: () => {
            // console.log("finish");
          }
        })
      }
    })
  }
})
