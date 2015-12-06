import java.awt.*;

public class PlayBar
{
	public int x;
	public int y;
	public int xB;
	public int yB;
	Graphics page;
	public double rangeF;
	public double rangeC;
	public double value;
	public Color color=Color.BLACK;
	public int last=0;
	public boolean pSelected=false;
	public boolean playing=true;
	public double progress=0;

	public PlayBar(Graphics p, int barWidth, int XWIDTH, int YWIDTH)
	{
		page=p;
		x=0;
		y=YWIDTH-barWidth;
		xB=XWIDTH;
		yB=barWidth-1;
	}

	public void printBar()
	{
//		last=getSliderX();
//		color=calcColor(last);
//		page.setColor(color);
//		page.fillRect(x,y,xB,yB);
//		page.setColor(new Color(255,255,255));
//		page.fillRect(x+BORDER,y+BORDER,xB-2*BORDER,yB-2*BORDER);
//		page.setColor(Color.BLUE);
//		int [] xPoints={last,last-ARROWWIDTH/2,last+ARROWWIDTH/2};
//		int [] yPoints={y,y+yB,y+yB};
//		page.fillPolygon(xPoints,yPoints,3);
//		page.setColor(Color.BLACK);
//		page.drawString(label,x+5+BORDER,y+5+yB/2);

		int arrowBorder=(int)(yB*.3);
		arrowBorder/=Math.sqrt(2);

		int [] xAPoints = {x+arrowBorder,x+yB-arrowBorder,x+arrowBorder};
		int [] yAPoints = {y+yB-arrowBorder,y+yB/2,y+arrowBorder};

		if(!pSelected)
			page.setColor(Color.BLUE);
		else
			page.setColor(Color.GREEN);
		page.fillRect(x,y,yB,yB);
		page.setColor(Color.BLACK);
		page.drawRect(x,y,xB,yB);
		if(!playing)
			page.fillPolygon(xAPoints,yAPoints,3);
		else if(playing)
		{
			double lWidth=.2;
			page.fillRect((int)(x-lWidth*yB/2+yB/3),(int)(y+lWidth*yB)+1,(int)(lWidth*yB),(int)(yB*(1-lWidth*2))+1);
			page.fillRect((int)(x-lWidth*yB/2+2*yB/3),(int)(y+lWidth*yB)+1,(int)(lWidth*yB),(int)(yB*(1-lWidth*2))+1);
		}
		page.setColor(Color.RED);
		int rectWidth=6;
		page.fillRect((int)(yB+progress*(xB-yB-rectWidth)),y+1,rectWidth,yB-1);

	}

	public boolean mouseHover(Point mp)
	{
		if(mp.y>y) {
			if(mp.x<yB)
			{
				pSelected=true;
			}
			else
				pSelected=false;
		}
		else
			pSelected=false;
		return pSelected;
	}

	public void click(Point mp)
	{
		if(toggleContains(mp))
		{
			playing=!playing;
		}
		else if(barContains(mp))
		{
			progress=(mp.x-yB)/(xB-yB);
		}
	}
	public boolean toggleContains(Point mp)
	{
		return mp.y>y&&mp.x<yB;
	}
	public boolean barContains(Point mp)
	{
		return mp.y>y&&mp.x>yB;
	}
}