package kr.co.elpusk.android.liblpu237.lpu237;

/**
 * this class is the abstract callback class of ToolsMsrStartApplySetting() or ToolsMsrSetInterfaceToDeviceAndApply() in ApiInterface interface.
 */
abstract public class Lpu237DoneCallback {

    /**
     * To use ToolsMsrStartApplySetting() or ToolsMsrSetInterfaceToDeviceAndApply(), <br>
     * you need to extend this class and implement the Run() method.<br>
     * The Run() method will be called when processing is done.<br>
     * If the result is not Lpu237Callback.RESULT_SUCCESS, <br>
     * it indicates an error has occurred or the operation was canceled by the user.<br>
     * In such cases, the task can be retried or the API usage can be terminated.<br>
     * because this method is called API internal thread, always be careful to avoid deadlock.
     * @param result the value of result can be <br>
     *               Lpu237Callback.RESULT_SUCCESS, Lpu237Callback.RESULT_ERROR, or Lpu237Callback.RESULT_CANCEL. <br>
     *               It indicates the execution result of processing.
     * @param sDescription description string of processing.
     */
    abstract public void Run(Lpu237Callback.Result result, String sDescription);
}
