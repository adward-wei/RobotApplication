package com.ubtechinc.alpha2ctrlapp.util;


import java.util.Locale;

public class AndroidToLan {

	public static String toLan(String lan) {
		

		String result = "EN";
		if (lan.equalsIgnoreCase("CN")
		/* || language.equalsIgnoreCase("CHINESE") */) {
			result = "CN";
		} else if (lan.equalsIgnoreCase("JP")
		/* || language.equalsIgnoreCase("JAPANESE") */) {// 日语
			result = "JP";
		} else if (lan.equalsIgnoreCase("DE")
		/* || language.equalsIgnoreCase("GERMAN") */) {// 德语
			result = "DE";
		} else if (lan.equalsIgnoreCase("HK") || lan.equalsIgnoreCase("TW")
		/*
		 * || language.equalsIgnoreCase("HONGKONG") ||
		 * language.equalsIgnoreCase("TAIWAN")
		 */) {// 中国(香港)
			result = "HK";
		} else if (lan.equalsIgnoreCase("FR")
		/* || language.equalsIgnoreCase("FRENCH") */) {// 法语
			result = "FR";
		} else if (lan.equalsIgnoreCase("PT")|| lan.equalsIgnoreCase("BR")
		/* || language.equalsIgnoreCase("PORTUGUESE") */) {// 葡萄牙
			result = "PT";
		} else if (lan.equalsIgnoreCase("RU")
		/* || language.equalsIgnoreCase("RUSSIAN") */) {// 俄罗斯
			result = "RU";
		} else if (lan.equalsIgnoreCase("IT")
		/* || language.equalsIgnoreCase("ITALIAN") */) { // 意大利
			result = "IT";
		} else if (lan.equalsIgnoreCase("ES")
		/* || language.equalsIgnoreCase("SPAINISH") */) {// 西班牙
			result = "ES";
		} else if (lan.equalsIgnoreCase("PL")
		/* || language.equalsIgnoreCase("POLISH") */) {// 波兰
			result = "PL";
		} else if (lan.equalsIgnoreCase("TR")
		/*
		 * || language.equalsIgnoreCase("TURKEY") ||
		 * language.equalsIgnoreCase("TURKISH")
		 */) {// 土耳其
			result = "TR";
		} else if (lan.equalsIgnoreCase("NL")
		/* || language.equalsIgnoreCase("DUTCH") */) {// 荷兰语
			result = "NL";
		} else if (lan.equalsIgnoreCase("GR")
		/* || language.equalsIgnoreCase("GREECE") */) {// 希腊
			result = "GR";
		} else if (lan.equalsIgnoreCase("HU")
		/* || language.equalsIgnoreCase("HUNGARIAN") */) {// 匈牙利语
			result = "HU";
		} else if (lan.equalsIgnoreCase("AR") || lan.equalsIgnoreCase("EG")
		/* || language.equalsIgnoreCase("ARABIC") */) {// 阿拉伯语
			result = "AR";
		} else if (lan.equalsIgnoreCase("DA")
		/* || language.equalsIgnoreCase("DANISH") */) {// 丹麦语
			result = "DA";
		} else if (lan.equalsIgnoreCase("FA")
		/* || language.equalsIgnoreCase("PERSIAN") */) {// 波斯语
			result = "FA";
		} else if (lan.equalsIgnoreCase("KO") || lan.equalsIgnoreCase("KR")
		/* || language.equalsIgnoreCase("KOREAN") */) {// 韩语
			result = "KO";

		} else if (lan.equalsIgnoreCase("FI")
		/* || language.equalsIgnoreCase("FINNISH") */) {// 芬兰语
			result = "FI";
		} else if (lan.equalsIgnoreCase("SV")
		/* || language.equalsIgnoreCase("SWEDISH") */) {// 瑞典语
			result = "SV";
		} else if (lan.equalsIgnoreCase("CS")
		/* || language.equalsIgnoreCase("CZECH") */) {// 捷克语
			result = "CS";
		} else {// 英语
			result = "EN";
		}
		return result;
	}
	public  static String getStandardLocale(String str) {
		switch (str.toLowerCase()) {
			case "zh":
			case "zh_cn":
				if (getSystemCountry().equalsIgnoreCase("cn"))
					return "CN";
				else
					return "HK";
			case "zh_tw":
			case "zh_hk":
				return "HK";

			case "en":
				return "EN";
			case "es":
				return "ES";
			case "it":
				return "IT";
			case "ja":
				return "JA";
			case "ko":
				return "KO";
			case "de":
				return "DE";
		}
		return "EN";
	}
	public static String getSystemCountry() {
		return Locale.getDefault().getCountry();
	}

