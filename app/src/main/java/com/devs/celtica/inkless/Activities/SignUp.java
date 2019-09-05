package com.devs.celtica.inkless.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Narrator;
import com.devs.celtica.inkless.Users.ReaderFull;
import com.devs.celtica.inkless.Users.Writer;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SignUp extends AppCompatActivity {
    RadioGroup type_user;
    RadioButton reader,writer,narrator;
    EditText username,email,mdp,confirmMdp,ccp;
    TextView nation,typeUserLabel;
    LinearLayout div_ccp,div_type_user;
    ScrollView div_insc;
    ProgressDialog progress;
    SpinnerDialog spinnerNations;
    public static boolean inscTerminé=false;
    public static boolean isOnSend=false;//pour que le user n envoie pas de sign up au meme temps

    RadioButton type_userChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (savedInstanceState != null) {
            //region Revenir a au Login ..
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //endregion
        }else {

            progress=new ProgressDialog(this);

            type_user=(RadioGroup)findViewById(R.id.signUp_type);
            reader=((RadioButton)findViewById(R.id.signUp_reader));
            writer=((RadioButton)findViewById(R.id.signUp_writer));
            narrator=((RadioButton)findViewById(R.id.signUp_narrator));
            div_type_user=((LinearLayout)findViewById(R.id.div_type_user));
            div_insc=((ScrollView)findViewById(R.id.div_insc_form));
            div_ccp=((LinearLayout)findViewById(R.id.signUp_divCCP));
            type_userChecked=reader;
            typeUserLabel=((TextView)findViewById(R.id.signUp_insc_type));
            typeUserLabel.setText(getResources().getString(R.string.signUp_reader));


            //region les input de formulaire  d'inscription ..
            username=(EditText)findViewById(R.id.signUp_insc_username);
            email=(EditText)findViewById(R.id.signUp_insc_email);
            mdp=(EditText)findViewById(R.id.signUp_insc_mdp);
            confirmMdp=(EditText)findViewById(R.id.signUp_insc_confirmMdp);
            ccp=(EditText)findViewById(R.id.signUp_insc_ccp);
            nation=(TextView)findViewById(R.id.signUp_insc_nation);


            final ArrayList<String> nations=new  ArrayList<String> ();
            for (String nation:getResources().getStringArray(R.array.nations_array)){
                nations.add(nation);
            }
            spinnerNations=new SpinnerDialog(SignUp.this,nations,getResources().getString(R.string.signUp_nationChoose),"Close");// With No Animation
            spinnerNations.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {
                    nation.setText(s+"");
                }
            });
            nation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    spinnerNations.showSpinerDialog();

                }
            });
            //endregion

            //region change check type user
            ((LinearLayout)findViewById(R.id.signUp_readerButt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reader.setChecked(true);
                    typeUserLabel.setText(getResources().getString(R.string.signUp_reader));
                }
            });

            ((LinearLayout)findViewById(R.id.signUp_writer_butt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    writer.setChecked(true);
                    typeUserLabel.setText(getResources().getString(R.string.signUp_writer));
                }
            });

            ((LinearLayout)findViewById(R.id.signUp_narratorButt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    narrator.setChecked(true);
                    typeUserLabel.setText(getResources().getString(R.string.signUp_narrator));
                }
            });

            type_user.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    type_userChecked=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());

                }
            });
            //endregion

            //region valider type et afficher formulaire d inscription
            ((Button)findViewById(R.id.signUp_type_valider)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    div_type_user.animate()
                            .setDuration(500)
                            .alpha(0)
                            .start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            div_type_user.setVisibility(View.GONE);
                            div_insc.setVisibility(View.VISIBLE);
                            if (type_userChecked==reader){
                                div_ccp.setVisibility(View.GONE);
                            }else {
                                div_ccp.setVisibility(View.VISIBLE);
                            }
                            div_insc.animate()
                                    .setDuration(500)
                                    .alpha(1)
                                    .start();
                        }
                    },520);
                }
            });
            //endregion

            //region valider l inscription
            ((Button)findViewById(R.id.signUp_insc_valider)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(username.getText().toString().equals("") || email.getText().toString().equals("") || mdp.getText().toString().equals("") || confirmMdp.getText().toString().equals("") || nation.getText().toString().equals("") || (type_userChecked!=reader && ccp.getText().toString().equals(""))){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.signUp_errRemplisage),Toast.LENGTH_SHORT).show();
                    }else {

                        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_email_err), Toast.LENGTH_SHORT).show();
                        else{
                            if(!mdp.getText().toString().equals(confirmMdp.getText().toString())){
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.signUp_confirmMdpErr),Toast.LENGTH_SHORT).show();
                            }else {
                                if (!isOnSend) {
                                    isOnSend = true;
                                    if (type_userChecked == reader) {
                                        new ReaderFull(5, username.getText().toString(), "", email.getText().toString(), mdp.getText().toString(), nation.getText().toString(), "").signUp(SignUp.this);
                                    } else if (type_userChecked == writer) {
                                        new Writer(5, username.getText().toString(), "", email.getText().toString(), mdp.getText().toString(), nation.getText().toString(), "", ccp.getText().toString()).signUp(SignUp.this);
                                    } else {
                                        new Narrator(5, username.getText().toString(), "", email.getText().toString(), mdp.getText().toString(), nation.getText().toString(), "", ccp.getText().toString()).signUp(SignUp.this);
                                    }
                                }
                            }
                        }

                    }
                }
            });
            //endregion
        }

    }

    public void revenirLogin(){
        //region Revenir a au Login ..
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //endregion
    }

    @Override
    public void onBackPressed() {
        if (isOnSend){
            finish();
        }else {
            if (div_insc.getVisibility()== View.VISIBLE) {
                div_insc.animate()
                        .setDuration(500)
                        .alpha(0)
                        .start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        div_insc.setVisibility(View.GONE);
                        div_type_user.setVisibility(View.VISIBLE);
                        if (type_userChecked == reader) {
                            div_ccp.setVisibility(View.GONE);
                        }
                        div_type_user.animate()
                                .setDuration(500)
                                .alpha(1)
                                .start();
                    }
                }, 520);
            }else {
                super.onBackPressed();
            }
        }
    }
}
