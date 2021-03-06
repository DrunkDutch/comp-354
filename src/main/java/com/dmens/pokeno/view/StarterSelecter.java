package com.dmens.pokeno.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;

public class StarterSelecter extends CardSelectorBase {

    public StarterSelecter(List<Card> cards, Player player)
    {
        super(cards, player);
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
                player.useCard(card);
                GameController.setActivePokemonOnBoard((Pokemon)card, true);
                GameController.updateHand(GameController.getHomePlayer().getHand(), true);
                GameController.board.update();

                dispose();
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
}
