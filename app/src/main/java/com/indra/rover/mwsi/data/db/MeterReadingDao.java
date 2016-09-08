package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;

import com.indra.rover.mwsi.data.pojo.T_Upload;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.InstallMisc;

/**
 * Created by leonardoilagan on 08/09/2016.
 */

public class MeterReadingDao extends ModelDao {

    public MeterReadingDao(Context context){
        super(context);
    }

    @Override
    public void open() {
        database = dbHelper.openDB();
    }

    @Override
    public void close() {
        database.close();
    }

    public long insertTUploadData(T_Upload t_upload,int i){
        long rowInsert = 0;

        try {
            open();
            ContentValues values = new ContentValues();
            values.put("MRU",t_upload.getMru_id());
            values.put("ACCTNUM",t_upload.getAcct_num());
            values.put("PREVUNPAID",t_upload.getPrevunpaid());
            values.put("SEPTIC_CHARGE",t_upload.getSeptic_charge());
            values.put("CHANGESIZE_CHARGE",t_upload.getChangesize_charge());
            values.put("RESTORATION_CHARGE",t_upload.getRestoration_charge());
            values.put("MISC_CHARGE",t_upload.getMisc_charge());
            values.put("INSTALL_SEWER_CHARGE",t_upload.getInstall_sewer_charge());
            values.put("ADVANCES",t_upload.getAdvance());
            InstallMisc installMisc = t_upload.getInstallMisc();
            values.put("INSTALL_SEWER_DUE",installMisc.getInstall_sewer_due());
            values.put("INSTALL_AR_DUE",installMisc.getInstall_ar_due());
            values.put("INSTALL_ADV_DUE",installMisc.getInstall_adv_due());
            values.put("REOPENING_FEE",t_upload.getReopening_fee());
            values.put("METER_CHARGES",t_upload.getMeter_charges());
            values.put("GD_CHARGE",t_upload.getGd_charge());
            values.put("OTHER_CHARGES",t_upload.getOther_charges());

            values.put("ULDOCNO",String.valueOf(i));
            rowInsert = database.insert("T_UPLOAD", null, values);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();

        }

        return rowInsert;
    }


}
