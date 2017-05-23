package com.funcaretest.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.funcaretest.test.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PublicTools {

	/** ByteBuffer转byte */
	public static byte[] byteBufferToByte(ByteBuffer byteBuffer) {
		byte[] datas = null;
		int length = byteBuffer.capacity();
		if (length <= 0) {
			return null;
		}
		if (byteBuffer.hasArray()) {
			datas = byteBuffer.array();
		}
		return datas;
	}

	/** ByteBuf转byte */
	public static byte[] byteBufToByte(ByteBuf byteBuf) {
		byte[] datas = null;
		int length = byteBuf.capacity();
		if (length <= 0) {
			return null;
		}
		if (byteBuf.hasArray()) {
			datas = byteBuf.array();
		}
		return datas;
	}

	/** byte转ByteBuffer */
	public static ByteBuffer byteToByteBuffer(byte[] datas) {
		ByteBuffer byteBuffer = null;
		if (datas == null) {
			return null;
		}
		int length = datas.length;
		if (length <= 0) {
			return null;
		}

		byteBuffer = ByteBuffer.allocate(length);
		byteBuffer = ByteBuffer.wrap(datas, 0, length);
		return byteBuffer;
	}

	/** byte转ByteBuf */
	public static ByteBuf byteToByteBuf(byte[] datas) {
		ByteBuf byteBuf = null;
		if (datas == null) {
			return null;
		}
		int length = datas.length;
		if (length <= 0) {
			return null;
		}

		byteBuf = Unpooled.copiedBuffer(datas);
		// 或者使用如下方式
		// byteBuf = Unpooled.buffer(length);
		// byteBuf.readBytes(datas);

		return byteBuf;
	}

	/** Bytebuf转ByteBuffer (也可以通过先转成字节 再转成ByteBuffer) */
	public static ByteBuffer bytebufToByteBuffer(ByteBuf byteBuf) {
		ByteBuffer byteBuffer = null;
		if (byteBuf == null) {
			return null;
		}
		ByteBuf tempByteBuf = byteBuf.copy();// 重新复制一个ByteBuf
		byteBuffer = tempByteBuf.nioBuffer();
		return byteBuffer;

	}

	/** ByteBuffer转ByteBuf (也可以通过先转成字节 再转成ByteBuf) */
	public static ByteBuf bytebufferToByteBuf(ByteBuffer byteBuffer) {
		ByteBuf byteBuf = null;
		if (byteBuffer == null) {
			return null;
		}
		ByteBuffer tempByteBuffer = ByteBuffer.wrap(byteBuffer.array(), 0, byteBuffer.capacity());// byteBuffer复制成一个新的
																									// position为0
		byteBuf = Unpooled.copiedBuffer(tempByteBuffer);// tempByteBuffer转ByteBuf
		return byteBuf;

	}

	/** 显示byte数组的十六进制形式 */
	public static void displayHexBytes(byte[] datas) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < datas.length; i++) {
			stringBuffer.append(String.format("0x%02X", datas[i]));
			if (i < datas.length - 1) {
				stringBuffer.append(",");
			}
		}
		System.out.println(stringBuffer.toString());
	}
	
	/**将类似0xFF 十六进制表示的字节字符串数组  变成二进制数组 */
	public static byte[] byteStrParamsToBytes(String[] packParams){
		byte[] params = null;
		if(packParams!=null && packParams.length>0){
			params = new byte[packParams.length];
			for (int i = 0; i < packParams.length; i++) {
				int value = Integer.parseInt(packParams[i].replace("0x", ""),16);//现将16进制字节字符串 转为int 
				params[i] = (byte) (value & 0xFF);
			}
		}
		return params;
	}

	/** 显示Int的二进制形式 */
	public static void displayIntToBinaryStr(int value) {
		StringBuilder binaryStr = new StringBuilder(Integer.toBinaryString(value));
		if (binaryStr.length() < 32) {
			int lackStr = 32 - binaryStr.length();
			for (int i = 0; i < lackStr; i++) {
				binaryStr.insert(0, "0");
			}
		}

		int interval = 4;// 每隔4个显示一个空格字符
		// binaryStr.insert(1*interval+0, " ");
		// binaryStr.insert(2*interval+1, " ");
		// binaryStr.insert(3*interval+2, " ");
		// binaryStr.insert(4*interval+3, " ");
		// binaryStr.insert(5*interval+4, " ");
		// binaryStr.insert(6*interval+5, " ");
		// binaryStr.insert(7*interval+6, " ");
		for (int i = 0; i < 7; i++) {
			binaryStr.insert((i + 1) * interval + i, " ");
		}

		System.out.println(binaryStr);
	}

	/**
	 *  得到properties中的键值对
	 * @param path properties文件路径
	 * @return
	 */
	public static Map<String, String> propertiesMap(String path) {
		if (path == null) {
			throw new NullPointerException();
		}

		Map<String, String> map = null;
		InputStream inStream = null;
		Properties properties = null;
		try {
			// inStream = Test.class.getClassLoader().getResourceAsStream(Constant.LOGIN_PROPERTIES);
			inStream = new FileInputStream(path);
			properties = new Properties();
			properties.load(inStream);
			Set<?> keySet = properties.keySet();
			if (keySet == null) {
				return null;
			}
			map = new HashMap<>();
			for (Object key : keySet) {
				map.put(key.toString(), properties.getProperty(key.toString()));
			}
//			System.out.println(map.size());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 *  判断key存不存在properties文件中
	 * @param key
	 * @param path  properties文件路径
	 * @return
	 */
	public static boolean isPropertiesExistsKey(String key,String path) {
		if (path == null) {
			throw new NullPointerException();
		}
		boolean isExists = false;
		InputStream inStream = null;
		Properties properties = null;
		try {
//			inStream = Test.class.getClassLoader().getResourceAsStream(Constant.LOGIN_PROPERTIES);
			inStream = new FileInputStream(path);
			properties = new Properties();
			properties.load(inStream);
			Set<?> keySet = properties.keySet();
			if (keySet.contains(key)) {// 如果存在这个key就返回true 并移除这个key
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isExists;
	}

	/**
	 * 显示一个Properties中的所有键值对信息
	 * @param path  properties文件路径
	 */
	public static void displayProperties(String path) {
		if (path == null) {
			throw new NullPointerException();
		}
		InputStream inStream = null;
		Properties properties = null;
		try {
//			inStream = Test.class.getClassLoader().getResourceAsStream(Constant.LOGIN_PROPERTIES);
			inStream = new FileInputStream(path);
			properties = new Properties();
			properties.load(inStream);
			Set<?> keySet = properties.keySet();
			if(keySet == null || keySet.size()==0){
				System.out.println("Properties no data");
				return;
			}
			for (Object key : keySet) {
				System.out.println(key.toString() + "=" + properties.getProperty(key.toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 更新properties中的某个键值对
	 * @param key
	 * @param value
	 * @param path properties文件路径
	 */
	public static void updateProperties(String key, String value, String path){
		if (path == null) {
			throw new NullPointerException();
		}
		writeProperties(key, value, path);
	}
	
	/** 
	 * 向properties中写入键值对
	 * @param key
	 * @param value
	 * @param path properties文件路径
	 */
	public static void writeProperties(String key, String value, String path) {
		if (path == null) {
			throw new NullPointerException();
		}
		OutputStream outputStream = null;
		Properties properties = null;
		try {
			properties = new Properties();
			// String path = Test.class.getClassLoader().getResource("").getPath()+Constant.LOGIN_PROPERTIES;
			// 判断这个key存在 如果就覆盖 并写上以前的键值对
			if (isPropertiesExistsKey(key,path)) {
				System.out.println("PublicTools -> writeProperties Properties key isExists = " + true);
				Map<String, String> map = propertiesMap(path);// 这一句如果写在outputStream后面会出现map为null
				outputStream = new FileOutputStream(path, false);
				Set<String> keySet = map.keySet();
				for (String keyStr : keySet) {
					if (keyStr.equals(key)) {
						properties.setProperty(key, value);
					} else {
						properties.setProperty(keyStr, map.get(keyStr));
					}
				}
				
				properties.store(outputStream, null);// 追加字段信息 和注释
			} else {
				System.out.println("PublicTools -> writeProperties Properties key isExists = " + false);// 不存在就追加
				outputStream = new FileOutputStream(path, true);// true表示追加
				// 添加之前 加一个回车换行
				outputStream.write("\r\n".getBytes());
				outputStream.flush();

				properties.setProperty(key, value);
				properties.store(outputStream, null);// 追加字段信息 和注释
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 移除指定key
	 * @param key
	 * @param path properties文件路径
	 */
	public static void removePropertiesKey(String key,String path){
		if (path == null) {
			throw new NullPointerException();
		}
		OutputStream outputStream = null;
		Properties properties = null;
		try {
			properties = new Properties();
			// String path = Test.class.getClassLoader().getResource("").getPath()+Constant.LOGIN_PROPERTIES;
			// 判断这个key存在 如果就覆盖 并写上以前的键值对
			if (isPropertiesExistsKey(key,path)) {
				System.out.println("PublicTools -> removePropertiesKey Properties key isExists = " + true);
				Map<String, String> map = propertiesMap(path);// 这一句如果写在outputStream后面会出现map为null
				outputStream = new FileOutputStream(path, false);
				Set<String> keySet = map.keySet();
				for (String keyStr : keySet) {
					if (keyStr.equals(key)) {
						//不重新setProperty
					} else {
						properties.setProperty(keyStr, map.get(keyStr));
					}
				}
				
				properties.store(outputStream, null);// 追加字段信息 和注释
			} else {
				System.out.println("PublicTools -> removePropertiesKey Properties key isExists = " + false);// 不存在就不做操作
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**显示map*/
	public static void displayMap(Map<Object,Object> map){
		if (map == null) {
			throw new NullPointerException();
		}
		Set<Object> keySet = map.keySet();
		for(Object key:keySet){
			System.out.println(key+"="+map.get(key));
		}
	}
	
	/**得到xml文件中根节点里面节点信息（内容键值对信息）*/
	public static Map<Object,Object> getXMLInfo(String path){
		Map<Object, Object> map = null;
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(path);
			map = new HashMap<>();
			Element rootElement = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> elements = rootElement.elements();
			for(Element element:elements){
				String key = element.getName();
				String value = element.getText();
				map.put(key, value);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**XML文件根节点中是否存在某个节点（键）*/
	public static boolean isExistsXMLElement(String path,String key){
		boolean isExists = false;
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(path);
			Element rootElement = document.getRootElement();
			List<Element> elements = rootElement.elements();
			for(Element element:elements){
				String eName = element.getName();
				if(eName.equals(key)){
					isExists = true;
					break;
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return isExists;
	}
	
	/**XML文件根节点中加入节点（键值对信息）*/
	public static void addXMLElement(String path,String key,String value){
		if(isExistsXMLElement(path,key)){//如果存在这个节点    就 提示存在这个节点
			System.out.println("PublicTools -> addXMLElement XML exists this element");
			return;
		}
		
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(path);
			Element rootElement = document.getRootElement();
			Element element = rootElement.addElement(key);
			element.setText(value);	
			OutputFormat format = OutputFormat.createPrettyPrint();  
	        format.setEncoding("UTF-8");  
	        XMLWriter writer = new XMLWriter(  
	                new OutputStreamWriter(new FileOutputStream(path)), format);  
	        writer.write(document);  
	        writer.close();       
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**XML文件中删除节点（键值对）*/
	public static void deleteXMLElement(String path,String key){
		
		if(!isExistsXMLElement(path,key)){
			System.out.println("PublicTools -> deleteXMLElement XML not exists this element");
			return;
		}
		
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(path);
			Element rootElement = document.getRootElement();
			List<Element> elements = rootElement.elements();
			for(Element element:elements){
				String eName = element.getName();
				if(eName.equals(key)){
					rootElement.remove(element);
					break;
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();  
	        format.setEncoding("UTF-8");  
	        XMLWriter writer = new XMLWriter(  
	                new OutputStreamWriter(new FileOutputStream(path)), format);  
	        writer.write(document);  
	        writer.close();       
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**XML文件中将某个节点数据置为空字符串（键值对）*/
	public static void clearXMLElement(String path,String key){
		updateXMLElement(path,key,"");
	}
	
	/**XML中修改节点信息（修改键值对信息）*/
	public static void updateXMLElement(String path,String key,String value){
		
		if(!isExistsXMLElement(path,key)){
			System.out.println("PublicTools -> updateXMLElement XML not exists this element");
			return;
		}
		
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(path);
			Element rootElement = document.getRootElement();
			List<Element> elements = rootElement.elements();
			for(Element element:elements){
				String eName = element.getName();
				if(eName.equals(key)){
					element.setText(value);
					break;
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();  
	        format.setEncoding("UTF-8");  
	        XMLWriter writer = new XMLWriter(  
	                new OutputStreamWriter(new FileOutputStream(path)), format);  
	        writer.write(document);  
	        writer.close();       
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**解析字段名集合与字段值集合的集合*/
	public static ArrayList<Map<String,String>> parseFiledAndValueToMaps(ArrayList<String> filedNames, ArrayList<ArrayList<String>> filedValues){
		ArrayList<Map<String,String>> mapLists = new ArrayList<>();
		if(filedNames!=null && filedValues!=null &&filedNames.size()>0&&filedValues.size()>0){
			
			int filedNameSize = filedNames.size();//字段名集合只有一组
			int filedValueSize = filedValues.size();//字段值集合 可能有多组
			
			//字段名集合长度要 等于某个字段值集合长度  （字段值集合至少有一组 这里就用第一组长度判断）
			if(filedNameSize == filedValues.get(0).size()){
				//以键值对 集合存放数据
				for(int i = 0; i < filedValueSize ; i++){
					HashMap<String,String> map = new HashMap<>();
					for(int j = 0; j < filedNameSize; j++){
						String key = filedNames.get(j);
						String value = filedValues.get(i).get(j);
						value = value.trim();//去掉字符串后空字符
						map.put(key, value);
					}
					mapLists.add(map);
				}
				
			}else{
				//字段名与字段值集合不匹配
				System.out.println("PublicTools -> parseLoginReadyData filedNames and filedValues do not match");
				return null;
			}
		}else{
			//字段名集合或 字段值集合的集合 是空的
			System.out.println("PublicTools -> parseLoginReadyData filedNames is null or filedValues is null");
			return null;
		}
		
		return mapLists;
	}

	/**显示键值对数据内容*/
	public static void displayFiledAndValueToMaps(ArrayList<Map<String,String>> mapLists){
		if(mapLists == null){
			throw new NullPointerException();
		}
		
		if(mapLists.size()==0){
			System.out.println("PublicTools -> displayFiledAndValueToMaps mapLists.size() = 0");
			return;
		}
		
		int size = mapLists.size();
		for(int i = 0 ;i < size; i++){
			Map<String,String> map = mapLists.get(i);
			System.out.println("map = "+map+" : ");
			Set<String> keys = map.keySet();
			for(String key:keys){
				System.out.print(key+" = " + map.get(key) +",");
			}
			System.out.println();
		}
	}
}
