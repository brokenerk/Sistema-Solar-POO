import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;
import java.util.*;

import java.net.UnknownHostException;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import java.net.*;

import java.awt.Toolkit;

import com.sun.j3d.utils.picking.*;

public class SistemaSolar extends MouseAdapter implements Runnable,ActionListener
{
	


	Thread hilo;
	private JComboBox<String> jcb;	
	private double vectores[]={0.75,0.78,0.91,0.95,1.07,1.12,1.2,1.25,1.31};//0.745,0.78,0.91,0.95,1.07,1.12,1,209,1.308
//revisar saturno y urano
	//PAGINA
	Pagina pag;
	
	
	//PANELES DEL FRAME
	private JPanel pCanvas=new JPanel();
	private JPanel pVideo=new JPanel();
	private JPanel pNavegador=new JPanel();
	private JPanel pComboBox=new JPanel();
	
	//VISTAS
	protected Transform3D viewTransform = new Transform3D();
	protected Vector3f viewVector;
	protected ViewingPlatform vp = null;
	protected TransformGroup viewTG = null;
	
	//Para recoger objs del group

	private PickCanvas pickCanvas;
	//TGPlanetas
	static final int num_planetas=9;//Se le suma el sol
	TransformGroup[] TGPlanetas;
	Planeta_1[] Planetas;
	//SATÉLITES
	static final int num_satelites=3;
	TransformGroup[] Satelites;	
	/*VENTANAS*/
	JFrame VPrincipal;
	
	/*Variables para el Canvas*/
	BranchGroup group;
	GraphicsConfiguration config;
	Canvas3D canvas;
	SimpleUniverse universo;

	//REVISAR   JDialog d
		VentanaConfiguracion Ventanita;
	/*Para sockets*/
	private int puerto;
	private String host;
	private Socket socket;
	private String mensaje;
	private DataOutputStream salidaDatos;
	/*Para video*/
	Reproducir video;

	public SistemaSolar()
	{
		
		Planetas= new Planeta_1[num_planetas];
		TGPlanetas=new TransformGroup[num_planetas];
		Satelites=new TransformGroup[num_satelites];
		
		CrearSol();
		CrearMercurio();
		CrearVenus();	
		CrearTierra();
		CrearMarte();
		CrearJupiter();
		CrearSaturno();
		CrearUrano();
		CrearNeptuno();
		
		
		for(int i=0;i<num_planetas;i++)
			TGPlanetas[i]=Planetas[i].RSEje;

		CrearVentanaPrincipal();
		Ventanita = new VentanaConfiguracion(VPrincipal);
		
		

		crearSocket();
		



		
		hilo=new Thread(this);
		hilo.start();
		recibirMensajesServidor();
		
		
	}
	public void crearSocket()
	{
		puerto= Ventanita.getPuerto();
		host= Ventanita.getHost();
		try 
		{
            		socket = new Socket(host, puerto);
       		} 
		catch (UnknownHostException ex) 
		{
			JOptionPane.showMessageDialog(VPrincipal,"No se ha podido conectar con el servidor (" + ex.getMessage() + ").","No sirvio kappa",JOptionPane.ERROR_MESSAGE);
        	} 
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(VPrincipal,"No se ha podido conectar con el servidor (" + ex.getMessage() + ").","No sirvio kappa",JOptionPane.ERROR_MESSAGE);
		}

		try
		{
            		salidaDatos = new DataOutputStream(socket.getOutputStream());
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
	
	public void escribirSocket(String nombre)
	{
		try
		{
			salidaDatos.writeUTF(nombre);
			
        	}
		catch (IOException ex)
		{
			System.out.println("Error al intentar enviar un mensaje: " + ex.getMessage());
        	}
	}
	public void run()
	{
		try
		{
			while(true)
			{

				Estrella[] es;
				int num=10;
				es=new Estrella[num];
				for(int i=0;i<es.length;i++)
				{
					es[i]=new Estrella();
					group.addChild(es[i].bg);
				}
				hilo.sleep(60000);
				System.out.println(""+group.numChildren());
				/*for(int i=0;i<2;i++)
				{
					group.removeChild(es[i].bg);
				}*/
				//System.out.println(""+group.numChildren());
			}
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,"ERROR: "+ ex,"ERROR",JOptionPane.ERROR_MESSAGE);
		}
	}



	public void crearFondo()
	{
		Background fondo;
		fondo=new Background();
		fondo.setApplicationBounds(new BoundingSphere());
		fondo.setColor(1.0f, 1.0f, 1.0f);
		TextureLoader textura = new TextureLoader( "fondo_1.jpg", null );
		ImageComponent2D img = textura.getImage( );
		fondo.setImage(img);
		group.addChild(fondo);
	}


