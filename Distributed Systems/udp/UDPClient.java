/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;

	public UDPClient() {
		// TO-DO: Initialise the UDP socket for sending data
		try {
			sendSoc = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Socket exception: " + e.getMessage());
		}

		System.out.println("UDPClient ready\n");
	}

	public static void main(String[] args) {
		InetAddress serverAddr = null;
		int recvPort;
		int countTo;
		String message;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);

		// TO-DO: Construct UDP client class and try to send messages
		UDPClient client = new UDPClient();

		// TO-DO: Send the messages to the server
		for (int tries=0; tries<countTo; tries++) {
			MessageInfo msg = new MessageInfo(countTo, tries);
			client.send(msg.toString(), serverAddr, recvPort);
			System.out.println("Sent: " + msg.toString());
		}

		System.out.println("\nUDPClient tested");
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int payloadSize;
		byte[] pktData;
		DatagramPacket pkt;

		// TO-DO: build the datagram packet and send it to the server
		try {
			pktData = payload.getBytes();
			payloadSize = pktData.length;
			pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
			sendSoc.send(pkt);
		} catch (Exception e) {
			System.out.println("IO exception: " + e.getMessage());
		}
	}
}
