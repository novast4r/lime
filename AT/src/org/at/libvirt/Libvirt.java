package org.at.libvirt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.at.settings.Host;
import org.at.settings.HostsSettings;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.DomainJobInfo;
import org.libvirt.LibvirtException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Libvirt {
	
	private HostsSettings s = new HostsSettings();
	
	
	public static Connect getHypervisorConnection(Host h) {
		try {
			return new Connect("qemu+ssh://"+h.getName()+"@"+h.getHostname()+"/system?no_tty=1", false); //+"/system?no_tty=1",false);
		} catch (LibvirtException e) {e.printStackTrace();}
		return null;
	}
	
	public static Domain migrate(Domain src, Connect dst) {
		try {
			return src.migrate(dst, 1, null,null, 0);
		} catch (LibvirtException e) {e.printStackTrace();}	
		return null;
	}
	
	public String getNameByMacAddress(String macTarget) {
		try {
			for(Host host : s.getHosts()) {
				Connect conn = getHypervisorConnection(host);
				String[] domains = conn.listDefinedDomains();
				
				Domain d=null;
				for(String domain : domains) {
					d = conn.domainLookupByName(domain);
					String conf = d.getXMLDesc(0);
					if(conf.contains(macTarget.toLowerCase())){
						String s=d.getName();
						//d.free();
						return s;
					}					
				
				}
				conn.close();

			}
		} catch (LibvirtException e) {e.printStackTrace();}
		finally{
			System.gc();
		}
		return null;
	}
	
	
	public String getMacAddressByName(String name){
		try {
			for(Host h: s.getHosts()) {
				Connect conn = getHypervisorConnection(h);
				Domain d = conn.domainLookupByName(name);
				if(d!=null) {
					String conf = d.getXMLDesc(0);
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					ByteArrayInputStream xml = new ByteArrayInputStream(conf.getBytes());
					Document doc = db.parse(xml);
					Element net = (Element)doc.getElementsByTagName("mac").item(0);
					String mac = net.getAttribute("address");
					return mac;
				}
				
				conn.close();
			}
		} catch (LibvirtException | SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		finally{
			System.gc();
		}
		return null;
	}
	
	public void getJobInfo(Domain d){
		try {
			DomainJobInfo info= d.getJobInfo();
			System.out.println("mem processed "+info.getMemProcessed()/1024/1024);
			System.out.println("mem remaining "+info.getMemRemaining()/1024/1024);
			System.out.println("mem total "+info.getMemTotal()/1024/1024);
			System.out.println("data processed "+info.getDataProcessed()/1024/1024);
			System.out.println("data remaining "+info.getDataRemaining()/1024/1024);
			System.out.println("data total "+info.getDataTotal()/1024/1024);
			System.out.println("time elapsed "+info.getTimeElapsed());
			System.out.println("time remaining "+info.getTimeRemaining());
		} catch (LibvirtException e) {e.printStackTrace();}
	}
	
}