	public void CrearCanvas(int x,int y)
	{
		
		int i;
		group = new BranchGroup();

		group.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		group.setCapability(Group.ALLOW_CHILDREN_READ);
		group.setCapability(Group.ALLOW_CHILDREN_WRITE);
		group.setCapability(BranchGroup.ALLOW_DETACH);


		crearFondo();
		
		for(i=0;i<num_planetas;i++)
			group.addChild(TGPlanetas[i]);
		for(i=0;i<num_satelites;i++)
			group.addChild(Satelites[i]);


		config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
	    	canvas.setSize(x+(x)/10,y+(y)/10);
		
		universo = new SimpleUniverse(canvas);


		universo.getViewingPlatform().setNominalViewingTransform();

		

		for(i=0;i<num_planetas;i++)
		{
			
			Planetas[i].RSEl.setCapability(Group.ALLOW_LOCAL_TO_VWORLD_READ);
			Planetas[i].RSEl.setCapability(Group.ALLOW_CHILDREN_EXTEND);
			Planetas[i].RSEl.setCapability(Group.ALLOW_CHILDREN_WRITE);
			Planetas[i].RSEl.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		}

		vp=universo.getViewingPlatform();//No se para qué sirve
		viewTG = vp.getViewPlatformTransform();
		//
    		universo.addBranchGraph(group);
		pickCanvas=new PickCanvas(canvas,group);
		pickCanvas.setMode(PickCanvas.BOUNDS);
	    	canvas.addMouseListener(this);
		
	
	}


