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
        String repo = "https://raw.githubusercontent.com/PrinceOfAmber/OverpoweredInventory/";
        String branch = "master-18";
        String file = "/version.dat";
        try 
        {
            in = new URL(repo + branch + file).openStream();
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
        
        String current = "1.0.11"; //TODO: get from mcinfo or something i guess

        isLatestVersion = current.equals(latestVersion);

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
