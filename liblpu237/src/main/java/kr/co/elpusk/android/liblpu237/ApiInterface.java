package kr.co.elpusk.android.liblpu237;

import android.app.Application;

import java.util.ArrayList;

import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Callback;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237DoneCallback;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237GetSetCallback;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Tags;

public interface ApiInterface {
    /**
     * Get the current library version string.
     * @return version string "a.b.c"
     */
    public String GetVersion();

    /**
     * initialize API
     * start internal worker thread.
     * @param app application context
     * @return true -> success, false -> fail
     */
    public boolean On(Application app);

    /**
     * unitialize API
     * stop internal worker thread.
     * @return none
     */
    public void Off();

    /**
     * get list of lpu237 device
     * @return ArrayList<UsbDevHandle> these handle can be used for Open
     */
    public ArrayList<UsbDevHandle> GetList();

    /**
     * open lpu237
     * @param sPath lpu237 device path
     * @return UsbDevHandle
     */
    public UsbDevHandle Open(String sPath);

    /**
     * setup for reading msr or ibutton.(get minimum system parameters, and setting)
     * @param handle
     * @param bChangeInterfaceToHid true -> If current interface isn't usb hid vendor, change to usb hid vendor and apply. false -> No Thanks you
     * @return true -> success, false -> fail
     */
    public boolean SetupForRead(UsbDevHandle handle,boolean bChangeInterfaceToHid);

    /**
     * close lpu237
     * @param handle
     * @return true -> success, false -> fail
     */
    public boolean Close(UsbDevHandle handle);

    /**
     * get lpu237 device id
     * @param handle
     * @return String type of lpu237 device id
     */
    public String GetId(UsbDevHandle handle);

    /**
     * Enable/Disable execution of MSR reading callback when msr is ready
     * Before executing, need open.
     * @param handle
     * @param bEnable
     * @return true -> success, false -> fail
     */
    public boolean EnableMsr(UsbDevHandle handle,boolean bEnable);

    /**
     * Enable/Disable execution of iButton reading callback when iButton is ready
     * Before executing, need open.
     * @param handle
     * @param bEnable
     * @return 
     */
    public boolean EnableiButton(UsbDevHandle handle,boolean bEnable);

    /**
     * Cancel waiting for msr/iButton reading.
     * Before executing, need open.
     * @param handle
     * @return  true -> success, false -> fail
     */
    public boolean CancelWait(UsbDevHandle handle);

    /**
     * Waits for msr/iButton reading. when reading is ready, it will call the callback.
     * Before executing, need open.
     * @param handle
     * @param cb callback instance that will be called when reading is ready. this determines ibutton or msr.
     * @return true -> success, false -> fail
     */
    public boolean WaitMsrOriButtonWithCallback(UsbDevHandle handle, Lpu237Callback cb);

    /**
     * Starts that system parameters load from lpu237 device.
     * Async method by callback.
     * @param handle lpu237 device handle
     * @param cb callback instance that will be called when system parameter is loading repeatedly.
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrStartGetSetting(UsbDevHandle handle, Lpu237GetSetCallback cb);

    /**
     * Start that system parameter save to lpu237 device.
     * Async method by callback.
     * Warning: to apply changes, you must execute ToolsMsrApplySetting()
     * @param handle lpu237 device handle
     * @param cb callback instance that will be called when system parameter is saving repeatedly.
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrStartSetSetting(UsbDevHandle handle, Lpu237GetSetCallback cb);

    /**
     * Start that the changed system parameter apply to lpu237 device.
     * Async method by callback.
     * @param handle lpu237 device handle
     * @param cb callback instance that will be called when system parameter is applied.
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrStartApplySetting(UsbDevHandle handle, Lpu237DoneCallback cb);

    /**
     * Check if the device supports msr.
     * this function can use after ToolsMsrStartGetSetting() is Done.
     * @param handle lpu237 device handle
     * @return true -> support, false -> not
     */
    public boolean ToolsMsrIsSupportMsr(UsbDevHandle handle);