	public void CrearVentanaPrincipal()
	{

		Vector<String> nomPlanet=new Vector<String>();
		for(int i=0;i<num_planetas;i++)	    	
			nomPlanet.addElement(Planetas[i].nombre); 
		
	    	jcb=new JComboBox<String>(nomPlanet);
		jcb.addActionListener(this);
	    	
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("d: "+d);
		VPrincipal=new JFrame("Planetario: Sistema Solar");
	   	VPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
	    	VPrincipal.setLayout(null);
		double x=d.getWidth()+300;
		double y=d.getHeight()+100;
		VPrincipal.setSize((int)(x-x*(0.2)),(int)(y-y*(0.2)));
		
		int tx=VPrincipal.getWidth();
		int ty=VPrincipal.getHeight();
		
		int sepX=(int)((tx*3)/100);
		int sepY=(int)((ty*3)/100);
		int ancho=(int)(tx-3*sepX)/2;
		int alto=(int)(ty-3*sepY)/2;
		int altoCan=(int)((ty-3*sepY)*9)/10;
		int altoCombo=(int)((ty-3*sepY))/10;
		CrearCanvas(ancho,altoCan);
		System.out.println("alto: "+altoCan);
		//pCanvas.setBackground(Color.BLUE);
		//pCanvas.setBounds(sepX,sepY,ancho,altoCan);
		pCanvas.setBounds(sepX,sepY,ancho,altoCan);
		
		pCanvas.add(canvas);
		
		pComboBox.setBounds(sepX,altoCan+2*sepY,ancho,altoCombo);
		pComboBox.add(jcb);
		
		pag=new Pagina(ancho,alto);
	
		
		pNavegador.setBounds(ancho+2*sepX,alto+2*sepY,ancho,alto);
		pNavegador.add(pag);
		
		//pVideo.setBackground(Color.BLUE);
//--------------------------------------------------------------------------------------------------
		pVideo.setBounds(ancho+2*sepX,sepY,ancho,alto);
		video=new Reproducir(pVideo.getSize());
		video.setPreferredSize(pVideo.getSize());
		pVideo.add(video);
		
		
		VPrincipal.add(pCanvas);
		VPrincipal.add(pComboBox);
		VPrincipal.add(pVideo);
		VPrincipal.add(pNavegador);

	    	//VPrincipal.pack(); 
		VPrincipal.setVisible(true);

	}
	
	
	public void mouseClicked(MouseEvent e)
	{
		int i,n=0;
		pickCanvas.setShapeLocation(e);//LE DA LA POSICIÓN(SUPONGO)
		PickResult result = pickCanvas.pickClosest();
		if(result!=null)
		{
			Primitive p = (Primitive)result.getNode(PickResult.PRIMITIVE);
		       Shape3D s = (Shape3D)result.getNode(PickResult.SHAPE3D);

			if(p!=null)
			{
					
				escribirSocket((String)p.getUserData());
				

			}
		}
		else
		{
			//JOptionPane.showMessageDialog(null,"No seleccionaste nada","SIN OBJETO",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		escribirSocket((String)jcb.getSelectedItem());
        	
	}
	



	
	public static void main(String[] s)
	{
		new SistemaSolar();
		//Reproducir video=new Reproducir(new Dimension(205,205));
		
	}

	
	
	
	
	public void recibirMensajesServidor()
	{
		// Obtiene el flujo de entrada del socket
		DataInputStream entradaDatos = null;
		
		try {
		    entradaDatos = new DataInputStream(socket.getInputStream());
		}
		catch (IOException ex)
		{
		    System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
		}
		catch (NullPointerException ex)
		{
		    System.out.println("El socket no se creo correctamente. ");
		}
		
		// Bucle infinito que recibe mensajes del servidor
		boolean conectado = true;
		while (conectado)
		{
		    	try
			{
		   	    	mensaje = entradaDatos.readUTF();
				System.out.println(""+mensaje);		   	    	
				vista(mensaje);
				
		   	}
			catch (IOException ex)
			{
				System.out.println("Error al leer del stream de entrada: " + ex.getMessage());
				conectado = false;
		    	}
			catch (NullPointerException ex)
			{
				System.out.println("El socket no se creo correctamente. ");
				conectado = false;
			}
		}
    	}
	public void vista(String nom)
	{	
		int n=0;
		for(int i=0;i<num_planetas;i++)
		{
			if(nom.equals(Planetas[i].nombre))
			{
				n=i;
				break;
			}
		}
		viewVector = new Vector3f(.0f,.0f,(float)vectores[n]);
		viewTransform.setTranslation(viewVector);
		viewTG.setTransform(viewTransform);
		vp.detach();
		Planetas[n].RSEl.addChild(vp);
		video.setReproduccion(nom);		
		//jcb.setSelectedIndex(n);
		
		pag.setPagina(nom);
	}




	public void CrearSol()
	{
		Planeta_1 s;
		s=new Planeta_1("Sol",695700f/*Radio*/,1146/*V_ROT*/,0/*V_TRAS*/,0.0f/*DIST*/,"Sol.jpg");
		Planetas[0]=s;
	}
	public void CrearMercurio()
	{
		Planeta_1 Mercurio=new Planeta_1("Mercurio",77420f,1146,1500,0.05f,"Mercurio.jpg");
		Planetas[1]=Mercurio;		
		
	}
	public void CrearVenus()
	{
		Planeta_1 Venus=new Planeta_1("Venus",97420f,1146,1700,0.1f,"Venus.jpg");
		Planetas[2]=Venus;			
		
	}
	public void CrearTierra()
	{
		
		Planeta_1 Tierra=new Planeta_1("Tierra",127420f,1146,2000,0.2f,"Tierra.jpg");		
		TGPlanetas[2]=Tierra.RSEje;
		Planetas[3]=Tierra;	
		Satelite Luna=new Satelite("Luna",60000f/*Radio*/,2000/*V_ROT*/,2000/*V_TRAS*/,2000/*Vel_Tras_Planeta*/,0.01f/*Dis_Planeta*/,0.2f/*Distancia al sol*/,0.02f,"Luna.jpg");
		Satelites[0]=Luna.MovFinal;
	}
	public void CrearMarte()
	{
		
		Planeta_1 Marte=new Planeta_1("Marte",107420f/*Radio*/,1146/*V_ROT*/,2200/*V_TRAS*/,0.25f/*DIST*/,"Marte.jpg");
		//TGPlanetas[3]=Marte.RSEje;
		Planetas[4]=Marte;	
	}
	public void CrearJupiter()
	{
		Planeta_1 Jupiter=new Planeta_1("Jupiter",407420f/*Radio*/,1146/*V_ROT*/,2400/*V_TRAS*/,0.35f/*DIST*/,"Jupiter.jpg");
		//TGPlanetas[4]=Jupiter.RSEje;
		Planetas[5]=Jupiter;	
		Satelite Ganimedes=new Satelite("Ganimedes",50000f/*Radio*/,6000/*V_ROT*/,3000/*V_TRAS*/,2400/*Vel_Tras_Planeta*/,0.065f/*Dis_Planeta*/,0.35f/*Distancia al sol*/,0.02f,"Ganimedes.jpg");
		Satelites[1]=Ganimedes.MovFinal;
		Satelite Europa=new Satelite("Europa",60000f/*Radio*/,6000/*V_ROT*/,1500/*V_TRAS*/,2400/*Vel_Tras_Planeta*/,0.05f/*Dis_Planeta*/,0.35f/*Distancia al sol*/,0.02f,"Europa.jpg");
		Satelites[2]=Europa.MovFinal;
	}
	public void CrearSaturno()
	{
		Planeta_1 Saturno=new Planeta_1("Saturno",357420f/*Radio*/,1146/*V_ROT*/,2600/*V_TRAS*/,0.4f/*DIST*/,"Saturno.jpg");
		//TGPlanetas[5]=Saturno.RSEje;
		Planetas[6]=Saturno;	
	}
	public void CrearUrano()
	{
		Planeta_1 Urano=new Planeta_1("Urano",257420f/*Radio*/,1146/*V_ROT*/,2800/*V_TRAS*/,0.5f/*DIST*/,"Urano.jpg");
		//TGPlanetas[6]=Urano.RSEje;
		Planetas[7]=Urano;	
	}
	public void CrearNeptuno()
	{
		Planeta_1 Neptuno=new Planeta_1("Neptuno",237420f/*Radio*/,1146/*V_ROT*/,3000/*V_TRAS*/,0.6f/*DIST*/,"Neptuno.jpg");
		//TGPlanetas[7]=Neptuno.RSEje;
		Planetas[8]=Neptuno;	
	}
	
}
