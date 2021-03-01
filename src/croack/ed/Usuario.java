package croack.ed;

public class Usuario
{
	
	public String uid = null;
	public String nombre = null;
	
	
	public Usuario()
	{}
	
	
	public Usuario(String _u, String _n)
	{
		this.uid = _u;
		this.nombre = _n;
	}
	
	
	public String toString()
	{
		return String.format("%s,%s", uid, nombre);
	}
	

}
