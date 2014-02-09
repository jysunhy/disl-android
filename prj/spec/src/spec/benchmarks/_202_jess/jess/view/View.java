package spec.benchmarks._202_jess.jess.view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import spec.benchmarks._202_jess.NullDisplay;
import spec.benchmarks._202_jess.jess.*;

/**
 A nifty graphical Rete Network viewer for Jess

 @author E.J. Friedman-Hill (C)1997
*/

class NetworkViewer extends Panel implements Observer
{
  Rete m_engine;
  Vector m_rows = new Vector();
  Hashtable doneNodes = new Hashtable();
  Label lbl;
  long lastMD = 0;

  public NetworkViewer(Rete engine)
  {
    m_engine = engine;
    setLayout(new BorderLayout());
    add("South", lbl = new Label());

    // Build the network
    
    for (int i=0; i<m_engine.compiler().roots().size(); i++)
      buildNetwork(((Successor) m_engine.compiler().roots().elementAt(i)).node, 1);
    message("Network complete");
  }
  
  public void update(Observable obs, Object obj)
  {
    doneNodes = new Hashtable();
    m_rows = new Vector();
    for (int i=0; i<m_engine.compiler().roots().size(); i++)
      buildNetwork(((Successor) m_engine.compiler().roots().elementAt(i)).node,
                   1);
    message("Network complete");
    repaint();
  }

  NodeView buildNetwork(Node n, int depth)
  {
    message("Building at depth " + depth);
    NodeView nv;
    if ((nv = (NodeView) doneNodes.get(n)) != null)
      // We've done this one already
      return nv;

    Vector row;
    if (m_rows.size() < depth)
      {
        row = new Vector();
        m_rows.addElement(row);
      }
    else
      row = (Vector) m_rows.elementAt(depth-1);
    
    nv = new NodeView(n, depth-1, row.size());
    doneNodes.put(n,nv);
    row.addElement(nv);
    for (int i=0; i<n.succ().size(); i++)
      {
        Successor s = (Successor) n.succ().elementAt(i);
        nv.addConnection(buildNetwork(s.node, depth + 1), s.callType);
      }
    return nv;
  }

  public void paint(Graphics g)
  {
    for (int i=0; i<m_rows.size(); i++)
      {
        Vector row = (Vector) m_rows.elementAt(i);
        for (int j=0; j<row.size(); j++)
          {
            NodeView nv = (NodeView) row.elementAt(j);
            nv.paint(g);
          }
      }
  }
  
  public boolean mouseUp(Event e, int x, int y)
  {
    long t = System.currentTimeMillis();
    if ((t - lastMD) < 500)
      {
        int rowidx = y / NodeView.HEIGHT;
        int colidx = x / NodeView.WIDTH;
        message ("No node in row " + rowidx + ", col " + colidx);
        if (m_rows.size() < rowidx + 1)
          return false;
        Vector row = (Vector) m_rows.elementAt(rowidx);
        if (row.size() < colidx + 1)
          return false;
        NodeView nv = (NodeView) row.elementAt(colidx);
        nv.fullDisplay();
        message("OPEN!");
        return true;
      }
    lastMD = System.currentTimeMillis();
    return false;
  }

  public boolean mouseMove(Event e, int x, int y)
  {
    int rowidx = y / NodeView.HEIGHT;
    int colidx = x / NodeView.WIDTH;
    message ("No node in row " + rowidx + ", col " + colidx);
    if (m_rows.size() < rowidx + 1)
      return false;
    Vector row = (Vector) m_rows.elementAt(rowidx);
    if (row.size() < colidx + 1)
      return false;
    NodeView nv = (NodeView) row.elementAt(colidx);
    nv.textDisplay(this);
    return true;
  }



  public void message(String s)
  {
    lbl.setText(s);
  }

}

class NodeView
{
  static final int WIDTH = 20;
  static final int HEIGHT = 20;
  static final int SIZE = 10;
  static final int MARGIN = (WIDTH-SIZE)/2;
  static final int HALF = SIZE/2;
  
  int top, left;
  Node m_node;
  Color c;
  Vector conn = new Vector();
  
  Color[] ctColor = { Color.blue, Color.orange, Color.green, Color.red };

  public NodeView(Node node, int row, int col)
  {
    m_node = node;
    top = row*HEIGHT + MARGIN;
    left = col*WIDTH + MARGIN;

    if (m_node instanceof Node1)
      c = Color.red;
    else if (m_node instanceof NodeNot2)
      c = Color.yellow;
    else if (m_node instanceof Node2)
      c = Color.green;
    else if (m_node instanceof NodeTerm)
        if (!((NodeTerm) m_node).active())
          c = Color.white;
        else
          c = Color.cyan;
    else
      c = Color.black;
  }

  public void paint(Graphics g)
  {
    g.setColor(c);
    g.fillRect(left, top, SIZE, SIZE);
    // g.setColor(Color.black);
    // g.drawString("" + conn.size(), left + 2, top + 5);
    for (int i=0; i<conn.size(); i++)
      {
        NVSucc nvs = (NVSucc) conn.elementAt(i);
        g.setColor(ctColor[nvs.callType]);
        g.drawLine(left+HALF, top+SIZE, nvs.nv.left+HALF, nvs.nv.top);
      }

    
  }

  public void textDisplay(NetworkViewer vwr)
  {
      vwr.message(m_node.toString());
  }

  public void fullDisplay()
  {
      final Frame f = new Frame("Node View");
      f.setLayout(new BorderLayout());
      Button b = new Button("Quit");
      b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          f.dispose();
        }
      });
      TextArea ta = new TextArea(40, 20);
      ta.setEditable(false);
      f.add("South", b);
      f.add("Center", ta);
      ta.setText(describeNode());

      f.resize(200,200);
      f.validate();
      f.show();
  }


  String describeNode()
  {
    String desc = m_node.toString();
    
    return desc;


  }

  public void addConnection(NodeView nv, int callType)
  {
    conn.addElement(new NVSucc(nv, callType));
  }
  
}

class NVSucc
{
  int callType;
  NodeView nv;
  NVSucc(NodeView nv, int callType)
  {
    this.nv = nv;
    this.callType = callType;
  }
       
}

public class View implements Userfunction {

  int _name = RU.putAtom("view");
  public int name() { return _name; }
  
  public Value Call(ValueVector vv, Context context) throws ReteException
  {
    final Frame f = new Frame("Network View");
    f.setLayout(new BorderLayout());
    NetworkViewer nv = new NetworkViewer(context.engine());
    f.add("Center", nv);
    Button b = new Button("Quit");
    b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          f.hide();
          f.dispose();
        }
    });
    f.add("South", b);
    f.resize(500,500);
    f.validate();
    f.show();
    
    // NullDisplay implements Observable
    if (context.engine().display() instanceof Observable)
      ((Observable) context.engine().display()).addObserver(nv);

    return Funcall.TRUE();
  }
}
