/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.*;

import java.net.MalformedURLException;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private double start;

	public RMIServer() throws RemoteException {
		super();
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages == -1){
			receivedMessages = new int[msg.totalMessages];
			totalMessages = msg.totalMessages;
			System.out.println("First Message Received! Message Content: " + msg.toString());
			start = System.nanoTime();
		}
		else{
			System.out.println("Message Received! Message Content: " + msg.toString());
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = msg.messageNum + 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == totalMessages - 1){
			System.out.println("Last Message Received!");
			totalMessages = -1;

			int missingmsg = 0;

			for(int i=0; i<msg.totalMessages; i++){
				if(receivedMessages[i] == 0){
					missingmsg = missingmsg + 1;
				}
			}

			double time = (System.nanoTime()-start)/1000000;
			time = Math.round(time*1000d)/1000d;

			System.out.println("\nTotal messages: " + msg.totalMessages);
			System.out.println("Received messages: " + (msg.totalMessages - missingmsg));
			System.out.println("Number of Missing Messages: " + missingmsg);
			System.out.println("Time taken: " + time + "ms");
			System.out.println("Testing completed");
		}

	}

	public static void main(String[] args) {

		RMIServer rmis = null;
		if(System.getSecurityManager() == null){
			System.setSecurityManager (new RMISecurityManager ());
		}

		// TO-DO: Initialise Security Manager
		try{
			RMIServer s = new RMIServer();
			// TO-DO: Instantiate the server class

			// TO-DO: Bind to RMI registry
			rebindServer("rmi://localhost/RMIServer", s);
		}
		catch(Exception e){
			System.out.println("Trouble: " + e);
		}

	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		try{
			//Registry r = LocateRegistry.createRegistry(4567);
			Naming.rebind(serverURL, server);
		}
		catch(Exception e){
			System.out.println("Horrible: " + e);
		}

	}
}
