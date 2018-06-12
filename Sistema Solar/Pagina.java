import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import java.net.URL;
import java.net.MalformedURLException;
public class Pagina extends JPanel
{
	private JScrollPane scroll;
	private JEditorPane panel;
	public Pagina(int tamX,int tamY)
	{
		setSize(tamX,tamY);
		
		
		panel=new JEditorPane();
		panel.setEditable(false);
		panel.setContentType("text/html");
		scroll=new JScrollPane(panel);
		panel.setPreferredSize(new Dimension(tamX-10, tamY-15));
		panel.setMinimumSize(new Dimension(tamX, tamY));
		//setLayout(new BorderLayout());
		add(scroll);
		
		
	}
	public void setPagina(String nombre)
	{
		URL direccion=null;
		try
		{
			direccion=new URL("file:Paginas/"+nombre+".html");
			System.out.println(""+direccion);
			panel.setPage(direccion);
			
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, "ERROR"+ex,"Error con URL",JOptionPane.ERROR_MESSAGE);
		}

		try
		{
			
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, "ERROR"+ex,"Error con URL",JOptionPane.ERROR_MESSAGE);
		}
	}

}
