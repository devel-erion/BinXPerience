

import java.awt.Graphics;

import java.io.*;
import java.util.ArrayList;

import java.awt.*;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;

public class BinXPerience extends Canvas 
{
	private static final int BUFFER_SIZE = 3;
	private static ArrayList<Integer> listPixels = new ArrayList<Integer>();

	public void paint(Graphics graphics)
	{
		/* We would be using this method only for the sake
		 * of brevity throughout the current section. Note
		 * that the Graphics class has been acquired along
		 * with the method that we overrode. */
	}

	public static void main(String[] args) throws InterruptedException
	{ 			

		BinXPerience canvas = new BinXPerience();
		// Configuring drawing parameters
		int max_width=450; int max_height=450;
		int rect_size=3;
		
		// Setting up Window for display
		JFrame frame = new JFrame();
		frame.setSize(max_width, max_height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(canvas);
		frame.setVisible(true);

		BufferedInputStream in;
		Graphics g = canvas.getGraphics();
		byte[] buffer= new byte[BUFFER_SIZE];
		while(true)
		{
			try 
			{
				//Music
				Synthesizer synth = MidiSystem.getSynthesizer();
				synth.open();
				final MidiChannel[] mc    = synth.getChannels();
				Instrument[]        instr = synth.getDefaultSoundbank().getInstruments();
				synth.loadInstrument(instr[76]);  // Bottle Blow


				String fileName="C:\\eclipse\\eclipse.exe";
				fileName="C:\\Program Files (x86)\\Microsoft Office\\Office14\\Excel.exe";
				in = new BufferedInputStream(new FileInputStream(fileName));				
				try 
				{
					int x=0, y=0, n=0;
					
					do
					{
						n = in.read(buffer,0,BUFFER_SIZE);
						int red = new Byte(buffer[0]).intValue() & 0xFE;
						int green =new Byte(buffer[1]).intValue() & 0xFE;
						int blue=new Byte(buffer[2]).intValue()& 0xFE;
					//	System.out.println(String.format("Red=0x%02X  Green=0x%02X Blue=0x%02X",red,green, blue));
						g.setColor( new Color(red,green,blue));  
						g.fillRect(x,y, rect_size, rect_size);
						x+=rect_size;

						if (x>=max_width)
						{
							x=0;
							y+=rect_size;
							mc[2].noteOn(red,50);
							mc[3].noteOn(green,50);
							mc[4].noteOn(blue,50);
							Thread.sleep(150);
							mc[2].noteOff(red>>2);
							mc[3].noteOff(green>>2);
							mc[5].noteOff(blue>>2);
						}
						
						if (y>=max_height)
						{
							x=0;
							y=0;
						}	
					} 	while (n >=0) ;
				} 
				finally 
				{
					in.close();
				}
			} 
			catch (IOException e)
			{
				System.out.println("File was not found!");
			}	
			catch (MidiUnavailableException e) {}

		}
	}
}
