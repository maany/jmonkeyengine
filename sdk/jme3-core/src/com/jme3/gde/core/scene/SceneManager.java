
package com.jme3.gde.core.scene;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MAYANK
 */


public class SceneManager {
   private Map<String,Spatial> spatials =new HashMap<String,Spatial>();
   public void addSpatial(Spatial spatial)
   {
       spatials.put(spatial.getName(),spatial);
   }
   public void removeSpatial(Spatial spatial)
   {
       spatials.remove(spatial.getName());
   }
   
   
}
