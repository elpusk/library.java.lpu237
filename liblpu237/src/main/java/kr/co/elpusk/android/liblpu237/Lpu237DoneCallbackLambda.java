package kr.co.elpusk.android.liblpu237;

import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Callback;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237DoneCallback;

public class Lpu237DoneCallbackLambda extends Lpu237DoneCallback {
    private Lpu237DoneFunction function;

    public Lpu237DoneCallbackLambda(Lpu237DoneFunction function) {
        this.function = function;
    }

    @Override
    public void Run(Lpu237Callback.Result result, String sDescription) {
        if( function != null ) {
            function.run(result, sDescription);
        }
    }
}
