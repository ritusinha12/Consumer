package com.ritu.consumer;

public class Constants {
	public final static String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
	public final static String JMS_FACTORY = "P6ConnFacJNDI";
	public final static String QUEUE = "P6EvntQ5MayJNDI";
	public final static String WEBLOGIC_URL = "t3://localhost:9012";
	
	public final static String DBurl = "jdbc:oracle:thin:@//localhost:1521/pdborcl";  // db instance as service
	public final static String DBuser = "RITU8011";
	public final static String DBpwd = "oracle";
	public final static String DBdriver = "oracle.jdbc.driver.OracleDriver";
}
