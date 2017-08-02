/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dmens.pokeno.view;

import com.dmens.pokeno.ability.AbilityCost;
import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.card.TrainerCard;
import com.dmens.pokeno.utils.FileUtils;
import com.dmens.pokeno.effect.*;

/**
 *
 * @author cclp94
 */
public class PokemonCardPanel extends javax.swing.JPanel {
    private String POKEMON_ICON_IMAGE = "images/pokemon.png";
    private String ENERGY_ICON_IMAGE = "images/energy.png";
    private String TRAINER_ICON_IMAGE = "images/backpackIcon.png";

    /**
     * Creates new form PokemonCardPanel
     */
    public PokemonCardPanel() {
        initComponents();
    }
    
    public PokemonCardPanel(Card card){
        initComponents();
        switch(card.getType()){
            case POKEMON:
                Pokemon p = (Pokemon) card;
                setPokemon(p);
                break;
            case ENERGY:
                EnergyCard e = (EnergyCard) card;
                setEnergy(e.getCategory().toString());
                break;
            case TRAINER:
                TrainerCard t = (TrainerCard)  card;
                setTrainerCard(t);
                break;
        }
        
    }
    
    public void updatePanel(Card card){
        switch(card.getType()){
            case POKEMON:
                Pokemon p = (Pokemon) card;
                setPokemon(p);
                break;
            case ENERGY:
                EnergyCard e = (EnergyCard) card;
                setEnergy(e.getCategory().toString());
                break;
            case TRAINER:
            	TrainerCard t = (TrainerCard)  card;
                setTrainerCard(t);
                break;
        }
    }
    
    public void clean(){
        this.cardName.setText("");
        this.imageLabel.setIcon(null);
        this.HP.setText("");
        this.ability1.setText("");
        this.description1.setText("");
        this.damage1.setText("");
        this.ability2.setText("");
        this.description2.setText("");
        this.damage2.setText("");
    }
    
    private void setPokemon(Pokemon poke){
        cardName.setText(poke.getName());
        imageLabel.setIcon(FileUtils.getFileAsImageIcon(POKEMON_ICON_IMAGE, 120, 120));
        this.HP.setText(Integer.toString(poke.getHP()));
        // set ability 1
        AbilityCost ability = poke.getAbilitiesAndCost().get(0);
        this.ability1.setText(ability.getAbility().getName());
        this.description1.setText(ability.showCosts());
        
        //is it a simple ApplyStatus effect or a simple Damage effect
        if(ability.getAbility().getEffects().get(0) instanceof ApplyStatus)
       	{
        	this.damage1.setText(ability.getAbility().getApplyStatusEffect().getStatus());
        }
        else if(ability.getAbility().getEffects().get(0) instanceof Damage)
        {
        	this.damage1.setText(Integer.toString(ability.getAbility().getDamageEffect().getValue()));
        }
        else if(ability.getAbility().getEffects().get(0) instanceof Deenergize)
        {
        	this.damage1.setText(Integer.toString(ability.getAbility().getDeenergizeEffect().getAmount()));
        }
        else if(ability.getAbility().getEffects().get(0) instanceof DrawCard)
        {
        	this.damage1.setText(Integer.toString(ability.getAbility().getDrawCardEffect().getValue()));
        }
        
        // Set ability 2 if present
        if(poke.getAbilitiesAndCost().size() >= 2)
        {
            ability = poke.getAbilitiesAndCost().get(1);
            this.ability2.setText(ability.getAbility().getName());
            this.description2.setText(ability.showCosts());
            //is it a simple ApplyStatus effect or a simple Damage effect
            if(ability.getAbility().getEffects().get(0) instanceof ApplyStatus)
           	{
            	this.damage2.setText(ability.getAbility().getApplyStatusEffect().getStatus());
            }
            else if(ability.getAbility().getEffects().get(0) instanceof Damage)
            {
            	this.damage2.setText(Integer.toString(ability.getAbility().getDamageEffect().getValue()));
            }
            else if(ability.getAbility().getEffects().get(0) instanceof Deenergize)
            {
            	this.damage2.setText(Integer.toString(ability.getAbility().getDeenergizeEffect().getAmount()));
            }
            else if(ability.getAbility().getEffects().get(0) instanceof DrawCard)
            {
            	this.damage2.setText(Integer.toString(ability.getAbility().getDrawCardEffect().getValue()));
            }
        }
        else
        {
            this.ability2.setText("");
            this.description2.setText("");
            this.damage2.setText("");
        }
        // TODO Add Abilities description
        //this.ability1.setText(abilities[0]);
        //this.ability2.setText(abilities[1]);
    }
    
    
    private void setEnergy(String cat){
        cardName.setText("Energy - "+ (Character.toUpperCase(cat.charAt(0)) + cat.substring(1)));
        imageLabel.setIcon(FileUtils.getFileAsImageIcon(ENERGY_ICON_IMAGE, 120, 120));
        this.HP.setText("");
        ability1.setText("");
        ability2.setText("");
        description1.setText("");
        description2.setText("");
        damage1.setText("");
        damage2.setText("");
    }
    
    private void setTrainerCard(TrainerCard trainer){
    	cardName.setText(trainer.getName());
        imageLabel.setIcon(FileUtils.getFileAsImageIcon(TRAINER_ICON_IMAGE, 120, 120));
        // Ability 1 field becomes card category
        ability1.setText(trainer.getCategory());
        // description 1 covers the ability
        description1.setText(trainer.getAbility().toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cardName = new javax.swing.JLabel();
        HP = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        ability1 = new javax.swing.JLabel();
        ability2 = new javax.swing.JLabel();
        damage1 = new javax.swing.JLabel();
        damage2 = new javax.swing.JLabel();
        description1 = new javax.swing.JLabel();
        description2 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(235, 307));
        setRequestFocusEnabled(false);

        cardName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cardName.setText("Name");
        cardName.setAlignmentY(0.0F);
        cardName.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cardName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        HP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        HP.setText("HP");
        HP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        HP.setFocusable(false);
        HP.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        imageLabel.setPreferredSize(new java.awt.Dimension(80, 80));

        ability1.setText("Ability 1");
        ability1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        ability2.setText("Ability 2");
        ability2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        damage1.setText("10");
        damage1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        damage2.setText("10");
        damage2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        description1.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        description1.setText("description");
        description1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        description2.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        description2.setText("description");
        description2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(description2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ability2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(damage2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cardName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(HP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ability1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(damage1))
                    .addComponent(description1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardName)
                    .addComponent(HP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ability1)
                    .addComponent(damage1))
                .addGap(1, 1, 1)
                .addComponent(description1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ability2)
                    .addComponent(damage2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(description2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HP;
    private javax.swing.JLabel ability1;
    private javax.swing.JLabel ability2;
    private javax.swing.JLabel cardName;
    private javax.swing.JLabel damage1;
    private javax.swing.JLabel damage2;
    private javax.swing.JLabel description1;
    private javax.swing.JLabel description2;
    private javax.swing.JLabel imageLabel;
    // End of variables declaration//GEN-END:variables
}
