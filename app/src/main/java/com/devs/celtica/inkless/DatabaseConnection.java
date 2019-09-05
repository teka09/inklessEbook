package com.devs.celtica.inkless;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devs.celtica.inkless.Publications.Book;

import java.util.ArrayList;

public class DatabaseConnection extends SQLiteOpenHelper {

    public static final String DbName ="Databasebook.db";
    public static final int DbVersion=1;

    // pour la table Book
    public static final String TABLE_NAME="BOOK";

   /* public static final String ID="id";
    public static final String Nom1="nom1";
    public static final String Nom2="nom2";
    public static final String Photo="Photo";
    public static final String Lien="Lien";

    String CREATE_Table_Book = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + ID + " INTEGER PRIMARY KEY , " + Nom1 +
            " TEXT, " + Nom2 + " TEXT, " + Photo + " TEXT, "
             + Lien + " TEXT)";*/

    public DatabaseConnection(Context context){
        super(context, DbName,null,DbVersion);
    }

       // ici onCreate et onUpgrade

    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS BOOK (id INTEGER PRIMARY KEY  ,nom1 TEXT , nom2 TEXT ,Photo TEXT , Lien TEXT)");

        //db.execSQL(CREATE_Table_Book);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db , int Oldversion ,int NewVersion){
        db.execSQL(" DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    //Insert values to the table BOOK this methode is used to insert data of book in our Database
    public void AddBook(Book book){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values=new ContentValues();

        values.put("nom1", book.id_pub);
        values.put("nom1", book.nom1);
        values.put("nom2", book.nom2);
        values.put("Photo", book.photo);
        values.put("Lien", book.lien);


        db.insert(TABLE_NAME, null, values);

        Log.d("#######################","Réponse Add Book :===> Données bien Inseré");

        db.close();
    }





    // cette methode pour récuperer touts les données w hna ndir wach nhab comme collection
    public ArrayList<Book> GetDataBook(){

        ArrayList<Book> BOOKS =new ArrayList<Book>();

        SQLiteDatabase db =this.getReadableDatabase();

        Log.d("#######################", "Début récupération \n");

            String query ="Select * from BOOK";

              Cursor resultat = db.rawQuery(query,null);

        resultat.moveToFirst();

        while (resultat.isAfterLast() == false){

            BOOKS.add(new Book(resultat.getString(1),resultat.getString(2),resultat.getString(3),resultat.getString(4)));

            resultat.moveToNext();
        }

       for (int i =0;i<BOOKS.size();i++) {

            Log.d("#######################", "############################################## \n");
            Log.d("#######################", "Lien Du Livre Book :===>" +BOOKS.get(i).lien);
            Log.d("#######################", "NOM 1 DU Book :===>"+BOOKS.get(i).nom1);
            Log.d("#######################", "NOM 2 DU  Book :===>"+BOOKS.get(i).nom2);
            Log.d("#######################", "lien Photo  :===>"+BOOKS.get(i).photo);

            Log.d("#######################", "############################################## \n");
        }

        resultat.close();
        db.close();

        return BOOKS;
    }


    //Vérification si le live est déja téléchargé ou pas
    public boolean FileDownloaded(int id_book){
        int nbr=0;

        SQLiteDatabase db =this.getReadableDatabase();

        Log.d("#######################", "Début De Vérification \n");

        String query ="Select COUNT(*) from BOOK where id="+id_book;

        Cursor resultat = db.rawQuery(query,null);

        if(resultat.getCount()>0)
        {
            resultat.moveToFirst();
            nbr=resultat.getInt(0);

        }

        if (nbr>0){
            return true;
        }else
        {
            return false;
        }

    }
}
