package c301.AdventureBook.Controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import c301.AdventureBook.Models.Story;

import android.content.Context;
import android.util.Log;

/**
 *  The CacheManager is responsible for caching and the retrieving of such cached stories.
 *  It will check if the cache is full before doing such operations and clear it if 
 *  it is in order for new stories to be cached.
 *  
 *  Tutorial: http://stackoverflow.com/questions/9942560/when-to-clear-the-cache-dir-in-android
 * 
 *  @author Justin Hoy
 * 
 */
public class CacheManager {

	private static final long MAX_SIZE = 5242880L; // 5MB
	ArrayList<Story> mCacheLibrary;
	Context applicationContext;

	private static CacheManager instance = null;

	public static CacheManager getInstance() {
		if (instance == null) {
			instance = new CacheManager();
		}
		return instance;
	}

	/**
	 * Initializes the manager to the application's context.
	 * 
	 * @param context the activityContext to set
	 */
	public void initApplicationContext(Context context) {
		this.applicationContext = context;
	}

	/**
	 * Caches a given library of stories into the phone after checking the cache size.
	 * 
	 * @param cacheLibrary the library of stories to be cached
	 */
	public void cacheData(ArrayList<Story> cacheLibrary) {

		try{
			FileOutputStream fos = null;
			ObjectOutputStream oos;
			ByteArrayOutputStream b;
			for (Story aStory:cacheLibrary) {
				b = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(b);
				oos.writeObject(aStory);
				File cacheDir = applicationContext.getCacheDir();
				long size = getDirSize(cacheDir);
				long newSize =  + size;
				if (newSize > MAX_SIZE) {
					cleanDir(cacheDir, newSize - MAX_SIZE);
				}

				String FILENAME = aStory.getFilename();
				File file = new File(cacheDir, FILENAME);
				fos = new FileOutputStream(file);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(aStory);
				Log.d("Successful Story Cache: ", aStory.getTitle());
				fos.close();
			}
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves all cached stories into the phone and sets it to be the current cache library in this manager.
	 * 
	 */
	public void retrieveData(){
		File cacheDir = applicationContext.getCacheDir();
		ArrayList<Story> tempLibrary = new ArrayList<Story>();

		if (cacheDir!= null && cacheDir.isDirectory()) {
			Log.v("Trim", "can read " + cacheDir.canRead());
			String[] fileNames = cacheDir.list();

			//Iterate for the fileName and delete
			for (String fileStr:fileNames) {
				Log.v("Cached", fileStr);
				// do something with the file
				if (fileStr.toLowerCase().contains(".sav")) {
					try {
						File file = new File(cacheDir, fileStr);
						FileInputStream fis = new FileInputStream(file);
						ObjectInputStream ois = new ObjectInputStream(fis);
						while (true) {
							Story someStory = (Story) ois.readObject();
							tempLibrary.add(someStory);
							Log.d("Success Cache Load", someStory.getTitle());
						}

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			this.mCacheLibrary = tempLibrary;
		}
	}

	/**
	 * Cleans the given directory to be the predefined maximum size allowed (in bytes).
	 * 
	 * @param dir the directory to be cleaned
	 * @param bytes the max size allowed (in bytes)
	 */
	private static void cleanDir(File dir, long bytes) {
		long bytesDeleted = 0;
		File[] files = dir.listFiles();

		for (File file : files) {
			bytesDeleted += file.length();
			file.delete();

			if (bytesDeleted >= bytes) {
				break;
			}
		}
	}

	/**
	 * Retrieves the size of a given directory (in bytes).
	 * 
	 * @param dir the directory to be checked
	 */
	private static long getDirSize(File dir) {
		long size = 0;
		File[] files = dir.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				size += file.length();
			}
		}

		return size;
	}

	/**
	 * Retrieves the cache library of a phone.
	 * 
	 * @return the stories in cache
	 */
	public ArrayList<Story> getCacheLibrary() {
		retrieveData();
		return this.mCacheLibrary;
	}

	/**
	 * Deletes a given story from cache.
	 * 
	 * @param story the story to be deleted
	 */
	public void deleteCachedStory(Story story){
		//String FILENAME = story.getTitle().toLowerCase() + "-" + story.getAuthor().toLowerCase() + ".sav";
		String FILENAME = story.getFilename();

		File cacheDir = applicationContext.getCacheDir();
		File[] files = cacheDir.listFiles();

		for (File file : files) {
			if(file.getName().equals(FILENAME)){
				file.delete();
			}
		}
	}

}