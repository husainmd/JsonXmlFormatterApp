package com.husain.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Beautifier {

	public static String formatAndBeautifyJSon(String jsonString) throws Exception
	{
		String[] strArr = jsonString.split("\n");
		jsonString="";
		for(String a : strArr)
		{
			jsonString += a.trim();
		}
		
		jsonString = jsonString.replace("\\n", "").replace("\\", "");	//Remove junk characters
		StringBuffer sbuf = new StringBuffer();
		char currentChar, nextChar;		
		for(int i=0; i<jsonString.length(); i++)
		{
			currentChar = jsonString.charAt(i);
			sbuf.append(currentChar);
			if(currentChar==','||currentChar=='{')
				sbuf.append("\n");			
			if(i < jsonString.length()-1)
			{
				nextChar = jsonString.charAt(i+1);
				if(nextChar=='}')
					sbuf.append("\n");
			}		
		}		
		String[] allLinesArray = sbuf.toString().split("\n");
		ArrayList<String> allLines = new ArrayList<String>();		
		for(int i=0; i<allLinesArray.length; i++)
		{
			try
			{
				allLines.add(allLinesArray[i].trim());
			}
			catch(Exception e){
				allLines.add(allLinesArray[i]);
			}
		}
		String strd="", newStrd="", previousLine="",currentLine="";
		int noOfTabs=0;
		for(int i=0; i<allLines.size(); i++)
		{
			strd="";previousLine="";currentLine="";
			if(i>0)
				previousLine = allLines.get(i-1);
			currentLine = allLines.get(i);
			if(previousLine.contains("{"))
				noOfTabs++;
			if(currentLine.contains("}"))
				noOfTabs--;
			for(int j=0; j<noOfTabs; j++)
				strd = "            "+strd;											//Space
			newStrd = strd + allLines.get(i);			
			allLines.set(i, newStrd);
		}
		sbuf = new StringBuffer();
		for(String a : allLines)
			sbuf.append(a+"\n");		
		return sbuf.toString();
	}
	
	public static String beautifyXML(String xmlString) throws Exception
	{
		StringBuffer sbuf = new StringBuffer();		
		for(int i=0; i<xmlString.length();i++)
		{
			sbuf.append(xmlString.charAt(i));
			if(i<xmlString.length()-1)
			{
				if(xmlString.charAt(i)=='>' && xmlString.charAt(i+1)=='<')
					sbuf.append("\n");
			}
		}		
		String[] allLinesArray = sbuf.toString().split("\n");		
		ArrayList<String> allTags = new ArrayList<String>();
		ArrayList<String> allLines = new ArrayList<String>();
		for(String singleLine : allLinesArray)
		{
			if(!singleLine.trim().equals(""))
			{
				allLines.add(singleLine.trim());
				allTags.add(getTagName(singleLine.trim()));
			}
		}			
		int noOfTabs=0;
		String newStr;		
		for(int i=0; i<allLines.size(); i++)
		{
			newStr="";
			for(int j=0; j<noOfTabs; j++)
				newStr = "            "+newStr;										//Space
			newStr = newStr+allLines.get(i);
			allLines.set(i, newStr);
			if(isOnlyStartedOnSameLine(allLines.get(i)))
				noOfTabs++;
			else if(isOnlyClosedOnSameLine(allLines.get(i)))
			{
				noOfTabs--;
				newStr="";
				for(int j=0; j<noOfTabs; j++)
					newStr = "            "+newStr;									//Space
				newStr = newStr+(allLines.get(i).replace("            ", ""));		//Space
				allLines.set(i, newStr);
			}
		}		
		sbuf = new StringBuffer();
		for(String s : allLines)
			sbuf.append(s+"\n");
		return sbuf.toString();
	}
	
	public static boolean isOnlyClosedOnSameLine(String line) throws Exception
	{
		String tagName = getTagName(line);
		if((!line.contains("<"+tagName)) && (line.contains("</"+tagName)) && (!line.contains("/>")))
			return true;
		else
			return false;
	}
	
	public static boolean isOnlyStartedOnSameLine(String line) throws Exception
	{
		String tagName = getTagName(line);
		if(line.contains("<"+tagName) && (!line.contains("</"+tagName)) && (!line.contains("/>")))
			return true;
		else
			return false;
	}
	
	public static boolean isStartedAndClosedOnSameLine(String line) throws Exception
	{
		String tagName = getTagName(line);
		if(line.contains("<"+tagName) && line.contains("</"+tagName))
			return true;
		else if(line.contains("<"+tagName) && line.contains("/>"))
			return true;
		else
			return false;
	}
	
	public static String getTagName(String str) throws Exception
	{
		String[] strArr = str.split("<");
		str = strArr[1];
		StringBuffer sbuf = new StringBuffer();
		int i=0;
		while(true)
		{
			if(str.charAt(i)==' ' || str.charAt(i)=='>')
				break;
			sbuf.append(str.charAt(i));
			i++;
		}
		str = sbuf.toString();
		return str.replace("/", "");
	}
	
	public static String formatXML(String str) throws Exception
	{
		//Edited Nov 11
		String[] strArr = str.split("\n");
		str="";
		for(String a : strArr)
		{
			str += a.trim();
		}
		
		String[] str1 = str.split("<");
		if(!str1[0].contains("xml"))
			str = str.replace(str1[0], "");
		str = str.replace("\\", "");
		str1 = str.split(">");
		if(str1[str1.length-1].contains("consumerName"))
			str = str.replace(str1[str1.length-1], "");
		return str;
	}
	
	public static void main(String args[]) throws Exception
	{		
		BufferedReader br = new BufferedReader(new FileReader("src\\main\\resources\\dummy.txt"));
		StringBuffer sbuf1 = new StringBuffer();
		String cfile="";
		while((cfile=br.readLine())!=null)
			sbuf1.append(cfile);		
		String str = sbuf1.toString();
		System.out.println(formatXML(str));
		System.out.println("\n\n\n\n");		
		System.out.println(beautifyXML(formatXML(str)));	
		br.close();
	}

}
