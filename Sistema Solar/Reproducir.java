import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.*;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Reproducir extends JPanel implements ActionListener
{
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	static String VLCLIBPATH = "/usr/lib/vlc";//Path de vcl.dll
	JPanel pvideo,pbotones;
	JButton play,mute;
	//JButton b;
	public Reproducir(Dimension d) 
	{
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLCLIBPATH);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.setPreferredSize(d);
		//setSize(900,900);
		setLayout(null);

		pvideo=new JPanel();
		pvideo.setLayout(null);
		pvideo.add(mediaPlayerComponent);
		pvideo.setBounds(0,0,(int)d.getWidth(),(int)(9*d.getHeight()/10));
		mediaPlayerComponent.setBounds(0,0,(int)pvideo.getWidth(),(int)pvideo.getHeight());
		add(pvideo);

		play=new JButton("PAUSE");
		mute=new JButton("MUTE");
		play.addActionListener(this);
		mute.addActionListener(this);

		pbotones=new JPanel();
		pbotones.setBounds(0,pvideo.getHeight(),(int)d.getWidth(),(int)(1*d.getHeight()/10));
		pbotones.setLayout(new GridLayout(1,2));
		pbotones.setBackground(Color.RED);
		pbotones.add(play);
		pbotones.add(mute);
		add(pbotones);
	}

	public void setReproduccion(String url)
	{
		mediaPlayerComponent.getMediaPlayer().playMedia("Videos/"+url+".mp4");
	}

	public void actionPerformed(ActionEvent e) 
	{  
		JButton n=(JButton)e.getSource();
		if(n==play)
		{
			if(play.getText()=="PLAY")
			{
				play.setText("PAUSE");
			}
			else
				play.setText("PLAY");
			mediaPlayerComponent.getMediaPlayer().setPause(mediaPlayerComponent.getMediaPlayer().isPlaying()?true:false);
		}
		else if(n==mute)
		{
			mediaPlayerComponent.getMediaPlayer().mute();
		}
	     
    }

}
