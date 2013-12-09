/**
 * 
 */
package chatSystemNetwork;

import chatSystemCommon.Hello;

/**
 * @author joanna
 *
 */
public class SingletonHello {

	private static SingletonHello instance=null;
	private Hello hello;
	
	/**
	 * private constructor
	 */
	private SingletonHello(){
		this.hello = new Hello(null,false);		
	}
	
	/**
	 * 
	 * @return instance of SingletonHello
	 */
	public static SingletonHello getInstance(){
		if (instance == null){
			instance = new SingletonHello();
		}
		return instance;
	}
	
	/**
	 * 
	 * @return instance of Hello initialized with null and false
	 */
	public Hello getHello(){
		return this.hello;
	}
}
