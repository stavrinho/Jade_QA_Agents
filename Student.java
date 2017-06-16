//package qa_agents.behaviour.student_agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.util.Logger;
import jade.core.behaviours.*;
import jade.lang.acl.*;


import java.util.Map;
import java.util.HashMap;

public class Student extends Agent  {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    private transient Logger myLogger = Logger.getJADELogger(getClass().getName());
    private Location home;
    public  Map <String, String> question;
    public  Map <String, String> answer;
    AID AndroidStudent = new AID( "AndroidStudent", AID.ISLOCALNAME );
    AID Teacher = new AID( "Teacher", AID.ISLOCALNAME );

    @Override
    protected void setup()  {

        Object[] args = getArguments();
        if (args!=null){
            question = (HashMap <String, String>) args[0];
        }
        home = here();


        addBehaviour(new CyclicBehaviour() {
                         private static final long serialVersionUID = 435357658778L;

                         @Override
                         public void action() {
                             ACLMessage msg = receive();

                             if (msg != null) {

                                 myLogger.log( Logger.INFO, "\n"+"- Student Agent: "+getLocalName()+" - received message by "+msg.getSender().getName()+"\n");

                                 Location dest = null;

                                 if (here().getName().equals("Main-Container") && msg.getSender().getName().contains("Teacher")) {
                                     // I'm at home --> move to the Main Container
                                     dest = new ContainerID("Container-1", null );
                                     myLogger.log( Logger.INFO, "\n"+"- Student Agent: "+getLocalName()+" My new destination: "+dest.getName()+"\n");
                                     doMove(dest);
                                 }
                                 else  if ( msg.getSender().getName().contains("AndroidStudent") ) {
                                     // I'm elsewhere --> go home
                                     dest = home;

                                     String ans = msg.getContent().toString();
                                     String[] vals = ans.substring( 1,ans.length()-1 ).split( "," );
                                     answer = new HashMap<String, String>();

                                     for (int i=0; i<vals.length; i++ ){
                                         String[] str = vals[i].trim().split( "=" );
                                         //System.out.println(" --*****--Parsing: "+ i + "-" +str[0]+":"+str[1]);
                                         answer.put(str[0], str[1]);
                                     }

                                     myLogger.log( Logger.INFO, "\n"+"- Student Agent: "+getLocalName()+" My new destination: "+dest.getName()+"\n");
                                     doMove(dest);

                                 }

                             }
                             else {
                                 block();
                             }
                         }
                     }
        );
    } //end setup

    @Override
    public void beforeMove()  {
    }

    @Override
    public void afterMove()    {
        // Restore the logger
        myLogger = Logger.getJADELogger(getClass().getName());
        myLogger.log(Logger.INFO, "\nAgent "+getLocalName()+" After Move - Just arrived in location = "+here().getName());

        if (here().getName().equals("Container-1")) {

            myLogger.log( Logger.INFO, "\n"+"- Student Agent: "+getLocalName()+"Just arrived to Container-1\n");

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent(question.toString());
            msg.addReceiver(AndroidStudent);
            myLogger.log( Logger.INFO, "\n"+"- Student Agent: "+getLocalName()+"Sending question\n");
            send(msg);
        }

        if (here().getName().equals("Main-Container")) {

            myLogger.log( Logger.INFO, "\n"+"Student Agent: "+getLocalName()+"Just arrived to Main-Container\n");

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent(answer.toString());
            msg.addReceiver(Teacher);
            myLogger.log( Logger.INFO, "\n"+"- Student Agent: "+getLocalName()+"Sending answer: "+answer.toString()+"\n");
            send(msg);
        }

    } 



}

