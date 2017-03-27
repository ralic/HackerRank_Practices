package org.vishia.mainGui;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Button;
import org.vishia.byteData.VariableContainer_ifc;
import org.vishia.mainGui.PanelContent;


/**This is a unique interface for the GUI-panel-manager to build its content.
 * To work with the graphical application see {@link GuiPanelMngBuildIfc}. 
 * <br><br>
 * The add-methods return the adequate graphic-objects from the used Graphic-basic 
 * in form of an abstract typed reference. It can be casted to the expected class 
 * adequate to the used graphic-base, to use special features additionally. For the most
 * simple applications, the capability of this class and adaption may be sufficient,
 * so the return value isn't necessary to use.
 * 
 * If you want to have only simple properties, which are resolved with this interface
 * and its implementation class, you don't need the returned graphic object.
 * But you can do any more with the especial platform, then you have to cast
 * the returned graphic object to the known object of the graphic-base.
 * <br><br>
 * Generally, the widgets are addressed with a identification in String-format to do something with it. 
 * The template type should be the base type of all widgets of the basic graphic library.
 * <ul>
 * <li>Swing: javax.swing.JComponent
 * <li>SWT: org.eclipse.swt.widgets.Control
 * </ul>
 * @author Hartmut Schorrig
 *
 */
public interface GuiPanelMngBuildIfc 
{
	
  
	/**Returns the width (number of grid step horizontal) of the last element.
   * @return Difference between current auto-position and last pos.
   */
  //int getWidthLast();

  /**Registers a panel to place the widgets. The panel can be selected
   * with its name calling the {@link #selectPanel(String)} -Routine
   * <br>parameter panel:
   * <ul><li>Swing: javax.swing.JPanel
   * <li>SWT: org.eclipse.swt.widgets.Composite
   * </ul>
   * @param name Name of the panel.
   * @param panel The panel. It should be from the correct type of the base-graphic-system.
   *              If it the instance is fault, a ClassCastException is thrown.
   *         
   */
  public PanelContent<?> registerPanel(String name, Object panel);
  
  
  /**Creates a panel for tabs and registers it in the GUI.
   * @param user If not null, then this user class will be notified when a tab is selected.
   *             The user should update showed values.
   * @return The Tab-container, there the tabs can be registered.
   */
  TabPanel createTabPanel(PanelActivatedGui user);
  
  /**selects a registered panel for the next add-operations.
   * see {@link #registerPanel(String, Object)}. 
   */
  void selectPanel(String sName);

  /**Sets the position for the next widget to add in the container.
   * @param xpos x-Position in x-Units, count from left of the box.
   * @param ypos y-Position in y-Units, count from top of the box. It is the bottom line of the widget.
   *              It means ypos = 0 is not a proper value. To show a text in the first line, use ypos=2.
   */
  //void setPosition(int ypos, int xpos);
  
  /**Sets the position for the next widget to add in the container.
   * @param line y-Position in y-Units, count from top of the box. 
   *              It is either the top or bottom line of the widget, depending on height.
   *              If < 0, then the previous position is valid furthermore.
   * @param column x-Position in x-Units, count from left of the box. 
   *              If < 0, then the previous position is valid furthermore.
   * @param heigth The height of the line. If <0, then the param line respectively the current line 
   *                will be used as bottom line of the next widget, and (line-height) is the top line. 
   *                If 0 then the last value of height is taken furthermore. 
   * @param length The number of columns. If <0, then the param column is the right column, 
   *                and column-length is the left column. If 0 then the last value of length is not changed.
   * @param direction direction for a next widget, use 'r', 'l', 'u', 'd' for right, left, up, down                
   */
  public void setPosition(int line, int column, int height, int length, char direction);
  
  
  public void setFinePosition(int line, int lineFrac, int column, int columnFrac, int height, int heigthFrac, int width, int widthFrac, char direction);
  
  public void setSize(int ySize, int ySizeFrac, int xSize, int xSizeFrac);
  
  
  /**Positions the next widget right to the previous one. */
  void setNextPositionX();

