package kr.co.elpusk.android.liblpu237;

import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Callback;

@FunctionalInterface
public interface Lpu237DoneFunction {
    void run(Lpu237Callback.Result result, String sDescription);
}
