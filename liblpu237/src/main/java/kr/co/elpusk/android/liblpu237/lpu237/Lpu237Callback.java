package kr.co.elpusk.android.liblpu237.lpu237;

abstract public class Lpu237Callback {
    public enum TypeRx{
        RX_RAW,//unknown
        RX_MSR,
        RX_IBUTTON
    }

    public enum Result{
        RESULT_SUCCESS,
        RESULT_ERROR,
        RESULT_CANCEL
    }

    private TypeRx m_type = TypeRx.RX_MSR;

    public Lpu237Callback(TypeRx t){
        m_type = t;
    }

    public TypeRx GetType(){
        return m_type;
    }
    private Lpu237Callback(){

    }
    abstract public void Run(Result read_result,byte[] rx);
}
