package com1032.cw1.ld00245.ld00245_todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.List;


public class List_Activity extends Activity {
    private ImageButton addButton;
    private ImageButton removeButton;
    private ListView lv;
    private List<String> items;
    private List<String> subitems;
    private TextView textView;
    private EditText input;
    private EditText subinput;
    private ArrayAdapter<String> adapter;
    private AlertDialog.Builder builder;
    private FileOutputStream outputStream;
    private DataStorage ds = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        lv = (ListView) findViewById(R.id.listView);
        items = intent.getExtras().getStringArrayList("items");
        subitems = intent.getExtras().getStringArrayList("subitems");
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(intent.getExtras().getString("name"));
        ds.setItemFilename(intent.getExtras().getString("name"));
        ds.setSubitemFilename(intent.getExtras().getString("name"));
        ds.setListItems(items);
        ds.setListSubitems(subitems);
        adapter = new CustomAdapter(this, ds.getListItems());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(mMessageClickedHandler);
        lv.setOnItemLongClickListener(mMessageLongClickedHandler);
        addButton =(ImageButton) findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder = new AlertDialog.Builder(List_Activity.this);
                LayoutInflater inflater = List_Activity.this.getLayoutInflater();
                Log.i("INFO", "Creating Dialog...");
                final View view = inflater.inflate(R.layout.additem_dialog_layout, null);
                builder.setView(view)
                        .setPositiveButton(R.string.Create_list_dialog_positive, new DialogInterface.OnClickListener() {
                            /**
                             *When the positive button is pressed add the result of the dialog box to the
                             * list of items and subitems (at the same index to avoid issues with data mismatch.
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                input = (EditText) view.findViewById(R.id.editText);
                                subinput = (EditText) view.findViewById(R.id.editText1);
                                Log.v("List_Activity", "Pre-Press");
                                Log.v("List_Activity", input.getText().toString());
                                if (!input.getText().toString().equals(null)) {
                                    ds.addToListItems(input.getText().toString());
                                    if (subinput.getText().toString().trim().length() == 0) {
                                        ds.addToListSubitems("No additional information specified");
                                    } else {
                                        ds.addToListSubitems(subinput.getText().toString());
                                    }
                                } else {
                                    Toast.makeText(List_Activity.this, R.string.add_list_error,Toast.LENGTH_SHORT).show();
                                }


                                saveFiles();
                                Log.v("List_Activity", "Post-Press");
                            }
                        })
                        .setNegativeButton(R.string.Create_list_dialog_negative, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.v("List_Activity", "Negative pressed");
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
                    }
                });
                dialog.show();
                Log.v("List_Activity", "Add button pressed");
            }

        });
        removeButton =(ImageButton) findViewById(R.id.button_remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(List_Activity.this);
                String[] temporary = ds.getListItems().toArray(new String[0]);
                builder.setTitle(R.string.removeitem_dialog_title)
                        .setItems(temporary, new DialogInterface.OnClickListener() {
                            /**
                             * Upon a click on an item within a list of tasks, removes the appropriate
                             * task and its details before resetting the adapter (to update the change).
                             */
                            public void onClick(DialogInterface dialog, int which) {
                                ds.removeListItems(which);
                                ds.removeListSubItems(which);
                                lv.setAdapter(adapter);
                                saveFiles();
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
                    }
                });
                dialog.show();
                Log.v("List_Activity", "Remove button pressed");
            }
        });
        Log.v("List_Activity", "List_Activity Created");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.v("List_Activity", "List_Activity Resumed");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.v("List_Activity", "List_Activity Paused");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.v("List_Activity", "List_Activity Stopped");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v("List_Activity", "List_Activity Destroyed");
    }

    /**
     * This method overrides the onBackPressed() method in order to allow for a confirmation before
     * exiting the application.
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(List_Activity.this);
        builder.setTitle(R.string.Backpress_dialog_title)
                .setMessage(R.string.Backpress_dialog_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(List_Activity.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
            }
        });
        dialog.show();
    }
    /**
     * An event handler for clicks on the list view, allows the user to see the details of the
     * specific list view item.
     */
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            String item = (String) lv.getAdapter().getItem(position);
            String subitem = ds.getListSubitems().get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(List_Activity.this);
                builder.setTitle(item)
                        .setMessage(subitem)
                        .setPositiveButton(R.string.add_list_dialog_positive, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.v("List_Activity", "Positive pressed");
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
                    }
                });
                dialog.show();
                Log.v("List_Activity", "List item clicked");
        }
    };
    /**
     * An event handler for long clicks on the list view, allows the user to set the priority of the
     * task which results in a colour change of the appropriate row.
     */
    private AdapterView.OnItemLongClickListener mMessageLongClickedHandler = new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView parent, View v, final int position, long id) {
            final String [] temporary = new String[3];
            temporary[0] = "High";
            temporary[1] = "Standard";
            temporary[2] = "Low";
            AlertDialog.Builder builder = new AlertDialog.Builder(List_Activity.this);
            builder.setTitle(R.string.priority_choice_dialog_title)
                    .setItems(temporary, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (temporary[which].equals("High")) {
                                lv.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.palerOrange));
                                Log.v("Check", "Position:" + position + "|");
                            } else if (temporary[which].equals("Standard")) {
                                lv.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.palerBlue));
                                Log.v("Check", "Position:" + position + "|");
                            } else {
                                lv.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.PureWhite));
                                Log.v("Check", "Position:" + position + "|");
                            }
                            saveFiles();
                        }
                    });
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
                }
            });
            dialog.show();
            return true;
        }
    };
    /**
     * As the name suggests, this method is used for saving the files onto the internal storage.
     * Both the item and subitem files are saved in order to maintain synchronicity and avoid
     * difference in indexes between the two files.
     */
    public void saveFiles(){
        try {
            outputStream = openFileOutput(ds.getItemFilename(), getApplicationContext().MODE_PRIVATE);
            ds.setFinalItemString();
            Log.v("List_Activity", "Pre-file save");
            outputStream.write(ds.getFinalItemString().getBytes());
            Log.v("List_Activity", "Post-file save");
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            outputStream = openFileOutput(ds.getSubitemFilename(), getApplicationContext().MODE_PRIVATE);
            ds.setFinalSubitemString();
            Log.v("List_Activity", "Pre-file save");
            outputStream.write(ds.getFinalSubitemString().getBytes());
            Log.v("List_Activity", "Post-file save");
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
