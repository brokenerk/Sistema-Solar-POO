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
import javax.media.j3d.Light;
public class Estrella
{
	private PointLight luz;
	private Color3f naranja=new Color3f(2.55f,1.17f,0.0f);
	private Color3f azul=new Color3f(0.45f,2.53f,2.32f);
	private float tam=0;
	private Color3f c;
	private Sphere estrella;
	public TransformGroup es;
	private float X,Y,Z;
	private Random aleat=new Random();//Si le ponemos semilla se ejecutará igual en todas las PC's
	public BranchGroup bg;
	public Estrella()
	{
		
		int opc=(int) (aleat.nextDouble() *2);
		//System.out.println(""+opc);
		if(opc==0)
		{
			c=naranja;
			tam=(float)0.01;
		}
		else
		{
			c=azul;
			tam=(float)0.03;
		}
		luz=new PointLight();
		luz.setEnable(true);
		luz.setInfluencingBounds(new BoundingSphere());
		luz.setColor(c);
		luz.setAttenuation(new Point3f(0.0f,1.0f,0.0f));
		luz.setPosition(new Point3f(-0.26f,-0.1f,2.5f));
		//group.addChild(luz);
		
		luz.setCapability(PointLight.ALLOW_POSITION_READ);
		luz.setCapability(PointLight.ALLOW_POSITION_WRITE);
		luz.setCapability(PointLight.ALLOW_ATTENUATION_READ);
		luz.setCapability(PointLight.ALLOW_ATTENUATION_WRITE);
		luz.setCapability(PointLight.ALLOW_COLOR_READ);
		luz.setCapability(PointLight.ALLOW_COLOR_WRITE);

		estrella=new Sphere(tam,Sphere.GENERATE_NORMALS,32,crearApariencia());
		es=new TransformGroup();
		es.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		es.addChild(estrella);
		es.addChild(luz);
		X=(float) (aleat.nextDouble() *(98)-45)/10;
		Y=(float) (aleat.nextDouble()*(54)-25)/10;
		Z=(float) (aleat.nextDouble()*(6)-10);
		//System.out.println("X="+X+",Y="+Y+",Z="+Z);
		Transform3D transform3D = new Transform3D();
		transform3D.setTranslation(new Vector3f(X,Y,Z));
		es.setTransform(transform3D);

		bg=new BranchGroup();
		bg.addChild(es);
		//es.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Thread vida=new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					float rA=(float)2.5;//Sirve para 	que se vea como aparece
					while(rA>0.21)
					{
											luz.setAttenuation(new Point3f(rA,rA,rA));
						rA-=0.01;
						Thread.sleep(10);

					}
					for(int i=0;i<10;i++)
					{
						float v1=(float)-0.14,v2=(float)-0.07;//Sirven para generar el efecto de destello
						float aux=(float)0.31;
						while(aux>0)
						{
							luz.setAttenuation(new Point3f(v1,v2,aux));
							aux-=0.01;
							Thread.sleep(5);
					

						}
						while(aux<0.31)
						{
							luz.setAttenuation(new Point3f(v1,v2,aux));
							aux+=0.01;
							Thread.sleep(5);
					

						}
				
					}
					luz.setAttenuation(new Point3f(0.03f,-0.17f,0.34f));
					for(int i=0;i<4;i++)
					{
						float aux=(float)0.79;
						
						while(aux>0)
						{
							luz.setAttenuation(new Point3f(0f,aux,0.34f));
							aux-=0.01;
							Thread.sleep(50);
					

						}
						while(aux<0.79)
						{
							luz.setAttenuation(new Point3f(0f,aux,0.34f));
							aux+=0.01;
							Thread.sleep(50);
					

						}

					}
					rA=(float)2.5;//Para la muerte
					while(rA>-1)
					{
						luz.setPosition(new Point3f(0.03f,0.03f,rA));
						rA-=0.01;
						Thread.sleep(15);
					}
					

				}
				catch(Exception ex){}
				
				
			}
		}
		);
		vida.start();
		
		
	}
	
	public Appearance crearApariencia()
	{
		Appearance apariencia =new Appearance();
		Material material =new Material();
		apariencia.setMaterial(material);
		return apariencia;
	}
	public PointLight getLuz()
	{
		return luz;
	}



}