  /**Adds a button
   * @param sButtonText text in the button
   * @param height in grid-units
   * @param width in grid-unigs
   * @param sCmd The command string will be transfered to the action-method
   * @param sUserAction The user action shoult be registered before 
   *         calling {@link #registerUserAction(String, UserActionGui)}
   * @param sName
   * @return
   */
  //Object addButton(String sButtonText, int height, int width, String sCmd, String sUserAction, String sName);
  public WidgetDescriptor<?> addButton(
  	String sName
  , UserActionGui action
  , String sCmd
  , String sShowMethod
  , String sDataPath
  , String sButtonText
  );
  
  
  /**Adds a button which saves its state, pressed or non-pressed.
   * 
   * @param sName
   * @param action
   * @param sCmd
   * @param sShowMethod
   * @param sDataPath
   * @param sButtonText
   * @return
   */
  public WidgetDescriptor<?> addSwitchButton(
  	String sName
  , UserActionGui action
  , String sCmd
  , String sShowMethod
  , String sDataPath
  , String sButtonText
  , String color0
  , String color1
  );
  
  
  
  
  /**Adds a Led (round)
   * @param sName
   * @return
   */
  //Object addButton(String sButtonText, int height, int width, String sCmd, String sUserAction, String sName);
  WidgetDescriptor<?> addLed(
  	String sName
  , String sShowMethod
  , String sDataPath
  );
  
  WidgetDescriptor<?> addValueBar(
  	String sName
  , String sShowMethod
  , String sDataPath
  );
  
  
  WidgetDescriptor<?> addSlider(
  	String sName
  , UserActionGui action
  , String sShowMethod
  , String sDataPath
  );
  
  
  /**Adds a table, which is able to scroll.
   * @param sName register name, used for {@link GuiPanelMngWorkingIfc#insertInfo(String, int, String).}
   * @param height The height in grid units for the appearance
   * @param columnWidths Array with width of the columns. 
   *        Each column has a fix default width per construction.
   *        It may or may not a fix widht, it may able to change by mouse actions,
   *        adequate to the possibilities of the used graphic base system. 
   * @return
   */
  Object addTable(String sName, int height, int[] columnWidths);

  /**Adds a simple text at the current position.
   * 
   * @param sText The text
   * @param size size, 'A' is small ...'E' is large.
   * @param color The color as RGB-value in 3 Byte. 0xffffff is white, 0xff0000 is red.
   * @return
   */
  Object addText(String sText, char size, int color);
  
  Object addImage(String sName, InputStream imageStream, int height, int width, String sCmd);

  
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
  void addLine(int colorValue, float xa, float ya, float xe, float ye);
    
  
  /** Adds a edit field for editing a number value.
   * <br><br>
   * The current content of the edit field is able to get anytime calling {@link GuiPanelMngWorkingIfc#getValue(String)}
   * with the given registering name.
   * <br><br>
   * To force a set of content or an action while getting focus of this field the method {@link #addActionFocused(String, UserActionGui, String)}
   * can be called after invoking this method (any time, able to change). The {@link UserActionGui#userActionGui(String, String, WidgetDescriptor, Map)}
   * is called in the GUI-thread before the field gets the focus.
   * <br><br>
   * To force a check of content or an action while finish editing the method {@link #addActionFocusRelease(String, UserActionGui, String)}
   * can be called after invoking this method (any time, able to change). The adequate userActionGui is called after editing the field.
   * <br><br>
   * If the {@link WidgetDescriptor#action} refers an instance of type {@link UserActionGui}, than it is the action on finish editing.
   * 
   * @param sName The registering name
   * @param widgetInfo The informations about the textfield.
   * @param editable true than edit-able, false to show content 
   * @param prompt If not null, than a description label is shown
   * @param promptStylePosition Position and size of description label:
   *   upper case letter: normal font, lower case letter: small font
   *   'l' left, 't' top (above field) 
   * @return
   */
  Object addTextField(WidgetDescriptor<?> widgetInfo, boolean editable, String prompt, char promptStylePosition);
  
