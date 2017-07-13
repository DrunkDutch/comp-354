package com.dmens.pokeno.view;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
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

public class MultiCardViewerFrame extends javax.swing.JFrame {

	public MultiCardViewerFrame()
	{
		JTextField f = new JTextField();
		f.setText("TestTest");
		add(f);
	}
	
	public MultiCardViewerFrame(ArrayList<Card> cards)
	{
		JPanel p = new JPanel();
		add(p);
		p.setLayout(new FlowLayout());
		
		for (Card card : cards)
		{
	        JTextField newCard = new JTextField();
	        newCard.setText(card.getName());
	        newCard.setEditable(false);
	        
	        MouseListener viewCard = new java.awt.event.MouseListener()
	        {
	            @Override
	            public void mouseClicked(MouseEvent me)
	            {
	            	
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
	        newCard.addMouseListener(viewCard);
	        
	        invalidate();
	        validate();
	        repaint();
		}
    }
}
