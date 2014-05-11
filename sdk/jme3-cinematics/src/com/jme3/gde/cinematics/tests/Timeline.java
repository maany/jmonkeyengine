/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.cinematics.tests;

import javax.swing.JTable;

/**
 *
 * @author MAYANK
 */
public class Timeline extends java.util.ArrayList<SpatialLayer>{
    private JTable timeline;
    private JTable layerHeaders;

    public JTable getTimeline() {
        return timeline;
    }

    public void setTimeline(JTable timeline) {
        this.timeline = timeline;
    }

    public JTable getLayerHeaders() {
        return layerHeaders;
    }

    public void setLayerHeaders(JTable layerHeaders) {
        this.layerHeaders = layerHeaders;
    }
    
}
