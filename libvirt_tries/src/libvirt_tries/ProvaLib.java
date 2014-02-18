package libvirt_tries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

public class ProvaLib {

	public static void main(String[] args) throws NumberFormatException, IOException {
		Connect enry_conn = null;
		Connect pasq_conn = null;
		Domain d = null;
		String enry_host ="enry@143.225.229.170";
		String pasq_host = "pasquale@192.168.1.3";//127.0.0.1";
		
		int choice = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		do{
			System.out.println("1.connect");
			System.out.println("2.migrate");
			System.out.println("3.disconnect");
			System.out.print(">");
			choice = Integer.parseInt(in.readLine());
			
			switch(choice){
			case 1:
				try {
					pasq_conn = new Connect("qemu+ssh://"+pasq_host+"/system?no_tty=yes",false);
					System.out.println("my pc connected");
					enry_conn = new Connect("qemu+ssh://"+enry_host+"/system?no_tty=yes",false);
					System.out.println("enry pc connected");
					d = pasq_conn.domainLookupByName("linx2");
				} catch (LibvirtException e) {
					e.printStackTrace();
					System.out.println("Eccezione connect: "+e.getMessage());
				}
				break;
				
			case 2:
				if(pasq_conn != null){
					System.out.println("Starting live migration from "+
							enry_host+" to "+pasq_host+"...");
					Domain d2 = null;
					
					try {
						d2 = d.migrate(enry_conn, 1, null, null, 0);
					} catch (LibvirtException e1) {
						System.out.println("Errore nella migrazione: "+e1.getMessage());
						e1.printStackTrace();
					}
					
					if(d2 != null){
						
						System.out.println("should be ok..");
					}
					
				}else
					System.out.println("initiate a connection first");
				break;
				
			case 3:
				try {
					if(enry_conn != null)
						enry_conn.close();
					if(pasq_conn != null)
						pasq_conn.close();
					System.out.println("connections closed");
				} catch (LibvirtException e) {
					System.err.println("Error closing connections");
					e.printStackTrace();
				}
				break;
			
			default:
				System.out.println("unrecognized input");
			
			}
			
			System.out.println("\n");
		}while(choice != 3);	
	}

}
