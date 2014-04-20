/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.cinematics.tests;

import com.jme3.cinematic.Cinematic;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Vector3d;

/**
 * Testing AnimationFactory and Cinematic GUI integration using existing API
 * @author MAYANK
 */
public class CinematicAnimationFactory {
    private Spatial selectedSpatial;
    Map<Integer,Vector3d> transformKeyFrames = new HashMap<Integer,Vector3d>();
    
    private Cinematic cinematic;
    /**
     * Convert data from Map into a Animation Cinematic clip
     */
    private void buildAnimationCinematic(){
        
    }
    
    public Spatial getSelectedSpatial() {
        return selectedSpatial;
    }

    public void setSelectedSpatial(Spatial selectedSpatial) {
        this.selectedSpatial = selectedSpatial;
    }
}
