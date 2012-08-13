

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.applet.Applet;
import java.awt.Graphics;

import java.awt.*;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
 
 




public class BinXPerience extends Canvas {
	private static final int BUFFER_SIZE = 3;
	// 	private static int[] intArray;
	private static ArrayList<Integer> listPixels = new ArrayList<Integer>();
	
	  public void paint(Graphics graphics)
	    {
	        /* We would be using this method only for the sake
	         * of brevity throughout the current section. Note
	         * that the Graphics class has been acquired along
	         * with the method that we overrode. */
	    }
		public static void main(String[] args) throws InterruptedException{ 
			
		/*	   try {


		          


		           for (int i = 0; i < 120; ++i) {


		               try {


		                   Thread.sleep(100);


		               } catch (InterruptedException e) {}


		               mc[4].noteOn(i,200);


		           }


		       } catch (MidiUnavailableException e) {}*/


		  
			
			
		BinXPerience canvas = new BinXPerience();
		// Configuring drawing parameters
		int max_width=450; int max_height=450;
		int rect_size=3;
		
        JFrame frame = new JFrame();
        frame.setSize(max_width, max_height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        BufferedInputStream in;
        Graphics g = canvas.getGraphics();
        while(true)
        {
		try {
        //Music
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        final MidiChannel[] mc    = synth.getChannels();
        Instrument[]        instr = synth.getDefaultSoundbank().getInstruments();
        synth.loadInstrument(instr[76]);  // Bottle Blow
        
        
     
		String fileName="C:\\eclipse\\eclipse.exe";
		fileName="C:\\Program Files (x86)\\Microsoft Office\\Office14\\Excel.exe";
		//fileName="C:\\upload\\tserver.exe";
		//fileName="C:\\Program Files\\WinRAR\\WinRar.exe";
		in = new BufferedInputStream(new FileInputStream(fileName));
		byte[] buffer= new byte[BUFFER_SIZE];
	
		try {
			int n = in.read(buffer,0,BUFFER_SIZE);
			int x=0, y=0;

			while (n >=0){
				n = in.read(buffer,0,BUFFER_SIZE);
				//n = in.read(buffer,0,BUFFER_SIZE);
				int red = new Byte(buffer[0]).intValue() & 0xFE;
				int green =new Byte(buffer[1]).intValue() & 0xFE;
				int blue=new Byte(buffer[2]).intValue()& 0xFE;
				//System.out.println(String.format("Red=0x%02X  Green=0x%02X Blue=0x%02X",red,green, blue));
				g.setColor( new Color(red,green,blue));  
				g.fillRect(x,y, rect_size, rect_size);
				x+=rect_size;
				
				
				if (x>=max_width){
					x=0;
					y+=rect_size;
					/*mc[2].noteOn(red,50);
				    mc[3].noteOn(green,50);
				    mc[4].noteOn(blue,50);
				    Thread.sleep(25);
					mc[2].noteOff(red>>2);
				    mc[3].noteOff(green>>2);
				    mc[5].noteOff(blue>>2);*/
					
				}
				if (y>=max_height){
					x=0;
					y=0;
				}	
			}	
		} finally{
		in.close();
		}
		} catch (IOException e){
			System.out.println("File was not found!");
		}	
		catch (MidiUnavailableException e) {}
		
		/*Image img= getImageFromArray(convertIntegers(listPixels),100,100);
        BufferedImage image = toBufferedImage(img);
        save(image, "jpg"); */ // png okay, j2se 1.4+*
        }
	}
		
		private static BufferedImage canvasToImage(Canvas cnvs) {
	        int w = cnvs.getWidth();
	        int h = cnvs.getHeight();
	        int type = BufferedImage.TYPE_INT_RGB;
	        BufferedImage image = new BufferedImage(w,h,type);
	        Graphics2D g2 = image.createGraphics();
	        cnvs.paint(g2);
	        g2.dispose();
	        return image;
	    }
	 	
		
	public static Image getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(0,0,width,height,pixels);
        return image;
    }
	public static int[] convertIntegers(ArrayList<Integer> integers){
		int[] ret = new int[integers.size()];
		System.out.println("Size of Arraylist="+integers.size());
		 for (int i=0; i < ret.length; i++)
		    {
			    System.out.println(String.format("RGB=0x%06X ",integers.get(i).intValue()));
		        ret[i] = integers.get(i).intValue();
		    }
		return ret;
		
	}
	
	private static void save(BufferedImage image, String ext) {
        String fileName = "savingAnImage";
        File file = new File(fileName + "." + ext);
        try {
            ImageIO.write(image, ext, file);  // ignore returned boolean
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                               ": " + e.getMessage());
        }
    }
 
    private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }
}
