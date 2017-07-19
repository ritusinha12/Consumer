package com.ritu.consumer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;



public class QueueConsumer implements MessageListener {
	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private QueueReceiver qreceiver;
	private Queue queue;

	private boolean quit = false;

	/**
	 * Message listener interface.
	 * 
	 * @param msg
	 *            message
	 */
	public void onMessage(Message msg) {
		try {
			String msgText;
			if (msg instanceof TextMessage) {
				msgText = ((TextMessage) msg).getText();
			} else {
				msgText = msg.toString();
			}
			System.out.println("Message Received: " + msgText);

			//** RS Start**************************************************************************
			// 2. Parse the xml
			
			if(msgText!=null && !msgText.equalsIgnoreCase("quit")){
				System.out.println("RS Step 2 parse XML");
				Map<String,String> p6datahashmap =  new HashMap<String,String>();
				p6datahashmap.put("BONAME", "Project");
				//p6datahashmap.put("ID", "P-test3");
				p6datahashmap.put("ID", msgText);
				p6datahashmap.put("NAME", "inte test");
				p6datahashmap.put("PROJECTSTARTDATE", "15/Jun/17");
				p6datahashmap.put("PROJECTFINISHDATE", "16/July/17");
				p6datahashmap.put("EPS", "TEST");
				p6datahashmap.put("STATUS", "Active");
				System.out.println("Step 1 End ---Hashmap :"+p6datahashmap);
				// 3. Write data in Staging table

				System.out.println("RS Step 3 Start -- DB Connection");
				EBSStagingDBConnection cet = new EBSStagingDBConnection();
				Connection connection = cet.getDBConnection(Constants.DBurl , Constants.DBuser,Constants.DBpwd);
				try {
					cet.setProjectTableData(connection,p6datahashmap);
					System.out.println("Step 3 End...");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//** RS END***************************************************************************
			if (msgText.equalsIgnoreCase("quit")) {
				synchronized (this) {
					quit = true;
					this.notifyAll(); // Notify main thread to quit
				}
			}
		} catch (JMSException jmse) {
			System.err.println("An exception occurred: " + jmse.getMessage());
		}
	}

	/**
	 * Creates all the necessary objects for receiving messages from a JMS
	 * queue.
	 *
	 * @param ctx
	 *            JNDI initial context
	 * @param queueName
	 *            name of queue
	 * @exception NamingException
	 *                if operation cannot be performed
	 * @exception JMSException
	 *                if JMS fails to initialize due to internal error
	 */
	public void init(Context ctx, String queueName) throws NamingException, JMSException {
		qconFactory = (QueueConnectionFactory) ctx.lookup(Constants.JMS_FACTORY);
		qcon = qconFactory.createQueueConnection();
		qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) ctx.lookup(queueName);
		qreceiver = qsession.createReceiver(queue);
		qreceiver.setMessageListener(this);
		qcon.start();
	}

	/**
	 * Closes JMS objects.
	 * 
	 * @exception JMSException
	 *                if JMS fails to close objects due to internal error
	 */
	public void close() throws JMSException {
		qreceiver.close();
		qsession.close();
		qcon.close();
	}

	/**
	 * main() method.
	 *
	 * @param args
	 *            WebLogic Server URL
	 * @exception Exception
	 *                if execution fails
	 */
	public static void main(String[] args) throws Exception {
		/*
		if (args.length != 1) {
			System.out.println("Usage: java examples.jms.queue.QueueReceive WebLogicURL");
			return;
		}*/

		String weblogicUrl = Constants.WEBLOGIC_URL;
		InitialContext ic = getInitialContext(weblogicUrl);
		QueueConsumer qr = new QueueConsumer();
		qr.init(ic, Constants.QUEUE);
		System.out.println("JMS Ready To Receive Messages (To quit, send a \"quit\" message).");

		// Wait until a "quit" message has been received.
		synchronized (qr) {
			while (!qr.quit) {
				try {
					qr.wait();
				} catch (InterruptedException ie) {
				}
			}
		}
		qr.close();
	}

	private static InitialContext getInitialContext(String url) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, Constants.JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, url);
		return new InitialContext(env);
	}
}
