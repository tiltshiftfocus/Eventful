package com.jerry.eventful;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by zm on 10/12/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sharedInstance;
    private Context context;

    DatabaseHelper(Context paramContext){
        super(paramContext, "Eventful", null, 1);
        context = paramContext;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (sharedInstance == null) {
            sharedInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sharedInstance;
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase){
        paramSQLiteDatabase.execSQL("create table events (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,when_db INTEGER NOT NULL);");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2){
        if (paramInt2 > paramInt1){
            Log.w(toString(), "Upgrading database from version " + paramInt1 + " to " + paramInt2 + ", which will destroy all old data");
            paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS events");
            onCreate(paramSQLiteDatabase);
        }
    }


    // Source from Tickmate
    // by Hannes Grauler
    private String getDatabasePath() {
        String db_path = context.getDatabasePath("Eventful").getAbsolutePath();
        return db_path;
    }

    public String getExternalDatabasePath(String name) throws IOException {
        String db_path = new File(getExternalDatabaseFolder(), name).getAbsolutePath();
        return db_path;
    }

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean exportDatabase(String externalName) throws IOException {
        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();

        File extDb = new File(getExternalDatabasePath(externalName));
        File myDb = new File(getDatabasePath());

        FileUtils.copyFile(new FileInputStream(myDb), new FileOutputStream(extDb));
        return true;
    }

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean importDatabase(String externalName) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();

        File extDb = new File(getExternalDatabasePath(externalName));
        File myDb = new File(getDatabasePath());
        if (extDb.exists()) {
            FileUtils.copyFile(new FileInputStream(extDb), new FileOutputStream(myDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    public String[] getExternalDatabaseNames() {

        File ext_dir;
        try {
            ext_dir = getExternalDatabaseFolder();
        } catch (IOException e) {
            return new String[0];
        }
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                File sel = new File(dir, filename);
                return filename.endsWith(".db") && !sel.isDirectory();
            }
        };
        return ext_dir.list(filter);

    }

    public File getExternalDatabaseFolder() throws IOException {

        // On Android 4.4, it is not possible to write on the external SD card
        // So fall back to getExternalFilesDir
        // We choose the first writable folder from the following list:
        File[] ext_dirs = {
                new File(FileUtils.getRemovableStorageDirectory(), "Eventful"),
                new File(FileUtils.getRemovableStorageDirectory(), "Android/data/" + context.getPackageName()),
                context.getExternalFilesDir("backup"),
                context.getExternalFilesDir(null),
                context.getFilesDir()
        };

        boolean valid = false;
        File ext_dir = null;
        for (int i = 0; !valid && i < ext_dirs.length; i++) {
            ext_dir = ext_dirs[i];
            if (ext_dir == null) {

                continue;
            }
            valid = true;
            if (!ext_dir.exists()) {
                if (ext_dir.mkdirs() == false) {
                    valid = false;

                    continue;
                }
            }
            if (valid && !ext_dir.canWrite()) { // check writing permissions
                valid = false;

                continue;
            }
        }
        if (!valid) {
            throw new IOException("Could not find external storage directory.");
        }
        return ext_dir;
    }


}
