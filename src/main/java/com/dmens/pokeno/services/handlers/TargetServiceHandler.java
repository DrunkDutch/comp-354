package com.dmens.pokeno.services.handlers;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.TargetService;

import java.util.List;

public class TargetServiceHandler {

    private static TargetService service;
    private static TargetServiceHandler handler;

    public static TargetServiceHandler getInstance(){
        if(handler == null)
            handler = new TargetServiceHandler();
        return handler;
    }

    public static TargetServiceHandler getInstance(TargetService service){
        if(handler == null)
            handler = new TargetServiceHandler(service);
        return handler;
    }

    public static void clearInstance(){
        handler = null;
    }

    private TargetServiceHandler(){
        this(TargetService.getInstance());
    }

    /**
     * Constructor used for tests
     */
    public TargetServiceHandler(TargetService service){
        this.service = service;
    }

    public TargetService getService(){
        return service;
    }

    public List<Card> getTarget(String target){
        return service.getTarget(target);
    }

    public void setYouPlayer(Player you){
        service.setYouPlayer(you);
    }

    public void setThemPlayer(Player them){
        service.setThemPlayer(them);
    }
}
