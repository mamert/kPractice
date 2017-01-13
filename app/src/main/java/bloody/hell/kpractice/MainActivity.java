package bloody.hell.kpractice;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.LinkedList;

import bloody.hell.kpractice.utils.BaseFrag;

public class MainActivity extends AppCompatActivity implements MainFrag.OnFragmentInteractionListener{

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        navigateTo(R.id.menu_item_main, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if(!navigateTo(item.getItemId(), false))
            return super.onOptionsItemSelected(item);
        return true;
    }



    // navigation stuff

    private int currentNavSection = -1;
    private LinkedList<Integer> fragBackstack_WellKindOf = new LinkedList<>();

    public boolean navigateTo(int itemId, boolean refreshEvenIfSameTab){
        return navigateTo(itemId, refreshEvenIfSameTab, false, false);
    }
    public boolean navigateTo(
            int itemId,
            boolean refreshEvenIfSameTab,
            boolean clearBackStack,
            boolean removePreviousInstancesFromBackstack) {
        BaseFrag frag = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (!refreshEvenIfSameTab && currentNavSection == itemId)
            return false; // do nothing, same section

        String fragTag = null;
        switch (itemId) {
            case R.id.menu_item_main:
                frag = MainFrag.newInstance();
                fragTag = MainFrag.TAG;
                break;
            default:
                Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
                break;
        }

        if (frag != null) {
            currentNavSection = itemId;
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, frag, fragTag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

            Integer iid = new Integer(itemId);

            if(clearBackStack){
                fragBackstack_WellKindOf.clear();
            } else if(removePreviousInstancesFromBackstack){
                fragBackstack_WellKindOf.remove(iid);
            }
            fragBackstack_WellKindOf.push(iid);
            return true;
        }
        return false;
    }





    // stuff from OnFragmentInteractionListeners
    @Override
    public void testSendingStuffToActivity(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
