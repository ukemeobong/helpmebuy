package com.andela.helpmebuy.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.fragments.RequestActivityFragment;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.utilities.ConnectionRequestListener;
import com.andela.helpmebuy.utilities.CurrentUserManager;

public class ContactActivity extends AppCompatActivity
        implements ConnectionRequestListener {

    private Context context = ContactActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_request);

        manageToolbar();
    }

    private void manageToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onConnectionUpdate(Connection connection) {
        String currentUserId = CurrentUserManager.get(context).getId();
        updateConnection(connection, connectionUrl(currentUserId));

        Connection connection1 = new Connection(connection.getConnectionStatus());
        connection1.setId(currentUserId);
        connection1.setMessage(connection.getMessage());
        connection1.setReceiver(currentUserId);
        connection1.setSender(connection.getSender());

        updateConnection(connection1, connectionUrl(connection.getSender()));
    }

    private void updateConnection(Connection connection, String connectionUrl) {
        new FirebaseCollection<>(connectionUrl, Connection.class)
                .save(connection, new DataCallback<Connection>() {
                    @Override
                    public void onSuccess(Connection data) {
                        displayMessage(R.string.operation_successful);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        displayMessage(R.string.operation_failed);
                    }
                });
    }

    private String connectionUrl(String userId) {
        return Constants.CONNECTIONS + "/" + userId;
    }

    private void displayMessage(int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RequestActivityFragment fragment = (RequestActivityFragment) getSupportFragmentManager()
                .getFragments().get(0);
        fragment.stopTimer();
    }
}
