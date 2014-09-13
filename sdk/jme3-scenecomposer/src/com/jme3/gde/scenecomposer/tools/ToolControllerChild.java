/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.scenecomposer.tools;

import com.jme3.asset.AssetManager;
import com.jme3.gde.core.sceneexplorer.nodes.JmeNode;
import com.jme3.gde.scenecomposer.SceneComposerToolController;
import com.jme3.scene.Node;

/**
 *
 * @author MAYANK
 */
public class ToolControllerChild extends SceneComposerToolController{

    public ToolControllerChild(Node toolsNode, AssetManager manager, JmeNode rootNode) {
        super(toolsNode, manager, rootNode);
    }
    
}
