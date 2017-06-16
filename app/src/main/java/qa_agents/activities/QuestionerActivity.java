package qa_agents.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import java.util.HashMap;
import java.util.Map;
import qa_agents.R;
import qa_agents.behaviour.student_agent.AndroidStudent;

import static qa_agents.behaviour.student_agent.AndroidStudent.question;

public class QuestionerActivity extends AppCompatActivity {

    //UI references
    private TextView welcome ;
    private TextView question1 ;
    private TextView question2 ;
    private Button receiveButton ;
    private Button sendButton ;
    private RadioGroup radioButtonGroup;
    private RadioButton r1 ;
    private RadioButton r2;
    private RadioButton r3;
    private CheckBox ch1;
    private CheckBox ch2;
    private CheckBox ch3;
    private CheckBox ch4;
    private int send=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questioner);
        welcome = (TextView) findViewById(R.id.StartMessage);
        question1 = (TextView) findViewById(R.id.Question1);
        question2 = (TextView) findViewById(R.id.Question2);
        receiveButton =(Button) findViewById(R.id.rec_button);
        sendButton = (Button) findViewById(R.id.send_button);
        r1 = (RadioButton) findViewById(R.id.radioButton1);
        r2 = (RadioButton) findViewById(R.id.radioButton2);
        r3 = (RadioButton) findViewById(R.id.radioButton3);
        ch1 = (CheckBox) findViewById(R.id.checkBox1);
        ch2 = (CheckBox) findViewById(R.id.checkBox2);
        ch3 = (CheckBox) findViewById(R.id.checkBox3);
        ch4 = (CheckBox) findViewById(R.id.checkBox4);
        radioButtonGroup = (RadioGroup) findViewById(R.id.GroupRadioButton);

        //Button for receiving questionary
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //msg_receive();
                welcome.setVisibility(View.GONE);
                question1.setVisibility(View.VISIBLE);
                question2.setVisibility(View.VISIBLE);
                radioButtonGroup.setVisibility(View.VISIBLE);
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                ch1.setVisibility(View.VISIBLE);
                ch2.setVisibility(View.VISIBLE);
                ch3.setVisibility(View.VISIBLE);
                ch4.setVisibility(View.VISIBLE);
                msg_receive();
                receiveButton.setAlpha(.5f); // make button disable
                receiveButton.setClickable(false); // make button disable
            }
        });

        //Button for sending the results
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("\n- SEND BUTTON PRESSED \n");
                int idx = radioButtonGroup.getCheckedRadioButtonId();
                RadioButton selected_radio = (RadioButton)  findViewById(idx);
                AndroidStudent.answer = new HashMap<String, String>();
                //Put Values to Map answer
                AndroidStudent.answer.put("q1", "Question 1");
                AndroidStudent.answer.put("a1", selected_radio.getText().toString());
                AndroidStudent.answer.put("q2", "Question 2");
                if (ch1.isChecked()) AndroidStudent.answer.put("a21", "Answer 1");
                if (ch2.isChecked()) AndroidStudent.answer.put("a22", "Answer 2");
                if (ch3.isChecked()) AndroidStudent.answer.put("a23", "Answer 3");
                if (ch4.isChecked()) AndroidStudent.answer.put("a24", "Answer 4");
//                AndroidStudent.isAnswerSend = true;

                if (send==0) {
                    Toast.makeText(QuestionerActivity.this, "You have Successfully send your answers to you Teacher", Toast.LENGTH_SHORT).show();
                    send++;
                }
                else {
                    Toast.makeText(QuestionerActivity.this, "You have already send your answers!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Handle the Visibility of Send Button
        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(r1.isChecked() || r2.isChecked() || r3.isChecked() ) {
                    sendButton.setVisibility(View.VISIBLE);
                }
                 if( !ch1.isChecked()&& !ch2.isChecked() && !ch3.isChecked() && !ch4.isChecked()) {
                    sendButton.setVisibility(View.GONE);
                }

            }
        }
        );
        // Handle the Visibility of Send Message Button
        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                           @Override
                                           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                               if(r1.isChecked() || r2.isChecked() || r3.isChecked() ) {
                                                   sendButton.setVisibility(View.VISIBLE);
                                               }
                                               if( !ch1.isChecked()&& !ch2.isChecked() && !ch3.isChecked() && !ch4.isChecked()) {
                                                   sendButton.setVisibility(View.GONE);
                                               }
                                           }
                                       }
        );

        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                           @Override
                                           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                               if(r1.isChecked() || r2.isChecked() || r3.isChecked() ) {
                                                   sendButton.setVisibility(View.VISIBLE);
                                               }
                                               if( !ch1.isChecked()&& !ch2.isChecked() && !ch3.isChecked() && !ch4.isChecked()) {
                                                   sendButton.setVisibility(View.GONE);
                                               }
                                           }
                                       }
        );

        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                         if(r1.isChecked() || r2.isChecked() || r3.isChecked() ) {
                                                             sendButton.setVisibility(View.VISIBLE);
                                                         }
                                                         if( !ch1.isChecked()&& !ch2.isChecked() && !ch3.isChecked() && !ch4.isChecked()) {
                                                             sendButton.setVisibility(View.GONE);
                                                         }
                                                     }
                                                 }
        );

        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                           @Override
                                           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                               if(ch1.isChecked() || ch2.isChecked() || ch3.isChecked()|| ch4.isChecked() ) {
                                                   sendButton.setVisibility(View.VISIBLE);
                                               }
                                               else {sendButton.setVisibility(View.GONE);}
                                           }
                                       }
        );

        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                              if(ch1.isChecked() || ch2.isChecked() || ch3.isChecked()|| ch4.isChecked() ) {
                                                  sendButton.setVisibility(View.VISIBLE);
                                              }
                                              else {sendButton.setVisibility(View.GONE);}
                                          }
                                      }
        );

        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                              if(ch1.isChecked() || ch2.isChecked() || ch3.isChecked()|| ch4.isChecked() ) {
                                                  sendButton.setVisibility(View.VISIBLE);
                                              }
                                              else {sendButton.setVisibility(View.GONE);}
                                          }
                                      }
        );

    }


    public void msg_receive() {
        Map<String, String> q =  question;
        question1.setText( q.get("q1") );
        question2.setText( q.get("q2") );

        r1.setText( q.get("a11") );
        r2.setText( q.get("a12") );
        r3.setText( q.get("a13") );

        ch1.setText( q.get("a21") );
        ch2.setText( q.get("a22") );
        ch3.setText( q.get("a23") );
        ch4.setText( q.get("a24") );

        //  Toast.makeText(MainActivity.this, msg_txt2, Toast.LENGTH_SHORT).show();
        //Toast.makeText(QuestionerActivity.this, "You have Successfully send your answers to you Teacher", Toast.LENGTH_SHORT).show();

    }



}