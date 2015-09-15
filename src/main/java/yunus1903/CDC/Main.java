package yunus1903.CDC;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main
{
	URL projectURL;
	URL jsonURL;
	
	int downloads;
	
	public static void main(String[] args)
	{
		new Main(args);
	}
	
	public Main(String[] args)
	{
		projectURL = getProjectURL(args);
		jsonURL = getJsonURL(projectURL);
		downloads = getDownloads(jsonURL);
		if(downloads == -1)
		{
			System.out.println("Something went wrong!");
			System.exit(1);
		}
		System.out.println(downloads);
	}
	
	public URL getProjectURL(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].startsWith("-project="))
			{
				try
				{
					String url = args[i].substring(9);
					if(url.contains("http") && url.contains("curse"))
					{
						return new URL(url);
					}
					else
					{
						System.out.println("This is not a Curse URL!");
						System.exit(0);
					}
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("You have to add the Project Argument!");
				System.exit(0);
			}
		}
		return projectURL;
	}
	
	public URL getJsonURL(URL projectURL)
	{
		if(projectURL.toString().contains("curseforge"))
		{
			String project = projectURL.toString().replace("http://minecraft.curseforge.com/mc-mods/", "");
			try
			{
				URL jsonURL = new URL("http://widget.mcf.li/mc-mods/minecraft/" + project + ".json");
				return jsonURL;
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			String project = projectURL.toString().replace("http://www.curse.com/", "");
			try
			{
				URL jsonURL = new URL("http://widget.mcf.li/" + project + ".json");
				return jsonURL;
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
		return projectURL;
	}
	
	public int getDownloads(URL jsonURL)
	{
		JSONParser parser = new JSONParser();
		 
        try
        {
            Object obj = parser.parse(new InputStreamReader(jsonURL.openStream(), "UTF-8"));
 
            JSONObject jsonObject = (JSONObject) obj;
            
            String array = jsonObject.get("downloads").toString();
            
            String[] stringArray = array.split(",");
            
            int downloads = Integer.parseInt(stringArray[0].replace("{\"total\":", ""));
            
            return downloads;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
	}
}
