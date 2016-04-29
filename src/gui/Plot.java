package gui;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;
 
public class Plot extends JPanel {
    double[][] data;
    final int PAD = 20;
    
    public Plot(double[][] data, String name){
    	this.data=data;
    	JFrame f = new JFrame(name);
        f.add(this);
        f.setSize(1000,400);
        f.setLocation(200,200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        // Ordinate label.
        String s = "data";
        float sy = PAD + ((h - 2*PAD) - s.length()*sh)/2 + lm.getAscent();
        for(int i = 0; i < s.length(); i++) {
            String letter = String.valueOf(s.charAt(i));
            float sw = (float)font.getStringBounds(letter, frc).getWidth();
            float sx = (PAD - sw)/2;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }
        // Abcissa label.
        s = "x axis";
        sy = h - PAD + (PAD - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);
        // Draw lines.
        double xInc = (double)(w - 2*PAD)/(data.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        for(int i=0; i<data[0].length;i++){
        if(i%2==0)
        	g2.setPaint(Color.green);
        if(i%2!=0)
        	g2.setPaint(Color.blue);
        for(int j = 0; j < data.length-1; j++) {
            double x1 = PAD + j*xInc;
            double y1 = h - PAD - scale*data[j][i];
            double x2 = PAD + (j+1)*xInc;
            double y2 = h - PAD - scale*data[j+1][i];
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        // Mark data points.
        if(i%2==0)
        	g2.setPaint(Color.black);
        if(i%2!=0)
        	g2.setPaint(Color.red);
        for(int j = 0; j < data.length; j++) {
            double x = PAD + j*xInc;
            double y = h - PAD - scale*data[j][i];
            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
        }
    }
 
    private double getMax() {
        double max = -Integer.MAX_VALUE;
        for(int i = 0; i < data.length; i++) {
        	for(int j=0; j<data[0].length; j++){
            if(data[i][j] > max)
                max = data[i][j];
        	}
        }
        return max;
    }
 
}