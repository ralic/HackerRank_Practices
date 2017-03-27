package org.vishia.guiViewCfg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatPrecisionException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.vishia.bridgeC.OS_TimeStamp;
import org.vishia.bridgeC.Va_list;
import org.vishia.byteData.ByteDataAccess;
import org.vishia.mainCmd.Report;
import org.vishia.mainGui.GuiPanelMngWorkingIfc;
import org.vishia.msgDispatch.LogMessage;
import org.vishia.msgDispatch.LogMessageFile;
import org.vishia.msgDispatch.MsgDispatcher;

/**This class receives the messages from the target and writes it in the actual list and in some files.
 * 
 * @author Hartmut Schorrig
 *
 */
public class MsgReceiver 
{

	private class MsgItem
	{
		String time;
		int ident;
		char state;
		String text;
	}
	
	private final Report console;

	/**The access to the gui, to change data to show. */
	private final GuiPanelMngWorkingIfc guiAccess;
	
	/**true if the messages will be displayed. Only then it will be got from receiver. */
	private boolean bActivated = false;
	
	private boolean bListToBottom = false;
	
	private final SimpleDateFormat dateFormat;
	
	List<MsgItem> msgOfDay = new LinkedList<MsgItem>();
	
	MsgRecvComm comm = new MsgRecvComm();
	
	/**The instance of message-file-output. */
	private final LogMessage fileOutput;
	
	/**The instance for dispatching the messages. */
	private final LogMessage msgDispatcher;
	
	final MsgItems_h.MsgItems recvData;
	
	final byte[] recvDataBuffer;
	
	final int[] nrofBytesReceived = new int[1];
	
	MsgItems_h.MsgItem msgItem = new MsgItems_h.MsgItem(); 
	
	private static final int ixMsgOutputFile = 1;
	
	private static final int ixMsgOutputGuiList = 0;
	
	/**Configuration data to represent the messages. */
	final MsgConfig msgConfig;

	private final Locale localization;
	
	/**Class to show the message in the GUI-List.
	 */
	private class ShowMsgInList implements LogMessage
	{

		@Override public void close() {
		}

		@Override public void flush() {
		}

		@Override public boolean isOnline() {
			return true;
		}

		@Override public boolean sendMsg(int identNumber, String text, Object... args) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override public boolean sendMsgTime(int identNumber, OS_TimeStamp creationTime,
				String text, Object... args) {
			// TODO Auto-generated method stub
			return false;
		}

		/**This is the only one method, which is called from the message dispatcher. Only this is implemented.
		 * @see org.vishia.msgDispatch.LogMessage#sendMsgVaList(int, org.vishia.bridgeC.OS_TimeStamp, java.lang.String, org.vishia.bridgeC.Va_list)
		 */
		@Override public boolean sendMsgVaList(int identNumber, OS_TimeStamp creationTime,
				String text, Va_list args) {
			String sTime = dateFormat.format(creationTime);
			char state = identNumber <0 ? '-' : '+';  //going/coming
			if(identNumber < 0){ identNumber = -identNumber; }
			//The configuration for this msg ident.
			String sText;
			try{ sText = String.format(localization, text,args.get());
			} catch(IllegalFormatConversionException exc){
				sText = "error in text format: " + text;
			}catch(IllegalFormatPrecisionException exc){
				sText = "error-precision in text format: " + text;
			}
			String sInfoLine = sTime + '\t' + identNumber + '\t' + state + '\t' + sText;
			guiAccess.insertInfo("msgOfDay", Integer.MAX_VALUE, sInfoLine);
			return true;
		}
		
	}
	
	ShowMsgInList guiMsgOutput = new ShowMsgInList(); 
	
	//JScrollPane guiNotify;
	//Component table;

	
	private void setRange(MsgDispatcher msgDispatcher, String dstMsg, int firstIdent, int lastIdent){
		int dstBits = 0;
		if(dstMsg.indexOf('f')>=0){ dstBits |= (1<<ixMsgOutputFile); }  //output to file
		if(dstMsg.indexOf('d')>=0){ dstBits |= (1<<ixMsgOutputGuiList); }  //output to GUI-display
		msgDispatcher.setOutputRange(firstIdent, lastIdent, dstBits, MsgDispatcher.mSet, 3);
	}
	
	
	public MsgReceiver(Report console, GuiPanelMngWorkingIfc guiAccess, String sTimeZone, String sPathZbnf)
	{ this.console = console;
	  this.guiAccess = guiAccess;
	  this.localization = Locale.ROOT;
	  this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.ROOT);
	  this.dateFormat.setTimeZone(TimeZone.getTimeZone(sTimeZone));
		TimeZone timeZoneForFile = TimeZone.getTimeZone("GMT");  //always GMT!
	  recvData = new MsgItems_h.MsgItems();
		recvDataBuffer = new byte[MsgItems_h.MsgItems.kIdxAfterLast];
		recvData.assignEmpty(recvDataBuffer);
		recvData.setBigEndian(true);
		msgConfig = new MsgConfig(console, sPathZbnf);
  
