package org.vishia.exampleJava2C.positionCtrl.gui;

import org.vishia.guiViewCfg.OamShowValues;
import org.vishia.mainCmd.Report;
import org.vishia.mainGui.GuiPanelMngWorkingIfc;

public class OamShowValuesPositionControl extends OamShowValues
{

	

	
	public OamShowValuesPositionControl(Report log, GuiPanelMngWorkingIfc guiAccess)
	{
		super(log, guiAccess);
	}
	
	@Override public void show(byte[] binData, int nrofBytes, int from)
	{
    super.show(binData, nrofBytes, from);

	}

	@Override public void showRedraw()
	{
	}
	

	
	

	
}
