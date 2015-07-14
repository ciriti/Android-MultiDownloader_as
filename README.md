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


##under construction
