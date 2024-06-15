package kr.co.elpusk.android.liblpu237.lpu237;

/**
 * this class is the abstract callback class of ToolsMsrStartGetSetting() and ToolsMsrStartSetSetting() in ApiInterface interface.
 */
abstract public class Lpu237GetSetCallback {
    /**
     * if m_b_get is true, this callback instance will be used for getting,
     * else setting.
     */
    protected boolean m_b_get = true;

    /**
     * To use ToolsMsrStartGetSetting() or ToolsMsrStartSetSetting(), <br>
     * you need to extend this class and implement the Run() method.<br>
     * The Run() method will be repeatedly called until the reading and <br>
     * writing of system parameters from the lpu237 are completed.<br>
     * The user should program to wait until the initiated task is completely finished<br>
     * when result is Lpu237Callback.RESULT_SUCCESS.<br>
     * If the result is not Lpu237Callback.RESULT_SUCCESS, <br>
     * it indicates an error has occurred or the operation was canceled by the user.<br>
     * In such cases, the task can be retried or the API usage can be terminated.<br>
     * because this method is called API internal thread, always be careful to avoid deadlock.
     * @param result the value of result can be <br>
     *               Lpu237Callback.RESULT_SUCCESS, Lpu237Callback.RESULT_ERROR, or Lpu237Callback.RESULT_CANCEL. <br>
     *               It indicates the execution result of the step represented by nCurZeroBaseStep.
     * @param sDescription description string of nCurZeroBaseStep.
     * @param nCurZeroBaseStep represents the current step of the operation and can have a value of 0 or greater and less than nTotalStep. <br>
     *                         If this value becomes nTotalStep - 1, it indicates that all read or write operations are complete.
     * @param nTotalStep represents the total number of steps initiated by ToolsMsrStartGetSetting() or ToolsMsrStartSetSetting().
     * @return you must return always true.
     */
    abstract public boolean Run(Lpu237Callback.Result result, String sDescription, int nCurZeroBaseStep, int nTotalStep );

    /**
     * Constructor
     * @param b_get true -> this instance will be used in ToolsMsrStartGetSetting().<br>
     *              false -> this instance will be used in ToolsMsrStartSetSetting().<br>
     */
    public Lpu237GetSetCallback(boolean b_get){
        m_b_get = b_get;
    }

    /**
     * get the type of callback.
     * @return true -> For ToolsMsrStartGetSetting().<br>
     * false -> For ToolsMsrStartSetSetting().
     */
    public boolean is_getting_type(){
        return m_b_get;
    }

}
