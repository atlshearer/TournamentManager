package com.atlshearer.tournamentmanager.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AdaptorStateChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Adaptor adaptor;
	
	public AdaptorStateChangeEvent(Adaptor adaptor) {
		this.adaptor = adaptor;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }

	public Adaptor getAdaptor() {
		return adaptor;
	}


}
