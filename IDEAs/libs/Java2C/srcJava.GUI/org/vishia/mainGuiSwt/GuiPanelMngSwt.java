/****************************************************************************
 * For this source the LGPL Lesser General Public License,
 * published by the Free Software Foundation is valid.
 * It means:
 * 1) You can use this source without any restriction for any desired purpose.
 * 2) You can redistribute copies of this source to everybody.
 * 3) Every user of this source, also the user of redistribute copies
 *    with or without payment, must accept this license for further using.
 * 4) But the LPGL ist not appropriate for a whole software product,
 *    if this source is only a part of them. It means, the user
 *    must publish this part of source,
 *    but don't need to publish the whole source of the own product.
 * 5) You can study and modify (improve) this source
 *    for own using or for redistribution, but you have to license the
 *    modified sources likewise under this LGPL Lesser General Public License.
 *    You mustn't delete this Copyright/Copyleft inscription in this source file.
 *
 * @author Hartmut Schorrig = hartmut.schorrig@vishia.de
 * @version 2010-03-07  (year-month-day)
 * list of changes:
 * 2010-03-07: Hartmut new: The idea for this class comes from the necessity of some helper methods for gui-dialog.
 *
 ****************************************************************************/

package org.vishia.mainGuiSwt;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
//import org.eclipse.swt.widgets.;

import org.vishia.byteData.VariableAccess_ifc;
import org.vishia.byteData.VariableContainer_ifc;
import org.vishia.mainGui.FileDialogIfc;
import org.vishia.mainGui.GuiDispatchCallbackWorker;
import org.vishia.mainGui.GuiPanelMngBase;
import org.vishia.mainGui.GuiPanelMngBuildIfc;
import org.vishia.mainGui.GuiPanelMngWorkingIfc;
import org.vishia.mainGui.GuiShellMngBuildIfc;
import org.vishia.mainGui.GuiShellMngIfc;
import org.vishia.mainGui.PanelActivatedGui;
import org.vishia.mainGui.PanelContent;
import org.vishia.mainGui.TabPanel;
import org.vishia.mainGui.UserActionGui;
import org.vishia.mainGui.WidgetDescriptor;
import org.vishia.msgDispatch.LogMessage;





/**This class manages the building of Panels with GUI-Elements for example for a Dialog Box
 * The container can be visibilized as a part of another container of any window.
 * An example is given in {@link org.vishia.mainGuiSwt.SampleGuiSwtButtonInput}
 * <br><br>
 * The positions on the display are based on grid units. 
 * The grid-size (number of x- and y-pixel per grid unit) is able to set, so a more large
 * or a more small representation of the graphic is shown, depending on the display point size,
 * the distance to view or personal defaults.
 * <br><br>
 * A layout manager like flow-layout is not used here. The user sets fix positions in the grid units. 
 * A grid unit is a half-small-line in the vertical direction and about a small character width 
 * in the x-direction. So a closed-to-user-thinking position is able to set for any component.
 * It is like rows or lines and columns. 
 * An automatically layout managing using self-deciding positioning of any components is not done. 
 * But a next added component may be positioned right from the previous or below the previous 
 * without manually calculation and setting of the position. It simplifies positioning. 
 * <br><br>
 * This class supports setting of values of shown components with symbolic access (String-name)
 * in runtime
 * and selecting of button-actions and data-container while creating with symbolic access
 * to the action-instance or the data-container. This property is used especially 
 * by the text-script-controlled built of a dialog widget using the {@link org.vishia.mainGui.GuiDialogZbnfControlled},
 * but it is able to simplify the access to data and actions elsewhere too.
 * <br><br>
 * <br><br>
 * <br><br>
 * <br><br>
 * 
 * @author Hartmut Schorrig
 *
 */
