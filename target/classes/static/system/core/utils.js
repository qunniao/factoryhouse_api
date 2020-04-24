var utils = {
	accAdd: function(arg1, arg2){
		var r1, r2, m;
		
		try{
			r1 = arg1.toString().split(".")[1].length;
		}catch(e){
			r1 = 0;
		}
		
		try{
			r2 = arg2.toString().split(".")[1].length;
		}catch(e){
			r2 = 0;
		}
		
		m = Math.pow(10,Math.max(r1, r2));
		
		return (arg1 * m + arg2 * m) / m;
	}, accSub: function(arg1, arg2){
		var r1, r2, m, n;
		
		try{
			r1 = arg1.toString().split(".")[1].length;
		}catch(e){
			r1 = 0;
		}
		
		try{
			r2 = arg2.toString().split(".")[1].length;
		}catch(e){
			r2 = 0;
		}
		
		m = Math.pow(10,Math.max(r1,r2));
		n = (r1 >= r2) ? r1 : r2;
		
		return ((arg1 * m - arg2 * m) / m).toFixed(n);
	}, accMul: function(arg1, arg2){
		var m = 0, s1=arg1.toString(), s2=arg2.toString();
		
		try{
			m += s1.split(".")[1].length;
		}catch(e){}
		
		try{
			m += s2.split(".")[1].length;
		}catch(e){}
		
		return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
	}, accDiv: function(arg1,arg2){
		var t1=0, t2=0, r1, r2;
	    
	    try{
	    	t1 = arg1.toString().split(".")[1].length;
	    }catch(e){}
	    
	    try{
	    	t2 = arg2.toString().split(".")[1].length;
	    }catch(e){}
	    
        r1 = Number(arg1.toString().replace(".",""));  
        r2 = Number(arg2.toString().replace(".",""));  
        
        return (r1 / r2) * Math.pow(10, t2 - t1);  
	}, changeTwoDecimal: function(floatvar){
		var f_x = parseFloat(floatvar);
		if (isNaN(f_x)){
			 alert('function:changeTwoDecimal->parameter error');
			console.log('function:changeTwoDecimal->parameter error');
			return false;
		}
		var f_x = Math.round(floatvar*100)/100;
		return f_x;
	}, 	changeThrDecimal: function(floatvar){
		var f_x = parseFloat(floatvar);
		if (isNaN(f_x)){
			 alert('function:changeTwoDecimal->parameter error');
			console.log('function:changeThrDecimal->parameter error');
			return false;
		}
		var f_x = Math.round(floatvar*1000)/1000;
		return f_x;
	}, decimal: function (num, v){
		var vv = Math.pow(10,v);
		return Math.round(num * vv) / vv;
	}, 	getBigNumber: function(currencyDigits){

		var MAXIMUM_NUMBER = 99999999999;
		var CN_ZERO = "零";
		var CN_ONE = "一";
		var CN_TWO = "二";
		var CN_THREE = "三";
		var CN_FOUR = "四";
		var CN_FIVE = "五";
		var CN_SIX = "六";
		var CN_SEVEN = "七";
		var CN_EIGHT = "八";
		var CN_NINE = "九";
		var CN_TEN = "十";
		var CN_HUNDRED = "百";
		var CN_THOUSAND = "千";
		var CN_TEN_THOUSAND = "万";
		var CN_HUNDRED_MILLION = "亿";
		var CN_SYMBOL = "";
		var CN_DOLLAR = "";
		var CN_TEN_CENT = "";
		var CN_CENT = "";
		var CN_INTEGER = "";

		var integral; // Represent integral part of digit number.
		var decimal; // Represent decimal part of digit number.
		var outputCharacters; // The output result.
		var parts;
		var digits, radices, bigRadices, decimals;
		var zeroCount;
		var i, p, d;
		var quotient, modulus;

		currencyDigits = currencyDigits.toString();
		if (currencyDigits == "") {
			return "O";
		}
		if (currencyDigits.match(/[^,.\d]/) != null) {
			alert("数字非法!");
			return "";
		}
		if ((currencyDigits).match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {
			alert("数字非法");
			return "";
		}

		currencyDigits = currencyDigits.replace(/,/g, ""); // Remove comma
		currencyDigits = currencyDigits.replace(/^0+/, ""); // Trim zeros at the
		
		if (Number(currencyDigits) > MAXIMUM_NUMBER) {
			alert("数字超过99999999999!");
			return "";
		}

		parts = currencyDigits.split(".");
		if (parts.length > 1) {
			integral = parts[0];
			decimal = parts[1];
			decimal = decimal.substr(0, 2);
		} else {
			integral = parts[0];
			decimal = "";
		}
		
		digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);
		radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);
		bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);
		decimals = new Array(CN_TEN_CENT, CN_CENT);
		
		outputCharacters = "";
		
		if (Number(integral) > 0) {
			zeroCount = 0;
			for (i = 0; i < integral.length; i++) {
				p = integral.length - i - 1;
				d = integral.substr(i, 1);
				quotient = p / 4;
				modulus = p % 4;
				if (d == "0") {
					zeroCount++;
				} else {
					if (zeroCount > 0) {
						outputCharacters += digits[0];
					}
					zeroCount = 0;
					outputCharacters += digits[Number(d)] + radices[modulus];
				}
				if (modulus == 0 && zeroCount < 4) {
					outputCharacters += bigRadices[quotient];
				}
			}
			outputCharacters += CN_DOLLAR;
		}
		
		if (decimal != "") {
			for (i = 0; i < decimal.length; i++) {
				d = decimal.substr(i, 1);
				if (d != "0") {
					outputCharacters += digits[Number(d)] + decimals[i];
				}
			}
		}
		if(outputCharacters.substring(0, 2) == "一十"){
			outputCharacters = outputCharacters.substring(1);
		}
		
		if (outputCharacters == "") {
			outputCharacters = CN_ZERO + CN_DOLLAR;
		}
		
		if (decimal == "") {
			outputCharacters += CN_INTEGER;
		}
		
		outputCharacters = CN_SYMBOL + outputCharacters;
		
		return outputCharacters;
	}, getCNMoney: function(currencyDigits){

		var MAXIMUM_NUMBER = 99999999999.99;
		var CN_ZERO = "零";
		var CN_ONE = "壹";
		var CN_TWO = "贰";
		var CN_THREE = "叁";
		var CN_FOUR = "肆";
		var CN_FIVE = "伍";
		var CN_SIX = "陆";
		var CN_SEVEN = "柒";
		var CN_EIGHT = "捌";
		var CN_NINE = "玖";
		var CN_TEN = "拾";
		var CN_HUNDRED = "佰";
		var CN_THOUSAND = "仟";
		var CN_TEN_THOUSAND = "万";
		var CN_HUNDRED_MILLION = "亿";
		var CN_SYMBOL = "";
		var CN_DOLLAR = "元";
		var CN_TEN_CENT = "角";
		var CN_CENT = "分";
		var CN_INTEGER = "整";

		var integral; // Represent integral part of digit number.
		var decimal; // Represent decimal part of digit number.
		var outputCharacters; // The output result.
		var parts;
		var digits, radices, bigRadices, decimals;
		var zeroCount;
		var i, p, d;
		var quotient, modulus;
		var negative  = "";
		
		if(currencyDigits < 0){
			negative = "负";
			currencyDigits = Math.abs(currencyDigits);
		}

		currencyDigits = currencyDigits.toString();
		if (currencyDigits == "") {
			return "零";
		}
		if (currencyDigits.match(/[^,.\d]/) != null) {
			alert("钱数非法!");
			return "";
		}
		if ((currencyDigits).match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {
			alert("钱数非法");
			return "";
		}

		currencyDigits = currencyDigits.replace(/,/g, ""); // Remove comma
		currencyDigits = currencyDigits.replace(/^0+/, ""); // Trim zeros at the
		
		if (Number(currencyDigits) > MAXIMUM_NUMBER) {
			alert("金额超过99999999999.99!");
			return "";
		}

		parts = currencyDigits.split(".");
		if (parts.length > 1) {
			integral = parts[0];
			decimal = parts[1];
			decimal = decimal.substr(0, 2);
		} else {
			integral = parts[0];
			decimal = "";
		}
		
		digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);
		radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);
		bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);
		decimals = new Array(CN_TEN_CENT, CN_CENT);
		outputCharacters = "";
		
		if (Number(integral) > 0) {
			zeroCount = 0;
			for (i = 0; i < integral.length; i++) {
				p = integral.length - i - 1;
				d = integral.substr(i, 1);
				quotient = p / 4;
				modulus = p % 4;
				if (d == "0") {
					zeroCount++;
				}else{
					if (zeroCount > 0) {
						outputCharacters += digits[0];
					}
					zeroCount = 0;
					outputCharacters += digits[Number(d)] + radices[modulus];
				}
				if (modulus == 0 && zeroCount < 4) {
					outputCharacters += bigRadices[quotient];
				}
			}
			outputCharacters += CN_DOLLAR;
		}
		
		if (decimal != "") {
			for (i = 0; i < decimal.length; i++) {
				d = decimal.substr(i, 1);
				if (d != "0") {
					outputCharacters += digits[Number(d)] + decimals[i];
				}
			}
		}
		
		if (outputCharacters == "") {
			outputCharacters = CN_ZERO + CN_DOLLAR;
		}
		
		if (decimal == "") {
			outputCharacters += CN_INTEGER;
		}
		
		outputCharacters = CN_SYMBOL + negative + outputCharacters;
		
		return outputCharacters;
	}, getMapFromStr: function(str){
		var arry = str.split(";");
		var opts = {};
		$.each(arry, function(i, v){
			opts[v.split(':')[0]] = v.split(':')[1];
		});
		return opts;
	}, getStrFromMap: function(opts){
	    var str = "";
	    $.each(opts, function(i, v){
	    	str += i+":"+v+";";
	    });
	    str.substring(0, str.length-1);
	    return str;
	}, padLeft: function(str, lenght) {
		if(str == undefined || str == null){
			return "";
		}else if(str.length >= lenght){
	        return str;
		}else{
	        return utils.padLeft("0" + str, lenght);
		}
	}, fillArray: function(length, defValue){
		var _array = [];
		
		if(defValue == undefined || defValue == null){
			defValue = 0;
		}
		
		for(var i=0;i<length;i++){
			_array[i] = defValue;
		}
		
		return _array;
	}, dateDiff: function(interval, date1, date2){
	    var objInterval = {'D' : 1000 * 60 * 60 * 24, 'H' : 1000 * 60 * 60, 'M' : 1000 * 60, 'S' : 1000, 'T' : 1};
	    interval = interval.toUpperCase();
	    var dt1 = Date.parse(date1.replace(/-/g, '/'));
	    var dt2 = Date.parse(date2.replace(/-/g, '/'));
	    try{
	        return Math.round((dt2 - dt1) / objInterval[interval]);
	    }catch (e){
	        return e.message;
	    }
	}, cleanChosen: function(id){
		$("#" + id).html("<option value=''></option>").trigger("chosen:updated");
		$("#" + id + "_chosen input").val("");
	}
};

