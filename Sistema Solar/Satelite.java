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

public class Satelite{

	float distancia_Satelite;

	Sphere EsferaSatelite;
	TransformGroup RSEl,TrRPl,RSPl,TrRS,MovFinal;
	Appearance ApSatelite;
	TextureLoader textura;
	float Radio;
	public Satelite(String nombre,float radio,long velocidad_rot,long velocidad_tras,long vel_tras_plan,float dis,float dis_sol,float h,String Textura)
	{
		distancia_Satelite=dis;
		Radio=radio/15460000;
		ApSatelite=new Appearance();
		textura=new TextureLoader("Imagenes/"+Textura,null);
		ApSatelite.setTexture(textura.getTexture());
		Sphere EsferaSatelite=new Sphere(Radio, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, ApSatelite);

		RSEl=rotate(EsferaSatelite,new Alpha(-1,velocidad_rot));//Lo roto sobre él
		TrRPl=translate(RSEl,new Vector3f(0.0f,h, distancia_Satelite));//Lo alejo con respecto al planeta
		RSPl=rotate(TrRPl,new Alpha(-1,velocidad_tras));//Lo roto sobre el alejamiento del plneta
		TrRS=translate(RSPl,new Vector3f(0.0f,0.0f,dis_sol));
		MovFinal=rotate(TrRS,new Alpha(-1,vel_tras_plan));

	
	}

	TransformGroup rotate(Node node, Alpha alpha) 
	{
		TransformGroup xformGroup = new TransformGroup();
		xformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		RotationInterpolator interpolator =new RotationInterpolator(alpha, xformGroup);
		interpolator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1.0));
		xformGroup.addChild(interpolator); xformGroup.addChild(node);
		return xformGroup;
	}
	TransformGroup translate(Node node, Vector3f vector) 
	{
		Transform3D transform3D = new Transform3D();
		transform3D.setTranslation(vector);
		TransformGroup transformGroup =new TransformGroup();
		transformGroup.setTransform(transform3D); 
		transformGroup.addChild(node);
		return transformGroup;
	}
	




	
}
