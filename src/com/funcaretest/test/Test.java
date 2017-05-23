package com.funcaretest.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
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

import com.funcaretest.domain.UserInfo;
import com.funcaretest.network.ClientManager;
import com.funcaretest.service.impl.LoginServiceImpl;
import com.funcaretest.service.impl.LoginServiceImpl.LoginServiceImplInstance;
import com.funcaretest.util.Constant;
import com.funcaretest.util.PublicTools;

public class Test {

	private static ClientManager clientManager = ClientManager.getInstance();

	public static void login(String username, String password) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(username);
		userInfo.setPassword(password);
		LoginServiceImpl loginServiceImpl = LoginServiceImplInstance.loginServiceImpl;
		loginServiceImpl.login(userInfo);
	}

	public static void initSocket() {
		clientManager.initSocket();

		while (true) {
			try {
				Thread.sleep(3000);
				if (clientManager.isConnect()) {
					System.out.println("Test -> initSocket isConnect:" + true);
					break;
				} else {
					System.out.println("Test -> initSocket isConnect:" + false);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void testLogin() {
		initSocket();
		if (clientManager.isConnect()) {
//			for (int i = 0; i < 1; i++) {
//				String username = "";
//				String password = "";
//				login(username, password);
//			}
			System.out.println("listener size = "
					+ clientManager.reveicerControl.getResponseObservableSize());
		} else {
			System.out.println("isCoonect:" + false);
		}
	}

	public static void main(String[] args) {
		// String path = Constant.LOGIN_CONFIG_XML_PATH;
		// String root = Constant.LOGIN_CONFIG_XML_ROOT;
		// System.out.println(path);
		// PublicTools.writeProperties("ownTEID","862498028752106",path);
		// PublicTools.removePropertiesKey("username", path);
		// PublicTools.removePropertiesKey("password", path);
		// PublicTools.removePropertiesKey("ownTEID", path);
		// PublicTools.displayProperties(path);
		// putXMLElement(path, "ownTEID", "862498028752106");
		// PublicTools.updateXMLElement(path, "123456", "862498028752106");
		// PublicTools.deleteXMLElement(path, "ownTEID");
		// PublicTools.addXMLElement(path,"ownTEID","862498028752106");
		// Map<Object,Object> map = PublicTools.getXMLInfo(path);
		// PublicTools.displayMap(map);
		// PublicTools.addXMLElement(path, "strTEID", "862498028752106");
		// PublicTools.deleteXMLElement(path, "strTEID");
//		testLogin();
		initSocket();
	}

}
