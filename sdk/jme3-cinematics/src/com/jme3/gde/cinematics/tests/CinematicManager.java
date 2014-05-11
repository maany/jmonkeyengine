/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.cinematics.tests;
import com.jme3.cinematic.Cinematic;
import java.util.*;
/**
 *
 * @author MAYANK
 */
public class CinematicManager {
    //Map<LayerName,
    //private Map<String,ArrayList<Map<String,Map<String,Object>>>> timelineModel = new Map<String,ArrayList<Map<String, Map<String, Object>>>>();
    private ArrayList<CinematicEditorPrototype> cinematics= new ArrayList <CinematicEditorPrototype>();
    private static CinematicManager manager = null;
    private CinematicManager()
    {
        
    }
    public static CinematicManager getInstance()
    {
        if(manager==null)
            manager = new CinematicManager();
        return manager;
    }
    public void loadCinematic(String name)
    {
        System.out.println("Cinematic Manager - loading Cinematic:The requested name is " + name);
        if(name.equals("New")){
            System.out.println("Cinematic Manager: Detected New Clip : ");
            CinematicEditorPrototype cinematic = new CinematicEditorPrototype();
            cinematics.add(cinematic);
            cinematic.setCinematicName("Untitled Clip:" + cinematics.size());
            System.out.println("Clip Name" + cinematic.getCinematicName());
            cinematic.setVisible(true);
            return;
        }
        for(CinematicEditorPrototype cinematic:cinematics)
        {
            if(cinematic.getCinematicName().equals(name))
            {
            cinematic.setVisible(true);
                
            }
            else{
            cinematic.setVisible(false);
            System.out.println(cinematic.getCinematicName() + "is not equal to " + name);
            }
        }
    }
    public String[] getCinematicsAsStringArray()
    {
        System.out.println("CinematicManager : Request is here to get the arraylist as string");
        String[] list = new String[cinematics.size()+1];
        int i;
        
        for(i=0;i<list.length;i++)
        {
            if(i==0)
            {
                list[i]="New";
                continue;
            }
            list[i] = cinematics.get(i-1).getCinematicName();
        }
        System.out.println("List : " + Arrays.toString(list) + " Size: " + list.length);
        //list[0] = "New";
        return list;
        
    }
    public CinematicEditorPrototype getCinematic(String name)
    {
        if(name.equals("New"))
        {
            loadCinematic(name);
            CinematicEditorPrototype temp = cinematics.get(cinematics.size()-1);
            return temp;
        }
        else {    
            for(CinematicEditorPrototype temp: cinematics)
            {
                if(temp.getCinematicName().equals(name))
                    return temp;
            }
        }
        return null;
    }
}
