/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.net.InetAddress;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private double start;

	private void run() throws SocketTimeoutException{
		int				pacSize;
		byte[]			pacData;
		byte[]			buffer;
		DatagramPacket 	pac;
		int rec_msg = 0;

		close = true;
		System.out.println("Server is ready\n");

		pacSize = 65508;
		pacData = new byte[pacSize];

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		try{
			while(close){
				pac = new DatagramPacket(pacData,pacSize);

				try{
					recvSoc.setSoTimeout(30000);
					recvSoc.receive(pac);

					String message = new String(pac.getData(), 0 , pac.getLength());
					System.out.println("Received: " + message.trim());
					rec_msg++;

					processMessage(message);
				} catch(SocketTimeoutException e){
					System.out.println("Messages received: " + rec_msg);
					rec_msg = 0;
				}
			}
		} catch(SocketException e){
			System.out.println("Socket exception: " + e.getMessage());
			e.printStackTrace();
		} catch(IOException e){
			System.out.println("IO exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			msg = new MessageInfo(data.trim());
		} catch(Exception e){
			System.out.println("Data exception: " + e.getMessage());
			e.printStackTrace();
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(receivedMessages == null){
			totalMessages = msg.totalMessages;
			receivedMessages = new int[msg.totalMessages];
			start = System.nanoTime();
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == (msg.totalMessages-1)){
			close = false;
			int msg_missing = 0;

			for(int i=0; i<msg.totalMessages; i++){
				if(receivedMessages[i] != 1){
					msg_missing++;
				}
			}

			double time = (System.nanoTime()-start)/1000000;
			time = Math.round(time*1000d)/1000d;

			double failed_percent = (double)msg_missing/(double)msg.totalMessages*100;
			failed_percent = Math.round(failed_percent*1000d)/1000d;

			System.out.println("\nTotal messages: " + msg.totalMessages);
			System.out.println("Received messages: " + (msg.totalMessages - msg_missing));
			System.out.println("Failed messages: " + msg_missing + " (" + failed_percent + "%)");
			System.out.println("Time taken: " + time + "ms");
			System.out.println("Testing completed");
		}
	}

	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try{
			recvSoc = new DatagramSocket(rp);
		} catch(SocketException e){
			System.out.println("Error: Socket could not be created on " + rp);
			e.printStackTrace();
			System.exit(-1);
		}

		// Make it so the server can run.
		close = true;
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if(args.length < 1){
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}

		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);

		try{
			server.run();
		} catch(SocketTimeoutException e){
			System.out.println("Socket exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

