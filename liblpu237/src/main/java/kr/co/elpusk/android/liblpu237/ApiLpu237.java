package kr.co.elpusk.android.liblpu237;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.hardware.usb.*;

import kr.co.elpusk.android.liblpu237.hidboot.HidBootCallback;
import kr.co.elpusk.android.liblpu237.hidboot.HidBootLoaderInfo;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Callback;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Const;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237DeviceType;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237DoneCallback;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237GetSetCallback;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Interface;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Runner;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Tags;
import kr.co.elpusk.android.liblpu237.rom.Rom;
import kr.co.elpusk.android.liblpu237.rom.RomResult;

public class ApiLpu237 implements ApiInterface {

    private UsbManager m_usbManager = null;
    private Application m_app = null;

    private Object m_lock_device_map = new Object();

    // lpu237 & hidbootloader
    private HashMap<String, UsbDevice> m_map_usbDevice = new HashMap<>();
    private HashMap<String, Lpu237Runner> m_map_lpu237 = new HashMap<>();

    private HashMap<File,Rom> m_map_rom = new HashMap<>();

    private Lpu237.Parameters m_parameter_for_recover = new Lpu237.Parameters();

    private static class Singleton {
        private static final ApiLpu237 instance = new ApiLpu237();
    }

    public static ApiLpu237 getInstance () {
        return Singleton.instance;
    }

    ////////////////////////////////////////////////////////////

    @Override
    public String GetVersion(){
        return "0.9.0.0";
    }
    @Override
    public boolean On(Application app) {
        boolean b_result = false;

        do{
            m_app = app;
            //
            m_usbManager = (UsbManager) m_app.getSystemService(m_app.USB_SERVICE);
            m_map_usbDevice.clear();
            b_result = true;
        }while(false);

        return b_result;
    }

    @Override
    public void Off(){
        m_map_usbDevice.clear();
        m_map_lpu237.clear();
        m_map_rom.clear();

        m_usbManager = null;
        m_app = null;
    }

    @Override
    public ArrayList<UsbDevHandle> GetList(){

        ArrayList<UsbDevHandle> list = new ArrayList<>();

        synchronized (m_lock_device_map){
            do{
                m_map_usbDevice.clear();

                HashMap<String, UsbDevice> usbDevices  = m_usbManager.getDeviceList();
                if(usbDevices == null){
                    continue;
                }
                if(usbDevices.isEmpty()){
                    continue;
                }

                for( Map.Entry<String, UsbDevice> entry : usbDevices.entrySet() ){
                    UsbDevice device = entry.getValue();
                    if( (device.getVendorId() == Lpu237Const.USB_VID) && (device.getProductId() == Lpu237Const.USB_PID )) {
                        m_map_usbDevice.put(entry.getKey(),entry.getValue());

                        UsbDevHandle handle = new UsbDevHandle(entry.getKey(), entry.getValue());
                        list.add(handle);
                    }
                    else if( (device.getVendorId() == HidBootLoaderInfo.USB_VID) && (device.getProductId() == HidBootLoaderInfo.USB_PID )) {
                        m_map_usbDevice.put(entry.getKey(),entry.getValue());
                    }
                }//end for

            }while (false);
        }
        
        if(list == null){
            list = new ArrayList<>();
        }

        return list;
    }

    @Override
    public UsbDevHandle Open(String sPath){
        UsbDevHandle h = null;

        synchronized (m_lock_device_map){
            do{
                if(!m_map_usbDevice.containsKey(sPath)){
                    continue;
                }

                UsbDevice dev = m_map_usbDevice.get(sPath);
                if(dev == null){
                    continue;
                }

                if( !m_usbManager.hasPermission(dev)) {
                    continue;
                }

                Lpu237Runner msr = null;
                if(!m_map_lpu237.containsKey(sPath)){
                    msr = new Lpu237Runner(m_usbManager,dev);
                    m_map_lpu237.put(sPath,msr);
                }
                else{
                    msr = m_map_lpu237.get(sPath);
                }

                if(msr == null){
                    continue;
                }
                if(msr.is_open()){
                    continue;//already open
                }
                if(!msr.HidOpen()){
                    continue;
                }

                h= new UsbDevHandle(sPath,dev);

            }while (false);
        }

        if(h==null){
            h = new UsbDevHandle(sPath,null);
        }
        return h;
    }

