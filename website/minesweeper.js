var IE = false;
var NN = false;
if (navigator.appName.indexOf("Microsoft") != -1)
	IE = true;
else if (navigator.appName.indexOf("Netscape") != -1)
	NN = true;

function tech_info() {
	var url, width, height;
	url = "technical_details.html";
	width = 600;
	height = 450;
	openWin(url, width, height);
}

function help() {
	var url, width, height;
	url = "help.html";
	width = 600;
	height = 450;
	openWin(url, width, height);
}

function unimplemented_features() {
	var url, width, height;
	url = "unimplemented_features.html";
	width = 360;
	height = 320;
	openWin(url, width, height);
}

function openWin(url, width, height) {
	var x_ord, y_ord, x_offset, y_offset;
	x_offset = 110;
	y_offset = 50;
	if (IE) {
		x_ord = window.screenLeft;
		y_ord = window.screenTop;
	} else if (NN) {
		x_ord = window.screenX;
		y_ord = window.screenY;
	}
	x_ord += x_offset;
	y_ord += y_offset;

	var extraspace = 40;	//handle any taskbar etc...
	
	if (x_ord+width > screen.availWidth + extraspace)
		x_ord = screen.availWidth-width-extraspace;

	if (y_ord+height > screen.availHeight + extraspace)
		y_ord = screen.availHeight-height-extraspace;
	
	window.open(url, "minesweeperWin", "top=" + y_ord + ",left=" + x_ord + ",screenX=" + x_ord + ",+screenY=" + y_ord + ",scrollbars=yes,status=yes,width=" + width + ",height=" + height + ",resizable=yes");
}

