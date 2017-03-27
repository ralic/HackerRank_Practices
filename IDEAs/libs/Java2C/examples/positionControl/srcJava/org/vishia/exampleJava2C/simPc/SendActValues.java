package org.vishia.exampleJava2C.simPc;

import org.vishia.communication.Address_InterProcessComm;
import org.vishia.communication.Address_InterProcessComm_Socket;
import org.vishia.communication.InspcDataExchangeAccess;
import org.vishia.communication.InterProcessComm;
import org.vishia.communication.InterProcessCommFactory;
import org.vishia.communication.InterProcessCommFactoryAccessor;
import org.vishia.exampleJava2C.java4c.OamVariables;

/**This class sends actual values to a Operation-and-Monitoring system via UDP-telegrams
 * using the Inspector-datagram-definition.
 * <br><br>
 * The data will be accumulated in a buffer of 1400 byte (one UDP-datagram). If the OaM-System
 * sends a request telegram, the buffer will be sent to the requestor's address. If no request 
 * is received, nothing will be sent.
 * <br><br>
 * The class builds a receive-thread in an anonymous private class which waits for the UDP-telegrams
 * from the OaM-system.
 * @author Hartmut Schorrig
 *
 */
public class SendActValues
{
	/**It is the accessor class to 
	 * 
	 */
	private final OamVariables.OamVariablesByteAccess oamVariableAccess = 
		new OamVariables.OamVariablesByteAccess();
	
	/**Set while the receiver thread runs. If it is set to false, the receiver thread will be finished. */
	private boolean rxRun;
	
	/**A request for data sending is pending. It is set if a datagram from any source is received. */
	private boolean bSendReq;
	
	private boolean bSent;
	
	/**Counts the order-identifier-number for sending.
	 */
	private int orderSend;
	
	/**Buffer to prepare the data to send. */
	private final byte[] valueBuffer = new byte[1400];
	
	private final static int recordsize = InspcDataExchangeAccess.Info.sizeofHead 
	                                    + OamVariables.OamVariablesByteAccess.sizeofHead;
	
	private final InspcDataExchangeAccess.Datagram datagram = new InspcDataExchangeAccess.Datagram();
	
	private final InspcDataExchangeAccess.Info infoHead = new InspcDataExchangeAccess.Info();
	
	InterProcessComm udpSocket = InterProcessCommFactoryAccessor.getInstance().create("UDP:0.0.0.0:60083");  
	
	Address_InterProcessComm dstAddress = new Address_InterProcessComm_Socket();
	
	//
	SendActValues()
	{ datagram.setBigEndian(true);
		datagram.assignEmpty(valueBuffer);
		int ix = 345;
    String test = "test" +   (ix+5);
  }
	
	
	void start(){
		//Address_InterProcessComm ownAddress = InterProcessCommFactory.createAddress("Socket:0.0.0.0:60083");
		InterProcessComm udpSocket1 = udpSocket;  //Note: For java2c: build Mtbl-reference
		int success = udpSocket1.open(null, true); //InterProcessComm.receiverShouldbeBlocking);
		if(success < 0){
			throw new IllegalArgumentException("Error on open:" + udpSocket1.translateErrorMsg(success));
		}
		
		rxRun = true;
		rxThread.start();
	}
	
	
	/**Writes the values in the buffer and sends the datagram, if sending is requested.
	 * This routine should be called in any cycle-time if a high-resolved value-curve should be 
	 * captured and shown in the OaM-system. Because there are less variables, any call uses only
	 * 16 byte. So about 80 cycles can be stored in one telegram.
	 * <br><br>
	 * Another variant of writing method may be build middle- and max- and min-values in any cycle
	 * to send only this accumulated informations in one datagram.
	 *   
	 * @param way process value
	 * @param current
	 */
	public void write(OamVariables oamVariables){
    int actSize = datagram.getLengthTotal();
		assert(actSize - recordsize < valueBuffer.length);
	  try{
			datagram.addChild(infoHead);
			oamVariableAccess.addToAndSetBinData(infoHead, oamVariables);
			infoHead.setInfoHead(infoHead.getLength(), 0xaaaa, ++orderSend);
	  } catch(IllegalArgumentException exc){} //do nothing, the buffer is enough.
	  actSize = datagram.getLengthTotal();
	  if(bSendReq){
	  	//the last: send it.
	  	datagram.setLengthDatagram(actSize);
	  	InterProcessComm udpSocket1 = udpSocket;  //Note: For java2c: build Mtbl-reference
			udpSocket1.send(valueBuffer, actSize, dstAddress);
	  	bSendReq = false;
			bSent = true;
	  }
	  if(actSize >= valueBuffer.length - recordsize ||bSent){
	  	bSent = false;
	  	try{ datagram.assignData(valueBuffer, 0);}
	  	catch(IllegalArgumentException exc){} //do nothing, the buffer is enough.
	  }
	}	
	
	
	/**This thread is used to receive a trigger, after them values are sent. */
	private Thread rxThread = new Thread()
	{
		/**@java2c=simpleArray. Because: Only 1 Element is used. */
		final int[] nrofBytes = new int[1];
		
		/**A small buffer, because there are no more information to receive. */
		final byte[] rxBuffer = new byte[100];
		
		/**Run-method to receive. It overrides the Thread.run().
		 */
		@Override public void run(){
			InterProcessComm udpSocket1 = udpSocket;  //Note: For java2c: build Mtbl-reference
			while(rxRun){
				nrofBytes[0] = 0;
				udpSocket1.receiveData(nrofBytes, rxBuffer, dstAddress);  ////
				int success = nrofBytes[0];
				if(success >= 0){
					bSendReq = true;
					//bSent = false;
				}
			}
		}
	};

}
