package kr.co.elpusk.android.liblpu237;

public class ApiFactory {
    public interface VERSION{
        String LAST = "LAST";
    }
    public static ApiInterface getInstance(String sLibVersion){
        if(sLibVersion.compareTo(VERSION.LAST)==0){
            return ApiLpu237.getInstance();
        }
        else{
            return null;
        }
    }

    private ApiFactory(){
        //disable create this instance.
    }
}
