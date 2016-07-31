package com.Steven.NkuScore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebLogger {
	boolean logined=false;
	URL url;
	MainActivity myActivity;
	String progress_str;
	String toast_str;
	int progress_int;
	/*
	List<NameValuePair>infos
	=new ArrayList<NameValuePair>(Arrays.asList(
			new BasicNameValuePair("user", "1234567"),
			new BasicNameValuePair("password", "123456"),
			new BasicNameValuePair("valicode", "1234")
	));
	*/
	List<String>infos
			=new ArrayList<String>(Arrays.asList(
			"user", "1234567",
			"password", "123456",
			"valicode", "1234"
	));
	List<String>headers
			=new ArrayList<String>(Arrays.asList(
			"Accept","text/html, application/xhtml+xml, */*",
			"Accept-Language","en-US",
			"User-Agent","Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
			"Accept-Encoding"," gzip, deflate",
			"Host","222.30.32.10",
			"DNT","1",
			"Connection","Keep-Alive"
	));
	List<String>headers2
			=new ArrayList<String>(Arrays.asList(
			"Accept","text/html, application/xhtml+xml, */*",
			"Referer","http://222.30.32.10/",
			"Accept-Language","en-US",
			"User-Agent","Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
			"Content-Type","application/x-www-form-urlencoded",
			"Accept-Encoding"," gzip, deflate",
			"Host","222.30.32.10",
			//"Content-Length","97",
			"DNT","1",
			"Connection","Keep-Alive",
			"Cache-Control","no-cache"
	));

	Bitmap valicode_bitmap;
	String res_page;
	String studentName;
	public int lenOfInfos(){
		int res=0;
		for(int i=1;i<6;i+=2){
			res+=infos.get(i).length();
		}
		return res;
	}
	public void setValue(List<String> headers,String key,String value){
		if(headers.indexOf(key)!=-1){
			headers.set(headers.indexOf(key)+1, value);
		}else{
			headers.add(key);
			headers.add(value);
		}
	}
	public void deleteValue(List<String> headers,String key){
		if(headers.indexOf(key)!=-1){
			headers.remove(headers.indexOf(key) + 1);
			headers.remove(headers.indexOf(key));
		}
	}
	public void setContentLength(int type){
		if(type==0){
			//setValue(headers2,"Content-Length",""+80+lenOfInfos());
		}else{
			//setValue(headers2,"Content-Length",""+851);
		}
	}
	public void setUser(String value){
		setValue(infos,"user",value);
	}
	public void setPassword(String value){
		setValue(infos,"password",value);
	}
	public void setValicode(String value){
		setValue(infos,"valicode",value);
	}
	public void setHeaders(HttpGet get,List<String> headers){
		for(int i=0;i<headers.size();i+=2){
			Header header=new BasicHeader(headers.get(i),headers.get(i+1));
			get.setHeader(header);
		}
	}
	public void setHeaders(HttpPost post,List<String> headers){
		for(int i=0;i<headers.size();i+=2){
			Header header=new BasicHeader(headers.get(i),headers.get(i+1));
			post.setHeader(header);
		}
	}
	public boolean reportStatus(String status,int value){
		System.out.println(status);
		progress_str=status;
		progress_int=value;
		myActivity.handler.sendEmptyMessage(0x124);
		return true;
	}
	public boolean toastStatus(String status){
		System.out.println(status);
		toast_str=status;
		myActivity.handler.sendEmptyMessage(0x126);
		return true;
	}
	public boolean reportReady(){
		myActivity.handler.sendEmptyMessage(0x125);
		return true;
	}
	public void showValicode(){

	}
	public String readAll(InputStream str){
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(str,"gb2312"));
			String line=null;
			String res_str="";
			while((line=br.readLine())!=null){
				res_str+=line;
			}
			return res_str;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	public boolean init(){
		reportStatus("正在初始化....", 10);
		try {
			//建立连接
			HttpClient client = new DefaultHttpClient();
			reportStatus("努力联网中...", 30);
			//启动计时，超时则弹出提示.
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					myActivity.handler.sendEmptyMessage(0x127);
				}
			},8000);
			HttpGet get=new HttpGet("http://222.30.32.10/ValidateCode");
			setHeaders(get,headers);
			HttpResponse response=client.execute(get);
			timer.cancel();
			reportStatus("获取Cookie....", 30);
			try{
				String cookie=response.getLastHeader("Set-Cookie").getValue();
				headers2.add("Cookie");
				headers2.add(cookie);
				headers.add("Cookie");
				headers.add(cookie);
			}catch(Exception e){
				e.printStackTrace();
			}
			reportStatus("获取验证码....", 50);
			HttpEntity entity=response.getEntity();
			InputStream is=entity.getContent();
			valicode_bitmap=BitmapFactory.decodeStream(is);
			myActivity.valicodeBitmap=valicode_bitmap;
			myActivity.handler.sendEmptyMessage(0x123);
			reportReady();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public boolean login(){
		if(logined)return true;
		//建立连接

		try {
			reportStatus("正在登录...", 0);
			HttpClient client = new DefaultHttpClient();
			HttpPost post=new HttpPost("http://222.30.32.10/stdloginAction.do");
			setContentLength(0);
			setHeaders(post, headers2);
			List<NameValuePair>loginPost
					=new ArrayList<NameValuePair>(Arrays.asList(
					new BasicNameValuePair("usercode_text",infos.get(1)),
					new BasicNameValuePair("userpwd_text",infos.get(3)),
					new BasicNameValuePair("checkcode_text",infos.get(5)),
					new BasicNameValuePair("operation",""),
					new BasicNameValuePair("submittype",new String("确 认".getBytes( "gbk" ),"ISO-8859-1"))
			));
			UrlEncodedFormEntity urlentity=new UrlEncodedFormEntity(loginPost,HTTP.ISO_8859_1);
			post.setEntity(urlentity);
			String pageContent=readAll(client.execute(post).getEntity().getContent());
			Pattern pattern=Pattern.compile("<LI>(.*)</LI>");
			Matcher matcher=pattern.matcher(pageContent);
			if(matcher.find()){
				//error message found
				reportReady();
				toastStatus(matcher.group(1));
				return false;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reportReady();
			toastStatus("客户端协议错误!");
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reportReady();
			toastStatus("输入输出错误!");
			return false;
		}
		logined=true;
		return true;
	}
	public boolean getScore(){
		if (!login()){
			init();
			return false;
		}
		res_page="";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get=new HttpGet("http://222.30.32.10/xsxk/scoreAlarmAction.do");
			setHeaders(get,headers);
			reportStatus("抱紧我莫慌...", 20);
			String pageContent=readAll(client.execute(get).getEntity().getContent());
			Pattern pattern=Pattern.compile("(<p align=\"center\">.*?</table>)");
			Matcher matcher=pattern.matcher(pageContent);
			String GPA_alarm="";
			while(matcher.find()){
				GPA_alarm+=matcher.group(0)+"\r\n<br></br>\r\n";
			}

			get=new HttpGet("http://222.30.32.10/xsxk/studiedAction.do");
			setHeaders(get,headers);
			pageContent=readAll(client.execute(get).getEntity().getContent());
			pattern=Pattern.compile("共 (.) 页");
			matcher=pattern.matcher(pageContent);
			reportStatus("获取页数...", 35);
			int pages_number=1;
			if(matcher.find()){
				pages_number=Integer.parseInt(matcher.group(1));
			}
			pattern=Pattern.compile("(\\[.*?类课.*?\\])");
			matcher=pattern.matcher(pageContent);
			String GPA_count="";
			while(matcher.find()){
				GPA_count+=matcher.group(0);
			}
			GPA_count+="<br></br>";
			reportStatus("创建HTML...", 40);
			res_page="";
			res_page+="<html>";
			res_page+="<h2 align=\"center\" style=\"font-weight:bold\">修课得分详情</h2>";
			pattern=Pattern.compile("姓名：\\w*");
			matcher=pattern.matcher(pageContent);
			if(matcher.find()){
				studentName = matcher.group(0).substring(3);
				res_page+="<p align=\"center\">";
				res_page=res_page+matcher.group(0)+"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
				//res_page+="</p>";
			}
			pattern=Pattern.compile("学号：\\d+");
			matcher=pattern.matcher(pageContent);
			if(matcher.find()){
				//res_page+="<p align=\"center\">";
				res_page+=matcher.group(0);
				res_page+="</p>";
			}
			res_page+="<p align=\"center\"> --><a href=\"#GPA\">学分绩统计</a><--</p>";
			res_page+="<p align=\"center\"> --><a href=\"#Warning\">预警信息</a><--</p>";
			res_page+=GPA_count;
			res_page+="<table bgcolor=\"#CCCCCC\" border=\"0\" cellspacing=\"2\" cellpadding=\"3\" width=\"100%\">";
			res_page+="<tr bgcolor=\"#3366CC\"><td>序号</td><td>课程代码</td><td>课程名称</td><td>课程类型</td><td>成绩</td><td>学分</td><td>重修成绩</td><td>重修情况</td></tr>";
			int page_index;
			setValue(headers, "Referer", "http://222.30.32.10/xsxk/studiedAction.do");
			for (page_index=0;page_index<pages_number;page_index++){
				reportStatus("良辰正在帮你读取第"+page_index+"页", 30);
				pattern=Pattern.compile("(<tr bgcolor=\"#FFFFFF\">(( *\t\t.*?)+?) *\t</tr>)");
				matcher=pattern.matcher(pageContent);
				while(matcher.find()){
					res_page+=matcher.group(0);
				}
				get=new HttpGet("http://222.30.32.10/xsxk/studiedPageAction.do?page=next");
				setHeaders(get, headers);
				//Log.d("zzz", pageContent);
				pageContent=readAll(client.execute(get).getEntity().getContent());
			}
			//Log.i("stevenpage",res_page);
			deleteValue(headers,"Referer");
			res_page+="</table>";
			res_page+=total()+"<a name=\"Warning\"></a> <br/><p align=\"center\" style=\"font-weight:bold\">得分预警信息</p>";
			res_page+=GPA_alarm+"</html>";
			reportStatus("正在将成绩单呈上来...", 100);
			reportReady();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public String total(){
		double score[][] = new double[100][2]; //ABC
		double scoreD[][] = new double[100][2]; //D
		double scoreE[][] = new double[50][2];//E
		int ABC = 0 , D = 0 , E = 0;
		int i = 0;
		for(i=0;i<100;i++){
			for (int p=0;p<2;p++){
				score[i][p]=0;
				scoreD[i][p]=0;
				if(i<50){
					scoreE[i][p]=0;
				}
			}
		}
		i = 0;
		Pattern pattern;
		Matcher matcher;
		pattern=Pattern.compile("<td align=\"center\" class=\"NavText\">[A-C]\\s.*?<td align=\"center\" class=\"NavText\">.*?<td align=\"center\" class=\"NavText\">.*?</td>");
		matcher=pattern.matcher(res_page);
		while (matcher.find()){
			Log.d("test",matcher.group(0));
			Pattern pattern1 = Pattern.compile("\\d+(\\.\\d+)?");
			Matcher matcher1 = pattern1.matcher(matcher.group(0));
			int j = 0;
			while (matcher1.find()) {
				Log.d("test1",matcher1.group(0));
				score[i][j]= Double.parseDouble(matcher1.group(0));
				j++;
			}
			if(j==1){
				score[i][1]=score[i][0];
				score[i][0]=-1;
			}
			i++;
		}
		ABC = i;
		i = 0;
		pattern=Pattern.compile("<td align=\"center\" class=\"NavText\">D\\s.*?<td align=\"center\" class=\"NavText\">.*?<td align=\"center\" class=\"NavText\">.*?</td>");
		matcher=pattern.matcher(res_page);
		while (matcher.find()){
			Log.d("test",matcher.group(0));
			Pattern pattern1 = Pattern.compile("\\d+(\\.\\d+)?");
			Matcher matcher1 = pattern1.matcher(matcher.group(0));
			int j = 0;
			while (matcher1.find()) {
				Log.d("test1",matcher1.group(0));
				scoreD[i][j]= Double.parseDouble(matcher1.group(0));
				j++;
			}
			if(j==1){
				scoreD[i][1]=scoreD[i][0];
				scoreD[i][0]=-1;
			}
			i++;
		}
		D = i;
		i = 0;
		pattern=Pattern.compile("<td align=\"center\" class=\"NavText\">E\\s.*?<td align=\"center\" class=\"NavText\">.*?<td align=\"center\" class=\"NavText\">.*?</td>");
		matcher=pattern.matcher(res_page);
		while (matcher.find()){
			Log.d("test",matcher.group(0));
			Pattern pattern1 = Pattern.compile("\\d+(\\.\\d+)?");
			Matcher matcher1 = pattern1.matcher(matcher.group(0));
			int j = 0;
			while (matcher1.find()) {
				Log.d("test1",matcher1.group(0));
				scoreE[i][j]= Double.parseDouble(matcher1.group(0));
				j++;
			}
			if(j==1){
				scoreE[i][1]=scoreE[i][0];
				scoreE[i][0]=-1;
			}
			i++;
		}
		E = i;

		double ABCGPA = 0 ,ABCDGPA = 0, ABCDEGPA = 0,ABCGPAF=0,ABCDGPAF = 0,ABCDEGPAF = 0;
		double ABCTOTAL = 0 , ABCDTOTAL = 0 ,ABCDETOTAL = 0 ,spe = 0;
		for(i=0;i<ABC;i++){
			if(score[i][0]>=60&&score[i][1]!=0) {
				ABCGPA += score[i][0] * score[i][1];
				ABCTOTAL += score[i][1];
			}else if(score[i][1]!=0&&score[i][0]!=-1){
				ABCGPA += 60 * score[i][1];
				ABCTOTAL+=score[i][1];
			}else if(score[i][0]==-1){
				spe+=score[i][1];
			}
		}
		if (ABCTOTAL!=0){
			ABCGPAF = ABCGPA/ABCTOTAL;
		}else ABCGPAF = 0;

		ABCDGPA += ABCGPA;
		ABCDTOTAL += ABCTOTAL;
		ABCTOTAL += spe;

		for(i=0;i<D;i++){
			if(scoreD[i][0]>=60&&scoreD[i][1]!=0) {
				ABCDGPA += scoreD[i][0] * scoreD[i][1];
				ABCDTOTAL += scoreD[i][1];
			}else if(scoreD[i][1]!=0&&scoreD[i][0]!=-1){
				ABCDGPA += 60 * scoreD[i][1];
				ABCDTOTAL+=scoreD[i][1];
			}else if(scoreD[i][0]==-1){
				spe+=scoreD[i][1];
			}
		}
		if (ABCDTOTAL!=0){
			ABCDGPAF = ABCDGPA/ABCDTOTAL;
		}else ABCDGPAF = 0;

		ABCDEGPA += ABCDGPA;
		ABCDETOTAL += ABCDTOTAL;
		ABCDTOTAL += spe;

		for(i=0;i<E;i++){
			if(scoreE[i][0]>=60&&scoreE[i][1]!=0) {
				ABCDEGPA += scoreE[i][0] * scoreE[i][1];
				ABCDETOTAL += scoreE[i][1];
			}else if(scoreE[i][1]!=0&&scoreE[i][0]!=-1){
				ABCDEGPA += 60 * scoreE[i][1];
				ABCDETOTAL+=scoreE[i][1];
			}else if(scoreE[i][0]==-1){
				spe+=scoreE[i][1];
			}
		}
		if (ABCDETOTAL!=0){
			ABCDEGPAF = ABCDEGPA/ABCDETOTAL;
		}else ABCDEGPAF = 0;
		ABCDETOTAL += spe;
		DecimalFormat df   = new DecimalFormat("######0.0000");
		String back="<p> <a name=\"GPA\"></a> </p> <br/> <p align=\"center\" style=\"font-weight:bold\">学分统计</p>";
		back+="<table bgcolor=\"#CCCCCC\" border=\"0\" width=\"100%\"> ";
		back+="<tr bgcolor=\"#3366CC\">\n" +
				"<td align=\"center\" class=\"NavText\">课程</td><td align=\"center\">总学分</td><td align=\"center\">学分绩</td>\n" +
				"</tr>";
		back+="<tr bgcolor=\"#FFFFFF\">\n" +
				"\n" +
				"<td align=\"center\" class=\"NavText\">ABC类课</td><td align=\"center\">"+ABCTOTAL+"</td><td align=\"center\">"+df.format(ABCGPAF)+"</td>\n" +
				"\n" +
				"</tr>";
		back+="<tr bgcolor=\"#FFFFFF\">\n" +
				"\n" +
				"<td align=\"center\" class=\"NavText\">ABCD类课</td><td align=\"center\">"+ABCDTOTAL+"</td><td align=\"center\">"+df.format(ABCDGPAF)+"</td>\n" +
				"\n" +
				"</tr>";
		back+="<tr bgcolor=\"#FFFFFF\">\n" +
				"\n" +
				"<td align=\"center\" class=\"NavText\">ABCDE类课</td><td align=\"center\">"+ABCDETOTAL+"</td><td align=\"center\">"+df.format(ABCDEGPAF)+"</td>\n" +
				"\n" +
				"</tr>"+"</table>";
//		back+="<p align=\"center\">ABC类课：\t"+ABCTOTAL+"学分</p>";
//		back+="<p align=\"center\">ABC类课总学分绩：\t"+df.format(ABCGPAF)+"</p>";
//		back+="<p align=\"center\">ABCD类课：\t"+ABCDTOTAL+"学分</p>";
//		back+="<p align=\"center\">ABCD类课总学分绩：\t"+df.format(ABCDGPAF)+"</p>";
//		back+="<p align=\"center\">ABCDE类课：\t"+ABCDETOTAL+"学分</p>";
//		back+="<p align=\"center\">ABCDE类课总学分绩：\t"+df.format(ABCDEGPAF)+"</p>";

		return back;
	}


	private void startActivity(Intent intent) {
		// TODO Auto-generated method stub

	}
	public boolean evaluateTeacher(){
		if (!login()){
			init();
			return false;
		}
		try {
			reportStatus("获取课程数....", 15);
			HttpClient client = new DefaultHttpClient();
			HttpGet get=new HttpGet("http://222.30.32.10/evaluate/stdevatea/queryCourseAction.do");
			setHeaders(get,headers);
			String pageContent=readAll(client.execute(get).getEntity().getContent());
			Pattern pattern=Pattern.compile("<td class=\"NavText\"><a href=\"queryTargetAction.do\\?operation=target&amp;index=(.)\">");
			Matcher matcher=pattern.matcher(pageContent);
			int course_number=0;
			while(matcher.find()){
				course_number+=1;
			}
			reportStatus("你有"+course_number+"门课程,请耐心等待", 20);
			String uri_str="http://222.30.32.10/evaluate/stdevatea/queryTargetAction.do?operation=target&index=";

			get=new HttpGet(uri_str+0);
			setHeaders(get,headers);
			pageContent=readAll(client.execute(get).getEntity().getContent());
			reportStatus("获取评价项...", 25);
			String post_value="";
			List<NameValuePair>evaluatePost
					=new ArrayList<NameValuePair>(Arrays.asList(
					new BasicNameValuePair("opinion","Good!"),
					new BasicNameValuePair("operation","Store")
			));

			pattern=Pattern.compile("<select name=\"(array\\[.*?\\])\" style=\"width:110px\"><option value=\"null\">&nbsp;</option>\t\t<option value=\"(.*?)\"");
			matcher=pattern.matcher(pageContent);
			while(matcher.find()){
				//"array[n]"="10"/"5"
				evaluatePost.add(new BasicNameValuePair(matcher.group(1),matcher.group(2)));
			}
			UrlEncodedFormEntity urlentity=new UrlEncodedFormEntity(evaluatePost,HTTP.ISO_8859_1);
			for (int idx=0;idx<course_number;idx++){
				reportStatus("正在评价第"+idx+"门课...",30);
				get=new HttpGet(uri_str+idx);
				setHeaders(get,headers);
				HttpResponse response=client.execute(get);
				setValue(headers2,"Referer",uri_str+idx);
				HttpPost post=new HttpPost("http://222.30.32.10/evaluate/stdevatea/queryTargetAction.do");
				setHeaders(post,headers2);
				post.setEntity(urlentity);
				response=client.execute(post);

			}
			deleteValue(headers2,"Referer");
			reportReady();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
}