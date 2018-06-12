import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JTextField;
import javax.swing.JComboBox;
 

public class ConexionServidor implements ActionListener {
    
   
	private Socket socket; 
	
	private JComboBox jcb;


	private DataOutputStream salidaDatos;
    
	public ConexionServidor(Socket socket, 	JComboBox jcb)
	{
		this.socket = socket;
		this.jcb=jcb;
        	try
		{
            		this.salidaDatos = new DataOutputStream(socket.getOutputStream());
        	}
		catch (IOException ex)
		{
			System.out.println("Error al crear el stream de salida : " + ex.getMessage());
        	}
		catch (NullPointerException ex)
		{
           		System.out.println("El socket  de Conexion no se creo correctamente. ");
        	}
	}
 
	@Override
	public void actionPerformed(ActionEvent e)
	{
        	try
		{
			salidaDatos.writeUTF((String)jcb.getSelectedItem());
			
        	}
		catch (IOException ex)
		{
			System.out.println("Error al intentar enviar un mensaje: " + ex.getMessage());
        	}
	}
 
}
