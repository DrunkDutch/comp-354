package com.dmens.pokeno.utils;

public class OptionPaneFactory {
    private static OptionPaneFactory factory;

    public static OptionPaneFactory getInstance(){
        if(factory == null)
            factory = new OptionPaneFactory();
        return factory;
    }

    public int createChoiceDialog(Object[] options, String title, boolean cancelable){
        return 0;
    }

    public void createMessageDialog(String message, String title){

    }
}
