package com.spotify11.demo.exception;

public class CurrentUserException extends Exception{

    private static final long serialVersionUID = 1L;

    public CurrentUserException(){

    }
    public CurrentUserException(String message){
        super(message);
    }
    
}
