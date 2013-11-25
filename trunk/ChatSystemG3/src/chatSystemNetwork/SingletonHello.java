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

	private static SingletonHello instance;
	private Hello hello;
	
	private SingletonHello(){
		this.hello = new Hello(null,false);		
	}
	
	public static SingletonHello getInstance(){
		if (instance == null){
			instance = new SingletonHello();
		}
		return instance;
	}
	
	public Hello getHello(){
		return this.hello;
	}
}
