package justin.controllerbluetooth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Button;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import ControllerBluetooth.R;

import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private static final String DEVICE_ADDRESS="98:D3:61:FD:55:52";
    private static final UUID PORT_UUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    Button signal_test;
    Button signal_test2;
    Button signal_test3;
    Button signal_test4;
    ImageButton stop;
    ImageButton extend;
    ImageButton retract;
    TextView output_view;
    SeekBar leftBar;
    SeekBar rightBar;
    int command;
    int rightProgress=0;
    int leftProgress=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        signal_test = (Button) findViewById(R.id.signal);
        signal_test2 = (Button) findViewById(R.id.signal2);
        signal_test3 = (Button) findViewById(R.id.signal3);
        signal_test4 = (Button) findViewById(R.id.signal4);
        output_view = (TextView) findViewById(R.id.output);
        output_view.setText("Welcome to Ferret");
        stop = (ImageButton) findViewById(R.id.imageButton);
        extend = (ImageButton) findViewById(R.id.imageButton2);
        retract = (ImageButton) findViewById(R.id.imageButton3);
        leftBar = (SeekBar) findViewById(R.id.seekBar);
        rightBar = (SeekBar) findViewById(R.id.seekBar2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(BTinit()) {
                    BTconnect();
                }
            }
        });

        signal_test.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = 20;
                    output_view.setText("Right\nPacket: 20");
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = 0;
                    output_view.setText("Halt\nPacket: 0");
                }
                sendCommand(command);
                return false;
            }

                });
        signal_test2.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = 10;
                    output_view.setText("Left\nPacket: 10");
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = 0;
                    output_view.setText("Halt\nPacket: 0");
                }
                sendCommand(command);

                return false;
            }

        });
        signal_test3.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = 30;
                    output_view.setText("Backward\nPacket: 30");
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = 0;
                    output_view.setText("Halt\nPacket: 0");
                }
                sendCommand(command);
                return false;
            }

        });
        signal_test4.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = 40;
                    output_view.setText("Forward\nPacket: 40");
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = 0;
                    output_view.setText("Halt\nPacket: 0");
                }
                sendCommand(command);

                return false;
            }

        });
        stop.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = 50;
                    output_view.setText("Actuator Stop\nPacket: 50");
                }
                sendCommand(command);

                return false;
            }

        });
        extend.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = 51;
                    output_view.setText("Actuator Out\nPacket: 51");
                }
                sendCommand(command);
                return false;
            }

        });
        retract.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = 52;
                    output_view.setText("Actuator In\nPacket: 52");
                }
                sendCommand(command);
                return false;
            }

        });
        leftBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                leftProgress=progress;
                //output_view.setText(Integer.toString(progress));
                command=100+(rightProgress*10)+leftProgress;
                output_view.setText("Variable Speed\nPacket:"+Integer.toString(command));
                sendCommand(command);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rightProgress=progress;
                //output_view.setText(Integer.toString(progress));
                command=100+(rightProgress*10)+leftProgress;
                output_view.setText("Variable Speed\nPacket:"+Integer.toString(command));
                sendCommand(command);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

        public void sendCommand(int command){
            try
            {
                outputStream.write(command);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        public boolean BTinit(){
            boolean found = false;

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(bluetoothAdapter == null) //Checks if the device supports bluetooth
            {
                Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
            }

            if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
            {
                Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableAdapter,0);

                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

            if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
            {
                Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
            }
            else
            {
                for(BluetoothDevice iterator : bondedDevices)
                {
                    if(iterator.getAddress().equals(DEVICE_ADDRESS))
                    {
                        device = iterator;
                        found = true;
                        break;
                    }
                }
            }
            //output_view.setText("Found Bluetooth");
            return found;
        }
        public boolean BTconnect(){
            boolean connected = true;

            try
            {
                UUID uuid = device.getUuids()[0].getUuid();
                output_view.setText("Connected over Port:\n"+uuid.toString());
                //BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                socket = device.createRfcommSocketToServiceRecord(uuid); //Creates a socket to handle the outgoing connection

                socket.connect();

                Toast.makeText(getApplicationContext(),
                        "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
            }
            catch(IOException e)
            {
                try{
                    socket.close();

                }
                catch(IOException e2){}
                e.printStackTrace();
                connected = false;
            }

            if(connected)
            {
                try
                {
                    outputStream = socket.getOutputStream(); //gets the output stream of the socket
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }

            return connected;
        }

    }


