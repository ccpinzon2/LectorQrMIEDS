package com.example.cristhianpinzon.lectorqr.Persistence.logic.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cristhianpinzon.lectorqr.Persistence.logic.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristhian Pinzon on 09/08/2017.
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    private static DatabaseAccess instance;

    public DatabaseAccess(Context context) {
        this.openHelper = new DBOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if (instance == null)
            instance = new DatabaseAccess(context);

        return instance;
    }
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }
    public List<User> getUsers(){
        List<User> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM usuario",null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            String id_user = cursor.getString(0);
            String name_user = cursor.getString(1);
            String id_tienda = cursor.getString(2);
            String nombre_tienda = cursor.getString(3);
            String direccion_tienda = cursor.getString(4);
            String telefono_tienda = cursor.getString(5);
            String tipo_tienda = cursor.getString(6);
            String marca_tienda = cursor.getString(7);
             String logo_tienda= cursor.getString(8);
            list.add(new User(id_user,name_user,id_tienda,nombre_tienda,direccion_tienda,telefono_tienda,tipo_tienda,marca_tienda,logo_tienda));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void deleteUser (){

        database.execSQL("DELETE FROM usuario");

    }


    public void addUser (User user) {
        if (user!=null){
            ContentValues values = new ContentValues();
            values.put("id_user",user.getId_user());
            values.put("name_user",user.getName_user());
            values.put("id_tienda",user.getId_tienda());
            values.put("nombre_tienda",user.getNombre_tienda());
            values.put("direccion_tienda",user.getDireccion_tienda());
            values.put("telefono_tienda",user.getTelefono_tienda());
            values.put("tipo_tienda",user.getTipo_tienda());
            values.put("marca_tienda",user.getMarca_tienda());
            values.put("logo_tienda",user.getLogo_tienda());
            Log.e("VALUES" ,values.get("logo_tienda").toString());
            database.insert("usuario",null,values);
            database.close();
        }else {
            Log.e("DATABASEACCESS","Error adduser en el metodo");
        }
    }


}
