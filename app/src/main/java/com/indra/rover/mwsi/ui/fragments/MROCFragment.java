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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.db.RefTableDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterOC;

import com.indra.rover.mwsi.data.pojo.meter_reading.references.ObservationCode;
import com.indra.rover.mwsi.ui.widgets.CustomSpinView;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.MessageTransport;
import com.indra.rover.mwsi.utils.PreferenceKeys;
import com.indra.rover.mwsi.utils.Utils;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MROCFragment extends Fragment implements View.OnClickListener,
        DialogUtils.DialogListener, Constants {
    private static final String ARG_ID = "id";

    View mView;
    String crdocno;
    RefTableDao refTableDao;
    MeterReadingDao mtrDao;
    MeterOC meterOC;
    List<ObservationCode> arryOC1;
    List<ObservationCode> arryOC2;
    final int CAM_OC1=31,CAM_OC2=32;
    DialogUtils dlgUtils;
    final int DLG_REVERT_RDG=101;
    CustomSpinView oc1_opt;
    CustomSpinView oc2_opt;
    Button btnCapOC1,btnCapOC2;
    EditText txtRemarks;

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
        dlgUtils = new DialogUtils(getActivity());
        dlgUtils.setListener(this);

    }




    private void launchCamera(int id){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
       startActivityForResult(intent, id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mroc, container, false);
        oc1_opt =  (CustomSpinView)mView.findViewById(R.id.oc1_opt);
        oc2_opt =  (CustomSpinView)mView.findViewById(R.id.oc2_opt);
        txtRemarks = (EditText)mView.findViewById(R.id.txtMRCDesc);
        btnCapOC1 = (Button)mView.findViewById(R.id.btnCapOc1);
        btnCapOC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File  file = getImageFile(CAM_OC1);
                if(file.exists()){
                    showImageDlg(CAM_OC1);
                }
                else {
                  launchCamera(CAM_OC1);
                }
            }
        });
        btnCapOC2 = (Button) mView.findViewById(R.id.btnCapOc2);
        btnCapOC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             File  file = getImageFile(CAM_OC2);
                if(file.exists()){
                    showImageDlg(CAM_OC2);
                }
                else {
                    launchCamera(CAM_OC2);
                }
            }
        });

        oc1_opt.getBtnClr().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oc1_opt.setSelection(0);
                deleteCapturedImage(CAM_OC1);
            }
        });

        oc2_opt.getBtnClr().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oc2_opt.setSelection(0);
                deleteCapturedImage(CAM_OC2);
            }
        });

        oc1_opt.getSpn().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position ==0){
                    btnCapOC1.setVisibility(View.GONE);
                }
                else {
                    btnCapOC1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        oc2_opt.getSpn().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position ==0){
                    btnCapOC2.setVisibility(View.GONE);
                }
                else {
                    btnCapOC2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mView.findViewById(R.id.btnCancel).setOnClickListener(this);
        mView.findViewById(R.id.btnSave).setOnClickListener(this);
        mView.findViewById(R.id.btnEdit).setOnClickListener(this);
        MainApp.bus.register(this);
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, arryOC1codes);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        oc1_opt.getSpn().setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, arryOC2codes);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        oc2_opt.getSpn().setAdapter(dataAdapter);
    }



    private void editMode(boolean isEditMode){
        int vis = View.VISIBLE;
        int vis2 = View.INVISIBLE;
        MainApp.isEditMode = isEditMode;
        if(!isEditMode){
            vis = View.INVISIBLE;
            vis2 = View.VISIBLE;
        }
        mView.findViewById(R.id.btnCancel).setVisibility(vis);
        mView.findViewById(R.id.btnSave).setVisibility(vis);

        mView.findViewById(R.id.btnEdit).setVisibility(vis2);
        String lbl = "Capture Image";
        if(!isEditMode){
            lbl= "View Image";
        }
        Button btn =  (Button)mView.findViewById(R.id.btnCapOc1);
        btn.setText(lbl);
        btn =  (Button)mView.findViewById(R.id.btnCapOc2);
        btn.setText(lbl);
        oc1_opt.editMode(isEditMode);
        oc2_opt.editMode(isEditMode);
        File file =   getImageFile(CAM_OC1);
        if(file.exists()){
            mView.findViewById(R.id.btnCapOc1).setVisibility(View.VISIBLE);
        }
        else {
            mView.findViewById(R.id.btnCapOc1).setVisibility(View.GONE);
        }
        file =   getImageFile(CAM_OC2);
        if(file.exists()){
            mView.findViewById(R.id.btnCapOc2).setVisibility(View.VISIBLE);
        }
        else {
            mView.findViewById(R.id.btnCapOc2).setVisibility(View.GONE);
        }
        txtRemarks.setEnabled(isEditMode);
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
        } else if(action.equals("remarks")){
            meterOC = mtrDao.getMeterOCs(crdocno);
            setUp();
        }
    }

    private void setUp(){
        if(meterOC!=null){
            String oc1 = meterOC.getOc1();
            String oc2 = meterOC.getOc2();
            int index = getPosition(oc1,arryOC1);
            if(index== 0){
                btnCapOC1.setVisibility(View.GONE);
            }
            else {
                btnCapOC1.setVisibility(View.VISIBLE);
            }
            oc1_opt.setSelection(index);
            index = getPosition(oc2,arryOC2);
            if(index== 0){
                btnCapOC2.setVisibility(View.GONE);
            }
            else {
                btnCapOC2.setVisibility(View.VISIBLE);
            }
            oc2_opt.setSelection(index);
            oc1_opt.setValues(oc1_opt.getSpn().getSelectedItem().toString());
            oc2_opt.setValues(oc2_opt.getSpn().getSelectedItem().toString());
            File file =   getImageFile(CAM_OC1);
            if(file.exists()){
                btnCapOC1.setText("View Image");
            }
            else {
                btnCapOC1.setText("Capture Image");
            }
            file =   getImageFile(CAM_OC2);
            if(file.exists()){
                btnCapOC2.setText("View Image");
            }
            else {
                btnCapOC2.setText("Capture Image");
            }
            String remarks =  meterOC.getRemarks();

                txtRemarks.setText(remarks);

        }

        editMode(false);
    }

    private void saveDB(){

        String remarks = String.valueOf(txtRemarks.getText());
        if(!Utils.isNotEmpty(remarks)){
            dlgUtils.showOKDialog("You are required to enter an OC remarks");
            return;
        }


        int index = oc1_opt.getSpn().getSelectedItemPosition();
        String oc1 ="";
        String oc2 ="";
        if(index !=0){
            oc1 =   arryOC1.get(index-1).getFf_code();
        }
       int  index2 = oc2_opt.getSpn().getSelectedItemPosition();
        if(index2!=0){
            oc2 = arryOC2.get(index2-1).getFf_code();
        }
        oc1_opt.setValues(oc1_opt.getSpn().getSelectedItem().toString());
        oc2_opt.setValues(oc2_opt.getSpn().getSelectedItem().toString());

        boolean  isOk =true;
        if(Utils.isNotEmpty(oc1)){
            File  file = getImageFile(CAM_OC1);
            if(!file.exists()){
                isOk = false;
            }
        }

        if(Utils.isNotEmpty(oc2)){
            File  file = getImageFile(CAM_OC2);
            if(!file.exists()){
                isOk = false;
            }
        }

        if(!isOk){
            dlgUtils.showOKDialog("You are required to capture a Image for OC");
            return;
        }


        mtrDao.addOC(oc1,oc2,crdocno, remarks);
        startReading(oc1,oc2);
        editMode(false);

    }


    private void startReading(String newOC1,String newOC2){
        if(!newOC2.equals(meterOC.getOc2())&& !newOC1.equals(meterOC.getOc1())){
            try {
                MainApp.bus.post(new MessageTransport("reading"));
            }catch (Exception e){

            }

        }


    }

    private void updateOC(){
        String bill_str = meterOC.getBill_scheme();
        String readStat = meterOC.getReadstat();
        if(readStat.equals("P")||readStat.equals("Q")){
            noOCEntry(1);
            return;
        }


        if(Utils.isNotEmpty(bill_str)) {
            int bill_scheme = Integer.parseInt(bill_str);
            String accoutNumb = meterOC.getAccount_num();
            switch (bill_scheme){
                case REG_SCHEME:
                    editMode(true);
                    break;
                case CS_MOTHER:
                case MB_MOTHER:
                    int countUnRead = mtrDao.countChildUnRead(accoutNumb);
                    if(countUnRead!=0){
                        noOCEntry(2);
                        return ;
                    }
                    else {
                        int countChildPrinted ;
                        if(bill_scheme == CS_MOTHER){
                             countChildPrinted = mtrDao.countMBChildBilled(accoutNumb);
                        }
                        else {
                            countChildPrinted = mtrDao.countCSChildBilled(accoutNumb);
                        }


                        if(countChildPrinted!=0){
                            noOCEntry(2);
                            return;
                        }
                        editMode(true);
                    }
                    break;
                case CS_CHILD:
                case MB_CHILD:
                    String parent_code = meterOC.getChilds_parent();
                    int countChildPrinted ;
                    if(bill_scheme == MB_CHILD){
                        countChildPrinted = mtrDao.countMBChildBilled(accoutNumb);
                    }
                    else {
                        countChildPrinted = mtrDao.countCSChildBilled(accoutNumb);
                    }


                    if(countChildPrinted!=0){
                        noOCEntry(4);
                        return;
                    }
                    MeterConsumption parentMeter =  mtrDao.getParentMeter(parent_code);
                    if(parentMeter!=null){
                        String parentStat = parentMeter.getReadstat();
                        if(parentStat.equals("P")||parentStat.equals("Q")){
                            noOCEntry(5);
                            return;
                        }
                        editMode(true);
                    }

                    break;
            }
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.btnCancel:
                editMode(false);
                break;
            case R.id.btnSave:
                saveDB();
                break;
            case R.id.btnEdit:
                updateOC();
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
            btn =  (Button)mView.findViewById(R.id.btnCapOc1);
            lbl =  oc1_opt.getText();
        }
        else {
            btn =  (Button)mView.findViewById(R.id.btnCapOc2);
            lbl =  oc2_opt.getText();
        }
        btn.setText("Capture Image");
        lbl.setText("");
        File file = getImageFile(ocType);
        if(file.exists()){
            file.delete();
        }
    }

    private void saveCaptureImage(int ocType,Bitmap bitMap){
        File    contentDir=new File(android.os.Environment.getExternalStorageDirectory()
                ,"com.indra.rover.mwsi/uploads/");
        if(!contentDir.exists())
            contentDir.mkdir();

        String str =  PreferenceKeys.getInstance(getActivity()).getData(Constants.contentFolder,"");
        if(Utils.isNotEmpty(str)){
            File dataCont =  new File(contentDir,str);
            if(!dataCont.exists()){
                dataCont.mkdir();
            }

            //this will contain the signature of customer who received the receipt of meter reading
            contentDir= new File(dataCont,"images");
            if(!contentDir.exists()){
                contentDir.mkdir();
            }
        }



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


     void showImageDlg(final int ocType){
        dlgImage = new Dialog(getActivity());
        dlgImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgImage.setContentView(R.layout.dialog_image_view2);
        dlgImage.setCancelable(false);
        File file = getImageFile(ocType);
        if(file.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ImageView imgFile = (ImageView)dlgImage.findViewById(R.id.imgOC);
            imgFile.setImageBitmap(myBitmap);
        }
        if(MainApp.isEditMode){
            dlgImage.findViewById(R.id.ctrs).setVisibility(View.VISIBLE);
        }
        else {
            dlgImage.findViewById(R.id.ctrs).setVisibility(View.GONE);
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
                dlgImage.dismiss();
            }
        });

        btn =  (Button) dlgImage.findViewById(R.id.btnRemove);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                File file = getImageFile(ocType);
                if(file.exists())
                    file.delete();
                if(ocType == CAM_OC1){
                    btnCapOC1.setText("Capture Image");
                }
                else {
                    btnCapOC2.setText("Capture Image");
                }
               // btnSign.setText("Ask For Signature");
                dlgImage.dismiss();
            }
        });

        btn =  (Button) dlgImage.findViewById(R.id.btnRetake);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dlgImage.dismiss();
                launchCamera(ocType);
            }
        });
        dlgImage.show();
    }


     File getImageFile(int ocType){
         File    contentDir=new File(android.os.Environment.getExternalStorageDirectory()
                 ,"com.indra.rover.mwsi/uploads/");
         if(!contentDir.exists())
             contentDir.mkdir();

         String str =  PreferenceKeys.getInstance(getActivity()).getData(Constants.contentFolder,"");
         if(Utils.isNotEmpty(str)){
             File dataCont =  new File(contentDir,str);
             if(!dataCont.exists()){
                 dataCont.mkdir();
             }

             //this will contain the signature of customer who received the receipt of meter reading
             contentDir= new File(dataCont,"images");
             if(!contentDir.exists()){
                 contentDir.mkdir();
             }
         }



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


    /**
     *  message dialog shown when entering OC
     *  0  - for already billed
     *  1 -  edited account
     *  2 -  for check meteradb
     *  3 -  for mb parent meter
     *  4 -  for mb child meter
     * @param type type
     */
    void noOCEntry(int type){
        StringBuilder  strBuilder = new StringBuilder();
        switch(type){

            case 1:
                strBuilder.append("Cannot Enter an");
                strBuilder.append('\n');
                strBuilder.append("OC for an already ");
                strBuilder.append('\n');
                strBuilder.append("Billed Account!");
                break;
            case 2:
                strBuilder.append("Cannot Enter OC for");
                strBuilder.append('\n');
                strBuilder.append("a Parent Meter");
                strBuilder.append('\n');
                strBuilder.append("with unread or");
                strBuilder.append('\n');
                strBuilder.append("billed Child Meters!");
                break;

            case 4:
                strBuilder.append("Cannot Enter OC for");
                strBuilder.append('\n');
                strBuilder.append("a Child Meter");
                strBuilder.append('\n');
                strBuilder.append("with already billed");
                strBuilder.append('\n');
                strBuilder.append("siblings Meters!");
                break;
            case 5:
                strBuilder.append("Cannot Enter OC for");
                strBuilder.append('\n');
                strBuilder.append("a Child Meter");
                strBuilder.append('\n');
                strBuilder.append("with already billed");
                strBuilder.append('\n');
                strBuilder.append("Parent Meters!");
                break;
        }
        dlgUtils.showOKDialog("NO OC ENTRY",strBuilder.toString());
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {
            switch (dialog_id){
                case DLG_REVERT_RDG:
                    break;

            }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }

     void revert_reading(){
        StringBuilder strBldr = new StringBuilder();
        strBldr.append("Deleting a billable");
        strBldr.append('\n');
        strBldr.append("OC with an actual reading");
        strBldr.append('\n');
        strBldr.append("Will Erase all Related OC");
        strBldr.append('\n');
        strBldr.append("and Reading Entries.");
        strBldr.append('\n');
        strBldr.append("Re-entry is required Procced?");
        String message = strBldr.toString();
        dlgUtils.showYesNoDialog(DLG_REVERT_RDG,"DELETE OC1 WITH RDG",message,new Bundle());
    }

}
