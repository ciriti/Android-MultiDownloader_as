# 4. Android - MultiDownloader

This project is a little architecture with the purpose to show you how is possible to apply the Observer Pattern on android.

## Observer pattern
The observer pattern is used when you have objects where the state change of one object has to be notified to all object which are interested.

This is how, graphically, it works:

![Component](art/pattern.png)

The following image is the uml representation of it:

![Component](art/pattern-observer-struttura.png)

## Let's build the model

The observer interface can be build with the following structure:

```java
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
```
To realize the subject we'll extend a Service adding the following structure

```java

public abstract class AbstractControllerServ extends Service{

	/**
	 * List of the registered observers
	 */
	protected ArrayList<Observer> observers;

	/**
	 * Method to register the Ob
	 * @param ob
	 */
	public void registerObserver(Observer ob){
		this.observers.add(ob);
	}

	/**
	 * method to deregister
	 * @param ob
	 */
	public void deregidterObserver(Observer ob){
		if(this.observers.indexOf(ob) > 0)
			this.observers.remove(ob);
	}

	/**
	 * Method to tell to Observers which file was completed
	 * @param updateData
	 */
	public void succesDownlodDownloadObserver(GenericItemBean updateData){
		for (Observer ob : observers) {
			ob.succesDownlod(updateData);
		}
	}

	/**
	 * Method to send the current download progress of a specific item
	 * @param updateData
	 */
	public void updateObserver(GenericItemBean updateData){
		for (Observer ob : observers) {
			ob.update(updateData);
		}
	}

	/**
	 * Method used to the Observes that a download of an item was failed
	 * @param updateData
	 */
	public void errorObserver(GenericItemBean updateData){
		for (Observer ob : observers) {
			ob.error(updateData);
		}
	}

}


```

##under construction