  /**Adds a curve view for displaying values with ordinary x-coordinate.
   * The scaling of the curve view is set to -100..100 per default. 
   * @param sName Its registered name
   * @param dyGrid height in grid-units
   * @param dxGrid width in grid-units
   * @param nrofXvalues depth of the buffer for x-values. It should be 6..20 times of dx.
   * @param nrofTracks number of curves (tracks).
   * @return The Canvas Object.
   */
  Object addCurveViewY(String sName, int nrofXvalues, int nrofTracks);
  
  
  
  Object addMouseButtonAction(String sName, UserActionGui action, String sCmdPress, String sCmdRelease, String sCmdDoubleClick);

  /**Adds the given Focus action to the named widget.
   * @param sName The name of the widget. It should be registered calling any add... method.
   * @param action
   * @param sCmdEnter
   * @param sCmdRelease
   * @return
   */
  WidgetDescriptor<?> addFocusAction(String sName, UserActionGui action, String sCmdEnter, String sCmdRelease);

  /**Adds the given Focus action to the known widget.
   * @param widgetInfo
   * @param action
   * @param sCmdEnter
   * @param sCmdRelease
   */
  void addFocusAction(WidgetDescriptor<?> widgetInfo, UserActionGui action, String sCmdEnter, String sCmdRelease);

  
  /**Sets the values for a line
   * @param sName The registered name
   * @param yNull array of values for all tracks in percent from 0..100.0, where its 0-line is shown.
   * @param yOffset This value will be subtract from the input values before scale. 
   *         It is the 0-line-reference of the input values. 
   * @param yScale This value is that value, which is shown in a 10%-difference in the output window.
   */
  void setLineCurveView(String sNameView, int trackNr, String sNameLine, String sVariable, int colorValue, int style, int y0Line, float yScale, float yOffset);
  
  
  /**Sets the appearance of the graphic respectively color and grid.
   * @param sName The registered name
   * @param backgroundColor The color of the background
   * @param colorLines Array of color values for the lines. Any value should be given 
   *        as red-green-blue value in 24 bit. Additionally 
   *        <ul>
   *        <li>bit24,25, is the thickness of the line.
   *        </ul> 
   * @param grid character to determine how the grid is shown. '0' no grid, 'a'..'f' weak to strong.
   * 
   */
  void setColorGridCurveViewY(String sName, int backgroundColor, int[] colorLines, char grid);
  
	/**Gets the value to the named color. It is a method of the graphic.
	 * @param sName supported: red, green, blue, yellow
	 * @return 3 bytes intensity: bit23..16 blue, bit15..8: green, bit 7..0 red. 
	 */
	int getColorValue(String sName);
	

  
  void repaint();
  
  /**Registered a user action for a button. The register of the action should be done
   * before it is used.
   * @param name Name of the action
   * @param action what to do.
   */
  void registerUserAction(String name, UserActionGui action);
  
  UserActionGui getRegisteredUserAction(String name);
  
  
  /**Returns a Set of all fields, which are created to show.
   * @return the set, never null, possible an empty set.
   */
  public Set<Map.Entry<String, WidgetDescriptor<?>>> getShowFields();

  
  /**The GUI-change-listener should be called in the dispatch-loop of the GUI-(SWT)-Thread.
   * @return The instance to call run(). 
   * Hint: run() returns after checking orders and should be called any time in the loop. 
   */
  public GuiDispatchCallbackWorker getTheGuiChangeWorker();
  
  
	/**Creates a new window additional to a given window with Panel Manager.
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param variableContainer A Container for variables
	 * @return
	 */
	GuiShellMngBuildIfc createWindow(int left, int top, int width, int height, VariableContainer_ifc variableContainer);
	
	/**Creates a Window for a modal dialog. The window is described by the returned interface. 
	 * It can be filled with elements. The dialog is able to show and hide calling 
	 * {@link GuiShellMngIfc#setWindowVisible(boolean)}. The interface therefore can be get calling
	 * {@link GuiShellMngBuildIfc#getShellMngIfc()}.
	 * @param title
	 * @return
	 */
	GuiShellMngBuildIfc createModalWindow(String title);
	
	
	FileDialogIfc createFileDialog();
	
	GuiShellMngIfc getShellMngIfc();

}
