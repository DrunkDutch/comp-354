package com.dmens.pokeno.services;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.player.Player;

import java.util.ArrayList;
import java.util.List;

enum TargetTypes {
    YOUR_ACTIVE("your-active"),
    OPPONENT_ACTIVE("opponent-active"),
    CHOICE_OPPONENT("choice:opponent"), // Means both bench and active
    CHOICE_YOUR("choice:your"), // Means both bench and active
    CHOICE_OPPONENT_BENCH("choice:opponent- bench"),
    CHOICE_YOUR_BENCH("choice:your-bench"),
    CHOICE_YOUR_POKEMON("choice:your-pokemon"), // Means pokemon from a source see 'Wally'
    YOUR_BENCH("your-bench"),
    OPPONENT_BENCH("opponent-bench"),
    YOUR_HAND("your-hand"),
    OPPONENT_HAND("opponent-hand"),
    YOU("you"),
    THEM("them");

    private TargetTypes(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static TargetTypes fromName(String type){
        if(YOUR_ACTIVE.equals(type))
            return YOUR_ACTIVE;
        if(OPPONENT_ACTIVE.equals(type))
            return OPPONENT_ACTIVE;
        if(CHOICE_OPPONENT.equals(type))
            return CHOICE_OPPONENT;
        if(CHOICE_YOUR.equals(type))
            return CHOICE_YOUR;
        if(CHOICE_OPPONENT_BENCH.equals(type))
            return CHOICE_OPPONENT_BENCH;
        if(CHOICE_YOUR_BENCH.equals(type))
            return CHOICE_YOUR_BENCH;
        if(CHOICE_YOUR_POKEMON.equals(type))
            return CHOICE_YOUR_POKEMON;
        if(YOUR_BENCH.equals(type))
            return YOUR_BENCH;
        if(OPPONENT_BENCH.equals(type))
            return OPPONENT_BENCH;
        if(YOUR_HAND.equals(type))
            return YOUR_HAND;
        if(OPPONENT_HAND.equals(type))
            return OPPONENT_HAND;
        if(THEM.equals(type))
            return THEM;
        else
            return YOU;
    }

    public boolean equals(String name){
        return name.equals(this.name);
    }
    private String name;
}

public class TargetService {
    private Player you;
    private Player them;

    private static TargetService service = new TargetService();

    private TargetService(){}

    public static TargetService getInstance(){
        return service;
    }

    public void setYouPlayer(Player you){
        this.you = you;
    }

    public void setThemPlayer(Player them){
        this.them = them;
    }

    public List<Card> getTarget(String target){
        List<Card> targets = new ArrayList<>();
        TargetTypes type = TargetTypes.fromName(target);
        switch(type){
            case YOUR_ACTIVE:
                targets.add(you.getActivePokemon());
                break;
            case OPPONENT_ACTIVE:
                targets.add(them.getActivePokemon());
                break;
            case YOUR_BENCH:
                targets.addAll(you.getBenchedPokemon());
                break;
            case OPPONENT_BENCH:
                targets.addAll(them.getBenchedPokemon());
                break;
            case YOUR_HAND:
                targets.addAll(you.getHand().getCards());
                break;
            case OPPONENT_HAND:
                targets.addAll(them.getHand().getCards());
                break;
            case CHOICE_YOUR:
                targets.add(you.chooseFromAll());
                break;
            case CHOICE_OPPONENT:
                targets.add(them.chooseFromAll());
                break;
            case CHOICE_YOUR_BENCH:
                targets.add(you.chooseFromBench());
                break;
            case CHOICE_OPPONENT_BENCH:
                targets.add(them.chooseFromBench());
                break;
        }

        return targets;
    }

    /**
     * Change turn and switch players
     */
    public void passTurn(){
        Player tmp = you;
        you = them;
        them = tmp;
    }

}

