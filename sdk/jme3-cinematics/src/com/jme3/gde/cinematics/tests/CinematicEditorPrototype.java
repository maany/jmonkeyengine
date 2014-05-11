/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.gde.cinematics.tests;

import com.jme3.animation.AnimationFactory;
import com.jme3.animation.LoopMode;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.gde.core.scene.SceneApplication;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.vecmath.Vector3d;

/**
 *
 * @author MAYANK
 */
public class CinematicEditorPrototype extends javax.swing.JFrame {
    
    private Cinematic cinematic ;
    private String cinematicName;
    private  FixedColumnTable fct;
    private Timeline timelineData = new Timeline();
    private Node rootNode;

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
    Integer durationValue = 30;
    Integer currentFrameValue = 0;
    boolean loaded = false;
    

    public String getCinematicName() {
        return cinematicName;
    }

    public void setCinematicName(String cinematicName) {
        this.cinematicName = cinematicName;
        clipName.setText(cinematicName);
    }

    public Cinematic getCinematic() {
        return cinematic;
    }

    public void setCinematic(Cinematic cinematic) {
        this.cinematic = cinematic;
    }
    /**
     * Creates new form CinematicEditorPrototype
     */
    public CinematicEditorPrototype() {
        initComponents();
        currentFrameValue=1;
        currentFrame.setValue(currentFrameValue);
        initTimeline();
        
        timeSlider.setMaximum(durationValue);
        duration.setValue(durationValue);
        timeSlider.setValue(1);
        timeSlider.setMinimum(1);
        
        
        loaded = true;
    }

    private void initTimeline() {
        DefaultTableModel model = (DefaultTableModel) timeline.getModel();
        model.setColumnCount(durationValue+1);
        int cols = model.getColumnCount();
        System.out.println("ColumnCount : " + cols);
        TableColumnModel columnModel = timeline.getColumnModel();
        
        TableColumn layersColumn = columnModel.getColumn(0);
        layersColumn.setHeaderValue("Layers");
        layersColumn.setMinWidth(80);
        layersColumn.setPreferredWidth(150);
        
        for(int i=1;i<cols;i++)
        {
            TableColumn column = columnModel.getColumn(i);
            column.setHeaderValue(i);
            System.out.println("running for " + i);
            column.setMinWidth(30);
            // column.setMaxWidth(30);
            column.setPreferredWidth(30);
        }
        System.out.println("success");
        timeline.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
       fct = new FixedColumnTable(1,timelineContainer);
    }
    
    
    
    public void addSpatialObject(Spatial spat,String name){
        
        SpatialLayer layer = new SpatialLayer();
        layer.setSpat(spat);
        layer.setLayerName(name);
        timelineData.add(layer);
        refreshTimeline();
        
        
        /*System.out.println("Editor : Adding spatial : "+ name);
        DefaultTableModel model = (DefaultTableModel)timeline.getModel();
        model.addRow(new Vector<Object>());
        
        System.out.println("Cinematic Editor: New Row created");
        JTable fixedTable = fct.getFixedTable();
        DefaultTableModel fixedModel =  (DefaultTableModel) fixedTable.getModel();
        fixedTable.setValueAt(name,fixedModel.getRowCount()-1,0);*/
        
        
        
    }
    
    public boolean animateEnabled ()
    {
        return animationSwitch.isSelected();
    }
    
   
    
     private void refreshTimeline() {
        
         DefaultTableModel model = (DefaultTableModel)timeline.getModel();
         DefaultTableModel fixed = (DefaultTableModel)fct.getFixedTable().getModel();
         //clear
         for(int i=0;i<timeline.getRowCount();i++)
         model.removeRow(i);
         // add rows
         for(int i=0;i<timelineData.size();i++)
         {
             System.out.println("running for layer : " + (i+1));
             SpatialLayer layer = timelineData.get(i);
             Vector<String> rowData = new Vector<String>();
             
             for( int j=0;j<timeline.getColumnCount();j++)
             {
                 if(layer.getMotionKeyFrames().containsKey(j))
                    rowData.add("M");
                 else
                     rowData.add("");
                 System.out.println("Value Added to column : " + rowData.get(j));
             }
             model.addRow(rowData);
             fixed.setValueAt(layer.getLayerName(),i,0);
         }
    }
     