	/**
	 * 
	 * 把国家语言转换为接口中对应的语言id
	 * @param lanName
	 * @return
	 */
	public static int getLanId(String lanName) {

		int lanId = 1001;
		if (lanName.equalsIgnoreCase("CN")
		/* || language.equalsIgnoreCase("CHINESE") */) {
			lanId = 1002;
			return lanId;
		} else if (lanName.equalsIgnoreCase("JP")
		/* || language.equalsIgnoreCase("JAPANESE") */) {// 日语
			lanId = 2;
			return lanId;
		} else if (lanName.equalsIgnoreCase("DE")
		/* || language.equalsIgnoreCase("GERMAN") */) {// 德语
			lanId = 1;
			return lanId;
		} else if (lanName.equalsIgnoreCase("HK")
				|| lanName.equalsIgnoreCase("TW")
		/*
		 * || language.equalsIgnoreCase("HONGKONG") ||
		 * language.equalsIgnoreCase("TAIWAN")
		 */) {// 中国(香港)
			lanId = 221;
			return lanId;
		} else if (lanName.equalsIgnoreCase("FR")
		/* || language.equalsIgnoreCase("FRENCH") */) {// 法语
			lanId = 4;
			return lanId;
		} else if (lanName.equalsIgnoreCase("PT")|| lanName.equalsIgnoreCase("BR")
		/* || language.equalsIgnoreCase("PORTUGUESE") */) {// 葡萄牙
			lanId = 6;
			return lanId;
		} else if (lanName.equalsIgnoreCase("RU")
		/* || language.equalsIgnoreCase("RUSSIAN") */) {// 俄罗斯
			lanId = 3;
			return lanId;
		} else if (lanName.equalsIgnoreCase("IT")
		/* || language.equalsIgnoreCase("ITALIAN") */) { // 意大利
			lanId = 1003;
			return lanId;
		} else if (lanName.equalsIgnoreCase("ES")
		/* || language.equalsIgnoreCase("SPAINISH") */) {// 西班牙
			lanId = 5;
			return lanId;
		} else if (lanName.equalsIgnoreCase("PL")
		/* || language.equalsIgnoreCase("POLISH") */) {// 波兰
			lanId = 7;
			return lanId;
		} else if (lanName.equalsIgnoreCase("TR")
		/*
		 * || language.equalsIgnoreCase("TURKEY") ||
		 * language.equalsIgnoreCase("TURKISH")
		 */) {// 土耳其
			lanId = 8;
			return lanId;
		} else if (lanName.equalsIgnoreCase("NL")
		/* || language.equalsIgnoreCase("DUTCH") */) {// 荷兰语
			lanId = 9;
			return lanId;
		} else if (lanName.equalsIgnoreCase("GR")
		/* || language.equalsIgnoreCase("GREECE") */) {// 希腊
			lanId = 10;
			return lanId;
		} else if (lanName.equalsIgnoreCase("HU")
		/* || language.equalsIgnoreCase("HUNGARIAN") */) {// 匈牙利语
			lanId = 11;
			return lanId;
		} else if (lanName.equalsIgnoreCase("AR") || lanName.equalsIgnoreCase("EG")
		/* || language.equalsIgnoreCase("ARABIC") */) {// 阿拉伯语
			lanId = 12;
			return lanId;
		} else if (lanName.equalsIgnoreCase("DA")
		/* || language.equalsIgnoreCase("DANISH") */) {// 丹麦语
			lanId = 13;
			return lanId;
		} else if (lanName.equalsIgnoreCase("FA")
		/* || language.equalsIgnoreCase("PERSIAN") */) {// 波斯语
			lanId = 15;
			return lanId;
		} else if (lanName.equalsIgnoreCase("KO")|| lanName.equalsIgnoreCase("KR")
		/* || language.equalsIgnoreCase("KOREAN") */) {// 韩语
			lanId = 14;
			return lanId;
		} else if (lanName.equalsIgnoreCase("FI")
		/* || language.equalsIgnoreCase("FINNISH") */) {// 芬兰语
			lanId = 18;
			return lanId;
		} else if (lanName.equalsIgnoreCase("SV")
		/* || language.equalsIgnoreCase("SWEDISH") */) {// 瑞典语
			lanId = 19;
			return lanId;
		} else if (lanName.equalsIgnoreCase("CS")
		/* || language.equalsIgnoreCase("CZECH") */) {// 捷克语
			lanId = 20;
			return lanId;
		} else if (lanName.equalsIgnoreCase("RO")) {//罗马尼亚
			lanId = 16;
			return lanId;
		} else if (lanName.equalsIgnoreCase("SR")) {//塞尔维亚
			lanId = 17;
			return lanId;
		} else {// 英语
			lanId = 1001;
			return lanId;
		}  
	}

