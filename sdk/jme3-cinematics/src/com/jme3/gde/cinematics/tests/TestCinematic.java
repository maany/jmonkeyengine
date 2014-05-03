/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.cinematics.tests;

import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimationFactory;
import com.jme3.animation.LoopMode;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author MAYANK
 */
public class TestCinematic {
    public Cinematic createCinematic(Spatial spat,Node node)
    {
        System.out.println("FROM TestCinematic : recieved" + spat.getName() + " inside node " + node.getName());
        System.out.println("Creating animation factory");
        AnimationFactory factory = new AnimationFactory(20,"teapotAnim");
        factory.addTimeTranslation(0, new Vector3f(10, 0, 10));
        factory.addTimeTranslation(20, new Vector3f(10, 0, -10));
        factory.addTimeScale(10, new Vector3f(4, 4, 4));
        factory.addTimeScale(20, new Vector3f(1, 1, 1));
        //factory.addTimeRotationAngles(20, 0, 4 * FastMath.TWO_PI, 0);
        AnimControl control = new AnimControl();
        control.addAnim(factory.buildAnimation());
        spat.addControl(control);
        System.out.println("AnimControl added to spatial\n Creating cinematic");
        
        Cinematic cinematic = new Cinematic(node, 20);
        cinematic.addCinematicEvent(0, new AnimationEvent(spat, "teapotAnim", LoopMode.DontLoop));
        System.out.println("Cinematic created. Returning cinematic to viewport");
        return cinematic;
    }
}
