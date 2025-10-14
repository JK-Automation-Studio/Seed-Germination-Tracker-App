package com.jkstudio.germinationtrackerapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private PieChart chart;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<PieEntry> entries = new ArrayList<>();
    private PieDataSet dataSet;
    private final String FILENAME = "SeedStarter.json";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set toolbar for app
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // set toolbar menu button color
        toolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white)); // Only thing that has worked to change color of overflow icon

        createChartUI();
        readFromFile();

        chart.invalidate(); // redraw chart



        // Detect slice clicks using chart gesture
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                Highlight h = chart.getHighlightByTouchPoint(me.getX(), me.getY());
                if (h != null) {
                    int index = (int) h.getX();
                    // Open your dialog here

                    showSlotDialog(index); // custom dialog to pick color + plant name

                }
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });



    } // End of oncreate

    @Override
    protected void onPause() {
        super.onPause();
        writeToFile();
    }
    private void createChartUI() {
        //get chart spinner
        chart = findViewById(R.id.germSlotsChart);
        // Default entry listings
        //entries = new ArrayList<>();
        //entries.add(new PieEntry(1, "Slot 1"));
        //entries.add(new PieEntry(1, "Slot 2"));
        //entries.add(new PieEntry(1, "Slot 3"));
        //entries.add(new PieEntry(1, "Slot 4"));
        //entries.add(new PieEntry(1, "Slot 5"));
        //entries.add(new PieEntry(1, "Slot 6"));

        // set default grays
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));


        // TODO Replace with readFromFile();
        // temporary default first item
        //entries.get(0).setLabel("Blue Gelato #4"); // set first label to Blue Gelato
        //colors.set(0,ContextCompat.getColor(this, R.color.blue)); // set first color to blue


        //create dataset with colors, slice spacing, etc
        dataSet = new PieDataSet(entries, "Cannakan Slots"); // new dataset with values, label
        dataSet.setColors(colors); // set colors of dataset to colors ArrayList
        dataSet.setSliceSpace(8f); // adds space between slices
        dataSet.setSelectionShift(2f); // adds distance for highlighted selection

        PieData data = new PieData(dataSet);

        chart.setData(data);
        chart.setDrawEntryLabels(true); // Draw labels in chart
        chart.setDrawRoundedSlices(true); // idk

        chart.getDescription().setEnabled(false); // do not show description
        chart.getLegend().setEnabled(false); // do not show legend
        chart.getData().setDrawValues(false); // do not show values, only labels

        chart.setDrawHoleEnabled(false); // remove center circle
        chart.setHighlightPerTapEnabled(false); // true for highlight version, false for onclick version
    }




    // Create toolbar with menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handle item clicks for toolbar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuSettings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
            /* TODO
                Create new SettingsActivity.java class
                and activity_settings.xml

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
             */
        }

        return super.onOptionsItemSelected(item);
    }
    private void showSlotDialog(int slotIndex) {
        final String[] colorNames = {"Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Gray"};
        final int[] colorValues = {
                getColor(R.color.red),
                getColor(R.color.orange),
                getColor(R.color.yellow),
                getColor(R.color.green),
                getColor(R.color.blue),
                getColor(R.color.purple),
                getColor(R.color.gray_dark)
        };

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_slot_edit, null);

        // Build the AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // dialog.setTitle("Edit Slot " + (slotIndex + 1))

        EditText input = dialogView.findViewById(R.id.editPlantName);
        RadioGroup colorGroup = dialogView.findViewById(R.id.colorGroup);

        // Set existing plant name
        input.setText(entries.get(slotIndex).getLabel());
        input.setTextColor(getResources().getColor(R.color.white)); // makes label show color visually
        input.setShadowLayer(2,4,2, R.color.black);

        // Build color options dynamically
        int currentColor = colors.get(slotIndex); // your existing color list
        int checkedIndex = -1;

        // for each color in array, create button with correct name and shadow color, add to radioGroup
        // if current loop through colorValues is the right one, set checkedIndex
        for (int i = 0; i < colorNames.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(colorNames[i]);
            rb.setShadowLayer(2, 4,2, colorValues[i]); // makes label show color visually
            rb.setTextColor(getResources().getColor(R.color.white)); // makes text color white
            colorGroup.addView(rb); // add button to group

            // Check the one that matches current color
            if (colorValues[i] == currentColor) {
                checkedIndex = i;
            }
        }

        if (checkedIndex != -1) {
            // if checkedIndex set, get that index's button in colorGroup and set it to checked
            ((RadioButton) colorGroup.getChildAt(checkedIndex)).setChecked(true);
        }


        // get buttons
        Button btnYes = dialogView.findViewById(R.id.buttonAddDialog);
        Button btnNo = dialogView.findViewById(R.id.buttonCancelDialog);

        btnYes.setOnClickListener(view -> {
            // on Confirm click, get input checkbox
            String plantName = input.getText().toString().trim();

            // if name is empty, set to default slot number
            if (plantName.isBlank()){
                plantName = "Slot " + (slotIndex + 1);
            }
            // Find which color is selected
            int selectedId = colorGroup.getCheckedRadioButtonId();
            int selectedColor = currentColor;
            if (selectedId != -1) {
                int index = colorGroup.indexOfChild(dialogView.findViewById(selectedId));
                selectedColor = colorValues[index];
            }

            // call updateSlot with proper index, name, color
            updateSlot(slotIndex, plantName, selectedColor);
            // close dialog
            dialog.dismiss();
        });


        btnNo.setOnClickListener(view -> {
            // close dialog without any actions
            dialog.dismiss();
        });

        // Set dialog bounding box to invisible
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Set background dim amount
        dialog.getWindow().setDimAmount(0.65f);

        // Show the dialog
        dialog.show();
    }

    private void updateSlot(int slotIndex, String plantName, int selectedColor) {

        updateSlotName(slotIndex, plantName);
        updateSlotColor(slotIndex, selectedColor);
        writeToFile();
        chart.highlightValues(null);

    }

    private void writeToFile() {

        //TODO make file writer

        try {
            // Create a JSON object
            JSONObject root = new JSONObject();
            root.put("rotation", chart.getRotationAngle());

            JSONArray slotsArray = new JSONArray();
            for (int i = 0; i < entries.size(); i++) {
                JSONObject slot = new JSONObject();
                slot.put("plant", entries.get(i).getLabel());
                slot.put("color", colors.get(i));
                slotsArray.put(slot);
                Log.i("i", "Plant written: "+slot.getString("plant")+" "+slot.getInt("color"));

            }
            root.put("slots", slotsArray);

            // Write to internal storage
            FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE);
            fos.write(root.toString().getBytes());
            fos.close();

            Log.d("Save", "Chart state saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readFromFile() {

        //TODO make file reader

        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            JSONObject root = new JSONObject(sb.toString());

            float rotation = (float) root.getDouble("rotation");
            chart.setRotationAngle(rotation);

            JSONArray slots = root.getJSONArray("slots");
            entries.clear();
            colors.clear();
            for (int i = 0; i < slots.length(); i++) {
                JSONObject slot = slots.getJSONObject(i);

                entries.add(new PieEntry(1, slot.getString("plant")));
                colors.add(slot.getInt("color"));
                Log.i("i", "Plant read: "+slot.getString("plant")+" "+slot.getInt("color"));

            }

            //updateChartUI(); // A method to rebuild PieData with updated values

            createChartUI();

            Log.d("Load", "Chart state restored successfully");
        } catch (FileNotFoundException e) {
            // first run, ignore
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void updateSlotName(int slotIndex, String plantName) {
        if (! plantName.isBlank()) {
            entries.get(slotIndex).setLabel(plantName); // set entry's label at index to plantName
            chart.invalidate(); // refresh view
        }
        else {
            entries.get(slotIndex).setLabel("Slot "+(slotIndex+1)); // if empty, default slot num
            chart.invalidate(); // refresh view
        }
    }


    private void updateSlotColor(int slotIndex, int newColor) {
        colors.set(slotIndex, newColor); // set color in colors array
        dataSet.setColors(colors); // set colors for pie chart slices
        chart.invalidate(); // refresh view
    }



}