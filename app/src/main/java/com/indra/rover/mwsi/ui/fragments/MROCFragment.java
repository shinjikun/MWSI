package com.indra.rover.mwsi.ui.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.db.RefTableDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterOC;

import com.indra.rover.mwsi.data.pojo.meter_reading.references.ObservationCode;
import com.indra.rover.mwsi.utils.MessageTransport;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MROCFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_ID = "id";

    View mView;
    private String crdocno;
    private RefTableDao refTableDao;
    Spinner spnOC1,spnOC2;
    MeterReadingDao mtrDao;
    MeterOC meterOC;
    List<ObservationCode> arryOC1;
    List<ObservationCode> arryOC2;
    private final int CAM_OC1=31,CAM_OC2=32;
    public MROCFragment() {
    }

    public static MROCFragment newInstance(String mru_id ) {
        MROCFragment fragment = new MROCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, mru_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refTableDao = new RefTableDao(getActivity());
        mtrDao = new MeterReadingDao(getActivity());
        if (getArguments() != null) {
            crdocno = getArguments().getString(ARG_ID);
            meterOC = mtrDao.getMeterOCs(crdocno);
        }

        MainApp.bus.register(this);
    }


    private void launchCamera(int id){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
       startActivityForResult(intent, id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mroc, container, false);

        Button imgCapture = (Button)mView.findViewById(R.id.btnImageCapture);
        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;
                if(btn.getText().equals("Capture Image")){
                    launchCamera(CAM_OC1);
                }
                else {
                    showImageDlg(CAM_OC1);
                }

            }
        });
        imgCapture = (Button) mView.findViewById(R.id.btnImageCapture1);
        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;
                if(btn.getText().equals("Capture Image")){
                    launchCamera(CAM_OC2);
                }
                else {
                    showImageDlg(CAM_OC2);
                }
            }
        });

        spnOC1 = (Spinner)mView.findViewById(R.id.spnOC1);
        spnOC2 = (Spinner)mView.findViewById(R.id.spnOC2);
        mView.findViewById(R.id.btnClrOc1).setOnClickListener(this);
        mView.findViewById(R.id.btnClrOc2).setOnClickListener(this);
        mView.findViewById(R.id.btnCancelOC).setOnClickListener(this);
        mView.findViewById(R.id.btnSaveOC).setOnClickListener(this);
        mView.findViewById(R.id.btnEditOC).setOnClickListener(this);
        initContent();
        setUp();
        return mView;
    }

    private void initContent(){
        List<ObservationCode> arrayList = refTableDao.getOCodes();
        // Spinner Drop down elements
       arryOC1 = new ArrayList<>();
       arryOC2 = new ArrayList<>();
        List<String> arryOC1codes = new ArrayList<>();
        List<String> arryOC2codes = new ArrayList<>();
        arryOC1codes.add(" ");
        arryOC2codes.add(" ");
        for(int i = 0;i<arrayList.size();i++){
            ObservationCode ocCode = arrayList.get(i);
            String value = ocCode.getFf_code() + " - "+ocCode.getFf_desc();
            if(ocCode.getBill_related() == 1){
                arryOC1.add(ocCode);
                arryOC1codes.add(value);
            }
            else {
                arryOC2.add(ocCode);
                arryOC2codes.add(value);
            }

        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arryOC1codes);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnOC1.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arryOC2codes);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnOC2.setAdapter(dataAdapter);
    }



    private void editMode(boolean isEditMode){
        int vis = View.VISIBLE;
        int vis2 = View.INVISIBLE;
        MainApp.isEditMode = isEditMode;
        if(!isEditMode){
            vis = View.INVISIBLE;
            vis2 = View.VISIBLE;
        }
        mView.findViewById(R.id.btnClrOc1).setVisibility(vis);
        mView.findViewById(R.id.btnClrOc2).setVisibility(vis);
        mView.findViewById(R.id.spnOC1).setVisibility(vis);
        mView.findViewById(R.id.spnOC2).setVisibility(vis);
        mView.findViewById(R.id.btnCancelOC).setVisibility(vis);
        mView.findViewById(R.id.btnSaveOC).setVisibility(vis);

        mView.findViewById(R.id.lblOC1).setVisibility(vis2);
        mView.findViewById(R.id.lblOC2).setVisibility(vis2);
        mView.findViewById(R.id.btnEditOC).setVisibility(vis2);
        String lbl = "Capture Image";
        if(!isEditMode){
            lbl= "View Image";
        }
        Button btn =  (Button)mView.findViewById(R.id.btnImageCapture);
        btn.setText(lbl);
        btn =  (Button)mView.findViewById(R.id.btnImageCapture1);
        btn.setText(lbl);
    }

    @Subscribe
    public void getMessage(MessageTransport msgTransport) {
        String action = msgTransport.getAction();
        if(action.equals("navigate")){
            crdocno = msgTransport.getMessage();
            meterOC = mtrDao.getMeterOCs(crdocno);
            setUp();
        }
        else if(action.equals("readstat")){
            String readstat = msgTransport.getMessage();
            meterOC.setReadstat(readstat);
        }
    }

    private void setUp(){
        if(meterOC!=null){
            String oc1 = meterOC.getOc1();
            String oc2 = meterOC.getOc2();
            int index = getPosition(oc1,arryOC1);
            spnOC1.setSelection(index);
            index = getPosition(oc2,arryOC2);
            spnOC2.setSelection(index);
            TextView txt = (TextView) mView.findViewById(R.id.lblOC1);
            txt.setText(spnOC1.getSelectedItem().toString());
            txt = (TextView) mView.findViewById(R.id.lblOC2);
            txt.setText(spnOC2.getSelectedItem().toString());
        }

        editMode(false);
    }

    private void saveDB(){
        int index = spnOC1.getSelectedItemPosition();
        String oc1 ="";
        String oc2 ="";
        if(index !=0){
            oc1 =   arryOC1.get(index-1).getFf_code();
        }
       int  index2 = spnOC2.getSelectedItemPosition();
        if(index2!=0){
            oc2 = arryOC2.get(index2-1).getFf_code();
        }
        TextView txt = (TextView) mView.findViewById(R.id.lblOC1);
        txt.setText(spnOC1.getSelectedItem().toString());
        txt = (TextView) mView.findViewById(R.id.lblOC2);
        txt.setText(spnOC2.getSelectedItem().toString());

        mtrDao.addOC(oc1,oc2,crdocno);
        startReading(oc1,oc2);
        editMode(false);

    }


    private void startReading(String newOC1,String newOC2){
        if(!newOC2.equals(meterOC.getOc2())&& !newOC1.equals(meterOC.getOc1())){
            MainApp.bus.post(new MessageTransport("reading"));
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.btnCancelOC:
                editMode(false);
                break;
            case R.id.btnSaveOC:
                saveDB();
                break;
            case R.id.btnEditOC:
                editMode(true);
                break;
            case R.id.btnClrOc1:
                spnOC1.setSelection(0);
                deleteCapturedImage(CAM_OC1);
                break;
            case R.id.btnClrOc2:
                spnOC2.setSelection(0);
                deleteCapturedImage(CAM_OC2);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CAM_OC1 || requestCode== CAM_OC2){
            if(resultCode == Activity.RESULT_OK){
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                saveCaptureImage(requestCode,bmp);
            }
        }

    }

    private void deleteCapturedImage(int ocType){
        Button btn;
        TextView lbl;
        if(ocType == CAM_OC1){
            btn =  (Button)mView.findViewById(R.id.btnImageCapture);
            lbl =  (TextView)mView.findViewById(R.id.lblOC1);
        }
        else {
            btn =  (Button)mView.findViewById(R.id.btnImageCapture1);
            lbl =  (TextView)mView.findViewById(R.id.lblOC2);
        }
        btn.setText("Capture Image");
        lbl.setText("");
    }

    private void saveCaptureImage(int ocType,Bitmap bitMap){
        File    contentDir=new File(android.os.Environment.getExternalStorageDirectory()
                ,getActivity().getPackageName()+"/downloads/images");
        if(!contentDir.exists())
            contentDir.mkdir();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(crdocno);
        strBuilder.append('-');
        if(ocType == CAM_OC1){
            strBuilder.append("OC1");
        }
        else {
            strBuilder.append("OC2");
        }
        strBuilder.append(".png");
        String fileName = strBuilder.toString();
        File myPath = new File(contentDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitMap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    bitMap, myPath.getPath(), fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void viewCaptureImage(int ocType){

    }

    private int getPosition(String oc_code, List<ObservationCode> arrayList) {
        int size = arrayList.size();
        int pos =0;
        for(int i=0;i<size;i++){
            ObservationCode oc = arrayList.get(i);
            if(oc.getFf_code().equals(oc_code)){
                pos = i+1;
            }
        }
        return pos;
    }

    Dialog dlgImage;
    public void showImageDlg(final int ocType){
        dlgImage = new Dialog(getActivity());
        dlgImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgImage.setContentView(R.layout.dialog_image_view);
        dlgImage.setCancelable(false);

        File file = getImageFile(ocType);
        if(file.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ImageView imgFile = (ImageView)dlgImage.findViewById(R.id.imgOC);
            imgFile.setImageBitmap(myBitmap);

        }
        ImageButton dlgBtnClose = (ImageButton) dlgImage.findViewById(R.id.dlg_btn_close);
        dlgBtnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dlgImage.dismiss();
            }
        });
        Button btn =  (Button) dlgImage.findViewById(R.id.dlg_ok);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            }
        });

        btn =  (Button) dlgImage.findViewById(R.id.btnRemove);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dlgImage.dismiss();
            }
        });



        dlgImage.show();
    }


    private File getImageFile(int ocType){
        File    contentDir=new File(android.os.Environment.getExternalStorageDirectory()
                ,getActivity().getPackageName()+"/downloads/images");
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(crdocno);
        strBuilder.append('-');
        if(ocType == CAM_OC1){
            strBuilder.append("OC1");
        }
        else {
            strBuilder.append("OC2");
        }
        strBuilder.append(".png");
        String fileName = strBuilder.toString();

       return new File(contentDir, fileName);
    }
}
