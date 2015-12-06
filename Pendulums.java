import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Pendulums extends Applet implements MouseListener, MouseMotionListener, KeyListener
{
	PixelShader shader;

	PlayBar pb;

	double TIMEPERIOD=0;
	int FRAMECOUNT=0;
	double PREVP=0;

	double SCALE=1;

	double VIEWDIST=400;
	double ZOOM=1;

	double SENSITIVTY=.004;
	double MSENS=2,ASENS=.5;

	int MAXPOINTS=200,INPUTDELAY=0;

	double MSENSP=MSENS;

	int mode=1,NUMSIDES=7, numP=0;

	double time=0,timeStep=0.05,timeScale=.7;
	double startingAngle=Math.PI/5;

	int BOUND=400;

	double sysTime=0,sysTimeP=0;

	public ArrayList<Point> gPoints=new ArrayList<Point>();
	ArrayList<Polygon> shapes= new ArrayList<Polygon>();
	ArrayList<Pend> pends = new ArrayList<Pend>();
	ArrayList<DataSet> paths = new ArrayList<DataSet>();

	Point3D pos,direction=new Point3D(0,0,0),gDirection=new Point3D(0,Math.PI/2,0);

	int XBOUND, YBOUND;

	boolean init=true,first=true;

	BufferedImage image;

	Graphics iPage;

	Point mP=new Point(0,0),mPP=new Point(0,0),mPC=new Point(0,0);

	public double calcPends(int nP)
	{
		pends = new ArrayList<Pend>();
		paths = new ArrayList<DataSet>();
		double tStop=0;
		for(int i=0;i<=nP;i++) {
			pends.add(new Pend(Math.pow(60/(2*Math.PI*(51+i)),2)*10000,startingAngle,new Point3D(-500.0/nP*i+250,0,0)));
			if(i==0)
				tStop=51*pends.get(0).getPeriod();
			paths.add(new DataSet(pends.get(i).getPath(.005,tStop)));
		}
		return tStop;
	}

	public void paint(Graphics p)
	{
		Dimension appletSize = this.getSize();
	 	XBOUND = appletSize.height;
		YBOUND = appletSize.width;
		if(init)
		{
			int numPends=15;
			calcPends(numPends);


	   		pos=new Point3D(-YBOUND/2,0,-350);

	   		image = new BufferedImage(YBOUND,XBOUND,BufferedImage.TYPE_3BYTE_BGR);
	   		shader=new PixelShader(image.getGraphics(),XBOUND,YBOUND,VIEWDIST);
	   		pb= new PlayBar(image.getGraphics(),35,XBOUND,YBOUND);
			init=false;
			this.addKeyListener(this);
			this.addMouseListener( this );
	 		this.addMouseMotionListener( this );
			sysTimeP=System.nanoTime();
		}

		iPage=image.getGraphics();
		shader.page=iPage;

		iPage.setColor(Color.WHITE);
		iPage.fillRect(0,0,YBOUND,XBOUND);

		shapes=new ArrayList<Polygon>();
		double centerRadius=10;

		for(int i=0;i<pends.size();i++)
		{
			pends.get(i).angle=paths.get(i).interpolate(time%paths.get(i).data.get(paths.get(i).data.size()-1).x);
			ArrayList<Polygon> add= pends.get(i).getShapes(NUMSIDES);
			for(Polygon pg : add)
				shapes.add(pg);
		}
		/*Prism prism = new Prism(new Point3D(0,0,-275),NUMSIDES,centerRadius,550);
		prism.color=Color.GREEN;
		prism.rotate(new Point3D(0,0,0),0,Math.PI/2,0);
		shapes.add(prism);*/
		sysTime=System.nanoTime();


		if(pb.playing) {
			pb.progress+=(sysTime-sysTimeP)/paths.get(0).data.get(paths.get(0).data.size()-1).x/1000000000*timeScale;
			pb.progress%=1;
		}
		time=pb.progress*paths.get(0).data.get(paths.get(0).data.size()-1).x;
		sysTimeP=sysTime;


	//	gDirection.y+=0.001;



		/*shapes.get(shapes.size()-2).explode(SCALE,new Point3D(initPt.x,initPt.y+CUBEWIDTH/2,initPt.z));
		shapes.get(shapes.size()-1).explode(SCALE,new Point3D(initPt.x,initPt.y/2,initPt.z));*/

		for(Polygon pg : shapes)
			pg.rotate(new Point3D(0,0,0),gDirection.x,gDirection.y,gDirection.z);

		pan2(new Point3D(-pos.x,-pos.y,-pos.z));

		for(Polygon pg : shapes)
		{
			pg.rotate(new Point3D(pos.x+XBOUND/2,pos.y+YBOUND/2,pos.z-VIEWDIST),direction.x,direction.y,direction.z);
		}

		shader.drawDepthSorted(shapes);

		iPage.setColor(Color.BLACK);

		pb.printBar();

		p.drawImage(image,0,0,new Color(255,255,255),null);

		repaint();

	}

	public void stop()
	{

	}

   public void keyPressed(KeyEvent e) {



   		if(e.getKeyCode()==KeyEvent.VK_LEFT)
   		{
   			gDirection.y-=.1*ASENS;
   		}
   		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
   		{
   			gDirection.y+=1*ASENS;
   		}
   		if(e.getKeyCode()==KeyEvent.VK_UP)
   		{
   			gDirection.z+=.1*ASENS;
   		}
   		if(e.getKeyCode()==KeyEvent.VK_DOWN)
   		{
   			gDirection.z-=.1*ASENS;
   		}
   		if(e.getKeyCode()==KeyEvent.VK_R)
   		{
   			sysTimeP=time;
   		}
   		try {
				Thread.sleep(INPUTDELAY);
				}
			catch(InterruptedException exc) {
				}
   		repaint();

	}

	public void keyReleased(KeyEvent e) {

  	}

	public void keyTyped(KeyEvent e) {

	}

	public void pan(Point3D d)
	{
		pos.x-=d.x;
		pos.y-=d.y;
		pos.z-=d.z;
	}

	public void pan2(Point3D d)
	{
		for(Polygon p : shapes)
			p.pan(d);
	}

	public void update( Graphics g ) {
		paint(g);
   }

	public void mouseEntered( MouseEvent e ) {
      // called when the pointer enters the applet's rectangular area
   }
   public void mouseExited( MouseEvent e ) {
      // called when the pointer leaves the applet's rectangular area
   }
   public void mouseClicked( MouseEvent e ) {



   }
   public void mousePressed( MouseEvent e ) {  // called after a button is pressed down

   	mPP=mP;
	mP=new Point(e.getX(),e.getY());
	mPC=new Point(e.getX(),e.getY());
	pb.click(mP);

   }
   public void mouseReleased( MouseEvent e ) {  // called after a button is released


	}
   public void mouseMoved( MouseEvent e ) {  // called during motion when no buttons are down

		mPP=mP;
		mP=new Point(e.getX(),e.getY());
		pb.mouseHover(mP);
   }
   public void mouseDragged( MouseEvent e ) {  // called during motion with buttons down

		pan(new Point3D(0,0,0));
		mPP=mP;
		mP=new Point(e.getX(),e.getY());
		if(!pb.barContains(mPC))
		{
			gDirection.y+=SENSITIVTY*(mP.x-mPP.x);
			gDirection.z+=(mP.y-mPP.y)*SENSITIVTY;						// Look Control
		}
		else
			pb.click(mP);



	   	repaint();

   }

   public Point3D rotate(Point3D pt, Point3D cent, double ang1, double ang2, double ang3)
   {
   		Point3D out=pt;
   		Vect xy=new Vect(new Point(cent.x,cent.y),new Point(pt.x,pt.y));
   		xy=new Vect(xy.getAngle()+ang1,xy.getMag());
   		out.x=pt.x+xy.getX();
		out.y=pt.y+xy.getY();

		Vect xz=new Vect(new Point(cent.x,cent.z),new Point(pt.x,pt.z));
   		xz=new Vect(xz.getAngle()+ang2,xz.getMag());
   		out.x=pt.x+xz.getX();
		out.z=pt.z+xz.getY();

		Vect yz=new Vect(new Point(cent.y,cent.z),new Point(pt.y,pt.z));
   		yz=new Vect(yz.getAngle()+ang3,yz.getMag());
   		out.y=pt.y+yz.getX();
		out.z=pt.z+yz.getY();

		return out;
   }

   public void zoom(double z,Point3D pt)
   {
		for(Polygon pg : shapes)
		{
			for(Line3D line : pg.lines )
			{
				Vect3D direc= new Vect3D(pt,line.point1);
				direc.multiply(z);
				line.point1=direc.getPointFrom(pt);

				direc= new Vect3D(pt,line.point2);
				direc.multiply(z);
				line.point2=direc.getPointFrom(pt);
			}
		}
   }

}