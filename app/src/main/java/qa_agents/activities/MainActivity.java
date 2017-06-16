package qa_agents.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import qa_agents.R;

import java.util.logging.Level;
import jade.android.AgentContainerHandler;
import jade.android.AgentHandler;
import jade.android.RuntimeCallback;
import jade.android.RuntimeService;
import jade.android.RuntimeServiceBinder;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.Logger;
import jade.wrapper.StaleProxyException;


/**
 * A connection screen that offers connection via host/port.
 */
public class MainActivity extends AppCompatActivity {

    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    private RuntimeServiceBinder runtimeServiceBinder;
    private AgentContainerHandler containerHandler;
    private AgentHandler agentHandler;
    private ServiceConnection serviceConnection;
    private Boolean isRunning = false;
    private MyHandler handler;
    Profile profile;

    // UI references.
    private AutoCompleteTextView mHostView;
    private EditText mPortView;
    private View mProgressView;
    private View mLoginFormView;


    private String host = "";
    private String port  = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the login form.
        mHostView = (AutoCompleteTextView) findViewById(R.id.host);
        mPortView = (EditText) findViewById(R.id.port);
        mPortView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.connect_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        handler = new MyHandler();
    }




    private void attemptLogin() {
        if (isRunning) {
            return;
        }

        // Reset errors.
        mHostView.setError(null);
        mPortView.setError(null);

        // Store values at the time of the login attempt.
        host = mHostView.getText().toString();
        port  = mPortView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(port)) {
            mPortView.setError(getString(R.string.error_invalid_password));
            focusView = mPortView;
            cancel = true;
        }

        // Check for a valid address.
        if (TextUtils.isEmpty(host)) {
            mHostView.setError(getString(R.string.error_field_required));
            focusView = mHostView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user connection attempt.
            showProgress(true);
            isRunning = true;
            start(host, port);

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void start(final String host, final String port) {
        if (runtimeServiceBinder == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    runtimeServiceBinder = (RuntimeServiceBinder) service;
                    profile = new ProfileImpl(false);
                    //Profile profile = new ProfileImpl("192.168.31.6", 1099, null, false);
                     profile = new ProfileImpl("stavrinho.ddns.net", 7778, null, false);

                    profile.setParameter(Profile.MAIN_HOST, host);
                    profile.setParameter(Profile.MAIN_PORT, port);

                    //profile.setParameter(Profile.MAIN_HOST, "192.168.31.6");
                    //profile.setParameter(Profile.MAIN_PORT, port);
                    //profile.setParameter(Profile.LOCAL_HOST, AndroidHelper.getLocalIPAddress());
                    //profile.setParameter(Profile.LOCAL_PORT,"54099");
                    //profile.setParameter(Profile.GUI, "true");

                    startJADE(profile);

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    runtimeServiceBinder = null;
                }
            };
            //Binds a service with our application
            bindService(new Intent(getApplicationContext(), RuntimeService.class),
                    serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void startJADE(Profile profile) {
        Log.i("Platform", "Creating Container!\n");
        runtimeServiceBinder.createAgentContainer(profile, new RuntimeCallback<AgentContainerHandler>() {
            @Override
            public void onSuccess(AgentContainerHandler agentContainerHandler) {
                Log.i("Platform", "Container successfully created!\n");
                containerHandler = agentContainerHandler;
                handler.sendSuccess();

                //Activity transition
                Intent intent = new Intent(MainActivity.this, QuestionerActivity.class);
                startActivity(intent);

                startAgent("AndroidStudent");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i("Platform", "start jade container failed!\n");
                handler.sendError();
            }
        });
    }

    private void startAgent(final String nickname) {
        Log.i("Platform", "Initializing Agent\n");
        containerHandler.createNewAgent(nickname, "qa_agents.behaviour.student_agent.AndroidStudent", null, new RuntimeCallback<AgentHandler>() {
            @Override
            public void onSuccess(AgentHandler ah) {
                agentHandler = ah;

                try {
                    agentHandler.getAgentController().start();
                    Log.i("Platform", "AndroidMobilityActivity - Mobile agent successfully created\n");
                    handler.sendSuccess();


                } catch (StaleProxyException e) {
                    Log.i("Platform", "AndroidMobilityActivity - start jade agent failed!\n");
                    handler.sendError();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                handler.sendError();
                logger.log(Level.WARNING, "start jade agent failed!");
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey("success")) {
                isRunning = false;
                showProgress(false);
                Toast.makeText(MainActivity.this, "Agent Started Successfully!", Toast.LENGTH_SHORT).show();
            } else if (bundle.containsKey("error")) {
                isRunning = false;
                showProgress(false);
                Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        public void sendSuccess() {
            Message msg = obtainMessage();
            Bundle b = new Bundle();
            b.putString("success", " ");
            msg.setData(b);
            sendMessage(msg);
        }

        public void sendError() {
            Message msg = obtainMessage();
            Bundle b = new Bundle();
            b.putString("error", " ");
            msg.setData(b);
            sendMessage(msg);
        }
    }
}