		MsgDispatcher msgDispatcher = new MsgDispatcher(1000, 100, 3, 0);
		this.msgDispatcher = msgDispatcher;
		int secondsToClose = -10; //any 10 seconds, the file will be closed, and re-openened at least after 10 seconds.
		int hoursPerFile = -3600; //This is 3600 seconds.
		fileOutput = new LogMessageFile("D:/DATA/msg/log$yyyy-MMM-dd-hh_mm$.log", secondsToClose, hoursPerFile, null, timeZoneForFile, msgDispatcher.getSharedFreeEntries());
		msgDispatcher.setOutputRoutine(ixMsgOutputFile, "File", false, fileOutput);
		msgDispatcher.setOutputRoutine(ixMsgOutputGuiList, "GUI-List", false, guiMsgOutput);
		//check all entries in the configuration to configure the MsgDispatcher:
		String dstMsg = "";
		int firstIdent = 0, lastIdent = -1;
		for(Map.Entry<Integer,MsgConfig.MsgConfigItem> entry: msgConfig.indexIdentNr.entrySet()){
			MsgConfig.MsgConfigItem item = entry.getValue();
			if(dstMsg.equals(item.dst)){
				lastIdent = item.identNr;
			} else {
				//a new dst, process the last one.
				if(lastIdent >=0){
					setRange(msgDispatcher, dstMsg, firstIdent, lastIdent);
				}
				//for next dispatching range: 
				firstIdent = lastIdent = item.identNr;
				dstMsg = item.dst;
			}
		}
		setRange(msgDispatcher, dstMsg, firstIdent, lastIdent);  //for the last block.
	}
	
	
	/**Now start work. */
	public void start(){
		bActivated = true;
	}
	
	
	
	void storeMsgOfDay(long absTime, int ident, Object... values)
	{
		MsgItem msgItem = new MsgItem();
		Date date = new Date(absTime);
		msgItem.time = dateFormat.format(date);
		msgItem.ident = ident < 0 ? -ident : ident;
		//The configuration for this msg ident.
		MsgConfig.MsgConfigItem cfgItem = msgConfig.indexIdentNr.get(msgItem.ident);
		String formatText;
	  
		if(cfgItem != null){
			formatText = cfgItem.text; //msgConfig"Format %f %d";
		} else {
			//no config for the ident found.
			formatText = "unknown message";
		}
		try{ msgItem.text = String.format(localization, formatText,values);
		
		} catch(IllegalFormatConversionException exc){
			msgItem.text = "error in text format: " + formatText;
		}catch(IllegalFormatPrecisionException exc){
			msgItem.text = "error-precision in text format: " + formatText;
		}
		msgItem.state = ident<0? '-' : '+';  //going/coming
		msgOfDay.add(msgItem);
		//String sInfoLine = msgItem.time + '\t' + msgItem.ident + '\t' + msgItem.state + '\t' + msgItem.text;
		//guiAccess.insertInfo("msgOfDay", Integer.MAX_VALUE, sInfoLine);
		while(msgOfDay.size() > 200){
			msgOfDay.remove(0);
		}
		msgDispatcher.sendMsgTime(ident, new OS_TimeStamp(absTime), formatText, values);
		
	  
	}
	
	
	void test()
	{ Date currTime = new Date();
	  long currMillisec = currTime.getTime();
		storeMsgOfDay(currMillisec, 1, 3.4, 34);
	}
	
	
	void testAndReceive()
	{
		if(bActivated){
			fileOutput.flush();
			comm.receiveData(nrofBytesReceived, recvDataBuffer, null);
			if(nrofBytesReceived[0] > 0){
				if(nrofBytesReceived[0] < MsgItems_h.MsgItems.kIdxAfterLast){
					console.writeError("msgReceiver: to less bytes: " + nrofBytesReceived[0]);
				} else {
					int nrofMsg = recvData.get_nrofMsg();
					try{
						for(int ii = 0; ii < nrofMsg; ii++){
							msgItem.assignAtIndex(MsgItems_h.MsgItems.kIdxmsgItems
									+ ii * MsgItems_h.MsgItem.kIdxAfterLast                 
									, recvData);
						  int timestamp = msgItem.get_timestamp();  //UDT
						  short timeMillisec = msgItem.get_timeMillisec();
						  short mode_typeVal = msgItem.get_mode_typeVal();
						  long timeMillisecUTC = (long)(timestamp)* 1000 + timeMillisec;
						  
						  int ident = msgItem.get_ident();
						  int value1 = msgItem.get_values(0);  //maybe float image
						  int value2 = msgItem.get_values(1);  //maybe float image
						  int value3 = msgItem.get_values(2);  //maybe float image
						  int value4 = msgItem.get_values(3);  //maybe float image
		
			        Object[] values = new Object[4];
			        short typeValue = mode_typeVal;
			        for(int ixV=0; ixV < values.length; ++ixV){
			        	int value = msgItem.get_values(ixV);
			        	switch(typeValue & 3){
			        	case 0: values[ixV] = new Integer(value); break;
			        	case 1:
			        	case 2:
			        	case 3: values[ixV] = new Float(Float.intBitsToFloat(value)); break;
			        	}
			        	typeValue >>=3;
			        }
						  
						  storeMsgOfDay(timeMillisecUTC, ident, values);

						  bListToBottom = true;				      
				      
						}
					} catch(IllegalArgumentException exc){ throw new RuntimeException("unexpected"); }
				}
			}
		}
	}
	
	
	

	
}
