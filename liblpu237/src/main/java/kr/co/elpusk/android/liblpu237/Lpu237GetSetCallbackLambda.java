package kr.co.elpusk.android.liblpu237;

import kr.co.elpusk.android.liblpu237.lpu237.Lpu237GetSetCallback;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Callback;

public class Lpu237GetSetCallbackLambda extends Lpu237GetSetCallback {
    private Lpu237GetSetFunction function;

    public Lpu237GetSetCallbackLambda(boolean b_get, Lpu237GetSetFunction function) {
        super(b_get);
        this.function = function;
    }

    @Override
    public boolean Run(Lpu237Callback.Result result, String sDescription, int nCurZeroBaseStep, int nTotalStep) {
        return function.run(result, sDescription, nCurZeroBaseStep, nTotalStep);
    }
}
