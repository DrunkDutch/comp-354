package com.dmens.pokeno.services.handlers;

import com.dmens.pokeno.services.SourceService;
import com.dmens.pokeno.services.TargetService;

public class SourceServiceHandler {
	private static SourceService service;
    private static SourceServiceHandler handler;
    
    public static SourceServiceHandler getInstance(){
        if(handler == null)
            handler = new SourceServiceHandler();
        return handler;
    }

    public static SourceServiceHandler getInstance(SourceService service){
        if(handler == null)
            handler = new SourceServiceHandler(service);
        return handler;
    }

    public static void clearInstance(){
        handler = null;
    }

    private SourceServiceHandler(){
        this(SourceService.getInstance());
    }
    
    /**
     * Constructor used for tests
     */
    public SourceServiceHandler(SourceService service){
        this.service = service;
    }

    public SourceService getService(){
        return service;
    }
}
