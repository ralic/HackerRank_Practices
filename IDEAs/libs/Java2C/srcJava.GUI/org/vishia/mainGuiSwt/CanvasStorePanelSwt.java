package org.vishia.mainGuiSwt;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.vishia.mainGui.CanvasStorage;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

/**Class to store some graphical figures to draw it in its {@link #drawBackground(GC, int, int, int, int)}-routine.
 * The figures are stored with its coordinates and it are drawn if necessary. 
 * <br><br>
 * This class is a org.eclipse.swt.widgets.Composite. 
 * It can contain some GUI-Elements like Button, Text, Label, Table etc from org.eclipse.swt.widgets.
 * The graphical figures are shown as background than.
 * 
 * @author Hartmut Schorrig
 *
 */
public class CanvasStorePanelSwt extends Canvas // implements CanvasStorePanel //
{
	
	
	/**The storage for the Canvas content. */
	CanvasStorage store = new CanvasStorage(){

		@Override public void redraw()
		{
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	//@Override 
	public Composite xxxgetGuiContainer(){
		return this;
	}

	
	
	
	
	//class MyCanvas extends Canvas{
	
  /**The listener for paint events. It is called whenever the window is shown newly. */
  private PaintListener paintListener = new PaintListener()
  {

		@Override
		public void paintControl(PaintEvent e) {
			// TODO Auto-generated method stub
			GC gc = e.gc;
			drawBackground(e.gc, e.x, e.y, e.width, e.height);
			stop();
		}
  	
  };
	
	private static final long serialVersionUID = 6448419343757106982L;
	
  private Color currColor;
	
	public CanvasStorePanelSwt(Composite parent, int style, Color backGround)
	{ super(parent, style);
	  currColor = getForeground();
		addPaintListener(paintListener);
		setBackground(backGround);
	}
	
	public void xxxsetForeground(Color color){
    currColor = color;		
	}
	
	
	
  @Override
  public void drawBackground(GC g, int x, int y, int dx, int dy) {
  	//NOTE: forces stack overflow because calling of this routine recursively: super.paint(g);
  	
  	for(CanvasStorage.PaintOrder order: store.paintOrders){
  		switch(order.paintWhat){
  		case CanvasStorage.paintLine:{
  			g.setForeground(order.color);
  	  	g.drawLine(order.x1, order.y1, order.x2, order.y2);
  	  } break;
  		default: throw new IllegalArgumentException("unknown order");
  		}
  	}
  }	
	//};
	
  void stop(){} //debug
  
}

