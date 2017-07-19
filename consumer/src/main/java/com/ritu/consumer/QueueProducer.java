package com.ritu.consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueProducer {
	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private QueueSender qsender;
	private Queue queue;
	private TextMessage msg;

	/**
	 * Creates all the necessary objects for sending messages to a JMS queue.
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
		qsender = qsession.createSender(queue);
		msg = qsession.createTextMessage();
		qcon.start();
	}

	/**
	 * Sends a message to a JMS queue.
	 *
	 * @param message
	 *            message to be sent
	 * @exception JMSException
	 *                if JMS fails to send message due to internal error
	 */
	public void send(String message) throws JMSException {
		msg.setText(message);
		qsender.send(msg);
	}

	/**
	 * Closes JMS objects.
	 * 
	 * @exception JMSException
	 *                if JMS fails to close objects due to internal error
	 */
	public void close() throws JMSException {
		qsender.close();
		qsession.close();
		qcon.close();
	}

	/**
	 * main() method.
	 *
	 * @param args
	 *            WebLogic Server URL
	 * @exception Exception
	 *                if operation fails
	 */
	public static void main(String[] args) throws Exception {
		/*if (args.length != 1) {
			System.out.println("Usage: java examples.jms.queue.QueueSend WebLogicURL");
			return;
		}*/
		String weblogicUrl = Constants.WEBLOGIC_URL;
		
		InitialContext ic = getInitialContext(weblogicUrl);
		QueueProducer qs = new QueueProducer();
		qs.init(ic, Constants.QUEUE);
		readAndSend(qs);
		qs.close();
	}

	private static void readAndSend(QueueProducer qs) throws IOException, JMSException {
		BufferedReader msgStream = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		boolean quitNow = false;
		do {
			System.out.print("Enter message (\"quit\" to quit): \n");
			line = msgStream.readLine();
			if (line != null && line.trim().length() != 0) {
				qs.send(line);
				System.out.println("JMS Message Sent: " + line + "\n");
				quitNow = line.equalsIgnoreCase("quit");
			}
		} while (!quitNow);

	}

	private static InitialContext getInitialContext(String url) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, Constants.JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, url);
		return new InitialContext(env);
	}
}
