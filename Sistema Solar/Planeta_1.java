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

public class Planeta_1{

	float distancia_planeta;
	String nombre;
	Sphere EsferaPlaneta;
	TransformGroup RSEl,TR,RSEje;
	Appearance ApPlaneta;
	TextureLoader textura;
	float Radio;
	public Planeta_1(String nombre,float radio,long velocidad_rot,long velocidad_tras,float dis,String Textura)
	{
		distancia_planeta=dis;
		Radio=radio/15460000;
		this.nombre=nombre;
		ApPlaneta=new Appearance();
		textura=new TextureLoader("Imagenes/"+Textura,null);
		ApPlaneta.setTexture(textura.getTexture());
		Sphere EsferaPlaneta=new Sphere(Radio, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, ApPlaneta);
		EsferaPlaneta.setUserData(nombre);
		RSEl=rotate(EsferaPlaneta,new Alpha(-1,velocidad_rot));
		TR=translate(RSEl,new Vector3f(0.0f,0.0f, distancia_planeta));
		RSEje=rotate(TR,new Alpha(-1,velocidad_tras));

	
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
