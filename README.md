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

The Observers and the Subject are built, we now have to use these. To make this we have to provide the implementation of the Observer, in this case take for example an activity and which implements the Observer interface.
This is NOT sufficient to use the pattern, to provide an instance of our Subject we'll realize a simple mechanism to get the Subject, in other world we'll have to bind the service and use them as an android remote server.

```java

public abstract class AbObserverActivity extends FragmentActivity implements Observer{
	
	protected ControllerServ mService;
	private boolean mBound = false;
	
	@Override
	protected void onStart() {
		super.onStart();
		// Bind to ControllerServ
		Intent intent = new Intent(this, ControllerServ.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
			rem();
		}
	}

	/**
	 * Register this Observer to the Subject
	 */
	protected void rec() {
		super.onResume();
		// segister Observer
		mService.registerObserver(this);
	}
	
	/**
	 * Deregister this Observer from the Subject
	 */
	protected void rem() {
		super.onPause();
		// deregister observer
		mService.deregidterObserver(this);
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
				IBinder service) {
			// We've bound to ControllerServ, cast the IBinder and get ControllerServ instance
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
			rec();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
			rem();
		}
	};

}

```

With this approach in onStart method we'll get the instance of Subject and then we'll register the activity as Observer, in onStop method we'll deregister the Observer and unbind the service. In this way we've avoid to leak the activity (or some other component who want to register itself).

## Service's implementation

One of the most interesting aspect is the engine used handle a multiple downloads. In this case i used a ThreadPoolExecutor with a fix number of threads:

```java
executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_THREAD);
```

To begin a new thread to download we'll use the service a a local service called by this:

```java
// we are sending an intent to tell to start a new download
public static void startDownloadItem(Context context, ItemToDownload infoItem) {
		Intent i = new Intent(context, ControllerServ.class);
		i.putExtra(ControllerServ.KEY_ITEM_DOWNLOAD, infoItem);
		context.startService(i);
}
```

and we'll process it in the onStartCommand:

```java

@Override
public int onStartCommand(Intent intent, int flags, int startId) {

	ItemToDownload fileInfo	= (ItemToDownload) intent.getSerializableExtra(KEY_ITEM_DOWNLOAD);
	if(packageMap.containsKey(fileInfo.getPackageName()) && !fileInfo.isActionRemove()){
		return 0;
	}
	mStartId = startId;

	command = FileDownloader.newInstance(handler, fileInfo);
		
	/**
	 * keep trace to Task if I have not remove it
	 */
	if(!fileInfo.isActionRemove())
		packageMap.put(fileInfo.getPackageName(), command);
	
	if(command != null) {
		if(!fileInfo.isActionRemove()){
			getParallelExecutor().execute(command);
		}else{
			interruptTask(getParallelExecutor(), fileInfo);
		}
	}
	/**
	 * significa che se si interrompe l'esecuzione del servizio non ripartira'�in automatico
	 */
	return START_NOT_STICKY;
}

```

## Display the result
In this case the best solition to show to user the progress of download is create a recycleview and for each item layout apply the observer pattern and using the following method  to register and unregister from the Subject:

```

@Override       
public void onViewAttachedToWindow(View v) {
      // register to subject in this case will be the Activity or fragment
}

@Override
public void onViewDetachedFromWindow(View v) {
     // deregister to subject in this case will be the Activity or fragment
}

```

As writed on comments for the view holder a good solution is use the frgment as Subject becose they'll call often the above methods.

##under construction
