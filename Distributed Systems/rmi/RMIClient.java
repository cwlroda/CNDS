/*
 * Created on 01-Mar-2016
 */
package rmi;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.*;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		try{
			// TO-DO: Initialise Security Manager
			if(System.getSecurityManager() == null){
				System.setSecurityManager (new RMISecurityManager ());
			}

			// TO-DO: Bind to RMIServer
			//Registry r = LocateRegistry.getRegistry(4567);
			RMIServerI remobj = (RMIServerI)Naming.lookup(urlServer);

			// TO-DO: Attempt to send messages the specified number of times
			for(int i=0; i<numMessages; i++){
				MessageInfo msg = new MessageInfo(numMessages, i);
				System.out.println("Message Sent: " + msg.toString());
				remobj.receiveMessage(msg);
			}
		} catch(Exception e){
			System.out.println("Exception:" + e);
		}
	}
}
