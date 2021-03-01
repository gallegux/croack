package croack.ed;

import croack.util.Codigo;

public class IP implements Comparable<IP>
{
	
	public String ip = null;
	public String sortIP = null;
	
	
	
	public IP(String _ip)
	{
		this.ip = _ip;
		
		calc();
	}
	
	
	public String toString()
	{
		return this.ip;
	}
	
	
	
	private void calc()
	{
		try {
			String[] octetos = this.ip.split("\\.");
			
			for (int x = 0; x < 4; x++) {
				if (octetos[x].length() == 2)
					octetos[x] = "0" + octetos[x];
				else if (octetos[x].length() == 1)
					octetos[x] = "00" + octetos[x];
			}
			
			this.sortIP = String.join(".", octetos);
		}
		catch (Exception e) {
			Codigo.log("excp", this.ip);
		}
	}


	@Override
	public int compareTo(IP o) {
		return this.sortIP.compareTo(o.sortIP);
	}

}
