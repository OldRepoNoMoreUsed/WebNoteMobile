package com.lonaso.webnotesmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lonaso.webnotesmobile.connection.ConnectionFragment;
import com.lonaso.webnotesmobile.notes.NoteStore;
import com.lonaso.webnotesmobile.groups.ListGroupFragment;
import com.lonaso.webnotesmobile.notes.ListeNoteFragment;
import com.lonaso.webnotesmobile.groups.UpdateUserFragment;
import com.lonaso.webnotesmobile.users.UserStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String m_currentFragmentTag; //save the current tag button to redisplay after a orientation change
    private static final String CURRENT_FRAGMENT_TAG = "currentFragmentTag"; //used for the bundle key
    protected OnBackPressedListener onBackPressedListener;

    private Toolbar toolbar;

    private static DrawerLayout drawer;
    private static NavigationView navigationView;
    private static ImageView userAvatar;
    private static Bitmap avatarBmp;

    private NoteStore noteStore = new NoteStore();

    public NoteStore getNoteStore(){
        return noteStore;
    }

    public interface OnBackPressedListener{
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        userAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.navUserAvatarImageView);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        displaySelectedScreen(R.id.nav_connection);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if(onBackPressedListener != null){
            onBackPressedListener.doBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_note:
                fragment = new ListeNoteFragment();
                break;
            case R.id.nav_groupe:
                fragment = new ListGroupFragment();
                break;
            case R.id.nav_compte:
                fragment = new UpdateUserFragment();
                break;
            case R.id.nav_connection:
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                fragment = new ConnectionFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            m_currentFragmentTag = fragment.getTag();
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }

    // Method from :
    // http://stackoverflow.com/questions/12555420/how-to-get-a-uri-object-from-bitmap
    public static Uri bitmapToUriConverter(Context context, Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File(context.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = context.openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
//            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }

    public static void setDrawerLock(int lockMode) {
        MainActivity.drawer.setDrawerLockMode(lockMode);
    }

    public static void setNavHeader(String name, String email, final String avatar)
    {
        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.navUserNameTextView);
        TextView userEmail = (TextView) header.findViewById(R.id.navUserEmailTextView);
        userName.setText(UserStore.USER.getName());
        userEmail.setText(UserStore.USER.getEmail());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(avatar).openStream();
                    avatarBmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (avatarBmp != null)
                    userAvatar.setImageBitmap(avatarBmp);
            }

        }.execute();
    }
}
