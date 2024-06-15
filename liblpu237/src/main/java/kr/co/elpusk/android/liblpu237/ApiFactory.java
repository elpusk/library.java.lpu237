package kr.co.elpusk.android.liblpu237;

/**
 * This class is a factory pattern implementation for creating instances based on the version of the ApiInterface.<br>
 * Currently, only one version exists, so the getInstance() method only accepts the 'LAST' parameter.<br>
 * If you want to use API of liblpu237, you have to create the instance of ApiInterface by ApiFactory.getInstance().
 * @since 1.0.0
 */
public class ApiFactory {

    /**
     * the definition of version string.
     */
    public interface VERSION{
        String LAST = "LAST";//the recently version
    }

    /**
     *
     * @param sLibVersion - library version string. Now use only "LAST".
     * @return the instatnce of ApiInterface interface.<br>null(may be system error)
     */
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