public class GuiPanelMngSwt extends GuiPanelMngBase<Control> implements GuiPanelMngBuildIfc, GuiPanelMngWorkingIfc
//GuiShellMngIfc<Control>   
{
  private static final long serialVersionUID = -2547814076794969689L;

	/**Version, able to read as hex yyyymmdd.
	 * Changes:
	 * <ul>
	 * <li>2010-12-02 Hartmut: in method insertInfo((): call of checkAdmissibility() for some input parameter, 
	 *     elsewhere exceptions may be possible on evaluating the inserted info in doBeforeDispatching().
	 *     There the causer isn't found quickly while debugging.
	 * <li>2010-12-02 Hartmut: Up to now this version variable, its description contains the version history.
	 * </ul>
	 */
	public final static int version = 0x20101202;

	/**The GUI may be determined by a external user file. Not all planned fields, buttons etc. 
   * may be placed in the GUI, a user can desire about the elements. 
   * But an access to a non-existing element should be detected. This Exception should be caught
   * and an alternate behavior on non-existing elements may be programmed in the application. 
   */
  public class NotExistException extends Exception
  {
    private static final long serialVersionUID = 1L;

    NotExistException(String name)
    { super("GUI-Element doesn't exist in GUI: " + name);
    }
  }
  
  
  /**The graphical frame. It is any part of a window (a Composite widget) or the whole window. */
  public final Composite graphicFrame;
  
  public final Shell theShellOfWindow;
  
  
  /**Properties of this Dialog Window. */
  public  final PropertiesGuiSwt propertiesGui;
  
  /**Position of the next widget to add. If some widgets are added one after another, 
   * it is similar like a flow-layout.
   * But the position can be set.
   */
  protected int xPos, xPosFrac =0, yPos, yPosFrac =0;
  
  /**Saved last use position. After calling {@link #setPosAndSize_(Control, int, int, int, int)}
   * the xPos and yPos are setted to the next planned position.
   * But, if a new position regarded to the last given one is selected, the previous one is need.
   */
  protected int xPosPrev, xPosPrevFrac, yPosPrev, yPosPrevFrac;
  
  /**width and height for the next element. */
  protected int xIncr, xSizeFrac, yIncr, ySizeFrac;
  
  /**'l' - left 'r'-right, 't' top 'b' bottom. */ 
  private  char xOrigin = 'l', yOrigin = 'b';
  
  char directionOfNextElement = 'r';
  
  /**The width of the last placed element. 
   * It is used to determine a next xPos in horizontal direction. */
  //int xWidth;
  
  /**True if the next element should be placed below the last. */
  boolean bBelow, bRigth;

  
  /**This mouse-click-implementor is added to any widget,
   * which is associated to a {@link WidgetDescriptor} in its data.
   * The infos of the last clicked widget can be got with it.
   */
  MouseClickInfo mouseClickForInfo = new MouseClickInfo(this);
  
  /**This class holds the informations for 1 widget, which things should be changed.
   * An instance of this is used temporary in a queue.
   */
  public static class GuiChangeReq
  {
  	/**The widget where the change should be done. */
  	final WidgetDescriptor<?> widgetDescr;
  	
  	/**The command which should be done to change. It is one of the static definitions cmd... of this class. */
  	final int cmd;
  	
  	final static int cmdInsert = 0xadd;     //add
  	
  	final static int cmdBackColor = 0xbacc0103;     //add
  	
  	final static int cmdColor = 0xc0103;
  	
  	final static int cmdRedraw = 0x3ed3a2;  //redraw
  	
  	final static int cmdRedrawPart = 0x3ed3a201;  //redraw
  	
  	final static int cmdRemove = 0xde1e7e;  //delete
  	
  	/**Numeric value describes the position of widget where the change should be done.
  	 * For example, if the widget is a table, it is either the table line or it is
  	 * Integer.MAX_VALUE or 0 to designate top or end.
  	 */
  	final int ident;
  	
  	/**The textual information which were to be changed or add. */
  	final Object info;
		
  	GuiChangeReq(WidgetDescriptor<?> widgetDescr, int cmd, int indent, Object info) 
		{ this.widgetDescr = widgetDescr;
		  this.cmd = cmd;
			this.ident = indent;
			this.info = info;
		}
  	
  }
  
  
  /**It is a marker interface. */
  protected interface XXXUserAction{}
  
  
  /**Action for user commands of buttons. 
   * An instance of this class is able to assign as button-action.
   * The actionPerformed-method is implemented here, but the procedure calls a used-defined
   * action method which is implemented in the user-space implementing the
   *  {@link UserActionGui#userActionGui(String, String, Map)}-interface. 
   */
  protected class XXXButtonUserAction implements XXXUserAction, SelectionListener
  {

    /**Reference to the users method. */
    private final UserActionGui userCmdGui;
    
    /**Constructor.
     * @param userCmdGui The users method for the action. */
    private XXXButtonUserAction(UserActionGui userCmdGui)
    {
      this.userCmdGui = userCmdGui;
    }
    

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}

    /**Implementing of the primary action method,
     * implements @see org.eclipse.swt.events#widgetSelected(SelectionEvent)
     * All values of input fields are read out and filled in a list, to support locally access
     * to the values of the dialog.
     */
		@Override
		public void widgetSelected(SelectionEvent e) {
      /**prepare all inputs: */
      //String sButtonCmd = e.getActionCommand();
      //String sParamString = e.paramString();
			Object data = null;
			//String sParam = "";
			Widget src = e.widget;
			Object widgetData = src.getData();
			@SuppressWarnings("unchecked")
			WidgetDescriptor<Control> infos = widgetData instanceof WidgetDescriptor<?> ? (WidgetDescriptor<Control>)widgetData : null; 
			if(src instanceof Button){
				Button button = (Button)src;
				data = button.getData();
				if(data != null && data instanceof String){
	    		//sParam = (String)data;
	    	}
			}
			/*
    	Map<String,String> values;
			if(sParam.endsWith("+")){
				values = getAllValues(); 
			} else {
				values = null;
			}
			*/
      userCmdGui.userActionGui("button", infos);
      
		}
    
  }
  
  
  
  /**Index of all input fields to access symbolic for all panels. */
  private final Map<String, WidgetDescriptor<?>> indexNameWidgets = new TreeMap<String, WidgetDescriptor<?>>();

  /**Index of all input fields to access symbolic. NOTE: The generic type of WidgetDescriptor is unknown,
   * because the set is used independently from the graphic system. */
  private final Map<String, WidgetDescriptor<?>> showFields = new TreeMap<String, WidgetDescriptor<?>>();

  //private final IndexMultiTable showFieldsM;

	/**This class is only used to store values to inspect. The Inspector is a tool which works with
	 * reflection and with it internal variable can be visited in runtime. See {@link org.vishia.inspector.Inspector}.
	 */
	@SuppressWarnings("unused")
	private static class TestHelp{
  	private CurveView curveView;
  } 
	TestHelp testHelp = new TestHelp(); 
  
  private ConcurrentLinkedQueue<GuiChangeReq> guiChangeRequests = new ConcurrentLinkedQueue<GuiChangeReq>();
  

  /**Creates an instance.
   * @param guiContainer The container where the elements are stored in.
   * @param width in display-units for the window's width, the number of pixel depends from param displaySize.
   * @param height in display-units for the window's height, the number of pixel depends from param displaySize.
   * @param displaySize character 'A' to 'E' to determine the size of the content 
   *        (font size, pixel per cell). 'A' is the smallest, 'E' the largest size. Default: use 'C'.
   */
  public GuiPanelMngSwt( Device device, Composite graphicFrame
  , int width, int height, char displaySize, VariableContainer_ifc variableContainer
	, LogMessage log)
  { //super(sTitle); 
  	this(graphicFrame, width, height, new PropertiesGuiSwt(device, displaySize), variableContainer, log);
  	
  }

  /**Creates an instance.
   * @param guiContainer The container where the elements are stored in.
   * @param width in display-units for the window's width, the number of pixel depends from param displaySize.
   * @param height in display-units for the window's height, the number of pixel depends from param displaySize.
   * @param displaySize character 'A' to 'E' to determine the size of the content 
   *        (font size, pixel per cell). 'A' is the smallest, 'E' the largest size. Default: use 'C'.
   */
  public GuiPanelMngSwt(Composite graphicFrame, int width, int height, PropertiesGuiSwt propertiesGui
  	, VariableContainer_ifc variableContainer
  	, LogMessage log
  	)
  { super(variableContainer, log); 
  	this.graphicFrame = graphicFrame;
  	Composite shell = graphicFrame;
  	if(!(shell instanceof Shell)){
  		shell = shell.getShell();
  	  //Rectangle boundsOfGraphicFrame = graphicFrame.getBounds();
  	}
  	this.theShellOfWindow = (Shell)shell;
  	this.propertiesGui = propertiesGui;
    if(width >0 && height >0){
    	//guiContainer.setSize(width * propertiesGui.xPixelUnit(), height * propertiesGui.yPixelUnit());
    }
    
    PanelContent<Control> panelContent = new PanelContent<Control>(graphicFrame);
  	panels.put("$", panelContent);
  	currPanel = panelContent;
  	sCurrPanel = "$";
  	
    
    xPos = xPosPrev = 0; //start-position
    yPos = yPosPrev = 4 * propertiesGui.yPixelUnit();
    
		userActions.put("syncVariableOnFocus", this.syncVariableOnFocus);


  }

  
	/**Creates a new window additional to a given window with Panel Manager.
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @return
	 */
	public GuiShellMngBuildIfc createWindow(int line, int column, int dy, int dx, VariableContainer_ifc variableContainer
  	)
	{
		//Display display = new Display();
		Shell shell = new Shell(graphicFrame.getDisplay());
		//TODO
		//Shell shell = (Shell)graphicFrame; //new Shell(display);
		setPosAndSize_(shell, line,0, column,0, dy,0, dx,0);
		//shell.setBounds(left,top, width, height);
		shell.setText("SHELL");
		GuiShellMngBuildIfc mng = new GuiShellMngSwt(shell, 0, 0, propertiesGui, variableContainer, log);
		//mng.setWindowVisible(true);
		
		return mng;

	}
  
  
  
  
	/**Creates a new window additional to a given window with Panel Manager.
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @return
	 */
	public GuiShellMngBuildIfc createModalWindow(String title)
	{
		//Display display = new Display();
		Shell shell = new Shell(SWT.APPLICATION_MODAL|SWT.TITLE);
		//TODO
		//Shell shell = (Shell)graphicFrame; //new Shell(display);
		setPosAndSize_(shell); //, line,0, column,0, dy,0, dx,0);
		//shell.setBounds(left,top, width, height);
		shell.setText(title);
		GuiShellMngBuildIfc mng = new GuiShellMngSwt(shell, 0, 0, propertiesGui, variableContainer, log);
		//mng.setWindowVisible(true);
		
		return mng;

	}
  
  
  
  
  /**Registers a panel to place the widgets. 
   * After registration, the panel can be selected
   * with its name calling the {@link #selectPanel(String)} -routine
   * @param name Name of the panel.
   * @param panel The panel.
   */
  public PanelContent<Control> registerPanel(String name, Object panelP){
  	Composite panel = (Composite)panelP;
  	PanelContent<Control> panelContent = new PanelContent<Control>(panel);
  	panels.put(name, panelContent);
  	panel.setLayout(null);
  	currPanel = panelContent;
  	sCurrPanel = name;
  	return panelContent;
  }
  
  
  /**This method can be override by the user to force some actions if the dialog window is closed. It may be left empty. */
  protected void windowClosing()
  {
  	
  }
  
  
  /**selects a registered panel for the next add-operations. 
   */
  public void selectPanel(String sName){
  	currPanel = panels.get(sName);
  	sCurrPanel = sName;
  	if(currPanel == null) {
  		log.sendMsg(0, "GuiPanelMng:selectPanel: unknown panel name %s", sName);
  	  //Note: because the currPanel is null, not placement will be done.
  	} else {
  		
  		Control parent = currPanel.panelComposite;
  		do
  		{ //Rectangle bounds = parent.getBounds();
  			parent = parent.getParent();
  		} while(!(parent instanceof Shell)); 
  		currPanelPos = currPanel.panelComposite.getBounds();
  		
  	}
  }
  
  
  /**Sets the position for the next widget to add in the container.
   * @param line y-Position in y-Units, count from top of the box. It is the bottom line of the widget.
   *              It means ypos = 0 is not a proper value. To show a text in the first line, use y=2.
   *              If <0, then the previous position is valid still.
   * @param column x-Position in x-Units, count from left of the box. 
   *              If <0, then the previous position is valid still.
   * @param heigth: The height of the line. If <0, then the param line is the buttom line, 
   *                and (line-height) is the top line. If 0 then the last value of height is not changed. 
   * @param length: The number of columns. If <0, then the param column is the right column, 
   *                and column-length is the left column. If 0 then the last value of length is not changed.
   * @param direction: direction for a next widget, use 'r', 'l', 'u', 'd' for right, left, up, down                
   */
  @Override public void setFinePosition(int line, int yPosFrac, int column, int xPosFrac, int height, int ySizeFrac, int width, int xSizeFrac, char direction)
  {
  	if("rlud".indexOf(direction)>=0 ){
  		directionOfNextElement = direction;
  	}
  	setSize(height, ySizeFrac, width, xSizeFrac);
  	if(column >=0 || xPosFrac >0){ 
  		this.xPos = xPosPrev = column;
  		this.xPosFrac = xPosFrac;
  	} else {
  		//use the same xPos as before adding the last Component, 
  		//because a new yPos is given.
  		column = xPosPrev; 
  		xPosFrac = xPosPrevFrac;
    }
  	if(line >=0 || yPosFrac >0){ 
  		this.yPos = yPosPrev = line;
  		this.yPosFrac = yPosFrac;
    } else {
  		//use the same yPos as before adding the last Component, 
  		//because a new xPos may be given.
  		line = yPosPrev; 
  		yPosFrac = yPosPrevFrac;
    }
  	if(height <0){
  		//yPosPrev = (yPos -= height);
  	}
  	if(width <0){
  		//xPosPrev = (xPos -= width);
  	}
  		
  	this.bBelow = false; //because yPos is set.
    this.bRigth = true;
  }
  
  
  public void setSize(int height, int ySizeFrac, int width, int xSizeFrac)
  {
  	if(height !=0){
  		yIncr = height >0 ? height : -height;
      this.ySizeFrac = ySizeFrac;
  	}
  	if(width !=0){
  		xIncr = width >0 ? width: -width;
  	  this.xSizeFrac = xSizeFrac;
    }
  	if(height >0){ yOrigin = 't'; }
  	else if(height < 0){ yOrigin = 'b'; }
  	else; //let it unchanged if height == 0
  	if(width >0){ xOrigin = 'l'; }
  	else if(width < 0){ xOrigin = 'r'; }
  	else; //let it unchanged if width == 0
  }
  
  
  /**Positions the next widget below to the previous one. */
  public void setNextPositionX()
  { //xPos = xWidth; 
  }
  
  /**Positions the next widget on the right next to the previous one. */
  public void setNextPositionY()
  { bBelow = true;
  }
  
  /**Returns the width (number of grid step horizontal) of the last element.
   * @return Difference between current auto-position and last pos.
   */
  public int xxxgetWidthLast(){ return 0; }
  
  /**Places a current component with knowledge of the current positions and the spreads of the component on graphic.
   * @param component The component to place.
   */
  private void setBounds_(Control component)
  { setPosAndSize_(component);
  	//setBounds_(component, 0,0, 0, 0);
  }
  
  /**Places a current component with knowledge of the current positions and the spreads of the component on graphic.
   * @param component The component to place.
   * @param diff movement of the placement in dedicated pixel units, usefull for example to place a table head and body.
   *             It may be null.
   */
  private void setBounds_(Control component, int pyGrid, int pxGrid, int dyGrid, int dxGrid)
  { int xPixelUnit = propertiesGui.xPixelUnit();
  	int yPixelUnit = propertiesGui.yPixelUnit();
  	
  //The size in pixel is given either by the current size of the widget,
  	//or it is calculate by given dxyGrid.
  	int xPixelSize = -1, yPixelSize = -1;  //initial with an unexpected value, it will be set in any case!
  	if(dyGrid <= 0 || dxGrid <= 0){ 
  	  Point size = component.getSize();
  	  if(dyGrid <= 0){ yPixelSize = size.y; }
  	  if(dxGrid <= 0){ xPixelSize = size.x; }
    }
    if(dyGrid > 0){ yPixelSize = dyGrid * yPixelUnit; }
	  if(dxGrid > 0){ xPixelSize = dxGrid * xPixelUnit; }
  
	  //The position-movement in pixel is given either by the current size of the widget,
  	//or it is set by given dxyGrid.
  	int xPixel0, yPixel0;
  	yPixel0 = pyGrid * yPixelUnit;  //unused
	  xPixel0 = pxGrid * xPixelUnit;
  
	  //If top origin, the yPos
	  //int yPos1 = yOrigin == 't' ? this.yPos - this.yIncr : this.yPos;
	  int xPixel = (int)(xPos * xPixelUnit) + xPixel0 +1;
    int yPixel = (int)(yPos * yPixelUnit) + yPixel0 +1;
    if(yOrigin == 'b'){
      yPixel -= yPixelSize; //yPos is left bottom, yPixel is left top.
    }
    if(yPixel < 1){ yPixel = 1; }
    component.setBounds(xPixel, yPixel, xPixelSize, yPixelSize);
    int dx = (xPixelSize + xPixel0 + propertiesGui.xPixelUnit() -1 ) / propertiesGui.xPixelUnit(); 
    int dy = (yPixelSize + yPixel0 + propertiesGui.yPixelUnit() -1 ) / propertiesGui.yPixelUnit(); 
    furtherSetPosition(dx, dy);
  }          
  
  
  protected void setPosAndSize_(Control component)
  {
  	setPosAndSize_(component, yPos, yPosFrac, xPos, xPosFrac, this.yIncr, ySizeFrac, this.xIncr, xSizeFrac);
    //int dx1 = (xPixelSize + propertiesGui.xPixelUnit() -1 ) / propertiesGui.xPixelUnit(); 
    //int dy1 = (yPixelSize + propertiesGui.yPixelUnit() -1 ) / propertiesGui.yPixelUnit(); 
    //furtherSetPosition(, dy);
  }
  
  
  protected void setSize_(Control component, int dy, int ySizeFrac, int dx, int xSizeFrac)
  {
  	if(dy <=0){ dy = this.yIncr; }
  	if(dx <=0){ dx = this.xIncr; }
  	setPosAndSize_(component, yPos, yPosFrac, xPos, xPosFrac, dy, ySizeFrac, dx, xSizeFrac);
    //int dx1 = (xPixelSize + propertiesGui.xPixelUnit() -1 ) / propertiesGui.xPixelUnit(); 
    //int dy1 = (yPixelSize + propertiesGui.yPixelUnit() -1 ) / propertiesGui.yPixelUnit(); 
    //furtherSetPosition(dx, dy);
  }
  
  
  protected void setPosAndSize_(Control component, int line, int yPosFrac, int column, int xPosFrac, int dy, int ySizeFrac, int dx, int xSizeFrac)
  {
  	Control parentComp = component.getParent();
  	//Rectangle pos;
  	if(parentComp != graphicFrame){
  		//it is not a widget in this panel:
  		stop();
  		//pos = currPanel.panelComposite.getBounds(); 
  	} else {
  		//pos =null;
  	}
  	if(line == 19 && yPosFrac == 5)
  		stop();
  	//use values from class if parameter are non-valid.
  	if(line <=0){ line = this.yPos; yPosFrac = this.yPosFrac; }
  	if(column <=0){ column = this.xPos; xPosFrac = this.xPosFrac; }
  	if(dy <=0){ dy = this.yIncr; ySizeFrac = this.ySizeFrac; }
  	if(dx <=0){ dx = this.xIncr; xSizeFrac = this.xSizeFrac; }
  	//
  	int xPixelUnit = propertiesGui.xPixelUnit();
  	int yPixelUnit = propertiesGui.yPixelUnit();
  	//calculate pixel
  	int xPixelSize, yPixelSize;  
  	xPixelSize = xPixelUnit * dx + propertiesGui.xPixelFrac(xSizeFrac) -2 ;
  	yPixelSize = yPixelUnit * dy + propertiesGui.yPixelFrac(ySizeFrac) -2;
	  int xPixel = (int)(column * xPixelUnit) + propertiesGui.xPixelFrac(xPosFrac) +1;
    int yPixel = (int)(line * yPixelUnit) + propertiesGui.yPixelFrac(yPosFrac) +1;
    if(yOrigin == 'b'){
      yPixel -= yPixelSize +1; //line is left bottom, yPixel is left top.
    }
    if(xOrigin == 'r'){
      xPixel -= xPixelSize +1; //yPos is left bottom, yPixel is left top.
    }
    if(yPixel < 1){ yPixel = 1; }
    if(xPixel < 1){ xPixel = 1; }
    component.setBounds(xPixel, yPixel, xPixelSize, yPixelSize);
    xPosPrev = xPos;    //save to support access to the last positions.
    yPosPrev = yPos;
    //set the next planned position:
    switch(directionOfNextElement){
    case 'r': xPos += xIncr; break;
    case 'l': xPos -= xIncr; break;
    case 'u': yPos -= yIncr; break;
    case 'd': yPos += yIncr; break;
    }
  }
  
  



  /**Sets the position for further add.. calls.
   * @param dx
   * @param dy
   */
  private void furtherSetPosition(int dx, int dy){
    xPosPrev = xPos;
    yPosPrev = yPos;
    //calculate maxWith etc.
    if(bBelow){ yPos += dy; }
    else { xPos += dx; }
  }
  

  
	@Override public TabPanel createTabPanel(PanelActivatedGui user)
	{
		return new TabPanelSwt(this, user);
	}
	
  
  
  
  public Label addText(String sText, char size, int color)
  {
  	Label widget = new Label((Composite)currPanel.panelComposite, 0);
  	widget.setForeground(propertiesGui.color(color));
  	widget.setBackground(propertiesGui.colorBackground);
  	widget.setText(sText);
  	//Font font = propertiesGui.stdInputFont;
  	Font font = propertiesGui.getTextFont(yIncr, ySizeFrac);
  	widget.setFont(font);
  	//font.getFontData()[0].
  	Point textSize = widget.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
  	//int width = sText.length();
  	//widget.setSize(sizePixel);
  	
  	setPosAndSize_(widget);
  	
  	Point widgetSize = widget.getSize();
  	if(widgetSize.x < textSize.x){
  		widget.setSize(textSize);
  	}
  	widget.setSize(textSize);
  	//guiContent.add(widget);
  	return widget;
  }

  
  /** Adds a edit field for editing a number value.
   * 
   * @param sName The registering name
   * @param width Number of grid units for length
   * @param editable true than edit-able, false to show content 
   * @param prompt If not null, than a description label is shown
   * @param promptStylePosition Position and size of description label:
   *   upper case letter: normal font, lower case letter: small font
   *   'l' left, 't' top (above field) 
   * @return
   */
  public Text addTextField(WidgetDescriptor<?> widgetInfo, boolean editable, String prompt, char promptStylePosition)
  { Text widget = new Text((Composite)currPanel.panelComposite, 0);
    widget.setFont(propertiesGui.stdInputFont);
    widget.setEditable(editable);
    if(editable)
    	stop();
    widget.setBackground(propertiesGui.color(0xFFFFFF));
    widget.addMouseListener(mouseClickForInfo);
    if(widgetInfo.sDataPath != null && widgetInfo.sDataPath.startsWith("showBinEnable("))
    	stop();
    int x =-1, y=-1; 
    if(x >=0 && y >=0){
      //edit.setBounds(x, y, dx * properties.xPixelUnit(), 2* properties.yPixelUnit());
    } else {
    	//widget.setSize(xIncr * propertiesGui.xPixelUnit()-1, 2* propertiesGui.yPixelUnit()-1);
    }
    //
    setPosAndSize_(widget);
    if(prompt != null && promptStylePosition == 't'){
    	final int yPixelField;
      final Font promptFont;
      char sizeFontPrompt;
      	switch(yIncr){
    	case 3:  promptFont = propertiesGui.smallPromptFont;
    	         yPixelField = propertiesGui.yPixelUnit() * 2 -3;
    	         break;
    	case 2:  promptFont = propertiesGui.smallPromptFont;
               yPixelField = (int)(1.5F * propertiesGui.yPixelUnit());
               break;
    	default: promptFont = propertiesGui.smallPromptFont;
    	         yPixelField = propertiesGui.yPixelUnit() * 2 -3;
    	}//switch
      Rectangle boundsField = widget.getBounds();
      Rectangle boundsPrompt = new Rectangle(boundsField.x, boundsField.y-3  //occupy part of field above, only above the normal letters
      	, boundsField.width, boundsField.height );
      
      if(promptStylePosition == 't'){	
      	boundsPrompt.height -= (yPixelField -4);
      	boundsPrompt.y -= 1;
      	
      	boundsField.y += (boundsField.height - yPixelField );
      	boundsField.height = yPixelField;
      }
      Label wgPrompt = new Label((Composite)currPanel.panelComposite, 0);
      wgPrompt.setFont(promptFont);
      wgPrompt.setText(prompt);
      Point promptSize = wgPrompt.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
    	if(promptSize.x > boundsPrompt.width){
    		boundsPrompt.width = promptSize.x;  //use the longer value, if the prompt text is longer as the field.
    	}
      widget.setBounds(boundsField);
      wgPrompt.setBounds(boundsPrompt);
    } 
    //
    if(widgetInfo.name !=null && widgetInfo.name.charAt(0) == '$'){
    	widgetInfo.name = sCurrPanel + widgetInfo.name.substring(1);
    }
    //link the widget with is information together.
    widgetInfo.widget = widget;
    widget.setData(widgetInfo);
    if(widgetInfo.name !=null){
      indexNameWidgets.put(widgetInfo.name, widgetInfo);
      if(!editable){
    	  showFields.put(widgetInfo.name, widgetInfo);
    	}
    }
    currPanel.widgetList.add(widgetInfo);
    return widget; 
  
  }
  
  /**Adds a line.
   * <br><br>To adding a line is only possible if the current panel is of type 
   * {@link CanvasStorePanelSwt}. This class stores the line coordinates and conditions 
   * and draws it as background if drawing is invoked.
   * 
   * @param colorValue The value for color, 0xffffff is white, 0xff0000 is red.
   * @param xa start of line relative to current position in grid units.
   *          The start is relative to the given position! Not absolute in window! 
   * @param ya start of line relative to current position in grid units.
   * @param xe end of line relative to current position in grid units.
   * @param ye end of line relative to current position in grid units.
   */
  public void addLine(int colorValue, float xa, float ya, float xe, float ye){
  	if(currPanel.panelComposite instanceof CanvasStorePanelSwt){
  		Color color = propertiesGui.color(colorValue);
  		int xgrid = propertiesGui.xPixelUnit();
  		int ygrid = propertiesGui.yPixelUnit();
  		int x1 = (int)((xPos + xa) * xgrid);
  		int y1 = (int)((yPos - ya) * ygrid);
  		int x2 = (int)((xPos + xe) * xgrid);
  		int y2 = (int)((yPos - ye) * ygrid);
  		//Any panel which is created in the SWT-implementation is a CanvasStorePanel.
  		//This is because lines should be drawn.
  		((CanvasStorePanelSwt) currPanel.panelComposite).store.drawLine(color, x1, y1, x2, y2);
  		furtherSetPosition((int)(xe + 0.99F), (int)(ye + 0.99F));
  	} else {
  		log.sendMsg(0, "GuiPanelMng:addLine: panel is not a CanvasStorePanel");
    }
  }
  
  
  
  
  /* (non-Javadoc)
   * @see org.vishia.mainGui.GuiPanelMngIfc#addImage(java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String)
   */
  @Override 
  public Object addImage(String sName, InputStream imageStream, int height, int width, String sCmd)
  {
    ////
  	ImageData imageData = new ImageData(imageStream);
  	byte[] data = imageData.data;
  	Label widget = new Label((Composite)currPanel.panelComposite, 0);
  	Image image = new Image(((Composite)currPanel.panelComposite).getDisplay(), imageData); 
  	widget.setImage(image);
    widget.setSize(propertiesGui.xPixelUnit() * width, propertiesGui.yPixelUnit() * height);
    setBounds_(widget);
    if(sCmd != null){
      widget.setData(sCmd);
    } 
    indexNameWidgets.put(sName, new WidgetDescriptor<Control>(sName, widget, 'i', sName, null));
    return widget;
  }

  
  
  @Override public WidgetDescriptor<?> addValueBar(
  	String sName
  , String sShowMethod
  , String sDataPath
  )
  {
  	ValueBarSwt widget = new ValueBarSwt(this);
  	setPosAndSize_(widget.widget);
  	WidgetDescriptor<Control> widgetInfos = new WidgetDescriptor<Control>(sName, widget, 'U');
  	widgetInfos.setShowMethod(sShowMethod);
  	widgetInfos.setDataPath(sDataPath);
    widget.widget.setData(widgetInfos);
    widget.widget.addMouseListener(mouseClickForInfo);
    currPanel.widgetList.add(widgetInfos);
    if(sName != null){
      indexNameWidgets.put(sName, widgetInfos);
    }
    return widgetInfos;
  }
  
  
  @Override public WidgetDescriptor<?> addSlider(
  	String sName
  , UserActionGui action
  , String sShowMethod
  , String sDataPath
  )
  {
  	Slider control = new Slider((Composite)this.currPanel.panelComposite, SWT.VERTICAL);
  	control.setBackground(propertiesGui.colorBackground);
  	setPosAndSize_(control);
   	WidgetDescriptor<Control> widgetInfos = new WidgetDescriptor<Control>(sName, control, 'V');
    if(action != null){
  		SelectionListenerForSlider actionSlider = new SelectionListenerForSlider(widgetInfos, action);
  		control.addSelectionListener(actionSlider);
  	}
    widgetInfos.setDataPath(sDataPath);
    control.setData(widgetInfos);
    control.addMouseListener(mouseClickForInfo);
    return widgetInfos;
  	
  }
  
  
  
  
  
  @Override public WidgetDescriptor<?> addButton(
  	String sName
  , UserActionGui action
  , String sCmd
  , String sShowMethod
  , String sDataPath
  , String sButtonText
  	//, int height, int width
  	//, String sCmd, String sUserAction, String sName)
  )
  {
  	char size = yIncr > 3? 'B' : 'A';
    ButtonSwt button = new ButtonSwt(this, size);
    button.setBackground(propertiesGui.colorBackground);
  	
    button.setText(sButtonText);
    button.setForeground(propertiesGui.color(0xff00));
    button.setSize(propertiesGui.xPixelUnit() * xIncr -2, propertiesGui.yPixelUnit() * yIncr -2);
    setBounds_(button);
    if(sName == null){ sName = sButtonText; }
    WidgetDescriptor<Control> widgetInfos = new WidgetDescriptor<Control>(sName, button, 'B');
    widgetInfos.sCmd = sCmd;
    widgetInfos.setShowMethod(sShowMethod);
    widgetInfos.sDataPath = sDataPath;
    button.setData(widgetInfos);
    //currPanel.widgetIndex.put(sName, widgetInfos);
    currPanel.widgetList.add(widgetInfos);
    if(sCmd != null){
      //button.setData(sCmd);
    } 
    if(action != null){
    	button.addMouseListener( new MouseClickActionForUserActionSwt(this, action, null, "Button-up", null));
    } else {
    	button.addMouseListener(mouseClickForInfo);
    }
    if(sName != null){
      indexNameWidgets.put(sName, widgetInfos);
    }
    return widgetInfos;
  }
  

  
  @Override public WidgetDescriptor<?> addSwitchButton(
  	String sName
  , UserActionGui action
  , String sCmd
  , String sShowMethod
  , String sDataPath
  , String sButtonText
  , String sColor0
  , String sColor1
  	//, int height, int width
  	//, String sCmd, String sUserAction, String sName)
  )
  {
  	char size = yIncr > 3? 'B' : 'A';
  	if(sColor0 == null || sColor1 == null) throw new IllegalArgumentException("SwitchButton " + sName + ": color0 and color1 should be given.");
  	
  	SwitchButtonSwt button = new SwitchButtonSwt(this, size);
  	button.setBackground(propertiesGui.colorBackground);
  	button.setColorPressed(propertiesGui.getColorValue(sColor1));  
    button.setColorReleased(propertiesGui.getColorValue(sColor0));  
    button.setText(sButtonText);
    button.setForeground(propertiesGui.color(0xff00));
    button.setSize(propertiesGui.xPixelUnit() * xIncr -2, propertiesGui.yPixelUnit() * yIncr -2);
    setBounds_(button);
    if(sName == null){ sName = sButtonText; }
    WidgetDescriptor<Control> widgetInfos = new WidgetDescriptor<Control>(sName, button, 'B');
    widgetInfos.sCmd = sCmd;
    widgetInfos.setShowMethod(sShowMethod);
    widgetInfos.sDataPath = sDataPath;
    button.setData(widgetInfos);
    //currPanel.widgetIndex.put(sName, widgetInfos);
    currPanel.widgetList.add(widgetInfos);
    if(sName != null){
      indexNameWidgets.put(sName, widgetInfos);
    }
    return widgetInfos;
  }
  
  @Override public WidgetDescriptor<?> addLed(
  	String sName
  , String sShowMethod
  , String sDataPath
  )
  {
  	LedSwt widget = new LedSwt(this, 'r');
  	widget.setBackground(propertiesGui.colorBackground);
  	
    widget.setForeground(propertiesGui.color(0xff00));
    widget.setSize(propertiesGui.xPixelUnit() * xIncr -2, propertiesGui.yPixelUnit() * yIncr -2);
    setBounds_(widget);
    WidgetDescriptor<Control> widgetInfos = new WidgetDescriptor<Control>(sName, widget, 'D');
    widgetInfos.sDataPath = sDataPath;
    widgetInfos.setShowMethod(sShowMethod);
    widget.setData(widgetInfos);
    currPanel.widgetList.add(widgetInfos);
    if(sName != null){
      indexNameWidgets.put(sName, widgetInfos);
    }
    return widgetInfos;
  }
  
	@Override
	public Canvas addCurveViewY(String sName, int nrofXvalues,
			int nrofTracks) {
		int dxWidget = this.xIncr * propertiesGui.xPixelUnit();
		int dyWidget = this.yIncr * propertiesGui.yPixelUnit();
		CurveView curveView = new CurveView((Composite)currPanel.panelComposite, dxWidget, dyWidget, nrofXvalues, nrofTracks);
		testHelp.curveView = curveView; //store to inspect.
		curveView.setSize(dxWidget, dyWidget);
		setBounds_(curveView); //, dyGrid, dxGrid);
		curveView.setGridVertical(10, 5);   //10 data-points per grid line, 50 data-points per strong line.
		curveView.setGridHorizontal(50.0F, 5);  //10%-divisions, with 5 sub-divisions
		curveView.setGridColor(propertiesGui.colorGrid, propertiesGui.colorGridStrong);
		indexNameWidgets.put(sName, new WidgetDescriptor<Control>(sName, curveView, 'c', sName, null));
		return curveView;
	}

  /**Adds a mouse click action (left button) to any widget. 
   * It is able to apply especially for widget without mouse handling,
   * for example geometric figures all a canvas area. 
   * It should not be applied to a widget which has a special handling for mouse buttons already.
   * <br><br>
   * The first parameter of user action will be called with
   *        <ul>
   *        <li>"d": for double-click
   *        <li>"p": for press (down)
   *        <li>"r": for release (up)
   *        <ul>
   * Hint: If only a release is evaluated and the press are ignored,
   * it is proper for handling with a touch panel. 
   * Then the reaction to an inadvertent press can be prevent 
   * by dragging the mouse without release away from the widget.         
   * 
   * @param sName registered name of the widget.
   * @param sRegisteredUserAction The registered user action. 
   * @return
   */
  @Override public Object addMouseButtonAction(String sName, UserActionGui action, String sCmdPress, String sCmdRelease, String sCmdDoubleClick)
  {
  	String sNameUsed = sName.charAt(0) == '$' ? sCurrPanel + sName.substring(1) : sName;
    WidgetDescriptor<?> widget = indexNameWidgets.get(sNameUsed);
  	if(widget == null || !(widget.widget instanceof Control)){
  		log.sendMsg(0, "GuiMainDialog:addClickAction: unknown widget %s", sName);
  	} else {
    	/**The class ButtonUserAction implements the general button action, which class the registered user action. */
      ((Control)(widget.widget)).addMouseListener( new MouseClickActionForUserActionSwt(this, action, sCmdPress, sCmdRelease, sCmdDoubleClick));
      
  	}
  	return widget.widget;
  }
	
	@Override public WidgetDescriptor<?> addFocusAction(String sName, UserActionGui action, String sCmdEnter, String sCmdRelease)
	{
    WidgetDescriptor<?> widget = indexNameWidgets.get(sName);
  	if(widget == null || !(widget.widget instanceof Control)){
  		log.sendMsg(0, "GuiMainDialog:addClickAction: unknown widget %s", sName);
  	} else {
    	/**The class ButtonUserAction implements the general button action, which class the registered user action. */
      ((Control)(widget.widget)).addFocusListener( new FocusActionForUserActionSwt(this, action, sCmdEnter, sCmdRelease));
      
  	}
  	return widget;
	}

	
	@Override public void addFocusAction(WidgetDescriptor<?> widgetInfo, UserActionGui action, String sCmdEnter, String sCmdRelease)
	{
    ((Control)(widgetInfo.widget)).addFocusListener( new FocusActionForUserActionSwt(this, action, sCmdEnter, sCmdRelease));
  }

	
	Table testTable;
  
  @Override public Table addTable(String sName, int height, int[] columnWidths)
  {
  	boolean TEST = false;
  	final Table table;
  	table = new Table((Composite)currPanel.panelComposite, SWT.FULL_SELECTION);
  	testTable = table;
  	table.setFont(this.propertiesGui.stdInputFont);
  	//table.setColumnSelectionAllowed(true);
  	//table.setRowHeight(2 * this.propertiesGui.xPixelUnit());
  	//Container widget = new Container();
  	int width=0;
  	int xPixel = propertiesGui.xPixelUnit();
  	for(int ixw=0; ixw<columnWidths.length; ++ixw){
  		int columnWidth = columnWidths[ixw];
  		width += columnWidth;
  		int columnWidthPixel = columnWidth * xPixel;
  		TableColumn tColumn = new TableColumn(table, 0, ixw);
  		tColumn.setWidth(columnWidthPixel);
  		//tColumn.setHeaderValue("Test"+ixw);
  		tColumn.setWidth(columnWidthPixel);
  	}
  	int widthPixel = width * xPixel;
  	int heightPixel = height * propertiesGui.yPixelUnit();
  	table.setSize(widthPixel, heightPixel);
  	if(TEST){
  		String[] sColumnTitle = { "A", "B", "C"};
    	String[][] contentTest = {{ "a", "b", "c"}, { "0", "1", "2"}, { "ikkkkkkkk", "j", "k"}, { "r", "s", "t"}, { "x", "y", "z"}};
    	for (int i = 0; i < contentTest.length; i++) {
    		TableItem item = new TableItem(table, SWT.NONE);
    		item.setText(contentTest[i]);
    	}
    }
  	
  	/*
  	for (int i = 0; i < tableInfo.length; i++) {
  		TableItem item = new TableItem(table, SWT.NONE);
  		item.setText(tableInfo[i]);
  	}
  	*/
  	
  	/*
    	JTableHeader header= table.getTableHeader();
  	Dimension dimHeader = header.getPreferredSize();
  	header.setBounds(0,0, dimHeader.width, dimHeader.height);
  	widget.add(header);
  	//Rectangle boundsHeader = header.getBounds();
  	Dimension size = table.getPreferredSize();
    //not sensitive: table.setBounds(0,dimHeader.height, size.width, size.height + 200);
  	JScrollPane tablePane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  	tablePane.setBounds(0,dimHeader.height, widthPixel, heightPixel - dimHeader.height);
    widget.add(tablePane);
  	setBounds_(widget);
  	//Point diff = new Point(0, boundsHeader.height);
  	//setBounds_(table, diff);
   	guiContent.add(widget);
   */
  	setBounds_(table);
  	indexNameWidgets.put(sName, new WidgetDescriptor<Control>(sName, table, 'L', sName, null));
  	return table;
  }
  
  
  public void repaint()
  {
  	//Point size = graphicFrame.getSize();
  	//graphicFrame.redraw(0,0,size.x, size.y, true);
  	graphicFrame.update();
  	
  	//((Composite)currPanel.panelComposite).update();
  	//((Composite)currPanel.panelComposite).redraw();
  }
  
  
  /**Sets the content of any field during operation. The GUI should be created already.
   * @param name Name of the field, corresponding with the <code>name</code>-designation of the field 
   *             in the configuration.
   * @param content The new content.
   * @return The old content of the field.
   * throws GuiDialogZbnfControlled.NotExistException if the field with the given name isn't found.
   */
  public String setFieldContent(String name, String content)
  throws NotExistException
  {
    WidgetDescriptor<?> descr = indexNameWidgets.get(name);
    if(descr == null) throw new NotExistException(name);
    assert(descr.widget instanceof Text);
    Text field = (Text)descr.widget;
    String oldContent = field.getText();
    field.setText(content);
    return oldContent;
  }
  
  /**Inserts a textual information at any widget. The widget may be for example:
   * <ul>
   * <li>a Table: Than a new line will be inserted or appended. 
   *     The content associated to the cells are separated with a tab-char <code>'\t'</code>.
   *     The line number is identified by the ident. 
   * <li>a Tree: Than a new leaf is insert after the leaf, which is identified by the ident.
   * <li>a Text-edit-widget: Than a text is inserted in the field.
   * </ul>
   * The insertion is written into a queue, which is red in another thread. 
   * It may be possible too, that the GUI is realized in another module, maybe remote.
   * It means, that a few milliseconds should be planned before the change appears.
   * If the thread doesn't run or the remote receiver isn't present, 
   * than the queue may be overflowed or the request may be lost.
   *    
   * @param name The name of the widget, which was given by the add...()-Operation
   * @param ident A identifying number. It meaning depends on the kind of widget.
   *        0 means, insert on top.  Integer.MAXVALUE means, insert after the last element (append).
   * @param content The content to insert.
   * @return
   */
  public String insertInfo(String name, int ident, String content)
  {
  	WidgetDescriptor<?> descr = indexNameWidgets.get(name);
  	if(descr == null){
  		log.sendMsg(0, "GuiMainDialog:insertInfo: unknown widget %s", name);
  	} else {
  		insertInfo(descr, ident, content);
  	}
  	return "";
  } 
  
  public String insertInfo(WidgetDescriptor<?> descr, int ident, String content)
  {
  	return insertInfo(descr, GuiChangeReq.cmdInsert, ident, content);
  }
  
  
  
  public String insertInfo(WidgetDescriptor<?> descr, int ident, Object value)
  {
  	return insertInfo(descr, GuiChangeReq.cmdInsert, ident, value);
  }
  
  
  public String insertInfo(WidgetDescriptor<?> descr, int cmd, int ident, Object value)
  {
  	if(descr.name !=null && descr.name.equals("writerEnergy1Sec") && cmd == GuiChangeReq.cmdInsert) ////)
  		stop();
  	//check the admissibility:
  	switch(cmd){
  	case GuiChangeReq.cmdInsert: checkAdmissibility(value != null && value instanceof String); break;
  	}
    guiChangeRequests.add(new GuiChangeReq(descr, cmd, ident, value));
	  synchronized(guiChangeRequests){ guiChangeRequests.notify(); }  //to wake up waiting on guiChangeRequests.
	  graphicFrame.getDisplay().wake(); //wake-up the GUI-thread, it may sleep elsewhere.
	  //((Composite)currPanel.panelComposite).getDisplay().wake();  //wake-up the GUI-thread, it may sleep elsewhere. 
	  return "";
  }
  
  
  private void checkAdmissibility(boolean value){
  	if(!value){
  		throw new IllegalArgumentException("failure");
  	}
  }
  
  
  /**Sets the background color of any widget. The widget may be for example:
   * <ul>
   * <li>a Table: Then a new line will be colored. 
   * <li>a Tree: Then a new leaf is colored.
   * <li>a Text-edit-widget: Then the field background color is set.
   * </ul>
   * The color is written into a queue, which is red in another thread. 
   * It may be possible too, that the GUI is realized in another module, maybe remote.
   * It means, that a few milliseconds should be planned before the change appears.
   * If the thread doesn't run or the remote receiver isn't present, 
   * than the queue may be overflowed or the request may be lost.
   *    
   * @param name The name of the widget, which was given by the add...()-Operation
   * @param ident A identifying number. It meaning depends on the kind of widget.
   *        0 means, insert on top.  Integer.MAXVALUE means, insert after the last element (append).
   * @param content The content to insert.
   * @return
   */
  public void setBackColor(String name, int ix, int color)
  {
  	WidgetDescriptor<?> descr = indexNameWidgets.get(name);
  	if(descr == null){
  		log.sendMsg(0, "GuiMainDialog:setBackColor: unknown widget %s", name);
  	} else {
  		setBackColor(descr, ix, color);
  	}
  } 
  
  
  /**Sets the background color of any widget. The widget may be for example:
   * <ul>
   * <li>a Table: Then a new line will be colored. 
   * <li>a Tree: Then a new leaf is colored.
   * <li>a Text-edit-widget: Then the field background color is set.
   * </ul>
   * The color is written into a queue, which is red in another thread. 
   * It may be possible too, that the GUI is realized in another module, maybe remote.
   * It means, that a few milliseconds should be planned before the change appears.
   * If the thread doesn't run or the remote receiver isn't present, 
   * than the queue may be overflowed or the request may be lost.
   *    
   * @param name The name of the widget, which was given by the add...()-Operation
   * @param ident A identifying number. It meaning depends on the kind of widget.
   *        0 means, insert on top.  Integer.MAXVALUE means, insert after the last element (append).
   * @param content The content to insert.
   * @return
   */
  public void setBackColor(WidgetDescriptor<?> descr1, int ix, int color)
  { @SuppressWarnings("unchecked") //casting from common to specialized: only one type of graphic system is used.
  	WidgetDescriptor<Control> descr = (WidgetDescriptor<Control>) descr1;
  	insertInfo(descr, GuiChangeReq.cmdBackColor, ix, color);
  } 
  
  
  @Override public void setLed(WidgetDescriptor<?> widgetDescr, int colorBorder, int colorInner)
  {
  	@SuppressWarnings("unchecked") //casting from common to specialized: only one type of graphic system is used.
  	WidgetDescriptor<Control> descr = (WidgetDescriptor<Control>) widgetDescr;
  	insertInfo(descr, GuiChangeReq.cmdColor, colorBorder, colorInner);
  	
  }
  
  
  
  
  /**Gets the content of any field during operation. The GUI should be created already.
   * @param name Name of the field, corresponding with the <code>name</code>-designation of the field 
   *             in the configuration.
   * @return The content of the field.
   * throws GuiDialogZbnfControlled.NotExistException if the field with the given name isn't found.
   */
  public String getFieldContent(String name)
  throws NotExistException
  {
    WidgetDescriptor<?> descr = indexNameWidgets.get(name);
    if(descr == null) throw new NotExistException(name);
    Text field = (Text)descr.widget;
    String content = field.getText();
    return content;
  }
  
  
  /**Returns a Set of all fields, which are created to show.
   * @return the set, never null, possible an empty set.
   */
  public Set<Entry<String, WidgetDescriptor<?>>> getShowFields()
  {
  	Set<Entry<String, WidgetDescriptor<?>>> set = showFields.entrySet();
  	return set; //(Set<Entry<String, WidgetDescriptor<?>>>)set;
  }

  
  @Override public FileDialogIfc createFileDialog()
  {
  	return new FileDialogSwt(theShellOfWindow);
  }

  
  
  /**The dispatch listener should be included in the dispatch loop in the SWT-Thread.
   * It should be called any time if the Graphic is updated and cyclically too.
   * <br><br>
   * The run-method of this class is called one time in any dispatch loop process.
   * It has to be returned immediately (not like the run-method of the thread),
   * after it may be changed the graphic appearance. The graphic appearance is changed
   * if any command is set in the {@link #guiChangeRequests}-Queue, see 
   * <ul>
   * <li>{@link #insertInfo(String, int, String)} 
   * </ul>
   */
  private final GuiDispatchCallbackWorker dispatchListener = new GuiDispatchCallbackWorker()
  {
  	void changeTable(Table table, int ident, String content)
  	{
  		String[] sLine = content.split("\t");
			TableItem item = new TableItem(table, SWT.NONE);
	  	item.setText(sLine);
      table.showItem(item);
	  	ScrollBar scroll = table.getVerticalBar();
      if(scroll !=null){
	      int maxScroll = scroll.getMaximum();
	      log.sendMsg(0, "TEST scroll=%d", maxScroll);
	      //scroll.setSelection(maxScroll);
      }  
      //table.set
      table.redraw(); //update();
  	 
  	}
  	
  	
  	boolean done = true;

  	
  	/**This method is called in the GUI-thread. 
  	 * 
  	 */
  	@Override public void doBeforeDispatching(boolean onlyWakeup)
  	{ if(!done){
  		  done=true;
				String[] contentTest = { "test", "b", "c"};
				TableItem item = new TableItem(testTable, SWT.NONE);
		  	item.setText(contentTest);
  	  }
  	  GuiChangeReq changeReq;
  	  while( (changeReq = guiChangeRequests.poll()) != null){
  	  	WidgetDescriptor<?> descr = changeReq.widgetDescr;
  	  	Object oWidget = descr.widget;
  	  	int colorValue;
  	  	switch(changeReq.cmd){
	  		case GuiChangeReq.cmdBackColor: 
	  			colorValue = ((Integer)(changeReq.info)).intValue();
	  			Color color = propertiesGui.color(colorValue & 0xffffff);
	  			((Control)(oWidget)).setBackground(color); 
	  			break;
	  		case GuiChangeReq.cmdRedraw: ((Control)(oWidget)).redraw(); break;
	  		case GuiChangeReq.cmdRedrawPart: 
	  			assert(oWidget instanceof CurveView);
	  			((CurveView)(oWidget)).redrawData(); break; //causes a partial redraw
	  		default: 
	  	  	if(oWidget instanceof Table){ 
	  	  		Table table = (Table)oWidget;
	  	  		switch(changeReq.cmd){
	  	  		case GuiChangeReq.cmdInsert: changeTable(table, changeReq.ident, (String)changeReq.info); break;
	  	  		default: log.sendMsg(0, "GuiMainDialog:dispatchListener: unknown cmd: %d on widget %s", changeReq.cmd, descr.name);
	  	  		}
	  	  	} else if(oWidget instanceof Text){ 
	  	  		Text field = (Text)oWidget;
	  	  		switch(changeReq.cmd){
	  	  		case GuiChangeReq.cmdInsert: field.setText((String)changeReq.info); break;
	  	  		default: log.sendMsg(0, "GuiMainDialog:dispatchListener: unknown cmd: %d on widget %s", changeReq.cmd, descr.name);
	  	  		}
	  	  	} else if(oWidget instanceof LedSwt){ 
	  	  		LedSwt field = (LedSwt)oWidget;
	  	  		switch(changeReq.cmd){
	  	  		case GuiChangeReq.cmdColor: field.setColor(changeReq.ident, (Integer)changeReq.info); break;
	  	  		default: log.sendMsg(0, "GuiMainDialog:dispatchListener: unknown cmd: %d on widget %s", changeReq.cmd, descr.name);
	  	  		}
	  	  	} else {
	  	      //all other widgets:		
	  	  		switch(changeReq.cmd){  ////
	  	  		default: log.sendMsg(0, "GuiMainDialog:dispatchListener: unknown cmd %x for widget: %s", changeReq.cmd, descr.name);
	  	  		}
	  	  	}
  	  	}//switch
  	  }
  	}  
  };
  
  
  
  static class SelectionListenerForSlider implements SelectionListener
  {
  	private final UserActionGui userAction; 

  	private final WidgetDescriptor<Control> widgetInfo;
  	
  	
  	
  	public SelectionListenerForSlider(WidgetDescriptor<Control> widgetInfo, UserActionGui userAction)
		{
			this.userAction = userAction;
  		this.widgetInfo = widgetInfo;
		}

		@Override	public void widgetDefaultSelected(SelectionEvent e)
		{
			widgetSelected(e);
			
		}

		@Override	public void widgetSelected(SelectionEvent e)
		{
			// TODO Auto-generated method stub
			//String sDataPath = widgetInfo.sDataPath;
			Slider slider = (Slider)e.widget;
			int value = slider.getSelection();
			//String sParam = Integer.toString(value);
			userAction.userActionGui("sliderValue", widgetInfo, value);
		}
  	
  }
  
  
  
  /**The GUI-change-listener should be called in the dispatch-loop of the GUI-(SWT)-Thread.
   * @return The instance to call run(). 
   * Hint: run() returns after checking orders and should be called any time in the loop. 
   */
  public GuiDispatchCallbackWorker getTheGuiChangeWorker(){ return dispatchListener; }

	@Override
	public void setSampleCurveViewY(String sName, float[] values) {
		WidgetDescriptor<?> descr = indexNameWidgets.get(sName);
		if(descr == null){
  		//log.sendMsg(0, "GuiMainDialog:setSampleCurveViewY: unknown widget %s", sName);
  	} else if(!(descr.widget instanceof CurveView)) {
  		log.sendMsg(0, "GuiMainDialog:setSampleCurveViewY: widget %s fault type", sName);
  	} else {
  		((CurveView)descr.widget).setSample(values);
  		
  	}
  	
	}

	
	@Override public void redrawWidget(String sName)
	{
		WidgetDescriptor<?> descr = indexNameWidgets.get(sName);
		if(descr == null){
  		//log.sendMsg(0, "GuiMainDialog:setSampleCurveViewY: unknown widget %s", sName);
  	} else if((descr.widget instanceof CurveView)) {
  		//sends a redraw information.
  		guiChangeRequests.add(new GuiChangeReq(descr, GuiChangeReq.cmdRedrawPart, 0, null));
  		((Composite)currPanel.panelComposite).getDisplay().wake();  //wake-up the GUI-thread, it may sleep elsewhere. 
  	} else {
  	}
	}
	
	@Override
	public void setColorGridCurveViewY(String sName, int backgroundColor,
			int[] colorLines, char grid) {
		// TODO Auto-generated method stub
		
	}

	@Override
  public void setLineCurveView(String sNameView, int trackNr, String sNameLine, String sVariable, int colorValue, int style, int nullLine, float yScale, float yOffset)
	{
		WidgetDescriptor<?> descr = indexNameWidgets.get(sNameView);
		if(descr == null){
  		//log.sendMsg(0, "GuiMainDialog:setSampleCurveViewY: unknown widget %s", sNameView);
  	} else if(!(descr.widget instanceof CurveView)) {
  		log.sendMsg(0, "GuiMainDialog:setSampleCurveViewY: widget %s fault type", sNameView);
  	} else {
      CurveView view = (CurveView)descr.widget;
      view.setLine(trackNr, sNameLine, colorValue, style, nullLine, yScale, yOffset);
  	}
	}
  
	
	//TODO write in common base
	@Override public int getColorValue(String sColorName){ return propertiesGui.getColorValue(sColorName); }


	
	@Override public String getValueFromWidget(WidgetDescriptor<?> widgetDescr)
	{ final String sValue;
  	Object widget = widgetDescr.widget;
		if(widget instanceof Text){
  	  sValue = ((Text)widget).getText();
  	} else if(widget instanceof SwitchButtonSwt){
  		SwitchButtonSwt button = (SwitchButtonSwt)widget;
  		sValue = button.isOn() ? "1" : "0"; 
  	} else if(widget instanceof Button){
  		sValue = "0"; //TODO input.button.isSelected() ? "1" : "0";
  	} else {
  		sValue = "";
  	}
		return sValue;
	}
	
	
	
	public Map<String, String> getAllValues()
	{
		Map<String, String> values = new TreeMap<String, String>();
    for(WidgetDescriptor<?> input: indexNameWidgets.values()){
    	String sValue = getValueFromWidget(input);
      values.put(input.name, sValue);
    }
    return values;
	}

	@Override public String getValue(String sName)
	{ final String sValue;
		WidgetDescriptor<?> widgetDescr = indexNameWidgets.get(sName);
		if(widgetDescr !=null){
			sValue = getValueFromWidget(widgetDescr);
		} else {
			sValue = null;
		}
		return sValue;
	}
	
	
	
	/**This userAction can be used by name (calling {@link #addFocusAction(String, UserActionGui, String, String)} 
	 * to set a variable when an input field is leaved.
	 */
	private UserActionGui syncVariableOnFocus = new UserActionGui()
	{	/**Writes the value to the named variable on leaving the focus.
		 * The name of the variable is contained in the {@link WidgetDescriptor}.
		 * @see org.vishia.mainGui.UserActionGui#userActionGui(java.lang.String, org.vishia.mainGui.WidgetDescriptor, java.lang.Object[])
		 */
		@Override public void userActionGui(String sIntension, WidgetDescriptor<?> infos, Object... params)
		{
			Object oWidget = infos.widget;
			final VariableAccess_ifc variable = infos.getVariableFromContentInfo(variableContainer);
			final int ixData = infos.getDataIx();
			final String sValue;
			if(variable !=null){
				if(sIntension.equals("o")){
					if(oWidget instanceof Text){ sValue = ((Text)oWidget).getText(); variable.setString(sValue, ixData); }
					else { sValue = null; }
				} else if(sIntension.equals("i")){
					if(oWidget instanceof Text){ sValue = variable.getString(ixData); ((Text)oWidget).setText(sValue == null ? "" : sValue); }
					else { sValue = null; }
				} else throw new IllegalArgumentException("GuiPanelMng.syncVariableOnFocus: unexpected intension on focus: " + sIntension); 
			} else throw new IllegalArgumentException("GuiPanelMng.syncVariableOnFocus: variable not found: " + infos.getDataPath()); 
   	}
	};
	
	
	

	void stop(){}  //debug helper

	@Override
	public GuiShellMngIfc getShellMngIfc()
	{ if(this instanceof GuiShellMngIfc){
		  return (GuiShellMngIfc)this;
	  } else {
	  	throw new IllegalStateException("The panel isn't a Window.");
	  }
	}


}
