/**

1. 호출 방법 
var loading = new KZ_Loading('数据加载中。。。');
setTimeout(function () {
  console.log('加载完成!');
  loading.hide();
}, 1000);

var loading = new KZ_Loading({
  content: '数据加载中。。。',
  time: 2000
});
loading.show();

*/

function KZ_Loading(config) {
		  if (this instanceof KZ_Loading) {
		    const domTemplate = '<div class="modal fade kz-loading" data-kzid="@@KZ_Loadin_ID@@" backdrop="static" keyboard="false"><div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px"><div class="progress progress-striped active" style="margin-bottom: 0;"><div class="progress-bar" style="width: 100%;"></div></div><h5>@@KZ_Loading_Text@@</h5></div></div>';
	    this.config = {
	      content: 'loading...',
	      time: 0,
	    };
	    if (config != null) {
	      if (typeof config === 'string') {
	        this.config = Object.assign(this.config, {
	          content: config
	        });
	      } else if (typeof config === 'object') {
	        this.config = Object.assign(this.config, config);
	      }
	    }
	    this.id = new Date().getTime().toString();
	    this.state = 'hide';
	 
	    /*显示 */
	    this.show = function () {
	      $('.kz-loading[data-kzid=' + this.id + ']').modal({
	        backdrop: 'static',
	        keyboard: false
	      });
	      this.state = 'show';
	      if (this.config.time > 0) {
	        var that = this;
	        setTimeout(function () {
	          that.hide();
	        }, this.config.time);
	      }
	    };
	    /*隐藏 */
	    this.hide = function (callback) {
	      $('.kz-loading[data-kzid=' + this.id + ']').modal('hide');
	      this.state = 'hide';
	      if (callback) {
	        callback();
	      }
	    };
	    /*销毁dom */
	    this.destroy = function () {
	      var that = this;
	      this.hide(function () {
	        var node = $('.kz-loading[data-kzid=' + that.id + ']');
	        node.next().remove();
	        node.remove();
	        that.show = function () {
	          throw new Error('Object has bean destroyed！');
	        };
	        that.hide = function () {};
	        that.destroy = function () {};
	      });
	    }
	 
	    var domHtml = domTemplate.replace('@@KZ_Loadin_ID@@', this.id).replace('@@KZ_Loading_Text@@', this.config.content);
	    $('body').append(domHtml);
	  } else {
	    return new KZ_Loading(config);
	  }
}