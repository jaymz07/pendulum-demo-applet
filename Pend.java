import java.io.*;
import java.util.*;
import java.awt.*;

public class Pend
{
	public double GRAVCONST=10000;
	public double angle;
	public double angleI;
	public double angVel;
	public double length;
	public double armRadius=4;
	public double sphereRadius=15;
	public Point3D center;
	public Pend(double len, double ang, Point3D cent)
	{
		angle=ang;
		length=len;
		angleI=ang;
		angVel=0.0;
		center=cent;
	}
	public void iterateVel(double h)
	{
		angVel=angVel+h*accel(angle);
	}
	public void iterateAng(double h)
	{
		angle=angle+h*angVel;
	}

	public double accel(double ang)
	{
		return -GRAVCONST/length*Math.sin(ang);
	}

	public void iterate(double timeStep)
	{
		iterateVel(timeStep);
		iterateAng(timeStep);
	}

	public ArrayList<Polygon> getShapes(int numSides)
	{
		Vect sPos = new Vect(angle,length);
		Point pPos=sPos.getPointFrom(new Point(center.y,center.z));

		ArrayList<Polygon> shapes = new ArrayList<Polygon>();
		Point3D sphCent=new Point3D(center.x,pPos.x,pPos.y);
		Sphere sph=new Sphere(sphCent,sphereRadius,numSides);
		sph.rotate(sphCent,0,0,angle);
		shapes.add(sph);
		Prism c = new Prism(new Point3D(center.x,center.y,center.z),numSides,armRadius,(int)(length-sphereRadius));
		c.rotate(center,0,0,-Math.PI/2+angle);
		c.color=Color.RED;
		shapes.add(c);
		return shapes;
	}
	public ArrayList<Polygon> getShapes(int numSides,double centerRad)
	{
		Vect sPos = new Vect(angle,length);
		Point pPos=sPos.getPointFrom(new Point(center.y,center.z));

		ArrayList<Polygon> shapes = new ArrayList<Polygon>();
		Point3D sphCent=new Point3D(center.x,pPos.x,pPos.y);
		Sphere sph=new Sphere(sphCent,sphereRadius,numSides);
		sph.rotate(sphCent,0,0,angle);
		shapes.add(sph);
		Prism c = new Prism(new Point3D(center.x,center.y,center.z+centerRad),numSides,armRadius,(int)(length-sphereRadius-centerRad));
		c.rotate(center,0,0,-Math.PI/2+angle);
		c.color=Color.RED;
		shapes.add(c);
		return shapes;
	}
	public ArrayList<Point> getPath(double timeStep,double timeStop)
	{
		ArrayList<Point> out = new ArrayList<Point>();
		double aI=angle;
		out.add(new Point(0,angle));
		double time=0;
		do
		{
			time+=timeStep;
			iterate(timeStep);
			out.add(new Point(time,angle));
		}
		while(time<timeStop);

//		out.add(new Point(time+timeStep,0));

		return out;
	}
	public double getPeriod()
	{
		ArrayList<Point> points = new ArrayList<Point>();
		for(double i=0;i<Math.PI/2;i+=Math.PI/2/1000)
		{
			double k=Math.sin(angleI/2);
			points.add(new Point(i,1.0/Math.sqrt(1-Math.pow(k*Math.sin(i),2))));
		}
		return 4*Math.sqrt(length/GRAVCONST)*(new DataSet(points)).simpIntegrate();
	}
}