    /**
     * Check if the device supports ibutton.
     * this function can use after ToolsMsrStartGetSetting() is Done.
     * @param handle lpu237 device handle
     * @return true -> support, false -> not
     */
    public boolean ToolsMsrIsSupportIbutton(UsbDevHandle handle);

    /**
     * Get the active and valid interface from loaded lpu237 system parameters.
     * this function can use after ToolsMsrStartGetSetting() is Done.
     * @param handle lpu237 device handle
     * @return int[] the the first item is active interface, from the second item is valid interfaces.
     * 0 -> usb keyboard interface.
     * 1 -> usb hid vendor interface.
     * 2 -> usb virtual COM interface.
     * 10 -> real UART interface.
     */
    public int[] ToolsMsrGetActiveAndValiedInterface(UsbDevHandle handle);

    /**
     * Set the active interface to lpu237 device.
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting(). 
     * @param handle lpu237 device handle
     * @param Inf int type the active interface
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetInterface(UsbDevHandle handle, int Inf);

    /**
     * Start that Set the active interface and apply to lpu237 device.
     * @param handle
     * @param Inf int type the active interface
     * @param cb the callback that will be called when system parameter is applied.
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetInterfaceToDeviceAndApply(UsbDevHandle handle, int Inf, Lpu237DoneCallback cb);

    /**
     * Get the buzzer status from loaded lpu237 system parameters.
     * @param handle lpu237 device handle
     * @return 1 -> on, 0 -> off, negative -> error
     */
    public int ToolsMsrGetBuzzer(UsbDevHandle handle);

    /**
     * Set the buzzer status to lpu237 device.
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting(). 
     * @param handle lpu237 device handle
     * @param bOn true -> on,  false -> off
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetBuzzer(UsbDevHandle handle, boolean bOn);

    /**
     * Get the language index from loaded lpu237 system parameters.
     * @param handle lpu237 device handle
     * @return language index from 0 to 10, negative -> error
     * english = 0//USA
     * spanish = 1
     * danish = 2
     * french = 3
     * german = 4
     * italian = 5
     * norwegian = 6
     * swedish = 7
     * ukEnglish = 8
     * israel = 9
     * turkey = 10
     */
    public int ToolsMsrGetLanguage(UsbDevHandle handle);

    /**
     * Get the track status from loaded lpu237 system parameters.
     * @param handle
     * @param n_track_index from 0 to 2, 0 -> track1, 1 -> track2, 2 -> track3
     * @return 1 -> enable, 0 -> disable, negative -> error
     */
    public int ToolsMsrGetTrackStatus(UsbDevHandle handle,int n_track_index);

    /**
     * Set the track status to lpu237 device.
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting().
     * @param handle lpu237 device handle
     * @param n_track_index from 0 to 2, 0 -> track1, 1 -> track2, 2 -> track3
     * @param bEnable true -> enable,  false -> disable
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetTrackStatus(UsbDevHandle handle,int n_track_index,boolean bEnable);

    /**
     * Get the msr private tag from loaded lpu237 system parameters.
     * @param handle lpu237 device handle
     * @param n_track_index from 0 to 2, 0 -> track1, 1 -> track2, 2 -> track3
     * @param b_prefix true -> the returned tag is prefix, false -> postfix.
     * @return Lpu237Tags private tag object instance, null -> error
     */
    public Lpu237Tags ToolsMsrGetPrivateTag(UsbDevHandle handle,int n_track_index,boolean b_prefix);

    /**
     * Set the msr private tag to lpu237 device.
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting().
     * @param handle lpu237 device handle
     * @param n_track_index from 0 to 2, 0 -> track1, 1 -> track2, 2 -> track3
     * @param b_prefix true -> the returned tag is prefix, false -> postfix.
     * @param pTag Lpu237Tags private tag object instance
     * @return true -> success, false -> fail
     */
    public boolean  ToolsMsrSetPrivateTag(UsbDevHandle handle,int n_track_index,boolean b_prefix,Lpu237Tags pTag);

