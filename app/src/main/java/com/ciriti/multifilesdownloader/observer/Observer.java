package com.ciriti.multifilesdownloader.observer;

import com.ciriti.multifilesdownloader.bean.GenericItemBean;

/**
 * 
 * @author carmelo.iriti
 *
 */
public interface Observer {

	/**
	 * This method is used to tell to the generic observer
	 * that the file was completely downloaded
	 * @param dataUpdate
	 */
	void succesDownlod(GenericItemBean dataUpdate);

	/**
	 * This method is used to send to the observer the percent
	 * downloaded of the file
	 * @param dataUpdate
	 */
	void update(GenericItemBean dataUpdate);

	/**
	 * This tell to the observer that there was an error
	 * @param dataUpdate
	 */
	void error(GenericItemBean dataUpdate);

}
