/**
 * 
 */
package chatSystemNetwork;

import chatSystemCommon.Hello;

/**
 * This class provides a unique and reusable message Hello
 * @author joanna
 *
 */
public class SingletonHello {

	private static SingletonHello instance;
	private Hello hello;
	
	/**
	 * private constructor
	 */
	private SingletonHello(){
		this.hello = new Hello(null,false);		
	}
	
	/**
	 * gets an instance of SingletonHello
	 * @return instance
	 */
	public static SingletonHello getInstance(){
		if (instance == null){
			instance = new SingletonHello();
		}
		return instance;
	}
	
	/**
	 * gets an instance of Hello
	 * @return instance of Hello initialized with null and false
	 */
	public Hello getHello(){
		return this.hello;
	}
}
