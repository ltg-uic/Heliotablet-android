package ltg.heliotablet_android.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ReasonContentProvider extends ContentProvider {

    private static final String AUTHORITY = "ltg.heliotablet_android.data";
    // create content URIs from the authority by appending path to database
    // table
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/reasons");
    private static final int ALL_REASONS = 1;
    private static final int SINGLE_REASON = 2;
    // a content URI pattern matches content URIs using wildcard characters:
    // *: Matches a string of any valid characters of any length.
    // #: Matches a string of numeric characters of any length.
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "reasons", ALL_REASONS);
        uriMatcher.addURI(AUTHORITY, "reasons/#", SINGLE_REASON);
    }

    private ReasonDBOpenHelper database;

    @Override
    public boolean onCreate() {
        //database = new ReasonDBOpenHelper(getContext());
        return false;
    }

    // The insert() method adds a new row to the appropriate table, using the
    // values
    // in the ContentValues argument. If a column name is not in the
    // ContentValues argument,
    // you may want to provide a default value for it either in your provider
    // code or in
    // your database schema.
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        if (uriType != ALL_REASONS) {
            throw new IllegalArgumentException("Invalid URI for insert");
        }
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long newID = sqlDB
                .insert(ReasonDBOpenHelper.TABLE_REASON, null, values);
        if (newID > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, newID);
            getContext().getContentResolver().notifyChange(uri, null);
            return newUri;
        } else {
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = database.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ReasonDBOpenHelper.TABLE_REASON);

        switch (uriMatcher.match(uri)) {
            case ALL_REASONS:
                // do nothing
                break;
            case SINGLE_REASON:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(ReasonDBOpenHelper.COLUMN_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    // Return the MIME type corresponding to a content URI
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_REASONS:
                return "ltg.heliotablet.cursor.dir/ltg.heliotablet.data.reasons";
            case SINGLE_REASON:
                return "ltg.heliotablet.cursor.item/ltg.heliotablet.data.reasons";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
