package com.dmens.pokeno.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

public class MultiCardViewerFrame extends javax.swing.JFrame
{
	
	ArrayList<JTextField> options;
	ArrayList<Card> possibleCards;
	ArrayList<Card> selections;
	
	public MultiCardViewerFrame()
	{
		JTextField f = new JTextField();
		f.setText("TestTest");
		add(f);
	}
	
	public MultiCardViewerFrame(ArrayList<Card> cards, ArrayList<Card> results) //results is sent by reference, it's where the selected cards are sent
	{
		selections = results;
		options = new ArrayList<JTextField>();
		possibleCards = new ArrayList<Card>();
		
		JPanel p = new JPanel();
		add(p);
		p.setLayout(new FlowLayout());
		
		//TODO - I should add a method which is called by the creator after construction which returns the selected values (that's when it's visible)
		//TODO - What's the best way to make some selectable and some not, an overloaded constructor I guess?
		//TODO - I still need a constructor for "no selections" or this is a subclass of the one with no selectables
		
		for (Card card : cards)
		{
	        JTextField newCard = new JTextField();
	        newCard.setText(card.getName());
	        newCard.setEditable(false);
	        Color grey = new Color(238,238,238);
	        newCard.setBackground(grey);
	        
	        MouseListener viewCard = new java.awt.event.MouseListener()
	        {
	            @Override
	            public void mouseClicked(MouseEvent me)
	            {
	            	if(newCard.getBackground().equals(grey))
	            		newCard.setBackground(Color.ORANGE);
	            	else if (newCard.getBackground().equals(Color.ORANGE))
            			newCard.setBackground(grey);
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
                    GameController.board.cardPreview(card);
                    GameController.board.update();
	            }

	            @Override
	            public void mouseExited(MouseEvent me)
	            {
	            	GameController.board.cleanCardPreview();
	            }
	        };
            p.add(newCard);
            options.add(newCard);
            possibleCards.add(card);
	        newCard.addMouseListener(viewCard);
	        
	        invalidate();
	        validate();
	        repaint();
		}
		
		JButton doneBtn = new JButton("Done");
		MouseListener doneBtnListner = new java.awt.event.MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
            	for (int i = 0; i < options.size(); i++)
            	{
            		if (options.get(i).getBackground().equals(Color.ORANGE))
            		{
            			selections.add(possibleCards.get(i));
            			System.out.println(possibleCards.get(i));
            		}
            	}
            	dispose();
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
                
            }

            @Override
            public void mouseExited(MouseEvent me)
            {
            	
            }
        };
		p.add(doneBtn);
		doneBtn.addMouseListener(doneBtnListner);
    }
}
