//package qa_agents.behaviour.student_agent;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.lang.acl.*;
import java.util.Map;
import java.util.HashMap;
import jade.util.Logger;

public class Teacher extends Agent 
{

	String name = "Student" ;
	AID student = new AID( name, AID.ISLOCALNAME );
	private transient Logger myLogger = Logger.getJADELogger(getClass().getName());
  
	protected void setup() 
    {
		addBehaviour(new CyclicBehaviour() {

                         @Override
                         public void action() {
                             ACLMessage msg = receive();

                             if (msg != null) {
								 
								 myLogger.log( Logger.INFO, "\n"+"- Teacher Agent: "+getLocalName()+" - received message by "+msg.getSender().getName()+"\n");

							 if ( msg.getSender().getName().contains("AndroidStudent") ) {

                                 AgentContainer cur_container = getContainerController();
								try {
									//setting the initial questions
									Map <String, String> question  = new HashMap<String, String>();
									question.put("q1", "What is the sum of 1 + 1 in the binary system");
									question.put("a11", "1");
									question.put("a12", "10");
									question.put("a13", "11");
									question.put("q2", "Who is the father of computer science");
									question.put("a21", "Alan Turing");
									question.put("a22", "Liam Neeson");
									question.put("a23", "Elon Musk");
									question.put("a24", "Steve Jobs");
									
									// Creating the agent Student
									AgentController acontroller = cur_container.createNewAgent( name, "qa_agents.behaviour.student_agent.Student", new Object[]{question});
									acontroller.start();
									System.out.println("\n- Teacher--> " + name + " Agent Created \n");
									
									msg = new ACLMessage(ACLMessage.INFORM);
									msg.setContent(" ");
									msg.addReceiver(student);				
									send(msg);								
									
								}
								catch (Exception e){}
							 }
							 else if ( msg.getSender().getName().startsWith("Student") ) {
								 myLogger.log( Logger.INFO, "\n"+"- Teacher Agent: "+getLocalName()+" - Received Answer: "+msg.getContent().toString()+"\n");
								 
							 }

                             }
                             else {
                                 block();
                             }
                         }
                     }
				);
		
		

	}
}