    @Override
    public boolean SetupForRead(UsbDevHandle handle,boolean bChangeInterfaceToHid)
    {
        boolean b_result = false;
        boolean b_need_leave_config = false;
        Lpu237 msr = null;
        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if( !msr.df_enter_config() ){
                    continue;
                }
                b_need_leave_config = true;
                if( !msr.df_get_name() ){
                    continue;
                }
                if( !msr.df_get_uid() ){
                    continue;
                }
                if( !msr.df_get_version_system() ){
                    continue;
                }
                if( !msr.df_get_version_structure() ){
                    continue;
                }
                if( !msr.df_get_type() ){
                    continue;
                }
                if( !msr.df_get_interface() ){
                    continue;
                }
                //
                if(bChangeInterfaceToHid){
                    if( msr.get_interface() != Lpu237Interface.usbVendorHid ){
                        msr.set_interface(Lpu237Interface.usbVendorHid);
                        if(!msr.df_set_interface()){
                            continue;
                        }
                        if(!msr.df_apply()){
                            continue;
                        }
                    }
                }
                //
                b_result = true;
            }

        }while(false);

        if( msr != null && b_need_leave_config ){
            msr.df_leave_config();
        }
        return b_result;
    }
    @Override
    public boolean Close(UsbDevHandle handle){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237 msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                b_result = msr.HidClose();
            }

        }while(false);
        return b_result;
    }

    @Override
    public String GetId(UsbDevHandle handle){
        String sId = "";
        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237 msr = m_map_lpu237.get(handle.get_path());

                sId = msr.getUid();
                if(!sId.isEmpty()){
                    continue;//success get ID. get cached data.
                }
                if(msr.is_open()){
                    continue;//error
                }
                if( !msr.HidOpen() ){
                    continue;
                }
                if( !msr.df_get_uid() ){
                    msr.HidClose();
                    continue;//error
                }
                sId = msr.getUid();
                msr.HidClose();
            }

        }while(false);
        return sId;
    }


    /**
     * Before executing, need open.
     * @param handle
     * @param bEnable true-enable
     * @return
     */
    @Override
    public boolean EnableMsr(UsbDevHandle handle,boolean bEnable){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(bEnable) {
                    if (!msr.df_enter_opos()) {
                        continue;
                    }
                }
                else{
                    if (!msr.df_leave_opos()) {
                        continue;
                    }
                }
                msr.set_read_msr(bEnable);
                b_result = true;
            }

        }while(false);
        return b_result;
    }


    @Override
    public boolean EnableiButton(UsbDevHandle handle,boolean bEnable){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                msr.set_read_ibutton(bEnable);
                b_result = true;
            }

        }while(false);
        return b_result;
    }

    /**
     * Before executing, need open.
     * @param handle
     * @return
     */
    public boolean CancelWait(UsbDevHandle handle){
        boolean b_result = false;
        Lpu237Runner msr = null;
        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }

                msr = m_map_lpu237.get(handle.get_path());
                if(msr == null){
                    continue;
                }
                if(!msr.is_open()){
                    continue;
                }

                b_result = msr.Cancel();
            }

            if(b_result){
                if(msr != null) {
                    while (msr.WhatIsDoing() != Lpu237Runner.Do.NOTHING) ;
                }
            }

        }while(false);
        return b_result;
    }

    @Override
    public boolean WaitMsrOriButtonWithCallback(UsbDevHandle handle, Lpu237Callback cb){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }

                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                b_result = msr.Reading(cb);
            }

        }while(false);
        return b_result;
    }

    @Override
    public boolean ToolsMsrStartGetSetting(UsbDevHandle handle, Lpu237GetSetCallback cb){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(!cb.is_getting_type()) {
                    continue;
                }
                b_result = msr.Doing(cb);
            }

        }while(false);
        return b_result;
    }

    @Override
    public boolean ToolsMsrStartSetSetting(UsbDevHandle handle, Lpu237GetSetCallback cb){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(cb.is_getting_type()) {
                    continue;
                }
                b_result = msr.Doing(cb);
            }

        }while(false);
        return b_result;
    }

    @Override
    public boolean ToolsMsrStartApplySetting(UsbDevHandle handle, Lpu237DoneCallback cb){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                b_result = msr.Doing(cb);
            }

        }while(false);
        return b_result;
    }

    @Override
    public boolean ToolsMsrIsSupportMsr(UsbDevHandle handle){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if( msr.get_device_type() != Lpu237DeviceType.IbuttonOlny ){
                    b_result = true;
                }
            }

        }while(false);
        return b_result;
    }

    @Override
    public boolean ToolsMsrIsSupportIbutton(UsbDevHandle handle){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if( msr.get_device_type() != Lpu237DeviceType.Compact ){
                    b_result = true;
                }
            }
        }while(false);
        return b_result;
    }

    @Override
    public int[] ToolsMsrGetActiveAndValiedInterface(UsbDevHandle handle){
        int[] infs = new int[0];

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                int[] a = msr.get_available_all_interfaces();

                infs = new int[a.length+1];
                infs[0] = msr.get_interface();
                System.arraycopy(a,0,infs,1,a.length);
            }
        }while(false);
        return infs;
    }

    @Override
    public boolean ToolsMsrSetInterface(UsbDevHandle handle, int Inf){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                msr.set_interface(Inf);
                b_result = true;
            }
        }while(false);
        return b_result;
    }

    @Override
    public boolean ToolsMsrSetInterfaceToDeviceAndApply(UsbDevHandle handle, int Inf, Lpu237DoneCallback cb){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                msr.set_interface(Inf);
                b_result = msr.Doing(cb);
            }
        }while(false);
        return b_result;
    }

    @Override
    public int ToolsMsrGetBuzzer(UsbDevHandle handle){
        int nValue = -1;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                String s = msr.getBuzzerFrequency();
                if(s.compareTo("ON")==0){
                    nValue = 1;
                }
                else{
                    nValue = 0;
                }
            }
        }while(false);
        return nValue;
    }

    @Override
    public boolean ToolsMsrSetBuzzer(UsbDevHandle handle, boolean bOn){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                if(bOn) {
                    if( msr.get_buzzer_frequency() != 2600) {
                        msr.set_buzzer_frequency(Lpu237.Parameters.DEFAULT_FREQUENCY_BUZZER);
                    }
                }
                else{
                    msr.set_buzzer_frequency(10);
                }
                b_result = true;
            }
        }while(false);
        return b_result;

    }

    @Override
    public int ToolsMsrGetLanguage(UsbDevHandle handle){
        int nValue = -1;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                nValue = msr.get_language_index();
            }
        }while(false);
        return nValue;
    }

    @Override
    public int ToolsMsrGetTrackStatus(UsbDevHandle handle,int n_track_index){
        int nValue = -1;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                if( msr.get_enable_track(n_track_index) ){
                    nValue = 1;
                }
                else{
                    nValue = 0;
                }
            }
        }while(false);
        return nValue;

    }

    @Override
    public boolean ToolsMsrSetTrackStatus(UsbDevHandle handle,int n_track_index,boolean bEnable){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                msr.set_enable_track(n_track_index,bEnable);
                b_result = true;
            }
        }while(false);
        return b_result;
    }

    @Override
    public Lpu237Tags ToolsMsrGetPrivateTag(UsbDevHandle handle, int n_track_index, boolean b_prefix){
        Lpu237Tags tag = null;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(b_prefix) {
                    tag = msr.get_private_prefix(n_track_index);
                }
                else{
                   tag = msr.get_private_postfix(n_track_index);
                }
            }
        }while(false);
        return tag;
    }

    @Override
    public boolean  ToolsMsrSetPrivateTag(UsbDevHandle handle,int n_track_index,boolean b_prefix, Lpu237Tags pTag){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(b_prefix) {
                    msr.set_private_prefix(n_track_index, pTag);
                }
                else{
                    msr.set_private_postfix(n_track_index, pTag);
                }
                b_result = true;
            }
        }while(false);
        return b_result;
    }

    @Override
    public int ToolsMsrGetiButtonMode(UsbDevHandle handle){
        int nValue = -1;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                nValue = msr.get_ibutton_type();
            }
        }while(false);
        return nValue;
    }

    @Override
    public boolean ToolsMsrSetiButtonMode(UsbDevHandle handle,int n_mode){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                msr.set_ibutton_type(n_mode);
                b_result = true;
            }
        }while(false);
        return b_result;

    }

    @Override
    public Lpu237Tags ToolsMsrGetiButtonTag(UsbDevHandle handle,boolean b_prefix){
        Lpu237Tags tag = null;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(b_prefix) {
                    tag = msr.get_ibutton_tag_prefix();
                }
                else{
                    tag = msr.get_ibutton_tag_postfix();
                }
            }
        }while(false);
        return tag;

    }

    @Override
    public boolean ToolsMsrSetiButtonTag(UsbDevHandle handle,boolean b_prefix,Lpu237Tags pTag){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(b_prefix) {
                    msr.set_ibutton_tag_prefix( pTag);
                }
                else{
                    msr.set_ibutton_tag_postfix( pTag);
                }
                b_result = true;
            }
        }while(false);
        return b_result;

    }

    @Override
    public Lpu237Tags ToolsMsrGetiButtonRemoveIndicationTag(UsbDevHandle handle){
        Lpu237Tags tag = null;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                tag = msr.get_ibutton_remove();
            }
        }while(false);
        return tag;
    }

    @Override
    public boolean ToolsMsrSetiButtonRemoveIndicationTag(UsbDevHandle handle,Lpu237Tags pTag){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }

                msr.set_ibutton_remove(pTag);
                b_result = true;
            }
        }while(false);
        return b_result;

    }

    @Override
    public Lpu237Tags ToolsMsrGetiButtonRTag(UsbDevHandle handle,boolean b_prefix){
        Lpu237Tags tag = null;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(b_prefix) {
                    tag = msr.get_ibutton_remove_tag_prefix();
                }
                else{
                    tag = msr.get_ibutton_remove_tag_postfix();
                }
            }
        }while(false);
        return tag;

    }

    @Override
    public boolean ToolsMsrSetiButtonRTag(UsbDevHandle handle,boolean b_prefix,Lpu237Tags pTag){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if(b_prefix) {
                    msr.set_ibutton_remove_tag_prefix( pTag);
                }
                else{
                    msr.set_ibutton_remove_tag_postfix( pTag);
                }
                b_result = true;
            }
        }while(false);
        return b_result;

    }

    @Override
    public int ToolsMsrGetiButtonStartZeroBaseOffsetOfRange(UsbDevHandle handle){
        int nValue = -1;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                nValue = msr.get_ibutton_start();
            }
        }while(false);
        return nValue;

    }

    @Override
    public int ToolsMsrGetiButtonEndZeroBaseOffsetOfRange(UsbDevHandle handle){
        int nValue = -1;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                nValue = msr.get_ibutton_end();
            }
        }while(false);
        return nValue;

    }

    @Override
    public boolean ToolsMsrSetiButtonZeroBaseRange(UsbDevHandle handle,int n_start,int n_end){
        boolean b_result = false;

        do{
            synchronized (m_lock_device_map){
                if(!is_valied(handle)){
                    continue;
                }
                Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
                if(!msr.is_open()){
                    continue;
                }
                if( n_start<0 || n_end <0){
                    continue;
                }
                if( n_start> n_end){
                    continue;
                }

                msr.set_ibutton_start(n_start);
                msr.set_ibutton_end(n_end);
                b_result = true;
            }
        }while(false);
        return b_result;

    }


    ///////////////////////////////////
    // From here private member
    private ApiLpu237(){
    }

    /**
     * check whether or not the given handle is valied.
     * this method must be called in m_lock_device_map
     * @param handle
     * @return true - valied
     */
    private boolean is_valied(UsbDevHandle handle){
        boolean b_result = false;

        do{
            if(handle == null){
                continue;
            }
            if(handle.is_empty()){
                continue;
            }

            if(!m_map_lpu237.containsKey(handle.get_path())){
                continue;
            }
            Lpu237Runner msr = m_map_lpu237.get(handle.get_path());
            if( msr == null){
                continue;
            }
            b_result = true;
        }while(false);
        return b_result;
    }

    /**
     * check whether or not the Rom object of given File is valied.
     * this method must be called in m_lock_device_map
     * @param fileRom
     * @return true - valied
     */
    private boolean is_valied(File fileRom){
        boolean b_result = false;
        Rom rom = null;

        do{
            synchronized (m_lock_device_map) {
                if (!m_map_rom.containsKey(fileRom)) {
                    continue;
                } else {
                    rom = m_map_rom.get(fileRom);
                }

                if (!rom.is_loaded_rom_header()) {
                    continue;
                }
                b_result = true;
            }
        }while(false);

        return b_result;
    }
}//the end of class

