package ca.outercove.uomiapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import androidx.annotation.*;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import androidx.navigation.ui.AppBarConfiguration;
import android.content.Intent;



import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import ca.outercove.uomiapplication.appObjects.NotificationsContent.NotificationsListItem;
import ca.outercove.uomiapplication.appObjects.SingleAccountViewContent;
import ca.outercove.uomiapplication.fragments.AccountsViewFragment;
import ca.outercove.uomiapplication.fragments.CreateTransactionFragment;
import ca.outercove.uomiapplication.fragments.DashboardFragment;
import ca.outercove.uomiapplication.appObjects.AccountsViewContent.AccountsViewItem;
import ca.outercove.uomiapplication.fragments.SingleAccountFragment;
import ca.outercove.uomiapplication.fragments.NotificationsFragment;

public class MainActivity extends AppCompatActivity implements
AccountsViewFragment.OnListFragmentInteractionListener,
NotificationsFragment.OnListFragmentInteractionListener,
DashboardFragment.OnFragmentInteractionListener,
SingleAccountFragment.OnListFragmentInteractionListener,
CreateTransactionFragment.OnFragmentInteractionListener {

    protected NavController mNavController;
    protected AppBarConfiguration appBarConfiguration;

    private SharedPreferences pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        this.pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        System.out.println(this.pref.getInt("userId", -1));
        // Set up the app bar
        Toolbar appBar = findViewById(R.id.uomiAppBar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Retrieve NavController and setup the Navigation using nav_main
        mNavController = Navigation.findNavController(findViewById(R.id.nav_host_fragment));
        AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(mNavController.getGraph());
        appBarConfiguration = builder.build();


        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        NavigationUI.setupActionBarWithNavController(this,  mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, mNavController);

        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                // If there is an account name, make it the title, otherwise use Fragment label
                try {
                    Integer accountName = arguments.getInt("accountId");
                    setActionBarTitle(accountName.toString());
                } catch (NullPointerException e) {
                    setActionBarTitle((String)destination.getLabel());
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Intent upIntent=NavUtils.getParentActivityIntent(this);
                //You might need to add some Launch Parameters like
                upIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onListFragmentInteraction(SingleAccountViewContent.TransactionItem item) {

    }

    @Override
    public void onFragmentInteraction(String s) {
        //setActionBarTitle(s);


    }

    @Override
    public void onListFragmentInteraction(AccountsViewItem item) {
        Bundle bundle = new Bundle();
        bundle.putInt("accountId", item.id);
        mNavController.navigate(R.id.actionAccountSelect, bundle);
    }

    @Override
    public void onListFragmentInteraction(NotificationsListItem item) {

    }

    @Override
    public void onFragmentInteraction(Integer accId) {
        Bundle bundle = new Bundle();
        bundle.putInt("accountId", accId);
        mNavController.navigate(R.id.postTransactionCreation, bundle);
    }
}
