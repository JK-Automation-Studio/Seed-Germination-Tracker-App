package com.jkstudio.germinationtrackerapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    PieChart chart;
    ArrayList colors = new ArrayList<Integer>();
    ArrayList<PieEntry> entries = new ArrayList<>();
    private PieDataSet dataSet;

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


        chart = findViewById(R.id.cannakanChart);

        // Default entry listings
        entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(1, "Slot 1"));
        entries.add(new PieEntry(1, "Slot 2"));
        entries.add(new PieEntry(1, "Slot 3"));
        entries.add(new PieEntry(1, "Slot 4"));
        entries.add(new PieEntry(1, "Slot 5"));
        entries.add(new PieEntry(1, "Slot 6"));

        colors.add(ContextCompat.getColor(this, R.color.gray));
        colors.add(ContextCompat.getColor(this, R.color.gray));
        colors.add(ContextCompat.getColor(this, R.color.gray));
        colors.add(ContextCompat.getColor(this, R.color.gray));
        colors.add(ContextCompat.getColor(this, R.color.gray));
        colors.add(ContextCompat.getColor(this, R.color.gray));


        entries.get(0).setLabel("Blue Gelato\n #4");
        colors.set(0,ContextCompat.getColor(this, R.color.blue));


        dataSet = new PieDataSet(entries, "Cannakan Slots");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(8f); // adds space between slices
        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);


        chart.setData(data);
        chart.setDrawEntryLabels(true);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawHoleEnabled(false);

        chart.invalidate();

        // Detect slice clicks
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String slotName = ((PieEntry) e).getLabel();
                int slotNum = (int) h.getX();
                Toast.makeText(MainActivity.this, "Tapped " + slotName + "\n"+slotNum, Toast.LENGTH_SHORT).show();

                showSlotDialog(slotNum); // custom dialog to pick color + plant name
            }

            @Override
            public void onNothingSelected() {}
        });


    } // End of oncreate

    private void showSlotDialog(int slotIndex) {
        final String[] colorNames = {"Red", "Green", "Blue", "Yellow"};
        final int[] colorValues = {
                getColor(R.color.red),
                getColor(R.color.green),
                getColor(R.color.blue),
                getColor(R.color.yellow)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Plant and Color for Slot " + (slotIndex + 1));

        final EditText input = new EditText(this);
        input.setHint("Enter plant variety");
        builder.setView(input);

        builder.setItems(colorNames, (dialog, which) -> {
            String plantName = input.getText().toString().trim();
            int selectedColor = colorValues[which];

            
            updateSlot(slotIndex,plantName,selectedColor);
            updateSlotColor(slotIndex, selectedColor);
            updateSlotName(slotIndex,plantName);
            // Optionally store plantName in a list or map for later tracking
        });

        builder.show();
    }

    private void updateSlot(int slotIndex, String plantName, int selectedColor) {
        
        updateSlotName(slotIndex, plantName);
        updateSlotColor(slotIndex, selectedColor);
        writeToFile();
    }

    private void writeToFile() {
    }

    private void updateSlotName(int slotIndex, String plantName) {
        if (! plantName.isBlank()) {
            entries.get(slotIndex).setLabel(plantName);
            //entries.set(slotIndex, plantName);
            //dataSet.setColors(colors);
            chart.invalidate(); // refresh view
        }
    }


    private void updateSlotColor(int slotIndex, int newColor) {
        //colors = new ArrayList<>();

        colors.set(slotIndex, newColor);
        dataSet.setColors(colors);
        chart.invalidate(); // refresh view
    }
}