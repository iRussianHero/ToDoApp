package providers;

import android.content.ContentProvider;
import android.content.UriMatcher;
import android.net.Uri;

import java.util.HashMap;

public class ToDoContentProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.todoprovider";
    static final String PATH = "content://"+ PROVIDER_NAME + "/ToDos";
    static final Uri CONTENT_URI = Uri.parse(PATH);
    private static HashMap<String, String> TODOS_PROJECTION_MAP;
    static final int TODOS = 1;
    static final int TODO_ID = 2;
    static final UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "ToDos", TODOS);
        uriMatcher.addURI(PROVIDER_NAME, "TO_Dos/#", TODO_ID);
    }
}
