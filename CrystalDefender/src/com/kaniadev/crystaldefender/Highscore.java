package com.kaniadev.crystaldefender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

public class Highscore {
	
	FileOutputStream file_output;
	OutputStreamWriter output_writer;
	FileInputStream file_input;
	InputStreamReader input_reader;
	Context context;
	
	public Highscore(Context context) {
		
		this.context = context;
		try {
			file_output = context.openFileOutput("top_highscore.txt", Context.MODE_APPEND);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void saveHighscore(int highscore) {
	
		String str = "" + highscore;
		
		try {
			context.deleteFile("top_highscore.txt");
			file_output = context.openFileOutput("top_highscore.txt", Context.MODE_APPEND);
			output_writer = new OutputStreamWriter(file_output);	
			output_writer.write(str);
			output_writer.flush();
			output_writer.close();
			
			Log.d("HIGHSCORE", "Saved into file.");
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();		
		}
	}
	
	public String loadHighscore() {
		
		String highscore = new String();
		String read_string;
		
		try {
			
			file_input = context.openFileInput("top_highscore.txt");
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		input_reader = new InputStreamReader(file_input);
		
		char[] inputBuffer = new char[10];
		
		int char_read;
		
		try {
			while ((char_read = input_reader.read(inputBuffer))>0) {
				
				read_string = String.copyValueOf(inputBuffer, 0,	char_read);
				highscore += read_string;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (highscore.length() > 1) {
			return highscore;
		} else {
			return "0";
		}
	}
}
