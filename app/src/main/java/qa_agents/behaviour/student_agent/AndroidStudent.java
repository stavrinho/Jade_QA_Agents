package qa_agents.behaviour.student_agent;

import jade.core.AID;
import jade.core.Agent;
import jade.util.Logger;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import java.util.Map;
import java.util.HashMap;


public class AndroidStudent extends Agent {

    private transient Logger myLogger = Logger.getJADELogger(getClass().getName());

    private static String q;
    public static Map<String, String> question;
    public static Map<String, String> answer ;
    private static AID student;
    private static AID Teacher = new AID( "Teacher", AID.ISLOCALNAME );
    private static boolean isAnswerSend = false;
    int n = 0;


    @Override
    protected void setup()  {
        addBehaviour(new CyclicBehaviour(this)
        {
            //int n = 0;


            public void action() {

                if(n==0) {
                    ACLMessage tmsg = new ACLMessage(ACLMessage.INFORM);
                    tmsg.setContent("Staring the process");
                    tmsg.addReceiver(Teacher);
                    send(tmsg);
                    myLogger.log(Logger.INFO, "\n" + "- Android Agent: " + getLocalName() + " i have set the message to" + tmsg.getSender() + "\n");
                    n++;
                }

                //receiving the message from Student after he has migrate to he phone
                ACLMessage msg = receive();
                if (msg != null) {
                    q = msg.getContent();
                    myLogger.log( Logger.INFO, "\n"+"- Android Agent:  "+getLocalName()+" Received::  "+ q +"\n");

                    student = msg.getSender(); //keeping the sender for future handling
                    String[] vals = q.substring( 1,q.length()-1 ).split( "," );
                    question = new HashMap<String, String>();

                    for (int i=0; i<vals.length; i++ ){
                        String[] str = vals[i].trim().split( "=" );
//                        System.out.println(" --*****--Parsing: "+ i + "-" +str[0]+":"+str[1]);
                        question.put(str[0], str[1]);
                    }

                } else {
                    block();
                }
            }

        });

        addBehaviour(new TickerBehaviour( this, 500 ){
            protected void onTick() {
                if (answer!=null && !isAnswerSend ){
                    System.out.println(" --*****--ANSWER SENDING ");
                    ACLMessage emsg = new ACLMessage(ACLMessage.INFORM);
                    emsg.setContent(answer.toString());
                    emsg.addReceiver(student);
                    send(emsg);
                    isAnswerSend = true;
                }
            }
        });

    } //end setup


}


