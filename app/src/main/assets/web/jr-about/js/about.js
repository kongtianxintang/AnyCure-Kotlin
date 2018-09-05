
/*
*html刚会写 请多多包涵...
*/
//初始值
var mingyi_code = 0;
var default_code = 0;
var pingan_code = 0;
var mai_rui_bo_code = 0;

//存放客户端传过来的code 默认为0;
var app_code = 0;
//存放客户端传过来的渠道号
var app_channel = 0;

//获取dom里的元素
var footer = document.getElementById('footer');
var icon = document.getElementById('icon');
var version = document.getElementById('version');
var tips = document.getElementById('new');
var update_cell = document.getElementById('updateCell');


// version.addEventListener('click',function () {
// 	updateCompanyName(00000003);
// 	updateVersionInfo('1.0.1');
// 	checkUpdate(0,00000003);
// })

update_cell.addEventListener('click',function () {
	linkToAppstore();
})

//mark:code是版本号 channel是渠道号 version版本信息
/*
00000001 明医
xxxxxxx2 捷荣
xxxxxxx3 平安
xxxxxxx6 迈瑞铂
*/
function updateInfo(obj) {
	app_code = obj.code;
	app_channel = obj.channel;
	updateVersionInfo(obj.version);
	checkUpdate(obj.code,obj.channel);
	updateCompanyName(obj.channel);
}

//显示版本信息
function updateVersionInfo(vsersionCode) {
	console.log(vsersionCode);
	version.innerHTML = vsersionCode;
}

//是否有版本升级
function checkUpdate(code,channel) {
	var isUpdate = false;
	switch (channel) {
		case "00000001":
			isUpdate = mingyi_code > code;
		break;
		case "00000002":
			isUpdate = default_code > code;
		break;
		case "00000003":
			isUpdate = pingan_code > code;
		break;
		case "00000006":
			isUpdate = mai_rui_bo_code > code;
		break;
		default:
			isUpdate = default_code > code;
		break;
	}
	var str = isUpdate ? "去更新吧":"无新版本";
	tips.innerHTML = str;
}


//公司名称 及 icon
function updateCompanyName(channel) {
	
	switch (channel) {
		case "00000001":
		footer.innerHTML = 'Copyright © 2018 深圳明医医疗科技有限公司';
		icon.src = './resource/appicon.png';
		break;
		case "00000002":
		footer.innerHTML = 'Copyright © 2018 东莞捷荣技术有限公司';
		icon.src = './resource/appicon.png';
		break;
		case "00000003":
		footer.innerHTML = 'Copyright © 2018 深圳迈瑞铂通讯科技有限公司';
		icon.src = './resource/pingan.png';
		break;
		case "00000006":
		footer.innerHTML = "Copyright © 2018 深圳迈瑞铂通讯科技有限公司"
		icon.src = "./resource/mai_rui_bo.png"
		break
		default:
		footer.innerHTML = 'Copyright © 2018 东莞捷荣技术有限公司 保留所有权利。';
		icon.src = './resource/appicon.png';
		break;
	}
}

//给客户端传值让他们去appstore
function linkToAppstore() {

	switch (app_channel) {
		case "00000001":
			if (mingyi_code > app_code) {
				var path = "https://itunes.apple.com/cn/app/anycure/id1334554055?mt=8";
				window.webkit.messageHandlers.linkToAppstore.postMessage(path)
			}
			break;
		case "00000002":
			if (default_code > app_code) {
				var path = "https://itunes.apple.com/cn/app/anycure/id1334554055?mt=8";
				window.webkit.messageHandlers.linkToAppstore.postMessage(path)
			}
			break;
		case "00000003":
			if (pingan_code > app_code) {
				var path = "https://itunes.apple.com/cn/app/ancure%E5%B9%B3%E5%AE%89%E7%96%97/id1342554789?mt=8";
				window.webkit.messageHandlers.linkToAppstore.postMessage(path)
			}
			break;
		case "00000006":
			if (mai_rui_bo_code > app_code) {
				var path = "https://itunes.apple.com/cn/app/%E8%BF%88%E7%91%9E%E9%93%82/id1428742345?mt=8";
				window.webkit.messageHandlers.linkToAppstore.postMessage(path)
			}
			aboutObj.linkToDownApk("测试地址->wwww.baidu.com")
			break;
		default:
			if (default_code > app_code) {
			var path = "https://itunes.apple.com/us/app/anycure/id1334554055?mt=8";
			window.webkit.messageHandlers.linkToAppstore.postMessage(path)
		}
		break;
	}
}
