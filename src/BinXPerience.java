

import java.io.*;

import java.awt.*;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class BinXPerience 
{
	private static final int BUFFER_SIZE = 4;

	public static void main(String[] args) throws InterruptedException
	{ 			

		// create the Options
		Options options = new Options();
		options.addOption( "b", "binary", true, "binary source to read" );
		options.addOption( "w", "window-width", true, "width of the window" );
		options.addOption( "h", "window-height", true, "height of the window" );
		options.addOption( "bs", "block-size", true, "block size");
		options.addOption( "i", "instrument", true, "select instrument offset ");
		options.addOption( "na", "no-audio", false, "disable audio ");

		System.out.println("Command Args: "+ Arrays.toString(args) );

		// create the command line parser
		CommandLineParser parser = new PosixParser();
		CommandLine cmdline = null ;

		// create the parser
		try {
			// parse the command line arguments
			cmdline = parser.parse( options, args );
			if (cmdline.hasOption("b")){
				System.out.println("Binary="+ cmdline.getOptionValue("b") );
			}
			if (cmdline.hasOption("bs")){
				System.out.println( "Block size="+ cmdline.getOptionValue("bs") );
			}
			System.out.println( "Window Size is "+ cmdline.getOptionValue("w") +" x "+ cmdline.getOptionValue("h") );
			System.out.println( "Instrument Offet ="+ cmdline.getOptionValue("i") );
		}
		catch( ParseException exp ) {
			// oops, something went wrong
			System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
		}

		String fileName=cmdline.getOptionValue("b");
		byte[] buffer= new byte[BUFFER_SIZE];

		Canvas canvas = new Canvas();


		// Configuring drawing parameters
		int max_width=Integer.parseInt(cmdline.getOptionValue("w")); 
		int max_height=Integer.parseInt(cmdline.getOptionValue("h"));
		int rect_size=Integer.parseInt(cmdline.getOptionValue("bs"));

		// Setting up Window for display
		JFrame frame = new JFrame();
		frame.setSize(max_width, max_height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(canvas);
		frame.setVisible(true);
		Graphics g = canvas.getGraphics();

		//Music
		Synthesizer synth;
		MidiChannel[] mc=null;
		if (!cmdline.hasOption("na")){
			try {
				synth = MidiSystem.getSynthesizer();

				synth.open();
				mc    = synth.getChannels();
				Instrument[]        instr = synth.getDefaultSoundbank().getInstruments();
				synth.loadInstrument(instr[ Integer.parseInt(cmdline.getOptionValue("i"))  ]); 
			} catch (MidiUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		while(true)
		{
			try 
			{

				BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));				
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
							if (!cmdline.hasOption("na")){
								mc[2].noteOn(red,50);
								mc[3].noteOn(green,50);
								mc[4].noteOn(blue,50);
								Thread.sleep(150);
								mc[2].noteOff(red>>2);
								mc[3].noteOff(green>>2);
								mc[5].noteOff(blue>>2);
							}
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
		}
	}
}
