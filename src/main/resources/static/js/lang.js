$.namespace("dataservice.lang");
dataservice.lang = function() {
	var LANG_EN = 0;
	var LANG_CN = 1;
	var lang = {
		'Index' : ['Index','首页'],
		'Version' : ['Version' , '版本'],
		/**weburi-detail*/
		'Content':['Content', '内容'] 
		
	};
	
	var COOKIE_LANG_NAME = "cookie_lang";
	
	function log(str) {
		if (typeof (console) != 'undefined' && typeof(console.log) != 'undefined') {
			console.log(str);
		} else {
			$('body').append('<input type="hidden" value="' + str + " />");
		}
	}
	
	function setCookie(name,value,expires,path,domain,secure)
	{
		var expDays = expires*24*60*60*1000;
		var expDate = new Date();
		expDate.setTime(expDate.getTime()+expDays);
		var expString = ((expires==null) ? "": (";expires="+expDate.toGMTString()));
		var pathString = ((path==null) ? "": (";path="+path));
		var domainString = ((domain==null) ? "": (";domain="+domain));
		var secureString = ((secure==true) ? ";secure": "");
		document.cookie = name + "="+ escape(value) + expString + pathString + domainString + secureString;
	}

	function getCookie(name)
	{
		var result = null;
		var myCookie = document.cookie + ";";
		var searchName = name + "=";
		var startOfCookie = myCookie.indexOf(searchName);
		var endOfCookie;
		if (startOfCookie != -1)
		{
			startOfCookie += searchName.length;
			endOfCookie = myCookie.indexOf(";",startOfCookie);
			result = unescape(myCookie.substring(startOfCookie,endOfCookie));
		}
		return result;
	}
	
	function setText($obj) {
		var key = $obj.attr('langKey');
		if (typeof(lang[key]) != 'undefined') {
			var text = lang[key][dataservice.lang.langNow];
			$obj.text(lang[key][dataservice.lang.langNow]);
		} else {
			log('key [' + key + '] not found');
		}
	}
	function setTitle($obj) {
		var key = $obj.attr('langKey');
		if (typeof(lang[key]) != 'undefined') {
			var title = lang[key][dataservice.lang.langNow];
			$obj.attr('title', title);
		} else {
			log('key [' + key + '] not found');
		}
	}
	
	return {
		langNow : LANG_CN,
		EVENT_LOAD_FINISHED : 'loadFinished',
		init : function(langNow) {
			if (typeof(langNow) != 'undefined') {
				this.setLangType(langNow);
			} else {
				var langInCookie = getCookie(COOKIE_LANG_NAME);
				if (langInCookie == LANG_CN || langInCookie == LANG_EN) {
					this.setLangType(langInCookie);
				}
			}
			$(document).on(this.EVENT_LOAD_FINISHED, '.lang', function() {
				log('load lang');
				setText($(this));
			});
			$(document).on(this.EVENT_LOAD_FINISHED, '.langTitle', function() {
				log('load title');
				setTitle($(this));
			});
			this.trigger();
			
			$(document).on('click','.langSelector',function() {
				var langSelected = $(this).attr('langNow');
				dataservice.lang.setLangType(langSelected);
				dataservice.lang.trigger();
				return false;
			});
		},
		setLangType : function (langNow) {
			this.langNow = langNow;
			setCookie(COOKIE_LANG_NAME,langNow,30,'/');
		},
		getLangType : function () {
			return this.langNow;
		},
		show : function($parent) {
			var $obj;
			var $objTitle;
			if ($parent) {
				$obj = $parent.find('.lang');
				$objTitle = $parent.find('.langTitle');
			} else {
				$obj = $('.lang');
				$objTitle = $('.langTitle');
			}
			$obj.each(function() {
				setText($(this));
			});
			$objTitle.each(function() {
				setTitle($(this));
			});
		},
		trigger : function() {
			log('to load lang now');
			$('.lang').trigger(this.EVENT_LOAD_FINISHED);//触发语言显示事件
			$('.langTitle').trigger(this.EVENT_LOAD_FINISHED);//触发语言显示事件
		}
	}
}();
