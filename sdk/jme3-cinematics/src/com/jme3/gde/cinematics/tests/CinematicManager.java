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
        if(name.equals("New")){
            
            CinematicEditorPrototype cinematic = new CinematicEditorPrototype();
            cinematics.add(cinematic);
            cinematic.setVisible(true);
            return;
        }
        for(CinematicEditorPrototype cinematic:cinematics)
        {
            if(cinematic.getCinematicName().equals(name))
            cinematic.setVisible(true);
            else
            cinematic.setVisible(false);
        }
    }
    public String[] getCinematicsAsStringArray()
    {
        String[] list = new String[cinematics.size()+1];
        int i;
        for(i=0;i<cinematics.size();i++)
        {
            list[i] = cinematics.get(i).getCinematicName();
        }
        list[i+1] = "New";
        return list;
    }
}
