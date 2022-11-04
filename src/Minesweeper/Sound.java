package Minesweeper;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	public void beepSound(URL url){
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void beep(){
		beepSound(getClass().getResource("/imgs/beep.wav"));
	}
	
	public void beep2(){
		beepSound(getClass().getResource("/imgs/beep2.wav"));
	}
	
	public void boom(){
		beepSound(getClass().getResource("/imgs/boom.wav"));
	}
	
	public void tada(){
		beepSound(getClass().getResource("/imgs/tada.wav"));
	}
}