    public void moveSpatial()
    {
        if(loaded && animateEnabled())
        {
            SpatialLayer layer = timelineData.get(timeline.getSelectedRow());
            Spatial spat = layer.getSpat();
            MotionKeyframe motionKeyframe ;
            if(!layer.getMotionKeyFrames().containsKey(currentFrameValue))
            {
                System.out.println("Creating new keyframe at " + currentFrameValue);
                motionKeyframe = new MotionKeyframe();
                timeline.setValueAt("m",timeline.getSelectedRow() ,currentFrameValue-1 );
                layer.getMotionKeyFrames().put(currentFrameValue, motionKeyframe);
            }
            else{
                System.out.println("Keyframe already exists at " + currentFrameValue);
                motionKeyframe = layer.getMotionKeyFrames().get(currentFrameValue);
            }
            motionKeyframe.setRotation(spat.getLocalRotation());
            motionKeyframe.setScale(spat.getLocalScale());
            motionKeyframe.setTranslation(spat.getLocalTranslation());
            
            
        }
    }
    
    
    public void removeMotionKeyframe(){
        SpatialLayer layer = timelineData.get(timeline.getSelectedRow());
        timeline.getModel().setValueAt(" ", timeline.getSelectedRow(),currentFrameValue);
        System.out.println("Removing keyframe" + currentFrameValue);
        layer.getMotionKeyFrames().remove(currentFrameValue);
        
    }
    
