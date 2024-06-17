package kr.co.elpusk.android.liblpu237.lpu237;

import java.util.Arrays;

import kr.co.elpusk.android.liblpu237.util.Tools;

/**
 * pre/postfix key object class of lpu237
 */
public class Lpu237Tags{

    /**
     * When i-button is removed, the maximum number of keys transmitted to PC.<br>
     * i-button mode is user definition.<br>
     * usb keyboard interface.
     */
    public static final int NUMBER_IBUTTON_REMOVE_TAG=20;

    /**
     * the maximum number of pre/postfix i-button keys transmitted to PC.<br>
     * usb keyboard interface.
     */
    public static final int NUMBER_TAG=7;

    static final int SIZE_TAG=2;
    private byte[] m_s_tag;// = new byte[NUMBER_TAG*SIZE_TAG];
    private int m_n_offset = 0;

    /**
     * default constructor
     */
    public Lpu237Tags(){
        m_s_tag = new byte[NUMBER_TAG*SIZE_TAG];
    }

    /**
     * constructor with the maximum number of keys
     * @param n_tag the maximum number of keys
     */
    public Lpu237Tags(int n_tag){
        if(n_tag>0){
            m_s_tag = new byte[n_tag*SIZE_TAG];
        }
        else{
            m_s_tag = new byte[NUMBER_TAG*SIZE_TAG];
        }

    }

    /**
     * constructor with definition keys
     * @param tags definition keys, The size must be a multiple of 2.<br>
     *             One key consists of 2 bytes.
     */
    public Lpu237Tags( byte[] tags ){
        if( tags != null ){
            int n_length = m_s_tag.length;
            if( n_length > tags.length )
                n_length = tags.length;
            System.arraycopy(tags,0,m_s_tag,0,n_length );
            m_n_offset = n_length;
        }
    }

    /**
     * add the new key at the end.<br>
     * this key is USB HID Key type<br>
     * @see <a href="https://download.microsoft.com/download/1/6/1/161ba512-40e2-4cc9-843a-923143f3456c/translate.pdf">USB HID to PS/2 Scan Code Translation Table</a>
     * @param c_modifier Modifier masks, used for the first byte in the HID report.<br>
     *  0x01 - left control key.<br>
     *  0x02 - left shift key.<br>
     *  0x04 - left alt key.
     * @param c_key USB HID Keyboard scan codes as per USB spec 1.11(HID Usage ID value)<br>
     *
     */
    public void push_back(byte c_modifier, byte c_key){
        do{
            if( is_full() )
                continue;
            //
            m_s_tag[m_n_offset++] = c_modifier;
            m_s_tag[m_n_offset++] = c_key;

        }while(false);
    }

    /**
     * check whether or not key is empty.
     * @return true ->empty.<br>
     * false -> not empty.
     */
    public boolean is_empty(){
        if( m_n_offset <= 0 )
            return true;
        else
            return false;
    }

    /**
     * check whether or not key is full(maximum size).
     * @return true ->full.<br>
     * false -> not full.
     */
    public boolean is_full(){
        if( m_n_offset >= m_s_tag.length )
            return true;
        else
            return false;
    }

    /**
     * get the maximum number of keys that can be stored.
     * @return the maximum number of keys that can be stored.
     */
    public int max_size(){
        int n_size = 0;

        if(m_s_tag!=null){
            n_size = m_s_tag.length/SIZE_TAG;
        }
        return n_size;
    }

    /**
     * resize the maximum number of keys that can be stored.
     */
    public void resize(int n_tag){
        m_s_tag = null;//release memory
        if(n_tag>0){
            m_s_tag = new byte[n_tag*SIZE_TAG];
        }
        else{
            m_s_tag = new byte[NUMBER_TAG*SIZE_TAG];
        }
    }

    /**
     * get the number of keys that is stored.
     * @return the number of keys that is stored.
     */
    public int get_length(){
        return m_n_offset/2;
    }

    /**
     * remove all keys
     */
    public void clear(){
        Arrays.fill(m_s_tag,(byte)0 );
        m_n_offset = 0;
    }

    /**
     * copy new Lpu237Tags object to this.
     * @param tag new Lpu237Tags
     */
    public void set_tag( Lpu237Tags tag ){
        System.arraycopy(tag.m_s_tag,0,m_s_tag, 0, tag.m_s_tag.length);
        m_n_offset = tag.m_n_offset;
    }

    /**
     * copy new Lpu237Tags object to this.
     * @param tags key definition array. The size must be a multiple of 2.
     */
    public void set_tag( byte[] tags ){//tag isn't included length byte.
        clear();
        if( tags != null ){
            int n_length = m_s_tag.length;
            if( n_length > tags.length )
                n_length = tags.length;
            System.arraycopy(tags,0,m_s_tag,0,n_length );
            m_n_offset = n_length;
        }
    }

    /**
     * get internal all key array( including null key(0x00,0x00) )
     * @return byte[] type ,internal all key array
     */
    public byte[] get_tag() {
        return m_s_tag;
    }

    /**
     * copy this data to new Lpu237Tags object.
     * @param dst new Lpu237Tags object.
     */
    public void copy_to( Lpu237Tags dst ){
        do{
            if( dst == null )
                continue;
            //
            dst.m_n_offset = this.m_n_offset;
            System.arraycopy(this.m_s_tag,0,dst.m_s_tag,0,this.m_s_tag.length);
        }while (false);
    }

    /**
     * get byte array that all tag key( including null key(0x00,0x00) ).<br>
     * the first byte is the length of saved key(unit is byte. NOT key. excluding null key(0x00,0x00) )
     * @return  the first byte is the length of saved key and all tag key.
     */
    public byte[] get_tag_stream_with_length(){
        byte[] m_s_stream = new byte[1+m_s_tag.length];
        System.arraycopy(m_s_tag,0,m_s_stream,1,m_s_tag.length);
        m_s_stream[0] = (byte)m_n_offset;
        return m_s_stream;
    }

    /**
     * Check if all key values are the same.
     * @param tag Lpu237Tags
     * @return true -> same.<br>false -> not same.
     */
    public boolean equal( Lpu237Tags tag ){
        boolean b_equal = false;

        do{
            if( tag == null )
                continue;

            if( m_s_tag.length != tag.m_s_tag.length){
                continue;
            }
            b_equal = true;
            for( int i = 0; i<m_s_tag.length; i++ ){
                if( m_s_tag[i] != tag.m_s_tag[i]){
                    b_equal = false;
                    break;//exit for
                }
            }//end for

        }while(false);
        return b_equal;
    }

    /**
     * get key value in hex string format.
     * @return  key value in hex string format.
     */
    @Override
    public String toString() {
        return Tools.byteArrayToHex(m_s_tag);
    }

}