	/**
	 * 获取当前语言对应的编号，用于动态库语言的查询
	 * 
	 * @param language
	 * @return
	 */
	public static int languages(String language) {

		int lan = 0;
		if (language.equalsIgnoreCase("CN")
		/* || language.equalsIgnoreCase("CHINESE") */) {
			lan = 1;
			return lan;
		} else if (language.equalsIgnoreCase("JP")
		/* || language.equalsIgnoreCase("JAPANESE") */) {// 日语
			lan = 2;
			return lan;
		} else if (language.equalsIgnoreCase("DE")
		/* || language.equalsIgnoreCase("GERMAN") */) {// 德语
			lan = 3;
			return lan;
		} else if (language.equalsIgnoreCase("HK")
				|| language.equalsIgnoreCase("TW")
		/*
		 * || language.equalsIgnoreCase("HONGKONG") ||
		 * language.equalsIgnoreCase("TAIWAN")
		 */) {// 中国(香港)
			lan = 4;
			return lan;
		} else if (language.equalsIgnoreCase("FR")
		/* || language.equalsIgnoreCase("FRENCH") */) {// 法语
			lan = 5;
			return lan;
		} else if (language.equalsIgnoreCase("PT")|| language.equalsIgnoreCase("BR") 
		/* || language.equalsIgnoreCase("PORTUGUESE") */) {// 葡萄牙
			lan = 6;
			return lan;
		} else if (language.equalsIgnoreCase("RU")
		/* || language.equalsIgnoreCase("RUSSIAN") */) {// 俄罗斯
			lan = 7;
			return lan;
		} else if (language.equalsIgnoreCase("IT")
		/* || language.equalsIgnoreCase("ITALIAN") */) { // 意大利
			lan = 8;
			return lan;
		} else if (language.equalsIgnoreCase("ES")
		/* || language.equalsIgnoreCase("SPAINISH") */) {// 西班牙
			lan = 9;
			return lan;
		} else if (language.equalsIgnoreCase("PL")
		/* || language.equalsIgnoreCase("POLISH") */) {// 波兰
			lan = 10;
			return lan;
		} else if (language.equalsIgnoreCase("TR")
		/*
		 * || language.equalsIgnoreCase("TURKEY") ||
		 * language.equalsIgnoreCase("TURKISH")
		 */) {// 土耳其
			lan = 11;
			return lan;
		} else if (language.equalsIgnoreCase("NL")
		/* || language.equalsIgnoreCase("DUTCH") */) {// 荷兰语
			lan = 12;
			return lan;
		} else if (language.equalsIgnoreCase("GR")
		/* || language.equalsIgnoreCase("GREECE") */) {// 希腊
			lan = 13;
			return lan;
		} else if (language.equalsIgnoreCase("HU")
		/* || language.equalsIgnoreCase("HUNGARIAN") */) {// 匈牙利语
			lan = 14;
			return lan;
		}/* else if (language.equalsIgnoreCase("AR") || language.equalsIgnoreCase("EG")
		 || language.equalsIgnoreCase("ARABIC") ) {// 阿拉伯语
			lan = 15;
			return lan;
		} */else if (language.equalsIgnoreCase("DA")
		/* || language.equalsIgnoreCase("DANISH") */) {// 丹麦语
			lan = 18;
			return lan;
		} else if (language.equalsIgnoreCase("FA")
		/* || language.equalsIgnoreCase("PERSIAN") */) {// 波斯语
			lan = 19;
			return lan;
		} else if (language.equalsIgnoreCase("KO") || language.equalsIgnoreCase("KR")
		 || language.equalsIgnoreCase("KOREAN") ) {// 韩语

			lan = 20;
			return lan;
		} else if (language.equalsIgnoreCase("FI")
		/* || language.equalsIgnoreCase("FINNISH") */) {// 芬兰语
			lan = 21;
			return lan;
		} else if (language.equalsIgnoreCase("SV")
		/* || language.equalsIgnoreCase("SWEDISH") */) {// 瑞典语
			lan = 22;
			return lan;
		} else if (language.equalsIgnoreCase("CS")
		/* || language.equalsIgnoreCase("CZECH") */) {// 捷克语
			lan = 23;
			return lan;
		} else {// 英语
			lan = 0;

			return lan;
		}
	}
	
	
	/**
	 * 
	 * 把国家语言转换为接口中对应的语言id
	 * @param lanName
	 * @return
	 */
	public static String getIdToLanName(int lanId) {
		String lanName="EN";
		if (lanId==1002) {
			lanName = "CN";
			return lanName;
		}else if (lanId==2) {
			lanName = "JP";
			return lanName;
		}else if (lanId==1) {
			lanName = "DE";
			return lanName;
		}else if (lanId==221) {
			lanName = "TW";//HK
			return lanName;
		}else if (lanId==4) {
			lanName = "FR";
			return lanName;
		}else if (lanId==6) {
			lanName = "PT";
			return lanName;
		}else if (lanId==3) {
			lanName = "RU";
			return lanName;
		}else if (lanId==1003) {
			lanName = "IT";
			return lanName;
		}else if (lanId==5) {
			lanName = "ES";
			return lanName;
		}else if (lanId==7) {
			lanName = "PL";
			return lanName;
		}else if (lanId==8) {
			lanName = "TR";
			return lanName;
		}else if (lanId==9) {
			lanName = "NL";
			return lanName;
		}else if (lanId==10) {
			lanName = "GR";
			return lanName;
		}else if (lanId==11) {
			lanName = "HU";
			return lanName;
		}else if (lanId==12) {
			lanName = "AR";//EG
			return lanName;
		}else if (lanId==13) {
			lanName = "DA";
			return lanName;
		}else if (lanId==15) {
			lanName = "FA";
			return lanName;
		}else if (lanId==14) {
			lanName = "KO";
			return lanName;
		}else if (lanId==18) {
			lanName = "FI";
			return lanName;
		}else if (lanId==19) {
			lanName = "SV";
			return lanName;
		}else if (lanId==20) {
			lanName = "CS";
			return lanName;
		}else if (lanId==16) {
			lanName = "RO";
			return lanName;
		}else if (lanId==17) {
			lanName = "SR";
			return lanName;
		}else{
			return lanName;
		}
	}



}
