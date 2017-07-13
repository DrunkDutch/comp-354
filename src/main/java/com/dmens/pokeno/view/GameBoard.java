/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dmens.pokeno.view;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.CardTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.deck.Hand;
import com.dmens.pokeno.player.AIPlayer;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.utils.FileUtils;

/**
 *
 * @author Jarrett
 */
public class GameBoard extends javax.swing.JFrame {
    /**
     * Creates new form GameBoard
     */
	
	private static final Logger LOG = LogManager.getLogger(GameBoard.class);
	
    public GameBoard() {
    	 try {
 			this.setContentPane(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("data/images/background.jpg"))));
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
        initComponents();

        PlayerHandPanel.setLayout(new FlowLayout());
        PlayerBenchPanel.setLayout(new FlowLayout());
        OpponentBenchPanel.setLayout(new FlowLayout());
        OpponentHandPanel.setLayout(new FlowLayout());
        OpponentHandScrollPanel.add(OpponentHandPanel);
        CardPreviewPanel.add(new PokemonCardPanel());
        cleanCardPreview();
        
        PlayerParalyzedLabel.setText("");
        PlayerAsleepLabel.setText("");
        PlayerStuckLabel.setText("");
        PlayerPoisonedLabel.setText("");
        OpponentParalyzedLabel.setText("");
        OpponentAsleepLabel.setText("");
        OpponentStuckLabel.setText("");
        OpponentPoisonedLabel.setText("");
        setBounds(0, 0, 1300, 900);
    }
    
    public void update()
    {
        invalidate();
        validate();
        repaint();
    }
    
    public void updateHand(Hand hand, boolean player){
    	if(player)
    		PlayerHandPanel.removeAll();
    	else
    		setOpponentHand(hand);
    	hand.getCards().forEach(card->{
    		addCardToHand(card, player);
    	});
    }
    
    public void updateBench(List<Pokemon> bench, boolean player){
    	if (player)
            PlayerBenchPanel.removeAll();
        else
            OpponentBenchPanel.removeAll();
    	bench.forEach(pokemon -> {
    		addCardToBench(pokemon, player);
    	});
    }
    
    public void addCardToHand(Card card, boolean player)
    {
        JTextField newCard = new JTextField();
        newCard.setText(card.getName());
        newCard.setEditable(false);
        
        MouseListener viewCard = new java.awt.event.MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                //player.useCard(card);
                //if (the card is valid)
                if(GameController.useCardForPlayer(card, 0))
                    PlayerHandPanel.remove(newCard);
                ViewDamageField.setText("");
                update();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mousePressed(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent me)
            {
                cardPreview(card);
            }

            @Override
            public void mouseExited(MouseEvent me)
            {
                ViewDamageField.setText("");
                cleanCardPreview();
                cleanEnergyPreview();
            }
        };
        
        if (player)
        {
            PlayerHandPanel.add(newCard);
            
        }
        newCard.addMouseListener(viewCard);
        update();
    }
    
    private void cardPreview(Card card){
        ((PokemonCardPanel)CardPreviewPanel.getComponent(0)).updatePanel(card);
    }
    
    private void cleanCardPreview(){
        ((PokemonCardPanel)CardPreviewPanel.getComponent(0)).clean();
        ViewFightingEnergyField.setText("");
        ViewLightningEnergyField.setText("");
        ViewPsychicEnergyField.setText("");
        ViewWaterEnergyField.setText("");
        ViewColorlessEnergyField.setText("");
        ViewDamageField.setText("");
    }
    
    public void addStatus(int type, boolean player)
    {
    	LOG.debug((player ? "Home's " : "AI's ") + " active - setting flag " +  type + ".");
        if (player)
            switch(type)
            {
                case 0: PlayerParalyzedLabel.setText("Paralyzed"); break;
                case 1: PlayerAsleepLabel.setText("Asleep"); break;
                case 2: PlayerStuckLabel.setText("Stuck"); break;
                case 3: PlayerPoisonedLabel.setText("Poisoned"); break;
            }
        else
            switch(type)
            {
                case 0: OpponentParalyzedLabel.setText("Paralyzed"); break;
                case 1: OpponentAsleepLabel.setText("Asleep"); break;
                case 2: OpponentStuckLabel.setText("Stuck"); break;
                case 3: OpponentPoisonedLabel.setText("Poisoned"); break;
            }
        update();
    }
    
    public void clearStatus(int type, boolean player)
    {
    	LOG.debug((player ? "Home's " : "AI's ") + " active - clearing flag " +  type + ".");
        if (player)
            switch(type)
            {
                case 0: PlayerParalyzedLabel.setText(""); break;
                case 1: PlayerAsleepLabel.setText(""); break;
                case 2: PlayerStuckLabel.setText(""); break;
                case 3: PlayerPoisonedLabel.setText(""); break;
            }
        else
            switch(type)
            {
                case 0: OpponentParalyzedLabel.setText(""); break;
                case 1: OpponentAsleepLabel.setText(""); break;
                case 2: OpponentStuckLabel.setText(""); break;
                case 3: OpponentPoisonedLabel.setText(""); break;
            }
        update();
    }
    
    public void clearAllStatus(boolean player)
    {
        if (player)
        {
            PlayerParalyzedLabel.setText("");
            PlayerAsleepLabel.setText("");
            PlayerStuckLabel.setText("");
            PlayerPoisonedLabel.setText("");
        }
        else
        {
            OpponentParalyzedLabel.setText("");
            OpponentAsleepLabel.setText("");
            OpponentStuckLabel.setText("");
            OpponentPoisonedLabel.setText("");
        }
        update();
    }
    
    public void clearEnergy(boolean player)
    {
        if (player)
        {
            PlayerFightingEnergyField.setText("0");
            PlayerLightningEnergyField.setText("0");
            PlayerPsychicEnergyField.setText("0");
            PlayerWaterEnergyField.setText("0");
        }
        else
        {
            OpponentFightingEnergyField.setText("0");
            OpponentLightningEnergyField.setText("0");
            OpponentPsychicEnergyField.setText("0");
            OpponentWaterEnergyField.setText("0");
        }
        update();
    }
    
    public void setEnergy(List<Integer> energies, boolean player)
    {
        if (player)
        {
            PlayerFightingEnergyField.setText(energies.get(0).toString());
            PlayerLightningEnergyField.setText(energies.get(1).toString());
            PlayerPsychicEnergyField.setText(energies.get(2).toString());
            PlayerWaterEnergyField.setText(energies.get(3).toString());
            PlayerColorlessEnergyField.setText(energies.get(4).toString());
        }
        else
        {            
            OpponentFightingEnergyField.setText(energies.get(0).toString());
            OpponentLightningEnergyField.setText(energies.get(1).toString());
            OpponentPsychicEnergyField.setText(energies.get(2).toString());
            OpponentWaterEnergyField.setText(energies.get(3).toString());
            OpponentColorlessEnergyField.setText(energies.get(4).toString());
        }
        update();
    }
    
    public void setEnergyPreview(List<Integer> energies){
        ViewFightingEnergyField.setText(energies.get(0).toString());
        ViewLightningEnergyField.setText(energies.get(1).toString());
        ViewPsychicEnergyField.setText(energies.get(2).toString());
        ViewWaterEnergyField.setText(energies.get(3).toString());
        ViewColorlessEnergyField.setText(energies.get(4).toString());
        update();
    }
    
    public void cleanEnergyPreview(){
        ViewFightingEnergyField.setText("");
        ViewLightningEnergyField.setText("");
        ViewPsychicEnergyField.setText("");
        ViewWaterEnergyField.setText("");
        ViewColorlessEnergyField.setText("");
        update();
    }
    
    public void addCardToBench(Card card, boolean player)
    {
        JTextField newCard = new JTextField();
        newCard.setText(card.getName());
        newCard.setEditable(false);
        
        MouseListener viewCard = new java.awt.event.MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (player)
                {
                    //PlayerBenchPanel.remove(newCard); //not necessarily
                    //CardViewArea.setText("");
                    //ViewDamageField.setText("");
                    //update();
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
                //play card
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent me)
            {
                if(card.isType(CardTypes.POKEMON))
                {
                    cardPreview(card);
                    //TODO - get card.damageTaken
                    ViewDamageField.setText("0");
                    //TODO - get card.attachedEnergies
                    GameController.updateEnergyCountersForCard(card, player ? 0 : 1);
                    update();
                }
            }

            @Override
            public void mouseExited(MouseEvent me)
            {
                cleanCardPreview();
            }
        };
        
        if (player)
            PlayerBenchPanel.add(newCard);
        else
            OpponentBenchPanel.add(newCard);
        
        newCard.addMouseListener(viewCard);
        update();
    }
    
    public void clearActivePokemon(boolean player){
    	if(player)
    		playerActivePokemonPanel.removeAll();
    	else
    		OpponentActivePanel.removeAll();
    }
    
    public void setActivePokemon(Pokemon card, boolean player)
    {
        javax.swing.JPanel activePokemonField;
        if (player)
        {
            activePokemonField = playerActivePokemonPanel;
            PlayerDamageField.setText(Integer.toString(card.getDamage()));
            
            clearEnergy(true);
            clearAllStatus(true);
            //TODO - setEnergy(card.attachedEnergies, true);
            //if(card.poisoned) {addStatus(3, true)}
            //...
        }
        else
        {
            activePokemonField = OpponentActivePanel;
            if (card == null)
                OpponentDamageField.setText("");
            else
                OpponentDamageField.setText(Integer.toString(card.getDamage()));
            
            clearEnergy(false);
            clearAllStatus(false);
            //TODO - setEnergy(card.attachedEnergies, false);
            //if(card.poisoned) {addStatus(3, false)}
            //...
        }
        activePokemonField.removeAll();
        activePokemonField.add(new PokemonCardPanel(card));
//        if (card != null)
//            activePokemonField.setText(card.toString());
//        else
//            activePokemonField.setText("");
        update();
    }
    
    public void updateActivePokemon(Player p)
    {
        if (p instanceof AIPlayer)
        {
            OpponentDamageField.setText(Integer.toString(p.getActivePokemon().getDamage()));
        }
        else
        {
            PlayerDamageField.setText(Integer.toString(p.getActivePokemon().getDamage()));
        }
        //do it with energy too
    }
    
    public void updateDeckSize(int deckSize, boolean player){
        if(player)
            PlayerDeckSize.setText("Deck Size: "+deckSize);
        else
            OpponentDeckSize.setText("Deck Size: "+deckSize);
    }
    
    public void updateGraveyard(int graveyard, boolean player){
    	if(player)
    		PlayerGraveryard.setText("Graveyard: " + Integer.toString(graveyard));
    	else
    		OpponentGraveyard.setText("Graveyard: " + Integer.toString(graveyard));
    }
    
    public void setOpponentHand(Hand hand)
    {
        OpponentHandPanel.removeAll();
        ImageIcon imageIcon = null;
        imageIcon = FileUtils.getFileAsImageIcon("images/card.png",60,60);
        for(Card c : hand.getCards()){
            OpponentHandPanel.add(new JLabel(imageIcon));
        }
        OpponentHandScrollPanel.removeAll();
        OpponentHandScrollPanel.add(OpponentHandPanel);
        //OpponentCardHand.setText("Cards in Hand: " + hand.size());
        //update();
    }
    
    public void setRewardCount(int rewardCount, boolean player)
    {
        if (player)
            PlayerRewardCardLabel.setText("Rewards: " + rewardCount);
        else
            OpponentRewardCardLabel.setText("Rewards: " + rewardCount);
        update();
    }
    
    public void showTurnPhase(String phase)
    {
        AnnouncementBox.setText(phase);
        update();
    }
    
    public void showCoinResult(String result)
    {
        CoinResultField.setText(result);
        update();
    }
    
    public void updateBoard(Pokemon playerActive, Pokemon opponentActive, Hand opponentHand, int playerRewardCount, int opponentRewardCount, String phase)
    {
        setActivePokemon(playerActive, true);
        setActivePokemon(opponentActive, false);
        setOpponentHand(opponentHand);
        //setRewardCount(playerRewardCount, opponentRewardCount);
        showTurnPhase(phase);
        update();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        OpponentLightningEnergyField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        OpponentWaterEnergyField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        OpponentPsychicEnergyField = new javax.swing.JTextField();
        OpponentFightingEnergyField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        PlayerWaterEnergyField = new javax.swing.JTextField();
        PlayerLightningEnergyField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        PlayerFightingEnergyField = new javax.swing.JTextField();
        PlayerPsychicEnergyField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ViewLightningEnergyField = new javax.swing.JTextField();
        ViewWaterEnergyField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        ViewPsychicEnergyField = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        ViewFightingEnergyField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        PlayerRewardCardLabel = new javax.swing.JLabel();
        OpponentRewardCardLabel = new javax.swing.JLabel();
        PlayerHandPanel = new javax.swing.JPanel();
        PlayerBenchPanel = new javax.swing.JPanel();
        OpponentDamageField = new javax.swing.JTextField();
        OpponentRewardCardLabel1 = new javax.swing.JLabel();
        OpponentRewardCardLabel2 = new javax.swing.JLabel();
        PlayerDamageField = new javax.swing.JTextField();
        OpponentRewardCardLabel3 = new javax.swing.JLabel();
        ViewDamageField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        OpponentBenchPanel = new javax.swing.JPanel();
        PlayerAttack1Btn = new javax.swing.JButton();
        PlayerAttack2Btn = new javax.swing.JButton();
        PlayerRetreatBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        PlayerParalyzedLabel = new javax.swing.JLabel();
        PlayerAsleepLabel = new javax.swing.JLabel();
        PlayerStuckLabel = new javax.swing.JLabel();
        PlayerPoisonedLabel = new javax.swing.JLabel();
        OpponentParalyzedLabel = new javax.swing.JLabel();
        OpponentAsleepLabel = new javax.swing.JLabel();
        OpponentStuckLabel = new javax.swing.JLabel();
        OpponentPoisonedLabel = new javax.swing.JLabel();
        AnnouncementBox = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        CoinResultField = new javax.swing.JTextField();
        PassBtn = new javax.swing.JButton();
        OpponentDeckSize = new javax.swing.JLabel();
        PlayerDeckSize = new javax.swing.JLabel();
        OpponentGraveyard = new javax.swing.JLabel();
        PlayerGraveryard = new javax.swing.JLabel();
        OpponentHandScrollPanel = new javax.swing.JScrollPane();
        OpponentHandPanel = new javax.swing.JPanel();
        CardPreviewPanel = new javax.swing.JPanel();
        playerActivePokemonPanel = new javax.swing.JPanel();
        OpponentActivePanel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        OpponentColorlessEnergyField = new javax.swing.JTextField();
        PlayerColorlessEnergyField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        ViewColorlessEnergyField = new javax.swing.JTextField();

        jLabel4.setText("Lightning");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        OpponentLightningEnergyField.setEditable(false);
        OpponentLightningEnergyField.setText("0");
        OpponentLightningEnergyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpponentLightningEnergyFieldActionPerformed(evt);
            }
        });

        jLabel7.setText("Water");

        OpponentWaterEnergyField.setEditable(false);
        OpponentWaterEnergyField.setText("0");

        jLabel8.setText("Psychic");

        OpponentPsychicEnergyField.setEditable(false);
        OpponentPsychicEnergyField.setText("0");

        OpponentFightingEnergyField.setEditable(false);
        OpponentFightingEnergyField.setText("0");

        jLabel9.setText("Fighting");

        jLabel10.setText("Lightning");

        PlayerWaterEnergyField.setEditable(false);
        PlayerWaterEnergyField.setText("0");

        PlayerLightningEnergyField.setEditable(false);
        PlayerLightningEnergyField.setText("0");
        PlayerLightningEnergyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerLightningEnergyFieldActionPerformed(evt);
            }
        });

        jLabel15.setText("Water");

        jLabel16.setText("Fighting");

        PlayerFightingEnergyField.setEditable(false);
        PlayerFightingEnergyField.setText("0");

        PlayerPsychicEnergyField.setEditable(false);
        PlayerPsychicEnergyField.setText("0");

        jLabel17.setText("Psychic");

        jLabel18.setText("Lightning");

        jLabel19.setText("Water");

        ViewLightningEnergyField.setEditable(false);
        ViewLightningEnergyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewLightningEnergyFieldActionPerformed(evt);
            }
        });

        ViewWaterEnergyField.setEditable(false);

        jLabel20.setText("Lightning");

        ViewPsychicEnergyField.setEditable(false);

        jLabel21.setText("Psychic");

        jLabel22.setText("Fighting");

        ViewFightingEnergyField.setEditable(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Hand");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Bench");

        PlayerRewardCardLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        PlayerRewardCardLabel.setText("Rewards: 6");

        OpponentRewardCardLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        OpponentRewardCardLabel.setText("Rewards: 0");

        javax.swing.GroupLayout PlayerHandPanelLayout = new javax.swing.GroupLayout(PlayerHandPanel);
        PlayerHandPanel.setLayout(PlayerHandPanelLayout);
        PlayerHandPanelLayout.setHorizontalGroup(
            PlayerHandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PlayerHandPanelLayout.setVerticalGroup(
            PlayerHandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        PlayerBenchPanel.setPreferredSize(new java.awt.Dimension(0, 50));

        javax.swing.GroupLayout PlayerBenchPanelLayout = new javax.swing.GroupLayout(PlayerBenchPanel);
        PlayerBenchPanel.setLayout(PlayerBenchPanelLayout);
        PlayerBenchPanelLayout.setHorizontalGroup(
            PlayerBenchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PlayerBenchPanelLayout.setVerticalGroup(
            PlayerBenchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        OpponentDamageField.setEditable(false);
        OpponentDamageField.setText("0");

        OpponentRewardCardLabel1.setText("DamageTaken");

        OpponentRewardCardLabel2.setText("DamageTaken");

        PlayerDamageField.setEditable(false);
        PlayerDamageField.setText("0");

        OpponentRewardCardLabel3.setText("DamageTaken");

        ViewDamageField.setEditable(false);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Bench");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Hand");

        javax.swing.GroupLayout OpponentBenchPanelLayout = new javax.swing.GroupLayout(OpponentBenchPanel);
        OpponentBenchPanel.setLayout(OpponentBenchPanelLayout);
        OpponentBenchPanelLayout.setHorizontalGroup(
            OpponentBenchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        OpponentBenchPanelLayout.setVerticalGroup(
            OpponentBenchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        PlayerAttack1Btn.setText("Attack1");
        PlayerAttack1Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerAttack1BtnActionPerformed(evt);
            }
        });

        PlayerAttack2Btn.setText("Attack2");
        PlayerAttack2Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerAttack2BtnActionPerformed(evt);
            }
        });

        PlayerRetreatBtn.setText("Retreat");
        PlayerRetreatBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerRetreatBtnActionPerformed(evt);
            }
        });

        PlayerParalyzedLabel.setText("Paralyzed");

        PlayerAsleepLabel.setText("Asleep");

        PlayerStuckLabel.setText("Stuck");

        PlayerPoisonedLabel.setText("Poisoned");

        OpponentParalyzedLabel.setText("Paralyzed");

        OpponentAsleepLabel.setText("Asleep");

        OpponentStuckLabel.setText("Stuck");

        OpponentPoisonedLabel.setText("Poisoned");

        AnnouncementBox.setEditable(false);
        AnnouncementBox.setText("Play initial Pokemon");

        jLabel12.setText("Current Phase:");

        jLabel13.setText("Coin:");

        CoinResultField.setEditable(false);

        PassBtn.setText("End Turn");
        PassBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PassBtnActionPerformed(evt);
            }
        });

        OpponentDeckSize.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        OpponentDeckSize.setText("Deck Size: 60");

        PlayerDeckSize.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        PlayerDeckSize.setText("Deck Size: 60");

        OpponentGraveyard.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        OpponentGraveyard.setText("Graveyard: 0");

        PlayerGraveryard.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        PlayerGraveryard.setText("Graveyard: 0");

        OpponentHandScrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        OpponentHandScrollPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout();
        flowLayout1.setAlignOnBaseline(true);
        OpponentHandPanel.setLayout(flowLayout1);
        OpponentHandScrollPanel.setViewportView(OpponentHandPanel);

        CardPreviewPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CardPreviewPanel.setPreferredSize(new java.awt.Dimension(235, 307));
        CardPreviewPanel.setLayout(new java.awt.GridLayout(1, 1));

        playerActivePokemonPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        playerActivePokemonPanel.setPreferredSize(new java.awt.Dimension(235, 307));
        playerActivePokemonPanel.setLayout(new java.awt.GridLayout(1, 1));

        OpponentActivePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        OpponentActivePanel.setPreferredSize(new java.awt.Dimension(235, 307));
        OpponentActivePanel.setLayout(new java.awt.GridLayout(1, 1));

        jLabel14.setText("Colorless");

        OpponentColorlessEnergyField.setEditable(false);
        OpponentColorlessEnergyField.setText("0");

        PlayerColorlessEnergyField.setEditable(false);
        PlayerColorlessEnergyField.setText("0");

        jLabel23.setText("Colorless");

        jLabel24.setText("Colorless");

        ViewColorlessEnergyField.setEditable(false);
        ViewColorlessEnergyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewColorlessEnergyFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(OpponentRewardCardLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(OpponentParalyzedLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(OpponentDeckSize)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addComponent(OpponentRewardCardLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OpponentDamageField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(OpponentGraveyard)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(OpponentAsleepLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(PlayerGraveryard)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PlayerAsleepLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(PlayerRewardCardLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PlayerParalyzedLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(PlayerStuckLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(PlayerPoisonedLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(OpponentStuckLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(OpponentPoisonedLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(PlayerDeckSize)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(OpponentRewardCardLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PlayerDamageField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(playerActivePokemonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OpponentActivePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(OpponentRewardCardLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ViewDamageField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel9))
                                        .addGap(29, 29, 29)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(OpponentLightningEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(OpponentFightingEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(OpponentPsychicEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(OpponentWaterEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(OpponentColorlessEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel23)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(PlayerColorlessEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel18)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(PlayerLightningEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel17)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(PlayerPsychicEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(PlayerWaterEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(PlayerAttack2Btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                            .addComponent(PlayerAttack1Btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(PlayerFightingEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(PlayerRetreatBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(PassBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CoinResultField, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(AnnouncementBox, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(CardPreviewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ViewLightningEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(15, 15, 15)
                                        .addComponent(ViewFightingEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ViewPsychicEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ViewWaterEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ViewColorlessEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PlayerBenchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
                            .addComponent(PlayerHandPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(OpponentHandScrollPanel)
                            .addComponent(OpponentBenchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(OpponentHandScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(OpponentBenchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(304, 304, 304)
                        .addComponent(jLabel1)
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PlayerDeckSize, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(PlayerDamageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(OpponentRewardCardLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PlayerParalyzedLabel)
                            .addComponent(PlayerRewardCardLabel))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PlayerAsleepLabel)
                            .addComponent(PlayerGraveryard))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addGap(9, 9, 9)
                        .addComponent(PlayerStuckLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PlayerPoisonedLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OpponentDamageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OpponentRewardCardLabel1)
                            .addComponent(OpponentDeckSize))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OpponentParalyzedLabel)
                            .addComponent(OpponentRewardCardLabel))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OpponentAsleepLabel)
                            .addComponent(OpponentGraveyard))
                        .addGap(15, 15, 15)
                        .addComponent(OpponentStuckLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(OpponentPoisonedLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(AnnouncementBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(CoinResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ViewFightingEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ViewLightningEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ViewPsychicEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ViewWaterEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ViewColorlessEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24)))
                            .addComponent(CardPreviewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(OpponentActivePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OpponentFightingEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OpponentLightningEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OpponentPsychicEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OpponentWaterEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OpponentColorlessEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ViewDamageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OpponentRewardCardLabel3))
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PlayerFightingEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PlayerLightningEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PlayerPsychicEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PlayerWaterEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PlayerColorlessEnergyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23))
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PlayerAttack1Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PlayerRetreatBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(PlayerAttack2Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PassBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(playerActivePokemonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(PlayerBenchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(PlayerHandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    

    private void OpponentLightningEnergyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpponentLightningEnergyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpponentLightningEnergyFieldActionPerformed

    private void PlayerLightningEnergyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerLightningEnergyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PlayerLightningEnergyFieldActionPerformed

    private void ViewLightningEnergyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewLightningEnergyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewLightningEnergyFieldActionPerformed

    private void PlayerAttack1BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerAttack1BtnActionPerformed
        if(GameController.useActivePokemonForPlayer(0,0))
        {
        	GameController.getHomePlayer().resolveEffects(GameController.getHomePlayer().getActivePokemon());
    		GameController.startAITurn();
        }
    }//GEN-LAST:event_PlayerAttack1BtnActionPerformed

    private void PlayerAttack2BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerAttack2BtnActionPerformed
        if(GameController.useActivePokemonForPlayer(0,1))
        {
    		GameController.getHomePlayer().resolveEffects(GameController.getHomePlayer().getActivePokemon());
    		GameController.startAITurn();
        }
    }//GEN-LAST:event_PlayerAttack2BtnActionPerformed

    private void PlayerRetreatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerRetreatBtnActionPerformed
        
    }//GEN-LAST:event_PlayerRetreatBtnActionPerformed

    private void PassBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PassBtnActionPerformed
    	GameController.getHomePlayer().resolveEffects(GameController.getHomePlayer().getActivePokemon());
    	GameController.startAITurn();
    }//GEN-LAST:event_PassBtnActionPerformed

    private void ViewColorlessEnergyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewColorlessEnergyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewColorlessEnergyFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    //public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameBoard().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField AnnouncementBox;
    private javax.swing.JPanel CardPreviewPanel;
    private javax.swing.JTextField CoinResultField;
    private javax.swing.JPanel OpponentActivePanel;
    private javax.swing.JLabel OpponentAsleepLabel;
    public javax.swing.JPanel OpponentBenchPanel;
    private javax.swing.JTextField OpponentColorlessEnergyField;
    private javax.swing.JTextField OpponentDamageField;
    private javax.swing.JLabel OpponentDeckSize;
    private javax.swing.JTextField OpponentFightingEnergyField;
    private javax.swing.JLabel OpponentGraveyard;
    private javax.swing.JPanel OpponentHandPanel;
    private javax.swing.JScrollPane OpponentHandScrollPanel;
    private javax.swing.JTextField OpponentLightningEnergyField;
    private javax.swing.JLabel OpponentParalyzedLabel;
    private javax.swing.JLabel OpponentPoisonedLabel;
    private javax.swing.JTextField OpponentPsychicEnergyField;
    private javax.swing.JLabel OpponentRewardCardLabel;
    private javax.swing.JLabel OpponentRewardCardLabel1;
    private javax.swing.JLabel OpponentRewardCardLabel2;
    private javax.swing.JLabel OpponentRewardCardLabel3;
    private javax.swing.JLabel OpponentStuckLabel;
    private javax.swing.JTextField OpponentWaterEnergyField;
    private javax.swing.JButton PassBtn;
    private javax.swing.JLabel PlayerAsleepLabel;
    private javax.swing.JButton PlayerAttack1Btn;
    private javax.swing.JButton PlayerAttack2Btn;
    public javax.swing.JPanel PlayerBenchPanel;
    private javax.swing.JTextField PlayerColorlessEnergyField;
    private javax.swing.JTextField PlayerDamageField;
    private javax.swing.JLabel PlayerDeckSize;
    private javax.swing.JTextField PlayerFightingEnergyField;
    private javax.swing.JLabel PlayerGraveryard;
    private javax.swing.JPanel PlayerHandPanel;
    private javax.swing.JTextField PlayerLightningEnergyField;
    private javax.swing.JLabel PlayerParalyzedLabel;
    private javax.swing.JLabel PlayerPoisonedLabel;
    private javax.swing.JTextField PlayerPsychicEnergyField;
    private javax.swing.JButton PlayerRetreatBtn;
    private javax.swing.JLabel PlayerRewardCardLabel;
    private javax.swing.JLabel PlayerStuckLabel;
    private javax.swing.JTextField PlayerWaterEnergyField;
    private javax.swing.JTextField ViewColorlessEnergyField;
    private javax.swing.JTextField ViewDamageField;
    private javax.swing.JTextField ViewFightingEnergyField;
    private javax.swing.JTextField ViewLightningEnergyField;
    private javax.swing.JTextField ViewPsychicEnergyField;
    private javax.swing.JTextField ViewWaterEnergyField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JPanel playerActivePokemonPanel;
    // End of variables declaration//GEN-END:variables

	public javax.swing.JPanel getOpponentActivePanel() {
		return OpponentActivePanel;
	}

	public void setOpponentActivePanel(javax.swing.JPanel opponentActivePanel) {
		OpponentActivePanel = opponentActivePanel;
	}

	public javax.swing.JPanel getOpponentBenchPanel() {
		return OpponentBenchPanel;
	}

	public void setOpponentBenchPanel(javax.swing.JPanel opponentBenchPanel) {
		OpponentBenchPanel = opponentBenchPanel;
	}

	public javax.swing.JLabel getOpponentGraveyard() {
		return OpponentGraveyard;
	}

	public void setOpponentGraveyard(javax.swing.JLabel opponentGraveyard) {
		OpponentGraveyard = opponentGraveyard;
	}

	public javax.swing.JPanel getOpponentHandPanel() {
		return OpponentHandPanel;
	}

	public void setOpponentHandPanel(javax.swing.JPanel opponentHandPanel) {
		OpponentHandPanel = opponentHandPanel;
	}

	public javax.swing.JButton getPlayerAttack1Btn() {
		return PlayerAttack1Btn;
	}

	public void setPlayerAttack1Btn(javax.swing.JButton playerAttack1Btn) {
		PlayerAttack1Btn = playerAttack1Btn;
	}

	public javax.swing.JButton getPlayerAttack2Btn() {
		return PlayerAttack2Btn;
	}

	public void setPlayerAttack2Btn(javax.swing.JButton playerAttack2Btn) {
		PlayerAttack2Btn = playerAttack2Btn;
	}

	public javax.swing.JLabel getPlayerGraveryard() {
		return PlayerGraveryard;
	}

	public void setPlayerGraveryard(javax.swing.JLabel playerGraveryard) {
		PlayerGraveryard = playerGraveryard;
	}

	public javax.swing.JPanel getPlayerHandPanel() {
		return PlayerHandPanel;
	}

	public void setPlayerHandPanel(javax.swing.JPanel playerHandPanel) {
		PlayerHandPanel = playerHandPanel;
	}

	public javax.swing.JButton getPlayerRetreatBtn() {
		return PlayerRetreatBtn;
	}

	public void setPlayerRetreatBtn(javax.swing.JButton playerRetreatBtn) {
		PlayerRetreatBtn = playerRetreatBtn;
	}

	public javax.swing.JPanel getPlayerActivePokemonPanel() {
		return playerActivePokemonPanel;
	}

	public void setPlayerActivePokemonPanel(javax.swing.JPanel playerActivePokemonPanel) {
		this.playerActivePokemonPanel = playerActivePokemonPanel;
	}
    
    
    
}
