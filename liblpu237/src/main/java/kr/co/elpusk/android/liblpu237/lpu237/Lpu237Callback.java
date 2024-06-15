package kr.co.elpusk.android.liblpu237.lpu237;

/**
 * this class is the abstract callback class of WaitMsrOriButtonWithCallback()  in ApiInterface interface.
 */
abstract public class Lpu237Callback {

    /**
     * the reading type of the current Lpu237Callback instance.
     */
    public enum TypeRx{
        RX_RAW,//unknown
        RX_MSR,//Magnetic card is reading.
        RX_IBUTTON //iButton card is reading.
    }

    /**
     * the result value of processing in Lpu237Callback, Lpu237GetSetCallback or Lpu237DoneCallback.
     */
    public enum Result{
        RESULT_SUCCESS,//processing success.
        RESULT_ERROR,//processing have a problem.
        RESULT_CANCEL//processing is cancelled.
    }

    private TypeRx m_type = TypeRx.RX_MSR;

    /**
     * Constructor
     * @param t TypeRx.RX_MSR -> MSR reading type.<br>
     *          TypeRx.RX_IBUTTON -> iButton reading type.
     */
    public Lpu237Callback(TypeRx t){
        m_type = t;
    }

    /**
     * get the type of this instance.
     * @return TypeRx.RX_MSR -> MSR reading type.<br>
     * TypeRx.RX_IBUTTON -> iButton reading type.
     */
    public TypeRx GetType(){
        return m_type;
    }
    private Lpu237Callback(){

    }

    /**
     * To use WaitMsrOriButtonWithCallback(), you need to extend this class and implement the Run() method.<br>
     * The Run() method will be called when the reading is done.(with success ,error or cancel)<br>
     * If the result is not Lpu237Callback.RESULT_SUCCESS, <br>
     * it indicates an error has occurred or the operation was canceled by the user.<br>
     * In such cases, the task can be retried or the API usage can be terminated.<br>
     * because this method is called API internal thread, always be careful to avoid deadlock.
     * @param read_result the value of result can be <br>
     *               Lpu237Callback.RESULT_SUCCESS, Lpu237Callback.RESULT_ERROR, or Lpu237Callback.RESULT_CANCEL. <br>
     *               It indicates the execution result of the reading.
     * @param rx byte[] type, the received data from lpu237.
     */
    abstract public void Run(Result read_result,byte[] rx);
}
