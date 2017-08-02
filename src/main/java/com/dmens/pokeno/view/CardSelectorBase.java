package com.dmens.pokeno.view;

import java.awt.FlowLayout;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.player.Player;

public abstract class CardSelectorBase extends JDialog {
	private static final long serialVersionUID = 1L;
	protected Player player;
	protected JPanel panel;
	
	public CardSelectorBase(List<Card> cards, Player player)
    {
		this.player = player;
        setModal(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        panel = new JPanel();
        add(panel);
        panel.setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        for (Card card : cards)
        {
            JTextField newCard = new JTextField();
            newCard.setText(card.getName());
            newCard.setEditable(false);

            MouseListener viewCard = registerListeners(card);
            panel.add(newCard);
            newCard.addMouseListener(viewCard);

            invalidate();
            validate();
            repaint();
        }
    }
	
	protected abstract java.awt.event.MouseListener registerListeners(Card card);

}
