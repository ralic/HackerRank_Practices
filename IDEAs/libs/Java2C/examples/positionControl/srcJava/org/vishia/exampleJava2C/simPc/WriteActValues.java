package org.vishia.exampleJava2C.simPc;

import java.io.File;

import org.vishia.byteData.RawDataAccess;
import org.vishia.util.FileSystem;
import org.vishia.util.StringPart;

public class WriteActValues {

	/**The file to write out actual values to visit from outside. */
	private final File fileActValues = new File("out/actValues.bin");
	
  
	
	private final byte[] valueBuffer = new byte[400];
	
	
	private final RawDataAccess actValueOut = new RawDataAccess();
	final static int recordsize = 8;
	
	int ix;
	
	WriteActValues(){
		
		actValueOut.setBigEndian(true);
		actValueOut.assignEmpty(valueBuffer);
	}
	
	
	public void write(float way, float current){
		assert(ix - recordsize < valueBuffer.length);
	
		actValueOut.setFloatVal(ix, way);
		actValueOut.setFloatVal(ix+4, current);
		ix += recordsize;
		
		if(ix + recordsize >= valueBuffer.length){
			/**The next entry will be force overflow: 
			 * Write in file or send with UDP. */
			FileSystem.writeBinFile(fileActValues, valueBuffer);
	    ix = 0;
		}
	}
	
}