    /**
     * Get the iButton mode from loaded lpu237 system parameters.
     * @param handle lpu237 device handle
     * @return iButton mode.
     * Zeros = 0
     * Zeros7 = 1
     * F12 = 2
     * Addmit = 3
     * None = 4//pre.post position is defined by user.
     */
    public int ToolsMsrGetiButtonMode(UsbDevHandle handle);

    /**
     * Set the iButton mode to lpu237 device.
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting().
     * @param handle lpu237 device handle
     * @param n_mode iButton mode. 0 -> Zeros, 1 -> Zeros7, 2 -> F12, 3 -> Addmit, 4 -> None
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetiButtonMode(UsbDevHandle handle,int n_mode);

    /**
     * Get the iButton tag from loaded lpu237 system parameters.
     * @param handle lpu237 device handle
     * @param b_prefix true -> the returned tag is prefix, false -> postfix.
     * @return iButton tag object instance. null -> error
     */
    public Lpu237Tags ToolsMsrGetiButtonTag(UsbDevHandle handle,boolean b_prefix);

    /**
     * Set the iButton tag to lpu237 device.
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting().
     * @param handle lpu237 device handle
     * @param b_prefix true -> the returned tag is prefix, false -> postfix.
     * @param pTag iButton tag object instance
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetiButtonTag(UsbDevHandle handle,boolean b_prefix,Lpu237Tags pTag);

    /**
     * Get the iButton remove indication tag from loaded lpu237 system parameters.
     * @param handle lpu237 device handle
     * @return iButton remove indication tag object instance. null -> error
     */
    public Lpu237Tags ToolsMsrGetiButtonRemoveIndicationTag(UsbDevHandle handle);


    /**
     * Get the iButton remove-tag from loaded lpu237 system parameters.
     * the pre/postfix tag of "iButton remove indication tag".
     * @param handle lpu237 device handle
     * @param b_prefix true -> the returned tag is prefix, false -> postfix.
     * @return iButton tag object instance. null -> error
     */
    public Lpu237Tags ToolsMsrGetiButtonRTag(UsbDevHandle handle,boolean b_prefix);

    /**
     * Set the iButton remove-tag to lpu237 device.
     * the pre/postfix tag of "iButton remove indication tag".
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting().
     * @param handle lpu237 device handle
     * @param b_prefix true -> the returned tag is prefix, false -> postfix.
     * @param pTag iButton tag object instance
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetiButtonRTag(UsbDevHandle handle,boolean b_prefix,Lpu237Tags pTag);

    /**
     * Set the iButton remove indication tag to lpu237 device.
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting().
     * @param handle lpu237 device handle
     * @param pTag iButton remove indication tag object instance
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetiButtonRemoveIndicationTag(UsbDevHandle handle,Lpu237Tags pTag);


    /**
     * Get the iButton start range offset from loaded lpu237 system parameters.
     * @param handle lpu237 device handle
     * @return iButton start range offset(0~15), negative -> error
     */
    public int ToolsMsrGetiButtonStartZeroBaseOffsetOfRange(UsbDevHandle handle);

    /**
     * Get the iButton end range offset from loaded lpu237 system parameters.
     * @param handle lpu237 device handle
     * @return iButton end range offset(0~15), negative -> error
     */
    public int ToolsMsrGetiButtonEndZeroBaseOffsetOfRange(UsbDevHandle handle);

    /**
     * Set the iButton zero base range to lpu237 device.
     * the change will be applied after ToolsMsrStartSetSetting() and ToolsMsrStartApplySetting().
     * @param handle lpu237 device handle
     * @param n_start the iButton start range offset.(0~15)
     * @param n_end the iButton end range offset.(0~15), n_end can not be less then n_start.
     * @return true -> success, false -> fail
     */
    public boolean ToolsMsrSetiButtonZeroBaseRange(UsbDevHandle handle,int n_start,int n_end);

}//the end of interface

