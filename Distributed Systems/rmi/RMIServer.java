/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {

	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (receivedMessages == null) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[msg.totalMessages];
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		// any missing messages
		if (msg.messageNum >= msg.totalMessages - 1) {
			int msg_missing = 0;

			for (int i = 0; i < totalMessages; i++) {
				if (receivedMessages[i] != 1) {
					System.out.println("Missing message: " + (i + 1));
					msg_missing++;
				}
			}

			System.out.println("Total sent messages: " + msg.totalMessages);
			System.out.println("Total lost messages: " + msg_missing);
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		String urlServer = new String("rmi://" + "localhost" + "/RMIServer");

		try {
			// TO-DO: Instantiate the server class
			rmis = new RMIServer();

			// TO-DO: Bind to RMI registry
			// LocateRegistry.getRegistry(1099);
			rebindServer(urlServer, rmis);
			System.out.println("RMIServer bound");
		} catch (Exception e) {
			System.err.println("RMIServer exception:" + e.toString());
			System.exit(-1);
		}

		System.out.println("Server ready");
	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO: Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we
		// could skip this (eg run rmiregistry in the start script)
		try {
			LocateRegistry.createRegistry(1099);
		} catch (Exception e) {
			System.out.println("Port already in use");
		}

		try {
			Naming.bind(serverURL, server);
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}

		// TO-DO: // Now rebind the server to the registry (rebind replaces any existing
		// servers
		// bound to the serverURL) // Note - Registry.rebind (as returned by
		// createRegistry / getRegistry) does something similar but
		// expects different things from the URL field. registry.rebind(serverURL,
		// server);
	}
}
