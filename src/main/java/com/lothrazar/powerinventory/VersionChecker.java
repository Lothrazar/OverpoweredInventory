package com.lothrazar.powerinventory;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL; 
import org.apache.commons.io.IOUtils;

public class VersionChecker implements Runnable 
{
	//thanks to http://jabelarminecraft.blogspot.ca/p/minecraft-forge-1721710-making-mod.html
	
	private static boolean isLatestVersion = false;
    private static String latestVersion = "";
 
    @Override
    public void run() 
    {
        InputStream in = null;
        try 
        {
            in = new URL("https://raw.githubusercontent.com/PrinceOfAmber/OverpoweredInventory/backport17/version.dat").openStream();
        } 
        catch (MalformedURLException e) 
        { 
            e.printStackTrace();
        } 
        catch (IOException e) 
        { 
            e.printStackTrace();
        }

        try 
        {
            latestVersion = IOUtils.readLines(in).get(0);
        } 
        catch (IOException e) 
        { 
            e.printStackTrace();
        } 
        finally 
        {
            IOUtils.closeQuietly(in);
        }
        
        
        System.out.println(latestVersion);
        
        
        
        String current = "1.0.6"; 
       // System.out.println("Latest mod version = "+latestVersion);
        isLatestVersion = current.equals(latestVersion);
     //   System.out.println("Are you running latest version = "+isLatestVersion);
        /*YES IT WORKS 
         * 
         * [18:46:08] [Version Check/INFO] [STDOUT]: [com.lothrazar.powerinventory.VersionChecker:run:50]: Latest mod version = 1.0.0
[18:46:08] [Version Check/INFO] [STDOUT]: [com.lothrazar.powerinventory.VersionChecker:run:52]: Are you running latest version = false

*/
    }
    
    public boolean isLatestVersion()
    {
    	return isLatestVersion;
    }
    
    public String getLatestVersion()
    {
    	return latestVersion;
    }
}