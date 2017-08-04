package com.dmens.pokeno.view;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;

public class MultiCardSelector <T extends Card> extends CardSelectorBase {
	private List<Card> pickedCards;
	private int amountOfCardsToPick;

	public MultiCardSelector(List<Card> cards, Player player, int amount){
		super(cards, player);
		pickedCards = new ArrayList<Card>();
		amountOfCardsToPick = amount;
		setTitle("Select " + amountOfCardsToPick + " cards.");
		setSize(300, 300);
		if (panel.getComponentCount() == 0)
		{
			dispose();
			return;
		}
        setVisible(true);
	}
	
	@Override
	protected MouseListener registerListeners(Card card) {
		return new java.awt.event.MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {

            }

            @Override
            public void mousePressed(MouseEvent me) {
            	pickedCards.add(card);
            	panel.remove((Component) me.getSource());
            	repaint();
            	if(pickedCards.size() == amountOfCardsToPick || panel.getComponentCount() == 0){
	            	setVisible(false);
            	}
            	setTitle("Select " + (amountOfCardsToPick - pickedCards.size()) + " cards.");
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
	}
	
	public List<Card> getSelectedCards(){
		if (panel.getComponentCount() == 0)
		{
			dispose();
			return pickedCards;
		}
		System.out.println(pickedCards.size());
		while(pickedCards.size() < amountOfCardsToPick && panel.getComponentCount() != 0);
		dispose();
		return pickedCards;
	}
}
