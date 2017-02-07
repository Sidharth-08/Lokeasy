package cardview.com.lokeasy;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Sidharth on 27-Jan-17.
 */
public final class InternalStorage{



    public static void writeObject(Context context, String key, String object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeChars(object);//.writeObject(object);
        oos.close();
        fos.close();
    }

    public static String readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        String object = ois.readLine();//.readObject();
        return object;
    }
}
