package com.atlshearer.tournamentmanager.commands;

public class InvalidCommandNameException extends Exception {

	private static final long serialVersionUID = 688775120283396094L;

    public InvalidCommandNameException(String errorMessage) {
        super(errorMessage);
    }
}
