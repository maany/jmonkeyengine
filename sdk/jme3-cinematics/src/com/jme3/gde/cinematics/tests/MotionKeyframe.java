/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.cinematics.tests;

import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import javax.vecmath.Vector3d;

/**
 *
 * @author MAYANK
 */
public class MotionKeyframe {
    private Vector3f translation;
    private Quaternion rotation;
    private Vector3f scale;

    public Vector3f getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3f translation) {
        Vector3f temp = translation.clone();
        this.translation = temp;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        Quaternion temp = rotation.clone();
        this.rotation = temp;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        Vector3f temp = scale.clone();
        this.scale = temp;
    }
        
    public Transform getTransform(){
        Transform trans = new Transform(translation, rotation, scale);
        return trans;
        
    }
    
}
