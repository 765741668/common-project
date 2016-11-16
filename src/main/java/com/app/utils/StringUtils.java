package com.app.utils;

public class StringUtils extends org.springframework.util.StringUtils {
	public static int isAcronym(String word)
	 {
	  for(int i = 0; i < word.length(); i++)
	  {
	   char c = word.charAt(i);
	   if (!Character.isLowerCase(c))
	   {
	    return i;
	   }
	  }
	  return -1;
	 }
	/**
	 * 在字符串大写字母前添加指定字符
	 * @param
	 * @param character
	 * @param addChar
	 * @return
	 */
	public static String addCharByIndexFront(String character,String addChar){
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<character.length();i++){
			char c=character.charAt(i);
			boolean result=Character.isLetter(c);
			if(!result){
				continue;
			}
			if(!Character.isLowerCase(c)){
				builder.append(addChar).append(Character.toLowerCase(c));
				continue;
			}
			builder.append(c);
		}
		return builder.toString();
	}
	
	public static boolean isEmpty(String...params) {
		for (String param : params) {
			if (param == null || param.equals("")||param.length()==0) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(String param) {
		if (param == null || param.equals("")||param.length()==0||param.equals("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串首字母大写
	 * @param chars
	 * @return
     */
	public static String firstCharToUpper(String chars){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(chars.substring(0,1).toUpperCase()).append(chars.substring(1));
		return stringBuilder.toString();
	}
}
