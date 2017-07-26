package com.dmens.pokeno.view;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class StarterSelecter extends JDialog {

    public StarterSelecter()
    {
        JTextField f = new JTextField();
        f.setText("TestTest");
        add(f);
    }

    public StarterSelecter(ArrayList<Pokemon> cards, Player player)
    {
        setModal(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        JPanel p = new JPanel();
        add(p);
        p.setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        for (Pokemon card : cards)
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
                    player.setActivePokemon(card);
                    GameController.setActivePokemonOnBoard(card, true);
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
            p.add(newCard);
            newCard.addMouseListener(viewCard);

            invalidate();
            validate();
            repaint();
        }
    }
}
