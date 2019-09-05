package com.devs.celtica.inkless.Users;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.Publications.AccueilBooksAdapter;
import com.devs.celtica.inkless.Publications.AfficherBooks;
import com.devs.celtica.inkless.Publications.TypeFiles;
import com.devs.celtica.inkless.Publications.UploadAudio;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Publications.UploadPdf;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        if (savedInstanceState != null) {
            //region Revenir a au Login ..
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //endregion
        }else {

            LinearLayout uploadButt=((LinearLayout)findViewById(R.id.profile_uploadButt));
            TextView user_type=(TextView)findViewById(R.id.profile_type);
            final CircleImageView profile_image=(CircleImageView)findViewById(R.id.profile_image);

            ((TextView)findViewById(R.id.profile_name)).setText(Login.reader.nom+"");



            //region récupéré la photo de profile ..
            Glide.with(Profile.this)
                    .load(Login.ajax.url+"/"+Login.reader.photo)
                    .thumbnail(Glide.with(Profile.this).load(R.drawable.wait))
                    .apply(new RequestOptions().override(400, 600))
                    //.error(Glide.with(Profile.this).load(R.drawable.bg_butt_bleu_fonce))
                    .into(profile_image);
            //endregion

            //region changer la photo de profile
            profile_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(Profile.this,profile_image);

                    popup.getMenu().add(getResources().getString(R.string.profile_photoAffiche));
                    popup.getMenu().add(getResources().getString(R.string.profile_photoChange));


                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().toString().equals(getResources().getString(R.string.profile_photoAffiche))){

                            }else if(item.getTitle().toString().equals(getResources().getString(R.string.profile_photoChange))){
                                Login.reader.openSelectFile(Profile.this, TypeFiles.PHOTO);
                            }

                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                    return false;
                }
            });
            //endregion

            if(Login.reader instanceof Writer){

                user_type.setText(getResources().getString(R.string.signUp_writer));

                //region voir stat ..
                ((LinearLayout)findViewById(R.id.profile_stat)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Profile.this, AfficherBooks.class));
                    }
                });
                //endregion

                //region upload new file ..
                uploadButt.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                       if(!((Writer)Login.reader).contrat_writer_valide){
                           Log.e("tstt","nope");
                           Toast.makeText(getApplicationContext(),getResources().getString(R.string.writer_noContrat),Toast.LENGTH_SHORT).show();

                       }else {

                           //region storage permission
                           if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                               //File write logic here
                               requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
                               return;
                           }
                           //endregion

                           else {
                               startActivity(new Intent(Profile.this, UploadPdf.class));

                               /*
                               AlertDialog.Builder mb = new AlertDialog.Builder(Profile.this); //c est l activity non le context ..

                               View v = getLayoutInflater().inflate(R.layout.div_pub_choice, null);
                               TextView book = (TextView) v.findViewById(R.id.div_choiceBook);
                               TextView audio = (TextView) v.findViewById(R.id.div_choiceAudio);

                               book.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       startActivity(new Intent(Profile.this, UploadPdf.class));
                                   }
                               });

                               audio.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       startActivity(new Intent(Profile.this, UploadAudio.class));
                                   }
                               });

                               mb.setView(v);
                               final AlertDialog ad = mb.create();
                               ad.show();
                               ad.setCanceledOnTouchOutside(false); //ne pas fermer on click en dehors ..
                               */
                           }
                       }



                    }
                });
                //endregion

            }else if(Login.reader instanceof Narrator){
                user_type.setText(getResources().getString(R.string.signUp_narrator));
            }
            else {
                user_type.setText(getResources().getString(R.string.signUp_reader));
                uploadButt.setVisibility(View.GONE);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Login.reader.changeProfilePhoto(Profile.this,data.getData());
        }
    }
}
