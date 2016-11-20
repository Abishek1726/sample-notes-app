package mynote.com.example.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Abi on 9/16/2016.
 */
public class NotesDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "NotesDB";
    static Context context;
    private static boolean contextSet = false;
    private static NotesDatabaseHelper notesDatabaseHelper = null;

    public static boolean isContextSet()
    { return contextSet; }

    public static void setContext(Context context)
    {
        NotesDatabaseHelper.context = context;
    }

    public static NotesDatabaseHelper getInstance()
    {
        if(notesDatabaseHelper != null)
            return notesDatabaseHelper;

        if(context == null)
        throw new NullPointerException("NullPointerException : Context is null - set context in NotesDatabaseHelper before creating Singleton instance");

        notesDatabaseHelper = new NotesDatabaseHelper(context);
        return notesDatabaseHelper;
    }

    private NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NotesTable.CREATE_NOTES_TABLE);
        db.execSQL(CheckNotesTable.CREATE_CHECK_NOTES_TABLE);
        db.execSQL(CheckItemTable.CREATE_CHECK_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void deleteNote(int id) {
        try {
            getWritableDatabase().delete(NotesTable.TABLE_NAME, NotesTable.COLUMN_PRIMARY + " = " + String.valueOf(id), null);
        }
        catch (Exception e)
        {

        }
        getWritableDatabase().close();
    }

    public class NotesTable {
        public final static String TABLE_NAME = "NOTES";
        public final static String COLUMN_TITLE = "TITLE";
        public final static String COLUMN_TEXT = "NOTES_TEXT";
        public final static String COLUMN_PRIMARY = "_id";
        public final static String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_TITLE + " TEXT, " + COLUMN_TEXT
                + " TEXT, " + COLUMN_PRIMARY + " INTEGER PRIMARY KEY AUTOINCREMENT);";


    }

    public class CheckNotesTable
    {
        public final static String TABLE_NAME = "CHECK_NOTES";
        public final static String COLUMN_TITLE = "TITLE";
        public final static String COLUMN_PRIMARY = "_id";
        public final static String CREATE_CHECK_NOTES_TABLE = "CREATE TABLE "+TABLE_NAME+ "("+COLUMN_TITLE+" TEXT, "+COLUMN_PRIMARY+
                " INTEGER PRIMARY KEY AUTOINCREMENT); ";

    }

    public class CheckItemTable
    {
        public final static String TABLE_NAME = "CHECK_ITEM";
        public final static String COLUMN_CHECK_NOTES_ID_FOREIGN = "cn_id";
        public final static String COLUMN_PRIMARY = "_id";
        public final static String COLUMN_TEXT = "CHECK_NOTES_TEXT";
        public final static String COLUMN_CHECKED = "IS_CHECKED";
        public static final String CREATE_CHECK_ITEM_TABLE = "CREATE TABLE "+TABLE_NAME+"("+COLUMN_TEXT+" TEXT, "+COLUMN_PRIMARY+
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+ COLUMN_CHECK_NOTES_ID_FOREIGN+" INTEGER, "+COLUMN_CHECKED+" INTEGER, "+" FOREIGN KEY("+COLUMN_CHECK_NOTES_ID_FOREIGN+")"+
                " REFERENCES "+CheckNotesTable.TABLE_NAME+"("+CheckNotesTable.COLUMN_PRIMARY+" )  );";
    }

    public void insertCheckNote(CheckNote checkNote)
    {
        ContentValues checkNoteValues = new ContentValues();

        checkNoteValues.put(CheckNotesTable.COLUMN_TITLE,checkNote.getTitle());

        long id = getWritableDatabase().insert(CheckNotesTable.TABLE_NAME,null,checkNoteValues);

        Set<Map.Entry<String,Boolean>> set = checkNote.getCheckNoteMap().entrySet();

        Iterator<Map.Entry<String,Boolean>> iterator = set.iterator();

        while (iterator.hasNext())
        {
            ContentValues checkItemValues = new ContentValues();

            Map.Entry<String,Boolean> entry = iterator.next();

            checkItemValues.put(CheckItemTable.COLUMN_TEXT,entry.getKey());

            int checkFlag = (entry.getValue())?1:0;

            checkItemValues.put(CheckItemTable.COLUMN_CHECKED,checkFlag);
            checkItemValues.put(CheckItemTable.COLUMN_CHECK_NOTES_ID_FOREIGN,id);

            getWritableDatabase().insert(CheckItemTable.TABLE_NAME,null,checkItemValues);
        }

        getWritableDatabase().close();

    }

    public List<CheckNote> getCheckNoteList()
    {
        List<CheckNote> list = new ArrayList<>();

        Cursor c = getWritableDatabase().query(CheckNotesTable.TABLE_NAME,null,null,null,null,null,null);

        String query = "SELECT "+CheckItemTable.COLUMN_TEXT+", "+
                CheckItemTable.COLUMN_CHECKED+" FROM "+CheckItemTable.TABLE_NAME+" INNER JOIN "+CheckNotesTable.TABLE_NAME+" ON "+
                CheckItemTable.TABLE_NAME+"."+CheckItemTable.COLUMN_CHECK_NOTES_ID_FOREIGN+" = "+
                CheckNotesTable.TABLE_NAME+"."+CheckNotesTable.COLUMN_PRIMARY+" WHERE "+CheckNotesTable.TABLE_NAME+"."+CheckNotesTable.COLUMN_PRIMARY+" = ? ;";

        if(c.moveToFirst())
        while (!c.isAfterLast())
        {
            String title = c.getString(c.getColumnIndex(CheckNotesTable.COLUMN_TITLE));
            long id = c.getLong(c.getColumnIndex(CheckNotesTable.COLUMN_PRIMARY));

            Cursor c2 = getWritableDatabase().rawQuery(query,new String[]{String.valueOf(id)});

            Map<String,Boolean> map = new HashMap<>();

            if(c2.moveToFirst())
                while(!c2.isAfterLast())
                {
                    String key = c2.getString(c2.getColumnIndex(CheckItemTable.COLUMN_TEXT));
                    int v = c2.getInt(c2.getColumnIndex(CheckItemTable.COLUMN_CHECKED));
                    boolean val = (v==1)?true:false;

                    map.put(key,val);

                    c2.moveToNext();
                }

            list.add(new CheckNote(title,map));

            c.moveToNext();
        }

        getWritableDatabase().close();

        return list;
    }


    public void insertNote(Note note) {

        ContentValues values = new ContentValues();

        values.put(NotesTable.COLUMN_TITLE, note.getTitle());
        values.put(NotesTable.COLUMN_TEXT, note.getText());
        try {
            getWritableDatabase().insert(NotesTable.TABLE_NAME, null, values);
            Toast.makeText(context, "Saved successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        getWritableDatabase().close();
    }
    public void updateNote(Note note)
    {
        ContentValues values = new ContentValues();


        values.put(NotesTable.COLUMN_TITLE, note.getTitle());
        values.put(NotesTable.COLUMN_TEXT, note.getText());
        try {
            String whereClause = NotesTable.COLUMN_PRIMARY+ " = " + String.valueOf(note.getID());
            getWritableDatabase().update(NotesTable.TABLE_NAME,values,whereClause,null);

            Toast.makeText(context, "Saved successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            Log.d("sql exception",e.toString());
        }
        getWritableDatabase().close();
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<Note>();

        try {
            Cursor c = getReadableDatabase().query(NotesTable.TABLE_NAME, null, null, null, null, null, null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    int i = c.getColumnIndex(NotesTable.COLUMN_TITLE);
                    String title = c.getString(i);
                    i = c.getColumnIndex(NotesTable.COLUMN_TEXT);
                    String text = c.getString(i);
                    i = c.getColumnIndex(NotesTable.COLUMN_PRIMARY);
                    int id = c.getInt(i);

                    Note note = new Note(title, text);
                    note.setID(id);

                    noteList.add(note);

                    c.moveToNext();
                }
            }

        } catch (Exception e) {

            Toast.makeText(context,"SQL error",Toast.LENGTH_LONG).show();
        }

        getWritableDatabase().close();
        return noteList;
    }

}
