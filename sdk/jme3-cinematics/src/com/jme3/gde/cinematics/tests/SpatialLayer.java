/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.cinematics.tests;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.AnimationFactory;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author MAYANK
 */
public class SpatialLayer {
    private String layerName;
    private Spatial spat;
    private HashMap<Integer,MotionKeyframe> motionKeyFrames = new HashMap<Integer,MotionKeyframe>();

    public HashMap<Integer, MotionKeyframe> getMotionKeyFrames() {
        return motionKeyFrames;
    }

    public void setMotionKeyFrames(HashMap<Integer, MotionKeyframe> motionKeyFrames) {
        this.motionKeyFrames = motionKeyFrames;
    }

    public Spatial getSpat() {
        return spat;
    }

    public void setSpat(Spatial spat) {
        this.spat = spat;
    }

        
    public void addMotionKeyframe(int time,MotionKeyframe frame)
    {
        
        motionKeyFrames.put(time,frame);
    }

    public void removeMotionKeyframe(int time)
    {
        motionKeyFrames.remove(time);
    }
    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }
    
    public void buildAnimation(float duration) {
        AnimationFactory factory = new AnimationFactory(duration, layerName+ "_animation");
        System.out.println("No of keyframes detected for layer " + layerName + " : " + motionKeyFrames.size());
        //sorting map by key i.e frame number
        Map<Integer,MotionKeyframe> treeMap = new TreeMap<Integer,MotionKeyframe>(motionKeyFrames);
       
        for(Map.Entry<Integer,MotionKeyframe> frame:treeMap.entrySet())
        {
            System.out.println("Adding to  : testValues : " + frame.getValue().getTransform().getRotation().getX());
            factory.addTimeTransform(frame.getKey(),frame.getValue().getTransform());
        }
        AnimControl control = new AnimControl();
        control.addAnim(factory.buildAnimation());
        spat.addControl(control);
    }
    
    public void loadSpatialState(int frame){
        if(motionKeyFrames.containsKey(frame))
        {
            System.out.println("Changing spatial position");
            
            spat.setLocalRotation(motionKeyFrames.get(frame).getRotation());
            spat.setLocalTranslation(motionKeyFrames.get(frame).getTranslation());
            spat.setLocalScale(motionKeyFrames.get(frame).getScale());
            System.out.println("translation (match it): " + spat.getLocalTranslation().x + ", " + spat.getLocalTranslation().y + ", " + spat.getLocalTranslation().z);
            System.out.println("translation (match it): " + motionKeyFrames.get(frame).getTranslation().x + ", " + motionKeyFrames.get(frame).getTranslation().y + ", " + motionKeyFrames.get(frame).getTranslation().z);
        }
    }
}