    public void buildPreview(){
        cinematic = new Cinematic(rootNode, durationValue);
        for(SpatialLayer layer: timelineData){
            Spatial spat = layer.getSpat();
            System.out.println("Sending request to build animation");
            layer.buildAnimation(durationValue);
            System.out.println("Adding cinematic animation event");
            cinematic.addCinematicEvent(0, new AnimationEvent(spat, layer.getLayerName()+ "_animation", LoopMode.DontLoop));
            SceneApplication.getApplication().getStateManager().attach(cinematic);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        yoyo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        animationSwitch = new javax.swing.JToggleButton();
        addKeyframe = new javax.swing.JButton();
        removeKeyFrame = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        duration = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        stop = new javax.swing.JButton();
        playPause = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        timeSlider = new javax.swing.JSlider();
        currentFrame = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        timelineContainer = new javax.swing.JScrollPane();
        timeline = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        clipName = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(yoyo, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.yoyo.text")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.jPanel1.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(animationSwitch, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.animationSwitch.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(addKeyframe, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.addKeyframe.text")); // NOI18N
        addKeyframe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addKeyframeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeKeyFrame, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.removeKeyFrame.text")); // NOI18N
        removeKeyFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeKeyFrameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(animationSwitch, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addKeyframe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeKeyFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(animationSwitch)
                    .addComponent(addKeyframe)
                    .addComponent(removeKeyFrame))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.jPanel2.border.title"))); // NOI18N

        duration.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(20), Integer.valueOf(0), null, Integer.valueOf(1)));
        duration.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                durationStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(stop, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.stop.text")); // NOI18N
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(playPause, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.playPause.text")); // NOI18N
        playPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playPauseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(duration, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playPause, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stop))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(duration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stop)
                    .addComponent(playPause))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.jPanel3.border.title"))); // NOI18N

        timeSlider.setMajorTickSpacing(1);
        timeSlider.setPaintLabels(true);
        timeSlider.setPaintTicks(true);
        timeSlider.setSnapToTicks(true);
        timeSlider.setValue(0);
        timeSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        timeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeSliderStateChanged(evt);
            }
        });

        currentFrame.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        currentFrame.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                currentFrameStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.jLabel3.text")); // NOI18N

        timeline.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        timeline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                timelineMousePressed(evt);
            }
        });
        timelineContainer.setViewportView(timeline);

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.jButton2.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timelineContainer, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(currentFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(timeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(currentFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(timeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(timelineContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)))
        );

        clipName.setText(org.openide.util.NbBundle.getMessage(CinematicEditorPrototype.class, "CinematicEditorPrototype.clipName.text")); // NOI18N
        clipName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clipNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(yoyo, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clipName, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yoyo)
                    .addComponent(clipName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopActionPerformed
        cinematic.stop();
        SceneApplication.getApplication().getStateManager().detach(cinematic);
    }//GEN-LAST:event_stopActionPerformed

    private void timeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeSliderStateChanged
        if(loaded){
        currentFrameValue = timeSlider.getValue();
        currentFrame.setValue(timeSlider.getValue());
        if(timeline.getSelectedRow()>-1){
        SpatialLayer layer = timelineData.get(timeline.getSelectedRow());
        layer.loadSpatialState(currentFrameValue);
        }
        }
    }//GEN-LAST:event_timeSliderStateChanged

    private void durationStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_durationStateChanged
        if(loaded)
        {
        Integer temp = new Integer(duration.getValue().toString());
            if(currentFrameValue>temp) {
            currentFrameValue=temp;
            }
        
            if(durationValue>temp)
            {   
                for(int i=durationValue;i>temp;i--)
                {
                    timeline.removeColumn(timeline.getColumnModel().getColumn(timeline.getColumnCount()-1));
                }
            
            }else 
            {
                int newCols = timeline.getColumnModel().getColumnCount() + (temp-durationValue)+1;
                DefaultTableModel model = (DefaultTableModel) timeline.getModel();
                model.setColumnCount(newCols);
                
                for(int i=durationValue+1;i<=temp;i++)
                 {
                    TableColumn column = new TableColumn(i);
                    column.setPreferredWidth(30);
                    column.setMinWidth(30);
                    column.setHeaderValue(i);
                    System.out.println("Adding Column");
                    //timeline.getColumnModel().addColumn(column);
                    timeline.addColumn(column);
                }
            }
        
            durationValue = temp;
            currentFrame.setValue(currentFrameValue);
            timeSlider.setValue(currentFrameValue);
            timeSlider.setMaximum(durationValue);
        }               
    }//GEN-LAST:event_durationStateChanged

    private void timelineMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timelineMousePressed
        int selectedRow = timeline.getSelectedColumn()+1;
        currentFrameValue = selectedRow;
        currentFrame.setValue(selectedRow);
        timeSlider.setValue(selectedRow);
        System.out.println("Selected Column : " + selectedRow + "SelectedRow :" + timeline.getSelectedRow());
        if(timeline.getSelectedRow()>-1){
        SpatialLayer layer = timelineData.get(timeline.getSelectedRow());
        layer.loadSpatialState(currentFrameValue);
        }
        
    }//GEN-LAST:event_timelineMousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void clipNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clipNameActionPerformed
        if(loaded)
        {
            if(!clipName.getText().equals(cinematicName))
                this.setCinematicName(clipName.getText());
        }
    }//GEN-LAST:event_clipNameActionPerformed

    private void currentFrameStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_currentFrameStateChanged
        if(loaded) {
        currentFrameValue = new Integer(currentFrame.getValue().toString());
        timeSlider.setValue(currentFrameValue);
        if(timeline.getSelectedRow()>-1){
        SpatialLayer layer = timelineData.get(timeline.getSelectedRow());
        layer.loadSpatialState(currentFrameValue);
        }
        }
    }//GEN-LAST:event_currentFrameStateChanged

    private void addKeyframeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addKeyframeActionPerformed
        moveSpatial();
    }//GEN-LAST:event_addKeyframeActionPerformed

    private void removeKeyFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeKeyFrameActionPerformed
        removeMotionKeyframe();
    }//GEN-LAST:event_removeKeyFrameActionPerformed

    private void playPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playPauseActionPerformed
        
        if(playPause.isSelected())
        {
            if(!SceneApplication.getApplication().getStateManager().hasState(cinematic)) {
                System.out.println("Building Preview");
                buildPreview();
            }
            System.out.println("Playing Cinematic");
            cinematic.play();
        }
        else {
            cinematic.pause();
        }
    }//GEN-LAST:event_playPauseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CinematicEditorPrototype.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CinematicEditorPrototype.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CinematicEditorPrototype.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CinematicEditorPrototype.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CinematicEditorPrototype().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addKeyframe;
    private javax.swing.JToggleButton animationSwitch;
    private javax.swing.JTextField clipName;
    private javax.swing.JSpinner currentFrame;
    private javax.swing.JSpinner duration;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToggleButton playPause;
    private javax.swing.JButton removeKeyFrame;
    private javax.swing.JButton stop;
    private javax.swing.JSlider timeSlider;
    private javax.swing.JTable timeline;
    private javax.swing.JScrollPane timelineContainer;
    private javax.swing.JLabel yoyo;
    // End of variables declaration//GEN-END:variables

   

 
}
